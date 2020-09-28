package com.dazzhub.skywars.Utils.signs.arena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Utils.signs.ISignLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ISignManager {

    private Main main;

    private HashMap<Location, Arena> signs;

    public ISignManager(Main main){
        this.main = main;
        this.signs = new HashMap<>();
    }

    public ISign loadSign(Arena arena) {
        Configuration arenac = main.getConfigUtils().getConfig(main, "Arenas/" + arena.getNameArena() + "/Settings");

        if (!arenac.getString("Arena.sign", "").isEmpty()) {
            Location location = ISignLocation.getLocation(arenac.getString("Arena.sign"));

            Block block = location.getWorld().getBlockAt(location);

            ISign iSign = new ISign((Sign) block.getState(), arena);

            signs.put(location, arena);
            return iSign;
        } else {
            return null;
        }

    }

    public void createSigns(Player p, Sign sign, Arena arena) {

        if (signs.containsKey(sign.getLocation())){
            p.sendMessage(c("&a&l\u2714 &fThe sign for the arena &9" + arena.getNameArena() + "&f already exist!"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
            return;
        }

        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + arena.getNameArena() + "/Settings");
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + arena.getNameArena() + "/Settings");
        Location location = ISignLocation.getLocation(ISignLocation.getString(sign.getLocation(), false));

        ISign as = new ISign(sign, arena);
        arena.setISign(as);
        as.updateSign();

        config.set("Arena.sign", ISignLocation.getString(sign.getLocation(), false));

        try {
            config.save(file);
        } catch (IOException e) {
            Console.error(e.getMessage());
        }

        signs.put(location, arena);

        p.sendMessage(c("&a&l\u2714 &fThe sign for the arena &9" + arena.getNameArena() + "&f created successfully!"));
        XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_YES.parseSound()));
    }

    public void removeSign(Player player, Arena arena, Location location){
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + arena.getNameArena() + "/Settings");
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + arena.getNameArena() + "/Settings");

        config.set("Arena.sign", "");
        try {
            config.save(file);
        } catch (IOException e) {
            Console.error(e.getMessage());
        }

        arena.setISign(null);

        signs.remove(location);

        player.sendMessage(c("&a&l\u2714 &fRemoved game sign"));
        XSound.play(player, String.valueOf(XSound.BLOCK_ANVIL_USE.parseSound()));
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

    public HashMap<Location, Arena> getSigns() {
        return signs;
    }
}
