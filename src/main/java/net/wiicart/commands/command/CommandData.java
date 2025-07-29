package net.wiicart.commands.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public record CommandData(String[] args, CommandSender sender, String label, Command command) {

    @Override
    public boolean equals(Object object) {
        if(object == null || getClass() != object.getClass()) return false;
        CommandData that = (CommandData) object;
        return Objects.equals(label, that.label) && Objects.deepEquals(args, that.args) && Objects.equals(command, that.command) && Objects.equals(sender, that.sender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(args), sender, label, command);
    }

    @Override
    public @NotNull String toString() {
        return "CommandData@" + Integer.toHexString(hashCode());
    }
}
