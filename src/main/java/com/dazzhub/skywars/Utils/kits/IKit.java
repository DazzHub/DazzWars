package com.dazzhub.skywars.Utils.kits;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.dazzhub.skywars.Utils.xseries.XMaterial;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class IKit {

    private String nameKit;
    private String actionKit;
    private int price;

    private String nameItemKit;
    private List<String> loreKit;
    private List<String> loreKitPurchased;
    private List<String> loreKitSelected;
    private Material material;
    private short dataValue;
    private String permission;
    private int slot;

    private Icon Icon;

    public IKit(String nameKit, String actionKit, int price, String nameItemKit, List<String> loreKit, List<String> loreKitPurchased, List<String> loreKitSelected, Material material, short value, String permission, int slot) {
        this.nameKit = nameKit;
        this.actionKit = actionKit;
        this.price = price;
        this.nameItemKit = nameItemKit;
        this.loreKit = loreKit;
        this.loreKitPurchased = loreKitPurchased;
        this.loreKitSelected = loreKitSelected;
        this.material = material;
        this.dataValue = value;
        this.permission = permission;
        this.slot = slot;
        this.Icon = new Icon(XMaterial.matchXMaterial(material),1, dataValue);
    }

    public Icon icon(){
        return Icon.setName(nameItemKit).setLore(loreKit).setKit(price, nameKit, loreKitPurchased, loreKitSelected);
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
