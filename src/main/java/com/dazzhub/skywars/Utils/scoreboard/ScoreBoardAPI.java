package com.dazzhub.skywars.Utils.scoreboard;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.UUID;

public class ScoreBoardAPI {

    private final Main main;

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

        gamePlayer.setScoreBoardBuilder(scoreboard);
        p.setScoreboard(scoreboard.getScoreboard());

    }

    public String charsLobby(Player p, String msg) {
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

}
