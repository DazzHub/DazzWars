package com.dazzhub.skywars.Utils.cages;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Cuboid;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.dazzhub.skywars.Utils.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;

@Getter
@Setter
public class ICage {

    private Main main;

    private String name;

    private int xDiff;
    private int yDiff;
    private int zDiff;

    private List<String> storedBlocks;

    //////////////////////////////////
    private String actionCage;
    private int price;

    private String nameItemCage;
    private List<String> loreCage;
    private List<String> loreCagePurchased;
    private List<String> loreCageSelected;
    private Material material;
    private short dataValue;
    private String permission;
    private int slot;

    private Icon Icon;

    public ICage(String name, int xDiff, int yDiff, int zDiff, List<String> storedBlocks,

                 String actionCage,
                 int price,
                 String nameItemCage,
                 List<String> loreCage,
                 List<String> loreCagePurchased,
                 List<String> loreCageSelected,
                 Material material,
                 short value,
                 String permission,
                 int slot
    ) {
        this.main = Main.getPlugin();
        this.name = name;
        this.xDiff = xDiff;
        this.yDiff = yDiff;
        this.zDiff = zDiff;
        this.storedBlocks = storedBlocks;
        /////////////////////////////////
        this.actionCage = actionCage;
        this.price = price;
        this.nameItemCage = nameItemCage;
        this.loreCage = loreCage;
        this.loreCagePurchased = loreCagePurchased;
        this.loreCageSelected = loreCageSelected;
        this.material = material;
        this.dataValue = value;
        this.permission = permission;
        this.slot = slot;
        this.Icon = new Icon(XMaterial.matchXMaterial(material),1, dataValue);
    }

    public Icon icon(){
        return Icon.setName(nameItemCage).setLore(loreCage).setCage(price, name, loreCagePurchased, loreCageSelected);
    }

    public void loadCage(Location loc) {
        Location point1 = new Location(loc.getWorld(), loc.getX() - getXDiff() / 2.0, loc.getY() - getYDiff() / 2.0, loc.getZ() - getZDiff() / 2.0);
        Location point2 = new Location(loc.getWorld(), loc.getX() + getXDiff() / 2.0, loc.getY() + getYDiff() / 2.0, loc.getZ() + getZDiff() / 2.0);

        Cuboid cuboid = new Cuboid(point1, point2);

        int index = 0;

        for (Block block : cuboid) {
            Material material = XMaterial.matchXMaterial(storedBlocks.get(index)).get().parseMaterial();

            block.setType(material);

            if (main.checkVersion()) {
                block.setData(XMaterial.matchXMaterial(storedBlocks.get(index)).get().getData());
            }

            index++;
        }
    }

}
