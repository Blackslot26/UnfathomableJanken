package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.Element;

/**
 * An {@link AI} implementation that selects an element at random among active available elements.
 */
public class RandomAI implements AI {
    @Override
    public <R> R accept(AIVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Element chooseElement(Game game) {
        return this.accept(new ChooseElementVisitor(game));
    }
}
