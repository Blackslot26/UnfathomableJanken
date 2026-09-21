package org.fornipinto.unfathomable_janken.game;

/**
 * Concrete {@link Player} representing an automated computer opponent powered by an {@link AI} strategy.
 */
public class AIPlayer extends Player {
    private AI ai;

    /**
     * Constructs a new {@link AIPlayer}.
     */
    public AIPlayer() {}

    /**
     * Configures the artificial intelligence strategy to be used by this player.
     *
     * @param ai The {@link AI} strategy instance.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public void setAI(AI ai) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Sets the chosen active element for the AI player.
     *
     * @param element The {@link Element} to set as current.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    @Override
    public void setCurrentElement(Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
