package net.wiicart.commands.command.tree;

import net.wiicart.commands.command.CartCommandExecutor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Class used to construct a {@link CommandTree}
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public final class TreeBuilder {

    private String name;

    private final Workbench bench;

    private final Set<String> aliases = new HashSet<>();

    private final Map<String, Consumer<TreeBuilder>> children = new HashMap<>();

    private CartCommandExecutor executor = CartCommandExecutor.EMPTY;

    TreeBuilder() {
        this("ROOT", new Workbench());
    }

    private TreeBuilder(@NotNull String name, @NotNull Workbench bench) {
        this.name = name;
        this.bench = bench;
    }

    /**
     * Provides a consumer to access the {@link Workbench} instance.
     * The Workbench can be used to run code and store/retrieve Objects while building a CommandTree.
     * @param consumer The consumer
     * @return this
     */
    @NotNull
    public TreeBuilder workbench(@NotNull Consumer<Workbench> consumer) {
        consumer.accept(bench);
        return this;
    }

    /**
     * Provides a way to modify this TreeBuilder via a Consumer.
     * @param consumer The Consumer
     * @return this
     */
    public TreeBuilder run(@NotNull Consumer<TreeBuilder> consumer) {
        consumer.accept(this);
        return this;
    }

    /**
     * Provides the {@link Workbench} instance, which can be used to run code and store/retrieve Objects.<br/>
     * This instance persists through TreeBuilders for children via {@link TreeBuilder#withChild(String, Consumer)}.
     * @return The Workbench instance
     */
    @NotNull
    public Workbench getWorkbench() {
        return bench;
    }

    /**
     * Adds a child to the current Node.
     * @param name The name of the child
     * @param builder The builder of the child, so you can modify it.
     * @return this
     */
    public TreeBuilder withChild(@NotNull String name, @NotNull Consumer<TreeBuilder> builder) {
        children.put(name.toLowerCase(Locale.ROOT), builder);
        return this;
    }

    /**
     * Adds what code is executed when this Node is triggered
     * @param executor The CartCommandExecutor implementation
     * @return this
     */
    public TreeBuilder executes(@NotNull CartCommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Sets the executor based off the result of {@link Workbench#retrieve(Class, String)}.
     * @param name The name of the stored object to retrieve.
     * @throws ClassCastException If the provided object does not implement {@link CartCommandExecutor}
     * @return this
     */
    public TreeBuilder executes(@NotNull String name) {
        this.executor = bench.retrieve(CartCommandExecutor.class, name);
        return this;
    }

    /**
     * Changes the name of this node.
     * If this node is the {@code ROOT} Node, nothing will change.
     * @param name The new name of the Node
     * @return this
     */
    public TreeBuilder named(@NotNull String name) {
        if (!this.name.equals("ROOT")) {
            this.name = name;
        }

        return this;
    }

    /**
     * Adds aliases to the Node.
     * @param aliases All aliases
     * @return this
     */
    public TreeBuilder withAliases(@NotNull String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    /**
     * Constructs a new Node based off all the values of this <code>TreeBuilder</code>.
     * @return A new Node.
     */
    @Contract("-> new")
    public @NotNull CommandTree.Node build() {

        Set<CommandTree.Node> constructedChildren = new HashSet<>();
        for(Map.Entry<String, Consumer<TreeBuilder>> entry : children.entrySet()) {
            String childName = entry.getKey();
            Consumer<TreeBuilder> consumer = entry.getValue();

            TreeBuilder child = new TreeBuilder(childName, bench);
            consumer.accept(child);
            constructedChildren.add(child.build());
        }

        return new CommandNodeImpl(
                name,
                executor,
                Set.of(),
                aliases,
                constructedChildren.toArray(new CommandTree.Node[0])
        );
    }

}
