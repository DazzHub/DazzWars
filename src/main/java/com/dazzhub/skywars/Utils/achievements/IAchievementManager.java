package com.dazzhub.skywars.Utils.achievements;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IAchievementManager {

    private final Main main;
    private final HashMap<Enums.AchievementType, List<IAchievement>> achievements;

    public IAchievementManager(Main main){
        this.main = main;
        this.achievements = new HashMap<>();
    }

    public void loadAchievements(){
        Configuration config = main.getConfigUtils().getConfig(main,"Achievements");

        for (String type : config.getConfigurationSection("Achievements").getKeys(false)) {
            Enums.AchievementType achievementType = Enums.AchievementType.valueOf(type);
            String string = "Achievements." + Enums.AchievementType.valueOf(type);
            List<IAchievement> list = new ArrayList<>();

            if (!config.getConfigurationSection(string).getKeys(false).isEmpty()) {
                for (String s : config.getConfigurationSection(string).getKeys(false)) {
                    String path = "Achievements." + achievementType.name() + "." + s + ".";
                    list.add(new IAchievement(Integer.parseInt(s), config.getString(path + "description"), config.getString(path + "type-reward"), config.getStringList(path + "executed-command")));
                }
            }

            this.achievements.put(achievementType, list);
        }

        Console.info("&eLoaded achievements: &a"+achievements.size());
    }

    public void checkPlayer(Player player, Enums.AchievementType achievementType, int n) {
        if (!player.hasPermission("sw.achievement."+achievementType.name())){
            return;
        }

        for (IAchievement achievement : this.achievements.get(achievementType)) {
            if (n == achievement.getLvl()) {
                achievement.sendReward(player);
                return;
            }
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
