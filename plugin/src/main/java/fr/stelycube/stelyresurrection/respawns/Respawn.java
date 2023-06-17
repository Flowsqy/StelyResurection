package fr.stelycube.stelyresurrection.respawns;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Respawn {
   private final String id;
   private Location location;
   private boolean all;
   private List<DamageCause> damageCauses;

   public Respawn(String id, Location location) {
      this(id, location, true);
   }

   public Respawn(String id, Location location, boolean all) {
      this(id, location, all, new ArrayList());
   }

   public Respawn(String id, Location location, boolean all, List<DamageCause> damageCauses) {
      this.id = id;
      this.location = location;
      this.all = all;
      this.damageCauses = damageCauses;
   }

   public final String getId() {
      return this.id;
   }

   public final Location getLocation() {
      return this.location;
   }

   public final void setLocation(Location location) {
      this.location = location;
   }

   public final boolean isAll() {
      return this.all;
   }

   public final void setAll(boolean all) {
      this.all = all;
   }

   public final List<DamageCause> getDamageCauses() {
      return this.damageCauses;
   }

   public final void setDamageCauses(List<DamageCause> damageCauses) {
      this.damageCauses = damageCauses;
   }

   public final String toString() {
      return new String("Respawn{Id:" + this.id + ", " + "Location:[World:" + this.location.getWorld() + ", x:" + this.location.getX() + ", y:" + this.location.getY() + ", z:" + this.location.getZ() + ", yaw" + this.location.getYaw() + ", pitch" + this.location.getPitch() + "], " + "All:" + this.all + ", " + "DamagesCauses:" + this.damageCauses.toString() + "}");
   }
}
