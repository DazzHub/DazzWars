package com.dazzhub.skywars.Utils.inventory.menu;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class IMenuLang {

    private Main main;

    private String lang;

    private HashMap<String, IMenu> menuFileName;
    private HashMap<String, String> menuCommand;

    public IMenuLang(Main main, String lang) {
        this.main = main;
        this.lang = lang;

        this.menuFileName = new HashMap<>();
        this.menuCommand = new HashMap<>();

        loadFileLangs();
    }

    public void reloadMenu() {
        menuFileName.clear();
        menuCommand.clear();

        loadFileLangs();
    }

    private void loadFileLangs() {
        File[] files = new File(main.getDataFolder(), "Inventory/Menu/" + lang).listFiles();

        if (files == null) return;

        for (File file : files) {
            String filename = file.getName();
            if (filename.endsWith(".yml")) {
                loadMenu(filename);
            }
        }

    }

    private void loadMenu(String filename) {

        FileConfiguration config = this.main.getConfigUtils().getConfig2(this.main, "Inventory/Menu/" + lang + "/" + filename);
        Set<String> menuNodes = config.getKeys(false);

        String menuName = config.getString("menu-settings.name");
        int menuRows = config.getInt("menu-settings.rows");
        String menuCommand = config.getString("menu-settings.command");

        Material materialview = null;
        short dataView = 0;
        List<String> loreView = null;
        boolean enableIcon = false;
        String skullPerms = null;

        if (config.getString("menu-settings.permissionView.ICON-ITEM") != null) {
            materialview = config.isInt("menu-settings.permissionView.ICON-ITEM") ? Material.getMaterial(config.getInt("menu-settings.permissionView.ICON-ITEM")) : Material.getMaterial(config.getString("menu-settings.permissionView.ICON-ITEM"));
            dataView = Short.parseShort(config.getString("menu-settings.permissionView.DATA-VALUE"));
            loreView = config.getStringList("menu-settings.permissionView.DESCRIPTION");
            enableIcon = config.getBoolean("menu-settings.permissionView.ICON-VIEW");
            skullPerms = config.getString("menu-settings.permissionView.SKULL-OWNER");
        }

        if (menuName == null || menuName.length() == 0) {
            Console.info("[Menu] The menu§2 " + filename + " §ecan't load: It was not found 'name', check your menu-settings");
            return;
        }

        if (menuRows == 0) {
            Console.info("[Menu] The menu§2 " + filename + " §ecan't load: It was not found 'row', check your menu-settings");
            return;
        }

        menuName = "§r" + menuName.replace("&", "§");
        if (menuName.length() > 32) {
            menuName = "§rError, name too long!";
        }

        Material finalMaterialview = materialview;
        short finalDataView = dataView;
        List<String> finalLoreView = loreView;
        boolean finalEnableIcon = enableIcon;
        String finalSkullPerms = skullPerms;

        HashMap<Integer, ordItems> items = new HashMap<>();
        menuNodes.forEach(s -> {
            ConfigurationSection confSection = config.getConfigurationSection(s);
            if (!s.equals("menu-settings")) {
                int price = confSection.getInt("PRICE");
                String command = confSection.getString("ACTION");

                String name = confSection.getString("NAME");
                String permission = confSection.getString("PERMISSION");

                List<String> description = confSection.getStringList("DESCRIPTION");
                List<String> descriptionpurchased = confSection.getStringList("DESCRIPTION-PURCHASED");
                List<String>  descriptionselected = confSection.getStringList("DESCRIPTION-SELECTED");

                String skullplayer = confSection.getString("SKULL-OWNER");

                int amount = confSection.getInt("ICON-AMOUNT");
                short data = (short) confSection.getInt("DATA-VALUE");

                int slotX = confSection.getInt("POSITION-X");
                int slotY = confSection.getInt("POSITION-Y");

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
                        .setLorePurchased(descriptionpurchased)
                        .setLoreSelected(descriptionselected)
                        .setPrice(price)
                        .setType(command)
                        .setSkull(skullplayer)
                        .addEnchantment(enchantments)
                        .addDamage(durability)
                        .addPermissionView(finalEnableIcon, permission, finalMaterialview, finalDataView, finalLoreView, finalSkullPerms)
                        ;

                items.put(Main.getRelativePosition(slotX, slotY), new ordItems(icon, Main.getRelativePosition(slotX, slotY), command, permission, price));
            }
        });

        IMenu menu = new IMenu(menuName, menuRows, menuCommand, items);

        this.menuFileName.put(filename, menu);

        if (!menuCommand.isEmpty()) {
            this.menuCommand.put(menuCommand.toLowerCase(), filename);
        }

    }

    public void openInventory(String inv, Player p){
        if (this.menuFileName.containsKey(inv)){
            this.menuFileName.get(inv).open(p);
        }
    }
}
