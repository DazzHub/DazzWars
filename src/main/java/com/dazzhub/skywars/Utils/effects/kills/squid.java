package com.dazzhub.skywars.Utils.effects.kills;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class squid implements getTypeKills {

    private GamePlayer gamePlayer;
    private List<ArmorStand> as;

    public squid(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.as = new ArrayList<>();
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();
        Location loc = p.getPlayer().getLocation().add(0, -0.3, 0);
        ArmorStand armor = (ArmorStand)loc.getWorld().spawnEntity(loc.add(0, -1, 0), EntityType.ARMOR_STAND);
        armor.setVisible(false);
        armor.setGravity(false);

        Entity e = p.getPlayer().getWorld().spawnEntity(loc, EntityType.SQUID);
        armor.setPassenger(e);

        as.add(armor);
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                Entity passenger = armor.getPassenger();
                armor.eject();
                armor.teleport(armor.getLocation().add(0,0.5,0));
                armor.setPassenger(passenger);

                ParticleEffect.FLAME.display(armor.getLocation().add(0.0, -0.2, 0.0),0f, 0f, 0f, 0f, 10, null);
                p.getPlayer().playSound(loc, XSound.ENTITY_CHICKEN_EGG.parseSound(), 1f, 1f);

                if(i == 20) {
                    as.remove(armor);
                    armor.remove();
                    e.remove();
                    ParticleEffect.EXPLOSION_HUGE.display(armor.getLocation().add(0.0, 0.5, 0.0),0f, 0f, 0f, 0f, 1, null);
                    i = 0;
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 0);
    }

}
