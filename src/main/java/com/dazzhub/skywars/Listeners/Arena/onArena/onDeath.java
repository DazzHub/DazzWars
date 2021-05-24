package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.comparables.comparator;
import com.dazzhub.skywars.Listeners.Custom.DeathEvent;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Listeners.Custom.WinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class onDeath implements Listener {

    private Main main;

    public onDeath(Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeathPlayer(DeathEvent e) {
        Arena arena = e.getArena();

        GamePlayer dead = e.getDead();
        GamePlayer killer = e.getKiller();

        Player p = dead.getPlayer();

        if (arena.getGameStatus().equals(Enums.GameStatus.INGAME) && p.getHealth() < 0.4) {

            switch (arena.getMode()) {
                case SOLO: {
                    dead.addDeathsSolo();
                    dead.removeCoins(main.getSettings().getInt("Coins.DeathSolo"));
                    break;
                }

                case TEAM: {
                    dead.addDeathsTeam();
                    dead.removeCoins(main.getSettings().getInt("Coins.DeathTeam"));
                    break;
                }

                case RANKED: {
                    dead.addDeathsRanked();
                    dead.removeLvlRanked(main.getSettings().getInt("Coins.lvlRankedLosed"));
                    dead.removeCoins(main.getSettings().getInt("Coins.DeathRanked"));
                    break;
                }
            }

            p.setFallDistance(0);
            p.setHealth(20.0D);
            p.setHealthScale(20);

            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                if (!dead.isAutomatic()){
                    arena.addSpectator(dead);
                } else {
                    if (dead.isInArena()) {
                        comparator.checkArenaPlayer(main.getArenaManager().getArenaList());

                        Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                                .filter(Arena::checkUsable)
                                .findAny().orElse(null);

                        if (arenaTo == null) {
                            arena.addSpectator(dead);
                        } else {
                            dead.getArena().removePlayer(dead, false);
                            Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));
                        }
                    }
                }
            }, 5);

        }

        if (arena.getAliveTeams().size() <= 1) {
            p.setFallDistance(0);
            p.setHealth(20.0D);
            p.setHealthScale(20);
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> p.teleport(arena.getSpawnSpectator()), 5);
        }

        if (!arena.getKillers().containsKey(dead.getName())){
            arena.getKillers().put(dead.getName(), 0);
        }

        if (killer != null) {
            killer.addKillsArena();

            if (arena.isNoClean()){
                killer.getPlayer().setHealth(killer.getPlayer().getKiller().getMaxHealth());
            }

            killer.addSouls(1);

            switch (arena.getMode()) {
                case SOLO: {
                    killer.addKillsSolo();

                    if (arena.getKillers().isEmpty()){
                        killer.addCoins(main.getSettings().getDouble("Coins.FirstKillSolo"));
                        killer.sendMessage(killer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.FirstKillSolo"))));
                    } else {
                        killer.addCoins(main.getSettings().getDouble("Coins.KillSolo"));
                        killer.sendMessage(killer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.KillSolo"))));
                    }

                    getTypeKills effect = killer.getTypeKill(killer.getKillEffectSolo());
                    if (effect != null) effect.playKillEffect();
                    break;
                }
                case TEAM: {
                    killer.addKillsTeam();

                    if (arena.getKillers().isEmpty()){
                        killer.addCoins(main.getSettings().getDouble("Coins.FirstKillTeam"));
                        killer.sendMessage(killer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.FirstKillTeam"))));
                    } else {
                        killer.addCoins(main.getSettings().getDouble("Coins.KillTeam"));
                        killer.sendMessage(killer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.KillTeam"))));
                    }

                    getTypeKills effect = killer.getTypeKill(killer.getKillEffectTeam());
                    if (effect != null) effect.playKillEffect();
                    break;
                }
                case RANKED: {
                    killer.addKillsRanked();

                    if (arena.getKillers().isEmpty()){
                        killer.addCoins(main.getSettings().getDouble("Coins.FirstKillRanked"));
                        killer.sendMessage(killer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.FirstKillRanked"))));
                    } else {
                        killer.addCoins(main.getSettings().getDouble("Coins.KillRanked"));
                        killer.sendMessage(killer.getLangMessage().getString("Messages.GiveCoins").replace("%coins%", String.valueOf(main.getSettings().getInt("Coins.KillRanked"))));
                    }

                    getTypeKills effect = killer.getTypeKill(killer.getKillEffectRanked());
                    if (effect != null) effect.playKillEffect();
                    break;
                }
            }

            arena.getKillers().put(killer.getName(), arena.getKillers().containsKey(killer.getName()) ? (arena.getKillers().get(killer.getName()) + 1) : 1);
            main.getAchievementManager().checkPlayer(p, Enums.AchievementType.KILLS, main.getPlayerManager().getPlayer(p.getUniqueId()).totalKills());
        }

        messageDeathPlayer(e);
    }


    public void messageDeathPlayer(DeathEvent e) {
        GamePlayer killerg = e.getKiller();
        GamePlayer dead = e.getDead();
        PlayerDeathEvent event = e.getEvent();

        dead.getArena().getPlayers().forEach(gamePlayer -> {

            if (event == null){
                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Player", "Error DeathMessages.Player")
                        .replace("%player%", dead.getPlayer().getName())
                        .replace("%killer%", killerg.getPlayer().getName())
                        .replaceAll("%blocks%", String.valueOf(checkDistance(dead.getPlayer(), killerg.getPlayer())))
                        .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        .replaceAll("%k_kill_count%", String.valueOf(killerg.getKillsStreak()))
                );
                return;
            }

            if (killerg != null) {
                switch (event.getEntity().getLastDamageCause().getCause()) {
                    case ENTITY_ATTACK: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Player", "Error DeathMessages.Player")
                                .replace("%player%", dead.getPlayer().getName())
                                .replace("%killer%", killerg.getPlayer().getName())
                                .replaceAll("%blocks%", String.valueOf(checkDistance(dead.getPlayer(), killerg.getPlayer())))
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                                .replaceAll("%k_kill_count%", String.valueOf(killerg.getKillsStreak()))
                        );
                        break;
                    }
                    case PROJECTILE: {
                        if ((event.getEntity().getLastDamageCause()) instanceof Arrow) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Arrow", "Error DeathMessages.Arrow")
                                    .replace("%player%", dead.getPlayer().getName())
                                    .replace("%killer%", killerg.getPlayer().getName())
                                    .replaceAll("%blocks%", String.valueOf(checkDistance(dead.getPlayer(), killerg.getPlayer())))
                                    .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                                    .replaceAll("%k_kill_count%", String.valueOf(killerg.getKillsStreak()))
                            );
                        } else if ((event.getEntity().getLastDamageCause()) instanceof EnderPearl) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.EnderPearl", "Error DeathMessages.EnderPearl")
                                    .replace("%player%", dead.getPlayer().getName())
                                    .replace("%killer%", killerg.getPlayer().getName())
                                    .replaceAll("%blocks%", String.valueOf(checkDistance(dead.getPlayer(), killerg.getPlayer())))
                                    .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                                    .replaceAll("%k_kill_count%", String.valueOf(killerg.getKillsStreak()))
                            );
                        }
                        break;
                    }
                }
            } else {
                switch (event.getEntity().getLastDamageCause().getCause()) {
                    case VOID: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Void", "Error DeathMessages.Void")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                    case FIRE_TICK: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Burned", "Error DeathMessages.Burned")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                    case FALL: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Fall", "Error DeathMessages.Fall")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                    case ENTITY_EXPLOSION: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Explosion", "Error DeathMessages.Explosion")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                    case LAVA: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Lava", "Error DeathMessages.Lava")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                    case DROWNING: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Water", "Error DeathMessages.Water")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                    case SUFFOCATION: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Suffocation", "Error DeathMessages.Suffocation")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                    case LIGHTNING: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.Lightning", "Error DeathMessages.Lightning")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                    default: {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.DeathMessages.UnknownCause", "Error DeathMessages.UnknownCause")
                                .replace("%player%", dead.getPlayer().getName())
                                .replaceAll("%p_kill_count%", String.valueOf(dead.getKillsStreak()))
                        );
                        break;
                    }
                }
            }
        });

    }

    private int checkDistance(Player player, Entity entity) {
        if (entity != null) {
            if (player.getWorld() == entity.getWorld()) {
                return (int) player.getLocation().distance(entity.getLocation());
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
