package me.kneesnap.CommandAliases;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * Decompilation of the original code.
 * Created sometime in 2016.
 */
public class CmdAlias implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("create") || subCommand.equals("delete") || subCommand.equals("list")) {
                if (!sender.hasPermission("alias." + subCommand)) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission for this subcommand!");
                    return true;
                }

                Map<String, Object> map = CommandAliases.INSTANCE.getConfig().getValues(false);
                if (subCommand.equalsIgnoreCase("create")) {
                    String aliasName = args[1];
                    if (args.length > 2) {
                        if (map.get(aliasName) == null) {
                            String[] cutArgs = new String[args.length - 2];
                            System.arraycopy(args, 2, cutArgs, 0, cutArgs.length);
                            CommandAliases.INSTANCE.getConfig().set(aliasName, String.join(" ", cutArgs));
                            CommandAliases.INSTANCE.saveConfig();
                            sender.sendMessage(ChatColor.GRAY + "Created alias " + ChatColor.GOLD + "/" + aliasName + ChatColor.GRAY + ".");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Alias already exists with that name!");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /" + label + " create <aliasName> <commandToRun...>");
                        sender.sendMessage(ChatColor.RED + "aliasName = The new command name, commandName = The old command");
                        sender.sendMessage(ChatColor.RED + "Ex: /" + label + " create spec gamemode spectator");
                        sender.sendMessage(ChatColor.RED + "Would run '/gamemode spectator' in the place of /spec.");
                    }
                } else if (subCommand.equals("delete")) {
                    if (args.length > 1) {
                        if (map.get(args[1]) == null) {
                            sender.sendMessage(ChatColor.RED + "Alias not found");
                        } else {
                            CommandAliases.INSTANCE.getConfig().set(args[1], null);
                            CommandAliases.INSTANCE.saveConfig();
                            sender.sendMessage(ChatColor.GRAY + "Deleted");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /" + label + " delete <aliasName>");
                        sender.sendMessage(ChatColor.RED + "Removes the alias linked to aliasName");
                    }
                } else if (subCommand.equals("list")) {
                    sender.sendMessage(ChatColor.GRAY + "[-" + ChatColor.GOLD + "Alias List" + ChatColor.GRAY + "-]");
                    for (String key : map.keySet()) {
                        String value = map.get(key).toString();
                        sender.sendMessage(ChatColor.GRAY + " - /" + key + " => /" + value);
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Unknown sub-command '" + subCommand + "'.");
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "[-" + ChatColor.GOLD + "CommandAliases" + ChatColor.GRAY + "-]");
            formatCommandInfo(sender, label, "create", " <aliasName> <commandName>");
            formatCommandInfo(sender, label, "delete", " <aliasName>");
            formatCommandInfo(sender, label, "list", "");

            Bukkit.getScheduler().runTaskAsynchronously(CommandAliases.INSTANCE, () -> { // Should have been async to begin with.
                if (CommandAliases.UPDATER.checkForUpdate())
                    sender.sendMessage(ChatColor.AQUA + "There is an update available!");
            });
        }

        return true;
    }

    private void formatCommandInfo(CommandSender sender, String label, String subcommand, String args) {
        sender.sendMessage(ChatColor.GRAY + " - /" + label + " " + subcommand + args + "  |  Permission: " + (sender.hasPermission("alias." + subcommand) ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
    }
}
