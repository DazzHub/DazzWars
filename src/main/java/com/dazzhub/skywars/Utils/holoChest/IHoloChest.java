package com.dazzhub.skywars.Utils.holoChest;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Holo.IHolograms;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
public class IHoloChest {

    private final Main main;

    private final Arena arena;
    private final HashMap<Chest, IHolograms> armorStandList;

    public IHoloChest(Arena arena) {
        this.main = Main.getPlugin();

        this.arena = arena;
        this.armorStandList = new HashMap<>();
    }

    public void addHolo(Chest chest) {
        if (this.armorStandList.containsKey(chest)) return;

        Location loc = new Location(chest.getLocation().getWorld(), chest.getLocation().getX() + 0.5, chest.getLocation().getY() + 1.0, chest.getLocation().getZ() + 0.5);

        IHolograms holo = new IHolograms(loc, new ArrayList<>());

        holo.getLines().add(c(main.getSettings().getString("refillHolo.line1")).replace("%time%", refill()));
        holo.getLines().add(c("&f"));

        holo.spawn();

        this.armorStandList.put(chest, holo);
    }

    public void updateHolo(){

        if (armorStandList.isEmpty()) return;

        for (Chest chest : this.armorStandList.keySet()) {

            IHolograms holo = this.armorStandList.get(chest);
            if (holo == null) continue;

            holo.getLines().set(0, c(main.getSettings().getString("refillHolo.line1")).replace("%time%", refill()));

            if (checkInv(chest.getBlockInventory())) {
                holo.getLines().set(1, c(main.getSettings().getString("refillHolo.line2")).replace("%time%", refill()));
            } else {
                holo.getLines().set(1, c("&f"));
            }

            holo.update();
        }

    }

    public void deleteAll() {
        List<IHolograms> as = new ArrayList<>(this.armorStandList.values());

        if (!as.isEmpty()){
            as.forEach(IHolograms::remove);
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

    private String refill(){
        if (arena.getRefillGame() != null && arena.getRefillGame().getTimer() >= 1) {
            return String.valueOf(arena.calculateTime(arena.getRefillGame().getTimer()));
        } else if (arena.getRefillGame() == null && !arena.getRefillTime().isEmpty()) {
            return String.valueOf(arena.calculateTime(0));
        } else {
            return "0";
        }
    }

    private String c(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
