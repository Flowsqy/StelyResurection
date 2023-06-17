package fr.stelycube.stelyresurrection.respawnpoint;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record LazyLocation(@NotNull String world, double x, double y, double z, float yaw, float pitch) {

    public static LazyLocation of(@NotNull Location location) {
        return new LazyLocation(Objects.requireNonNull(location.getWorld()).getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Nullable
    public Location toLoc() {
        final World world = Bukkit.getWorld(this.world);
        if (world == null) {
            return null;
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

}
