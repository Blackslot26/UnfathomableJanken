package org.fornipinto.unfathomable_janken.game;

import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.log.*;
import org.fornipinto.unfathomable_janken.game.player.AIPlayer;
import org.fornipinto.unfathomable_janken.game.player.HumanPlayer;
import org.fornipinto.unfathomable_janken.game.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Core game class coordinating the players, turn state, and overall game execution.
 */
public class Game {
    final private HumanPlayer mainPlayer;
    final private AIPlayer enemyPlayer;
    private Player attacker;
    private final List<LogItem> log;
    private State state;

    /**
     * Constructs a new {@link Game}.
     *
     * @param mainPlayer  The main human player participating in this game.
     * @param enemyPlayer The enemy AI player participating in this game.
     */
    public Game(HumanPlayer mainPlayer, AIPlayer enemyPlayer) {
        this.mainPlayer = Objects.requireNonNull(mainPlayer);
        this.enemyPlayer = Objects.requireNonNull(enemyPlayer);
        this.attacker = mainPlayer;
        this.log = new ArrayList<>();
        this.log.add(new GameStartedLogItem());
        this.state = State.SELECTING_ELEMENT;

        selectEnemyNextElement();
    }

    /**
     * Returns the main human player participating in this game.
     *
     * @return The {@link HumanPlayer} instance.
     */
    public HumanPlayer getMainPlayer() {
        return mainPlayer;
    }

    /**
     * Returns the enemy AI player participating in this game.
     *
     * @return The {@link AIPlayer} instance.
     */
    public AIPlayer getEnemyPlayer() {
        return enemyPlayer;
    }

    /**
     * Returns true if it is the main human player's turn to attack, false otherwise.
     *
     * @return True if the main player is the attacker, false otherwise.
     */
    public boolean isMainPlayerTurn() {
        return attacker == mainPlayer;
    }

    /**
     * Returns true if it is the enemy AI player's turn to attack, false otherwise.
     *
     * @return True if the enemy player is the attacker, false otherwise.
     */
    public boolean isEnemyPlayerTurn() {
        return attacker == enemyPlayer;
    }

    private Player getDefender() {
        if (attacker == mainPlayer) {
            return enemyPlayer;
        } else {
            return mainPlayer;
        }
    }

    /**
     * Selects an element for the specified player.
     *
     * @param player  The player selecting the element.
     * @param element The element being selected.
     */
    public void selectElement(HumanPlayer player, Element element) {
        if (state != State.SELECTING_ELEMENT) return;

        player.setCurrentElement(element);
        log.add(new ElementSelectedLogItem(player, element));
        state = State.READY_TO_ATTACK;
    }

    /**
     * Process current turn and apply relevant changes.
     */
    public void processTurn() {
        if (state != State.READY_TO_ATTACK) return;

        final var defender = getDefender();
        final var attackerElement = attacker.getCurrentElement();
        final var defenderElement = getDefender().getCurrentElement();
        final var damage = attacker.attack(getDefender());
        log.add(new ElementAttackedLogItem(attacker, getDefender(), attackerElement, defenderElement, damage));

        if (!defender.hasActiveElements()) {
            state = State.GAME_OVER;
            log.add(new GameOverLogItem(attacker));
            return;
        } else if (defender.getCurrentElement() == null) {
            if (defender == mainPlayer) {
                state = State.SELECTING_ELEMENT;
            } else {
                selectEnemyNextElement();
            }
        }

        attacker = defender;
    }

    /**
     * Returns the current game log.
     *
     * @return The game log.
     */
    public List<LogItem> getLog() {
        return List.copyOf(log);
    }

    /**
     * Returns the current state of the game.
     *
     * @return The current state.
     */
    public State getState() {
        return state;
    }

    /**
     * Returns true if the game has been won by the main player, false otherwise.
     *
     * @return True if the main player won, false otherwise.
     */
    public boolean wasGameWon() {
        return state == State.GAME_OVER && attacker == mainPlayer;
    }

    private void selectEnemyNextElement() {
        enemyPlayer.selectNextElement(this);
        log.add(new ElementSelectedLogItem(enemyPlayer, enemyPlayer.getCurrentElement()));
    }

    /**
     * Represents the possible states of the game.
     */
    public enum State {
        /**
         * The game is running in its attack phase.
         */
        READY_TO_ATTACK,

        /**
         * The game is in the process of selecting an element.
         */
        SELECTING_ELEMENT,

        /**
         * The game has ended.
         */
        GAME_OVER,
    }
}
