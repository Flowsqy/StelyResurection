package fr.stelycube.stelyresurrection;

import fr.stelycube.stelyresurrection.commands.Commands;
import fr.stelycube.stelyresurrection.configs.MessageConfig;
import fr.stelycube.stelyresurrection.events.Events;
import fr.stelycube.stelyresurrection.respawns.RespawnManager;
import org.bukkit.plugin.java.JavaPlugin;

public class StelyResurrectionPlugin extends JavaPlugin {
   private static StelyResurrectionPlugin instance;
   private static MessageConfig message;

   public void onEnable() {
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
