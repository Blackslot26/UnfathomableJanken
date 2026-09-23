package org.fornipinto.unfathomable_janken.game.player;

import org.fornipinto.unfathomable_janken.game.element.EarthElement;
import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.element.FireElement;
import org.fornipinto.unfathomable_janken.game.element.WaterElement;

import java.util.List;

/**
 * Abstract base class representing a player in the game.
 * <p>
 * A player has an identifying name, a list of available {@link Element} instances, and an active
 * {@link Element} selected for the current turn.
 */
public abstract class Player {
    private final List<Element> elements;
    protected Element currentElement;

    /**
     * Constructs a new {@link Player}.
     */
    protected Player() {
        final var types = List.of(
            new EarthElement(),
            new EarthElement(),
            new WaterElement(),
            new FireElement(),
            new EarthElement(),
            new FireElement()
        );

        this.elements = types.stream().map(Element::new).toList();
    }

    /**
     * Returns the name of the player.
     *
     * @return The player name.
     */
    public abstract String getName();

    /**
     * Checks whether the player still has any active elements available for combat.
     *
     * @return {@code true} if at least one element is active; {@code false} otherwise.
     * @throws UnsupportedOperationException If this method is not implemented.
     */
    public final boolean hasActiveElements() {
        return !getActiveElements().isEmpty();
    }

    /**
     * Returns the list of all active elements currently possessed by this player.
     *
     * @return An unmodifiable or active {@link List} of {@link Element} instances.
     */
    public final List<Element> getActiveElements() {
        return elements.stream().filter(Element::isActive).toList();
    }

    /**
     * Returns the active element currently selected by this player.
     *
     * @return The current {@link Element} or {@code null} if none is selected.
     */
    public final Element getCurrentElement() {
        return currentElement;
    }

    /**
     * Returns the total energy this player can have.
     *
     * @return The total energy this player can have.
     */
    public final int getTotalEnergy() {
        return elements.size() * 100;
    }

    /**
     * Returns the current energy this player has.
     *
     * @return The current energy this player has.
     */
    public final int getEnergy() {
        return elements.stream().mapToInt(Element::getEnergy).sum();
    }
}
