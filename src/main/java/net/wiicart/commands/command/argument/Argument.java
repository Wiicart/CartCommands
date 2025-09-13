package net.wiicart.commands.command.argument;

import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public interface Argument extends TabCompleter {
    @Contract("_ -> new")
    static @NotNull ArgumentSequence sequence(Argument... arguments) {
        return new ArgumentSequence(arguments);
    }

    /**
     * Provides the expected argument type
     * @return The Class of the type
     */
    @NotNull Class<?> type();

}
