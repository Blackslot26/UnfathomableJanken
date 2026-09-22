package org.fornipinto.unfathomable_janken.game.element;

/**
 * Concrete implementation of {@link ElementType} representing the Earth element.
 */
public class EarthElement implements ElementType {
    /**
     * Constructs a new {@link EarthElement}.
     */
    public EarthElement() {}

    @Override
    public void damageFire(Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void damageWater(Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void damageEarth(Element element) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
