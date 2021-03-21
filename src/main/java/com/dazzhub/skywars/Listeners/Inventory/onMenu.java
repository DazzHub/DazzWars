package com.dazzhub.skywars.Listeners.Inventory;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.ClickMenu;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.inventory.menu.IMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class onMenu implements Listener {

    private Main main;

    public onMenu(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick2(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player p = (Player) event.getWhoClicked();

        String invName = event.getView().getTitle();

        String inv = main.getPlayerManager().getPlayer(p.getUniqueId()).getLangMessage().getString("Messages.SoulWell.NameMenu");

        if (inv == null) return;

        if (invName.equalsIgnoreCase(c(inv))) {
            if(event.getView().getTopInventory().equals(event.getInventory())) event.setCancelled(true);
            if(event.getView().getBottomInventory().equals(event.getInventory())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(ClickMenu event){
        String cmd = event.getOrdItems().getCommand();

        if (cmd == null || cmd.equals("")) {
            return;
        }

        if (cmd.contains(";")) {
            String[] array = cmd.split(";");
            String[] array2;
            for (int length = (array2 = array).length, i = 0; i < length; ++i) {
                String sub = array2[i];
                if (sub.startsWith(" ")) {
                    sub = sub.substring(1);
                }
                parseCommand(event.getGamePlayer().getPlayer(), sub, event.getOrdItems().getPrice());
            }
        } else {
            parseCommand(event.getGamePlayer().getPlayer(), cmd, event.getOrdItems().getPrice());
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void handleCommandEvent(PlayerCommandPreprocessEvent e) {
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(e.getPlayer().getUniqueId());

        if (gamePlayer == null) return;

        String cmd = e.getMessage();
        cmd = cmd.substring(1);

        if (cmd.length() == 0) {
            return;
        }

        String file = this.main.getMenuManager().getMenuLangs().get(gamePlayer.getLang()).getMenuCommand().get(cmd.toLowerCase());
        if (file != null) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            this.main.getMenuManager().getMenuLangs().get(gamePlayer.getLang()).openInventory(file, p);
        }
    }

    private void parseCommand(Player p, String cmd, Integer price) {
        if (cmd != null && !cmd.equals("")) {

            if (cmd.contains("%player%")) {
                cmd = cmd.replace("%player%", p.getName());
            }

            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            if (cmd.startsWith("console:")) {
                String consoleCommand = cmd.substring(8);
                if (consoleCommand.startsWith(" ")) {
                    consoleCommand = consoleCommand.substring(1);
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
            } else if (cmd.startsWith("player:")) {
                String playerCmd = cmd.substring(7);
                if (playerCmd.startsWith(" ")) {
                    playerCmd = playerCmd.substring(1);
                }
                p.chat(playerCmd);
            } else if (cmd.startsWith("open:")) {
                String openCommand = cmd.substring(5);
                if (openCommand.startsWith(" ")) {
                    openCommand = openCommand.substring(1);
                }

                IMenu menu = this.main.getMenuManager().getMenuLangs().get(gamePlayer.getLang()).getMenuFileName().get(openCommand);
                if (menu == null) return;

                Bukkit.getScheduler().runTaskLater(main,() -> menu.open(p),2);
            } else if (cmd.startsWith("cage:")) {
                String action = cmd.substring(5);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String[] cage = action.split("/");

                if (cage[1].equalsIgnoreCase("SOLO")) {
                    if (main.getCageManager().getCagesSolo().containsKey(cage[0])) {

                        if (!gamePlayer.getCagesSoloList().contains(cage[0])) {

                            if (price != 0 && gamePlayer.getCoins() < price) {
                                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                                return;
                            }

                            gamePlayer.getCagesSoloList().add(cage[0]);
                            gamePlayer.setCageSolo(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Buy").replace("%cage%", cage[0]));
                            gamePlayer.removeCoins(price);

                            if (gamePlayer.isInArena()){
                                main.getCageManager().getCagesSolo().get(gamePlayer.getCageSolo()).loadCage(gamePlayer.getArenaTeam().getSpawn());
                            }

                        } else {
                            if (!gamePlayer.getCageSolo().equals(cage[0])){
                                if (gamePlayer.isInArena()){
                                    main.getCageManager().getCagesSolo().get(cage[0]).loadCage(gamePlayer.getArenaTeam().getSpawn());
                                }
                            }
                            gamePlayer.setCageSolo(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Selected").replace("%cage%", cage[0]));
                        }

                    }
                } else if (cage[1].equalsIgnoreCase("TEAM")) {
                    if (main.getCageManager().getCagesTeam().containsKey(cage[0])) {

                        if (!gamePlayer.getCagesTeamList().contains(cage[0])) {

                            if (price != 0 && gamePlayer.getCoins() < price) {
                                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                                return;
                            }

                            gamePlayer.getCagesTeamList().add(cage[0]);
                            gamePlayer.setCageTeam(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Buy").replace("%cage%", cage[0]));
                            gamePlayer.removeCoins(price);

                            if (gamePlayer.isInArena()){
                                main.getCageManager().getCagesTeam().get(gamePlayer.getCageTeam()).loadCage(gamePlayer.getArenaTeam().getSpawn());
                            }

                        } else {

                            if (!gamePlayer.getCageTeam().equals(cage[0])){
                                if (gamePlayer.isInArena()){
                                    main.getCageManager().getCagesTeam().get(cage[0]).loadCage(gamePlayer.getArenaTeam().getSpawn());
                                }
                            }

                            gamePlayer.setCageTeam(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Selected").replace("%cage%", cage[0]));
                        }
                    }
                } else if (cage[1].equalsIgnoreCase("RANKED")) {
                    if (main.getCageManager().getCagesRanked().containsKey(cage[0])) {

                        if (!gamePlayer.getCagesRankedList().contains(cage[0])) {

                            if (price != 0 && gamePlayer.getCoins() < price) {
                                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                                return;
                            }

                            gamePlayer.getCagesRankedList().add(cage[0]);
                            gamePlayer.setCageRanked(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Buy").replace("%cage%", cage[0]));
                            gamePlayer.removeCoins(price);

                            if (gamePlayer.isInArena()){
                                main.getCageManager().getCagesRanked().get(gamePlayer.getCageRanked()).loadCage(gamePlayer.getArenaTeam().getSpawn());
                            }

                        } else {
                            if (!gamePlayer.getCageTeam().equals(cage[0])){
                                if (gamePlayer.isInArena()){
                                    main.getCageManager().getCagesRanked().get(cage[0]).loadCage(gamePlayer.getArenaTeam().getSpawn());
                                }
                            }

                            gamePlayer.setCageRanked(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Selected").replace("%cage%", cage[0]));
                        }
                    }
                }

            } else if (cmd.startsWith("kit:")) {
                String action = cmd.substring(4);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String[] kit = action.split("/");

                if (kit[1].equalsIgnoreCase("SOLO")) {
                    if (main.getKitManager().getKitSoloHashMap().containsKey(kit[0].toLowerCase())) {

                        if (!gamePlayer.getKitSoloList().contains(kit[0])) {

                            if (price != 0 && gamePlayer.getCoins() < price) {
                                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                                return;
                            }

                            gamePlayer.getKitSoloList().add(kit[0]);
                            gamePlayer.setKitSolo(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Buy").replace("%kit%", kit[0]));
                            gamePlayer.removeCoins(price);
                        } else {
                            gamePlayer.setKitSolo(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Selected").replace("%kit%", kit[0]));
                        }

                    }
                } else if (kit[1].equalsIgnoreCase("TEAM")) {
                    if (main.getKitManager().getKitTeamHashMap().containsKey(kit[0].toLowerCase())) {

                        if (!gamePlayer.getKitTeamList().contains(kit[0])) {

                            if (price != 0 && gamePlayer.getCoins() < price) {
                                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                                return;
                            }

                            gamePlayer.getKitTeamList().add(kit[0]);
                            gamePlayer.setKitTeam(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Buy").replace("%kit%", kit[0]));
                            gamePlayer.removeCoins(price);
                        } else {
                            gamePlayer.setKitTeam(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Selected").replace("%kit%", kit[0]));
                        }

                    }
                } else if (kit[1].equalsIgnoreCase("RANKED")) {
                    if (main.getKitManager().getKitRankedHashMap().containsKey(kit[0].toLowerCase())) {

                        if (!gamePlayer.getKitRankedList().contains(kit[0])) {

                            if (price != 0 && gamePlayer.getCoins() < price) {
                                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                                return;
                            }

                            gamePlayer.getKitRankedList().add(kit[0]);
                            gamePlayer.setKitRanked(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Buy").replace("%kit%", kit[0]));
                            gamePlayer.removeCoins(price);
                        } else {
                            gamePlayer.setKitRanked(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Selected").replace("%kit%", kit[0]));
                        }

                    }
                }
            } else if (cmd.startsWith("wineffect:")) {
                String action = cmd.substring(10);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String[] wineffect = action.split("/");

                if (wineffect[1].equalsIgnoreCase("SOLO")) {
                    if (!gamePlayer.getWinEffectsSoloList().contains(wineffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getWinEffectsSoloList().add(wineffect[0]);
                        gamePlayer.setWinEffectSolo(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Buy").replace("%win%", wineffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setWinEffectSolo(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Selected").replace("%win%", wineffect[0]));
                    }
                } else if (wineffect[1].equalsIgnoreCase("TEAM")) {
                    if (!gamePlayer.getWinEffectsTeamList().contains(wineffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getWinEffectsTeamList().add(wineffect[0]);
                        gamePlayer.setWinEffectTeam(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Buy").replace("%win%", wineffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setWinEffectTeam(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Selected").replace("%win%", wineffect[0]));
                    }
                } else if (wineffect[1].equalsIgnoreCase("RANKED")) {
                    if (!gamePlayer.getWinEffectsRankedList().contains(wineffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getWinEffectsRankedList().add(wineffect[0]);
                        gamePlayer.setWinEffectRanked(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Buy").replace("%win%", wineffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setWinEffectRanked(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Selected").replace("%win%", wineffect[0]));
                    }
                }
            } else if (cmd.startsWith("killeffect:")) {
                String action = cmd.substring(11);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String[] killeffect = action.split("/");

                if (killeffect[1].equalsIgnoreCase("SOLO")) {
                    if (!gamePlayer.getKillEffectsSoloList().contains(killeffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getKillEffectsSoloList().add(killeffect[0]);
                        gamePlayer.setKillEffectSolo(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Buy").replace("%kill%", killeffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setKillEffectSolo(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Selected").replace("%kill%", killeffect[0]));
                    }
                } else if (killeffect[1].equalsIgnoreCase("TEAM")) {
                    if (!gamePlayer.getKillEffectsTeamList().contains(killeffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getKillEffectsTeamList().add(killeffect[0]);
                        gamePlayer.setKillEffectTeam(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Buy").replace("%kill%", killeffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setKillEffectTeam(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Selected").replace("%kill%", killeffect[0]));
                    }
                } else if (killeffect[1].equalsIgnoreCase("RANKED")) {
                    if (!gamePlayer.getKillEffectsRankedList().contains(killeffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getKillEffectsRankedList().add(killeffect[0]);
                        gamePlayer.setKillEffectRanked(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Buy").replace("%kill%", killeffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setKillEffectRanked(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Selected").replace("%kill%", killeffect[0]));
                    }
                }
            } else if (cmd.startsWith("traileffect:")) {
                String action = cmd.substring(12);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String[] traileffect = action.split("/");

                if (traileffect[1].equalsIgnoreCase("SOLO")) {
                    if (!gamePlayer.getTrailsSoloList().contains(traileffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getTrailsSoloList().add(traileffect[0]);
                        gamePlayer.setTrailSolo(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Buy").replace("%trail%", traileffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setTrailSolo(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Selected").replace("%trail%", traileffect[0]));
                    }
                } else if (traileffect[1].equalsIgnoreCase("TEAM")) {
                    if (!gamePlayer.getTrailsTeamList().contains(traileffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getTrailsTeamList().add(traileffect[0]);
                        gamePlayer.setTrailTeam(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Buy").replace("%trail%", traileffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setTrailTeam(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Selected").replace("%trail%", traileffect[0]));
                    }
                } else if (traileffect[1].equalsIgnoreCase("RANKED")) {
                    if (!gamePlayer.getTrailsRankedList().contains(traileffect[0])) {

                        if (price != 0 && gamePlayer.getCoins() < price) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                            return;
                        }

                        gamePlayer.getTrailsRankedList().add(traileffect[0]);
                        gamePlayer.setTrailRanked(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Buy").replace("%trail%", traileffect[0]));
                        gamePlayer.removeCoins(price);
                    } else {
                        gamePlayer.setTrailRanked(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Selected").replace("%trail%", traileffect[0]));
                    }
                }
            } else if (cmd.startsWith("soul:")) {
                String souls = cmd.substring(5);
                if (souls.startsWith(" ")) {
                    souls = souls.substring(1);
                }

                gamePlayer.addSouls(Integer.parseInt(souls));
            } else if (cmd.startsWith("join:")) {
                String arenaName = cmd.substring(5);
                if (arenaName.startsWith(" ")) {
                    arenaName = arenaName.substring(1);
                }

                if (!main.getArenaManager().getArenas().containsKey(arenaName)) return;

                Arena arena = Main.getPlugin().getArenaManager().getArenas().get(arenaName);
                if (arena == null) return;

                if (arena.checkUsable()) {
                    Bukkit.getPluginManager().callEvent(new JoinEvent(p, arena, Enums.JoinCause.COMMAND));
                }

            } else if (cmd.startsWith("join2:")) {
                String arenaName = cmd.substring(6);
                if (arenaName.startsWith(" ")) {
                    arenaName = arenaName.substring(1);
                }

                if (!main.getArenaManager().getArenas().containsKey(arenaName)) return;

                Arena arena = Main.getPlugin().getArenaManager().getArenas().get(arenaName);
                if (arena == null) return;

                if (arena.checkUsable()) {
                    Bukkit.getPluginManager().callEvent(new JoinEvent(p, arena, Enums.JoinCause.COMMAND));
                }

            } else if (cmd.startsWith("lang:")) {
                String action = cmd.substring(5);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                if (!main.getSettings().getStringList("ListLanguage").contains(action)) {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Language.error"));
                    return;
                }

                gamePlayer.setLang(action);

                Bukkit.getScheduler().runTaskLater(main, () -> gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Language.change")),2);

            } else if (cmd.startsWith("chest:")) {
                String vote = cmd.substring(6);
                if (vote.startsWith(" ")) {
                    vote = vote.substring(1);
                }

                vote = vote.replace("%", "");

                if (gamePlayer.isInArena()) {
                    Arena arena = gamePlayer.getArena();

                    if (!arena.getVotesSystem().getCustomChests().contains(p.getUniqueId())) {
                        arena.getVotesSystem().addCustomChests(p, vote);

                        for (GamePlayer game : arena.getPlayers()) {
                            Configuration lang = game.getLangMessage();

                            game.sendMessage(lang.getString("Messages.TypeVote.VoteFor")
                                    .replace("%player%", p.getName())
                                    .replace("%vote%", vote)
                                    .replace("%votes%", String.valueOf(arena.getVotesSystem().getCustomChests().size()))
                            );
                        }
                    }
                }
            } else if (cmd.startsWith("vote:")) {
                String vote = cmd.substring(5);
                if (vote.startsWith(" ")) {
                    vote = vote.substring(1);
                }

                vote = vote.toUpperCase();

                if (gamePlayer.isInArena()) {
                    Arena arena = gamePlayer.getArena();

                    if (!arena.getVotesSystem().containsVote(p, vote.replace("%", ""))) {
                        arena.getVotesSystem().addVote(p, Enums.TypeVotes.valueOf(vote.replace("%", "")));

                        for (GamePlayer game : arena.getPlayers()) {
                            Configuration lang = game.getLangMessage();

                            game.sendMessage(lang.getString("Messages.TypeVote.VoteFor")

                                    .replace("%player%", p.getName())
                                    .replace("%vote%", vote)

                                    .replace("%BASIC%", lang.getString("Messages.TypeVote.Chest.basic", "basic"))
                                    .replace("%NORMAL%", lang.getString("Messages.TypeVote.Chest.normal", "normal"))
                                    .replace("%OP%", lang.getString("Messages.TypeVote.Chest.op", "op"))

                                    .replace("%DAY%", lang.getString("Messages.TypeVote.Time.day", "day"))
                                    .replace("%SUNSET%", lang.getString("Messages.TypeVote.Time.sunset", "sunset"))
                                    .replace("%NIGHT%", lang.getString("Messages.TypeVote.Time.night", "night"))

                                    .replace("%HEART10%", lang.getString("Messages.TypeVote.Heart.10h", "10 hearts"))
                                    .replace("%HEART20%", lang.getString("Messages.TypeVote.Heart.20h", "20 hearts"))
                                    .replace("%HEART30%", lang.getString("Messages.TypeVote.Heart.30h", "30 hearts"))

                                    .replace("%BORDER%", lang.getString("Messages.TypeVote.Events.border", "border"))
                                    .replace("%DRAGON%", lang.getString("Messages.TypeVote.Events.dragon", "dragon"))
                                    .replace("%DROPPARTY%", lang.getString("Messages.TypeVote.Events.dropparty", "dropparty"))
                                    .replace("%STORM%", lang.getString("Messages.TypeVote.Events.storm", "storm"))
                                    .replace("%TNTFALL%", lang.getString("Messages.TypeVote.Events.tntfall", "tntfall"))

                                    .replace("%NOCLEAN%", lang.getString("Messages.TypeVote.Scenario.noclean", "noclean"))
                                    .replace("%NOFALL%", lang.getString("Messages.TypeVote.Scenario.nofall", "nofall"))
                                    .replace("%NOPROJECTILE%", lang.getString("Messages.TypeVote.Scenario.noprojectile", "noprojectile"))

                                    .replace("%votes%", String.valueOf(arena.getVotesSystem().getVotes(vote.replace("%", ""))))
                                    .replace("%NONE%", lang.getString("Messages.TypeVote.none"))
                            );

                        }
                    } else {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TypeVote.AlreadyVote"));
                    }
                }
            } else if (cmd.startsWith("leave")) {
                if (gamePlayer.isInArena()) {
                    Arena arena = gamePlayer.getArena();
                    LeftEvent leftEvent = new LeftEvent(p, arena, Enums.LeftCause.INTERACT);
                    Bukkit.getPluginManager().callEvent(leftEvent);
                }
            } else if (cmd.startsWith("close")) {
                p.closeInventory();
            }
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
