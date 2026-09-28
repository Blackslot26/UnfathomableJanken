package org.fornipinto.unfathomable_janken.game.ai;

import org.fornipinto.unfathomable_janken.game.element.Element;

import org.fornipinto.unfathomable_janken.game.Game;

class ChooseElementVisitor implements AIVisitor<Element> {
    private final Game game;

    public ChooseElementVisitor(Game game) {
        this.game = game;
    }

    @Override
    public Element visit(RandomAI ai) {
        var activeElements = game.getEnemyPlayer().getActiveElements();
        if (activeElements != null && !activeElements.isEmpty()) {
            return activeElements.get((int) (Math.random() * activeElements.size()));
        }
        return null;
    }

    @Override
    public Element visit(StrategicAI ai) {
        if (game.getMainPlayer().getCurrentElement() == null) {
            return this.visit(new RandomAI());
        }
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

    @Override
    public Element visit(SuperAI ai) {
        Element rivalElement = game.getMainPlayer().getCurrentElement();
        var aiDeck = game.getEnemyPlayer().getActiveElements();
        if(rivalElement == null) {
            return elegirPeorOpcion();
        } else {
            Element mejorRemate = null;
            int minValorGlobal = Integer.MAX_VALUE;
            for (Element aiElement : aiDeck) {
                int dmg = rivalElement.getType().<Integer>accept(aiElement.getType());
                if (dmg >= rivalElement.getEnergy()) {
                    if (calcularDmgTotal(aiElement) < minValorGlobal) {
                        minValorGlobal = calcularDmgTotal(aiElement);
                        mejorRemate = aiElement;
                    }
                }
            }
            if(mejorRemate != null) {
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
                        minGlobalValue = calcularDmgTotal(aiElement);
                    } else if (dmg == maxDmg) {
                        int valorGlobal = calcularDmgTotal(aiElement);
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

    private Element elegirPeorOpcion() {
        Element peorOpcion = null;
        int minDmg = Integer.MAX_VALUE;
        for (Element aiElement : game.getEnemyPlayer().getActiveElements()) {
            int localDmgTotal = calcularDmgTotal(aiElement);
            if (localDmgTotal < minDmg) {
                minDmg = localDmgTotal;
                peorOpcion = aiElement;
            }
        }
        return peorOpcion;
    }

    private int calcularDmgTotal(Element aiElement) {
        int dmgTotal = 0;
        for (Element playerElement : game.getMainPlayer().getActiveElements()) {
            int dmgHecho = playerElement.getType().<Integer>accept(aiElement.getType());
            int dmgRecibido = aiElement.getType().<Integer>accept(playerElement.getType());
            dmgTotal += dmgHecho - dmgRecibido;
        }
        return dmgTotal;
    }
}