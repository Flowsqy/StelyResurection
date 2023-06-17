package fr.stelycube.stelyresurrection.listener;

import fr.stelycube.stelyresurrection.respawns.RespawnManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {

    private final String respawnMessage;
    private final RespawnPointManager respawnManager;

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.HIGH)
    private void onRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(RespawnManager.getRespawnPoint(e.getPlayer(), e.getPlayer().getLastDamageCause() != null ? e.getPlayer().getLastDamageCause().getCause() : EntityDamageEvent.DamageCause.VOID));
        e.getPlayer().sendMessage(respawnMessage);
        //this.message.sendMessage(e.getPlayer(), "events.respawn");
    }

}
