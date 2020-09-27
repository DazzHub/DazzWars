package com.dazzhub.skywars.Utils.effects.kills;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Tools;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class redstone implements getTypeKills {

    private GamePlayer gamePlayer;

    public redstone(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();
        final Location loc = p.getPlayer().getLocation();

        for (double height = 0.0; height < 1.0; height += 0.8) {
            p.getPlayer().getWorld().playEffect(loc.clone().add(Tools.randomRange(-1.0f, 1.0f), height, Tools.randomRange(-1.0f, 1.0f)), Effect.STEP_SOUND, XMaterial.REDSTONE_BLOCK.parseMaterial());
            p.getPlayer().getWorld().playEffect(loc.clone().add(Tools.randomRange(1.0f, -1.0f), height, Tools.randomRange(-1.0f, 1.0f)), Effect.STEP_SOUND, XMaterial.REDSTONE_BLOCK.parseMaterial());
        }
    }
}
