package com.dazzhub.skywars.Utils.effects.kills;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

public class twister implements getTypeKills {

    private GamePlayer gamePlayer;

    public twister(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();
        final Location location = p.getPlayer().getLocation();

        new BukkitRunnable() {
            int angle = 0;
            @Override
            public void run() {
                int max_height = 7;
                double max_radius = 5;
                int lines = 5;
                double height_increasement = 0.25;
                double radius_increasement = max_radius / max_height;

                for (int l = 0; l < lines; l++) {
                    for (double y = 0; y < max_height; y+=height_increasement ) {
                        double radius = y * radius_increasement;

                        double v = 360 / lines * l + y * 30;

                        double x = Math.cos(Math.toRadians(v - angle)) * radius;
                        double z = Math.sin(Math.toRadians(v - angle)) * radius;

                        ParticleEffect.CLOUD.display(location.clone().add(x,y,z),0f, 0f, 0f, 0f, 10, null);
                    }
                }

                if(angle == 70) {
                    cancel();
                }

                angle++;
            }
        }.runTaskTimer(Main.getPlugin(), 2, 0);
    }

}
