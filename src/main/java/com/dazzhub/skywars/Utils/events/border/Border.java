package com.dazzhub.skywars.Utils.events.border;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.locUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class Border implements eventBorder {

    private final Main main;

    private final Arena arena;
    private WorldBorder wb;

    private int task;
    private int timer;

    public Border(Arena arena) {
        this.main = Main.getPlugin();

        this.arena = arena;
        this.timer = arena.getArenaConfig().getInt("Arena.border.TimeSpawn",102);
    }

    @Override
    public void spawnBorder() {
        if (this.arena.getArenaConfig().getBoolean("Arena.border.SpawnDefault",false)) {

            World world;

            if (arena.getResetArena() == Enums.ResetArena.SLIMEWORLDMANAGER) {
                world = Bukkit.getWorld(this.arena.getUuid());
            } else {
                world = Bukkit.getWorld(this.arena.getNameWorld());
            }

            WorldBorder wb = world.getWorldBorder();

            wb.setCenter(this.arena.getSpawnSpectator());
            wb.setSize(this.arena.getArenaConfig().getInt("Arena.border.Settings.Size"),100);
            wb.setWarningDistance(0);

            this.wb = wb;
        }
    }

    @Override
    public void startEvent() {
        if (this.wb == null){
            World world;

            if (arena.getResetArena() == Enums.ResetArena.SLIMEWORLDMANAGER) {
                world = Bukkit.getWorld(this.arena.getUuid());
            } else {
                world = Bukkit.getWorld(this.arena.getNameWorld());
            }

            WorldBorder wb = world.getWorldBorder();

            wb.setCenter(this.arena.getSpawnSpectator());
            wb.setSize(this.arena.getArenaConfig().getInt("Arena.border.Settings.Size",1));
            wb.setDamageBuffer(this.arena.getArenaConfig().getInt("Arena.border.Settings.Damage.inborder",1));
            wb.setDamageAmount(this.arena.getArenaConfig().getInt("Arena.border.Settings.Damage.borde",1));
            wb.setWarningDistance(0);

            this.wb = wb;
        }

        this.task = new BukkitRunnable() {

            @Override
            public void run() {

                arena.getPlayers().forEach(gamePlayer -> {
                    Set<String> keys = gamePlayer.getLangMessage().getConfigurationSection("Messages.TypeEvent.Border.Starting").getKeys(false);
                    keys.stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TypeEvent.Border.Starting." + time_config, "Error TypeEvent.Border.Starting." + time_config).replaceAll("%seconds%", String.valueOf(timer))));
                });

                if (timer <= 1){
                    this.cancel();
                    startMove();
                }

                timer--;
            }

        }.runTaskTimer(main,0,20).getTaskId();


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

    @Override
    public int getTimer() {
        return timer;
    }
}
