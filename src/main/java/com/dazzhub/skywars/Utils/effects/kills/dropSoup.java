package com.dazzhub.skywars.Utils.effects.kills;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import com.dazzhub.skywars.Utils.inventory.Icon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class dropSoup implements getTypeKills {

    private GamePlayer gamePlayer;

    private Random r;
    private List<Item> items;

    public dropSoup(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;

        this.r = new Random();
        this.items = new ArrayList<>();
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();

        for (int i = 0; i < 30; i++) {
            Item item = p.getPlayer().getWorld().dropItem(p.getPlayer().getLocation(), new Icon(XMaterial.MUSHROOM_STEW).setName(UUID.randomUUID().toString()).build());
            item.setPickupDelay(300);
            items.add(item);

            item.setVelocity(new Vector(r.nextDouble() - 0.5D, r.nextDouble() / 2.0D, r.nextDouble() - 0.5D));
        }

        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
        items.forEach(i -> {
            ParticleEffect.BLOCK_DUST.display(p.getLocation(), 0f, 0f, 0f, 0f, 10, null);
            i.remove();
        }),50L);
    }

}
