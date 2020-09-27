package com.dazzhub.skywars.Utils.effects.kills;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

public class wave implements getTypeKills {

    private GamePlayer gamePlayer;

    public wave(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();
        final Location loc = p.getLocation();

        new BukkitRunnable() {
            double t = Math.PI/4;
            public void run(){
                t = t + 0.1*Math.PI;

                for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/32){
                    double x = t*Math.cos(theta);
                    double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
                    double z = t*Math.sin(theta);
                    loc.add(x,y,z);

                    ParticleEffect.DRIP_WATER.display(loc,0f, 0f, 0f, 0f, 10, null);
                    ParticleEffect.SNOW_SHOVEL.display(loc,0f, 0f, 0f, 0f, 10, null);

                    loc.subtract(x,y,z);
                    theta = theta + Math.PI/64;
                }

                if (t > 8){
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 4, 0);
    }

}
