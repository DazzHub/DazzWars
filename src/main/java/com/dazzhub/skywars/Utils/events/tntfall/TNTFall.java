package com.dazzhub.skywars.Utils.events.tntfall;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class TNTFall implements eventTNT{

    private Main main;

    private Arena arena;

    private int task;
    private int timer;
    private int duration;

    public TNTFall(Arena arena) {
        this.main = Main.getPlugin();

        this.arena = arena;

        this.timer = arena.getArenaConfig().getInt("Arena.tntfall.TimeSpawn", 120);
        this.duration = arena.getArenaConfig().getInt("Arena.tntfall.TimeDuration",20);
    }

    @Override
    public void startEvent() {
        task = new BukkitRunnable() {
            @Override
            public void run() {

                arena.getPlayers().forEach(gamePlayer -> {
                    Set<String> keys = gamePlayer.getLangMessage().getConfigurationSection("Messages.TypeEvent.TntFall.Starting").getKeys(false);
                    keys.stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TypeEvent.TntFall.Starting." + time_config, "Error TypeEvent.TntFall.Starting." + time_config).replaceAll("%seconds%", String.valueOf(timer))));
                });

                if (timer <= 1){
                    this.cancel();
                    startDrop();
                }

                timer--;
            }
        }.runTaskTimer(main,0,20).getTaskId();
    }

    private void startDrop(){
        task = new BukkitRunnable() {
            @Override
            public void run() {

                Tools.spawnEntities(arena.getSpawnSpectator(), EntityType.PRIMED_TNT, null, arena.getArenaConfig().getInt("Arena.tntfall.AmountFallTnT"), arena.getArenaConfig().getInt("Arena.tntfall.Radio"), false, false);

                if (duration <= 1){
                    this.cancel();
                }

                duration--;
            }
        }.runTaskTimer(main,0,20).getTaskId();
    }

    @Override
    public void stopEvent() {
        Bukkit.getScheduler().cancelTask(task);
        task = 0;
    }
}
