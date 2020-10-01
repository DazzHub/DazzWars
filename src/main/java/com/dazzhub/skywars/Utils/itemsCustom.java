package com.dazzhub.skywars.Utils;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;


@Getter
@Setter
public class itemsCustom {

    private Main main;

    private ItemStack addSpawn;
    private ItemStack setSpectator;
    private ItemStack addChestCenter;

    private ItemStack cageWand;
    private ItemStack soulWand;

    public itemsCustom(Main main) {
        this.main = main;

        cageWand = new Icon(XMaterial.STICK)
                .setName(c("&6&l&nCAGE WAND"))
                .setLore(c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                        c(""),
                        c("&8» &eLeft click to select the first corner"),
                        c("&8» &eRight click to select the second corner"),
                        c(""),
                        c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"))
                .build();

        soulWand = new Icon(XMaterial.BLAZE_ROD)
                .setName(c("&6&l&nSOUL WAND"))
                .setLore(c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                        c(""),
                        c("&8» &eLeft click to block for set location soulwell"),
                        c(""),
                        c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"))
                .build();
    }

    public ItemStack addSpawn(String namea){
        this.addSpawn = new Icon(XMaterial.STICK)
                .setName(c("&6&l&nADD SPAWNS:&r &e&l") + namea)
                .setLore(c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                        c(""),
                        c("&8» &eLeft click to add spawns"),
                        c("&8» &eRight click to remove previus spawn"),
                        c(""),
                        c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")).build();
        return addSpawn;
    }

    public ItemStack setSpectator(String namea) {
        this.setSpectator = new Icon(XMaterial.STICK)
                .setName(c("&6&l&nSET SPECTATOR:&r &e&l") + namea)
                .setLore(c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                        c(""),
                        c("&8» &eLeft click to set spectator spawn"),
                        c("&8» &eRight click to remove spawn spectator"),
                        c(""),
                        c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")).build();
        return setSpectator;
    }

    public ItemStack addChestCenter(String namea) {
        this.addChestCenter = new Icon(XMaterial.STICK)
                .setName(c("&6&l&nADD CENTER CHEST:&r &e&l") + namea)
                .setLore(c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"),
                        c(""),
                        c("&8» &eLeft click to add center chest"),
                        c("&8» &eRight click to remove center chest"),
                        c(""),
                        c("&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬")).build();
        return addChestCenter;
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
