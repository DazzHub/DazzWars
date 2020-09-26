package com.dazzhub.skywars.Utils.scoreboard;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

public class ScoreBoardAPI {

    private Main main;
    private HashMap<UUID, Integer> scoretask;

    public ScoreBoardAPI(Main main) {
        this.main = main;
        this.scoretask = new HashMap<>();

    }

    public void setScoreBoard(Player p, ScoreboardType scoreboardType) {
        removeScoreBoard(p);
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        ScoreBoardBuilder scoreboard = new ScoreBoardBuilder(p, randomString(), false);

        int id = Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> {
            Configuration config = main.getPlayerManager().getPlayer(p.getUniqueId()).getScoreboardMessage();
            scoreboard.setName(charsLobby(p, config.getString(scoreboardType.toString() + ".title")));

            int line = config.getStringList(scoreboardType.toString() + ".lines").size();
            for (String s : config.getStringList(scoreboardType.toString() + ".lines")) {
                scoreboard.lines(line, charsLobby(p, s));
                line--;
            }
        }, 2, 20).getTaskId();

        p.setScoreboard(scoreboard.getScoreboard());
        this.scoretask.put(p.getUniqueId(), id);
    }

    public void removeScoreBoard(Player p) {
        if (this.scoretask.containsKey(p.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(this.scoretask.get(p.getUniqueId()));

            /*Removing to prevent Memory Leaks*/
            this.scoretask.remove(p.getUniqueId());
        }
    }

    private String charsLobby(Player p, String msg) {
        return PlaceholderAPI.setPlaceholders(p, msg);
    }

    private String randomString() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public enum ScoreboardType {
        LOBBY, STARTING, STARTINGTEAM, INGAME, INGAMETEAM, SPECTATOR;
    }

}
