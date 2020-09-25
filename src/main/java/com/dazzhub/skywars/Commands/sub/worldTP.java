package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

        if (!(sender instanceof Player)){
            return;
        }

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        if (args.length < 2){
            return;
        }

        Player p = (Player) sender;

        String sub = args[1];
        if (new File(main.getServer().getWorldContainer(), sub).exists()) {
            createWorld(sub);

            Location loc = Bukkit.getWorld(sub).getSpawnLocation();

            Bukkit.getScheduler().scheduleSyncDelayedTask(main, () ->
                    p.teleport(loc)
            , 5);

        } else {
            p.sendMessage(help(sender));
        }
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/sw world <name> &8>&f Teleport to world");
    }

    /* CREATE WORLD */
    private void createWorld(String world){
        WorldCreator wc = new WorldCreator(world);

        wc.type (WorldType.FLAT);
        wc.generatorSettings("36");
        wc.generateStructures(false);

        Bukkit.createWorld(wc);

        this.main.getServer().getWorlds().add(Bukkit.getServer().getWorld(world));
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
