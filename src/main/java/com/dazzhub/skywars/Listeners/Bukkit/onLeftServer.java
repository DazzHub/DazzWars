package com.dazzhub.skywars.Listeners.Bukkit;
import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.DeathEvent;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Party.Party;
import com.dazzhub.skywars.Utils.Enums;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class onLeftServer implements Listener {

    private Main main;

    public onLeftServer(Main main) {
        this.main = main;
    }

    @EventHandler
    public void PlayerLeftEvent(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        boolean isTagged = main.getPlayerManager().getTaggedCooldown().isTagged(p);

        main.getPlayerManager().getTaggedCooldown().removeDeath(p);
        main.getPlayerManager().getTaggedCooldown().removeTimer(p);

        if (isTagged){
            GamePlayer gameKiller = main.getPlayerManager().getPlayer(main.getPlayerManager().getTaggedCooldown().getKiller(p));

            Bukkit.getPluginManager().callEvent(new DeathEvent(gamePlayer, gameKiller, gamePlayer.getArena(), null));
        }

        if (gamePlayer == null) return;

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();
            LeftEvent leftEvent = new LeftEvent(p, arena, Enums.LeftCause.DISCONNECT);
            Bukkit.getPluginManager().callEvent(leftEvent);
        }

        Party party = gamePlayer.getParty();
        if (party != null) {
            if (party.getOwner().equals(gamePlayer)) {
                main.getPartyManager().removeParty(gamePlayer);
                party.broadcast("Messages.Party.OwnerDisconnect", p.getName(), "");
            } else {
                party.getMembers().remove(gamePlayer);
                gamePlayer.setParty(null);

                party.broadcast("Messages.Party.MemberDisconnect", "", p.getName());
            }
        }

        if (gamePlayer.getHolograms() != null) gamePlayer.getHolograms().deleteHologram();


        Bukkit.getScheduler().runTaskAsynchronously(main, () -> main.getPlayerDB().savePlayer(p.getUniqueId()));
    }

}
