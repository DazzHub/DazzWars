package com.dazzhub.skywars.Listeners.Lobby;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class onMobs implements Listener {

    private final Main main;

    public onMobs(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onSpawn(EntitySpawnEvent e) {
        if (e.getEntity() instanceof Monster) {
            Entity entity = e.getEntity();
            World world = entity.getLocation().getWorld();
            if (main.getSettings().getStringList("lobbies.onMonsters.").contains(world.getName())) {
                entity.remove();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSpawn2(CreatureSpawnEvent e) {
        if (e.getEntity() instanceof Monster) {
            Entity entity = e.getEntity();
            World world = entity.getLocation().getWorld();
            if (main.getSettings().getStringList("lobbies.onMonsters.").contains(world.getName())) {
                e.setCancelled(true);
                entity.remove();
            }
        }
    }

    @EventHandler
    public void onSpawn3(EntitySpawnEvent e) {
        if (e.getEntity() instanceof Animals) {
            Entity entity = e.getEntity();
            World world = entity.getLocation().getWorld();
            if (main.getSettings().getStringList("lobbies.onAnimals.").contains(world.getName())) {
                entity.remove();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSpawn4(CreatureSpawnEvent e) {
        if (e.getEntity() instanceof Animals) {
            Entity entity = e.getEntity();
            World world = entity.getLocation().getWorld();
            if (main.getSettings().getStringList("lobbies.onAnimals.").contains(world.getName())) {
                e.setCancelled(true);
                entity.remove();
            }
        }
    }
}
