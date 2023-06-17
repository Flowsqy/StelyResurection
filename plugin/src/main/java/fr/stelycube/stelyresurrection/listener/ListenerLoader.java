package fr.stelycube.stelyresurrection.listener;

import fr.stelycube.stelyresurrection.StelyResurrectionPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class ListenerLoader {

    public void load(@NotNull StelyResurrectionPlugin plugin) {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new DeathListener(), plugin);
        pluginManager.registerEvents(new RespawnListener(), plugin);
    }

}
