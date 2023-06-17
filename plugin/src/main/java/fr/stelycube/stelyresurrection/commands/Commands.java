package fr.stelycube.stelyresurrection.commands;

import fr.stelycube.stelyresurrection.StelyResurrectionPlugin;
import fr.stelycube.stelyresurrection.commands.tabCompleters.TabCompleters;
import fr.stelycube.stelyresurrection.configs.MessageConfig;
import fr.stelycube.stelyresurrection.respawns.Respawn;
import fr.stelycube.stelyresurrection.respawns.RespawnManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Commands implements CommandExecutor {
   static final MessageConfig message = StelyResurrectionPlugin.getMessage();

   public static void load() {
      PluginCommand setres = StelyResurrectionPlugin.getInstance().getCommand("setres");
      PluginCommand delres = StelyResurrectionPlugin.getInstance().getCommand("delres");
      setres.setExecutor(new Commands());
      setres.setTabCompleter(new TabCompleters());
      setres.setPermissionMessage(message.getString("util.noperm") != null ? message.getString("util.noperm") : "§cVous n'avez pas la permission de faire cette commande");
      delres.setExecutor(new Commands());
      delres.setTabCompleter(new TabCompleters());
      delres.setPermissionMessage(message.getString("util.noperm") != null ? message.getString("util.noperm") : "§cVous n'avez pas la permission de faire cette commande");
   }

   public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
      if (!(sender instanceof Player)) {
         System.out.println(message.getString("util.onlyplayer") != null ? message.getString("util.onlyplayer") : "§cSeul un joueur peut faire cette commande!");
         return true;
      } else {
         Player player = (Player)sender;
         message.setSender(player);
         if (label.equalsIgnoreCase("setres")) {
            if (args != null && args.length != 0) {
               if (args.length == 1) {
                  RespawnManager.set(new Respawn(args[0], player.getLocation()));
                  message.sendMessage("commands.setres", "%name%", args[0]);
               } else {
                  List<String> argsList = new ArrayList(Arrays.asList(args));
                  boolean all = false;
                  List<DamageCause> damageCauses = new ArrayList();
                  argsList.remove(0);
                  Iterator var10 = argsList.iterator();

                  while(true) {
                     while(var10.hasNext()) {
                        String cause = (String)var10.next();
                        if (RespawnManager.isDamageCause(cause) && !damageCauses.contains(RespawnManager.getDamageCause(cause))) {
                           damageCauses.add(RespawnManager.getDamageCause(cause));
                        } else if (cause.equalsIgnoreCase("ALL")) {
                           all = true;
                        }
                     }

                     if (damageCauses.isEmpty()) {
                        all = true;
                     }

                     RespawnManager.set(new Respawn(args[0], player.getLocation(), all, damageCauses));
                     message.sendMessage("commands.setres" + (damageCauses.isEmpty() ? "" : "causes"), "%name%", "%causes%", args[0], damageCauses.toString().replace("[", "").replace("]", "").toLowerCase(Locale.ROOT));
                     break;
                  }
               }
            } else {
               message.sendMessage("help.setres");
            }
         } else if (label.equalsIgnoreCase("delres")) {
            if (args != null && args.length == 1) {
               if (!RespawnManager.contains(args[0])) {
                  message.sendMessage("util.invalidname", "%name%", args[0]);
               } else {
                  RespawnManager.remove(args[0]);
                  message.sendMessage("commands.delres", "%name%", args[0]);
               }
            } else {
               message.sendMessage("help.delres");
            }
         }

         return true;
      }
   }
}
