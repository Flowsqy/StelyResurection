package fr.stelycube.stelyresurrection.respawnpoint;

import org.jetbrains.annotations.NotNull;

public record RespawnPoint(@NotNull String name, @NotNull LazyLocation location) {
}
