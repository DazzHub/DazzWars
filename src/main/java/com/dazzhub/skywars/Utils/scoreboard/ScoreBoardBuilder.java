package com.dazzhub.skywars.Utils.scoreboard;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.ArenaTeam;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.CenterMessage;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class ScoreBoardBuilder {

    private final Scoreboard scoreboard;
    private final Objective objective;

    private final BiMap<Integer, ScoreBoardEntry> entries;

    private Objective nameHealthObj;
    private Objective tabObjective;

    private Team gameSpectator;
    private Team gameTeams;
    private Team gameEnemy;

    private final boolean health, spectator, gamePlayers, teams;
    private final Enums.ScoreboardType scoreboardType;

    public ScoreBoardBuilder(Player p, String score_name, boolean health, boolean spectator, boolean gamePlayers, boolean teams, Enums.ScoreboardType scoreboardType) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective(score_name, "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.entries = HashBiMap.create();

        this.health = health;
        this.spectator = spectator;
        this.gamePlayers = gamePlayers;
        this.teams = teams;

        this.scoreboardType = scoreboardType;

        if (health) {
            this.nameHealthObj = this.scoreboard.registerNewObjective(score_name + "life", "health");
            this.nameHealthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
            this.nameHealthObj.setDisplayName(ChatColor.DARK_RED + "\u2764");
        }

        GamePlayer gamePlayer = Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId());

        if (!gamePlayer.getLangMessage().getBoolean("Messages.ScoreBoard.Team.enabled", true)){
            return;
        }

        if (spectator) {
            this.gameSpectator = this.scoreboard.registerNewTeam("3-S" + score_name);
            this.gameSpectator.setCanSeeFriendlyInvisibles(true);
        }

        if (gamePlayers) {
            this.gameEnemy = this.scoreboard.registerNewTeam("2-P" + score_name);

            if (gamePlayer.getLangMessage().getBoolean("Messages.ScoreBoard.Team.killCount")) {
                this.tabObjective = this.scoreboard.registerNewObjective(score_name + "kills", "dummy");
                this.tabObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            }
        }

        if (teams) {
            this.gameTeams = this.scoreboard.registerNewTeam("1-T" + score_name);
            this.gameTeams.setAllowFriendlyFire(false);
        }
    }

    public void updatelife(Arena arena) {
        if (nameHealthObj == null) return;

        for (GamePlayer gamePlayer : arena.getPlayers()) {
            Player player2;
            Player player = player2 = gamePlayer.getPlayer();
            this.nameHealthObj.getScore(player.getName()).setScore((int) (player2).getHealth() + absorbHearts(player2));
        }
    }

    public void updateSpectator(Arena arena) {
        if (gameSpectator == null) return;

        for (GamePlayer gamePlayer : arena.getSpectators()) {

            if (!this.gameSpectator.hasEntry(gamePlayer.getName())) {
                this.gameSpectator.addEntry(gamePlayer.getName());
            }

            this.gameSpectator.setPrefix(chars(gamePlayer, gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.Spectator").split(":")[0]));
            this.gameSpectator.setSuffix(chars(gamePlayer, gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.Spectator").split(":")[1]));
        }
    }

    public void updateEnemy(GamePlayer player, Arena arena) {
        if (gameEnemy == null) return;

        for (GamePlayer gamePlayer : arena.getPlayers()) {

            if (player.getArenaTeam() != null && player.getArenaTeam().getMembers().contains(gamePlayer)) continue;

            if (!this.gameEnemy.hasEntry(gamePlayer.getName())) {
                this.gameEnemy.addEntry(gamePlayer.getName());

                if (tabObjective != null) {
                    this.tabObjective.getScore(gamePlayer.getName()).setScore(gamePlayer.getKillsStreak());
                }
            }

            this.gameEnemy.setPrefix(chars(gamePlayer, gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.Game").split(":")[0]));
            this.gameEnemy.setSuffix(chars(gamePlayer, gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.Game").split(":")[1]));

            if (tabObjective != null) {
                this.tabObjective.getScore(gamePlayer.getPlayer().getName()).setScore(gamePlayer.getKillsStreak());
            }
        }

    }

    public void updateTeams(GamePlayer gamePlayer) {
        if (gameTeams == null) return;

        ArenaTeam arenaTeam = gamePlayer.getArenaTeam();

        if (arenaTeam == null || arenaTeam.getMembers().isEmpty()) {
            return;
        }

        for (GamePlayer members : gamePlayer.getArenaTeam().getMembers()) {

            if (!this.gameTeams.hasEntry(members.getName())) {
                this.gameTeams.addEntry(members.getName());

                if (tabObjective != null) {
                    this.tabObjective.getScore(gamePlayer.getName()).setScore(gamePlayer.getKillsStreak());
                }
            }

            this.gameTeams.setPrefix(chars(members, gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.TeamsFriends").split(":")[0]));
            this.gameTeams.setSuffix(chars(members, gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.TeamsFriends").split(":")[1]));

            if (tabObjective != null) {
                this.tabObjective.getScore(gamePlayer.getPlayer().getName()).setScore(gamePlayer.getKillsStreak());
            }
        }
    }

    private int absorbHearts(Player pl) {
        for (PotionEffect pe : pl.getActivePotionEffects()) {
            if (pe.getType().equals(PotionEffectType.ABSORPTION)) {
                return pe.getAmplifier() * 2 + 2;
            }
        }
        return 0;
    }

    private String chars(GamePlayer gamePlayer, String c) {
        return PlaceholderAPI.setPlaceholders(gamePlayer.getPlayer(), ChatColor.translateAlternateColorCodes('&', c));
    }

    public void createScore(String name, int value) {

        if (name.isEmpty()){
            objective.getScore(getEntry(value)).setScore(value);
            return;
        }

        if (!Main.getPlugin().checkVersion()){

            if (name.length() > 64) {
                name = "to long";
            }

            Team team = getScoreboard().registerNewTeam("score-" + value);
            team.setPrefix(name);

            Score score;
            score = getObjective().getScore(name);
            score.setScore(value);

            entries.put(value, new ScoreBoardEntry(name, value, team, score));
            return;
        }

        if (name.length() <= 16){
            Team team = getScoreboard().registerNewTeam("score-" + value);
            team.setPrefix(name);

            Score score;
            score = getObjective().getScore(name);
            score.setScore(value);

            entries.put(value, new ScoreBoardEntry(name, value, team, score));
            return;
        }

        Team team = getScoreboard().registerNewTeam("score-" + value);
        Iterator<String> iterator = Splitter.fixedLength(16).split(name).iterator();

        team.setPrefix(iterator.next());
        String entry = iterator.next();

        if (name.length() > 32) {
            team.setSuffix(iterator.next());
        }

        team.addEntry(entry);

        Score score;
        score = getObjective().getScore(entry);
        score.setScore(value);

        entries.put(value, new ScoreBoardEntry(name, value, team, score));
    }

    public void updateScore(String name, int value){

        if (entries.get(value) != null){
            ScoreBoardEntry entryC = entries.get(value);

            if (entryC.getName().equalsIgnoreCase(name)) return;

            getScoreboard().resetScores(entryC.getScore().getEntry());
            entryC.getTeam().unregister();

            if (!Main.getPlugin().checkVersion()){

                if (name.length() > 64) {
                    name = "to long";
                }

                Team team = getScoreboard().registerNewTeam("score-" + value);
                team.setPrefix(name);

                Score score;
                score = getObjective().getScore(name);
                score.setScore(value);

                entries.put(value, new ScoreBoardEntry(name, value, team, score));
                return;
            }

            if (name.length() <= 16){
                Team team = getScoreboard().registerNewTeam("score-" + value);
                team.setPrefix(name);

                Score score;
                score = getObjective().getScore(name);
                score.setScore(value);

                entries.replace(value, new ScoreBoardEntry(name, value, team, score));
                return;
            }

            Team team = getScoreboard().registerNewTeam("score-" + value);
            Iterator<String> iterator = Splitter.fixedLength(16).split(name).iterator();

            team.setPrefix(iterator.next());
            String entry = iterator.next();

            if (name.length() > 32) {
                team.setSuffix(iterator.next());
            }

            team.addEntry(entry);

            Score score;
            score = getObjective().getScore(entry);
            score.setScore(value);

            entries.replace(value, new ScoreBoardEntry(name, value, team, score));
        }
    }

    public void setName(String title) {
        objective.setDisplayName(title);
    }

    private String getEntry(Integer n) {
        if (n == 0) {
            return "§0";
        }
        if (n == 1) {
            return "§1";
        }
        if (n == 2) {
            return "§2";
        }
        if (n == 3) {
            return "§3";
        }
        if (n == 4) {
            return "§4";
        }
        if (n == 5) {
            return "§5";
        }
        if (n == 6) {
            return "§6";
        }
        if (n == 7) {
            return "§7";
        }
        if (n == 8) {
            return "§8";
        }
        if (n == 9) {
            return "§9";
        }
        if (n == 10) {
            return "§a";
        }
        if (n == 11) {
            return "§b";
        }
        if (n == 12) {
            return "§c";
        }
        if (n == 13) {
            return "§d";
        }
        if (n == 14) {
            return "§e";
        }
        if (n == 15) {
            return "§f";
        }
        return "";
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public String maxChars(int n, String s) {
        if (ChatColor.translateAlternateColorCodes('&', s).length() > n) {
            return s.substring(0, n);
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public Objective getObjective() {
        return objective;
    }

    public void add(Player player) {
        player.setScoreboard(scoreboard);
    }

    public Enums.ScoreboardType getScoreboardType() {
        return scoreboardType;
    }
}