package com.ar.askgaming.nightmare.Controllers;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.ar.askgaming.nightmare.NightMare;

public class Commands implements TabExecutor {

    private final NightMare plugin;

    public Commands() {
        this.plugin = NightMare.getInstance();

        plugin.getServer().getPluginCommand("nightmare").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("Use /nightmare help for more information.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadConfig();
                plugin.getLang().clearCache();
                sender.sendMessage("Config reloaded.");
                break;
        
            default:
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

}
