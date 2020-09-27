package com.dazzhub.skywars.Utils.effects.wins;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import com.dazzhub.skywars.Utils.effects.getTypeWins;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.Random;

public class fallingsheep implements getTypeWins {

    private GamePlayer gamePlayer;

    public fallingsheep(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playWinEffect() {
        Player p = gamePlayer.getPlayer();

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {

                if (i == 0 || i == 10 || i == 20){
                    createSheep(p);
                }

                if (i == 21){
                    this.cancel();
                }

                i++;
            }
        }.runTaskTimer(Main.getPlugin(),0,5);
    }

    private static void createSheep(Player p){
        Random r = new Random();

        p.playSound(p.getLocation(), XSound.ENTITY_GENERIC_EXPLODE.parseSound(), 1.4f, 1.5f);
        ParticleEffect.EXPLOSION_HUGE.display(p.getLocation(),0f, 0f, 0f, 0f, 10, null);

        for (int i = 0; i < 50; i++) {

            Sheep sheep = p.getWorld().spawn(p.getLocation(), Sheep.class);

            try {
                sheep.setColor(DyeColor.values()[randomRangeInt()]);
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            sheep.setVelocity(new Vector(r.nextDouble() - 0.5, r.nextDouble() / 2, r.nextDouble() - 0.5).multiply(2).add(new Vector(0, 0.8, 0)));
            sheep.setBaby();
            sheep.setAgeLock(true);

            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), sheep::remove, 100);
        }
    }


    private static int randomRangeInt() {
        return (int) (Math.random() < 0.5 ? ((1 - Math.random()) * (15) + 0) : (Math.random() * (15) + 0));
    }

}
