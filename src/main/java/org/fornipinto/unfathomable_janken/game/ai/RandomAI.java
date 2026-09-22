package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.Element;

/**
 * An {@link AI} implementation that selects an element at random among active available elements.
 */
public class RandomAI implements AI {
    /**
     * Constructs a new {@link RandomAI}.
     */
    public RandomAI() {}

    @Override
    public Element chooseElement(Game game) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
