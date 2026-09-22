package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.core.Size;
import org.fornipinto.unfathomable_janken.ui.core.*;

import java.util.Arrays;
import java.util.Objects;

/**
 * A general-purpose layout component that arranges its children linearly,
 * either horizontally (like a Row) or vertically (like a Column).
 * <p>
 * Use {@link Row} or {@link Column} for convenience.
 */
public class LinearLayout extends Component {
    protected final Component[] children;
    protected final Orientation orientation;
    private MainAxisSize mainAxisSize = MainAxisSize.MAX;
    private CrossAxisAlignment crossAxisAlignment = CrossAxisAlignment.START;

    /**
     * Constructs a LinearLayout with the specified orientation and items.
     *
     * @param orientation The orientation (horizontal or vertical).
     * @param children    The children as Fixed or Flexible items.
     */
    public LinearLayout(Orientation orientation, Component... children) {
        this.orientation = Objects.requireNonNull(orientation);
        this.children = Arrays.stream(Objects.requireNonNull(children)).filter(Objects::nonNull).toArray(Component[]::new);
    }

    /**
     * Sets how much space the layout should occupy in the main axis.
     *
     * @param mainAxisSize The main axis size to set.
     * @return This layout instance.
     */
    public LinearLayout mainAxisSize(MainAxisSize mainAxisSize) {
        this.mainAxisSize = Objects.requireNonNullElse(mainAxisSize, MainAxisSize.MAX);
        return this;
    }

    /**
     * Sets the cross-axis alignment for this layout.
     *
     * @param crossAxisAlignment The cross-axis alignment to set.
     * @return This layout instance.
     */
    public LinearLayout crossAxisAlignment(CrossAxisAlignment crossAxisAlignment) {
        this.crossAxisAlignment = Objects.requireNonNullElse(crossAxisAlignment, CrossAxisAlignment.START);
        return this;
    }

    @Override
    public void layout(Constraints constraints) {
        if (children.length == 0) {
            size = constraints.smallest();
            return;
        }

        final var isMainAxisBounded = orientation == Orientation.HORIZONTAL
            ? constraints.hasBoundedWidth()
            : constraints.hasBoundedHeight();

        final var mainAxisMaxLength = orientation == Orientation.HORIZONTAL
            ? constraints.maxWidth()
            : constraints.maxHeight();

        final var isCrossAxisBounded = orientation == Orientation.HORIZONTAL
            ? constraints.hasBoundedHeight()
            : constraints.hasBoundedWidth();

        final var crossAxisMaxLength = orientation == Orientation.HORIZONTAL
            ? constraints.maxHeight()
            : constraints.maxWidth();

        final var crossAxisMinChildLength = (crossAxisAlignment == CrossAxisAlignment.STRETCH && isCrossAxisBounded) ? crossAxisMaxLength : 0;
        final var crossAxisMaxChildLength = isCrossAxisBounded ? crossAxisMaxLength : null;

         var allocatedMainAxisLength = 0;
         var maxChildCrossAxisLength = 0;
         var totalFlex = 0;

        for (Component child : children) {
            final var data = child.data();

            if (data instanceof FlexibleData(int flex)) {
                totalFlex = totalFlex + flex;
            } else {
                final Constraints childConstraints = switch (orientation) {
                    case HORIZONTAL -> new Constraints(
                        0,
                        null,
                        crossAxisMinChildLength,
                        crossAxisMaxChildLength
                    );
                    case VERTICAL -> new Constraints(
                        crossAxisMinChildLength,
                        crossAxisMaxChildLength,
                        0,
                        null
                    );
                };

                child.layout(childConstraints);

                allocatedMainAxisLength = allocatedMainAxisLength + getChildMainAxisLength(child);
                maxChildCrossAxisLength = Math.max(maxChildCrossAxisLength, getChildCrossAxisLength(child));
            }
        }

        if (totalFlex > 0) {
            if (!isMainAxisBounded) {
                throw new IllegalStateException("LinearLayout children have non-zero flex but incoming constraints are unbounded.");
            }

            var remainingFreeSpace = Math.max(0, mainAxisMaxLength - allocatedMainAxisLength);
            var remainingFlex = totalFlex;

            for (Component child : children) {
                final var data = child.data();

                if (data instanceof FlexibleData(int flex)) {
                    final var length = Math.round((flex / (float) remainingFlex) * remainingFreeSpace);
                    remainingFreeSpace = remainingFreeSpace - length;
                    remainingFlex = remainingFlex - flex;

                    final var childConstraints = switch (orientation) {
                        case HORIZONTAL -> new Constraints(
                            length,
                            length,
                            crossAxisMinChildLength,
                            crossAxisMaxChildLength
                        );
                        case VERTICAL -> new Constraints(
                            crossAxisMinChildLength,
                            crossAxisMaxChildLength,
                            length,
                            length
                        );
                    };

                    child.layout(childConstraints);

                    allocatedMainAxisLength = allocatedMainAxisLength + getChildMainAxisLength(child);
                    maxChildCrossAxisLength = Math.max(maxChildCrossAxisLength, getChildCrossAxisLength(child));
                }
            }
        }

        final var finalMainAxisLength = switch (this.mainAxisSize) {
            case MIN -> allocatedMainAxisLength;
            case MAX -> isMainAxisBounded ? mainAxisMaxLength : allocatedMainAxisLength;
        };

        final var finalCrossAxisLength = (crossAxisAlignment == CrossAxisAlignment.STRETCH && isCrossAxisBounded)
            ? crossAxisMaxLength
            : maxChildCrossAxisLength;

        final var naturalSize = switch (orientation) {
            case HORIZONTAL -> new Size(finalMainAxisLength, finalCrossAxisLength);
            case VERTICAL -> new Size(finalCrossAxisLength, finalMainAxisLength);
        };

        size = constraints.constrain(naturalSize);
    }

    @Override
    public void draw(Canvas canvas) {
        if (children.length == 0) return;

        int mainAxisPosition = 0;

        for (Component child : children) {
            final int crossAxisPosition = switch (crossAxisAlignment) {
                case START, STRETCH -> 0;
                case CENTER -> crossAxisLength() / 2 - getChildCrossAxisLength(child) / 2;
                case END -> crossAxisLength() - getChildCrossAxisLength(child);
            };

            switch (orientation) {
                case HORIZONTAL -> {
                    canvas.draw(child, mainAxisPosition, crossAxisPosition);
                    mainAxisPosition += child.size().width();
                }
                case VERTICAL -> {
                    canvas.draw(child, crossAxisPosition, mainAxisPosition);
                    mainAxisPosition += child.size().height();
                }
            }
        }
    }

    private int crossAxisLength() {
        return switch (orientation) {
            case HORIZONTAL -> size.height();
            case VERTICAL -> size.width();
        };
    }

    private int getChildMainAxisLength(Component component) {
        return switch (orientation) {
            case HORIZONTAL -> component.size().width();
            case VERTICAL -> component.size().height();
        };
    }

    private int getChildCrossAxisLength(Component component) {
        return switch (orientation) {
            case HORIZONTAL -> component.size().height();
            case VERTICAL -> component.size().width();
        };
    }

    /**
     * The orientation of the layout.
     * <p>
     * Horizontal layouts arrange children in a row, while vertical layouts arrange children in a column.
     */
    public enum Orientation {
        /**
         * Horizontal orientation (like a Row).
         */
        HORIZONTAL,

        /**
         * Vertical orientation (like a Column).
         */
        VERTICAL
    }
}

