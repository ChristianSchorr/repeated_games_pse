package loop;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import loop.controller.HeadController;
import loop.preloader.SplashScreenLoader;

import java.lang.reflect.Field;

/**
 * This class contains the main method of the program and initialises the main window.
 *
 * @author Peter Koepernik
 */
public class Main extends Application {
    
    public static final String RING_LOGO_PATH = "file:src/main/resources/loop_ring.png";
    
    private static final boolean doWaitForSplash = false;
    
    @FXML
    public void init() {
        if (doWaitForSplash) {
            try {
                Thread.sleep(SplashScreenLoader.animationMillis + 200); //let splash animation play
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/windows/HomeWindow.fxml"));
        Parent root = loader.load();
        HeadController controller = loader.getController();
        Scene scene = new Scene(root, 1920, 1080);
        
        stage.setTitle("loop");
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            try {
                controller.closeSimulator(null);
                Platform.exit();
                System.exit(0);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
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
