package org.fornipinto.unfathomable_janken.game.player;

import org.fornipinto.unfathomable_janken.game.element.Element;

/**
 * Concrete {@link Player} representing a human player whose choices are driven by user input.
 */
public class HumanPlayer extends Player {
    private final String name;

    /**
     * Constructs a new {@link HumanPlayer}.
     *
     * @param name The name of the human player.
     */
    public HumanPlayer(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the active element to be used by this player in the current turn.
     *
     * @param element The {@link Element} to set as current.
     */
    public final void setCurrentElement(Element element) {
        this.currentElement = element;
    }
}
