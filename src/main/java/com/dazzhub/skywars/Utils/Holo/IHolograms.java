package com.dazzhub.skywars.Utils.Holo;

import com.dazzhub.skywars.Utils.Console;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IHolograms {

    private Location location;
    private List<String> lines;

    private List<ArmorStand> armorlines;
    private double distance = 0.30;

    public IHolograms(Location location, List<String> lines){
        this.location = location;
        this.lines = lines;
        this.armorlines = new ArrayList<>();
    }


    public void spawn() {
        int ind = 0;

        for (String line : lines) {
            Location finalLoc = location.clone();

            finalLoc.setY(location.getY() + (distance * lines.size()));

            if (ind > 0) {
                finalLoc = armorlines.get(ind - 1).getLocation();
            }

            finalLoc.setY(finalLoc.getY() - distance);

            ArmorStand as = createAs(line, finalLoc);

            armorlines.add(as);
            ind++;
        }
    }

    public void update(){

        for (int i = 0; i < lines.size(); i++) {
            this.armorlines.get(i).setCustomName(lines.get(i));
        }

    }

    public void remove() {

        List<String> newLines = Lists.newArrayList(lines);
        List<ArmorStand> newArmorLines = Lists.newArrayList(armorlines);

        for (int i = 0; i < newLines.size(); i++) {
            newArmorLines.get(i).remove();
        }

        armorlines = new ArrayList<>();
        lines = new ArrayList<>();
    }


    private ArmorStand createAs(String line, Location loc){
        ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

        as.setCustomNameVisible(true);
        as.setSmall(true);

        as.setCustomName(line);

        as.setBasePlate(false);
        as.setGravity(false);
        as.setCanPickupItems(false);
        as.setVisible(false);
        as.setMarker(true);

        return as;
    }

}
