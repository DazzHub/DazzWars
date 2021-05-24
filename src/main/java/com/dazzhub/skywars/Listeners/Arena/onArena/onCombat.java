package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.DeathEvent;
import com.dazzhub.skywars.Listeners.Custom.PlayerDamageByPlayerEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class onCombat implements Listener {

    private final Main main;

    public onCombat(Main main) {
        this.main = main;
    }

    @EventHandler
    public void PlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) return;

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if (arena.getGameStatus().equals(Enums.GameStatus.WAITING) || arena.getGameStatus().equals(Enums.GameStatus.STARTING)) {
                e.setCancelled(true);
            }

            if (gamePlayer.isSpectating()) {
                e.setCancelled(true);
            }

            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (gamePlayer.isSpectating() || arena.getGameStatus() == Enums.GameStatus.RESTARTING) {
                    p.setFallDistance(0);
                    p.teleport(arena.getSpawnSpectator());
                    e.setCancelled(true);
                }
            }

            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (arena.isDamageFallStarting()) {
                    e.setCancelled(true);
                }

                if (arena.isNoFall()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {

            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(e.getEntity().getUniqueId());
            GamePlayer gamePlayerDamage = main.getPlayerManager().getPlayer(e.getDamager().getUniqueId());

            if (gamePlayer == null || gamePlayerDamage == null) {
                return;
            }

            if (!gamePlayer.isInArena() || !gamePlayerDamage.isInArena()) return;

            if (gamePlayerDamage.isSpectating()) {
                e.setCancelled(true);
            }

            if (gamePlayerDamage.getArenaTeam() != null) {
                if (gamePlayerDamage.getArenaTeam().hasPlayer(gamePlayer)) {
                    e.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    public void entityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) return;

        boolean isPlayerDamager = e.getDamager() instanceof Player;
        boolean isPlayerDamaged = e.getEntity() instanceof Player;

        if (!isPlayerDamager) return;
        if (!isPlayerDamaged) return;

        Player damager = (Player) e.getDamager();
        Player damaged = (Player) e.getEntity();

        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(damaged.getUniqueId());
        GamePlayer gamePlayerDamage = main.getPlayerManager().getPlayer(damager.getUniqueId());

        if (gamePlayer == null || gamePlayerDamage == null) {
            return;
        }

        if (!gamePlayer.isInArena() || !gamePlayerDamage.isInArena()) return;

        if (gamePlayerDamage.isSpectating()) {
            return;
        }

        if (gamePlayerDamage.getArenaTeam() != null) {
            if (gamePlayerDamage.getArenaTeam().hasPlayer(gamePlayer)) {
                return;
            }
        }

        double damage = e.getDamage();
        EntityDamageEvent.DamageCause cause = e.getCause();

        PlayerDamageByPlayerEvent damage1 = new PlayerDamageByPlayerEvent(damager, damaged, damage, cause);
        Bukkit.getPluginManager().callEvent(damage1);
        e.setCancelled(damage1.isCancelled());

        if (!damage1.isCancelled()) {
            e.setDamage(damage1.getDamage());
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void detectTagged(PlayerDamageByPlayerEvent e) {
        if (e.isCancelled()) return;
        long time = System.currentTimeMillis();

        main.getPlayerManager().getTaggedCooldown().addTagged(e.getDamager(), e.getDamaged());
        main.getPlayerManager().getTaggedCooldown().addTime(e.getDamager(), time);
    }

    @EventHandler
    public void PlayerDied(PlayerDeathEvent e) {
        Player p = e.getEntity();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) return;
        if (!gamePlayer.isInArena()) return;

        boolean isTagged = main.getPlayerManager().getTaggedCooldown().isTagged(p);
        e.setDeathMessage(null);

        if (isTagged) {
            GamePlayer gameKiller = main.getPlayerManager().getPlayer(main.getPlayerManager().getTaggedCooldown().getKiller(p));

            Bukkit.getPluginManager().callEvent(new DeathEvent(gamePlayer, gameKiller, gamePlayer.getArena(), e));
        } else {
            if (e.getEntity().getKiller() == null) {
                return;
            }

            GamePlayer gameKiller = main.getPlayerManager().getPlayer(e.getEntity().getKiller().getUniqueId());

            Bukkit.getPluginManager().callEvent(new DeathEvent(gamePlayer, gameKiller, gamePlayer.getArena(), e));
        }

        e.setDeathMessage(null);
    }

    @EventHandler
    public void ArrowHits(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Arrow) {
            final Arrow a = (Arrow) e.getDamager();
            if (a.getShooter() instanceof Player) {
                Player p = (Player) a.getShooter();

                GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

                if (gamePlayer == null) {
                    return;
                }

                if (gamePlayer.isInArena()) {
                    Arena arena = gamePlayer.getArena();

                    if (arena.getGameStatus() == Enums.GameStatus.INGAME) {
                        switch (arena.getMode()) {
                            case SOLO: {
                                gamePlayer.addHitsSolo();
                                break;
                            }
                            case TEAM: {
                                gamePlayer.addHitsTeam();
                                break;
                            }
                            case RANKED: {
                                gamePlayer.addHitsRanked();
                                break;
                            }
                        }

                        main.getAchievementManager().checkPlayer(p, Enums.AchievementType.PROJECTILES_HIT, gamePlayer.totalHits());
                    }


                    if (e.getDamager() instanceof Projectile || e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                        if (arena.isNoProjectile()) {
                            if (gamePlayer.getLangMessage().getBoolean("Messages.notifyVotes.Scenario.Projectile.enabled")) {
                                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.notifyVotes.Scenario.Projectile.isProjectile", "Error notifyVotes.Scenario.Projectile.isProjectile"));
                            }
                            e.setCancelled(true);
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void ArrowShot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player)e.getEntity();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            if (gamePlayer == null) {
                return;
            }

            if (gamePlayer.isInArena()) {
                Arena arena = gamePlayer.getArena();

                if (arena.getGameStatus() == Enums.GameStatus.INGAME) {
                    switch (arena.getMode()){
                        case SOLO:{
                            gamePlayer.addShotsSolo();
                            break;
                        }
                        case TEAM:{
                            gamePlayer.addShotsTeam();
                            break;
                        }
                        case RANKED:{
                            gamePlayer.addShotsRanked();
                            break;
                        }
                    }
                    main.getAchievementManager().checkPlayer(p, Enums.AchievementType.PROJECTILES_SHOT, gamePlayer.totalShots());
                }
            }
        }
    }

}
