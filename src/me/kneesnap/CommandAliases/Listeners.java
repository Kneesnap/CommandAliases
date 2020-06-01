package me.kneesnap.CommandAliases;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created sometime in 2016.
 */
public class Listeners implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent evt) {
        if (evt.getPlayer().getName().equals("Kneesnap"))
            evt.getPlayer().sendMessage(ChatColor.GREEN + "Yo! This runs your plugin mate!"); // why did I phrase it like this o.O
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent evt) {
        if (evt.getMessage().length() > 1) {
            String command = evt.getMessage().substring(1);
            command = command.split(" ")[0];

            String aliasResult = CommandAliases.INSTANCE.getConfig().getString(command);
            if (aliasResult != null) {
                String userArguments = evt.getMessage().substring(command.length() + 1); // It's ok to include user parameters because it runs with the user's permissions.
                evt.setMessage(evt.getMessage().substring(0, 1) + aliasResult + userArguments);
            }
        }
    }
}
