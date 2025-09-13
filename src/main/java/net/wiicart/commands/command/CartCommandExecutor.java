package net.wiicart.commands.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface CartCommandExecutor extends CommandExecutor {

    CartCommandExecutor EMPTY = d -> {};

    void onCommand(@NotNull CommandData data);

    default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        onCommand(new CommandData(args, sender, label, command));
        return true;
    }

}
