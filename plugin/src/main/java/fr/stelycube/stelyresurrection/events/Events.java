package fr.stelycube.stelyresurrection.events;

import fr.stelycube.stelyresurrection.StelyResurrectionPlugin;
import fr.stelycube.stelyresurrection.configs.MessageConfig;
import fr.stelycube.stelyresurrection.respawns.RespawnManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Events implements Listener {
   private final MessageConfig message = StelyResurrectionPlugin.getMessage();

   public static final void load() {
      Bukkit.getPluginManager().registerEvents(new Events(), StelyResurrectionPlugin.getInstance());
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public final void onDeath(PlayerDeathEvent e) {
      e.getDrops().clear();
      e.setDroppedExp(0);
      if (e.getEntity().hasPermission("res.keepInventory")) {
         e.setKeepInventory(true);
         e.getDrops().clear();
      }

      if (e.getEntity().hasPermission("res.keepXp")) {
         e.setKeepLevel(true);
         e.setDroppedExp(0);
      }

      this.message.sendMessage(e.getEntity(), "events.death");
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public final void onRespawn(PlayerRespawnEvent e) {
      e.setRespawnLocation(RespawnManager.getRespawnPoint(e.getPlayer(), e.getPlayer().getLastDamageCause() != null ? e.getPlayer().getLastDamageCause().getCause() : DamageCause.VOID));
      this.message.sendMessage(e.getPlayer(), "events.respawn");
   }
}
