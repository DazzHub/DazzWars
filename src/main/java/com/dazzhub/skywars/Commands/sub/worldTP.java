package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;

public class worldTP implements subCommand {

    private Main main;

    public worldTP(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("world", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        Player p = (Player) sender;

        if (args.length < 2) {
            p.sendMessage(help(sender));
            return;
        }

        String sub = args[1];

        if (args.length == 3) {
            if (sub.equalsIgnoreCase("tp")) {
                String world = args[2];
                if (new File(main.getServer().getWorldContainer(), world).exists()) {
                    createWorld(world);

                    Location loc = Bukkit.getWorld(world).getSpawnLocation();

                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                        p.setGameMode(GameMode.CREATIVE);
                        p.teleport(loc);
                    }, 5);

                } else {
                    p.sendMessage(help(sender));
                }
            } else if (sub.equalsIgnoreCase("create")) {
                String world = args[2];

                createWorld(world);

                Location loc = Bukkit.getWorld(world).getSpawnLocation();

                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                    p.setGameMode(GameMode.CREATIVE);
                    p.teleport(loc);
                }, 5);
            }
        } else {
            if (sub.equalsIgnoreCase("info")) {
                p.sendMessage(c("&eWorld &8> &c" + p.getWorld().getName()));
            }
        }
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/sw world tp/create/info <name> &8>&f Manager world");
    }

    /* CREATE WORLD */
    private void createWorld(String world){
        WorldCreator wc = new WorldCreator(world);

        wc.type (WorldType.FLAT);
        wc.generatorSettings("36");
        wc.generateStructures(false);

        Bukkit.createWorld(wc);

        for (Entity e : Bukkit.getWorld(world).getEntities()){
            if (!(e instanceof Player)){
                e.remove();
            }
        }

        Bukkit.getWorld(world).setGameRuleValue("doMobSpawning","false");

        this.main.getServer().getWorlds().add(Bukkit.getServer().getWorld(world));
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
