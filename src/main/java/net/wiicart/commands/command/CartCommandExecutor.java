package net.wiicart.commands.command;

import net.wiicart.commands.function.TriConsumer;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CartExecutor extends TriConsumer<CommandSender, String[], String> {

    static CartExecutor EMPTY = (s, a, l) -> {};

    default void accept(CommandSender sender, String[] args, String label) {
        onCommand(sender, args, label);
    }

    void onCommand(CommandSender sender, String[] args, String label);

}
