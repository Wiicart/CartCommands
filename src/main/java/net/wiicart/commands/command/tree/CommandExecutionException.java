package net.wiicart.commands.command.tree;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A RuntimeException wrapper that adds {@link CommandExecutionException#getPath()}
 * for additional debugging.
 */
public class CommandExecutionException extends RuntimeException {

    private final Deque<String> stack = new ArrayDeque<>();

    CommandExecutionException(@NotNull Throwable cause) {
        super(cause);
    }

    /**
     * Provides the path the command took through the tree,
     * providing the names of the {@link CommandTree.Node}(s) involved.
     *
     * @return A String outlining the path.
     */
    public String getPath() {
        return String.join("/", stack);
    }

    CommandExecutionException addNode(String node) {
        stack.push(node);
        return this;
    }

}
