package com.dazzhub.skywars.Utils.chests;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void editChest(String type, int percent, Player player) {
        IChest chest = this.getChestHashMap().get(type);
        if (chest == null) return;

        Map<Integer, Inventory> toEdit = chest.getChestItemList();

        String fileName = chest.getNameChest();
        if (!toEdit.containsKey(percent)) {
            toEdit.put(percent, Bukkit.createInventory(player, 54, "editChest/" + fileName + "/" + percent));
        }

        toEdit.get(percent).setItem(53, new Icon(XMaterial.matchXMaterial(Material.STAINED_GLASS_PANE), 1, (short) 5).setName("&aSAVE").build(player));
        toEdit.get(percent).setItem(52, new Icon(XMaterial.matchXMaterial(Material.STAINED_GLASS_PANE), 1, (short) 14).setName("&cCANCEL").build(player));

        player.openInventory(toEdit.get(percent));
    }

    public void save(String title, Player p) {
        IChest chest = this.getChestHashMap().get(ChatColor.stripColor(title));

        if (chest != null) {
            this.save(chest.getChestItemList(), chest);

            p.sendMessage(c("&a&l\u2714 &f Items successfully added"));
            XSound.play(p, String.valueOf(XSound.BLOCK_ANVIL_USE));
        } else {
            p.sendMessage(c("&c&l\u2718 &fError adding items"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO));
        }
    }

    private void save(Map<Integer, Inventory> chestList, IChest chest) {
        String fileName = chest.getNameChest();

        File chestFile = new File(main.getDataFolder(), "Chests/chest/" + fileName + ".yml");
        if (!chestFile.exists()) {
            main.saveResource(fileName, false);
        } else {
            try {
                FileConfiguration storage = YamlConfiguration.loadConfiguration(chestFile);
                for (int percent : chestList.keySet()) {
                    List<ItemStack> items = new ArrayList<>();
                    for (ItemStack item : chestList.get(percent).getContents()) {
                        if (item != null && !item.getType().equals(XMaterial.matchXMaterial(Material.AIR).parseMaterial()) && !item.getType().equals(XMaterial.matchXMaterial(Material.STAINED_GLASS_PANE).parseMaterial())) {
                            items.add(item);
                        }
                    }

                    storage.set("chestItems." + percent + ".items", items);
                }

                storage.save(chestFile);
            } catch (IOException ioException) {
                Console.error("Failed to save chestfile " + fileName + ": " + ioException.getMessage());
            }
        }
    }

    public HashMap<String, IChest> getChestHashMap() {
        return chestHashMap;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
