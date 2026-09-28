package org.fornipinto.unfathomable_janken.game.player;

import org.fornipinto.unfathomable_janken.game.element.Element;

import java.util.List;

/**
 * Concrete {@link Player} representing a human player whose choices are driven by user input.
 */
public class HumanPlayer extends Player {
    /**
     * Constructs a new {@link HumanPlayer} with randomly generated elements.
     *
     * @param name The name of the human player.
     */
    public HumanPlayer(String name) {
        super(name);
    }

    /**
     * Constructs a new {@link HumanPlayer} with the specified elements.
     *
     * @param name     The name of the human player.
     * @param elements The initial list of elements possessed by this player.
     */
    public HumanPlayer(String name, List<Element> elements) {
        super(name, elements);
    }

    /**
     * Sets the active element to be used by this player in the current turn.
     *
     * @param element The {@link Element} to set as current.
     */
    public final void setCurrentElement(Element element) {
        assignCurrentElement(element);
    }
}
