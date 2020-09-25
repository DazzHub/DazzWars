package com.dazzhub.skywars.Commands.sub.holograms;

import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Cuboid;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class addHologram implements subCommand {

    private Main main;

    public addHologram(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("addhologram", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!(sender instanceof Player)) {
            return;
        }

        Player p = (Player) sender;
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (args.length == 2) {

            String typeHologram = args[1].toUpperCase();

            if (gamePlayer.getHologramMessage().getConfigurationSection("TypeHolograms").getKeys(false).contains(typeHologram)) {
                main.getHologramsManager().createHologram(p, typeHologram);
                p.sendMessage(c("&a&l\u2714 &fHologram &e" + typeHologram + "&f add to location!"));
            } else {
                p.sendMessage("&c&l\u2718 &fThis hologram is not on the list");
            }

        } else {
            p.sendMessage(help(sender));
        }

    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/Sw addholgoram <type> &8>&f Add cages");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
