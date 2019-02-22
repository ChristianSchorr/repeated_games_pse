package loop.view.historylistview.templates;

import javafx.fxml.FXMLLoader;
import loop.model.simulator.SimulationResult;

import java.io.IOException;

/**
 * This class manges the SimulationResultCellTemplate for canceled Simulations
 *
 * @author Christian Schorr
 */
public class CanceledSimulationResultCellTemplate extends SimulationResultCellTemplate  {

    private static final String FXML_NAME = "/view/listViewTemplates/canceledSimulationCell.fxml";
    private SimulationResult item;


    public CanceledSimulationResultCellTemplate(SimulationResult item) {
        this.item = item;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        super.initialize(item);
    }
}
