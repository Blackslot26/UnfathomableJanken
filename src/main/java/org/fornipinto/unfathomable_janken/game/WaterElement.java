package org.fornipinto.unfathomable_janken.game;

/**
 * Concrete implementation of {@link ElementType} representing the Water element.
 */
public class WaterElement implements ElementType {
    /**
     * Constructs a new {@link WaterElement}.
     */
    public WaterElement() {}

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
