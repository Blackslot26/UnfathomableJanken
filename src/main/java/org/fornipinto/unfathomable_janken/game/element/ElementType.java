package org.fornipinto.unfathomable_janken.game.element;

/**
 * Interface representing the elemental type of an {@link Element} and defining the double-dispatch
 * mechanism for elemental damage calculations and combat interactions.
 * <p>
 * Implementations follow a rock-paper-scissors (Janken) dynamic between Fire, Water, and Earth elements.
 */
public interface ElementType {
    /**
     * Applies damage or combat effects when interacting against a fire element.
     *
     * @param element The target or interacting {@link Element} instance.
     */
    void damageFire(Element element);

    /**
     * Applies damage or combat effects when interacting against a water element.
     *
     * @param element The target or interacting {@link Element} instance.
     */
    void damageWater(Element element);

    /**
     * Applies damage or combat effects when interacting against an earth element.
     *
     * @param element The target or interacting {@link Element} instance.
     */
    void damageEarth(Element element);
}
