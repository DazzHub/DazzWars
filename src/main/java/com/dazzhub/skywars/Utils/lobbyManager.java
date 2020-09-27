package com.dazzhub.skywars.Utils;

import com.dazzhub.skywars.Main;
import com.cryptomorin.xseries.XSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class lobbyManager
{
    private Main plugin;

    public lobbyManager(Main plugin) {
        this.plugin = plugin;
    }

    public Location getLobby() {
        FileConfiguration config = this.plugin.getConfigUtils().getConfig(this.plugin, "Arenas/Lobby");
        return locUtils.stringToLoc(config.getString("Lobby"));
    }

    public void setLobby(Player p) {
        FileConfiguration config = this.plugin.getConfigUtils().getConfig(this.plugin, "Arenas/Lobby");
        config.set("Lobby", locUtils.locToString(p.getLocation()));
        try {
            config.save(this.plugin.getConfigUtils().getFile(this.plugin, "Arenas/Lobby"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        p.sendMessage(this.c("&a&l\u2714&f You set the &9Lobby"));
        XSound.play(p, String.valueOf(XSound.ENTITY_CHICKEN_EGG.parseSound()));
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
