package loop.view.historylistview.templates;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import loop.controller.ResultHistoryItem;
import loop.model.simulator.SimulationResult;

import java.io.IOException;

/**
 * This class manges the SimulationResultCellTemplate for canceled Simulations
 *
 * @author Christian Schorr
 */
public class CanceledSimulationResultCellTemplate extends SimulationResultCellTemplate  {

    private static final String FXML_NAME = "/view/listViewTemplates/canceledSimulationCell.fxml";
    private ResultHistoryItem item;

    public CanceledSimulationResultCellTemplate(ResultHistoryItem item) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_NAME));
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fxmlLoader.setController(this);
        this.item = item;
    }

    public void initialize() {
        super.initialize(item);
    }
}
