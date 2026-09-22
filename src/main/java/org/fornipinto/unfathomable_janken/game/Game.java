package org.fornipinto.unfathomable_janken.game;

import org.fornipinto.unfathomable_janken.game.player.AIPlayer;
import org.fornipinto.unfathomable_janken.game.player.HumanPlayer;
import org.fornipinto.unfathomable_janken.game.player.Player;

import java.util.Objects;

/**
 * Core game class coordinating the players, turn state, and overall game execution.
 */
public class Game {
    private GameManager manager;
    final private HumanPlayer mainPlayer;
    final private AIPlayer enemyPlayer;
    private Player attacker;

    /**
     * Constructs a new {@link Game}.
     *
     * @param mainPlayer The main human player participating in this game.
     */
    public Game(HumanPlayer mainPlayer) {
        this.mainPlayer = Objects.requireNonNull(mainPlayer);
        this.enemyPlayer = new AIPlayer();
        this.attacker = mainPlayer;
    }

    /**
     * Returns the main human player participating in this game.
     *
     * @return The {@link HumanPlayer} instance.
     */
    public HumanPlayer getMainPlayer() {
        return mainPlayer;
    }

    /**
     * Returns the enemy AI player participating in this game.
     *
     * @return The {@link AIPlayer} instance.
     */
    public AIPlayer getEnemyPlayer() {
        return enemyPlayer;
    }

    /**
     * Returns the attacking player in the current turn.
     *
     * @return The attacker {@link Player} instance.
     */
    public Player getAttacker() {
        return attacker;
    }

    private Player getDefender() {
        if (attacker == mainPlayer) {
            return enemyPlayer;
        } else {
            return mainPlayer;
        }
    }

    private void processTurn() {
        if (mainPlayer.getCurrentElement() == null) {
            final var newElement = Objects.requireNonNull(manager.requireElementSelection(mainPlayer));
            mainPlayer.setCurrentElement(newElement);
        } else if (enemyPlayer.getCurrentElement() == null) {
            enemyPlayer.selectNextElement(this);
        } else {
        }
    }
}
