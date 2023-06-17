package fr.stelycube.stelyresurrection.configs;

import fr.stelycube.stelyresurrection.StelyResurrectionPlugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
   private static final Plugin plugin = StelyResurrectionPlugin.getInstance();
   private YamlConfiguration config;
   private File configFile;
   private File configFolder;
   private String fileName;

   public Config(String file) {
      this(file, "plugins" + File.separator + plugin.getDescription().getName());
   }

   public Config(String file, String path) {
      this.mkdir(path);
      this.mkFile(file);
      this.loadConfig();
      this.fileName = file;
   }

   public final void mkdir(String path) {
      this.configFolder = new File(path);
      if (!this.configFolder.exists()) {
         this.configFolder.mkdirs();
      }

   }

   public final void mkFile(String file) {
      this.configFile = new File(this.configFolder, file);
      if (!this.configFile.exists()) {
         if (plugin.getResource(file) != null && !this.configFile.exists()) {
            try {
               Throwable var2 = null;
               Object var3 = null;

               try {
                  InputStream in = plugin.getResource(file);

                  try {
                     Files.copy(in, this.configFile.toPath(), new CopyOption[0]);
                  } finally {
                     if (in != null) {
                        in.close();
                     }

                  }
               } catch (Throwable var15) {
                  if (var2 == null) {
                     var2 = var15;
                  } else if (var2 != var15) {
                     var2.addSuppressed(var15);
                  }

                  throw var2;
               }
            } catch (IOException var16) {
               var16.printStackTrace();
            }
         } else {
            try {
               this.configFile.createNewFile();
            } catch (IOException var13) {
               System.out.println("Le fichier n'a pas pu être créé");
            }
         }
      }

   }

   public final void loadConfig() {
      this.config = YamlConfiguration.loadConfiguration(this.configFile);
   }

   public final void saveConfig() {
      try {
         this.config.save(this.configFile);
      } catch (IOException var2) {
         System.out.println("La config n'a pas pu être sauvegarder correctement");
      }

   }

   public final void reloadConfig() {
      this.loadConfig();
   }

   public final void set(String path, Object value) {
      try {
         this.config.set(path, value);
      } catch (Exception var4) {
      }

   }

   public final void setSave(String path, Object value) {
      this.set(path, value);
      this.saveConfig();
   }

   public final boolean contains(String path) {
      return this.config.contains(path);
   }

   public final boolean isBoolean(String path) {
      try {
         Boolean.parseBoolean(this.config.getString(path));
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public final boolean isInt(String path) {
      try {
         Integer.parseInt(this.config.getString(path));
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public final boolean isDouble(String path) {
      try {
         Double.parseDouble(this.config.getString(path));
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public final boolean isLong(String path) {
      try {
         Long.parseLong(this.config.getString(path));
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public final boolean isFloat(String path) {
      try {
         Float.parseFloat(this.config.getString(path));
         return true;
      } catch (Exception var3) {
         return false;
      }
   }

   public final boolean isList(String path) {
      return this.config.isList(path);
   }

   public final boolean isString(String path) {
      return this.config.contains(path) && this.config.getString(path) != null;
   }

   public final boolean getBoolean(String path) {
      return this.isBoolean(path) ? Boolean.valueOf(this.config.getBoolean(path)) : false;
   }

   public final int getInt(String path) {
      return this.isInt(path) ? Integer.valueOf(this.config.getInt(path)) : 0;
   }

   public final double getDouble(String path) {
      return this.isDouble(path) ? Double.valueOf(this.config.getString(path)) : 0.0D;
   }

   public final long getLong(String path) {
      return this.isLong(path) ? Long.valueOf(this.config.getLong(path)) : 0L;
   }

   public final float getFloat(String path) {
      return this.isFloat(path) ? Float.valueOf(this.config.getString(path)) : 0.0F;
   }

   public final ArrayList<String> getStringList(String path) {
      return this.isList(path) ? (ArrayList)this.config.getStringList(path) : null;
   }

   public final ArrayList<?> getList(String path) {
      return this.isList(path) ? this.getList(path) : null;
   }

   public String getString(String path) {
      return this.config.getString(path);
   }

   public final Object get(String path) {
      return this.config.get(path);
   }

   public final boolean isNull(String path) {
      return !this.contains(path) || this.config.getString(path) == null || this.config.getString(path).equalsIgnoreCase("");
   }

   public final YamlConfiguration toConfigurationSection() {
      return this.config;
   }

   public final String getName() {
      return this.fileName;
   }
}
