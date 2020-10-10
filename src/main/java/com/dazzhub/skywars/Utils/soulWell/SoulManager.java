package com.dazzhub.skywars.Utils.soulWell;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.dazzhub.skywars.Utils.locUtils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SoulManager {

    private final Main main;

    private final List<SoulWell> soulWellList;
    private final List<ItemStack> itemStacks;
    private final ArrayList<Location> locations;

    public SoulManager(Main main) {
        this.main = main;
        this.soulWellList = new ArrayList<>();
        this.itemStacks = new ArrayList<>();
        this.locations = new ArrayList<>();
    }

    public void loadSoulWells(){
        Configuration config = main.getConfigUtils().getConfig(main,"SoulWell");

        if (config.getConfigurationSection("Locations") != null) {
            if (config.getConfigurationSection("Locations").getKeys(false) != null) {
                for (String key : config.getConfigurationSection("Locations").getKeys(false)) {
                    locations.add(locUtils.stringToLoc(config.getString("Locations." + key)));
                }
            }
        }

        config.getConfigurationSection("Soul").getKeys(false).forEach(types -> config.getConfigurationSection("Soul." + types).getKeys(false).forEach(key -> {
            this.soulWellList.add(new SoulWell(Integer.parseInt(key), types));
            String path = "Soul." + types + "." + key;
            Material material = config.isInt(path + ".ICON.ICON-ITEM") ? Material.getMaterial(config.getInt(path + ".ICON.ICON-ITEM")) : Material.getMaterial(path + ".ICON.ICON-ITEM");
            this.itemStacks.add(new Icon(XMaterial.matchXMaterial(material), 1, (short) config.getInt(path + ".ICON.DATA-VALUE")).setName(config.getString(path + ".NAME")).setLore(config.getStringList(path + ".ICON.DESCRIPTION")).build());
        }));

        Console.info("&eLoaded SoulWell:&a " + locations.size());
    }

    public void preOpen(GamePlayer gamePlayer, String type, int chance) {
        SoulWell well = getMysteryBox(type, chance);

        if (well != null){
            well.openSoulWell(gamePlayer);
        } else {
            Console.warning("SoulWell error open");
        }
    }

    public SoulWell getMysteryBox(String type, int i) {
        return this.soulWellList.stream().filter(mysteryBox -> mysteryBox.getChance() == i && mysteryBox.getType().equals(type)).findFirst().orElse(null);
    }

}
