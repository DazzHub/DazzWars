package com.dazzhub.skywars.Utils.effects.kills;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class headExplode implements getTypeKills {

    private GamePlayer gamePlayer;
    private List<ArmorStand> as;

    public headExplode(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.as = new ArrayList<>();
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();

        Location loc = p.getPlayer().getLocation();
        ItemStack skull = new ItemStack(XMaterial.PLAYER_HEAD.parseItem());
        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
        skullMeta.setOwner(p.getPlayer().getName());
        skull.setItemMeta(skullMeta);

        ArmorStand armor = (ArmorStand)loc.getWorld().spawnEntity(loc.add(0, -1, 0), EntityType.ARMOR_STAND);
        armor.setVisible(false);
        armor.setCustomName("§c§l" + p.getPlayer().getName());
        armor.setCustomNameVisible(true);
        armor.setHelmet(skull);
        armor.setGravity(false);

        as.add(armor);
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                armor.teleport(armor.getLocation().add(0,0.5,0));
                armor.setHeadPose(armor.getHeadPose().add(0.0, 1, 0.0));
                ParticleEffect.FLAME.display(armor.getLocation().add(0.0, -0.2, 0.0),0f, 0f, 0f, 0f, 10, null);
                if(i == 20) {
                    as.remove(armor);
                    armor.remove();
                    ParticleEffect.EXPLOSION_HUGE.display(armor.getLocation().add(0.0, 0.5, 0.0),0f, 0f, 0f, 0f, 1, null);
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 0);
    }
}
