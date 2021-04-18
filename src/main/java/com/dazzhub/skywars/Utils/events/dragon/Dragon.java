package com.dazzhub.skywars.Utils.events.dragon;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class Dragon implements eventDragon {

    private final Main main;
    private final Arena arena;

    private EnderDragon dragon;
    private int task;
    private int timer;

    public Dragon(Arena arena) {
        this.main = Main.getPlugin();
        this.arena = arena;
        this.timer = arena.getArenaConfig().getInt("Arena.dragon.TimeSpawn",120);
    }

    @Override
    public void startEvent() {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {

                arena.getPlayers().forEach(gamePlayer -> {
                    Set<String> keys = gamePlayer.getLangMessage().getConfigurationSection("Messages.TypeEvent.Dragon.Starting").getKeys(false);
                    keys.stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TypeEvent.Dragon.Starting." + time_config, "Error TypeEvent.Dragon.Starting." + time_config).replaceAll("%seconds%", String.valueOf(timer))));
                });

                if (timer <= 1){
                    this.cancel();

                    Bukkit.getScheduler().runTask(main, () -> {
                        World world;

                        if (arena.getResetArena() == Enums.ResetArena.SLIMEWORLDMANAGER) {
                            world = Bukkit.getWorld(arena.getUuid());
                        } else {
                            world = Bukkit.getWorld(arena.getNameWorld());
                        }

                        Location loc = arena.getSpawnSpectator().add(0,5,0);

                        dragon = (EnderDragon) world.spawnEntity(loc, EntityType.ENDER_DRAGON);

                        dragon.setCustomName(c(arena.getArenaConfig().getString("Arena.dragon.Name", "&bDragon")));
                        dragon.setCustomNameVisible(true);
                    });

                }

                timer--;
            }
        }.runTaskTimer(main,0,20).getTaskId();
    }

    @Override
    public void killDragon() {
        if (Bukkit.getScheduler().isCurrentlyRunning(task)) {
            Bukkit.getScheduler().cancelTask(task);
            task = 0;
        }

        if (dragon == null) return;

        dragon.remove();
    }

    @Override
    public int getTimer() {
        return timer;
    }

    private String c(final String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
