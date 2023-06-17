package fr.stelycube.stelyresurrection.listener;

import fr.stelycube.stelyresurrection.StelyResurrectionPlugin;
import fr.stelycube.stelyresurrection.config.MessageConfig;
import fr.stelycube.stelyresurrection.respawnpoint.RespawnPointManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class ListenerLoader {

    public void load(@NotNull StelyResurrectionPlugin plugin, @NotNull RespawnPointManager respawnPointManager, @NotNull MessageConfig messageConfig) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new DeathListener(messageConfig.getMessage("events.death")), plugin);
        pluginManager.registerEvents(new RespawnListener(messageConfig.getMessage("events.respawn"), respawnPointManager), plugin);
    }

}
