package com.dazzhub.skywars.Utils.chests;

import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class IChest {

    private Main main;

    private String nameChest;
    private Configuration config;

    private HashMap<Integer, Inventory> chestItemList;

    private Random r;
    private List<Integer> randomLoc;

    public IChest(Main main, String nameChest) {
        this.main = main;
        this.nameChest = nameChest;

        this.config = main.getConfigUtils().getConfig(this.main, "Chests/chest/" + nameChest);

        this.chestItemList = new HashMap<>();

        this.r = new Random();
        this.randomLoc = new ArrayList<>();

        this.loadChance();
    }

    private void loadChance() {
        if (config.getConfigurationSection("chestItems") != null) {
            for (String key : config.getConfigurationSection("chestItems").getKeys(false)) {
                if (isInteger(key)) {
                    int percent = Integer.parseInt(key);

                    List<?> items = config.getList("chestItems." + key + ".items");

                    chestItemList.put(percent, Bukkit.createInventory(null, 54, nameChest + " " + percent));

                    items.forEach(iStack -> chestItemList.get(percent).addItem((ItemStack) iStack));
                }
            }
        }
    }

    public void refillChest(Chest chest){
        Inventory inventory = null;

        if (chest != null) {
            inventory = chest.getInventory();
        }

        if (inventory == null) return;

        inventory.clear();

        int added = 0;

        this.randomLoc.clear();

        for (int i = 0; i < inventory.getSize(); ++i) {
            this.randomLoc.add(i);
        }

        Collections.shuffle(this.randomLoc);

        int maxi = main.getConfigUtils().getConfig(main, "Chests/ChestType").getInt("maxItemChest");

        for (int chance : chestItemList.keySet()) {
            for (ItemStack item : chestItemList.get(chance)) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    if (this.r.nextInt(100) + 1 <= chance) {

                        if (added <= r.nextInt(maxi)){
                            inventory.setItem(this.randomLoc.get(added), item);
                        } else {
                            break;
                        }

                        added++;
                    }
                }
            }
        }
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException | NullPointerException ex2) {
            return false;
        }
    }
}
