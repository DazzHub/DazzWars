package com.dazzhub.skywars.Utils.effects.wins;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import com.dazzhub.skywars.Utils.effects.getTypeWins;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.concurrent.ThreadLocalRandom;

public class chickens implements getTypeWins {

    private GamePlayer gamePlayer;

    public chickens(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playWinEffect() {
        Player p = gamePlayer.getPlayer();

        for (int n = 0; 10 > n; ++n) {
            new BukkitRunnable() {
                public void run() {
                    Chicken c = spawnChicken(p.getLocation(), random(-0.5, 0.5), random(-0.5, 0.5));
                    c.getLocation().getWorld().playSound(c.getLocation(), XSound.ENTITY_FIREWORK_ROCKET_LAUNCH.parseSound(), 1.0f, 1.0f);
                    new BukkitRunnable() {
                        int time = 20;
                        
                        public void run() {
                            if (this.time == 0) {
                                if (c.isDead()) {
                                    ParticleEffect.NOTE.display(c.getLocation(),0f, 0f, 0f, 0f, 152, null);
                                    c.getLocation().getWorld().playSound(c.getLocation(), XSound.ENTITY_FIREWORK_ROCKET_BLAST.parseSound(), 1.0f, 1.0f);
                                    this.cancel();
                                }
                                else {
                                    ParticleEffect.NOTE.display(c.getLocation(),0f, 0f, 0f, 0f, 152, null);
                                    c.getLocation().getWorld().playSound(c.getLocation(), XSound.ENTITY_FIREWORK_ROCKET_BLAST.parseSound(), 1.0f, 1.0f);
                                    c.remove();
                                }
                            }
                            else {
                                --this.time;
                                if (c.isDead()) {
                                    ParticleEffect.NOTE.display(c.getLocation(),0f, 0f, 0f, 0f, 152, null);
                                    c.getLocation().getWorld().playSound(c.getLocation(), XSound.ENTITY_FIREWORK_ROCKET_BLAST.parseSound(), 1.0f, 1.0f);
                                    this.cancel();
                                }
                                else {
                                    ParticleEffect.NOTE.display(c.getLocation(),0f, 0f, 0f, 0f, 152, null);
                                }
                            }
                        }
                    }.runTaskTimer(Main.getPlugin(), 0L, 10L);
                }
            }.runTaskLater(Main.getPlugin(), (n * 10));
        }
    }

    public static double random(double n, double n2) {
        return n + ThreadLocalRandom.current().nextDouble() * (n2 - n);
    }

	private static Chicken spawnChicken(Location loc, double n, double n3) {
        Chicken chicken = loc.getWorld().spawn(loc, Chicken.class);
        chicken.setVelocity(new Vector(n, 1.5, n3));
        return chicken;
    }

}
