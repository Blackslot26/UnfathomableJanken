package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.Element;

/**
 * An {@link AI} implementation that strategically evaluates player element history, remaining energy,
 * and elemental counter-picks before choosing an element.
 */
public class StrategicAI implements AI {
    private final AI fallbackAI = new RandomAI();

    @Override
    public <R> R accept(AIVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Element chooseElement(Game game) {
        final var rivalElement = game.getMainPlayer().getCurrentElement();

        if (rivalElement == null) {
            return fallbackAI.chooseElement(game);
        }

        Element bestChoice = null;

        for (final var element : game.getEnemyPlayer().getActiveElements()) {
            if (bestChoice == null) {
                bestChoice = element;
                continue;
            }

            final var damage = element.calculateDamageAgainst(rivalElement);
            final var bestChoiceDamage = bestChoice.calculateDamageAgainst(rivalElement);

            if (damage > bestChoiceDamage) {
                bestChoice = element;
            }
        }

        return bestChoice;
    }
}
