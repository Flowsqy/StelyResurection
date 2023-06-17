package fr.stelycube.stelyresurrection.respawnpoint;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class RespawnPointManager {

    private final RespawnPointStorage storage;
    private final RespawnPointMap respawnPoints;

    public RespawnPointManager() {
        this.respawnPoints = new RespawnPointMap(new Random());
    }

    @NotNull
    public Iterable<RespawnPoint> getRegisteredPoints() {
        return respawnPoints;
    }

    public void set(@NotNull String respawnPointName, @NotNull Location respawnPointLocation) {
        set(respawnPointName, respawnPointLocation, Collections.emptySet());
    }

    public void set(@NotNull String respawnPointName, @NotNull Location respawnPointLocation, @NotNull Set<EntityDamageEvent.DamageCause> causes) {
        final RespawnPoint respawnPoint = new RespawnPoint(respawnPointName, respawnPointLocation);
        respawnPoints.set(respawnPoint, causes);
        storage.set(respawnPoint, causes);
    }

    public boolean delete(@NotNull String respawnPointName) {
        final boolean modified = respawnPoints.remove(respawnPointName);
        if (!modified) {
            return false;
        }
        storage.remove(respawnPointName);
        return true;
    }

    @NotNull
    public Location getRespawnLocation(@NotNull Player respawnedPlayer, @NotNull EntityDamageEvent.DamageCause damageCause) {
        final Optional<RespawnPoint> respawnPoint = respawnPoints.getRandom(damageCause);
        if (respawnPoint.isEmpty()) {
            final Location bedLocation = respawnedPlayer.getBedSpawnLocation();
            return bedLocation == null ? respawnedPlayer.getWorld().getSpawnLocation() : bedLocation;
        }
        return respawnPoint.get().location();
    }
}
