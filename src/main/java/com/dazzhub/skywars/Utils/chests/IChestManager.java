package com.dazzhub.skywars.Utils.chests;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class IChestManager {

    private Main main;
    private HashMap<String, IChest> chestHashMap;

    public IChestManager(Main main) {
        this.main = main;
        this.chestHashMap = new HashMap<>();
    }

    public void loadChests() {

        List<String> chestList = main.getConfigUtils().getConfig(this.main, "Chests/ChestType").getStringList("Types");

        for (String chest : chestList) {
            if (!new File(this.main.getDataFolder(), "Chests/chest/" + chest).exists()) {
                configCreate.get().setup(main, "Chests/chest/" + chest);
            }

            this.chestHashMap.put(chest, new IChest(this.main, chest));
        }

        Console.info("&eLoaded chests: &a" + getChestHashMap().size());
    }

    public HashMap<String, IChest> getChestHashMap() {
        return chestHashMap;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
