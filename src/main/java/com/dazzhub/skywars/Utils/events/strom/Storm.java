package com.dazzhub.skywars.Utils.events.strom;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class Storm implements eventStorm {

    private Main main;

    private Arena arena;

    private int task;
    private int timer;
    private int duration;

    public Storm(Arena arena){
        this.main = Main.getPlugin();

        this.arena = arena;
        this.timer = arena.getArenaConfig().getInt("Arena.storm.TimeSpawn",120);
        this.duration = arena.getArenaConfig().getInt("Arena.storm.TimeDuration",20);
    }

    @Override
    public void startEvent() {
        task = new BukkitRunnable() {
            @Override
            public void run() {

                arena.getPlayers().forEach(gamePlayer -> {
                    Set<String> keys = gamePlayer.getLangMessage().getConfigurationSection("Messages.TypeEvent.Storm.Starting").getKeys(false);
                    keys.stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TypeEvent.Storm.Starting." + time_config, "Error TypeEvent.Storm.Starting." + time_config).replaceAll("%seconds%", String.valueOf(timer))));
                });

                if (timer <= 1){
                    this.cancel();
                    startStorm();
                }

                timer--;
            }
        }.runTaskTimer(main,0,20).getTaskId();
    }

    private void startStorm() {
        World world;

        if (arena.getResetArena() == Enums.ResetArena.SLIMEWORLDMANAGER) {
            world = Bukkit.getWorld(this.arena.getUuid());
        } else {
            world = Bukkit.getWorld(this.arena.getNameWorld());
        }

        world.setTime(13000L);
        task = new BukkitRunnable() {
            @Override
            public void run() {

                Tools.spawnEntities(arena.getSpawnSpectator(), null, null, arena.getArenaConfig().getInt("Arena.storm.AmountLight"), arena.getArenaConfig().getInt("Arena.storm.Radio"), false, true);

                if (duration <= 1){
                    world.setTime(1000L);
                    this.cancel();
                }

                duration--;
            }
        }.runTaskTimer(main,0,20).getTaskId();
    }

    @Override
    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(task);
        task = 0;
    }

    @Override
    public int getTimer() {
        return timer;
    }
}
