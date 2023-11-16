package seng202.team5;

import seng202.team5.views.windows.LoginWindow;

/**
 * Default entry point class.
 */
public final class App {

    /**
     * Removes instantiation of a static class.
     */
    private App() {

    }

    /**
     * Entry point which runs the javaFX application.
     * Also shows off some different logging levels.
     * @param args program arguments from command line
     */
    public static void main(final String[] args) {
        LoginWindow.main(args);
    }
}
