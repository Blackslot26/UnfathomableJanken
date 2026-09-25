package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.Element;

/**
 * An {@link AI} implementation that strategically evaluates player element history, remaining energy,
 * and elemental counter-picks before choosing an element.
 */
public class StrategicAI implements AI {
    @Override
    public <R> R accept(AIVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Element chooseElement(Game game) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
