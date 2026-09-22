package org.fornipinto.unfathomable_janken.game.element;

/**
 * Concrete implementation of {@link ElementType} representing the Water element.
 */
public class WaterElement implements ElementType {
    /**
     * Constructs a new {@link WaterElement}.
     */
    public WaterElement() {}

    @Override
    public int getDamaged(ElementType elementType) {
        return elementType.damageWater();
    }

    @Override
    public int damageFire() {
        return 10;
    }

    @Override
    public int damageWater() {
        return 50;
    }

    @Override
    public int damageEarth() {
        return 100;
    }
}
