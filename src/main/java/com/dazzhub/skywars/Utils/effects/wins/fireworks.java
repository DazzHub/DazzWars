package com.dazzhub.skywars.Utils.effects.wins;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import com.dazzhub.skywars.Utils.effects.getTypeWins;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class fireworks implements getTypeWins {

    private Location location;

    public fireworks(Location location){
        this.location = location;
    }

    @Override
    public void playWinEffect() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
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
        });
    }

}
