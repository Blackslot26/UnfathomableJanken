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
        final var activeElements = game.getEnemyPlayer().getActiveElements();

        if (!activeElements.isEmpty()) {
            return activeElements.get((int) (Math.random() * activeElements.size()));
        }

        return null;
    }
}
