package com.dazzhub.skywars.Utils.events.dragon;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class Dragon implements eventDragon {

    private Main main;
    private Arena arena;

    private Monster dragon;
    private int timer;

    public Dragon(Arena arena) {
        this.main = Main.getPlugin();
        this.arena = arena;
        this.timer = arena.getArenac().getInt("Arena.dragon.TimeSpawn");
    }

    @Override
    public void startEvent() {
        new BukkitRunnable() {
            @Override
            public void run() {

                arena.getPlayers().forEach(gamePlayer -> {
                    Set<String> keys = gamePlayer.getLangMessage().getConfigurationSection("Messages.TypeEvent.Dragon.Starting").getKeys(false);
                    keys.stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TypeEvent.Dragon.Starting." + time_config).replaceAll("%seconds%", String.valueOf(timer))));
                });

                if (timer <= 1){
                    this.cancel();

                    Bukkit.getScheduler().runTask(main, () -> {
                        World world = Bukkit.getWorld(arena.getNameWorld());
                        Location loc = arena.getSpawnSpectator().add(0,5,0);

                        dragon = (Monster) world.spawnEntity(loc, EntityType.ENDER_DRAGON);

                        dragon.setCustomName(c(arena.getArenac().getString("Arena.dragon.Name")));
                        dragon.setCustomNameVisible(true);
                    });

                }

                timer--;
            }
        }.runTaskTimerAsynchronously(main,0,20);
    }

    @Override
    public void killDragon() {
        if (dragon == null) return;

        dragon.remove();
    }

    private String c(final String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
