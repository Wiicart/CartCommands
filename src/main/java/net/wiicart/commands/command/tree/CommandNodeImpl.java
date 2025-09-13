package net.wiicart.commands.command.tree;

import net.wiicart.commands.command.CartCommandExecutor;
import net.wiicart.commands.command.CommandData;
import net.wiicart.commands.command.argument.ArgumentSequence;
import static org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

final class CommandNodeImpl implements CommandTree.Node {

    private final String name;

    private final Set<CommandTree.Node> children;

    private final CartCommandExecutor executor;

    private final Set<ArgumentSequence> arguments;

    private final Set<String> aliases;

    CommandNodeImpl(
            @NotNull String name,
            @NotNull CartCommandExecutor executor,
            @NotNull Set<ArgumentSequence> arguments,
            @NotNull Set<String> aliases,
            @NotNull CommandTree.Node... children
    ) {
        this.name = name;
        this.executor = executor;
        this.arguments = Set.copyOf(arguments);
        this.aliases = Set.copyOf(aliases);
        this.children = new HashSet<>(Arrays.asList(children));
    }

    @Override
    public void onCommand(@NotNull CommandData data) {
        executor.onCommand(data);
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
    @Experimental
    public @NotNull Set<ArgumentSequence> arguments() {
        return arguments;
    }

    @Override
    public @NotNull Set<String> aliases() {
        return aliases;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

}
