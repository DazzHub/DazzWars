package com.dazzhub.skywars.Utils.inventory.Item;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.comparables.comparator;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.inventory.actions.OptionClickEvent;
import com.dazzhub.skywars.Utils.inventory.actions.OptionClickEventHandler;
import com.dazzhub.skywars.Utils.inventory.menu.IMenu;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class IItem {

    private Main main;

    private HashMap<Integer, ordItems> itemsList;
    private OptionClickEventHandler handler;

    public IItem(HashMap<Integer, ordItems> items) {

        this.main = Main.getPlugin();
        this.itemsList = items;

        this.handler = (event -> {
            Player player = event.getPlayer();
            String target = event.getTarget();
            String cmd = event.getCmd();

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
                    parseCommand(player, target, sub);
                }
            } else {
                parseCommand(player, target, cmd);
            }

        });
    }

    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent, PlayerInteractAtEntityEvent playerInteractAtEntityEvent, EntityDamageByEntityEvent entityDamageByEntityEvent, Player target) {
        if (playerInteractEvent != null) {
            Player p = playerInteractEvent.getPlayer();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
            int slot = p.getInventory().getHeldItemSlot();

            ordItems ordItems = itemsList.get(slot);
            if (ordItems == null) return;
            if (slot == ordItems.getSlot()) {
                if (compareItem(playerInteractEvent.getItem(), ordItems.getIcon().build(p))) return;
                OptionClickEvent e = new OptionClickEvent(p, null, ordItems.getIcon(), ordItems.getSlot(), ordItems.getCommand(), ordItems.getPermission(), ordItems.getInteract(), ordItems.getPrice());
                if (hasPerm(p, e)) {
                    if (e.getInteract().equalsIgnoreCase("Clicks")) {
                        this.handler.onOptionClick(e);
                    }
                } else {
                    gamePlayer.sendMessage(c(gamePlayer.getLangMessage().getString("Messages.item-deny")));
                }
            }
        } else if (playerInteractAtEntityEvent != null && target != null) {
            Player p = playerInteractAtEntityEvent.getPlayer();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
            int slot = p.getInventory().getHeldItemSlot();

            ordItems ordItems = itemsList.get(slot);
            if (ordItems == null) return;

            if (slot == ordItems.getSlot()) {
                if (compareItem(p.getItemInHand(), ordItems.getIcon().build(p))) return;
                OptionClickEvent e = new OptionClickEvent(p, target.getName(), ordItems.getIcon(), ordItems.getSlot(), ordItems.getCommand(), ordItems.getPermission(), ordItems.getInteract(), ordItems.getPrice());
                if (hasPerm(p, e)) {
                    if (e.getInteract().equalsIgnoreCase("AtEntity")) {
                        this.handler.onOptionClick(e);
                    }
                } else {
                    gamePlayer.sendMessage(c(gamePlayer.getLangMessage().getString("Messages.item-deny")));
                }
            }
        } else if (entityDamageByEntityEvent != null && target != null) {
            Player p = (Player) entityDamageByEntityEvent.getDamager();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
            int slot = p.getInventory().getHeldItemSlot();

            ordItems ordItems = itemsList.get(slot);
            if (ordItems == null) return;

            if (slot == ordItems.getSlot()) {
                if (compareItem(p.getItemInHand(), ordItems.getIcon().build(p))) return;
                OptionClickEvent e = new OptionClickEvent(p, target.getName(), ordItems.getIcon(), ordItems.getSlot(), ordItems.getCommand(), ordItems.getPermission(), ordItems.getInteract(), ordItems.getPrice());
                if (hasPerm(p, e)) {
                    if (e.getInteract().equalsIgnoreCase("ByDamage")) {
                        this.handler.onOptionClick(e);
                    }
                } else {
                    gamePlayer.sendMessage(c(gamePlayer.getLangMessage().getString("Messages.item-deny")));
                }
            }
        }
    }

    public boolean hasPerm(Player player, OptionClickEvent e) {
        return e.getPermission() == null || e.getPermission().length() == 0 || player.hasPermission(e.getPermission());
    }

    private void parseCommand(Player p, String target, String cmd) {
        if (cmd != null && !cmd.equals("")) {

            if (cmd.contains("%player%")) {
                cmd = cmd.replace("%player%", p.getName());
            }

            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
            Arena arenaPlayer = gamePlayer.getArena();

            if (cmd.startsWith("console:")) {
                String consoleCommand = cmd.substring(8);
                if (consoleCommand.startsWith(" ")) {
                    consoleCommand = consoleCommand.substring(1);
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
            } else if (cmd.startsWith("open:")) {
                String openCommand = cmd.substring(5);
                if (openCommand.startsWith(" ")) {
                    openCommand = openCommand.substring(1);
                }
                IMenu menu = main.getMenuManager().getMenuFileName().get(openCommand);
                if (menu == null) return;

                menu.open(p, target);
            } else if (cmd.startsWith("leavespect")) {
                if (gamePlayer.isInArena())
                    Bukkit.getPluginManager().callEvent(new LeftEvent(p, arenaPlayer, Enums.LeftCause.INTERACTSPECTATOR));
            } else if (cmd.startsWith("leave")) {
                if (gamePlayer.isInArena())
                    Bukkit.getPluginManager().callEvent(new LeftEvent(p, arenaPlayer, Enums.LeftCause.INTERACT));
            } else if (cmd.startsWith("arenas")) {
                main.getArenasMenu().open(p);
            } else if (cmd.startsWith("spectate")) {
                if (gamePlayer.isInArena()) arenaPlayer.getSpectatorMenu().menuPlayers(p);
            } else if (cmd.startsWith("autojoin")) {
                if (!gamePlayer.isInArena()) {
                    comparator.checkArenaPlayer(Main.getPlugin().getArenaManager().getArenaList());
                    Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream().filter(Arena::checkUsable).findAny().orElse(null);

                    if (arenaTo == null) {
                        return;
                    }

                    Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));

                } else {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.alredyInGame"));
                }
            } else if (cmd.startsWith("auto")) {
                if (gamePlayer.isInArena()) {
                    Bukkit.getPluginManager().callEvent(new LeftEvent(p, arenaPlayer, Enums.LeftCause.SPECTATOR));

                    comparator.checkArenaPlayer(Main.getPlugin().getArenaManager().getArenaList());
                    Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream()
                            .filter(arena -> !arena.getPlayers().contains(gamePlayer) && !gamePlayer.isSpectating() && arena.checkUsable())
                            .findAny().orElse(null);

                    if (arenaTo == null) {
                        Bukkit.getPluginManager().callEvent(new LeftEvent(p, arenaPlayer, Enums.LeftCause.DISCONNECTSPECTATOR));
                        return;
                    }

                    Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.COMMAND));

                }
            } else {
                p.chat(cmd);
            }
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

    public boolean compareItem(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack == null || itemStack2 == null || !itemStack.getType().equals(itemStack2.getType()) || !itemStack.getItemMeta().equals(itemStack2.getItemMeta());
    }

    public void createItem(Player p) {
        itemsList.values().forEach(item -> p.getInventory().setItem(item.getSlot(), item.getIcon().build(p)));
    }
}
