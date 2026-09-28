package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.Game;
import org.fornipinto.unfathomable_janken.game.element.EarthElement;
import org.fornipinto.unfathomable_janken.game.element.Element;
import org.fornipinto.unfathomable_janken.game.element.ElementType;
import org.fornipinto.unfathomable_janken.game.element.ElementTypeVisitor;
import org.fornipinto.unfathomable_janken.game.element.FireElement;
import org.fornipinto.unfathomable_janken.game.element.MetalElement;
import org.fornipinto.unfathomable_janken.game.element.WaterElement;
import org.fornipinto.unfathomable_janken.game.element.WoodElement;
import org.fornipinto.unfathomable_janken.game.player.AIPlayer;
import org.fornipinto.unfathomable_janken.game.player.HumanPlayer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @Test
    void testRandomAIChoosesActiveElementOrNullWhenEmpty() {
        final var randomAI = new RandomAI();
        final var human = new HumanPlayer("Player");
        final var aiPlayer = new AIPlayer("AI", randomAI);
        final var game = new Game(human, aiPlayer);

        final var chosen = aiPlayer.getCurrentElement();
        assertNotNull(chosen);
        assertTrue(aiPlayer.getActiveElements().contains(chosen));

        // Deactivate all enemy elements
        for (final var element : aiPlayer.getElements()) {
            element.getDamaged(100);
        }

        assertNull(randomAI.chooseElement(game));
    }

    @Test
    void testStrategicAIFallsBackToRandomWhenRivalHasNoCurrentElement() {
        final var human = new HumanPlayer("Player");
        final var aiPlayer = new AIPlayer("AI", new StrategicAI());
        final var game = new Game(human, aiPlayer);

        assertNull(human.getCurrentElement());
        assertNotNull(aiPlayer.getCurrentElement());
        assertTrue(aiPlayer.getActiveElements().contains(aiPlayer.getCurrentElement()));
    }

    @Test
    void testStrategicAIChoosesHighestDamageElementAgainstRival() {
        final var rivalFire = new Element(new FireElement());
        final var human = new HumanPlayer("Player", List.of(rivalFire));

        // Against Fire: Metal deals 20, Wood deals 30, Fire deals 35, Earth deals 40, Water deals 60
        final var aiMetal = new Element(new MetalElement());
        final var aiWood = new Element(new WoodElement());
        final var aiWater = new Element(new WaterElement());
        final var aiEarth = new Element(new EarthElement());
        final var strategicAI = new StrategicAI();
        final var aiPlayer = new AIPlayer("AI", strategicAI, List.of(aiMetal, aiWood, aiWater, aiEarth));

        final var game = new Game(human, aiPlayer);
        game.selectElement(human, rivalFire);

        final var chosen = strategicAI.chooseElement(game);

        assertSame(aiWater, chosen);
    }

    @Test
    void testSuperAIChoosesWorstGlobalOptionWhenRivalElementIsNull() {
        // Human has only Fire
        final var humanFire = new Element(new FireElement());
        final var human = new HumanPlayer("Player", List.of(humanFire));

        // Against Human's Fire:
        // - AI Water: dealt 60, received 20 -> totalDamage = +40
        // - AI Metal: dealt 20, received 60 -> totalDamage = -40 (worst global value)
        final var aiWater = new Element(new WaterElement());
        final var aiMetal = new Element(new MetalElement());
        final var superAI = new SuperAI();
        final var aiPlayer = new AIPlayer("AI", superAI, List.of(aiWater, aiMetal));

        final var game = new Game(human, aiPlayer);

        assertSame(aiMetal, superAI.chooseElement(game));
    }

    @Test
    void testSuperAIChoosesFatalElementWithMinimumGlobalValue() {
        // Rival Fire has 35 energy left (so both Fire [35 dmg] and Water [60 dmg] can deal fatal damage)
        final var rivalFire = new Element(new FireElement());
        rivalFire.getDamaged(65);
        final var humanEarth = new Element(new EarthElement());
        final var human = new HumanPlayer("Player", List.of(rivalFire, humanEarth));

        // Global value against [Fire, Earth]:
        // - AI Water: vs Fire (+40), vs Earth (-40) -> totalDamage = 0
        // - AI Fire:  vs Fire (0),   vs Earth (-10) -> totalDamage = -10 (smaller global value!)
        final var aiWater = new Element(new WaterElement());
        final var aiFire = new Element(new FireElement());
        final var superAI = new SuperAI();
        final var aiPlayer = new AIPlayer("AI", superAI, List.of(aiWater, aiFire));

        final var game = new Game(human, aiPlayer);
        game.selectElement(human, rivalFire);

        final var chosen = superAI.chooseElement(game);

        assertSame(aiFire, chosen);
    }

    @Test
    void testSuperAIChoosesMaxDamageAndBreaksTiesByMinimumGlobalValueWhenNoFatalOption() {
        // Rival Fire has full 100 energy (no single attack is fatal)
        final var rivalFire = new Element(new FireElement());
        final var humanWater = new Element(new WaterElement());
        final var human = new HumanPlayer("Player", List.of(rivalFire, humanWater));

        // Put weaker element (Metal: 20 dmg to Fire) first, and stronger (Earth: 40 dmg, Water: 60 dmg) after
        final var aiMetal = new Element(new MetalElement());
        final var aiEarth = new Element(new EarthElement());
        final var aiWater = new Element(new WaterElement());
        final var superAI = new SuperAI();
        final var aiPlayer = new AIPlayer("AI", superAI, List.of(aiMetal, aiEarth, aiWater));

        final var game = new Game(human, aiPlayer);
        game.selectElement(human, rivalFire);

        assertSame(aiWater, superAI.chooseElement(game));
    }

    @Test
    void testSuperAIBreaksTiesByMinimumGlobalValueWhenDamagesAreEqual() {
        final var rivalFire = new Element(new FireElement());
        final var humanEarth = new Element(new EarthElement());
        final var human = new HumanPlayer("Player", List.of(rivalFire, humanEarth));

        // Custom ElementType that deals 60 damage to Fire (tying with Water),
        // but has a lower global value against [Fire, Earth] than Water
        final var customType = new ElementType() {
            @Override
            public <R> R accept(ElementTypeVisitor<R> visitor) {
                return visitor.visit(new MetalElement());
            }

            @Override
            public Integer visit(FireElement fireElement) {
                return 60;
            }

            @Override
            public Integer visit(WaterElement waterElement) {
                return 20;
            }

            @Override
            public Integer visit(EarthElement earthElement) {
                return 20;
            }

            @Override
            public Integer visit(WoodElement woodElement) {
                return 20;
            }

            @Override
            public Integer visit(MetalElement metalElement) {
                return 20;
            }
        };

        final var aiWater = new Element(new WaterElement());
        final var aiCustom = new Element(customType);
        final var superAI = new SuperAI();
        final var aiPlayer = new AIPlayer("AI", superAI, List.of(aiWater, aiCustom));

        final var game = new Game(human, aiPlayer);
        game.selectElement(human, rivalFire);

        assertSame(aiCustom, superAI.chooseElement(game));
    }

    @Test
    void testExhaustiveEquivalenceWithRemoteImplementation() {
        final var allTypes = List.of(
            new FireElement(),
            new WaterElement(),
            new EarthElement(),
            new WoodElement(),
            new MetalElement()
        );

        final var strategicAI = new StrategicAI();
        final var superAI = new SuperAI();

        for (final var rivalType : allTypes) {
            for (final var energy : List.of(100, 60, 40, 35, 30, 20, 10)) {
                final var rivalElement = new Element(rivalType);
                if (energy < 100) {
                    rivalElement.getDamaged(100 - energy);
                }

                final var humanDeck = List.of(
                    rivalElement,
                    new Element(new WaterElement()),
                    new Element(new WoodElement())
                );
                final var aiDeck = allTypes.stream().map(Element::new).toList();

                final var human = new HumanPlayer("Player", humanDeck);
                final var aiPlayer = new AIPlayer("AI", superAI, aiDeck);

                final var game = new Game(human, aiPlayer);
                game.selectElement(human, rivalElement);

                assertSame(
                    originalStrategicAI(game),
                    strategicAI.chooseElement(game),
                    "StrategicAI mismatch for rival=" + rivalType.getClass().getSimpleName()
                );
                assertSame(
                    originalSuperAI(game),
                    superAI.chooseElement(game),
                    "SuperAI mismatch for rival=" + rivalType.getClass().getSimpleName() + " energy=" + energy
                );
            }
        }
    }

    private static Element originalStrategicAI(Game game) {
        Element rivalElement = game.getMainPlayer().getCurrentElement();
        Element mejorOpcion = null;
        int maxDmg = -1;
        for (Element aiElement : game.getEnemyPlayer().getActiveElements()) {
            int dmg = rivalElement.getType().<Integer>accept(aiElement.getType());
            if (dmg > maxDmg) {
                maxDmg = dmg;
                mejorOpcion = aiElement;
            }
        }
        return mejorOpcion;
    }

    private static Element originalSuperAI(Game game) {
        Element rivalElement = game.getMainPlayer().getCurrentElement();
        var aiDeck = game.getEnemyPlayer().getActiveElements();

        if (rivalElement == null) {
            return originalElegirPeorOpcion(game);
        } else {
            Element mejorRemate = null;
            int minValorGlobal = Integer.MAX_VALUE;
            for (Element aiElement : aiDeck) {
                int dmg = rivalElement.getType().<Integer>accept(aiElement.getType());
                if (dmg >= rivalElement.getEnergy()) {
                    if (originalCalcularDmgTotal(game, aiElement) < minValorGlobal) {
                        minValorGlobal = originalCalcularDmgTotal(game, aiElement);
                        mejorRemate = aiElement;
                    }
                }
            }
            if (mejorRemate != null) {
                return mejorRemate;
            } else {
                int maxDmg = -1;
                Element mejorOpcion = null;
                int minGlobalValue = Integer.MAX_VALUE;
                for (Element aiElement : aiDeck) {
                    int dmg = rivalElement.getType().<Integer>accept(aiElement.getType());
                    if (dmg > maxDmg) {
                        maxDmg = dmg;
                        mejorOpcion = aiElement;
                        minGlobalValue = originalCalcularDmgTotal(game, aiElement);
                    } else if (dmg == maxDmg) {
                        int valorGlobal = originalCalcularDmgTotal(game, aiElement);
                        if (valorGlobal < minGlobalValue) {
                            minGlobalValue = valorGlobal;
                            mejorOpcion = aiElement;
                        }
                    }
                }
                return mejorOpcion;
            }
        }
    }

    private static Element originalElegirPeorOpcion(Game game) {
        Element peorOpcion = null;
        int minDmg = Integer.MAX_VALUE;
        for (Element aiElement : game.getEnemyPlayer().getActiveElements()) {
            int localDmgTotal = originalCalcularDmgTotal(game, aiElement);
            if (localDmgTotal < minDmg) {
                minDmg = localDmgTotal;
                peorOpcion = aiElement;
            }
        }
        return peorOpcion;
    }

    private static int originalCalcularDmgTotal(Game game, Element aiElement) {
        int dmgTotal = 0;
        for (Element playerElement : game.getMainPlayer().getActiveElements()) {
            int dmgHecho = playerElement.getType().<Integer>accept(aiElement.getType());
            int dmgRecibido = aiElement.getType().<Integer>accept(playerElement.getType());
            dmgTotal += dmgHecho - dmgRecibido;
        }
        return dmgTotal;
    }
}
