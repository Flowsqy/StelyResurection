package fr.stelycube.stelyresurrection.respawnpoint;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public record RespawnPoint(@NotNull String name, @NotNull Location location) {
}
