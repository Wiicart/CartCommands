package net.wiicart.commands.command.tree;

import net.wiicart.commands.command.CartCommandExecutor;
import net.wiicart.commands.command.CommandData;
import net.wiicart.commands.command.argument.ArgumentSequence;
import net.wiicart.commands.tabcomplete.TabCompleteUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import static org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * The class most commands will extend to create a Command.
 * This stores the root node and logic for passing a Command down the tree.
 */
@SuppressWarnings("unused")
public abstract class CommandTree implements CommandExecutor, TabCompleter {

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
     * Protected constructor for subclasses.
     * Use {@link CommandTree#builder()} to build the Tree.
     * @param root The root node.
     */
    protected CommandTree(@NotNull Node root) {
        this.root = root;
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
}
