package com.dazzhub.skywars.Utils.inventory.menu;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.inventory.actions.OptionClickEvent;
import com.dazzhub.skywars.Utils.inventory.actions.OptionClickEventHandler;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;
import java.util.HashMap;

@Getter
@Setter
public class IMenu {

    private Main main;
    private Inventory inv;

    private String name;
    private Integer rows;
    private String command;

    private HashMap<Integer, ordItems> itemsList;
    private OptionClickEventHandler handler;
    private BukkitScheduler scheduler;

    public IMenu(String name, Integer rows, String command, HashMap<Integer, ordItems> items) {
        this.main = Main.getPlugin();
        this.name = name;
        this.rows = rows;
        this.command = command;
        this.itemsList = items;

        this.scheduler = main.getServer().getScheduler();

        this.handler = (event -> {
            Player player = event.getPlayer();
            String target = event.getTarget();
            Integer price = event.getPrice();

            String cmd = event.getCmd();

            if (cmd == null || cmd.equals("")) {
                return;
            }
            scheduler.runTaskLater(main, () -> {
                if (cmd.contains(";")) {
                    String[] array = cmd.split(";");
                    String[] array2;
                    for (int length = (array2 = array).length, i = 0; i < length; ++i) {
                        String sub = array2[i];
                        if (sub.startsWith(" ")) {
                            sub = sub.substring(1);
                        }
                        parseCommand(player, target, sub, price);
                    }
                }
                else {
                    parseCommand(player, target, cmd, price);
                }
            }, 2L);
        });

    }

    public void open(Player p, String target){
        if (target == null){
            this.inv = Bukkit.createInventory(p, rows*9, c(name));
        } else {
            this.inv = Bukkit.createInventory(p, rows*9, c(name + "/" + target));
        }

        main.getPlayerManager().getPlayer(p.getUniqueId()).setTaskId(new BukkitRunnable() {
            @Override
            public void run() {
                itemsList.values().forEach(item -> inv.setItem(item.getSlot(), hideAttributes(item.getIcon().build(p))));
            }
        }.runTaskTimerAsynchronously(main, 0,10).getTaskId());

        p.openInventory(inv);
    }


    public void onInventoryClick(InventoryClickEvent event) {

        int slot = event.getRawSlot();
        Player p = (Player) event.getWhoClicked();

        if (p == null) {
            return;
        }

        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

        if (event.getView().getTitle().equalsIgnoreCase(inv.getTitle())) {
            ordItems ordItems = itemsList.get(slot);
            if (ordItems == null) return;

            if (slot == ordItems.getSlot()) {
                OptionClickEvent e = new OptionClickEvent(p, inv.getTitle().contains("/") ? inv.getTitle().split("/")[1] : null, ordItems.getIcon(), slot, ordItems.getCommand(), ordItems.getPermission(), ordItems.getInteract(), ordItems.getPrice());
                if (hasPerm(p, e)) {
                    this.handler.onOptionClick(e);
                } else {
                    gamePlayer.sendMessage(c(gamePlayer.getLangMessage().getString("Messages.menu-deny")));
                }
                Bukkit.getScheduler().runTaskLater(main, p::closeInventory, 1L);
            }
        }
    }

    public void closeInv(Player p){
        Bukkit.getScheduler().cancelTask(main.getPlayerManager().getPlayer(p.getUniqueId()).getTaskId());
    }

    public boolean hasPerm(Player player, OptionClickEvent e) {
        return e.getPermission() == null || e.getPermission().length() == 0 || player.hasPermission(e.getPermission());
    }

    public ItemStack hideAttributes(ItemStack item) {
        if (item == null) {
            return null;
        }

        ItemMeta meta = item.getItemMeta();
        if (isNullOrEmpty(meta.getItemFlags())) {
            meta.addItemFlags(ItemFlag.values());
            item.setItemMeta(meta);
        }
        return item;
    }

    private boolean isNullOrEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    private void parseCommand(Player p, String target, String cmd, Integer price) {
        if (cmd != null && !cmd.equals("")) {

            if (cmd.contains("%player%")) {
                cmd = cmd.replace("%player%", p.getName());
            }

            if (cmd.contains("%target%") && target != null) {
                cmd = cmd.replace("%target%", target);
            }

            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            if (price != 0 && gamePlayer.getCoins() < price) {
                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.InsufficientCoins").replace("%coins%", String.valueOf(price)));
                return;
            } else {
                gamePlayer.removeCoins(price);
            }

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

                IMenu menu = main.getMenuManager().getMenuFileName().get(openCommand);
                if (menu == null) return;

                menu.open(p, target);
            } else if (cmd.startsWith("cage:")) {
                String action = cmd.substring(5);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }

                String[] cage = action.split("/");

                if (cage[1].equalsIgnoreCase("SOLO")) {
                    if (main.getCageManager().getCagesSolo().containsKey(cage[0])) {

                        if (!gamePlayer.getCagesSoloList().contains(cage[0])) {

                            gamePlayer.getCagesSoloList().add(cage[0]);
                            gamePlayer.setCageSolo(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Buy").replace("%cage%", cage[0]));

                        } else {
                            gamePlayer.setCageSolo(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Selected").replace("%cage%", cage[0]));
                        }

                    }
                } else if (cage[1].equalsIgnoreCase("TEAM")) {
                    if (main.getCageManager().getCagesTeam().containsKey(cage[0])) {

                        if (!gamePlayer.getCagesTeamList().contains(cage[0])) {
                            gamePlayer.getCagesTeamList().add(cage[0]);
                            gamePlayer.setCageTeam(cage[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Cage.Buy").replace("%cage%", cage[0]));

                        } else {
                            gamePlayer.setCageTeam(cage[0]);
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
                    if (main.getiKitManager().getKitSoloHashMap().containsKey(kit[0].toLowerCase())) {

                        if (!gamePlayer.getKitSoloList().contains(kit[0])) {
                            gamePlayer.getKitSoloList().add(kit[0]);
                            gamePlayer.setKitSolo(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Buy").replace("%kit%", kit[0]));
                        } else {
                            gamePlayer.setKitSolo(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Selected").replace("%kit%", kit[0]));
                        }

                    }
                } else if (kit[1].equalsIgnoreCase("TEAM")) {
                    if (main.getiKitManager().getKitTeamHashMap().containsKey(kit[0].toLowerCase())) {

                        if (!gamePlayer.getKitTeamList().contains(kit[0])) {
                            gamePlayer.getKitTeamList().add(kit[0]);
                            gamePlayer.setKitTeam(kit[0]);
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Buy").replace("%kit%", kit[0]));
                        } else {
                            gamePlayer.setKitTeam(kit[0]);
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
                        gamePlayer.getWinEffectsSoloList().add(wineffect[0]);
                        gamePlayer.setWinEffectSolo(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Buy").replace("%win%", wineffect[0]));
                    } else {
                        gamePlayer.setWinEffectSolo(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Selected").replace("%win%", wineffect[0]));
                    }
                } else if (wineffect[1].equalsIgnoreCase("TEAM")) {
                    if (!gamePlayer.getWinEffectsTeamList().contains(wineffect[0])) {
                        gamePlayer.getWinEffectsTeamList().add(wineffect[0]);
                        gamePlayer.setWinEffectTeam(wineffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.WinEffect.Buy").replace("%win%", wineffect[0]));
                    } else {
                        gamePlayer.setWinEffectTeam(wineffect[0]);
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
                        gamePlayer.getKillEffectsSoloList().add(killeffect[0]);
                        gamePlayer.setKillEffectSolo(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Buy").replace("%kill%", killeffect[0]));
                    } else {
                        gamePlayer.setKillEffectSolo(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Selected").replace("%kill%", killeffect[0]));
                    }
                } else if (killeffect[1].equalsIgnoreCase("TEAM")) {
                    if (!gamePlayer.getKillEffectsTeamList().contains(killeffect[0])) {
                        gamePlayer.getKillEffectsTeamList().add(killeffect[0]);
                        gamePlayer.setKillEffectTeam(killeffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.KillEffect.Buy").replace("%kill%", killeffect[0]));
                    } else {
                        gamePlayer.setKillEffectTeam(killeffect[0]);
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
                        gamePlayer.getTrailsSoloList().add(traileffect[0]);
                        gamePlayer.setTrailSolo(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Buy").replace("%trail%", traileffect[0]));
                    } else {
                        gamePlayer.setTrailSolo(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Selected").replace("%trail%", traileffect[0]));
                    }
                } else if (traileffect[1].equalsIgnoreCase("TEAM")) {
                    if (!gamePlayer.getTrailsTeamList().contains(traileffect[0])) {
                        gamePlayer.getTrailsTeamList().add(traileffect[0]);
                        gamePlayer.setTrailTeam(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Buy").replace("%trail%", traileffect[0]));
                    } else {
                        gamePlayer.setTrailTeam(traileffect[0]);
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.TrailEffect.Selected").replace("%trail%", traileffect[0]));
                    }
                }
            } else if (cmd.startsWith("lang:")) {
                String action = cmd.substring(5);
                if (action.startsWith(" ")) {
                    action = action.substring(1);
                }
                Configuration lang = gamePlayer.getLangMessage();

                if (!main.getSettings().getStringList("ListLanguage").contains(action)) {
                    gamePlayer.sendMessage(lang.getString("Messages.Language.error"));
                    return;
                }

                gamePlayer.setLang(action);

                gamePlayer.sendMessage(lang.getString("Messages.Language.change"));
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
            }
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
