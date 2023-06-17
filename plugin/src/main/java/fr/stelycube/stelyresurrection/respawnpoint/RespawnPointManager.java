package fr.stelycube.stelyresurrection.respawnpoint;

import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class RespawnPointManager {


    public boolean delete(@NotNull String respawnPointName) {
    }

    @NotNull
    public Iterable<RespawnPoint> getRegisteredPoints() {

    }

    public void set(@NotNull String respawnPointName, @NotNull Location respawnPointLocation) {
    }

    public void set(@NotNull String respawnPointName, @NotNull Location respawnPointLocation, @NotNull Set<EntityDamageEvent.DamageCause> causes) {
    }

}
