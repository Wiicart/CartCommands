package net.wiicart.commands.command.tree;

import net.wiicart.commands.command.CartCommandExecutor;
import net.wiicart.commands.command.CommandData;
import net.wiicart.commands.command.argument.ArgumentSequence;
import net.wiicart.commands.tabcomplete.TabCompleteUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import static org.jetbrains.annotations.ApiStatus.*;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * The class most commands will extend to create a Command.
 * This stores the root node and logic for passing a Command down the tree.
 */
@SuppressWarnings("unused")
public class CommandTree implements CommandExecutor, TabCompleter {

    /**
     * Passed into a constructor to signify the CartCommandExecutor is the class itself.
     * Gets around the issue of passing "this" into super-class constructor.
     */
    protected static final This THIS = new This();

    private final Node root;

    /**
     * Provides a new {@link TreeBuilder} instance.
     * @return A new <code>TreeBuilder</code>.
     */
    @NotNull
    public static TreeBuilder builder() {
        return new TreeBuilder();
    }

    /**
     * Builds a CommandTree from a single CartCommandExecutor.
     * @param executor The executor
     * @return A new CommandTree
     */
    @Contract("_ -> new")
    public static @NotNull CommandTree from(@NotNull CartCommandExecutor executor) {
        return new CommandTree(builder().executes(executor).build());
    }

    @NotNull
    @Contract("_ -> new")
    public static CommandTree single(@NotNull CartCommandExecutor executor) {
        return new CommandTree(builder().executes(executor).build());
    }

    /**
     * Primary constructor.
     * Use {@link CommandTree#builder()} to build the Tree.
     * @param root The root node.
     */
    public CommandTree(@NotNull Node root) {
        this.root = root;
    }

    /**
     * A constructor that provides a builder, so that the root's executor can be this class.
     * If you are using this constructor, your class MUST implement {@link CartCommandExecutor}
     * @param marker The static final {@link CommandTree.This} instance
     * @param b A consumer providing the builder.
     */
    protected CommandTree(@NotNull This marker, @NotNull Consumer<TreeBuilder> b) {
        if (this instanceof CartCommandExecutor ex) {
            TreeBuilder builder = builder();
            builder.executes(ex);
            b.accept(builder);
            this.root = builder.build();
        } else {
            throw new ClassCastException("CommandTree must implement CartCommandExecutor when using the \"THIS\" constructor.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        root.onCommand(sender, command, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return root.onTabComplete(sender, command, alias, args);
    }


    public interface Node extends CommandExecutor, TabCompleter, CartCommandExecutor {

        /**
         * It is not recommended to override this in most cases, as it can break Tree logic.
         * Implement Node#onCommand(CommandData)
         * @param sender Source of the command
         * @param command Command which was executed
         * @param label Alias of the command which was used
         * @param args Passed command arguments
         * @return true
         */
        default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            try {
                if (args.length > 0) {
                    for (Node node : children()) {
                        if (node.matches(args[0])) {
                            return node.onCommand(
                                    sender,
                                    command,
                                    label,
                                    Arrays.copyOfRange(args, 1, args.length)
                            );
                        }
                    }
                }

                // No child node is handling this, so this node must handle it.
                this.onCommand(new CommandData(args, sender, label, command));
            } catch (Exception e) {
                if (e instanceof CommandExecutionException ex) {
                    throw ex.addNode(this.name());
                } else {
                    throw new CommandExecutionException(e).addNode(this.name());
                }
            }

            return true;
        }


        default List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            List<String> list = new ArrayList<>();
            list.add(this.name());

            if (args.length == 0) {
                return list;
            }
            if (args.length == 1) {
                return TabCompleteUtil.filter(list, args[0]);
            }

            // Pass on to child nodes if appropriate
            for (Node node : children()) {
                if (node.matches(args[1])) {
                    return node.onTabComplete(
                            sender,
                            command,
                            alias,
                            Arrays.copyOfRange(args, 1, args.length)
                    );
                }
            }

            return list;
        }

        default boolean matches(@NotNull String arg) {
            for (String str : aliases()) {
                if (str.equalsIgnoreCase(arg)) {
                    return true;
                }
            }

            return name().equalsIgnoreCase(arg);
        }

        /**
         * The primary name of the Node and how its mainly reached.
         * This name should be fully lowercased unless it's the root node,
         * in which case it is all-caps "ROOT".
         * @return The Node's name
         */
        @NotNull String name();

        /**
         * Provides a Set containing all the Node's direct children.
         * @return A Set
         */
        @NotNull Set<Node> children();

        @Experimental
        @NotNull Set<ArgumentSequence> arguments();

        @NotNull Set<String> aliases();

        boolean isLeaf();

    }

    protected static class This {
        private This() {}
    }
}
