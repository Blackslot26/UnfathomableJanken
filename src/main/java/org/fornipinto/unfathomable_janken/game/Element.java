package org.fornipinto.unfathomable_janken.game;

/**
 * Represents an elemental entity possessed by a {@link Player}.
 * <p>
 * An element holds an energy value and is associated with a specific {@link ElementType} that determines
 * its combat interactions. An element remains active as long as it has positive energy remaining.
 */
public class Element {
    private int energy;
    private ElementType type;

    /**
     * Constructs a new {@link Element}.
     */
    public Element() {}

    /**
     * Returns the current energy level of this element.
     *
     * @return The energy value.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public int getEnergy() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Indicates whether this element is still active and capable of being used in play.
     *
     * @return {@code true} if the element is active; {@code false} otherwise.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public boolean isActive() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Applies damage to this element, reducing its current energy.
     *
     * @param damage The amount of damage to inflict.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public void getDamaged(int damage) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
