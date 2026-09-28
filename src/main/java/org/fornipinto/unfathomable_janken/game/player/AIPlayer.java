package org.fornipinto.unfathomable_janken.game.player;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.ai.AI;
import org.fornipinto.unfathomable_janken.game.element.Element;

import java.util.List;
import java.util.Objects;

/**
 * Concrete {@link Player} representing an automated computer opponent powered by an {@link AI} strategy.
 */
public class AIPlayer extends Player {
    private AI ai;

    /**
     * Constructs a new {@link AIPlayer} with randomly generated elements.
     *
     * @param name The name of the AI player.
     * @param ai   The {@link AI} strategy instance to be used by this player.
     */
    public AIPlayer(String name, AI ai) {
        super(name);
        this.ai = Objects.requireNonNull(ai);
    }

    /**
     * Constructs a new {@link AIPlayer} with the specified elements.
     *
     * @param name     The name of the AI player.
     * @param ai       The {@link AI} strategy instance to be used by this player.
     * @param elements The initial list of elements possessed by this player.
     */
    public AIPlayer(String name, AI ai, List<Element> elements) {
        super(name, elements);
        this.ai = Objects.requireNonNull(ai);
    }

    /**
     * Configures the artificial intelligence strategy to be used by this player.
     *
     * @param ai The {@link AI} strategy instance.
     */
    public void setAI(AI ai) {
        this.ai = Objects.requireNonNull(ai);
    }

    /**
     * Selects the next element for this player based on the configured {@link AI} strategy and the current game state.
     *
     * @param game The current game state.
     */
    public void selectNextElement(Game game) {
        if (currentElement != null) {
            throw new IllegalStateException("Cannot select next element: player already has a selected element.");
        }

        assignCurrentElement(ai.chooseElement(game));
    }
}
