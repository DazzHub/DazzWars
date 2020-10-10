package com.dazzhub.skywars.Commands.sub;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.subCommand;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class reload implements subCommand {

    private Main main;

    public reload(Main main) {
        this.main = main;
        adminCmd.subCommandHashMap.put("reload", this);
    }

    @Override
    public void onCommand(CommandSender sender, Command cmd, String[] args) {

        if (!sender.hasPermission("skywars.admin")) {
            return;
        }

        main.getMenuManager().reloadMenu();
        sender.sendMessage(c("&a&l\u2714 &fMenus reloaded"));

        main.getArenaManager().getArenas().values().forEach(arena -> {
            arena.getPlayers().forEach(arena::removePlayer);
            arena.setGameStatus(Enums.GameStatus.DISABLED);
        });

        Bukkit.getOnlinePlayers().forEach(on -> {
            if (main.getLobbyManager().getLobby() != null) {
                on.teleport(main.getLobbyManager().getLobby());
            }

            main.getItemManager().giveItems(on, main.getSettings().getString("Inventory.Lobby"), true);
        });

        main.getArenaManager().loadArenas();

        sender.sendMessage(c("&a&l\u2714 &fArenas reloaded"));
    }

    @Override
    public String help(CommandSender sender) {
        return c("&e/sw reload &8>&f reload configs");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
