package fr.stelycube.stelyresurrection.configs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MessageConfig extends Config {
   private Config message;
   private CommandSender sender = Bukkit.getConsoleSender();

   public MessageConfig(String file) {
      super(file);
      this.message = new Config(file);
   }

   public MessageConfig(String file, String path) {
      super(file, path);
      this.message = new Config(file, path);
   }

   public final String getString(String path) {
      return this.message.getString(path) != null ? this.message.getString(path).replace("&", "§").replaceAll("%prefix%", this.getPrefix()) : "";
   }

   public final String getPrefix() {
      return this.message.isNull("prefix") ? new String() : this.message.getString("prefix").replaceAll("&", "§");
   }

   public final void setSender(CommandSender sender) {
      this.sender = sender;
   }

   public final boolean sendMessage(CommandSender sender, String path, Object... replace) {
      String msg;
      try {
         msg = this.getString(path);
      } catch (NullPointerException var8) {
         msg = null;
      }

      if (msg != null) {
         int args = replace.length % 2 == 1 ? (replace.length - 1) / 2 : replace.length / 2;

         for(int b = 0; b < args; ++b) {
            msg = msg.replace(String.valueOf(replace[b]), String.valueOf(replace[b + args]));
         }
      }

      try {
         if (!this.message.getString(path).equalsIgnoreCase("")) {
            sender.sendMessage(msg.split("%nl%"));
         }
      } catch (NullPointerException var7) {
         System.out.println("La ligne " + path + " dans le " + this.message.getName() + " est mal definie");
      }

      return true;
   }

   public final boolean sendMessage(String path, Object... replace) {
      return this.sendMessage(this.sender, path, replace);
   }

   public final boolean sendConsoleMessage(String path, Object... replace) {
      return this.sendMessage(Bukkit.getConsoleSender(), path, replace);
   }
}
