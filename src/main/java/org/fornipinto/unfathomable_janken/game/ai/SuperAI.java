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
        final var rivalElement = game.getMainPlayer().getCurrentElement();
        final var enemyElements = game.getEnemyPlayer().getActiveElements();

        if (rivalElement == null) {
            return selectWorstOption(game);
        }

        Element bestChoice = null;
        var minimumGlobalValue = Integer.MAX_VALUE;

        for (final var element : enemyElements) {
            final var damage = element.calculateDamageAgainst(rivalElement);

            if (damage >= rivalElement.getEnergy()) {
                final var globalValue = computeTotalDamage(game, element);

                if (globalValue < minimumGlobalValue) {
                    minimumGlobalValue = globalValue;
                    bestChoice = element;
                }
            }
        }

        if (bestChoice != null) {
            return bestChoice;
        }

        for (final var element : enemyElements) {
            if (bestChoice == null) {
                bestChoice = element;
                minimumGlobalValue = computeTotalDamage(game, element);
                continue;
            }

            final var damage = element.calculateDamageAgainst(rivalElement);
            final var bestChoiceDamage = bestChoice.calculateDamageAgainst(rivalElement);

            if (damage > bestChoiceDamage) {
                bestChoice = element;
                minimumGlobalValue = computeTotalDamage(game, element);
            } else if (damage == bestChoiceDamage) {
                final var globalValue = computeTotalDamage(game, element);

                if (globalValue < minimumGlobalValue) {
                    minimumGlobalValue = globalValue;
                    bestChoice = element;
                }
            }
        }

        return bestChoice;
    }

    private Element selectWorstOption(Game game) {
        Element worstChoice = null;
        var minimumDamage = Integer.MAX_VALUE;

        for (final var element : game.getEnemyPlayer().getActiveElements()) {
            final var totalDamage = computeTotalDamage(game, element);

            if (totalDamage < minimumDamage) {
                minimumDamage = totalDamage;
                worstChoice = element;
            }
        }

        return worstChoice;
    }

    private int computeTotalDamage(Game game, Element aiElement) {
        var totalDamage = 0;

        for (final var playerElement : game.getMainPlayer().getActiveElements()) {
            final var dealtDamage = aiElement.calculateDamageAgainst(playerElement);
            final var receivedDamage = playerElement.calculateDamageAgainst(aiElement);
            totalDamage = totalDamage + dealtDamage - receivedDamage;
        }

        return totalDamage;
    }
}
