package loop.view.historylistview;

import com.sun.javafx.font.Glyph;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import loop.model.simulator.SimulationResult;

/**
 * @author Christian Schorr
 *
 */
public class SimulationResultCell extends ListCell<SimulationResult> {

    @Override
    protected void updateItem(SimulationResult item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        setGraphic(null);

        if (item != null && !empty) {

        }
    }
}

/*
<HBox styleClass="task-card-green">
<Rectangle fill="#008A00" height="100" width="10"/>
<VBox spacing="1" style="-fx-padding: 10 0 0 10">
<HBox alignment="CENTER" spacing="10">
<Glyph fontFamily="FontAwesome" fontSize="30" icon="CHECK" style="-fx-text-fill: #008A00"/>
<Label style="-fx-text-fill: #008A00" styleClass="card-subtitle" text="Gefangenendilemma"/>
<Label style="-fx-text-fill: #008A00" styleClass="card-subtitle" text="#001"/>
</HBox>
<HBox alignment="CENTER_LEFT" spacing="10">
<Glyph fontFamily="FontAwesome" fontSize="20" icon="CLOCK_ALT"/>
<Label text="Laufzeit: "/>
<Label text="33 min"/>
</HBox>
<HBox alignment="CENTER_LEFT" spacing="10">
<Glyph fontFamily="FontAwesome" fontSize="20" icon="CALENDAR"/>
<Label text="Beendet:"/>
<Label text="vor 2h"/>
</HBox>
</VBox>
</HBox>
*/