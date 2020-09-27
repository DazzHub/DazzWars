package com.dazzhub.skywars.Utils.effects.kills;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Tools;
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

public class satan implements getTypeKills {

    private GamePlayer gamePlayer;
    private List<ArmorStand> as;

    public satan(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.as = new ArrayList<>();
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();

        Location loc = p.getPlayer().getLocation();
        ItemStack skull = new ItemStack(XMaterial.PLAYER_HEAD.parseItem());
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(p.getPlayer().getName());
        skull.setItemMeta(skullMeta);

        ArmorStand armor = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armor.setVisible(false);
        armor.setCustomName("§c§l" + p.getPlayer().getName());
        armor.setCustomNameVisible(true);
        armor.setHelmet(skull);
        armor.setGravity(false);

        as.add(armor);
        new BukkitRunnable() {
            int i = 0;

            public void run() {
                i++;

                for (int i = 0; i < 2; i++) {
                    ParticleEffect.SMOKE_LARGE.display(loc.clone().add(Tools.randomRange(-1.0F, 1.0F), 2.5D, Tools.randomRange(-1.0F, 1.0F)),0f, 0f, 0f, 0f, 10, null);
                    ParticleEffect.SMOKE_LARGE.display(loc.clone().add(Tools.randomRange(-1.0F, 1.0F), 2.5D, Tools.randomRange(-1.0F, 1.0F)),0f, 0f, 0f, 0f, 10, null);
                    ParticleEffect.SMOKE_LARGE.display(loc.clone().add(Tools.randomRange(-1.0F, 1.0F), 2.5D, Tools.randomRange(-1.0F, 1.0F)),0f, 0f, 0f, 0f, 10, null);
                    ParticleEffect.SMOKE_LARGE.display(loc.clone().add(Tools.randomRange(-1.0F, 1.0F), 2.7D, Tools.randomRange(-1.0F, 1.0F)),0f, 0f, 0f, 0f, 10, null);
                }

                ParticleEffect.FLAME.display(loc.clone().add(Tools.randomRange(-0.8F, 0.8F), 2.5D, Tools.randomRange(-0.8F, 0.8F)),0f, 0f, 0f, 0f, 10, null);
                ParticleEffect.FLAME.display(loc.clone().add(Tools.randomRange(-0.8F, 0.8F), 2.5D, Tools.randomRange(0.8F, -0.8F)),0f, 0f, 0f, 0f, 10, null);
                ParticleEffect.DRIP_LAVA.display(loc.clone().add(Tools.randomRange(-0.8F, 0.8F), 2.5D, Tools.randomRange(-0.8F, 0.8F)),0f, 0f, 0f, 0f, 10, null);

                if (i == 100) {
                    as.remove(armor);
                    armor.remove();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 0);
    }
}
