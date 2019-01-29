package loop.view.controls.multislider.skin;

/*
 * Copyright (c) 2013, 2016 ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.sun.javafx.scene.control.skin.BehaviorSkinBase;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.converter.FormatStringConverter;
import javafx.util.converter.NumberStringConverter;
import loop.view.controls.multislider.MultiSlider;
import loop.view.controls.multislider.Range;
import loop.view.controls.multislider.behavior.MultiSliderBehavior;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by alberto on 09/01/2017.
 */
public class MultiSliderSkin extends BehaviorSkinBase<MultiSlider, MultiSliderBehavior> {

    /**
     * Track if slider is vertical/horizontal and cause re layout
     */
    private NumberAxis tickLine = null;
    private double trackToTickGap = 2;

    private boolean showTickMarks;
    private double thumbWidth;
    private double thumbHeight;

    private Orientation orientation;

    private StackPane track;
    private double trackStart;
    private double trackLength;
    private double lowThumbPos;

    private List<ThumbRange> thumbs;

    // temp fields for mouse drag handling
    private double preDragPos;          // used as a temp value for low and high thumbsRange
    private Point2D preDragThumbPoint;  // in skin coordinates

    private IntegerProperty currentId = new SimpleIntegerProperty(0);
    private int id = 0;

    private BooleanProperty isRanged = new SimpleBooleanProperty(false);

    /**
     * Constructor for all BehaviorSkinBase instances.
     *
     * @param control  The control for which this Skin should attach to.
     * @param behavior The behavior for which this Skin should defer to.
     */
    public MultiSliderSkin(final MultiSlider control, final MultiSliderBehavior behavior) {
        super(control, behavior);
        ThumbRange.styleId = 0;

        orientation = getSkinnable().getOrientation();
        isRanged.bindBidirectional(control.getIsRanged());

        initTrack();
        initInitialThumbs();

        registerChangeListener(control.orientationProperty(), "ORIENTATION");
        registerChangeListener(control.showTickMarksProperty(), "SHOW_TICK_MARKS");
        registerChangeListener(control.showTickLabelsProperty(), "SHOW_TICK_LABELS");
        registerChangeListener(control.majorTickUnitProperty(), "MAJOR_TICK_UNIT");
        registerChangeListener(control.minorTickCountProperty(), "MINOR_TICK_COUNT");
        registerChangeListener(control.minProperty(), "MIN");
        registerChangeListener(control.maxProperty(), "MAX");

        getSkinnable().currentRangeIdProperty().bindBidirectional(currentId);
    }

    public void addRange(Double min, Double max, Consumer<Integer> handler) {
        int i = getNextId();
        currentId.setValue(i);

        Range range = getSkinnable().getRange(id);
        getSkinnable().updateRange(range);
        getSkinnable().createNewRange(min, max);
        initThumbs(new ThumbRange(i, isRanged.get(), getSkinnable().getRange(i), handler));
        thumbs.get(0).rangeBar.setOnMouseClicked((e) -> {
            handler.accept(0);
        });
    }

    public void deleteRange() {
        ThumbRange currentTr = getCurrentThumb();
        if (currentTr != null) {
            getChildren().remove(currentTr.high);
            getChildren().remove(currentTr.low);
            getChildren().remove(currentTr.rangeBar);
            thumbs.remove(currentTr);
        }
    }

    public void clear() {
        id = 0;
        initTrack();
        initInitialThumbs();
    }

    /**
     * Set up the initial thumbs. There has to be always at least two thumbs on the slider.
     */
    private void initInitialThumbs() {
        thumbs = new ArrayList<>();
        ThumbRange initialThumbs = new ThumbRange(getNextId(), isRanged.get(), getSkinnable().getRange(0));
        initThumbs(initialThumbs);
        setShowTickMarks(getSkinnable().isShowTickMarks(), getSkinnable().isShowTickLabels());
    }

    public void removeLast() {
        ThumbRange thumb = thumbs.get(thumbs.size() - 1);
        ThumbRange.styleId--;

        getChildren().remove(thumb.low);
        getChildren().remove(thumb.high);
        getChildren().remove(thumb.rangeBar);
        thumbs.remove(thumb);
        id--;

        for (Range r : getSkinnable().getRanges()) {
            if (r.getId() == thumb.id) {
                getSkinnable().getRanges().remove(r);
                return;
            }
        }
    }

    /**
     * Init the given thumbs and add them to the view.
     *
     * @param t thumbRange
     */
    private void initThumbs(ThumbRange t) {
        thumbs.add(t);

        getChildren().addAll(t.low, t.high, t.rangeBar);

        if (isRanged.get()) {
            t.low.setOnMousePressed(me -> {
                currentId.setValue(t.id);
                preDragThumbPoint = t.low.localToParent(me.getX(), me.getY());
                preDragPos = (getSkinnable().getLowValue() - getSkinnable().getMin()) / (getMaxMinusMinNoZero());
            });


            t.low.setOnMouseDragged(me -> {
                Point2D cur = t.low.localToParent(me.getX(), me.getY());
                double dragPos = (isHorizontal()) ? cur.getX() - preDragThumbPoint.getX() : -(cur.getY() - preDragThumbPoint.getY());
                getBehavior().lowThumbDragged(preDragPos + dragPos / trackLength);
            });
        }

        t.high.setOnMousePressed(me -> {
            currentId.setValue(t.id);
            preDragThumbPoint = t.high.localToParent(me.getX(), me.getY());
            preDragPos = (getSkinnable().getHighValue() - getSkinnable().getMin()) / (getMaxMinusMinNoZero());
        });


        t.high.setOnMouseDragged(me -> {
            boolean orientation = getSkinnable().getOrientation() == Orientation.HORIZONTAL;
            double trackLength = orientation ? track.getWidth() : track.getHeight();
            Point2D cur = t.high.localToParent(me.getX(), me.getY());
            double dragPos = getSkinnable().getOrientation() != Orientation.HORIZONTAL ? -(cur.getY() - preDragThumbPoint.getY()) : cur.getX() - preDragThumbPoint.getX();
            getBehavior().highThumbDragged(preDragPos + dragPos / trackLength);

        });

        /*
         * On primary button click add a new range to the slider.
         * On secondary button click delete the range.
         */
        if (isRanged.get()) {
            t.rangeBar.setOnMousePressed(me -> {
                if (me.isPrimaryButtonDown()) {
                    int i = getNextId();
                    currentId.setValue(i);

                    boolean created;

                    if (isHorizontal()) {
                        created = getBehavior().rangeBarPressed((me.getX() / trackLength), t.id);
                    } else {
                        created = getBehavior().rangeBarPressed((me.getY() / trackLength), t.id);
                    }

                    if (created) {
                        initThumbs(new ThumbRange(i, isRanged.get(), getSkinnable().getRange(i)));
                    }
                } else {
                    currentId.setValue(t.id);
                    boolean isDeleted = getBehavior().rangeBarPressedSecondary();
                    if (isDeleted) {
                        ThumbRange currentTr = getCurrentThumb();
                        if (currentTr != null) {
                            getChildren().remove(currentTr.high);
                            getChildren().remove(currentTr.low);
                            getChildren().remove(currentTr.rangeBar);
                            thumbs.remove(currentTr);
                        }
                    }
                }
            });
        } else {
            t.rangeBar.setOnMouseClicked((e) -> {
                t.handler.accept(t.id);
            });
        }
    }

    /**
     * Initialize the slider track.
     * This must be executed before adding all the other components.
     */
    private void initTrack() {
        track = new StackPane();
        track.getStyleClass().setAll("track");

        getChildren().clear();
        getChildren().add(track);

        /*
         * Try to add a new range on mouse click
         */
        if (isRanged.get()) {
            track.setOnMousePressed(me -> {
                int index = getNextId();
                currentId.setValue(index);

                boolean isCrated;
                if (isHorizontal()) {
                    isCrated = getBehavior().trackPress((me.getX() / trackLength));
                } else {
                    isCrated = getBehavior().trackPress((me.getY() / trackLength));
                }

                if (isCrated) {
                    initThumbs(new ThumbRange(index, isRanged.get(), getSkinnable().getRange(index)));
                }
            });
        }
    }

    /**
     * Reposition all thumbs and range bars of the view
     */
    private void positionAllThumbs() {
        MultiSlider s = getSkinnable();
        int prevVal = currentId.get();

        for (ThumbRange thumb : thumbs) {
            currentId.setValue(thumb.id);

            double lxl = trackStart + (trackLength * ((s.getLowValue() - s.getMin()) / (getMaxMinusMinNoZero())) - thumbWidth / 2D);
            double lxh = trackStart + (trackLength * ((s.getHighValue() - s.getMin()) / (getMaxMinusMinNoZero())) - thumbWidth / 2D);
            double ly = lowThumbPos;

            ThumbRange thumbRange = getCurrentThumb();

            if (thumbRange != null) {
                thumbRange.low.setLayoutX(lxl);
                thumbRange.low.setLayoutY(ly);

                thumbRange.high.setLayoutX(lxh);
                thumbRange.high.setLayoutY(ly);

                double lowOffset = thumbRange.low.getWidth() / 2;
                if (thumbRange.id == 0) lowOffset = 0;
                thumbRange.rangeBar.resizeRelocate(thumbRange.low.getLayoutX() + lowOffset, track.getLayoutY(),
                        thumbRange.high.getLayoutX() - thumbRange.low.getLayoutX() - lowOffset, track.getHeight());
            }
        }

        for (ThumbRange thumb : thumbs) {
            thumb.high.toFront();
        }

        currentId.setValue(prevVal);
    }


    @Override
    protected void layoutChildren(final double x, final double y, final double w, final double h) {

        ThumbRange thumbRange = getCurrentThumb();

        if (thumbRange != null) {
            thumbWidth = thumbRange.low.prefWidth(-1);
            thumbHeight = thumbRange.low.prefHeight(-1);
            thumbRange.low.resize(thumbWidth, thumbHeight);
            thumbRange.high.resize(thumbWidth, thumbHeight);
        }

        // we are assuming the is common radius's for all corners on the track
        double trackRadius = track.getBackground() == null ? 0 : track.getBackground().getFills().size() > 0 ?
                track.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius() : 0;

        double tickLineHeight = (showTickMarks) ? tickLine.prefHeight(-1) : 0;
        double trackHeight = track.prefHeight(-1);
        double trackAreaHeight = Math.max(trackHeight, thumbHeight);
        double totalHeightNeeded = trackAreaHeight + ((showTickMarks) ? trackToTickGap + tickLineHeight : 0);
        double startY = y + ((h - totalHeightNeeded) / 2); // center slider in available height vertically

        trackLength = w - thumbWidth;
        trackStart = x + (thumbWidth / 2);

        double trackTop = (int) (startY + ((trackAreaHeight - trackHeight) / 2));
        lowThumbPos = (int) (startY + ((trackAreaHeight - thumbHeight) / 2));

        // layout track
        track.resizeRelocate(trackStart - trackRadius, trackTop, trackLength + trackRadius + trackRadius, trackHeight);

        positionAllThumbs();

        if (showTickMarks) {
            tickLine.setLayoutX(trackStart);
            tickLine.setLayoutY(trackTop + trackHeight + trackToTickGap);
            tickLine.resize(trackLength, tickLineHeight);
            tickLine.requestAxisLayout();
        } else {
            if (tickLine != null) {
                tickLine.resize(0, 0);
                tickLine.requestAxisLayout();
            }
            tickLine = null;
        }
    }

    @Override
    protected void handleControlPropertyChanged(String p) {
        super.handleControlPropertyChanged(p);
        if ("ORIENTATION".equals(p)) { //$NON-NLS-1$
            orientation = getSkinnable().getOrientation();
            if (showTickMarks && tickLine != null) {
                tickLine.setSide(isHorizontal() ? Side.BOTTOM : Side.RIGHT);
            }
            getSkinnable().requestLayout();
        } else if ("MIN".equals(p)) { //$NON-NLS-1$
            if (showTickMarks && tickLine != null) {
                tickLine.setLowerBound(getSkinnable().getMin());
            }
            getSkinnable().requestLayout();
        } else if ("MAX".equals(p)) { //$NON-NLS-1$
            if (showTickMarks && tickLine != null) {
                tickLine.setUpperBound(getSkinnable().getMax());
            }
            getSkinnable().requestLayout();
        } else if ("SHOW_TICK_MARKS".equals(p) || "SHOW_TICK_LABELS".equals(p)) { //$NON-NLS-1$ //$NON-NLS-2$
            setShowTickMarks(getSkinnable().isShowTickMarks(), getSkinnable().isShowTickLabels());
        } else if ("MAJOR_TICK_UNIT".equals(p)) { //$NON-NLS-1$
            if (tickLine != null) {
                tickLine.setTickUnit(getSkinnable().getMajorTickUnit());
                getSkinnable().requestLayout();
            }
        } else if ("MINOR_TICK_COUNT".equals(p)) { //$NON-NLS-1$
            if (tickLine != null) {
                tickLine.setMinorTickCount(Math.max(getSkinnable().getMinorTickCount(), 0) + 1);
                getSkinnable().requestLayout();
            }
        }
        super.handleControlPropertyChanged(p);
    }

    /**
     * Each time a new id is requested the count is updated to +1.
     *
     * @return the value of the next id
     */
    private int getNextId() {
        return id++;
    }

    /**
     * Get the current ThumbRange.
     *
     * @return the current thumbRange
     */
    private ThumbRange getCurrentThumb() {
        for (ThumbRange thumbRange : thumbs) {
            if (thumbRange.id == currentId.get()) {
                return thumbRange;
            }
        }
        return null;
    }

    /**
     * @return the difference between max and min, but if they have the same
     * value, 1 is returned instead of 0 because otherwise the division where it
     * can be used will return Nan.
     */
    private double getMaxMinusMinNoZero() {
        MultiSlider s = getSkinnable();
        return s.getMax() - s.getMin() == 0 ? 1 : s.getMax() - s.getMin();
    }

    private double minTrackLength() {
        return 2 * ((getCurrentThumb() != null) ? getCurrentThumb().low.prefWidth(-1) : 1);
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (isHorizontal()) {
            return (leftInset + minTrackLength() + ((getCurrentThumb() != null) ? getCurrentThumb().low.prefWidth(-1) : 1) + rightInset);
        } else {
            return (leftInset + ((getCurrentThumb() != null) ? getCurrentThumb().low.prefWidth(-1) : 1) + rightInset);
        }
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (isHorizontal()) {
            return (topInset + ((getCurrentThumb() != null) ? getCurrentThumb().low.prefHeight(-1) : 1) + bottomInset);
        } else {
            return (topInset + minTrackLength() + ((getCurrentThumb() != null) ? getCurrentThumb().low.prefHeight(-1) : 1) + bottomInset);
        }
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (isHorizontal()) {
            if (showTickMarks) {
                return Math.max(140, tickLine.prefWidth(-1));
            } else {
                return 140;
            }
        } else {
            return leftInset + Math.max(((getCurrentThumb() != null) ? getCurrentThumb().low.prefWidth(-1) : 1), track.prefWidth(-1)) +
                    ((showTickMarks) ? (trackToTickGap + tickLine.prefWidth(-1)) : 0) + rightInset;
        }
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (isHorizontal()) {
            return getSkinnable().getInsets().getTop() + Math.max(((getCurrentThumb() != null) ? getCurrentThumb().low.prefHeight(-1) : 1), track.prefHeight(-1)) +
                    ((showTickMarks) ? (trackToTickGap + tickLine.prefHeight(-1)) : 0) + bottomInset;
        } else {
            if (showTickMarks) {
                return Math.max(140, tickLine.prefHeight(-1));
            } else {
                return 140;
            }
        }
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (isHorizontal()) {
            return Double.MAX_VALUE;
        } else {
            return getSkinnable().prefWidth(-1);
        }
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        if (isHorizontal()) {
            return getSkinnable().prefHeight(width);
        } else {
            return Double.MAX_VALUE;
        }
    }

    private boolean isHorizontal() {
        return getSkinnable().getOrientation() == Orientation.HORIZONTAL;
    }

    /**
     * When ticks or labels are changing of visibility, we compute the new
     * visibility and add the necessary objects. After this method, we must be
     * sure to add the high Thumb and the rangeBar.
     *
     * @param ticksVisible
     * @param labelsVisible
     */
    private void setShowTickMarks(boolean ticksVisible, boolean labelsVisible) {
        showTickMarks = (ticksVisible || labelsVisible);
        MultiSlider MultiSlider = getSkinnable();
        if (showTickMarks) {
            if (tickLine == null) {
                tickLine = new NumberAxis();
                tickLine.tickLabelFormatterProperty().bind(getSkinnable().labelFormatterProperty());
                tickLine.setAnimated(false);
                tickLine.setAutoRanging(false);
                tickLine.setSide(isHorizontal() ? Side.BOTTOM : Side.RIGHT);
                tickLine.setUpperBound(MultiSlider.getMax());
                tickLine.setLowerBound(MultiSlider.getMin());
                tickLine.setTickUnit(MultiSlider.getMajorTickUnit());
                tickLine.setTickMarkVisible(ticksVisible);
                tickLine.setTickLabelsVisible(labelsVisible);
                tickLine.setMinorTickVisible(ticksVisible);
                // add 1 to the slider minor tick count since the axis draws one
                // less minor ticks than the number given.
                tickLine.setMinorTickCount(Math.max(MultiSlider.getMinorTickCount(), 0) + 1);
                getChildren().addAll(tickLine);
            } else {
                tickLine.setTickLabelsVisible(labelsVisible);
                tickLine.setTickMarkVisible(ticksVisible);
                tickLine.setMinorTickVisible(ticksVisible);
            }
        }

        getSkinnable().requestLayout();
    }

    private static class ThumbPane extends StackPane {
        void setFocus(boolean value) {
            setFocused(value);
        }
    }

    private static class ThumbRange {
        int id;
        ThumbPane low;
        ThumbPane high;
        StackPane rangeBar;

        static int styleId = 0;

        FadeTransition fadeIn = new FadeTransition(Duration.millis(750));
        FadeTransition fadeOut = new FadeTransition(Duration.millis(750));

        Consumer<Integer> handler;

        ThumbRange(int id, boolean isRanged, Range range, Consumer<Integer> handler) {
            this.id = id;
            low = new ThumbPane();
            low.getStyleClass().setAll("low-thumb");
            low.setFocusTraversable(false);
            if (!isRanged) {
                low.setVisible(false);
                low.setDisable(true);
            }

            high = new ThumbPane();
            high.getStyleClass().setAll("high-thumb");
            high.setFocusTraversable(false);

            Label label = new Label();

            fadeIn.setNode(label);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeOut.setNode(label);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);


            DoubleProperty prop = new SimpleDoubleProperty();
            prop.bind(range.highProperty().subtract(range.lowProperty()));
            range.highProperty().addListener((n) -> {
                label.setOpacity(1);
                fadeOut.playFromStart();
            });

            label.setOpacity(0);
            label.textProperty().bindBidirectional(range.highProperty(), new NumberStringConverter(NumberFormat.getIntegerInstance()));
            label.getStyleClass().setAll(String.format("text%d", (styleId % 4)));

            VBox box = new VBox();
            box.setStyle("-fx-padding: 40 0 0 0");
            box.getChildren().add(label);
            box.setAlignment(Pos.CENTER);
            box.setMinWidth(50);
            box.setMinHeight(50);

            high.getChildren().add(box);
            high.setOnMouseEntered((e) -> {
                fadeOut.stop();
                fadeIn.play();
            });
            high.setOnMouseExited((e) -> {
                fadeIn.stop();
                fadeOut.play();
            });

            rangeBar = new StackPane();
            rangeBar.getStyleClass().setAll(String.format("range-bar%d", (styleId % 4)));
            rangeBar.setFocusTraversable(false);

            this.handler = handler;

            styleId++;
        }

        ThumbRange(int id, boolean isRanged, Range range) {
            this (id, isRanged, range, (i) -> {});
        }
    }

}
