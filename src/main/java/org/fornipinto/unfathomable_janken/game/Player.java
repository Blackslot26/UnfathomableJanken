package org.fornipinto.unfathomable_janken.game;

import java.util.List;

/**
 * Abstract base class representing a player in the game.
 * <p>
 * A player has an identifying name, a list of available {@link Element} instances, and an active
 * {@link Element} selected for the current turn.
 *
 * @see HumanPlayer
 * @see AIPlayer
 * @see Element
 */
public abstract class Player {
    private String name;
    private List<Element> elements;
    private Element currentElement;

    /**
     * Constructs a new {@link Player}.
     */
    protected Player() {}

    /**
     * Returns the name of the player.
     *
     * @return The player name.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public String getName() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Checks whether the player still has any active elements available for combat.
     *
     * @return {@code true} if at least one element is active; {@code false} otherwise.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public boolean hasActiveElements() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Returns the list of all active elements currently possessed by this player.
     *
     * @return An unmodifiable or active {@link List} of {@link Element} instances.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public List<Element> getActiveElements() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Sets the active element to be used by this player in the current turn.
     *
     * @param element The {@link Element} to set as current.
     */
    public abstract void setCurrentElement(Element element);
}
