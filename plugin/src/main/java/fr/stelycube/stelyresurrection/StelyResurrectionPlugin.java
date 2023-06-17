package fr.stelycube.stelyresurrection;

import fr.stelycube.stelyresurrection.command.CommandLoader;
import fr.stelycube.stelyresurrection.config.ConfigLoader;
import fr.stelycube.stelyresurrection.listener.ListenerLoader;
import fr.stelycube.stelyresurrection.respawnpoint.RespawnPointManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StelyResurrectionPlugin extends JavaPlugin {

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

        final RespawnPointManager respawnPointManager = new RespawnPointManager();

        final fr.stelycube.stelyresurrection.config.MessageConfig messageConfig = new fr.stelycube.stelyresurrection.config.MessageConfig();
        messageConfig.load(configLoader, this, "message.yml");
        messageConfig.loadPrefix();

        final ListenerLoader listenerLoader = new ListenerLoader();
        listenerLoader.load(this, respawnPointManager, messageConfig);

        final CommandLoader commandLoader = new CommandLoader();
        commandLoader.load(this, respawnPointManager, messageConfig);
    }

}
