package com.dazzhub.skywars.Utils.scoreboard;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@Getter
@Setter
public class ScoreBoardBuilder {

    private Scoreboard scoreboard;

    private Objective scoreObjective;
    private Objective tabObjective;
    private Objective nameHealthObj;

    private Team GameSpectator;
    private Team gameTeams;
    private Team gameEnemy;

    private boolean health, spectator, gamePlayers, teams;
    private Enums.ScoreboardType scoreboardType;

    public ScoreBoardBuilder(Player p, String score_name, boolean health, boolean spectator, boolean gamePlayers, boolean teams, Enums.ScoreboardType scoreboardType) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.scoreObjective = this.scoreboard.registerNewObjective(score_name, "dummy");
        this.scoreObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.scoreboardType = scoreboardType;
        this.health = health;
        this.spectator = spectator;
        this.gamePlayers = gamePlayers;
        this.teams = teams;

        if (health) {
            this.nameHealthObj = this.scoreboard.registerNewObjective("namelifee", "health");
            this.nameHealthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);
            this.nameHealthObj.setDisplayName(ChatColor.DARK_RED + "\u2764");
        }

        GamePlayer player = Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId());
        Arena arena = player.getArena();

        if (spectator){
            if (arena == null) return;

            this.GameSpectator = this.scoreboard.registerNewTeam("Spectator");
            for (GamePlayer gamePlayer : arena.getSpectators()) {
                if (gamePlayer != null) {
                    if (!gamePlayer.getPlayer().isOnline()) {
                        continue;
                    }
                    this.GameSpectator.addEntry(p.getName());
                }
            }
        }

        if (gamePlayers){
            this.gameEnemy = this.scoreboard.registerNewTeam("PlayerGaming");

            if (player.getLangMessage().getBoolean("Messages.ScoreBoard.Team.killCount")) {
                this.tabObjective = this.scoreboard.registerNewObjective("PlayerGaming", "dummy");
                this.tabObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            }

            if (arena == null) return;
            for (GamePlayer gamePlayer : arena.getPlayers()) {
                if (gamePlayer.getLangMessage().getBoolean("Messages.ScoreBoard.Team.killCount")) {
                    if (this.tabObjective != null){ this.tabObjective.getScore(gamePlayer.getPlayer().getName()).setScore(gamePlayer.getKillsStreak()); }
                }
                this.gameEnemy.addEntry(gamePlayer.getName());
            }
        }

        if (teams){
            this.gameTeams = this.scoreboard.registerNewTeam("TeamsFriends");
            if (arena == null) return;
            if (!player.isSpectating()) {
                if (player.getArenaTeam() != null && !player.getArenaTeam().getMembers().isEmpty()) {
                    for (GamePlayer gamePlayer1 : player.getArenaTeam().getMembers()) {
                        this.gameTeams.setPrefix(chars(gamePlayer1.getPlayer(), player.getLangMessage().getString("Messages.ScoreBoard.Team.TeamsFriends").split(":")[0]));
                        this.gameTeams.setSuffix(chars(gamePlayer1.getPlayer(), player.getLangMessage().getString("Messages.ScoreBoard.Team.TeamsFriends").split(":")[1]).replace("%kills%", String.valueOf(gamePlayer1.getKillsStreak())));
                    }
                }
            }
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
        if (GameSpectator == null) return;
        for (GamePlayer gamePlayer : arena.getSpectators()) {
            if (gamePlayer != null) {
                this.GameSpectator.setPrefix(chars(gamePlayer.getPlayer(), gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.Spectator").split(":")[0]));
                this.GameSpectator.setSuffix(chars(gamePlayer.getPlayer(), gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.Spectator").split(":")[1]).replace("%kills%", String.valueOf(gamePlayer.getKillsStreak())));
            }
        }
    }

    public void updateEnemy(GamePlayer player, Arena arena) {
        if (gameEnemy == null) return;
        if (tabObjective == null) return;
        for (GamePlayer gamePlayer : arena.getPlayers()) {

            if (player.getArenaTeam() != null && player.getArenaTeam().getMembers().contains(gamePlayer)) continue;

            if (!this.gameEnemy.hasEntry(gamePlayer.getName())) {
                this.tabObjective.getScore(gamePlayer.getName()).setScore(gamePlayer.getKillsStreak());
                this.gameEnemy.addEntry(gamePlayer.getName());
            }
            this.gameEnemy.setPrefix(chars(gamePlayer.getPlayer(), gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.Game").split(":")[0]));
            this.gameEnemy.setSuffix(chars(gamePlayer.getPlayer(), gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.Game").split(":")[1]).replace("%kills%", String.valueOf(gamePlayer.getKillsStreak())));
            this.tabObjective.getScore(gamePlayer.getPlayer().getName()).setScore(gamePlayer.getKillsStreak());
        }
    }

    public void updateTeams(GamePlayer gamePlayer) {
        if (gameTeams == null) return;
        if (tabObjective == null) return;

        if (!gamePlayer.isSpectating()) {
            if (gamePlayer.getArenaTeam() != null && !gamePlayer.getArenaTeam().getMembers().isEmpty()) {
                for (GamePlayer gamePlayer1 : gamePlayer.getArenaTeam().getMembers()) {
                    if (!gameTeams.hasEntry(gamePlayer1.getName())){
                        gameTeams.addEntry(gamePlayer1.getName());
                        this.tabObjective.getScore(gamePlayer.getName()).setScore(gamePlayer.getKillsStreak());
                    }

                    this.gameTeams.setPrefix(chars(gamePlayer1.getPlayer(), gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.TeamsFriends").split(":")[0]));
                    this.gameTeams.setSuffix(chars(gamePlayer1.getPlayer(), gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Team.TeamsFriends").split(":")[1]).replace("%kills%", String.valueOf(gamePlayer1.getKillsStreak())));
                    this.tabObjective.getScore(gamePlayer.getPlayer().getName()).setScore(gamePlayer.getKillsStreak());
                }
            }
        }
    }

    private String chars(Player p, String c) {
        String transformed = (ChatColor.translateAlternateColorCodes('&', c));

        if (Main.getPlugin().getSettings().getBoolean("UsePlaceholderAPI")) {
            transformed = PlaceholderAPI.setPlaceholders(p, transformed);
        }

        return transformed;
    }

    private int absorbHearts(Player pl) {
        for (PotionEffect pe : pl.getActivePotionEffects()) {
            if (pe.getType().equals(PotionEffectType.ABSORPTION)) {
                return pe.getAmplifier() * 2 + 2;
            }
        }
        return 0;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public void setName(String substring) {
        if (substring.length() > 42) {
            substring = substring.substring(0, 42);
        }
        this.scoreObjective.setDisplayName(this.color(substring));
    }

    public void lines(Integer line, String text) {
        text = color(text);
        Team team = this.scoreboard.getTeam("SCT_" + line);
        if (text.length() > 32) {
            text = text.substring(0, 32);
        }
        String[] splitStringLine = this.splitStringLine(text);
        if (team == null) {
            Team registerNewTeam = this.scoreboard.registerNewTeam("SCT_" + line);
            registerNewTeam.addEntry(this.getEntry(line));
            this.setPrefix(registerNewTeam, splitStringLine[0]);
            this.setSuffix(registerNewTeam, splitStringLine[1]);
            this.scoreObjective.getScore(this.getEntry(line)).setScore(line);
        } else {
            this.setPrefix(team, splitStringLine[0]);
            this.setSuffix(team, splitStringLine[1]);
        }
    }

    /*public boolean dLine(Integer line) {
        Team team = this.scoreboard.getTeam("SCT_" + line);
        if (team != null) {
            team.unregister();
            scoreObjective.getScoreboard().resetScores(getEntry(line));
            return true;
        }
        return false;
    }*/

    public void setPrefix(Team team, String prefix) {
        if (prefix.length() > 16) {
            team.setPrefix(prefix.substring(0, 16));
            return;
        }
        team.setPrefix(prefix);
    }

    public void setSuffix(Team team, String s) {
        if (s.length() > 16) {
            team.setSuffix(this.maxChars(16, s));
        } else {
            team.setSuffix(s);
        }
    }

    public String maxChars(int n, String s) {
        if (ChatColor.translateAlternateColorCodes('&', s).length() > n) {
            return s.substring(0, n);
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public String getEntry(Integer n) {
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
        return this.scoreboard;
    }

    public void build(Player player) {
        player.setScoreboard(this.scoreboard);
    }

    private String[] splitStringLine(String s) {
        StringBuilder sb = new StringBuilder(s.substring(0, Math.min(s.length(), 16)));
        StringBuilder sb2 = new StringBuilder((s.length() > 16) ? s.substring(16) : "");
        if (sb.toString().length() > 1 && sb.charAt(sb.length() - 1) == '§') {
            sb.deleteCharAt(sb.length() - 1);
            sb2.insert(0, '§');
        }
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < sb.toString().length(); ++i) {
            if (sb.toString().charAt(i) == '§' && i < sb.toString().length() - 1) {
                string.append("§").append(sb.toString().charAt(i + 1));
            }
        }
        String string2 = String.valueOf(sb2);
        if (sb.length() > 14) {
            string2 = ((string.length() == 0) ? ("§" + string2) : (string + string2));
        }
        return new String[]{(sb.toString().length() > 16) ? sb.toString().substring(0, 16) : sb.toString(), (string2.length() > 16) ? string2.substring(0, 16) : string2};
    }

}
