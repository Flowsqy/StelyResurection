package fr.stelycube.stelyresurrection.command;

import fr.stelycube.stelyresurrection.respawnpoint.RespawnPoint;
import fr.stelycube.stelyresurrection.respawnpoint.RespawnPointManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DelResCommand implements TabExecutor {

    private final RespawnPointManager respawnPointManager;
    private final String helpMessage;
    private final String invalidNameMessage;
    private final String successMessage;

    public DelResCommand(@NotNull RespawnPointManager respawnPointManager, @Nullable String helpMessage, @Nullable String invalidNameMessage, @Nullable String successMessage) {
        this.respawnPointManager = respawnPointManager;
        this.helpMessage = helpMessage;
        this.invalidNameMessage = invalidNameMessage;
        this.successMessage = successMessage;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            if (helpMessage != null) {
                sender.sendMessage(helpMessage);
            }
            return true;
        }

        final String respawnPointName = args[0];
        final boolean success = respawnPointManager.delete(respawnPointName);
        if (!success) {
            if (invalidNameMessage != null) {
                sender.sendMessage(invalidNameMessage.replace("%name%", respawnPointName));
            }
            return true;
        }

        if (successMessage != null) {
            sender.sendMessage(successMessage.replace("%name%", respawnPointName));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        final String arg = args[0];
        final List<String> completions = new LinkedList<>();
        for (RespawnPoint respawnPoint : respawnPointManager.getRegisteredPoints()) {
            final String respawnPointName = respawnPoint.name();
            if (!respawnPointName.startsWith(arg)) {
                continue;
            }
            completions.add(respawnPointName);
        }
        return completions;
    }
}
