package org.fornipinto.unfathomable_janken.game.player;

import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.element.FireElement;
import org.fornipinto.unfathomable_janken.game.element.WaterElement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testSetCurrentElementValidatesOwnershipAndActivity() {
        final var ownedElement = new Element(new FireElement());
        final var foreignElement = new Element(new WaterElement());
        final var player = new HumanPlayer("Player", List.of(ownedElement));

        assertThrows(NullPointerException.class, () -> player.setCurrentElement(null));
        assertThrows(IllegalArgumentException.class, () -> player.setCurrentElement(foreignElement));

        player.setCurrentElement(ownedElement);
        assertSame(ownedElement, player.getCurrentElement());

        ownedElement.getDamaged(100);
        assertFalse(ownedElement.isActive());
        assertThrows(IllegalArgumentException.class, () -> player.setCurrentElement(ownedElement));
    }

    @Test
    void testYamlNameGeneratorProducesCompoundName() {
        final NameGenerator generator = new DictionaryBasedNameGenerator();
        final var generatedName = generator.generateName();

        assertNotNull(generatedName);
        assertFalse(generatedName.isBlank());
        assertTrue(generatedName.contains(" "));
    }
}
