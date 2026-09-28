package org.fornipinto.unfathomable_janken.game.element;

/**
 * A visitor for the different types of elements.
 *
 * @param <R> The return type of the visitor.
 */
public interface ElementTypeVisitor<R> {
    /**
     * Visits a {@link WaterElement}.
     *
     * @param water The water element to visit.
     * @return The result of visiting the water element.
     */
    R visit(WaterElement water);

    /**
     * Visits a {@link FireElement}.
     *
     * @param fire The fire element to visit.
     * @return The result of visiting the fire element.
     */
    R visit(FireElement fire);

    /**
     * Visits a {@link EarthElement}.
     *
     * @param earth The earth element to visit.
     * @return The result of visiting the earth element.
     */
    R visit(EarthElement earth);

    /**
     * Visits a {@link WoodElement}.
     *
     * @param wood The wood element to visit.
     * @return The result of visiting the wood element.
     */
    R visit(WoodElement wood);

    /**
     * Visits a {@link MetalElement}.
     *
     * @param metal The metal element to visit.
     * @return The result of visiting the metal element.
     */
    R visit(MetalElement metal);
}
