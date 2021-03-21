package com.dazzhub.skywars.Utils.inventory.Item;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.dazzhub.skywars.Utils.inventory.listEnchants;
import com.dazzhub.skywars.Utils.inventory.menu.IMenuLang;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class IItemManager {

    private final Main main;
    private final HashMap<String, IItemLang> itemLangs;

    public IItemManager(Main main) {
        this.main = main;
        this.itemLangs = new HashMap<>();
    }

    public void loadFiles() {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            loadDefaultFiles();

            File[] fileList = new File(main.getDataFolder(), "Inventory/Player").listFiles();

            if (fileList == null) return;

            for (File file : fileList) {
                if (file.isDirectory()) {
                    itemLangs.put(file.getName(), new IItemLang(main, file.getName()));
                }
            }
        });
    }

    private void loadDefaultFiles() {
        File playerFolder = new File(main.getDataFolder(), "Inventory/Player");

        if (!playerFolder.exists()) {
            //es-ES
            configCreate.get().setup(main, "Inventory/Player/es-ES/lobby");
            configCreate.get().setup(main, "Inventory/Player/es-ES/arenasolo");
            configCreate.get().setup(main, "Inventory/Player/es-ES/arenateam");
            configCreate.get().setup(main, "Inventory/Player/es-ES/arenaranked");
            configCreate.get().setup(main, "Inventory/Player/es-ES/spectator");

            //en-EN
            configCreate.get().setup(main, "Inventory/Player/en-EN/lobby");
            configCreate.get().setup(main, "Inventory/Player/en-EN/arenasolo");
            configCreate.get().setup(main, "Inventory/Player/en-EN/arenateam");
            configCreate.get().setup(main, "Inventory/Player/en-EN/arenaranked");
            configCreate.get().setup(main, "Inventory/Player/en-EN/spectator");
        }

    }

    public HashMap<String, IItemLang> getItemLangs() {
        return itemLangs;
    }
}