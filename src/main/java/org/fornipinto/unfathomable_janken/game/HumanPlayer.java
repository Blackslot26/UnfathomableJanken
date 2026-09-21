package org.fornipinto.unfathomable_janken.game;

/**
 * Concrete {@link Player} representing a human player whose choices are driven by user input.
 */
public class HumanPlayer extends Player {
    /**
     * Constructs a new {@link HumanPlayer}.
     */
    public HumanPlayer() {}
    /**
     * Sets the chosen active element for the human player.
     *
     * @param element The {@link Element} to set as current.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    @Override
    public void setCurrentElement(Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
