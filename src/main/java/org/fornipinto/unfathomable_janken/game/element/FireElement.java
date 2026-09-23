package org.fornipinto.unfathomable_janken.game.element;

/**
 * Concrete implementation of {@link ElementType} representing the Fire element.
 */
public class FireElement implements ElementType {

    @Override
    public <R> R accept(ElementTypeVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Integer visit(FireElement fireElement) {
        return 50;
    }

    @Override
    public Integer visit(WaterElement waterElement) {
        return 100;
    }

    @Override
    public Integer visit(EarthElement earthElement) {
        return 10;
    }
}
