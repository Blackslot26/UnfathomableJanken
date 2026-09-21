package org.fornipinto.unfathomable_janken.animation.curves;

/**
 * A linear animation curve that maps input directly to output.
 */
public final class LinearCurve extends Curve {
    @Override
    double transformInternal(double t) {
        return t;
    }
}
