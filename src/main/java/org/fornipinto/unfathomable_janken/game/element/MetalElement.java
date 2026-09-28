package org.fornipinto.unfathomable_janken.game.element;

/**
 * Concrete implementation of {@link ElementType} representing the Metal element.
 */
public class MetalElement implements ElementType {
    @Override
    public <R> R accept(ElementTypeVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Integer visit(FireElement fireElement) {
        return 30;
    }

    @Override
    public Integer visit(WaterElement waterElement) {
        return 40;
    }

    @Override
    public Integer visit(EarthElement earthElement) {
        return 60;
    }

    @Override
    public Integer visit(WoodElement woodElement) {
        return 20;
    }

    @Override
    public Integer visit(MetalElement metalElement) {
        return 35;
    }
}