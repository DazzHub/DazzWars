package com.dazzhub.skywars.Utils.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreBoardBuilder {

    private Scoreboard scoreboard;
    private boolean reset;

    private Objective scoreObjective;

    public ScoreBoardBuilder(Player p, String score_name, boolean health) {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.scoreObjective = this.scoreboard.registerNewObjective(score_name, "dummy");
        this.scoreObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public String color(String s) {
        return s.replaceAll("&", "§");
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

    public boolean isReset() {
        return this.reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
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
        String string = "";
        for (int i = 0; i < sb.toString().length(); ++i) {
            if (sb.toString().charAt(i) == '§' && i < sb.toString().length() - 1) {
                string = string + "§" + sb.toString().charAt(i + 1);
            }
        }
        String string2 = String.valueOf(sb2);
        if (sb.length() > 14) {
            string2 = (string.isEmpty() ? ("§" + string2) : (string + string2));
        }
        return new String[]{(sb.toString().length() > 16) ? sb.toString().substring(0, 16) : sb.toString(), (string2.length() > 16) ? string2.substring(0, 16) : string2};
    }

}
