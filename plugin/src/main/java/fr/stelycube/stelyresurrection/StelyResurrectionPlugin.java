package fr.stelycube.stelyresurrection;

import fr.stelycube.stelyresurrection.commands.Commands;
import fr.stelycube.stelyresurrection.config.ConfigLoader;
import fr.stelycube.stelyresurrection.configs.MessageConfig;
import fr.stelycube.stelyresurrection.events.Events;
import fr.stelycube.stelyresurrection.respawns.RespawnManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StelyResurrectionPlugin extends JavaPlugin {
    private static StelyResurrectionPlugin instance;
    private static MessageConfig message;

    public void onEnable() {
        final Logger logger = getLogger();
        final File dataFolder = getDataFolder();
        final ConfigLoader configLoader = new ConfigLoader();

        if (!configLoader.checkDataFolder(dataFolder)) {
            logger.log(Level.WARNING, "Can not write in the directory : " + dataFolder.getAbsolutePath());
            logger.log(Level.WARNING, "Disable the plugin");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        message = new MessageConfig("message.yml");
        RespawnManager.load();
        Events.load();
        Commands.load();
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }

    public static final StelyResurrectionPlugin getInstance() {
        return instance;
    }

    public static final MessageConfig getMessage() {
        return message;
    }
}
