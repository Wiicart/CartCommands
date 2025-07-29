package net.wiicart.commands.command;

@FunctionalInterface
public interface CartCommandExecutor {

    CartCommandExecutor EMPTY = d -> {};

    void onCommand(CommandData data);

}
