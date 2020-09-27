package com.dazzhub.skywars.Utils.effects.wins;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeWins;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class fireworks implements getTypeWins {

    private Location location;
    private int timer;

    public fireworks(GamePlayer gamePlayer){
        this.location = gamePlayer.getPlayer().getLocation();
        this.timer = gamePlayer.getArena().getFinishedGame() - 3;
    }

    @Override
    public void playWinEffect() {
        new BukkitRunnable() {
            @Override
            public void run() {
                spawn();

                if (timer <= 0){
                    this.cancel();
                }

                timer--;
            }
        }.runTaskTimer(Main.getPlugin(), 0,5);

    }

    private void spawn(){
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder()
                .withColor(Color.LIME)
                .withColor(Color.FUCHSIA)
                .withColor(Color.RED)
                .flicker(true)
                .trail(true)
                .withFade(Color.GREEN)
                .build()
        );
        fw.setFireworkMeta(fwm);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), fw::detonate, 5);
    }

}
