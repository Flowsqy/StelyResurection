package fr.stelycube.stelyresurrection.respawnpoint;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RespawnPointStorage {

    private final File file;
    private YamlConfiguration configuration;

    public RespawnPointStorage(File file) {
        this.file = file;
    }

    public void load(@NotNull RespawnPointMap map) {
        configuration = YamlConfiguration.loadConfiguration(file);
        final Set<RespawnPoint> allPoints = new HashSet<>();
        final Map<EntityDamageEvent.DamageCause, Set<RespawnPoint>> specifics = new HashMap<>();
        for (String key : configuration.getKeys(false)) {
            final LazyLocation location = getLocation(key + ".location.");
            if (location == null) {
                continue;
            }
            final Set<EntityDamageEvent.DamageCause> causes = new HashSet<>();
            for (String cause : configuration.getStringList(".causes")) {
                final EntityDamageEvent.DamageCause damageCause;
                try {
                    damageCause = EntityDamageEvent.DamageCause.valueOf(cause);
                } catch (IllegalArgumentException e) {
                    continue;
                }
                causes.add(damageCause);
            }
            final RespawnPoint point = new RespawnPoint(key, location);
            if (causes.isEmpty()) {
                allPoints.add(point);
                continue;
            }
            for (EntityDamageEvent.DamageCause cause : causes) {
                final Set<RespawnPoint> points = specifics.computeIfAbsent(cause, k -> new HashSet<>());
                points.add(point);
            }
        }
        map.load(allPoints, specifics);
    }

    @Nullable
    private LazyLocation getLocation(@NotNull String path) {
        final String world = configuration.getString(path + "world");
        if (world == null) {
            return null;
        }
        final double x = configuration.getDouble(path + "x");
        final double y = configuration.getDouble(path + "y");
        final double z = configuration.getDouble(path + "z");
        final float yaw = (float) configuration.getDouble(path + "yaw");
        final float pitch = (float) configuration.getDouble(path + "pitch");
        return new LazyLocation(world, x, y, z, yaw, pitch);
    }

    public void set(@NotNull RespawnPoint respawnPoint, @NotNull Set<EntityDamageEvent.DamageCause> causes) {
        final String path = respawnPoint.name() + ".";
        final String locPath = path + "location.";
        final LazyLocation loc = respawnPoint.location();
        configuration.set(locPath + "world", loc.world());
        configuration.set(locPath + "x", loc.x());
        configuration.set(locPath + "y", loc.y());
        configuration.set(locPath + "z", loc.z());
        configuration.set(locPath + "yaw", loc.yaw());
        configuration.set(locPath + "pitch", loc.pitch());
        configuration.set(path + "causes", causes.stream().map(Enum::name).toList());
    }

    public void remove(@NotNull String respawnPointName) {
        configuration.set(respawnPointName, null);
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
