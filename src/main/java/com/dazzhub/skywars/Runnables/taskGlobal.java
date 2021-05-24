package com.dazzhub.skywars.Runnables;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Runnable.utils.SnakeRunnableSync;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardBuilder;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class taskGlobal extends SnakeRunnableSync {

    private final Main main;

    public taskGlobal(Main main) {
        this.main = main;
    }

    @Override
    public void onTick() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            main.getPlayerManager().getTaggedCooldown().checkTimer(p);

            if (gamePlayer == null) continue;

            if (gamePlayer.getMenu() != null) {
                gamePlayer.getMenu().addItems(gamePlayer.getPlayer());
            }

            ScoreBoardBuilder scoreboard = gamePlayer.getScoreBoardBuilder();

            if (scoreboard != null) {

                if (scoreboard.getScoreboardType().equals(Enums.ScoreboardType.LOBBY)) {
                    if (main.getSettings().getStringList("lobbies.onScoreboard").contains(p.getWorld().getName())) {
                        continue;
                    }
                }

                Configuration config = main.getPlayerManager().getPlayer(p.getUniqueId()).getScoreboardMessage();
                scoreboard.setName(main.getScoreBoardAPI().charsLobby(p, config.getString(scoreboard.getScoreboardType().toString() + ".title")));

                int line = config.getStringList(scoreboard.getScoreboardType().toString() + ".lines").size();

                for (String s : config.getStringList(scoreboard.getScoreboardType().toString() + ".lines")) {
                    scoreboard.updateScore(main.getScoreBoardAPI().charsLobby(p, s), line);
                    line--;
                }

                if (gamePlayer.isInArena()) {
                    if (scoreboard.isHealth()) scoreboard.updatelife(gamePlayer.getArena());
                    if (scoreboard.isSpectator()) scoreboard.updateSpectator(gamePlayer.getArena());
                    if (scoreboard.isGamePlayers()) scoreboard.updateEnemy(gamePlayer, gamePlayer.getArena());
                    if (scoreboard.isTeams()) scoreboard.updateTeams(gamePlayer);
                }
            }
        }
    }
}
