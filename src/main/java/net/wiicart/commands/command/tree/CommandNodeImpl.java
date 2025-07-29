package net.wiicart.commands.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class CommandNodeImpl implements CommandTree.Node {

    private final String name;

    private final CommandTree.Node parent;

    private final Set<CommandTree.Node> children;

    private final CartCommandExecutor executor;

    CommandNodeImpl(@NotNull String name, CartCommandExecutor executor, CommandTree.Node... children) {
        this.name = name;
        parent = null;
        this.executor = executor;
        this.children = new HashSet<>(Arrays.asList(children));
    }

    CommandNodeImpl(@NotNull String name, @Nullable CommandTree.Node parent, CartCommandExecutor executor, CommandTree.Node... children) {
        this.name = name;
        this.parent = parent;
        this.executor = executor;
        this.children = new HashSet<>(Arrays.asList(children));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }

    @Override
    @NotNull
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }

    @Override
    public void onCommand(CommandSender sender, String[] args, String label) {
        executor.onCommand(sender, args, label);
    }


    @Override
    public @NotNull String name() {
        return name;
    }

    @Override
    public @NotNull Set<CommandTree.Node> children() {
        return new HashSet<>(children);
    }

    @Override
    public void addChild(@NotNull CommandTree.Node child) {
        children.add(child);
    }

    @Override
    @Nullable
    public CommandTree.Node parent() {
        return parent;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }
}
