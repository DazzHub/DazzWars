package com.dazzhub.skywars.Listeners.Arena.onArena;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.DeathEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.meta.SkullMeta;

public class onArena implements Listener {

    private Main main;

    public onArena(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) {
            return;
        }

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();
            if (arena.getGameStatus().equals(Enums.GameStatus.INGAME)) {

                if (gamePlayer.isSpectating()) return;

                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.CHEST) && !arena.getIslandChest().contains(e.getClickedBlock().getLocation())) {
                    if (e.getClickedBlock().getType() != null && e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
                        BlockState block = e.getClickedBlock().getState();
                        if (block instanceof Chest) {
                            Chest chest = (Chest) block;

                            if (arena.getCenterChest().contains(e.getClickedBlock().getLocation())
                                    && !arena.getCenterChestCheck().contains(e.getClickedBlock().getLocation())
                                    && !arena.getChestsAddInGame().contains(e.getClickedBlock().getLocation())
                                    && !arena.getIslandChest().contains(e.getClickedBlock().getLocation())) {
                                main.getChestManager().getChestHashMap().get("CENTER").refillChest(chest);
                                arena.getCenterChestCheck().add(chest.getLocation());
                            }

                            if (!arena.getIslandChest().contains(e.getClickedBlock().getLocation())
                                    && !arena.getChestsAddInGame().contains(e.getClickedBlock().getLocation())
                                    && !arena.getCenterChest().contains(e.getClickedBlock().getLocation())
                                    && !arena.getCenterChestCheck().contains(e.getClickedBlock().getLocation())) {
                                main.getChestManager().getChestHashMap().get(arena.getChestType()).refillChest(chest);
                                arena.getIslandChest().add(chest.getLocation());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void PlayerSetChest(BlockPlaceEvent e) {
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer == null) {
            return;
        }
        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();
            if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.TRAPPED_CHEST) {
                if (arena.getGameStatus().equals(Enums.GameStatus.INGAME)) {
                    Location loc = e.getBlock().getLocation();
                    arena.getChestsAddInGame().add(loc);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer() != null) {
            Player p = e.getPlayer();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            if (gamePlayer == null) {
                return;
            }

            if (gamePlayer.isInArena()) {
                Arena arena = gamePlayer.getArena();
                if (arena.getGameStatus().equals(Enums.GameStatus.WAITING) || arena.getGameStatus().equals(Enums.GameStatus.STARTING)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void DropItems(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if (arena.getGameStatus().equals(Enums.GameStatus.WAITING) || arena.getGameStatus().equals(Enums.GameStatus.STARTING)) {
                e.setCancelled(true);
            }

            if (gamePlayer.isSpectating()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PickupItems(PlayerPickupItemEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if (arena.getGameStatus().equals(Enums.GameStatus.WAITING) || arena.getGameStatus().equals(Enums.GameStatus.STARTING)) {
                e.setCancelled(true);
            }

            if (gamePlayer.isSpectating()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void PlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if (arena.getGameStatus().equals(Enums.GameStatus.WAITING) || arena.getGameStatus().equals(Enums.GameStatus.STARTING)) {
                e.setCancelled(true);
            }

            if (gamePlayer.isSpectating()) {
                e.setCancelled(true);
            }

            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                if (gamePlayer.isSpectating()) {
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
    public void onMovePlayer(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();

        if (to == null || (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ())) {
            Player p = e.getPlayer();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            if (gamePlayer == null) return;

            if (gamePlayer.isInArena()) {
                Arena arena = gamePlayer.getArena();

                if (arena.getGameStatus().equals(Enums.GameStatus.WAITING) || arena.getGameStatus().equals(Enums.GameStatus.STARTING)) {
                    double distance = checkDistanceCage(p, gamePlayer.getArenaTeam().getSpawn());
                    if (distance > main.getSettings().getInt("ReTeleportCage")) {
                        p.teleport(gamePlayer.getArenaTeam().getSpawn());
                    }
                }
            }

        }
    }

    private int checkDistanceCage(Player player, Location loc) {
        if (loc != null) {
            if (player.getLocation().getWorld() == loc.getWorld()) {
                return (int) player.getLocation().distance(loc);
            } else {
                return 0;
            }
        } else {
            return 0;
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
                }
            }
        }
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
                    }


                    if (e.getDamager() instanceof Projectile || e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                        if (arena.isNoProjectile()) {
                            if (gamePlayer.getLangMessage().getBoolean("Messages.notifyVotes.Scenario.Projectile.enabled")) {
                                gamePlayer.sendMessage(c(gamePlayer.getLangMessage().getString("Messages.notifyVotes.Scenario.Projectile.isProjectile")));
                            }
                            e.setCancelled(true);
                        }

                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) {
            return;
        }

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if (arena.getGameStatus() == Enums.GameStatus.INGAME) {
                gamePlayer.addBlockPlaced();
            }
        }
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent e) {
        Player p = (Player) e.getWhoClicked();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) {
            return;
        }

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if (arena.getGameStatus() == Enums.GameStatus.INGAME) {
                gamePlayer.addItemCrafted();
            }
        }
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) {
            return;
        }

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if (arena.getGameStatus() == Enums.GameStatus.INGAME) {
                gamePlayer.addBlockBroken();
            }
        }
    }

    @EventHandler
    public void onEnchantItemEvent(EnchantItemEvent e) {
        Player p = e.getEnchanter();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) {
            return;
        }

        if (gamePlayer.isInArena()) {
            Arena arena = gamePlayer.getArena();

            if (arena.getGameStatus() == Enums.GameStatus.INGAME) {
                gamePlayer.addItemEnchanted();
            }
        }
    }

    @EventHandler
    public void PlayerDied(PlayerDeathEvent event) {
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(event.getEntity().getUniqueId());

        GamePlayer target = null;

        if (gamePlayer == null){
            return;
        }

        if (!gamePlayer.isInArena()){
            return;
        }

        if (event.getEntity().getKiller() != null) {
            target = main.getPlayerManager().getPlayer(event.getEntity().getKiller().getUniqueId());
        }

        DeathEvent deathEvent = new DeathEvent(gamePlayer, target, gamePlayer.getArena(), event);
        Bukkit.getPluginManager().callEvent(deathEvent);

        event.setDeathMessage(null);
    }

    @EventHandler
    public void onInventoryClickSpectator(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player) e.getWhoClicked();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        Configuration config = gamePlayer.getLangMessage();

        if (e.getView().getTitle().startsWith(c(config.getString("Messages.MenuSpectator.TITLE")))) {
            e.setCancelled(true);

            SkullMeta skullMeta = (SkullMeta)e.getCurrentItem().getItemMeta();

            for (int i = 0; i < 29; i++) {
                if (e.getSlot() == i) {
                    try {
                        Player p2 = Bukkit.getPlayer(skullMeta.getOwner());

                        if (p2 != null){
                            p.teleport(p2.getLocation().add(0.5,1.5,0.5));
                        }
                    } catch (Exception ignored){}
                }
            }

            if (e.getSlot() == Main.getRelativePosition(config.getInt("Messages.MenuSpectator.Close.POSITION-X"),config.getInt("Messages.MenuSpectator.Close.POSITION-Y"))) {
                p.closeInventory();
            }
        }
    }

    @EventHandler
    public void onTrails(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            if (gamePlayer.isInArena()) {
                Projectile proj = (Projectile) e.getProjectile();

                if (proj != null && !gamePlayer.getProjectilesList().contains(proj)) {
                    switch (gamePlayer.getArena().getMode()){
                        case SOLO:{
                            gamePlayer.getTrail(gamePlayer.getTrailSolo(), proj);
                            break;
                        }
                        case TEAM:{
                            gamePlayer.getTrail(gamePlayer.getTrailTeam(), proj);
                            break;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void offTrails(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            Projectile proj = e.getEntity();
            gamePlayer.getProjectilesList().remove(proj);
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
