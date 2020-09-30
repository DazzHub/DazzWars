package com.dazzhub.skywars.Listeners.Lobby;

import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onTime implements Listener {

    private Main main;

    public onTime(Main main) {
        this.main = main;

        new BukkitRunnable() {
            public void run() {
                Bukkit.getServer().getWorlds().stream().filter(w -> main.getSettings().getStringList("lobbies.onTime").contains(w.getName())).forEach(w -> w.setTime(0L));
            }
        }.runTaskTimerAsynchronously(main, 0L, 3000L);
    }

    @EventHandler
    public void onChangeWeather(WeatherChangeEvent e) {
        if (main.getSettings().getStringList("lobbies.onTime").contains(e.getWorld().getName())) {
            if (e.toWeatherState()) {
                e.setCancelled(true);
            }
        }
    }

}
