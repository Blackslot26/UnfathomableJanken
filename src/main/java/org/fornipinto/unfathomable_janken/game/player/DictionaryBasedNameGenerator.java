package org.fornipinto.unfathomable_janken.game.player;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A {@link NameGenerator} implementation that loads adjectives and nouns from a YAML resource file
 * and generates random compound names.
 */
public final class DictionaryBasedNameGenerator implements NameGenerator {
    private static final String DEFAULT_RESOURCE = "dictionary.yaml";

    private final List<String> adjectives;
    private final List<String> nouns;
    private final Random random = new Random();

    /**
     * Constructs a new {@link DictionaryBasedNameGenerator} using the default {@code dictionary.yaml} resource.
     */
    public DictionaryBasedNameGenerator() {
        this(DEFAULT_RESOURCE);
    }

    /**
     * Constructs a new {@link DictionaryBasedNameGenerator} using the specified classpath resource.
     *
     * @param resourcePath The classpath resource path to the YAML dictionary.
     */
    public DictionaryBasedNameGenerator(String resourcePath) {
        final var yaml = new Yaml();

        try (var input = ClassLoader.getSystemResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IOException("Dictionary file not found: " + resourcePath);
            }

            final Map<String, Object> dictionary = yaml.load(input);
            final var loadedAdjectives = dictionary.get("adjectives");
            final var loadedNouns = dictionary.get("nouns");

            if (loadedAdjectives instanceof List<?> adjectivesList && loadedNouns instanceof List<?> nounsList) {
                this.adjectives = adjectivesList.stream().map(Object::toString).toList();
                this.nouns = nounsList.stream().map(Object::toString).toList();
            } else {
                throw new IOException("Invalid dictionary format.");
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error occurred while loading AI player name dictionary.", exception);
        }
    }

    @Override
    public String generateName() {
        final var randomAdjective = adjectives.get(random.nextInt(adjectives.size()));
        final var randomNoun = nouns.get(random.nextInt(nouns.size()));
        return String.format("%s %s", randomAdjective, randomNoun);
    }
}
