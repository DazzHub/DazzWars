package com.dazzhub.skywars.Utils.inventory.Item;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.dazzhub.skywars.Utils.inventory.listEnchants;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import lombok.Getter;
import lombok.Setter;
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

@Getter
@Setter
public class IItemLang {

    private final Main main;

    private final String lang;

    private final HashMap<String, IItem> itemByFile;

    public IItemLang(Main main, String lang) {
        this.main = main;
        this.lang = lang;

        this.itemByFile = new HashMap<>();

        loadFiles();
    }

    public void reloadItems() {
        itemByFile.clear();

        loadFiles();
    }

    public void loadFiles() {
        File[] files = new File(main.getDataFolder(), "Inventory/Player/" + lang).listFiles();

        if (files == null) return;

        for (File file : files){
            String filename = file.getName();
            if (filename.endsWith(".yml")) {
                loadMenus(filename);
            }
        }

    }

    private void loadMenus(String filename) {

        FileConfiguration config = this.main.getConfigUtils().getConfig2(this.main, "Inventory/Player/" + lang + "/" + filename);
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

            Material material;

            if (main.checkVersion()) {
                material = confSection.isInt("ICON-ITEM") ? Material.getMaterial(confSection.getInt("ICON-ITEM")) : Material.getMaterial(confSection.getString("ICON-ITEM"));
            } else {
                material = Material.getMaterial(confSection.getString("ICON-ITEM"));
            }

            if (material == null) {
                material = Material.BEDROCK;
            }

            Icon icon = new Icon(XMaterial.matchXMaterial(material), amount, data)
                    .setName(name)
                    .setLore(description)
                    .setSkull(skullplayer)
                    .addEnchantment(enchantments)
                    .addDamage(durability)
                    ;

            items.put(slot, new ordItems(icon, slot, command, permission, 0));

        });

        IItem item = new IItem(items);

        itemByFile.put(filename.replace(".yml", ""), item);
    }

    public void giveItems(Player p, String Type, boolean clearinv) {
        if (main.getSettings().getStringList("lobbies.onItemJoin").contains(p.getWorld().getName())) {
            return;
        }

        if (clearinv) {
            ItemStack[] emptyinv = new ItemStack[p.getInventory().getContents().length];
            p.getInventory().setContents(emptyinv);
        }

        IItem item = getItemByFile().get(Type);

        if (item != null) {
            item.createItem(p);
        }

    }
}
