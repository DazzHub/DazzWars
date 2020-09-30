package com.dazzhub.skywars.Listeners.Lobby;

import com.dazzhub.skywars.Main;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;

public class onPlayer implements Listener {

    private Main main;

    public onPlayer(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent e) {
        if(main.getSettings().getStringList("lobbies.onFeed").contains(e.getEntity().getWorld().getName())) {
            e.setFoodLevel(20);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerClickTab(PlayerChatTabCompleteEvent e) {
        e.getTabCompletions().clear();
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (main.getSettings().getStringList("lobbies.onDrop").contains(p.getWorld().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void noUproot(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL) {
            if (main.getSettings().getStringList("lobbies.onBreak").contains(e.getPlayer().getWorld().getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        if (main.getSettings().getStringList("lobbies.onResetGameMode").contains(p.getWorld().getName())) {
            p.setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler
    public void onPortal(PortalCreateEvent e) {
        String world = e.getWorld().getName();
        if (main.getSettings().getStringList("lobbies.onPortal").contains(world)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        String world = e.getLocation().getWorld().getName();
        if (main.getSettings().getStringList("lobbies.onExplosion").contains(world)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent e) {
        String world = e.getSource().getLocation().getWorld().getName();
        if (main.getSettings().getStringList("lobbies.onFlow").contains(world)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        String world = event.getPlayer().getWorld().getName();
        if (main.getSettings().getStringList("lobbies.onFlow").contains(world)) {
            event.setCancelled(true);
        }
    }
}
