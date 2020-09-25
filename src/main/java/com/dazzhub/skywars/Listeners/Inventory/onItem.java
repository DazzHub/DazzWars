package com.dazzhub.skywars.Listeners.Inventory;

import com.dazzhub.skywars.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class onItem implements Listener {

    private Main main;

    public onItem(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            main.getItemManager().getItemByFile().keySet().stream().map(inv -> main.getItemManager().getItemByFile().get(inv)).filter(Objects::nonNull).forEach(item -> item.onPlayerInteract(event, null, null, null));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            Player target = (Player) e.getRightClicked();
            if (target == null) return;
            main.getItemManager().getItemByFile().keySet().stream().map(inv -> main.getItemManager().getItemByFile().get(inv)).filter(Objects::nonNull).forEach(item -> item.onPlayerInteract(null, e, null, target));
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player target = (Player) event.getEntity();
            if (target == null) return;
            main.getItemManager().getItemByFile().keySet().stream().map(inv -> main.getItemManager().getItemByFile().get(inv)).filter(Objects::nonNull).forEach(item -> item.onPlayerInteract(null, null, event, target));
        }
    }
}
