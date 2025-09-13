package net.wiicart.commands;

import net.wiicart.commands.command.CartCommandExecutor;
import net.wiicart.commands.command.CommandData;
import net.wiicart.commands.command.tree.CommandTree;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class WorkbenchTest extends CommandTree {

    @Test
    public void test() {
        this.onCommand(null, null, null, new String[]{"hi"});
        this.onCommand(null, null, null, new String[]{"execute"});
    }

    public WorkbenchTest() {
        super(builder()
                .workbench(bench -> {
                    bench.store("executor", Executor::new);
                    bench.store("ten", () -> 10);
                    bench.store("hi", () -> "Hi");
                })
                .run(builder -> {
                    System.out.println(builder.getWorkbench().retrieve(String.class, "hi"));
                    System.out.println(builder.getWorkbench().retrieve(Integer.class, "ten"));
                })
                .withChild("execute", builder -> builder.executes("executor"))
                .executes(data -> System.out.println("Root called"))
                .build()
        );
    }

    static class Executor implements CartCommandExecutor {

        Executor() {
            System.out.println("Executor constructed");
        }

        @Override
        public void onCommand(@NotNull CommandData data) {
            System.out.println("Executor reached");
        }

    }
}
