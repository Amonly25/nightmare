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
            case "start":
                start(sender, args);
                break;
            case "stop":
                stop(sender, args);
                break;
            case "help":
            default:
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("reload", "start", "stop", "help");
        }
        if (args.length == 2){
            if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("stop")) {
                return List.of("bloodmoon", "headlesshorseman");
            }
        }
        return null;
    }
    private void start(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /nightmare start <event>");
            return;
        }
        switch (args[1].toLowerCase()) {
            case "bloodmoon":
                plugin.getNightManager().getBloodMoon().start();
                break;
            case "headlesshorseman":
                plugin.getNightManager().getHeadLessHorseMan().start();
                break;
            default:
                sender.sendMessage("Unknown event: " + args[1]);
                break;
        }
    }
    private void stop(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /nightmare stop <event>");
            return;
        }
        switch (args[1].toLowerCase()) {
            case "bloodmoon":
                plugin.getNightManager().getBloodMoon().end();
                break;
            case "headlesshorseman":
                plugin.getNightManager().getHeadLessHorseMan().end();
                break;
            default:
                sender.sendMessage("Unknown event: " + args[1]);
                break;
        }
    }

}
