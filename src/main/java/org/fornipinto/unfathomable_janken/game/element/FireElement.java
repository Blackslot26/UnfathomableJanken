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
        return 35;
    }

    @Override
    public Integer visit(WaterElement waterElement) {
        return 20;
    }

    @Override
    public Integer visit(EarthElement earthElement) {
        return 30;
    }

    @Override
    public Integer visit(WoodElement woodElement) {
        return 60;
    }

    @Override
    public Integer visit(MetalElement metalElement) {
        return 40;
    }
}
