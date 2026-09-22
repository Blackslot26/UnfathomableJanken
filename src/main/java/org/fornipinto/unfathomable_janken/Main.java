package org.fornipinto.unfathomable_janken;

import org.fornipinto.unfathomable_janken.engine.Engine;
import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.game.scenes.MainMenuScene;

import java.io.IOException;
import java.util.Arrays;

/**
 * The entry point of the application.
 */
public final class Main {
    /**
     * Indicates whether the application is running in debug mode.
     */
    public static final boolean debugMode = Boolean.parseBoolean(System.getProperty("debug", "false"));

    static void main() throws IOException {
        if (debugMode) {
            try (final DebugLogger ignored = new DebugLogger()) {
                run();
            } catch (IOException exception) {
                System.err.printf(
                    "Failed to initialize debug logger: %n%s%n%s%n",
                    exception.getMessage(),
                    Arrays.toString(exception.getStackTrace())
                );
            }
        } else {
            final var nullStream = new java.io.PrintStream(java.io.OutputStream.nullOutputStream());
            System.setOut(nullStream);
            System.setErr(nullStream);
            run();
        }
    }

    static void run() throws IOException {
        final Scene scene = new MainMenuScene();

        try (final Engine engine = new Engine(scene)) {
            engine.waitUntilStopped();
        } catch (Exception exception) {
            System.err.printf(
                "Exception caught in Main:%n%s%n%s%n",
                exception.getMessage(),
                Arrays.toString(exception.getStackTrace())
            );
        }
    }
}
