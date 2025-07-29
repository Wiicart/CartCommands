package net.wiicart.commands.command.argument;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

class ArgumentImpl implements Argument {

    private final Class<?> type;

    private final TabCompleter tabCompleter;

    ArgumentImpl(Class<?> type, TabCompleter tabCompleter) {
        this.type = type;
        this.tabCompleter = tabCompleter;
    }

    @Override
    public @NotNull Class<?> type() {
        return type;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return tabCompleter.onTabComplete(sender, command, alias, args);
    }

}
