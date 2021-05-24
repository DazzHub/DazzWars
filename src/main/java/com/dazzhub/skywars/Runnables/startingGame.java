package com.dazzhub.skywars.Runnables;

import com.cryptomorin.xseries.messages.Titles;
import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Runnable.RunnableFactory;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class startingGame implements Runnable {

    private Main main;

    private Arena arena;
    private RunnableFactory factory;
    private int timer;
    private boolean checkForce;

    public startingGame(RunnableFactory factory, Arena arena) {
        this.main = Main.getPlugin();

        this.factory = factory;
        this.arena = arena;
        this.timer = arena.getStartingGame();
        this.checkForce = true;
    }

    @Override
    public void run() {

        if (this.checkForce && this.arena.isForceStart()){
            this.timer = 5;
            this.checkForce = false;
        }

        if (!this.arena.checkStart()) {
            this.cancel();

            arena.getPlayers().forEach(p -> {
                p.sendMessage(p.getLangMessage().getString("Messages.Timer.Cancelled", "Error Timer.Cancelled").replace("%seconds%", String.valueOf(timer)));
                String sound = p.getLangMessage().getString("Messages.Sounds.Cancelled", "AMBIENT_CAVE");
                p.playSound(sound);
            });

            arena.setGameStatus(Enums.GameStatus.WAITING);
            this.timer = arena.getStartingGame();
            this.arena.setUsable(false);

            this.arena.setForceStart(false);
            this.checkForce = true;
        }


        arena.getPlayers().forEach(gamePlayer -> {
            gamePlayer.getPlayer().setLevel(getTimer());
            gamePlayer.getLangMessage().getConfigurationSection("Messages.Timer.Starting").getKeys(false).stream().filter(s -> timer == Integer.parseInt(s)).forEach(s -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Timer.Starting." + s, "Timer.Starting." + s).replaceAll("%seconds%", String.valueOf(timer))));
            gamePlayer.getLangMessage().getConfigurationSection("Messages.Sounds.Starting").getKeys(false).stream().filter(s -> timer == Integer.parseInt(s)).forEach(s -> gamePlayer.playSound(gamePlayer.getLangMessage().getString("Messages.Sounds.Starting." + s, "AMBIENT_CAVE")));
            gamePlayer.getLangMessage().getConfigurationSection("Messages.Title.Starting").getKeys(false).stream().filter(time_config -> timer == Integer.parseInt(time_config)).forEach(time_config -> Titles.sendTitle(gamePlayer.getPlayer(), gamePlayer.getLangMessage().getInt("Messages.Title.Fade",20), gamePlayer.getLangMessage().getInt("Messages.Title.Stay",20), gamePlayer.getLangMessage().getInt("Messages.Title.Out",20), c(gamePlayer.getLangMessage().getString("Messages.Title.Starting." + time_config, "Error title starting").split(";")[0]).replaceAll("%seconds%", String.valueOf(timer)), c(gamePlayer.getLangMessage().getString("Messages.Title.Starting." + time_config, "Error subtitle starting").split(";")[1]).replaceAll("%seconds%", String.valueOf(timer))));
        });


        if (timer <= 0) {
            arena.checkVotes();

            arena.getPlayers().forEach(p -> {
                Titles.sendTitle(p.getPlayer(), p.getLangMessage().getInt("Messages.LuckTitle.Fade",20), p.getLangMessage().getInt("Messages.LuckTitle.Stay",20), p.getLangMessage().getInt("Messages.LuckTitle.Out",20), c(p.getLangMessage().getString("Messages.LuckTitle.Info", "Error title LuckTitle;&f").split(";")[0]).replace("%player%", p.getPlayer().getName()), c(p.getLangMessage().getString("Messages.LuckTitle.Info", "&f;Error subtitle LuckTitle").split(";")[1]).replace("%player%", p.getPlayer().getName()));
                announcerVote(p);

                p.resetPlayer(false);

                switch (arena.getMode()) {
                    case SOLO: {
                        main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.INGAME, true, false, true, true);
                        main.getCageManager().getCagesSolo().get(p.getCageSolo()).removeCage(p.getArenaTeam().getSpawn());
                        if (!p.getKitSolo().equals("none")) {
                            main.getKitManager().giveKit(p.getKitSolo().toLowerCase(), arena.getMode().name(), p.getPlayer(), p);
                        }

                        p.addGamesSolo();
                        break;
                    }
                    case TEAM: {
                        main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.INGAMETEAM, true, false, true, true);
                        main.getCageManager().getCagesTeam().get(p.getCageTeam()).removeCage(p.getArenaTeam().getSpawn());
                        if (!p.getKitTeam().equals("none")) {
                            main.getKitManager().giveKit(p.getKitTeam().toLowerCase(), arena.getMode().name(), p.getPlayer(), p);
                        }

                        p.addGamesTeam();
                        break;
                    }
                    case RANKED: {
                        main.getScoreBoardAPI().setScoreBoard(p.getPlayer(), Enums.ScoreboardType.INGAMERANKED, true, false, true, true);
                        main.getCageManager().getCagesRanked().get(p.getCageRanked()).removeCage(p.getArenaTeam().getSpawn());
                        if (!p.getKitTeam().equals("none")) {
                            main.getKitManager().giveKit(p.getKitRanked().toLowerCase(), arena.getMode().name(), p.getPlayer(), p);
                        }

                        p.addGamesRanked();
                        break;
                    }
                }

            });

            arena.inGame();
            this.cancel();
        }

        this.arena.setTotalTime(timer);
        timer--;
    }

    private void announcerVote(GamePlayer gamePlayer){
        Configuration lang = gamePlayer.getLangMessage();

        List<String> message1 = lang.getStringList("Messages.InfoGame");

        List<String> message = message1.stream().map(s -> s
                .replace("%chest%", arena.getChestType()
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

                .replace("%event%", arena.getEventsType().name()
                        .replace("BORDER", lang.getString("Messages.TypeVote.Events.border", "border"))
                        .replace("DRAGON", lang.getString("Messages.TypeVote.Events.dragon", "dragon"))
                        .replace("DROPPARTY", lang.getString("Messages.TypeVote.Events.dropparty", "dropparty"))
                        .replace("STORM", lang.getString("Messages.TypeVote.Events.storm", "storm"))
                        .replace("TNTFALL", lang.getString("Messages.TypeVote.Events.tntfall", "tntfall"))
                )

                .replace("%scenario%", arena.getScenariosType().name()
                        .replace("NOCLEAN", lang.getString("Messages.TypeVote.Scenario.noclean", "noclean"))
                        .replace("NOFALL", lang.getString("Messages.TypeVote.Scenario.nofall", "nofall"))
                        .replace("NOPROJECTILE", lang.getString("Messages.TypeVote.Scenario.noprojectile", "noprojectile"))
                )

                .replace("NONE", lang.getString("Messages.TypeVote.none", "none"))
        ).collect(Collectors.toList());

        message.forEach(gamePlayer::sendMessage);
    }

    private void cancel(){
        this.factory.getRunnableWorker(this, false).remove(this);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
