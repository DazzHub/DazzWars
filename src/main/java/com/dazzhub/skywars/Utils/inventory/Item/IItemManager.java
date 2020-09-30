package com.dazzhub.skywars.Utils.inventory.Item;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.dazzhub.skywars.Utils.inventory.listEnchants;
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

    private Main main;
    private HashMap<String, IItem> itemByFile;

    public IItemManager(Main main) {
        this.main = main;
        this.itemByFile = new HashMap<>();
    }

    public void loadFiles() {
        File playerFolder = new File(main.getDataFolder(), "Inventory/Player");
        if (!playerFolder.exists()) {
            configCreate.get().setup(main, "Inventory/Player/lobby");
            configCreate.get().setup(main, "Inventory/Player/arenasolo");
            configCreate.get().setup(main, "Inventory/Player/arenateam");
            configCreate.get().setup(main, "Inventory/Player/arenaranked");
            configCreate.get().setup(main, "Inventory/Player/spectator");
        }

        File file = new File(main.getDataFolder(), "Inventory/Player");
        File[] files = file.listFiles();

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            if (files != null && files.length > 0) {
                File[] array;
                for (int length = (array = files).length, i = 0; i < length; ++i) {
                    File f = array[i];
                    if (f != null) {
                        String filename = f.getName();
                        if (filename.endsWith(".yml")) {
                            loadMenus(filename);
                        }
                    }
                }
            }
        });
    }

    private void loadMenus(String filename) {

        FileConfiguration config = this.main.getConfigUtils().getConfig2(this.main, "Inventory/Player/" + filename);
        Set<String> menuNodes = config.getKeys(false);

        HashMap<Integer, ordItems> items = new HashMap<>();
        menuNodes.forEach(s -> {
            ConfigurationSection confSection = config.getConfigurationSection(s);

                String command = confSection.getString("ACTION");

                String name = confSection.getString("NAME");
                String permission = confSection.getString("PERMISSION");
                List<String> description = confSection.getStringList("DESCRIPTION");
                String skullplayer = confSection.getString("SKULL-OWNER");

                int amount = confSection.getInt("ICON-AMOUNT");
                short data = (short) confSection.getInt("DATA-VALUE");

                int slot = confSection.getInt("SLOT");

                String interact = confSection.getString("INTERACT");

                List<String> enchants = confSection.getStringList("ENCHANTMENTS");
                HashMap<Enchantment, Integer> enchantments = new HashMap<>();

                if (!enchants.isEmpty()) {
                    enchants.stream().map(enchantfor -> enchantfor.split(":")).forEach(enchantsplit -> enchantments.put(listEnchants.getEnchantmentFromString(enchantsplit[0]), Integer.parseInt(enchantsplit[1])));
                }

                short durability = (short) confSection.getInt("DURABILITY");

                Material material = confSection.isInt("ICON-ITEM") ? Material.getMaterial(confSection.getInt("ICON-ITEM")) : Material.getMaterial(confSection.getString("ICON-ITEM"));

                Icon icon = new Icon(XMaterial.matchXMaterial(material), amount, data)
                        .setName(name)
                        .setLore(description)
                        .setSkull(skullplayer)
                        .addEnchantment(enchantments)
                        .addDamage(durability)
                ;

                items.put(slot, new ordItems(icon, slot, command, permission, interact, 0));

        });

        IItem item = new IItem(items);

        itemByFile.put(filename.replace(".yml", ""), item);
    }

    public void giveItems(Player p, String Type, boolean clearinv) {
        if (clearinv) {
            ItemStack[] emptyinv = new ItemStack[p.getInventory().getContents().length];
            p.getInventory().setContents(emptyinv);
        }

        IItem item = getItemByFile().get(Type);

        if (item != null) {
            item.createItem(p);
        }

    }

    public HashMap<String, IItem> getItemByFile() {
        return itemByFile;
    }
}
