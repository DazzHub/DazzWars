package com.dazzhub.skywars.Utils.kits;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class IKit {

    private String nameKit;

    public IKit(String nameKit) {
        this.nameKit = nameKit;
    }

    public void giveKit(Player p, String mode) {
        Configuration config = Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(), "Kits/" + mode + "/" + nameKit);

        ItemStack[] invContents = new ItemStack[p.getInventory().getContents().length];

        for (int i = 0; i < p.getInventory().getContents().length; ++i) {
            invContents[i] = config.getItemStack("KIT.Inventory." + i);
            if (invContents[i] != null) {
                p.getInventory().setItem(i, invContents[i]);
            }
        }

        ItemStack[] acontents = p.getInventory().getArmorContents();
        List<?> alist = config.getList("KIT.Armor");
        for (int j = 0; j < alist.size(); ++j) {
            acontents[j] = (ItemStack)alist.get(j);
        }

        p.getInventory().setArmorContents(acontents);
    }
}
