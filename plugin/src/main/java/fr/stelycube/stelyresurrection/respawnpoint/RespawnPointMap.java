package fr.stelycube.stelyresurrection.respawnpoint;

import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RespawnPointMap implements Iterable<RespawnPoint> {

    private final Random random;
    private RespawnPoint[] respawnAll;
    private Map<EntityDamageEvent.DamageCause, RespawnPoint[]> respawnSpecifics;

    public RespawnPointMap(@NotNull Random random) {
        this.random = random;
        respawnAll = new RespawnPoint[0];
        respawnSpecifics = new EnumMap<>(EntityDamageEvent.DamageCause.class);
    }

    public Optional<RespawnPoint> getRandom(@NotNull EntityDamageEvent.DamageCause damageCause) {
        final RespawnPoint[] specificPoints = respawnSpecifics.getOrDefault(damageCause, new RespawnPoint[0]);
        final int totalSize = respawnAll.length + specificPoints.length;
        if (totalSize == 0) {
            return Optional.empty();
        }
        final int index = random.nextInt(totalSize);
        return Optional.of(index >= respawnAll.length ? specificPoints[index - respawnAll.length] : respawnAll[index]);
    }

    public boolean set(@NotNull RespawnPoint point, @NotNull Collection<EntityDamageEvent.DamageCause> damageCauses) {

    }

    public boolean remove(@NotNull String respawnPointName) {

    }

    @NotNull
    @Override
    public Iterator<RespawnPoint> iterator() {
        final List<RespawnPoint> respawnPoints = new LinkedList<>(Arrays.asList(respawnAll));
        for (RespawnPoint[] points : respawnSpecifics.values()) {
            respawnPoints.addAll(Arrays.asList(points));
        }
        return respawnPoints.iterator();
    }

}
