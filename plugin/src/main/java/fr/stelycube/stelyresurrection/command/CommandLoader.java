package fr.stelycube.stelyresurrection.command;

import fr.stelycube.stelyresurrection.StelyResurrectionPlugin;
import fr.stelycube.stelyresurrection.config.MessageConfig;
import fr.stelycube.stelyresurrection.respawnpoint.RespawnPointManager;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommandLoader {

    public void load(@NotNull StelyResurrectionPlugin plugin, @NotNull RespawnPointManager respawnPointManager, @NotNull MessageConfig messageConfig) {
        final String permissionMessage = messageConfig.getMessage("util.noperm");

        final PluginCommand setResCommand = Objects.requireNonNull(plugin.getCommand("setres"));
        final SetResCommand setResExecutor = new SetResCommand();
        setResCommand.setExecutor(setResExecutor);
        setResCommand.setTabCompleter(setResExecutor);
        setResCommand.setPermissionMessage(permissionMessage);

        final PluginCommand delResCommand = Objects.requireNonNull(plugin.getCommand("delres"));
        final DelResCommand delResExecutor = new DelResCommand(
                respawnPointManager,
                messageConfig.getMessage("help.delres"),
                messageConfig.getMessage("util.invalidname"),
                messageConfig.getMessage("commands.delres")
        );
        delResCommand.setExecutor(delResExecutor);
        delResCommand.setTabCompleter(delResExecutor);
        delResCommand.setPermissionMessage(permissionMessage);

    }

}
