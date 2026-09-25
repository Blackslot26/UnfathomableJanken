package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.Element;

/**
 * An advanced {@link AI} implementation utilizing deeper game tree analysis or optimal predictive
 * models to counter opponent choices.
 */
public class SuperAI implements AI {
    @Override
    public <R> R accept(AIVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Element chooseElement(Game game) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
