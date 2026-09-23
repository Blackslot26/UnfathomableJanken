package org.fornipinto.unfathomable_janken.game.log;

/**
 * A log registry for the game start event.
 */
public final class GameStartedLogItem implements LogItem {
    @Override
    public <R> R accept(LogItemVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
