package net.wiicart.commands;

import net.wiicart.commands.command.tree.CommandExecutionException;
import net.wiicart.commands.command.tree.CommandTree;
import org.junit.Test;

/*
Tests that CommandTrees are constructed correctly and that the CommandExecutionException path accurately
reflects the expected path

A tree is built that is ROOT/join/silent, so the same should be expected in the Exception's path.
 */
public class PathTest extends CommandTree {

    @Test
    public void init() {
        PathTest test = new PathTest();
        try {
            test.onCommand(null, null, null, new String[]{"join", "silent"});
        } catch (Exception e) {
            assert e instanceof CommandExecutionException;
            assert ((CommandExecutionException) e).getPath().equalsIgnoreCase("ROOT/join/silent");
        }
    }

    public PathTest() {
        super(CommandTree.builder()
                .withChild("join", b ->
                        b.withChild(
                        "silent",
                        c -> c.executes((data -> {
                            throw new RuntimeException("test");
                        }))
                )).build()
        );
    }
}
