package org.fornipinto.unfathomable_janken.game.scenes;

import org.fornipinto.unfathomable_janken.engine.Engine;
import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.ui.components.Align;
import org.fornipinto.unfathomable_janken.ui.components.Column;
import org.fornipinto.unfathomable_janken.ui.components.SizedBox;
import org.fornipinto.unfathomable_janken.ui.components.Text;
import org.fornipinto.unfathomable_janken.ui.core.Alignment;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.CrossAxisAlignment;
import org.fornipinto.unfathomable_janken.ui.core.MainAxisSize;
import org.jline.terminal.KeyEvent;

/**
 * A scene that displays the game over screen, indicating whether the player won or lost the game.
 */
public class GameOverScene implements Scene {

    static private final String WINNER_TEXT = """
        ▄▄▄    ▄▄▄   ▄▄▄▄    ▄▄    ▄▄            ▄▄▄▄▄▄      ▄▄▄▄       ▄▄▄▄   ▄▄   ▄▄▄\s
         ██▄  ▄██   ██▀▀██   ██    ██            ██▀▀▀▀██   ██▀▀██    ██▀▀▀▀█  ██  ██▀ \s
          ██▄▄██   ██    ██  ██    ██            ██    ██  ██    ██  ██▀       ██▄██   \s
           ▀██▀    ██    ██  ██    ██            ███████   ██    ██  ██        █████   \s
            ██     ██    ██  ██    ██            ██  ▀██▄  ██    ██  ██▄       ██  ██▄ \s
            ██      ██▄▄██   ▀██▄▄██▀            ██    ██   ██▄▄██    ██▄▄▄▄█  ██   ██▄\s
            ▀▀       ▀▀▀▀      ▀▀▀▀              ▀▀    ▀▀▀   ▀▀▀▀       ▀▀▀▀   ▀▀    ▀▀\s
        """;

    static private final String LOSER_TEXT = """
        ▄▄          ▄▄▄▄      ▄▄▄▄    ▄▄▄▄▄▄▄▄  ▄▄▄▄▄▄  \s
        ██         ██▀▀██   ▄█▀▀▀▀█   ██▀▀▀▀▀▀  ██▀▀▀▀██\s
        ██        ██    ██  ██▄       ██        ██    ██\s
        ██        ██    ██   ▀████▄   ███████   ███████ \s
        ██        ██    ██       ▀██  ██        ██  ▀██▄\s
        ██▄▄▄▄▄▄   ██▄▄██   █▄▄▄▄▄█▀  ██▄▄▄▄▄▄  ██    ██\s
        ▀▀▀▀▀▀▀▀    ▀▀▀▀     ▀▀▀▀▀    ▀▀▀▀▀▀▀▀  ▀▀    ▀▀▀
        """;
    private final boolean didPlayerWin;

    /**
     * Creates a new GameOverScene.
     *
     * @param didPlayerWin A boolean indicating whether the player won or lost the game.
     */
    public GameOverScene(boolean didPlayerWin) {
        this.didPlayerWin = didPlayerWin;
    }

    @Override
    public Component build() {
        return new Align(
            Alignment.CENTER,
            new Column(
                new Text(didPlayerWin ? WINNER_TEXT : LOSER_TEXT),
                new SizedBox(0, 4),
                new Text("Press ENTER to go back to main menu.")
            )
                .crossAxisAlignment(CrossAxisAlignment.CENTER)
                .mainAxisSize(MainAxisSize.MIN)
        );
    }

    @Override
    public void onKeyPress(KeyEvent event) {
        if (event.getSpecial() == KeyEvent.Special.Enter) {
            Engine.context().sceneManager().pop();
            Engine.context().sceneManager().pop();
        }
    }
}
