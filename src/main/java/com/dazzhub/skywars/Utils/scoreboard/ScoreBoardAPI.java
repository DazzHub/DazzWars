package com.dazzhub.skywars.Utils.scoreboard;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.security.SecureRandom;

public class ScoreBoardAPI {

    private Main main;

    public ScoreBoardAPI(Main main) {
        this.main = main;
    }

    public void setScoreBoard(Player p, Enums.ScoreboardType scoreboardType, boolean health, boolean spectator, boolean gamePlayers, boolean teams) {
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        ScoreBoardBuilder scoreboard = new ScoreBoardBuilder(p, randomString(),
                health,
                spectator,
                gamePlayers,
                teams,
                scoreboardType
        );

        Configuration config = main.getPlayerManager().getPlayer(p.getUniqueId()).getScoreboardMessage();
        scoreboard.setName(main.getScoreBoardAPI().charsLobby(p, config.getString(scoreboard.getScoreboardType().toString() + ".title")));

        int line = config.getStringList(scoreboard.getScoreboardType().toString() + ".lines").size();

        for (String s : config.getStringList(scoreboard.getScoreboardType().toString() + ".lines")) {
            scoreboard.createScore(main.getScoreBoardAPI().charsLobby(p, s), line);
            line--;
        }

        gamePlayer.setScoreBoardBuilder(scoreboard);
        scoreboard.add(p);
    }

    public String charsLobby(Player p, String msg) {
        if (p == null || msg == null || msg.isEmpty()) return "";

        return PlaceholderAPI.setPlaceholders(p, msg);
    }

    private String randomString() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 8; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }
}
