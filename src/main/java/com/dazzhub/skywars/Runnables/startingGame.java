package com.dazzhub.skywars.Runnables;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import com.dazzhub.skywars.Utils.xseries.Titles;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.ELF;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class startingGame extends BukkitRunnable {

    private Main main;

    private Arena arena;
    private int timer;

    public startingGame(Arena arena) {
        this.main = Main.getPlugin();

        this.arena = arena;
        this.timer = arena.getStartingGame();
    }

    @Override
    public void run() {

        /* CHECKER PLAYER NEED TO START */
        if (!this.arena.checkStart()) {
            this.cancel();

            arena.getPlayers().forEach(p -> {
                p.sendMessage(p.getLangMessage().getString("Messages.Timer.Cancelled", "error config").replace("%seconds%", String.valueOf(timer)));
                String sound = p.getLangMessage().getString("Messages.Sounds.Cancelled");
                p.playSound(sound);
            });

            arena.setGameStatus(Enums.GameStatus.WAITING);
            this.timer = arena.getStartingGame();
            this.arena.setUsable(false);

            arena.setStartingGameTask(new startingGame(arena));
        }

        /* SYNC DATA */
        Bukkit.getScheduler().runTask(this.main, () -> {
            arena.getPlayers().forEach(p -> {
                p.getPlayer().setLevel(timer);
            });
        });

        /* ASYNC DATA */
        arena.getPlayers().forEach(gamePlayer -> {
            gamePlayer.getLangMessage().getConfigurationSection("Messages.Timer.Starting").getKeys(false).stream().filter(s -> timer == Integer.parseInt(s)).forEach(s -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Timer.Starting." + s).replaceAll("%seconds%", String.valueOf(timer))));

            gamePlayer.getLangMessage().getConfigurationSection("Messages.Sounds.Starting").getKeys(false).stream().filter(s -> timer == Integer.parseInt(s)).forEach(s -> gamePlayer.playSound(gamePlayer.getLangMessage().getString("Messages.Sounds.Starting." + s)));

            gamePlayer.getLangMessage().getConfigurationSection("Messages.Title.Starting").getKeys(false).stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> Titles.sendTitle(gamePlayer.getPlayer(), gamePlayer.getLangMessage().getInt("Messages.Title.Fade"), gamePlayer.getLangMessage().getInt("Messages.Title.Stay"), gamePlayer.getLangMessage().getInt("Messages.Title.Out"), gamePlayer.getLangMessage().getString("Messages.Title.Starting." + time_config).split(";")[0].replaceAll("%seconds%", String.valueOf(timer)), gamePlayer.getLangMessage().getString("Messages.Title.Starting." + time_config).split(";")[1].replaceAll("%seconds%", String.valueOf(timer))));
        });

        if (timer <= 0){

            arena.checkVotes();

            arena.getPlayers().forEach(p -> {
                /* ASYNC DATA */
                Titles.sendTitle(p.getPlayer(), p.getLangMessage().getInt("Messages.LuckTitle.Fade"), p.getLangMessage().getInt("Messages.LuckTitle.Stay"), p.getLangMessage().getInt("Messages.LuckTitle.Out"), p.getLangMessage().getString("Messages.LuckTitle.Info").split(";")[0].replace("%player%", p.getPlayer().getName()), p.getLangMessage().getString("Messages.LuckTitle.Info").split(";")[1].replace("%player%", p.getPlayer().getName()));
                announcerVote(p);

                /* SYNC DATA */
                Bukkit.getScheduler().runTask(main, () -> {
                    arena.removeCage(p, arena.getMode(),3);
                    p.resetPlayer(false);

                    switch (arena.getMode()) {
                        case SOLO:
                            main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), ScoreBoardAPI.ScoreboardType.INGAME);

                            if (!p.getKitSolo().equals("none")) {
                                main.getiKitManager().giveKit(p.getKitSolo().toLowerCase(), arena.getMode().getName(), p.getPlayer(), p);
                            }

                            p.addGamesSolo();
                            break;
                        case TEAM:
                            main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), ScoreBoardAPI.ScoreboardType.INGAMETEAM);

                            if (!p.getKitTeam().equals("none")) {
                                main.getiKitManager().giveKit(p.getKitTeam().toLowerCase(), arena.getMode().getName(), p.getPlayer(), p);
                            }

                            p.addGamesTeam();
                            break;
                    }

                });
            });

            this.arena.setGameStatus(Enums.GameStatus.INGAME);
            arena.getInGameTask().runTaskTimerAsynchronously(Main.getPlugin(), 0, 20L);
            this.cancel();
        }

        timer--;
    }

    private void announcerVote(GamePlayer gamePlayer){
        Configuration lang = gamePlayer.getLangMessage();

        List<String> message1 = lang.getStringList("Messages.InfoGame");

        List<String> message = message1.stream().map(s -> s
                .replace("%chest%", arena.getChestType().name()
                        .replace("BASIC", lang.getString("Messages.TypeVote.Chest.basic", "basic"))
                        .replace("NORMAL", lang.getString("Messages.TypeVote.Chest.normal", "normal"))
                        .replace("OP", lang.getString("Messages.TypeVote.Chest.op", "op"))
                )

                .replace("%time%", arena.getTimeType().name()
                        .replace("DAY", lang.getString("Messages.TypeVote.Time.day", "day"))
                        .replace("SUNSET", lang.getString("Messages.TypeVote.Time.sunset", "sunset"))
                        .replace("NIGHT", lang.getString("Messages.TypeVote.Time.night", "night"))
                )

                .replace("%heart%", arena.getHealthType().name()
                        .replace("HEART10", lang.getString("Messages.TypeVote.Heart.10h", "10 hearts"))
                        .replace("HEART20", lang.getString("Messages.TypeVote.Heart.20h", "20 hearts"))
                        .replace("HEART30", lang.getString("Messages.TypeVote.Heart.30h", "30 hearts"))
                )

                .replace("NONE", lang.getString("Messages.TypeVote.none", "none"))
        ).collect(Collectors.toList());

        message.forEach(gamePlayer::sendMessage);
    }

}