package net.wiicart.commands.command.argument;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;

@SuppressWarnings("ClassCanBeRecord")
public class ArgumentSequence implements Iterable<Argument> {

    public static final ArgumentSequence EMPTY = new ArgumentSequence();

    private final Argument[] arguments;

    public ArgumentSequence(Argument... arguments) {
        this.arguments = arguments;
    }

    public @Nullable Argument get(int index) {
        if (arguments.length >= index) {
            return arguments[index];
        } else {
            return null;
        }
    }

    public int size() {
        return arguments.length;
    }

    public Argument[] arguments() {
        return Arrays.copyOf(arguments, arguments.length);
    }

    public boolean isBlank() {
        for (Argument argument : arguments) {
            if (argument != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public @NotNull Iterator<Argument> iterator() {
        return Arrays.stream(arguments()).iterator();
    }
}
