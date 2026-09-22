package org.fornipinto.unfathomable_janken.game;

/**
 * Core game class coordinating the players, turn state, and overall game execution.
 */
public class Game {
    private GameManager manager;
    private HumanPlayer mainPlayer;
    private AIPlayer enemyPlayer;
    private Player currentPlayer;

    /**
     * Constructs a new {@link Game}.
     *
     * @param mainPlayer The main human player participating in this game.
     */
    public Game(HumanPlayer mainPlayer) {
        this.mainPlayer = mainPlayer;
        this.enemyPlayer = new AIPlayer();
        this.currentPlayer = mainPlayer;
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
     * Returns the player whose turn or action is currently active.
     *
     * @return The active {@link Player} instance.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
