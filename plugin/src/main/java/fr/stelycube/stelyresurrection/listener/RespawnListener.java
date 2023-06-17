package fr.stelycube.stelyresurrection.listener;

import fr.stelycube.stelyresurrection.respawnpoint.RespawnPointManager;
import fr.stelycube.stelyresurrection.respawns.RespawnManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RespawnListener implements Listener {

    private final String respawnMessage;
    private final RespawnPointManager respawnManager;

    public RespawnListener(@Nullable String respawnMessage, @NotNull RespawnPointManager respawnManager) {
        this.respawnMessage = respawnMessage;
        this.respawnManager = respawnManager;
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.HIGH)
    private void onRespawn(PlayerRespawnEvent e) {
        final Player respawnedPlayer = e.getPlayer();
        e.setRespawnLocation(RespawnManager.getRespawnPoint(e.getPlayer(), e.getPlayer().getLastDamageCause() != null ? e.getPlayer().getLastDamageCause().getCause() : EntityDamageEvent.DamageCause.VOID));
        if (respawnMessage != null) {
            e.getPlayer().sendMessage(respawnMessage);
        }
    }

}
