package com.dazzhub.skywars.Listeners.Bukkit;
import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class onLeftServer implements Listener {

    private Main main;

    public onLeftServer(Main main) {
        this.main = main;
    }

    @EventHandler
    public void PlayerLeftEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();
            LeftEvent leftEvent = new LeftEvent(p, arena, Enums.LeftCause.DISCONNECT);
            Bukkit.getPluginManager().callEvent(leftEvent);
        }

        main.getScoreBoardAPI().removeScoreBoard(p);
        if (gamePlayer.getHolograms() != null) gamePlayer.getHolograms().deleteHologram();
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> main.getPlayerDB().savePlayer(p.getUniqueId()));
    }
}
