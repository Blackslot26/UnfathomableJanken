package org.fornipinto.unfathomable_janken.game;

import org.fornipinto.unfathomable_janken.game.log.ElementAttackedLogItem;
import org.fornipinto.unfathomable_janken.game.log.ElementSelectedLogItem;
import org.fornipinto.unfathomable_janken.game.log.GameStartedLogItem;
import org.fornipinto.unfathomable_janken.game.log.LogItem;
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
    private GameManager manager;
    final private HumanPlayer mainPlayer;
    final private AIPlayer enemyPlayer;
    private Player attacker;
    private final List<LogItem> log;

    /**
     * Constructs a new {@link Game}.
     *
     * @param mainPlayer The main human player participating in this game.
     */
    public Game(HumanPlayer mainPlayer) {
        this.mainPlayer = Objects.requireNonNull(mainPlayer);
        this.enemyPlayer = new AIPlayer();
        this.attacker = mainPlayer;
        this.log = new ArrayList<>();
        log.add(new GameStartedLogItem());
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
     * Returns the attacking player in the current turn.
     *
     * @return The attacker {@link Player} instance.
     */
    public Player getAttacker() {
        return attacker;
    }

    private Player getDefender() {
        if (attacker == mainPlayer) {
            return enemyPlayer;
        } else {
            return mainPlayer;
        }
    }

    /**
     * Process current turn and apply relevant changes.
     */
    public void processTurn() {
        if (manager == null) return;

        if (mainPlayer.getCurrentElement() == null) {
            manager.requireElementSelection(mainPlayer);
            log.add(new ElementSelectedLogItem(mainPlayer, mainPlayer.getCurrentElement()));
        } else if (enemyPlayer.getCurrentElement() == null) {
            enemyPlayer.selectNextElement(this);
            log.add(new ElementSelectedLogItem(enemyPlayer, enemyPlayer.getCurrentElement()));
        } else {
            final var attackerElement = attacker.getCurrentElement();
            final var defenderElement = getDefender().getCurrentElement();
            final var damage = attackerElement.attack(defenderElement);
            log.add(new ElementAttackedLogItem(attacker, getDefender(), attackerElement, defenderElement, damage));
            attacker = getDefender();
        }
    }

    /**
     * Sets the {@link GameManager} responsible for managing game state and interactions.
     *
     * @param manager The {@link GameManager} instance to set.
     */
    public void setManager(GameManager manager) {
        this.manager = Objects.requireNonNull(manager);
    }

    /**
     * Returns the current game log.
     *
     * @return The game log.
     */
    public List<LogItem> getLog() {
        return List.copyOf(log);
    }
}
