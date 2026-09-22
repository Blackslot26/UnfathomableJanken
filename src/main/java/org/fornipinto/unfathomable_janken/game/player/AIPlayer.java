package org.fornipinto.unfathomable_janken.game.player;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.ai.AI;
import org.yaml.snakeyaml.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Concrete {@link Player} representing an automated computer opponent powered by an {@link AI} strategy.
 */
public class AIPlayer extends Player {
    private final String name;
    private AI ai;

    /**
     * Constructs a new {@link AIPlayer}.
     */
    public AIPlayer() {
        super();
        this.name = generateName();
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Configures the artificial intelligence strategy to be used by this player.
     *
     * @param ai The {@link AI} strategy instance.
     */
    public void setAI(AI ai) {
        this.ai = ai;
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

        currentElement = ai.chooseElement(game);
    }

    static private String generateName() {
        final var yaml = new Yaml();

        try (var input = ClassLoader.getSystemResourceAsStream("dictionary.yaml")) {
            if (input == null) {
                throw new IOException("Dictionary file not found.");
            }

            final Map<String, Object> dictionary = yaml.load(input);
            final var adjectives = dictionary.get("adjectives");
            final var nouns = dictionary.get("nouns");

            if (adjectives instanceof List<?> adjectivesList && nouns instanceof List<?> nounsList) {
                final var randomAdjective = adjectivesList.get((int) (Math.random() * adjectivesList.size()));
                final var randomNoun = nounsList.get((int) (Math.random() * nounsList.size()));
                return String.format("%s %s", randomAdjective, randomNoun);
            } else {
                throw new IOException("Invalid dictionary format.");
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error occurred while generating AI player name.", exception);
        }
    }
}
