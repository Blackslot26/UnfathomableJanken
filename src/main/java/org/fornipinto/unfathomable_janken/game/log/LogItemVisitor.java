package org.fornipinto.unfathomable_janken.game.log;

/**
 * A visitor interface for log items.
 *
 * @param <R> The return type of the visitor methods.
 */
public interface LogItemVisitor<R> {
    /**
     * Visits a {@link GameStartedLogItem}.
     *
     * @param item The log item to visit.
     * @return The result of visiting the log item.
     */
    R visit(GameStartedLogItem item);

    /**
     * Visits a {@link ElementSelectedLogItem}.
     *
     * @param item The log item to visit.
     * @return The result of visiting the log item.
     */
    R visit(ElementSelectedLogItem item);

    /**
     * Visits a {@link ElementAttackedLogItem}.
     *
     * @param item The log item to visit.
     * @return The result of visiting the log item.
     */
    R visit(ElementAttackedLogItem item);

    /**
     * Visits a {@link GameOverLogItem}.
     *
     * @param item The log item to visit.
     * @return The result of visiting the log item.
     */
    R visit(GameOverLogItem item);
}
