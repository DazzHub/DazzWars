package com.dazzhub.skywars.Utils.holoChest;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Holo.IHolograms;
import com.dazzhub.skywars.Utils.locUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class IHoloChest {

    private final Main main;

    private final Arena arena;
    private final HashMap<String, IHolograms> armorStandList;

    public IHoloChest(Arena arena) {
        this.main = Main.getPlugin();

        this.arena = arena;
        this.armorStandList = new HashMap<>();
    }

    public void addHolo(Block block) {
        String locString = locUtils.locToStringSimple(block.getLocation());

        if (this.armorStandList.containsKey(locUtils.locToStringSimple(block.getLocation()))) return;

        Location loc = new Location(block.getLocation().getWorld(), block.getLocation().getX() + 0.5, block.getLocation().getY() + 1.0, block.getLocation().getZ() + 0.5);

        IHolograms holo = new IHolograms(loc, new ArrayList<>());

        holo.getLines().add(c(main.getSettings().getString("refillHolo.line1")).replace("%time%", arena.refillTimer()));
        holo.getLines().add(c("&f"));

        holo.spawn();

        this.armorStandList.put(locString, holo);
    }

    public void updateHolo(){

        if (armorStandList.isEmpty()) return;

        for (String location : this.armorStandList.keySet()) {

            IHolograms holo = this.armorStandList.get(location);
            if (holo == null) {
                continue;
            }

            if (armorStandList.containsKey(location)) {

                if (holo.getLines() == null || holo.getLines().isEmpty()) {
                    continue;
                }

                holo.getLines().set(0, c(main.getSettings().getString("refillHolo.line1")).replace("%time%", arena.refillTimer()));

                Location loc1 = locUtils.stringToLocNoCenter(location);

                if (loc1 == null){
                    continue;
                }

                if (loc1.getBlock().isEmpty() || loc1.getBlock().getType() == Material.AIR) {
                    delete(loc1);
                    continue;
                }

                Chest chest = (Chest) loc1.getBlock().getState();

                if (checkInv(chest.getBlockInventory())) {
                    holo.getLines().set(1, c(main.getSettings().getString("refillHolo.line2")).replace("%time%", arena.refillTimer()));
                } else {
                    holo.getLines().set(1, c("&f"));
                }

                holo.update();
            }
        }

    }

    public void deleteAll() {
        List<IHolograms> as = new ArrayList<>(this.armorStandList.values());

        if (!as.isEmpty()){
            as.forEach(IHolograms::remove);
        }
    }

    public void delete(Location loc) {
        String locString = locUtils.locToStringSimple(loc);

        if (armorStandList.containsKey(locString)) {
            armorStandList.get(locString).remove();
            armorStandList.remove(locString);
        }
    }

    private boolean checkInv(Inventory inv){
        boolean empty = true;

        for (ItemStack i : inv){
            if (i != null){
                empty = !true;
                break;
            }
        }

        return empty;

    }

    private String c(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
