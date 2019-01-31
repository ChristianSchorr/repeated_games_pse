package loop.preloader;

import com.sun.javafx.application.LauncherImpl;

import loop.Main;

public class PreLoader {

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Main.class, SplashScreenLoader.class, args);
    }

}
