package org.fornipinto.unfathomable_janken;

import org.fornipinto.unfathomable_janken.engine.Engine;
import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.game.scenes.EmptyScene;

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
        run();
    }

    static void run() throws IOException {
        final Scene scene = new EmptyScene();

        try (final Engine engine = new Engine(scene)) {
            if (debugMode) {
                try (final DebugLogger ignored = new DebugLogger()) {
                    engine.waitUntilStopped();
                } catch (IOException exception) {
                    System.err.printf(
                        "Failed to initialize debug logger: %n%s%n%s%n",
                        exception.getMessage(),
                        Arrays.toString(exception.getStackTrace())
                    );
                }
            } else {
                engine.waitUntilStopped();
            }
        } catch (Exception exception) {
            System.err.printf(
                "Exception caught in Main:%n%s%n%s%n",
                exception.getMessage(),
                Arrays.toString(exception.getStackTrace())
            );
        }
    }
}
