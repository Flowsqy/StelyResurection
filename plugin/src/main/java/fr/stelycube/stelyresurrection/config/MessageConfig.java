package fr.stelycube.stelyresurrection.config;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MessageConfig {

    private YamlConfiguration configuration;
    private String prefix;

    public void load(@NotNull ConfigLoader configLoader, @NotNull JavaPlugin javaPlugin, @NotNull String fileName) {
        configuration = configLoader.initFile(javaPlugin.getDataFolder(), Objects.requireNonNull(javaPlugin.getResource(fileName)), fileName);
    }

    @Nullable
    private String formatColor(@Nullable String message) {
        return message == null ? null : ChatColor.translateAlternateColorCodes('&', message);
    }

    public void loadPrefix() {
        prefix = formatColor(configuration.getString("prefix"));
    }

    @Nullable
    public String getMessage(@NotNull String path) {
        final String message = formatColor(configuration.getString(path));
        if (message == null) {
            return null;
        }
        return prefix == null ? message : message.replace("%prefix%", prefix);
    }


}
