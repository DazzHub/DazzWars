package com.dazzhub.skywars.Utils.scoreboard;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.ArenaTeam;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Getter
public class ScoreBoardBuilder {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final BiMap<String, ScoreBoardEntry> entries;

    private int teamId;

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
        this.teamId = 1;

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

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setName(String title) {
        objective.setDisplayName(title);
    }

    public BiMap<String, ScoreBoardEntry> getEntries() {
        return HashBiMap.create(entries);
    }

    public ScoreBoardEntry getEntry(String key) {
        return entries.get(key);
    }

    public ScoreBoardEntry add(String name, int value) {
        return add((String) null, name, value, true);
    }

    public ScoreBoardEntry add(Enum key, String name, int value) {
        return add(key.name(), name, value);
    }

    public ScoreBoardEntry add(String key, String name, int value) {
        return add(key, name, value, false);
    }

    public ScoreBoardEntry add(Enum key, String name, int value, boolean overwrite) {
        return add(key.name(), name, value, overwrite);
    }

    public ScoreBoardEntry add(String key, String name, int value, boolean overwrite) {
        if (key == null && !contains(name)) {
            throw new IllegalArgumentException("Entry could not be found with the supplied name and no key was supplied");
        }

        if (overwrite && contains(name)) {
            ScoreBoardEntry entry = getEntryByName(name);
            if (key != null && entries.get(key) != entry) {
                throw new IllegalArgumentException("Supplied key references a different score than the one to be overwritten");
            }

            entry.setValue(value);
            return entry;
        }

        if (entries.get(key) != null) {
            throw new IllegalArgumentException("Score already exists with that key");
        }

        int count = 0;
        String origName = name;
        if (!overwrite) {
            Map<Integer, String> created = create(name);
            for (Entry<Integer, String> entry : created.entrySet()) {
                count = entry.getKey();
                name = entry.getValue();
            }
        }

        ScoreBoardEntry entry = new ScoreBoardEntry(key, this, value, origName, count);
        entry.update(name);
        entries.put(key, entry);
        return entry;
    }

    public void remove(String key) {
        remove(getEntry(key));
    }

    public void remove(ScoreBoardEntry entry) {
        if (entry.getScoreBoardBuilder() != this) {
            throw new IllegalArgumentException("Supplied entry does not belong to this Scoreboard");
        }

        String key = entries.inverse().get(entry);
        if (key != null) {
            entries.remove(key);
        }

        entry.remove();
    }

    private Map<Integer, String> create(String name) {
        int count = 0;
        while (contains(name)) {
            name = ChatColor.RESET + name;
            count++;
        }

        if (name.length() > 48) {
            name = name.substring(0, 47);
        }

        if (contains(name)) {
            throw new IllegalArgumentException("Could not find a suitable replacement name for '" + name + "'");
        }

        Map<Integer, String> created = new HashMap<>();
        created.put(count, name);
        return created;
    }

    public int getTeamId() {
        return teamId++;
    }

    public ScoreBoardEntry getEntryByName(String name) {
        for (ScoreBoardEntry entry : entries.values()) {
            if (entry.getName().equals(name)) {
                return entry;
            }
        }

        return null;
    }

    public boolean contains(String name) {
        for (ScoreBoardEntry entry : entries.values()) {
            if (entry.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public void add(Player player) {
        player.setScoreboard(scoreboard);
    }

    public Enums.ScoreboardType getScoreboardType() {
        return scoreboardType;
    }
}