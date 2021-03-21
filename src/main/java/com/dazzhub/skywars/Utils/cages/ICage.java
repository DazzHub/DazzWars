package com.dazzhub.skywars.Utils.cages;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Cuboid;
import com.dazzhub.skywars.Utils.inventory.Icon;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Setter
public class ICage {

    private Main main;

    private String name;

    private int xDiff;
    private int yDiff;
    private int zDiff;

    private List<String> storedBlocks;

    public ICage(String name, int xDiff, int yDiff, int zDiff, List<String> storedBlocks) {
        this.main = Main.getPlugin();
        this.name = name;
        this.xDiff = xDiff;
        this.yDiff = yDiff;
        this.zDiff = zDiff;
        this.storedBlocks = storedBlocks;
    }

    public void loadCage(Location loc) {
        Location point1 = new Location(loc.getWorld(), loc.getX() - getXDiff() / 2.0, loc.getY() - getYDiff() / 2.0, loc.getZ() - getZDiff() / 2.0);
        Location point2 = new Location(loc.getWorld(), loc.getX() + getXDiff() / 2.0, loc.getY() + getYDiff() / 2.0, loc.getZ() + getZDiff() / 2.0);

        Cuboid cuboid = new Cuboid(point1, point2);

        int index = 0;

        for (Block block : cuboid) {

            if (main.checkVersion()) {

                if (!storedBlocks.get(index).contains(":")) {
                    XMaterial xMaterial = XMaterial.valueOf(storedBlocks.get(index));

                    if (xMaterial.parseItem() != null){
                        ItemStack item = new ItemStack(xMaterial.parseItem());
                        block.setType(item.getType());
                        block.setData(item.getData().getData());
                    } else {
                        Console.error("Did not find the block type");
                        break;
                    }
                } else {
                    String[] type = storedBlocks.get(index).split(":");

                    block.setType(Material.valueOf(type[0]));
                    block.setData((byte) Integer.parseInt(type[1]));
                }
            } else {
                Material material = XMaterial.matchXMaterial(storedBlocks.get(index)).get().parseMaterial();
                block.setType(material);
            }

            index++;
        }
    }

    public void removeCage(Location loc) {
        Location loc1 = new Location(loc.getWorld(), loc.getX() - getXDiff() / 2.0, loc.getY() - getYDiff() / 2.0, loc.getZ() - getZDiff() / 2.0);
        Location loc2 = new Location(loc.getWorld(), loc.getX() + getXDiff() / 2.0, loc.getY() + getYDiff() / 2.0, loc.getZ() + getZDiff() / 2.0);

        Cuboid cuboid = new Cuboid(loc1, loc2);

        for (Block block : cuboid) {
            block.setType(Material.AIR);
        }
    }
}
