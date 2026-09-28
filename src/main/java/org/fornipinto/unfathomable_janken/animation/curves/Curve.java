package org.fornipinto.unfathomable_janken.animation.curves;

/**
 * An abstract class that represents a Bézier curve.
 */
public abstract class Curve {
    /**
     * A cubic animation curve that starts slowly, speeds up, and then ends slowly.
     */
    public static final Curve EASE_IN_OUT = new CubicCurve(0.42, 0.0, 0.58, 1.0);

    /**
     * A cubic animation curve that starts slowly and then speeds up.
     */
    public static final Curve EASE_IN = new CubicCurve(0.42, 0.0, 1.0, 1.0);

    /**
     * A cubic animation curve that starts quickly and then slows down.
     */
    public static final Curve EASE_OUT = new CubicCurve(0.0, 0.0, 0.58, 1.0);

    /**
     * Transforms the given value t (0.0 to 1.0) according to the curve.
     *
     * @param t A value between 0.0 and 1.0 that represents the progress of the animation.
     * @return The transformed value according to the curve.
     */
    public final double transform(double t) {
        if (t == 0.0 || t == 1.0) {
            return t;
        } else {
            return transformInternal(t);
        }
    }

    abstract double transformInternal(double t);
}

