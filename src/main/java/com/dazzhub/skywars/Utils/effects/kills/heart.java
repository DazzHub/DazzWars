package com.dazzhub.skywars.Utils.effects.kills;

import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Tools;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.xenondevs.particle.ParticleEffect;

public class heart implements getTypeKills {

    private GamePlayer gamePlayer;

    public heart(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void playKillEffect() {
        Player p = this.gamePlayer.getPlayer();
        Location loc = p.getPlayer().getLocation();
        for (double height = 0.0; height < 1.0; height += 0.2) {
            ParticleEffect.HEART.display(loc.clone().add(Tools.randomRange(-1.0f, 1.0f), height, Tools.randomRange(-1.0f, 1.0f)),0f, 0f, 0f, 0f, 10, null);
        }
    }
}
