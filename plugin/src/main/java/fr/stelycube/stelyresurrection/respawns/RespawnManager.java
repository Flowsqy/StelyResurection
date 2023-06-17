package fr.stelycube.stelyresurrection.respawns;

import fr.stelycube.stelyresurrection.configs.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class RespawnManager {
   private static final Config respawn = new Config("respawn.yml");
   private static final Map<String, ArrayList<Location>> respawnPoints = new HashMap();
   private static final Map<Player, Location> lastRespawnLoc = new HashMap();
   private static final List<String> allRespawnsId = new ArrayList();

   public static final void load() {
      loadRespawns();
   }

   public static final Location getRespawnPoint(Player player, DamageCause damageCause) {
      Location baseLoc = player.getBedSpawnLocation() != null ? player.getBedSpawnLocation() : player.getWorld().getSpawnLocation();
      Location finalLoc;
      if (!respawnPoints.containsKey(damageCause.name())) {
         finalLoc = respawnPoints.containsKey("ALL") ? getRandomLoc((ArrayList)respawnPoints.get("ALL")) : baseLoc;
      } else {
         finalLoc = getRandomLoc((ArrayList)respawnPoints.get(damageCause.name()));
      }

      lastRespawnLoc.put(player, finalLoc);
      return finalLoc;
   }

   public static final void set(Respawn respawn) {
      if (respawn.isAll()) {
         if (!respawnPoints.containsKey("ALL")) {
            respawnPoints.put("ALL", new ArrayList());
         }

         ((ArrayList)respawnPoints.get("ALL")).add(respawn.getLocation());
      }

      DamageCause damageCause;
      for(Iterator var2 = respawn.getDamageCauses().iterator(); var2.hasNext(); ((ArrayList)respawnPoints.get(damageCause.name())).add(respawn.getLocation())) {
         damageCause = (DamageCause)var2.next();
         if (!respawnPoints.containsKey(damageCause.name())) {
            respawnPoints.put(damageCause.name(), new ArrayList());
         }
      }

      fromRespawnToConfig(respawn);
      allRespawnsId.add(respawn.getId());
   }

   public static final void remove(String name) {
      respawn.setSave(name, (Object)null);
      loadRespawns();
   }

   public static final boolean contains(String name) {
      return allRespawnsId.contains(name);
   }

   public static final List<String> getAllLoc() {
      return allRespawnsId;
   }

   public static final Map<String, ArrayList<Location>> getRespawnList() {
      return respawnPoints;
   }

   public static final Map<Player, Location> getLastRespawnLoc() {
      return lastRespawnLoc;
   }

   private static final void loadRespawns() {
      respawnPoints.clear();
      allRespawnsId.clear();
      Iterator var1 = (respawn.toConfigurationSection().getKeys(false) != null ? new ArrayList(respawn.toConfigurationSection().getKeys(false)) : new ArrayList()).iterator();

      while(true) {
         String respawnPoint;
         do {
            do {
               if (!var1.hasNext()) {
                  return;
               }

               respawnPoint = (String)var1.next();
            } while(!isValidRespawn(respawnPoint));
         } while(allRespawnsId.contains(respawnPoint));

         Respawn tempRespawn = fromConfigToRespawn(respawnPoint);
         if (tempRespawn.isAll()) {
            if (!respawnPoints.containsKey("ALL")) {
               respawnPoints.put("ALL", new ArrayList());
            }

            ((ArrayList)respawnPoints.get("ALL")).add(tempRespawn.getLocation());
         }

         DamageCause damageCause;
         for(Iterator var4 = tempRespawn.getDamageCauses().iterator(); var4.hasNext(); ((ArrayList)respawnPoints.get(damageCause.name())).add(tempRespawn.getLocation())) {
            damageCause = (DamageCause)var4.next();
            if (!respawnPoints.containsKey(damageCause.name())) {
               respawnPoints.put(damageCause.name(), new ArrayList());
            }
         }

         allRespawnsId.add(tempRespawn.getId());
      }
   }

   private static final Location getRandomLoc(ArrayList<Location> listLoc) {
      Random random = new Random();
      return (Location)listLoc.get(random.nextInt(listLoc.size()));
   }

   public static final boolean isValidRespawn(String path) {
      return respawn.isString(path + ".location.world") && Bukkit.getWorld(respawn.getString(path + ".location.world")) != null && respawn.isDouble(path + ".location.x") && respawn.isDouble(path + ".location.y") && respawn.isDouble(path + ".location.z") && respawn.isFloat(path + ".location.yaw") && respawn.isFloat(path + ".location.pitch");
   }

   public static final Config getFile() {
      return respawn;
   }

   private static final void fromRespawnToConfig(Respawn respawn) {
      RespawnManager.respawn.set(respawn.getId() + ".location.world", respawn.getLocation().getWorld().getName());
      RespawnManager.respawn.set(respawn.getId() + ".location.x", respawn.getLocation().getX());
      RespawnManager.respawn.set(respawn.getId() + ".location.y", respawn.getLocation().getY());
      RespawnManager.respawn.set(respawn.getId() + ".location.z", respawn.getLocation().getZ());
      RespawnManager.respawn.set(respawn.getId() + ".location.yaw", respawn.getLocation().getYaw());
      RespawnManager.respawn.set(respawn.getId() + ".location.pitch", respawn.getLocation().getPitch());
      String cause = (respawn.isAll() ? "ALL" : "") + (respawn.getDamageCauses().isEmpty() ? "" : (respawn.isAll() ? "," : "") + respawn.getDamageCauses().toString().replace("[", "").replace("]", "").replace(" ", ""));
      RespawnManager.respawn.setSave(respawn.getId() + ".cause", cause);
   }

   private static final Respawn fromConfigToRespawn(String path) {
      if (!isValidRespawn(path)) {
         throw new IllegalArgumentException("Invalid location");
      } else {
         World world = Bukkit.getWorld(respawn.getString(path + ".location.world"));
         double x = respawn.getDouble(path + ".location.x");
         double y = respawn.getDouble(path + ".location.y");
         double z = respawn.getDouble(path + ".location.z");
         float yaw = respawn.getFloat(path + ".location.yaw");
         float pitch = respawn.getFloat(path + ".location.pitch");
         String causes = respawn.isString(path + ".cause") ? respawn.getString(path + ".cause").toUpperCase(Locale.ROOT) : new String("ALL");
         boolean all = causes.contains("ALL");
         List<DamageCause> damageCauses = new ArrayList();
         String[] var16;
         int var15 = (var16 = causes.split(",")).length;

         for(int var14 = 0; var14 < var15; ++var14) {
            String possibCause = var16[var14];
            if (isDamageCause(possibCause) && !damageCauses.contains(getDamageCause(possibCause))) {
               damageCauses.add(getDamageCause(possibCause));
            }
         }

         return new Respawn(path, new Location(world, x, y, z, yaw, pitch), all, damageCauses);
      }
   }

   public static final DamageCause getDamageCause(String arg) {
      DamageCause[] var4;
      int var3 = (var4 = DamageCause.values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         DamageCause dc = var4[var2];
         if (dc.name().equalsIgnoreCase(arg)) {
            return dc;
         }
      }

      return null;
   }

   public static final boolean isDamageCause(String arg) {
      return getDamageCause(arg) != null;
   }
}
