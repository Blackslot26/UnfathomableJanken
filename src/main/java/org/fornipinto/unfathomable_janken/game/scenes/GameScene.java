package org.fornipinto.unfathomable_janken.game.scenes;

import org.fornipinto.unfathomable_janken.engine.Scene;
import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.player.Player;
import org.fornipinto.unfathomable_janken.ui.components.*;
import org.fornipinto.unfathomable_janken.ui.core.Border;
import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.MainAxisSize;
import org.fornipinto.unfathomable_janken.ui.core.Paint;

/**
 * A scene that renders the game.
 */
public class GameScene implements Scene {
    private final Game game;

    /**
     * Creates a new GameScene.
     *
     * @param game The game object to render.
     */
    public GameScene(Game game) {
        this.game = game;
    }

    @Override
    public Component build() {
        return new Box(
            new Column(
                playerCard(game.getEnemyPlayer()),
                new Flexible(1, new SizedBox(1, 1)),
                playerCard(game.getMainPlayer())
            )
        );
    }

    private Component playerCard(Player player) {
        return new Box(
            Border.SINGLE,
            new Column(
                new Text(player.getName(), Paint.BOLD),
                new Text("Energy: " + player.getEnergy() + " / " + player.getTotalEnergy())
            ).mainAxisSize(MainAxisSize.MIN)
        );
    }
}
