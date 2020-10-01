package com.dazzhub.skywars.Utils.soulWell;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.inventory.Icon;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SoulManager {

    private Main main;

    private List<SoulWell> soulWellList;
    private List<ItemStack> itemStacks;

    public SoulManager(Main main) {
        this.main = main;
        this.soulWellList = new ArrayList<>();
        this.itemStacks = new ArrayList<>();
    }

    public void loadSoulWells(){
        Configuration config = main.getConfigUtils().getConfig(main,"SoulWell");

        config.getConfigurationSection("Soul").getKeys(false).forEach(types -> config.getConfigurationSection("Soul." + types).getKeys(false).forEach(key -> {
            this.soulWellList.add(new SoulWell(Integer.parseInt(key), types));
            String path = "Soul." + types + "." + key;
            Material material = config.isInt(path + ".ICON.ICON-ITEM") ? Material.getMaterial(config.getInt(path + ".ICON.ICON-ITEM")) : Material.getMaterial(path + ".ICON.ICON-ITEM");
            this.itemStacks.add(new Icon(XMaterial.matchXMaterial(material), 1, (short) config.getInt(path + ".ICON.DATA-VALUE")).setName(config.getString(path + ".NAME")).setLore(config.getStringList(path + ".ICON.DESCRIPTION")).build());
        }));

        Console.info("&eLoaded SoulWell: &a: " + soulWellList.size());
    }

    public void preOpen(GamePlayer gamePlayer, String type, int chance) {
        SoulWell well = getMysteryBox(type, chance);
        if (well != null){
            well.openSoulWell(gamePlayer);
        } else {
            System.out.println("Upss");
        }
    }

    public SoulWell getMysteryBox(String type, int i) {
        for (SoulWell mysteryBox : this.soulWellList) {
            if (mysteryBox.getChance() == i && mysteryBox.getType().equals(type)) {
                return mysteryBox;
            }
        }
        return null;
    }
}
