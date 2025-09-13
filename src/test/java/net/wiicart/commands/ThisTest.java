package net.wiicart.commands;

import net.wiicart.commands.command.CartCommandExecutor;
import net.wiicart.commands.command.CommandData;
import net.wiicart.commands.command.tree.CommandTree;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class ThisTest extends CommandTree implements CartCommandExecutor {

    @Test
    public void test() {
        onCommand(null, null, null, new String[]{"hi", "testing"});
        onCommand(null, null, null, new String[]{"test"});
    }

    public ThisTest() {
        super(THIS, b -> b.withChild("hi", b1 -> b1.executes(new TestChild())));
    }


    @Override
    public void onCommand(@NotNull CommandData data) {
        assert data.args()[0].equalsIgnoreCase("test");
    }

    private static class TestChild implements CartCommandExecutor {

        @Override
        public void onCommand(@NotNull CommandData data) {
            assert data.args()[0].equals("testing");
        }

    }
}
