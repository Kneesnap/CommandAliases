package me.kneesnap.CommandAliases;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Decompilation of the original plugin.
 * Created sometime in 2016.
 */
public class CommandAliases extends JavaPlugin {
    public static CommandAliases INSTANCE;
    public static Updater UPDATER;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        INSTANCE = this;
        UPDATER = new Updater(this);
        getCommand("alias").setExecutor(new CmdAlias());
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        if (!UPDATER.isUpToDate())
            getLogger().info("There's an update available!");
    }

    @Override
    public File getFile() {
        return super.getFile();
    }
}
