package loop;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import loop.model.repository.FileIO;

import java.lang.reflect.Field;
import java.net.URL;

/**
 * This class contains the main method of the program and initialises the main window.
 *
 * @author Peter Koepernik
 */
public class Main extends Application {
    
    public static final String RING_LOGO_PATH = "file:src/main/resources/loop_ring.png";
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/windows/HomeWindow.fxml"));
        Scene scene = new Scene(root, 1920, 1080);

        stage.setTitle("loop");
        stage.setScene(scene);
        stage.getIcons().add(new Image(RING_LOGO_PATH));
        stage.show();
    }

    /**
     * The entry point of the application. Loads the UI and opens the main window.
     *
     * @param args the commandline arguments (unused)
     */
    public static void main(String[] args) {
        hackTooltip();
        launch(args);
    }

    private static void hackTooltip() {
        Tooltip tooltip = new Tooltip();
        try {
            Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
            fieldBehavior.setAccessible(true);
            Object objBehavior = fieldBehavior.get(tooltip);

            Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
            fieldTimer.setAccessible(true);
            Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(500)));

            Field hideTimer = objBehavior.getClass().getDeclaredField("hideTimer");
            hideTimer.setAccessible(true);
            objTimer = (Timeline) hideTimer.get(objBehavior);

            objTimer.getKeyFrames().clear();
            objTimer.getKeyFrames().add(new KeyFrame(new Duration(10000)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
