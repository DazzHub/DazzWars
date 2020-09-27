package com.dazzhub.skywars.Utils.effects.kills;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

public class frostFlame implements getTypeKills {

    private GamePlayer gamePlayer;

    public frostFlame(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();
        Location loc = p.getPlayer().getLocation();
        new BukkitRunnable() {
            double t = 0.0D;
            public void run() {
                t += 0.3;
                for (double phi = 0.0D; phi <= 6; phi += 1.5) {
                    double x = 0.11D * (12.5 - t) * Math.cos(t + phi);
                    double y = 0.23D * t;
                    double z = 0.11D * (12.5 - t) * Math.sin(t + phi);
                    loc.add(x, y, z);
                    ParticleEffect.FLAME.display(p.getLocation(),0f, 0f, 0f, 0f, 10, null);
                    loc.subtract(x, y, z);

                    if (t >= 12.5) {
                        loc.add(x, y, z);
                        if (phi > Math.PI) {
                            cancel();
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1L, 1L);
    }
}
