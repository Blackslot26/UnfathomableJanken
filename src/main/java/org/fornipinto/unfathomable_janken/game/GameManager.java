package org.fornipinto.unfathomable_janken.game;

import org.fornipinto.unfathomable_janken.game.element.Element;

/**
 * Interface that defines the game management contract for orchestrating player interactions
 * and round progression.
 * <p>
 * The game manager handles user/game prompts such as requiring a player to select an
 * active {@link Element} to use in combat or comparison.
 */
public interface GameManager {
    /**
     * Prompts the current player or environment to select an active element.
     *
     * @return The chosen {@link Element} instance.
     */
    Element requireElementSelection();
}
