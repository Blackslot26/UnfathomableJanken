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
     */
    public Game() {}

    /**
     * Returns the main human player participating in this game.
     *
     * @return The {@link HumanPlayer} instance.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public HumanPlayer getMainPlayer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Returns the enemy AI player participating in this game.
     *
     * @return The {@link AIPlayer} instance.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public AIPlayer getEnemyPlayer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Returns the player whose turn or action is currently active.
     *
     * @return The active {@link Player} instance.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public Player getCurrentPlayer() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
