package org.fornipinto.unfathomable_janken.game.player;

/**
 * Strategy interface for generating player names.
 */
@FunctionalInterface
public interface NameGenerator {
    /**
     * Generates a player name.
     *
     * @return The generated name.
     */
    String generateName();
}
