package com.dazzhub.skywars.Utils.effects.wins;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import com.dazzhub.skywars.Utils.effects.getTypeWins;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class parachute implements getTypeWins {

    private static List<Chicken> chickens = new ArrayList<>();
    private GamePlayer gamePlayer;

    public parachute(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playWinEffect() {
        Player p = gamePlayer.getPlayer();
        Location loc = p.getLocation();

        p.teleport(loc.clone().add(0, 15, 0));
        p.setVelocity(new Vector(0, 0, 0));

        for (int i = 0; i < 20; i++) {
            Chicken chicken = (Chicken) p.getWorld().spawnEntity(p.getLocation().add(randomDouble(), 3, randomDouble()), EntityType.CHICKEN);
            chickens.add(chicken);
            chicken.setLeashHolder(p);
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getPlugin(), () -> checkFalling(p), 5);

    }

    public static void checkFalling(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {

                ParticleEffect.FLAME.display(p.getLocation().add(0.0, 0.5, 0.0),0f, 0f, 0f, 0f, 10, null);

                if (!isNotOnAir(p) && p.getVelocity().getY() < -0.3) {
                    p.setVelocity(new Vector(0, 0.1, 0));
                } else {
                    ParticleEffect.EXPLOSION_HUGE.display(p.getLocation().add(0.0, 0.5, 0.0),0f, 0f, 0f, 0f, 1, null);
                    p.setFallDistance(0);
                    killParachute(p);
                    this.cancel();
                }

            }
        }.runTaskTimer(Main.getPlugin(),0,80);
    }

    private static void killParachute(Player p) {
        chickens.forEach(chicken -> {
            chicken.setLeashHolder(null);
            chicken.remove();
        });

        p.setVelocity(new Vector(0, 0.15, 0));
        p.setFallDistance(0);
    }

    private static boolean isNotOnAir(Player p) {
        return p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR;
    }

    private static double randomDouble() {
        return Math.random() < 0.5 ? ((1 - Math.random()) * (0.5 - (double) 0) + (double) 0) : (Math.random() * (0.5 - (double) 0) + (double) 0);
    }

}
