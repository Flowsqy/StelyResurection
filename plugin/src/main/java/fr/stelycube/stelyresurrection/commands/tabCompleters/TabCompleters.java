package fr.stelycube.stelyresurrection.commands.tabCompleters;

import fr.stelycube.stelyresurrection.respawns.RespawnManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class TabCompleters implements TabCompleter {
   private static final List<String> damageCause;

   static {
      damageCause = new ArrayList(Arrays.asList((new ArrayList(Arrays.asList(DamageCause.values()))).toString().replace("[", "").replace("]", "").toLowerCase(Locale.ROOT).split(", ")));
      if (!damageCause.contains("all")) {
         damageCause.add("all");
      }

   }

   public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
      if (sender instanceof Player) {
         if (label.equalsIgnoreCase("setres") && sender.hasPermission("res.setres")) {
            if (args != null && args.length != 1) {
               return this.setReturn(args[args.length - 1], damageCause);
            }

            return this.setReturn(args[0], new ArrayList());
         }

         if (label.equalsIgnoreCase("delres") && sender.hasPermission("res.delres")) {
            if (args != null && args.length != 1) {
               return new ArrayList();
            }

            return this.setReturn(args[0], RespawnManager.getAllLoc());
         }
      }

      return null;
   }

   private final List<String> setReturn(String arg, List<String> list2) {
      List<String> list = new ArrayList();
      if (arg != null && !arg.equalsIgnoreCase("")) {
         Iterator var5 = list2.iterator();

         while(var5.hasNext()) {
            String possib = (String)var5.next();
            if (possib.toLowerCase().startsWith(arg.toLowerCase(Locale.ROOT))) {
               list.add(possib);
            }
         }

         return list;
      } else {
         return list2;
      }
   }
}
