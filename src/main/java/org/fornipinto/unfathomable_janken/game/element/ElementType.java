package org.fornipinto.unfathomable_janken.game.element;

/**
 * Interface representing the elemental type of an {@link Element} and defining the double-dispatch
 * mechanism for elemental damage calculations.
 */
public interface ElementType {
    /// Compute the damage inflicted on this element type by another element type.
    ///
    /// @param elementType The opposing {@link ElementType} to compute damage against.
    ///
    /// @return The damage value inflicted on this element type by the opposing element type.
    int getDamaged(ElementType elementType);

    /**
     * Computes damage against a fire element.
     *
     * @return The damage value inflicted on a fire element.
     */
    int damageFire();

    /**
     * Computes damage against a water element.
     *
     * @return The damage value inflicted on a water element.
     */
    int damageWater();

    /**
     * Computes damage against an earth element.
     *
     * @return The damage value inflicted on an earth element.
     */
    int damageEarth();
}
