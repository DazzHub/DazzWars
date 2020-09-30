package com.dazzhub.skywars.Listeners.Lobby;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class onBreak implements Listener {

    private Main main;

    public onBreak(Main main) {
        this.main = main;
    }

    @EventHandler
    public void PlayerBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (main.getSettings().getStringList("lobbies.onBreak").contains(p.getWorld().getName())) {
            if (!gamePlayer.isEditMode()) {
                e.setCancelled(true);
            }
        }
    }
}
