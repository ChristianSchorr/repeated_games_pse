package loop.preloader;

import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;
import java.net.URISyntaxException;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreenLoader extends Preloader {
    
    private static final String GIFPath = "/SplashVersion2.gif";
    public static final int animationMillis = 4500;
    private Stage splashScreen;

    @Override
    public void start(Stage stage) throws Exception {
        splashScreen = stage;
        splashScreen.initStyle(StageStyle.UNDECORATED);
        
        //add gif
        VBox box = new VBox();
        Animation ani = new AnimatedGif(getClass().getResource(GIFPath).toExternalForm(), animationMillis);
        ani.setCycleCount(1);
        ani.play();
        box.getChildren().add(ani.getView());
        
        splashScreen.setScene(new Scene(box));
        splashScreen.show();
    }

    /*@Override
    public void handleApplicationNotification(PreloaderNotification notification) {
        if (notification instanceof StateChangeNotification) {
            splashScreen.hide();
        }
    }*/
    
    @Override
    public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
       if (stateChangeNotification.getType() == Type.BEFORE_START) {
          splashScreen.hide();
       }
    }
    
    public class AnimatedGif extends Animation {

        public AnimatedGif( String filename, double durationMs) {

            GifDecoder d = new GifDecoder();
            d.read( filename);
            
            Image[] sequence = new Image[ d.getFrameCount()];
            for( int i=0; i < d.getFrameCount(); i++) {

                WritableImage wimg = null;
                BufferedImage bimg = d.getFrame(i);
                sequence[i] = SwingFXUtils.toFXImage( bimg, wimg);

            }

            super.init( sequence, durationMs);
        }

    }

    public class Animation extends Transition {

        private ImageView imageView;
        private int count;

        private int lastIndex;

        private Image[] sequence;
        
        private boolean stop = false;
        
        private Animation() {
        }

        public Animation( Image[] sequence, double durationMs) {
            init( sequence, durationMs);
        }

        private void init( Image[] sequence, double durationMs) {
            this.imageView = new ImageView(sequence[0]);
            this.sequence = sequence;
            this.count = sequence.length;

            setCycleCount(1);
            setCycleDuration(Duration.millis(durationMs));
            setInterpolator(Interpolator.LINEAR);

        }

        protected void interpolate(double k) {
            if (stop) return;
            
            final int index = Math.min((int) Math.floor(k * count), count - 1);
            
            if (index >= count - 10) {
                stop = true;
                imageView.setImage(sequence[count - 1]);
            }
            
            if (index != lastIndex) {
                imageView.setImage(sequence[index]);
                lastIndex = index;
            }

        }

        public ImageView getView() {
            return imageView;
        }

    }
    
}
