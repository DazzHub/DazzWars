package com.dazzhub.skywars.Utils.effects.wins;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeWins;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

public class antigravity implements getTypeWins {

    private ArmorStand as;
    private GamePlayer gamePlayer;

    public antigravity(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playWinEffect() {
        Player p = gamePlayer.getPlayer();

        as = p.getWorld().spawn(p.getLocation(), ArmorStand.class);
        as.setMetadata("NO_INTER", new FixedMetadataValue(Main.getPlugin(), ""));
        as.setGravity(false);
        as.setSmall(true);
        as.setVisible(false);
        as.setHelmet(new ItemStack(Material.SEA_LANTERN));
        runeff(p);
    }

    private void runeff(Player p) {
        if (as != null && as.isValid()) {
            as.setHeadPose(as.getHeadPose().add(0, 0.1, 0));
            p.setVelocity(new Vector(0, 0, 0));

            Location test = p.getLocation().clone();
            new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    i++;

                    p.teleport(test.add(0, 0.4, 0));

                    ParticleEffect.PORTAL.display(p.getLocation(),0f, 0f, 0f, 0f, 30, null);
                    ParticleEffect.SPELL.display(p.getLocation(),0f, 0f, 0f, 0f, 30, null);

                    if (i == 20){
                        p.setVelocity(new Vector(0, 0.1, 0));
                        p.playSound(p.getLocation(), XSound.ENTITY_GENERIC_EXPLODE.parseSound(), 1.4f, 1.5f);
                        spawn(p.getLocation());

                        as.remove();
                        as = null;

                        this.cancel();
                    }
                }
            }.runTaskTimer(Main.getPlugin(),1,0);

        }
    }

    private void spawn(Location location){
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
