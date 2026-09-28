package org.fornipinto.unfathomable_janken.game.element;

/**
 * Interface representing the elemental type of an {@link Element} and defining the double-dispatch
 * mechanism for elemental damage calculations.
 */
public interface ElementType extends ElementTypeVisitor<Integer> {
    /**
     * Accepts a visitor to perform an operation on this element type.
     *
     * @param visitor The visitor to accept.
     * @param <R>     The return type of the visitor's operation.
     * @return The result of the operation.
     */
    <R> R accept(ElementTypeVisitor<R> visitor);


    /**
     * Computes damage against a fire element.
     *
     * @return The damage value inflicted on a fire element.
     */
    Integer visit(FireElement fireElement);

    /**
     * Computes damage against a water element.
     *
     * @return The damage value inflicted on a water element.
     */
    Integer visit(WaterElement waterElement);

    /**
     * Computes damage against an earth element.
     *
     * @return The damage value inflicted on an earth element.
     */
    Integer visit(EarthElement earthElement);

    /**
     * Computes damage against a wood element.
     *
     * @return The damage value inflicted on a wood element.
     */
    Integer visit(WoodElement woodElement);

    /**
     * Computes damage against a metal element.
     *
     * @return The damage value inflicted on a metal element.
     */
    Integer visit(MetalElement metalElement);
}
