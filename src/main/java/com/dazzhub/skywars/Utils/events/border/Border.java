package com.dazzhub.skywars.Utils.events.border;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class Border implements eventBorder {

    private Main main;

    private Arena arena;
    private WorldBorder wb;

    private int task;
    private int timer;

    public Border(Arena arena) {
        this.main = Main.getPlugin();

        this.arena = arena;
        this.wb = null;
        this.timer = arena.getArenac().getInt("Arena.border.TimeSpawn");
    }

    @Override
    public void spawnBorder() {
        if (this.arena.getArenac().getBoolean("Arena.border.SpawnDefault")) {
            World world = Bukkit.getWorld(this.arena.getNameWorld());
            WorldBorder wb = world.getWorldBorder();

            wb.setCenter(this.arena.getSpawnSpectator());
            wb.setSize(this.arena.getArenac().getInt("Arena.border.Settings.Size"));
            wb.setWarningDistance(0);
        }
    }

    @Override
    public void startEvent() {
        if (this.wb == null){
            World world = Bukkit.getWorld(this.arena.getNameWorld());
            WorldBorder wb = world.getWorldBorder();

            wb.setCenter(this.arena.getSpawnSpectator());
            wb.setSize(this.arena.getArenac().getInt("Arena.border.Settings.Size"));
            wb.setDamageBuffer(this.arena.getArenac().getInt("Arena.border.Settings.Damage.inborder"));
            wb.setDamageAmount(this.arena.getArenac().getInt("Arena.border.Settings.Damage.borde"));
            wb.setWarningDistance(0);
        }

        this.task = new BukkitRunnable() {

            @Override
            public void run() {

                arena.getPlayers().forEach(gamePlayer -> {
                    Set<String> keys = gamePlayer.getLangMessage().getConfigurationSection("Messages.TypeEvent.Border.Starting").getKeys(false);
                    keys.stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TypeEvent.Border.Starting." + time_config).replaceAll("%seconds%", String.valueOf(timer))));
                });

                if (timer <= 1){
                    this.cancel();
                    startMove();
                }

                timer--;
            }

        }.runTaskTimerAsynchronously(main,0,20).getTaskId();


    }

    private void startMove() {
        if (wb == null) return;

        this.task = new BukkitRunnable() {

            @Override
            public void run() {
                wb.setSize(wb.getSize() - 1.0);

                if (wb.getSize() <= 1){
                    this.cancel();
                }
            }

        }.runTaskTimer(main,20,20).getTaskId();
    }

    @Override
    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(task);
        task = 0;
    }

}
