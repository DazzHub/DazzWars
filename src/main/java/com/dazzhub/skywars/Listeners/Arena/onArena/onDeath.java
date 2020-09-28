package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.DeathEvent;
import com.dazzhub.skywars.Listeners.Custom.WinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onDeath implements Listener {

    private Main main;

    public onDeath(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onDeathPlayer(DeathEvent e) {
        Arena arena = e.getArena();

        GamePlayer dead = e.getDead();
        GamePlayer killer = e.getKiller();

        Player p = dead.getPlayer();

        if (arena.getGameStatus().equals(Enums.GameStatus.INGAME) && p.getHealth() < 0.4) {

            switch (arena.getMode()) {
                case SOLO:
                    dead.addDeathsSolo();
                    dead.removeCoins(main.getSettings().getInt("Coins.DeathSolo"));
                    break;
                case TEAM:
                    dead.addDeathsTeam();
                    dead.removeCoins(main.getSettings().getInt("Coins.DeathTeam"));
                    break;
            }

            p.setFallDistance(0);
            p.setHealth(20.0D);
            p.setHealthScale(20);

            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> arena.addSpectator(dead), 5);

        }

        if (arena.getAliveTeams().size() <= 1) {
            p.setFallDistance(0);
            p.setHealth(20.0D);
            p.setHealthScale(20);
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> p.teleport(arena.getSpawnSpectator()), 5);
        }


        if (killer != null) {
            killer.addKillsArena();
            arena.getKillers().put(killer.getName(), arena.getKillers().containsKey(killer.getName()) ? (arena.getKillers().get(killer.getName()) + 1) : 1);
            switch (arena.getMode()) {
                case SOLO: {
                    killer.addKillsSolo();
                    getTypeKills effect = killer.getTypeKill(killer.getKillEffectSolo());
                    if (effect != null) effect.playKillEffect();
                    break;
                }
                case TEAM: {
                    killer.addKillsTeam();
                    getTypeKills effect = killer.getTypeKill(killer.getKillEffectTeam());
                    if (effect != null) effect.playKillEffect();
                    break;
                }
            }
        }
    }
}
