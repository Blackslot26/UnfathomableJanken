package org.fornipinto.unfathomable_janken.ui.components;

import org.fornipinto.unfathomable_janken.ui.core.Component;
import org.fornipinto.unfathomable_janken.ui.core.Composent;
import org.fornipinto.unfathomable_janken.ui.core.Constraints;
import org.fornipinto.unfathomable_janken.ui.core.Paint;

import java.util.List;
import java.util.function.Function;

/**
 * A component that allows the user to select an option from a list of options.
 *
 * @param <T> The type of the options in the selector
 */
public class Selector<T> extends Composent {
    final private List<T> options;
    final private T selected;

    final private Function<T, String> optionToString;
    final private List<String> labels;
    final private int labelsMaxWidth;

    /**
     * Constructs a new Selector component with the given options and selected option.
     *
     * @param options  The list of options
     * @param selected The selected option
     */
    public Selector(List<T> options, T selected) {
        this(options, selected, Object::toString);
    }

    /**
     * Constructs a new Selector component with the given options, selected option, and a function to convert options to strings.
     *
     * @param options        The list of options
     * @param selected       The selected option
     * @param optionToString The function to convert options to strings
     */
    public Selector(List<T> options, T selected, Function<T, String> optionToString) {
        this.options = options;
        this.selected = selected;
        this.optionToString = optionToString;
        this.labels = options.stream().map(optionToString).toList();
        this.labelsMaxWidth = labels.stream().mapToInt(String::length).max().orElse(1);
    }

    @Override
    public Component build() {
        final var isFirst = selected == options.getFirst();
        final var isLast = selected == options.getLast();
        final var width = labelsMaxWidth + 4;

        return new ConstrainedBox(
            Constraints.tightWidth(width),
            new Row(
                new Text("◀", isFirst ? Paint.DIM : new Paint()),
                new Flexible(1, new SizedBox(1, 0)),
                new Text(optionToString.apply(selected)),
                new Flexible(1, new SizedBox(1, 0)),
                new Text("▶", isLast ? Paint.DIM : new Paint())
            )
        );
    }
}
