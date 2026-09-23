package org.fornipinto.unfathomable_janken.game.scenes;

import org.fornipinto.unfathomable_janken.ui.core.*;
import org.jline.terminal.KeyEvent;
import org.fornipinto.unfathomable_janken.engine.Engine;
import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.player.HumanPlayer;
import org.fornipinto.unfathomable_janken.ui.components.*;

/**
 * A scene that renders the main menu.
 */
public class MainMenuScene implements Scene {
    private String playerName = "";

    @Override
    public Component build() {
        return new Align(
            Alignment.CENTER,
            new ConstrainedBox(
                Constraints.tightWidth(30),
                new Box(
                    Border.SINGLE,
                    new Column(
                        new Text("Enter your name: "),
                        new SizedBox(0, 1),
                        new TextInput(playerName, true)
                    ).mainAxisSize(MainAxisSize.MIN)
                )
            )
        );
    }

    @Override
    public void onKeyPress(KeyEvent event) {
        if (event.getSpecial() == KeyEvent.Special.Escape) {
            if (playerName.isEmpty()) {
                Engine.context().sceneManager().pop();
            } else {
                playerName = "";
            }
        } else if (event.getSpecial() == KeyEvent.Special.Backspace) {
            if (!playerName.isEmpty()) {
                playerName = playerName.substring(0, playerName.length() - 1);
            }
        } else if (event.getSpecial() == KeyEvent.Special.Enter) {
            if (playerName.isEmpty()) {
                return;
            }

            final var player = new HumanPlayer(playerName);
            final var game = new Game(player);
            final var gameScene = new GameScene(game);

            Engine.context().sceneManager().push(gameScene);
        } else if (event.getType() == KeyEvent.Type.Character && !Character.isISOControl(event.getCharacter())) {
            playerName += event.getCharacter();
        }
    }
}
