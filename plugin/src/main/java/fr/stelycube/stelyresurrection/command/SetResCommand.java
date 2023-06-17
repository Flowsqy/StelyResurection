package fr.stelycube.stelyresurrection.command;

import fr.stelycube.stelyresurrection.respawnpoint.RespawnPointManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SetResCommand implements TabExecutor {

    private final RespawnPointManager respawnPointManager;
    private final String helpMessage;
    private final String onlyPlayerMessage;
    private final String successMessage;
    private final String successCausesMessage;

    public SetResCommand(@NotNull RespawnPointManager respawnPointManager, @Nullable String helpMessage, @Nullable String onlyPlayerMessage, @Nullable String successMessage, @Nullable String successCausesMessage) {
        this.respawnPointManager = respawnPointManager;
        this.helpMessage = helpMessage;
        this.onlyPlayerMessage = onlyPlayerMessage;
        this.successMessage = successMessage;
        this.successCausesMessage = successCausesMessage;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            if (onlyPlayerMessage != null) {
                sender.sendMessage(onlyPlayerMessage);
            }
            return true;
        }
        if (args.length == 0) {
            if (helpMessage != null) {
                sender.sendMessage(helpMessage);
            }
            return true;
        }
        final String respawnPointName = args[0];
        final Location respawnPointLocation = player.getLocation();

        final Set<EntityDamageEvent.DamageCause> causes = EnumSet.noneOf(EntityDamageEvent.DamageCause.class);
        for (int index = 1; index < args.length; index++) {
            final String causeArg = args[index];
            final EntityDamageEvent.DamageCause damageCause = getDamageCause(causeArg);
            if (damageCause == null) {
                continue;
            }
            causes.add(damageCause);
        }

        if (causes.isEmpty()) {
            respawnPointManager.set(respawnPointName, respawnPointLocation);
            if (successMessage != null) {
                sender.sendMessage(successMessage.replace("%name%", respawnPointName));
            }
            return true;
        }

        respawnPointManager.set(respawnPointName, respawnPointLocation, causes);
        if (successCausesMessage != null) {
            final List<String> causeNames = causes.stream().map(c -> c.name().toLowerCase(Locale.ENGLISH)).toList();
            final String causesString = String.join(", ", causeNames);
            final String formattedSuccessMessage = successCausesMessage
                    .replace("%name%", respawnPointName)
                    .replace("%causes%", causesString);
            sender.sendMessage(formattedSuccessMessage);
        }
        return true;
    }

    private EntityDamageEvent.DamageCause getDamageCause(@NotNull String arg) {
        for (EntityDamageEvent.DamageCause cause : EntityDamageEvent.DamageCause.values()) {
            if (cause.name().equalsIgnoreCase(arg)) {
                return cause;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) {
            return Collections.emptyList();
        }
        final String arg = args[args.length - 1];
        final List<String> completions = new LinkedList<>();
        for (EntityDamageEvent.DamageCause damageCause : EntityDamageEvent.DamageCause.values()) {
            final String damageCauseName = damageCause.name().toLowerCase(Locale.ENGLISH);
            if (!damageCauseName.startsWith(arg)) {
                continue;
            }
            completions.add(damageCauseName);
        }
        return completions;
    }
}
