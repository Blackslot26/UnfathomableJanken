package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.player.AIPlayer;

/**
 * Interface representing an artificial intelligence strategy for making tactical decisions in the game.
 * <p>
 * Implementations evaluate the current {@link Game} state and determine the best {@link Element}
 * for an {@link AIPlayer} to select.
 */
public interface AI {
    /**
     * Chooses an element to play given the current game context.
     *
     * @param game The current {@link Game} instance containing player and round state.
     * @return The chosen {@link Element}.
     */
    Element chooseElement(Game game);
}
