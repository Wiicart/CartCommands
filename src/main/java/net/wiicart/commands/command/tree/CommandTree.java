package net.wiicart.commands.command;

import net.wiicart.commands.command.argument.Argument;
import net.wiicart.commands.command.argument.ArgumentSequence;
import net.wiicart.commands.tabcomplete.TabCompleteUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class CommandTree implements CommandExecutor, TabCompleter {

    private final Node root;

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

        @NotNull
        static CommandTreeBuilder builder(@NotNull String name) {
            return new CommandTreeBuilder(name);
        }

        @NotNull
        static CommandTreeBuilder builder(@NotNull String name, @NotNull CommandTree.Node parent) {
            return new CommandTreeBuilder(name, parent);
        }

        /**
         * It is not recommended to override this in most cases, as it can break Tree logic.
         * Typically, {@link Node#onCommand(CommandSender, String[], String)} should be overridden instead.
         * @param sender Source of the command
         * @param command Command which was executed
         * @param label Alias of the command which was used
         * @param args Passed command arguments
         * @return true
         */
        default boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            for (Node node : children()) {
                if (node.name().equalsIgnoreCase(args[0])) {
                    return node.onCommand(
                            sender,
                            command,
                            label,
                            Arrays.copyOfRange(args, 1, args.length)
                    );
                }
            }

            this.onCommand(sender, args, label); // No child node is handling this, so this node must handle it.

            return true;
        }

        /**
         * It is not recommended to override this in most cases, as it can break Tree logic.
         * Tab completion should be implemented through {@link Argument}
         * @param sender Source of the command.  For players tab-completing a
         *     command inside of a command block, this will be the player, not
         *     the command block.
         * @param command Command which was executed
         * @param alias The alias used
         * @param args The arguments passed to the command, including final
         *     partial argument to be completed and command label
         * @return
         */
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
                if (args[1].equalsIgnoreCase(node.name())) {
                    return node.onTabComplete(
                            sender,
                            command,
                            alias,
                            Arrays.copyOfRange(args, 1, args.length)
                    );
                }
            }

            ArgumentSequence arguments = this.arguments();
            int index = 0;
            for (Argument argument : arguments) {
                if (index > args.length) {
                    return list;
                }

                List<String> tabArg = argument.onTabComplete(sender, command, alias, Arrays.copyOfRange(args, index, args.length));
                if (tabArg.isEmpty()) {
                    return TabCompleteUtil.EMPTY;
                } else {
                    list.addAll(tabArg);
                    index++;
                }
            }

            return list;
        }

        @NotNull String name();

        /**
         * Provides a Set containing all the Node's direct children.
         * @return A Set
         */
        @NotNull Set<Node> children();

        void addChild(@NotNull CommandTree.Node child);

        @Nullable CommandTree.Node parent();

        @NotNull ArgumentSequence arguments();

        boolean isLeaf();

    }
}
