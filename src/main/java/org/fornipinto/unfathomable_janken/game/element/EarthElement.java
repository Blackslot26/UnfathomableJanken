package org.fornipinto.unfathomable_janken.game.element;

/**
 * Concrete implementation of {@link ElementType} representing the Earth element.
 */
public class EarthElement implements ElementType {
    /**
     * Constructs a new {@link EarthElement}.
     */
    public EarthElement() {
    }

    @Override
    public int getDamaged(ElementType elementType) {
        return elementType.damageEarth();
    }

    @Override
    public int damageFire() {
        return 100;
    }

    @Override
    public int damageWater() {
        return 10;
    }

    @Override
    public int damageEarth() {
        return 50;
    }
}
