package org.fornipinto.unfathomable_janken.game.element;

import org.fornipinto.unfathomable_janken.game.player.Player;

import java.util.Objects;

/**
 * Represents an elemental entity possessed by a {@link Player}.
 * <p>
 * An element holds an energy value and is associated with a specific {@link ElementType} that determines
 * its combat interactions. An element remains active as long as it has positive energy remaining.
 */
public final class Element {
    private int energy = 100;
    private final ElementType type;

    /**
     * Constructs a new {@link Element}.
     *
     * @param type The {@link ElementType} associated with this element.
     */
    public Element(ElementType type) {
        this.type = type;
    }

    /**
     * Returns the current energy level of this element.
     *
     * @return The energy value.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public int getEnergy() {
        return this.energy;
    }

    /**
     * Indicates whether this element is still active and capable of being used in play.
     *
     * @return {@code true} if the element is active; {@code false} otherwise.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public boolean isActive() {
        return this.energy > 0;
    }

    /**
     * Applies damage to this element, reducing its current energy.
     *
     * @param damage The amount of damage to inflict.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public void getDamaged(int damage) {
        if (damage <= 0) {
            throw new IllegalArgumentException("Damage must be a positive integer");
        }

        this.energy = Math.max(0, this.energy - damage);
    }

    /**
     * Returns the type of this element.
     *
     * @return The {@link ElementType} associated with this element.
     */
    public ElementType getType() {
        return this.type;
    }

    /**
     * Attack another element.
     *
     * @param other The element to attack.
     * @return The amount of damage inflicted on the other element.
     */
    public int attack(Element other) {
        Objects.requireNonNull(other);

        final var damage = other.getType().accept(this.type);
        other.getDamaged(damage);
        return damage;
    }
}
