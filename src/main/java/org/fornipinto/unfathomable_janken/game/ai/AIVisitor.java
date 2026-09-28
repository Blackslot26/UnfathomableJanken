package org.fornipinto.unfathomable_janken.game.ai;

/**
 * Visitor interface for AI strategies, allowing operations to be performed on different types of AI implementations.
 *
 * @param <R> The return type of the visitor's operation.
 */
public interface AIVisitor<R> {
    /**
     * Visits a RandomAI instance and performs an operation on it.
     *
     * @param ai The RandomAI instance to visit.
     * @return The result of the operation.
     */
    R visit(RandomAI ai);

    /**
     * Visits a StrategicAI instance and performs an operation on it.
     *
     * @param ai The StrategicAI instance to visit.
     * @return The result of the operation.
     */
    R visit(StrategicAI ai);

    /**
     * Visits a SuperAI instance and performs an operation on it.
     *
     * @param ai The SuperAI instance to visit.
     * @return The result of the operation.
     */
    R visit(SuperAI ai);
}
