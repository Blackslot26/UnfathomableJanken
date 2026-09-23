package org.fornipinto.unfathomable_janken.game.log;

import org.fornipinto.unfathomable_janken.game.player.Player;

import java.util.Objects;

/**
 * A log registry for the game over event.
 */
public final class GameOverLogItem implements LogItem {
    private final Player winner;

    /**
     * Constructs a new {@link GameOverLogItem} with the specified winner.
     *
     * @param winner The player who won the game.
     */
    GameOverLogItem(Player winner) {
        this.winner = Objects.requireNonNull(winner);
    }

    @Override
    public <R> R accept(LogItemVisitor<R> visitor) {
        return visitor.visit(this);
    }

    /**
     * Returns the player who won the game.
     *
     * @return The {@link Player} instance.
     */
    public Player getWinner() {
        return winner;
    }
}
