package com.dazzhub.skywars.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Console {

    public static void info(String message){
        Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &e" + message));
    }

    public static void warning(String message){
        Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cWARNING&8> &e" + message));
    }

    public static void error(String message){
        Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cERROR&8> &e" + message));
    }

    private static String c(String msg){ return ChatColor.translateAlternateColorCodes('&', msg); }

}
