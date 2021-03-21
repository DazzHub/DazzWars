package com.dazzhub.skywars.Listeners.Lobby;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class onDamage implements Listener {

    private Main main;

    public onDamage(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerDamageLobby(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))  return;

        Player p = (Player) e.getEntity();

        if (main.getSettings().getStringList("lobbies.onDamage").contains(p.getWorld().getName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerDamageLobby(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();

        if ("parcelasoficial".equalsIgnoreCase(p.getWorld().getName())) {
            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (main.getLobbyManager().getLobby() != null){
                    p.teleport(main.getLobbyManager().getLobby());
                }
            }
        }
    }
}
