package fr.stelycube.stelyresurrection.listener;

import fr.stelycube.stelyresurrection.respawnpoint.RespawnPointManager;
import org.bukkit.Location;
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
    private final RespawnPointManager respawnPointManager;

    public RespawnListener(@Nullable String respawnMessage, @NotNull RespawnPointManager respawnPointManager) {
        this.respawnMessage = respawnMessage;
        this.respawnPointManager = respawnPointManager;
    }

    @SuppressWarnings("unused")
    @EventHandler(priority = EventPriority.HIGH)
    private void onRespawn(PlayerRespawnEvent e) {
        final Player respawnedPlayer = e.getPlayer();
        final EntityDamageEvent lastDeath = respawnedPlayer.getLastDamageCause();
        final EntityDamageEvent.DamageCause damageCause = lastDeath == null ? EntityDamageEvent.DamageCause.VOID : lastDeath.getCause();
        final Location respawnLocation = respawnPointManager.getRespawnLocation(respawnedPlayer, damageCause);
        e.setRespawnLocation(respawnLocation);
        if (respawnMessage != null) {
            e.getPlayer().sendMessage(respawnMessage);
        }
    }

}
