package com.dazzhub.skywars.Listeners.Lobby;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class onPlace implements Listener {

    private Main main;

    public onPlace(Main main) {
        this.main = main;
    }

    @EventHandler
    public void PlayerBuild(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (main.getSettings().getStringList("lobbies.onPlace").contains(p.getWorld().getName())) {
            if (!gamePlayer.isEditMode()) {
                e.setBuild(false);
                e.setCancelled(true);
            }
        }
    }

}
