package org.fornipinto.unfathomable_janken.engine;

/**
 * A ticker that executes actions on each engine tick.
 */
public interface Ticker {
    /**
     * Called when the ticker is disposed and should clean up any resources.
     */
    void dispose();
}
