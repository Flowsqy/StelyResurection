package fr.stelycube.stelyresurrection.respawnpoint;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Random;

public class RespawnPointLoader {

    public RespawnPointManager load(@NotNull File dataFolder) {
        final RespawnPointMap map = new RespawnPointMap(new Random());
        final RespawnPointStorage storage = new RespawnPointStorage(new File(dataFolder, "respawn.yml"));
        storage.load(map);
        return new RespawnPointManager(storage, map);
    }

}
