package com.dazzhub.skywars.Utils.inventory.Item;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.comparables.comparator;
import com.dazzhub.skywars.Listeners.Custom.ClickMenu;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Party.Party;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.inventory.actions.OptionClickEvent;
import com.dazzhub.skywars.Utils.inventory.actions.OptionClickEventHandler;
import com.dazzhub.skywars.Utils.inventory.menu.IMenu;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;

public class IItem implements Listener {

    private final Main main;

    private final HashMap<Integer, ordItems> itemsList;

    public IItem(HashMap<Integer, ordItems> items) {
        this.main = Main.getPlugin();
        this.itemsList = items;

        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent != null) {
            Player p = playerInteractEvent.getPlayer();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
            int slot = p.getInventory().getHeldItemSlot();

            ordItems ordItems = itemsList.get(slot);
            if (ordItems == null) return;

            if (slot == ordItems.getSlot()) {
                if (compareItem(playerInteractEvent.getItem(), ordItems.getIcon().build(p))) return;

                if (hasPerm(p, ordItems.getPermission())) {
                    callCommands(p, ordItems);
                } else {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.item-deny"));
                }

            }
        }
    }

    public void callCommands(Player player, ordItems ordItems) {
        String cmd = ordItems.getCommand();

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
                parseCommand(player, sub);
            }
        } else {
            parseCommand(player, cmd);
        }
    }

    public boolean hasPerm(Player player, String perm) {
        return perm == null || perm.length() == 0 || player.hasPermission(perm);
    }

    private void parseCommand(Player p, String cmd) {
        if (cmd != null && !cmd.equals("")) {

            if (cmd.contains("%player%")) {
                cmd = cmd.replace("%player%", p.getName());
            }

            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
            if (gamePlayer == null) return;
            Party party = gamePlayer.getParty();

            Arena arenaPlayer = gamePlayer.getArena();

            if (cmd.startsWith("console:")) {
                String consoleCommand = cmd.substring(8);
                if (consoleCommand.startsWith(" ")) {
                    consoleCommand = consoleCommand.substring(1);
                }
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), consoleCommand);
            } else if (cmd.startsWith("leavespect")) {
                if (gamePlayer.isInArena())
                    Bukkit.getPluginManager().callEvent(new LeftEvent(p, arenaPlayer, Enums.LeftCause.INTERACTSPECTATOR));
            } else if (cmd.startsWith("leave")) {
                if (gamePlayer.isInArena())
                    Bukkit.getPluginManager().callEvent(new LeftEvent(p, arenaPlayer, Enums.LeftCause.INTERACT));
            } else if (cmd.startsWith("spectate")) {
                if (gamePlayer.isInArena()) arenaPlayer.getSpectatorMenu().menuPlayers(p);
            } else if (cmd.startsWith("autojoin")) {
                if (!gamePlayer.isInArena()) {
                    if (party != null) {
                        if (!party.getOwner().equals(gamePlayer)) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.JoinArenaNoOwner"));
                            return;
                        }
                    }

                    comparator.checkArenaPlayer(Main.getPlugin().getArenaManager().getArenaList());
                    Arena arenaTo = Main.getPlugin().getArenaManager().getArenaList().stream().filter(Arena::checkUsable).findAny().orElse(null);

                    if (arenaTo == null) {
                        return;
                    }

                    Bukkit.getPluginManager().callEvent(new JoinEvent(p, arenaTo, Enums.JoinCause.INTERACT));

                }
            } else if (cmd.startsWith("open:")) {
                String openCommand = cmd.substring(5);
                if (openCommand.startsWith(" ")) {
                    openCommand = openCommand.substring(1);
                }

                IMenu menu = this.main.getMenuManager().getMenuLangs().get(gamePlayer.getLang()).getMenuFileName().get(openCommand);
                if (menu == null) return;

                Bukkit.getScheduler().runTaskLater(main, () -> menu.open(p),2);

            } else if (cmd.startsWith("auto")) {
                if (gamePlayer.isInArena()) {

                    if (party != null) {
                        if (!party.getOwner().equals(gamePlayer)) {
                            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.JoinArenaNoOwner"));
                            return;
                        }
                    }

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
        Bukkit.getScheduler().runTaskLater(main, () -> {
            for (ordItems item : itemsList.values()) {
                p.getInventory().setItem(item.getSlot(), item.getIcon().build(p));
            }
        },2);
    }
}
