package com.dazzhub.skywars.Utils;

import com.dazzhub.skywars.MySQL.utils.GamePlayer;

import java.util.HashMap;

public class Cooldown {

    private int time;
    private HashMap<GamePlayer, Long> cooldowns;

    public Cooldown(int time) {
        this.time = time;
        this.cooldowns = new HashMap<>();
    }

    public void addCooldown(GamePlayer player) {
        this.cooldowns.put(player, System.currentTimeMillis());
    }

    public boolean checkCooldown(GamePlayer player) {

        if (cooldowns.containsKey(player)) {

            long startTime = cooldowns.get(player);
            long endTime = System.currentTimeMillis();
            long leftTime = time * 1000;
            long total = leftTime - (endTime - startTime);

            if (total <= 0) {
                cooldowns.remove(player);
                return false;
            } else {
                return true;
            }
        }

        return false;
    }

    public HashMap<GamePlayer, Long> getCooldowns() {
        return cooldowns;
    }
}