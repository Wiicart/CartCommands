package net.wiicart.commands.command.tree;

import net.wiicart.commands.command.CartCommandExecutor;
import net.wiicart.commands.command.CommandNodeImpl;
import net.wiicart.commands.command.argument.ArgumentSequence;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class CommandTreeBuilder {

    private String name;

    private ArgumentSequence arguments;

    private final @Nullable CommandTree.Node parent;

    private final Map<String, Consumer<CommandTreeBuilder>> children = new HashMap<>();

    private CartCommandExecutor executor = CartCommandExecutor.EMPTY;

    CommandTreeBuilder(@NotNull String name) {
        this.name = name;
        parent = null;
    }

    CommandTreeBuilder(@NotNull String name, @NotNull CommandTree.Node parent) {
        this.name = name;
        this.parent = parent;
    }

    public CommandTreeBuilder withChild(@NotNull String name, @NotNull Consumer<CommandTreeBuilder> builder) {
        children.put(name, builder);
        return this;
    }

    public CommandTreeBuilder executes(@NotNull CartCommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    public CommandTreeBuilder named(@NotNull String name) {
        this.name = name;
        return this;
    }

    @Contract("-> new")
    public @NotNull CommandTree.Node build() {
        CommandTree.Node node = new CommandNodeImpl(name, parent, executor);

        for(Map.Entry<String, Consumer<CommandTreeBuilder>> entry : children.entrySet()) {
            String childName = entry.getKey();
            Consumer<CommandTreeBuilder> consumer = entry.getValue();

            CommandTreeBuilder child = new CommandTreeBuilder(childName, node);
            consumer.accept(child);
            node.addChild(child.build());
        }

        return node;
    }

}
