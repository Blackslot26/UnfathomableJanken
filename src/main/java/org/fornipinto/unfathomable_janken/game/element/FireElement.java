package org.fornipinto.unfathomable_janken.game.element;

/**
 * Concrete implementation of {@link ElementType} representing the Fire element.
 */
public class FireElement implements ElementType {
    /**
     * Constructs a new {@link FireElement}.
     */
    public FireElement() {
    }

    @Override
    public int getDamaged(ElementType elementType) {
        return elementType.damageFire();
    }

    @Override
    public int damageFire() {
        return 50;
    }

    @Override
    public int damageWater() {
        return 100;
    }

    @Override
    public int damageEarth() {
        return 10;
    }
}
