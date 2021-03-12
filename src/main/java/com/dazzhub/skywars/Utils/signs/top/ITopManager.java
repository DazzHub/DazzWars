package com.dazzhub.skywars.Utils.signs.top;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Runnables.inGame;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Runnable.RunnableFactory;
import com.dazzhub.skywars.Utils.Runnable.RunnableType;
import com.dazzhub.skywars.Utils.Runnable.RunnableWorkerType;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardBuilder;
import com.dazzhub.skywars.Utils.signs.ISignLocation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class ITopManager {

    private Main main;
    private HashMap<Location, ITop> tops;
    private int task;

    public ITopManager(Main main){
        this.main = main;
        this.tops = new HashMap<>();
    }

    public void loadSign() {
        Configuration signs = main.getSigns();

        if (!signs.getString("Top", "").isEmpty()) {

            signs.getConfigurationSection("Top").getKeys(false).forEach(key ->{

                String[] type = signs.getString("Top." + key).split(";");

                Location location = ISignLocation.getLocation(type[0]);

                String[] lines = Stream.of(type[1].split(",")).toArray(String[]::new);

                Block block = location.getWorld().getBlockAt(location);
                Sign sign = (Sign) block.getState();

                createSigns(null, sign, lines, false);
            });
        }

        updateTop();
    }

    public void createSigns(Player p, Sign sign, String[] lines, boolean save) {
        String[] owner = null;

        if (lines[3].equalsIgnoreCase("solo")){
            if (lines[2].equalsIgnoreCase("%kills%")){
                owner = main.getPlayerDB().TopKillsSolo(10);
            } else if (lines[2].equalsIgnoreCase("%deaths%")){
                owner = main.getPlayerDB().TopDeathsSolo(10);
            } else if (lines[2].equalsIgnoreCase("%wins%")){
                owner = main.getPlayerDB().TopWinsSolo(10);
            }
        } else if (lines[3].equalsIgnoreCase("team")){
            if (lines[2].equalsIgnoreCase("%kills%")){
                owner = main.getPlayerDB().TopKillsTeam(10);
            } else if (lines[2].equalsIgnoreCase("%deaths%")){
                owner = main.getPlayerDB().TopDeathsTeam(10);
            } else if (lines[2].equalsIgnoreCase("%wins%")){
                owner = main.getPlayerDB().TopWinsTeam(10);
            }
        } else if (lines[3].equalsIgnoreCase("ranked")){
            if (lines[2].equalsIgnoreCase("%kills%")){
                owner = main.getPlayerDB().TopKillsRanked(10);
            } else if (lines[2].equalsIgnoreCase("%deaths%")){
                owner = main.getPlayerDB().TopDeathsRanked(10);
            } else if (lines[2].equalsIgnoreCase("%wins%")){
                owner = main.getPlayerDB().TopWinsRanked(10);
            } else if (lines[2].equalsIgnoreCase("%lvl%")){
                owner = main.getPlayerDB().TopLvlRanked(10);
            }
        }

        if (owner == null) {
            Console.warning("Create sign top error in lines!");
            return;
        }

        if (Integer.parseInt(lines[1]) == 0){
            Console.warning("Create sign top error in lines!");
            return;
        }

        if (tops.containsKey(sign.getLocation())) {
            if (p != null) {
                p.sendMessage(c("&a&l\u2714 &fThe top sign already exist in location!"));
                XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
                return;
            }
        }

        File file = this.main.getConfigUtils().getFile(this.main, "Signs");
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Signs");
        Location location = ISignLocation.getLocation(ISignLocation.getString(sign.getLocation(), false));

        int total = Integer.parseInt(lines[1]) - 1;
        ITop top = new ITop(sign, owner[total], Arrays.asList(lines));
        top.updateSign();

        if (save){
            int amount;
            try {
                amount = config.getConfigurationSection("Top").getKeys(false).size() + 1;
            }
            catch (Exception ex) {
                amount = 1;
            }

            config.set("Top."+amount, ISignLocation.getString(sign.getLocation(), false) + ";" + Arrays.toString(lines).replace("[", "").replace("]","").replace(" ", ""));

            try {
                config.save(file);
            } catch (IOException e) {
                Console.error(e.getMessage());
            }
        }

        tops.put(location, top);

        if (p != null) {
            p.sendMessage(c("&a&l\u2714 &fThe top sign created successfully!"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_YES.parseSound()));
        }
    }

    private void updateTop(){
        RunnableFactory factory = Main.getPlugin().getFactory();

        factory.registerRunnable(RunnableWorkerType.ASYNC, RunnableType.TIMER, 20L,

        new Runnable() {
            int timer = main.getSettings().getInt("TopUpdate");

            @Override
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()){
                    GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

                    if (gamePlayer == null) continue;

                    ScoreBoardBuilder scoreboard = gamePlayer.getScoreBoardBuilder();

                    if (scoreboard == null) continue;

                    if (scoreboard.getScoreboardType() == Enums.ScoreboardType.LOBBY) {
                        if (!main.getSettings().getStringList("lobbies.onScoreboard").contains(p.getWorld().getName())){
                            return;
                        }
                    }

                    Configuration config = main.getPlayerManager().getPlayer(p.getUniqueId()).getScoreboardMessage();
                    scoreboard.setName(main.getScoreBoardAPI().charsLobby(p, config.getString(scoreboard.getScoreboardType().toString() + ".title")));

                    int line = config.getStringList(scoreboard.getScoreboardType().toString() + ".lines").size();

                    for (String s : config.getStringList(scoreboard.getScoreboardType().toString() + ".lines")) {
                        scoreboard.lines(line, main.getScoreBoardAPI().charsLobby(p, s));
                        line--;
                    }

                    if (gamePlayer.isInArena()){
                        if (scoreboard.isHealth()) scoreboard.updatelife(gamePlayer.getArena());
                        if (scoreboard.isSpectator()) scoreboard.updateSpectator(gamePlayer.getArena());
                        if (scoreboard.isGamePlayers()) scoreboard.updateEnemy(gamePlayer, gamePlayer.getArena());
                        if (scoreboard.isTeams()) scoreboard.updateTeams(gamePlayer);
                    }
                }

                if (timer <= 1) {
                    for (ITop iTop : tops.values()) {
                        iTop.updateSign();
                    }

                    Bukkit.getScheduler().runTask(main, () -> Bukkit.getOnlinePlayers().stream().map(on -> main.getPlayerManager().getPlayer(on.getUniqueId())).filter(gamePlayer -> gamePlayer.getHolograms() != null).forEach(gamePlayer -> gamePlayer.getHolograms().reloadHologram()));

                    timer = main.getSettings().getInt("TopUpdate");
                }

                timer--;
            }
        });
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

    public HashMap<Location, ITop> getTops() {
        return tops;
    }
}
