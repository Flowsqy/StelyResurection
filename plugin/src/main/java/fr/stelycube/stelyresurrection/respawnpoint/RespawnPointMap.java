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

    public void set(@NotNull RespawnPoint point, @NotNull Collection<EntityDamageEvent.DamageCause> damageCauses) {
        final Optional<RespawnPoint[]> allPoints = set(respawnAll, point);
        allPoints.ifPresent(respawnPoints -> respawnAll = respawnPoints);
        for (EntityDamageEvent.DamageCause cause : damageCauses) {
            final RespawnPoint[] specificPoints = respawnSpecifics.getOrDefault(cause, new RespawnPoint[0]);
            final Optional<RespawnPoint[]> newSpecificPoints = set(specificPoints, point);
            if (newSpecificPoints.isEmpty()) {
                continue;
            }
            respawnSpecifics.put(cause, newSpecificPoints.get());
        }
    }

    private Optional<RespawnPoint[]> set(@NotNull RespawnPoint[] points, @NotNull RespawnPoint point) {
        boolean modified = false;
        for (int index = 0; index < points.length; index++) {
            if (!points[index].name().equals(point.name())) {
                continue;
            }
            points[index] = point;
            modified = true;
            break;
        }
        if (modified) {
            return Optional.empty();
        }
        final RespawnPoint[] newPoints = new RespawnPoint[respawnAll.length + 1];
        System.arraycopy(points, 0, newPoints, 0, points.length);
        return Optional.of(newPoints);
    }

    private record SpecificRespawnPoints(@NotNull EntityDamageEvent.DamageCause cause, @NotNull RespawnPoint[] points) {
    }

    public boolean remove(@NotNull String respawnPointName) {
        boolean modified = false;
        final Optional<RespawnPoint[]> allPoints = remove(respawnAll, respawnPointName);
        if (allPoints.isPresent()) {
            respawnAll = allPoints.get();
            modified = true;
        }
        final List<SpecificRespawnPoints> specificRespawnPoints = new LinkedList<>();
        for (var entry : respawnSpecifics.entrySet()) {
            final RespawnPoint[] specificPoints = entry.getValue();
            final Optional<RespawnPoint[]> newSpecificPoints = remove(specificPoints, respawnPointName);
            if (newSpecificPoints.isEmpty()) {
                continue;
            }
            modified = true;
            specificRespawnPoints.add(new SpecificRespawnPoints(entry.getKey(), newSpecificPoints.get()));
        }
        for (SpecificRespawnPoints points : specificRespawnPoints) {
            respawnSpecifics.put(points.cause(), points.points());
        }
        return modified;
    }

    private Optional<RespawnPoint[]> remove(@NotNull RespawnPoint[] points, @NotNull String respawnPointName) {
        int index = -1;
        for (int i = 0; i < points.length; i++) {
            if (!points[i].name().equals(respawnPointName)) {
                continue;
            }
            index = i;
            break;
        }
        if (index == -1) {
            return Optional.empty();
        }
        final RespawnPoint[] newPoints = new RespawnPoint[points.length - 1];
        System.arraycopy(points, 0, newPoints, 0, index);
        System.arraycopy(points, index + 1, newPoints, index, newPoints.length - index);
        return Optional.of(newPoints);
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
