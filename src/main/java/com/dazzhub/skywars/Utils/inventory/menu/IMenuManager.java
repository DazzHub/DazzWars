package com.dazzhub.skywars.Utils.inventory.menu;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.dazzhub.skywars.Utils.inventory.listEnchants;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
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
public class IMenuManager {

    private Main main;

    private HashMap<String, IMenu> menuFileName;
    private HashMap<String, IMenu> menuTileName;
    private HashMap<String, String> menuCommand;

    public IMenuManager(Main main) {
        this.main = main;

        this.menuFileName = new HashMap<>();
        this.menuTileName = new HashMap<>();
        this.menuCommand = new HashMap<>();
    }

    public void loadFiles() {
        File menuFolder = new File(main.getDataFolder(), "Inventory/Menu");
        if (!menuFolder.exists()) {
            configCreate.get().setup(main, "Inventory/Menu/arenas");

            configCreate.get().setup(main, "Inventory/Menu/vote");
            configCreate.get().setup(main, "Inventory/Menu/vote-chest");
            configCreate.get().setup(main, "Inventory/Menu/vote-heart");
            configCreate.get().setup(main, "Inventory/Menu/vote-time");
            configCreate.get().setup(main, "Inventory/Menu/vote-events");
            configCreate.get().setup(main, "Inventory/Menu/vote-scenarios");

            configCreate.get().setup(main, "Inventory/Menu/shop");
            configCreate.get().setup(main, "Inventory/Menu/shop-solo");
            configCreate.get().setup(main, "Inventory/Menu/shop-team");
            configCreate.get().setup(main, "Inventory/Menu/shop-ranked");

            configCreate.get().setup(main, "Inventory/Menu/cage-solo");
            configCreate.get().setup(main, "Inventory/Menu/cage-team");
            configCreate.get().setup(main, "Inventory/Menu/cage-ranked");

            configCreate.get().setup(main, "Inventory/Menu/killeffect-solo");
            configCreate.get().setup(main, "Inventory/Menu/killeffect-team");
            configCreate.get().setup(main, "Inventory/Menu/killeffect-ranked");

            configCreate.get().setup(main, "Inventory/Menu/kit-solo");
            configCreate.get().setup(main, "Inventory/Menu/kit-team");
            configCreate.get().setup(main, "Inventory/Menu/kit-ranked");

            configCreate.get().setup(main, "Inventory/Menu/wineffect-solo");
            configCreate.get().setup(main, "Inventory/Menu/wineffect-team");
            configCreate.get().setup(main, "Inventory/Menu/wineffect-ranked");

            configCreate.get().setup(main, "Inventory/Menu/traileffect-solo");
            configCreate.get().setup(main, "Inventory/Menu/traileffect-team");
            configCreate.get().setup(main, "Inventory/Menu/traileffect-ranked");

            configCreate.get().setup(main, "Inventory/Menu/lang-change");
        }

        File file = new File(main.getDataFolder(), "Inventory/Menu");
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

            Console.info("&eLoaded menus: &a"+getMenuFileName().size());
        });
    }

    public void reloadMenu() {
        menuFileName.clear();
        menuTileName.clear();
        menuCommand.clear();

        loadFiles();
    }

    private void loadMenus(String filename) {

        FileConfiguration config = this.main.getConfigUtils().getConfig2(this.main, "Inventory/Menu/" + filename);
        Set<String> menuNodes = config.getKeys(false);

        String menuName = config.getString("menu-settings.name");
        int menuRows = config.getInt("menu-settings.rows");
        String menuCommand = config.getString("menu-settings.command");

        Material materialview = null;
        short dataView = 0;
        List<String> loreView = null;
        boolean enableIcon = false;

        if (config.getString("menu-settings.permissionView.ICON-ITEM") != null) {
            materialview = config.isInt("menu-settings.permissionView.ICON-ITEM") ? Material.getMaterial(config.getInt("menu-settings.permissionView.ICON-ITEM")) : Material.getMaterial(config.getString("menu-settings.permissionView.ICON-ITEM"));
            dataView = Short.parseShort(config.getString("menu-settings.permissionView.DATA-VALUE"));
            loreView = config.getStringList("menu-settings.permissionView.DESCRIPTION");
            enableIcon = config.getBoolean("menu-settings.permissionView.ICON-VIEW");
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

                Material material = confSection.isInt("ICON-ITEM") ? Material.getMaterial(confSection.getInt("ICON-ITEM")) : Material.getMaterial(confSection.getString("ICON-ITEM"));

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
                        .addPermissionView(finalEnableIcon, permission, finalMaterialview, finalDataView, finalLoreView)
                ;

                items.put(Main.getRelativePosition(slotX, slotY), new ordItems(icon, Main.getRelativePosition(slotX, slotY), command, permission, null, price));
            }
        });

        IMenu menu = new IMenu(menuName, menuRows, menuCommand, items);

        this.menuFileName.put(filename, menu);
        this.menuTileName.put(menuName, menu);

        if (!menuCommand.isEmpty()) {
            this.menuCommand.put(menuCommand.toLowerCase(), filename);
        }
    }

    public void openInventory(String inv, Player p, String target){
        if (this.menuFileName.containsKey(inv)){
            this.menuFileName.get(inv).open(p, target);
        }
    }

}
