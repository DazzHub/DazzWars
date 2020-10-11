package com.dazzhub.skywars.Listeners.Lobby;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.locUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class onSoulWell implements Listener {

    private final Main main;
    private final Random r;

    public static Boolean using;

    public onSoulWell(Main main) {
        this.main = main;
        this.r = new Random();

        using = false;
    }

    @EventHandler
    public void soulWellSet(PlayerInteractEvent e) throws IOException {
        Player player = e.getPlayer();

        if (compareItem(player.getItemInHand(), main.getItemsCustom().getSoulWand()) && player.hasPermission("skywars.admin") && e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            e.setCancelled(true);
            Location location = e.getClickedBlock().getLocation();

            File file = this.main.getConfigUtils().getFile(this.main, "SoulWell");
            FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "SoulWell");

            if (!Main.getPlugin().getSoulManager().getLocations().contains(location)) {

                if (config.getStringList("Locations") == null) {
                    config.createSection("Locations");
                    config.save(file);
                }

                config.set("Locations." + UUID.randomUUID(), locUtils.locToString(location));
                config.save(file);

                Main.getPlugin().getSoulManager().getLocations().add(location);

                XSound.play(player, String.valueOf(XSound.BLOCK_LAVA_POP.parseSound()));
                player.sendMessage(c("&a&l\u2714 &fYou have set a SoulWell."));
            } else {
                player.sendMessage(c("&c&l\u2718 &fA SoulWell already exists at that location."));
            }
        }
    }



    @EventHandler
    public void onBreak(BlockBreakEvent event) throws IOException {
        Location location = event.getBlock().getLocation();
        if (main.getSoulManager().getLocations().contains(location)) {
            File file = this.main.getConfigUtils().getFile(this.main, "SoulWell");
            FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "SoulWell");
            config.getStringList("Locations").remove(locUtils.locToString(location));
            config.save(file);
        }
    }

    @EventHandler
    public void onPlayerInteractSoul(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());
        if (gamePlayer == null) return;

        /* Fixear */
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (main.getSoulManager().getLocations().contains(e.getClickedBlock().getLocation())) {
                if (!using) {
                    e.setCancelled(true);
                    if (p.hasPermission("skywars.soulwell")) {

                            if (gamePlayer.getSouls() > 0) {
                                int needc = gamePlayer.getLangMessage().getInt("Messages.SoulWell.SoulsToOpen");

                                if (!(gamePlayer.getSouls() >= needc)) {
                                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.SoulWell.InsufficientSouls", "Error SoulWell.InsufficientSouls").replace("%soul%", String.valueOf(needc)));
                                    using = false;
                                } else {
                                    gamePlayer.setSouls(gamePlayer.getSouls() - 1);
                                    preGive(gamePlayer);
                                }

                            } else {
                                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.SoulWell.NeedSouls", "Error SoulWell.NeedSouls"));
                            }

                    } else {
                        gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.SoulWell.NoPermission", "Error SoulWell.NoPermission"));
                    }
                } else {
                    gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.SoulWell.Using", "Error SoulWell.Using"));
                }
            }
        }
    }

    public void preGive(GamePlayer gamePlayer) {
        Configuration config = main.getConfigUtils().getConfig(main, "SoulWell");
        List<String> typesBoxes = new ArrayList<>(config.getStringList("TypesSoul"));
        String randomBox = typesBoxes.get(r.nextInt(typesBoxes.size()));

        ConfigurationSection section = config.getConfigurationSection("Soul." + randomBox.split(":")[0]);
        List<Integer> chances = section.getKeys(false).stream().map(Integer::valueOf).collect(Collectors.toList());

        int c = chances.get(r.nextInt(chances.size()));

        int boxesChance = Integer.parseInt(randomBox.split(":")[1]);

        int c2 = config.getInt("Soul." + randomBox.split(":")[0] + "." + c + ".CHANCE");

        if (r.nextInt(100) + 1 > c2 || r.nextInt(100) + 1 > boxesChance) {
            preGive(gamePlayer);
            return;
        }

        if (r.nextInt(100) + 1 <= boxesChance && r.nextInt(100) + 1 <= c2) {
            main.getSoulManager().preOpen(gamePlayer, randomBox.split(":")[0], c2);
        } else {
            preGive(gamePlayer);
        }
    }

    private boolean compareItem(ItemStack itemStack, ItemStack itemStack2) {
        return itemStack != null && itemStack2 != null && itemStack.getType().equals(itemStack2.getType()) && itemStack.getItemMeta().equals(itemStack2.getItemMeta());
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}


