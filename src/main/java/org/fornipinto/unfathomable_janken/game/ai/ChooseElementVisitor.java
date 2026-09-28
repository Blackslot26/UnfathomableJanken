package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.element.Element;

import org.fornipinto.unfathomable_janken.game.Game;

class ChooseElementVisitor implements AIVisitor<Element> {
    private final Game game;

    /**
     * Constructs a new {@link ChooseElementVisitor} with the specified game context.
     *
     * @param game The game context used to make decisions about element selection.
     */
    public ChooseElementVisitor(Game game) {
        this.game = game;
    }

    /**
     * Chooses a random element from the enemy player's active elements.
     *
     * @param ai The RandomAI instance to visit.
     * @return The chosen {@link Element}.
     */
    @Override
    public Element visit(RandomAI ai) {
        final var activeElements = game.getEnemyPlayer().getActiveElements();

        if (!activeElements.isEmpty()) {
            return activeElements.get((int) (Math.random() * activeElements.size()));
        }

        return null;
    }

    /**
     * Chooses an element from the enemy player's active elements based on the damage inflicted.
     * <p>
     * If the main player has no current element, it defaults to a random choice.
     *
     * @param ai The StrategicAI instance to visit.
     * @return The chosen {@link Element}.
     */
    @Override
    public Element visit(StrategicAI ai) {
        if (game.getMainPlayer().getCurrentElement() == null) {
            return this.visit(new RandomAI());
        }

        final var rivalElement = game.getMainPlayer().getCurrentElement();
        Element bestChoice = null;

        for (final var element : game.getEnemyPlayer().getActiveElements()) {
            if (bestChoice == null) {
                bestChoice = element;
                continue;
            }

            final var damage = rivalElement.getType().accept(element.getType());
            final var bestChoiceDamage = rivalElement.getType().accept(bestChoice.getType());

            if (damage > bestChoiceDamage) {
                bestChoice = element;
            }
        }

        return bestChoice;
    }

    /**
     * Chooses the best element to play based on the current game state and the main player's current element.
     * <p>
     * If the main player has no current element, it chooses the element with the least overall value.
     * Otherwise, it selects the element that can inflict fatal damage to the main player's current element while
     * minimizing its own overall value.
     * <p>
     * If no fatal damage option is available, it chooses the element that inflicts the most damage while minimizing its
     * own overall value.
     *
     * @param ai The SuperAI instance to visit.
     * @return The chosen {@link Element}.
     */
    @Override
    public Element visit(SuperAI ai) {
        final var rivalElement = game.getMainPlayer().getCurrentElement();
        final var enemyElements = game.getEnemyPlayer().getActiveElements();

        if (rivalElement == null) {
            return selectWorstOption();
        }

        Element bestChoice = null;
        var minimumGlobalValue = Integer.MAX_VALUE;

        for (final var element : enemyElements) {
            final var damage = rivalElement.getType().accept(element.getType());

            if (damage >= rivalElement.getEnergy()) {
                final var globalValue = computeTotalDamage(element);

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
                minimumGlobalValue = computeTotalDamage(element);
                continue;
            }

            final var damage = rivalElement.getType().accept(element.getType());
            final var bestChoiceDamage = rivalElement.getType().accept(bestChoice.getType());

            if (damage > bestChoiceDamage) {
                bestChoice = element;
                minimumGlobalValue = computeTotalDamage(element);
            } else if (damage.equals(bestChoiceDamage)) {
                final var globalValue = computeTotalDamage(element);

                if (globalValue < minimumGlobalValue) {
                    minimumGlobalValue = globalValue;
                    bestChoice = element;
                }
            }
        }

        return bestChoice;
    }

    private Element selectWorstOption() {
        Element worstChoice = null;
        var minimumDamage = Integer.MAX_VALUE;

        for (final var element : game.getEnemyPlayer().getActiveElements()) {
            final var totalDamage = computeTotalDamage(element);

            if (totalDamage < minimumDamage) {
                minimumDamage = totalDamage;
                worstChoice = element;
            }
        }

        return worstChoice;
    }

    private int computeTotalDamage(Element aiElement) {
        var totalDamage = 0;

        for (final var playerElement : game.getMainPlayer().getActiveElements()) {
            final var dealtDamage = playerElement.getType().accept(aiElement.getType());
            final var receivedDamage = aiElement.getType().accept(playerElement.getType());
            totalDamage = totalDamage + dealtDamage - receivedDamage;
        }

        return totalDamage;
    }
}
