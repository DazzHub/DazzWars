package com.dazzhub.skywars.Utils.hologram;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.locUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class HologramsManager {

    private Main main;

    public HologramsManager(Main main) {
        this.main = main;
    }

    public void createHologram(Player p, String typeHologram) {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")){
            p.sendMessage("&8> &eHolographicDisplays plugin is not active");
            return;
        }

        File file = this.main.getConfigUtils().getFile(this.main,"Settings");
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Settings");

        try {
            config.set("Holograms."+typeHologram, locUtils.locToString(p.getLocation()));
            config.save(file);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Bukkit.getScheduler().runTaskLater(main, () -> {
           for (Player on : Bukkit.getOnlinePlayers()){
              GamePlayer gamePlayer = main.getPlayerManager().getPlayer(on.getUniqueId());

              if (gamePlayer.getHolograms() != null){
                  gamePlayer.getHolograms().deleteHologram();
              }

              loadHologram(on.getPlayer());
           }
        },2);

    }

    public void loadHologram(Player p) {
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (main.getSettings().getString("Holograms","").isEmpty()) return;

        Holograms holo = new Holograms(main, gamePlayer);

        for (String typeHologram : main.getSettings().getConfigurationSection("Holograms").getKeys(false)) {
            holo.createHologram(typeHologram);
        }
    }

}
