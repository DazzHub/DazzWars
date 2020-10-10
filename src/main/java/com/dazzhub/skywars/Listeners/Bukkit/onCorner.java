package com.dazzhub.skywars.Listeners.Bukkit;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.cryptomorin.xseries.XSound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class onCorner implements Listener {

    private Main main;

    public onCorner(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerAddSpawnPoints(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if (main.getItemsCustom().getAddSpawn() == null || main.getItemsCustom().getSetSpectator() == null){
            return;
        }

        ItemMeta meta1 = main.getItemsCustom().getAddSpawn().getItemMeta();
        ItemMeta meta2 = main.getItemsCustom().getSetSpectator().getItemMeta();

        if (meta1 == null || meta2 == null) return;

        String[] arena1 = meta1.getDisplayName().split(":");
        String[] arena2 = meta2.getDisplayName().split(":");

        String namearena = ChatColor.stripColor(arena1[1]).substring(1);
        String namearena2 = ChatColor.stripColor(arena2[1]).substring(1);

        if (compareItem(p.getItemInHand(), main.getItemsCustom().getAddSpawn())) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Main.getPlugin().getArenaManager().addSpawn(p, e.getClickedBlock().getLocation(), namearena, true);
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Main.getPlugin().getArenaManager().removeSpawn(p, namearena);
            }
            e.setCancelled(true);
        }

        if (compareItem(p.getItemInHand(), main.getItemsCustom().getAddChestCenter())) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Main.getPlugin().getArenaManager().addChestCenter(p, e.getClickedBlock().getLocation(), namearena);
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Main.getPlugin().getArenaManager().removeChestCenter(p, e.getClickedBlock().getLocation(), namearena);
            }
            e.setCancelled(true);
        }

        if (compareItem(p.getItemInHand(), main.getItemsCustom().getSetSpectator())) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)){
                Main.getPlugin().getArenaManager().setSpectator(p, e.getClickedBlock().getLocation(), namearena2, true);
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                Main.getPlugin().getArenaManager().removeSpectator(p, namearena2);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerWandCage(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (compareItem(player.getItemInHand(), main.getItemsCustom().getCageWand())) {
            e.setCancelled(true);
            int n = e.getAction().equals(Action.LEFT_CLICK_BLOCK) ? 0 : (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) ? 1 : 2);

            if (n == 2) {
                return;
            }

            Location location = e.getClickedBlock().getLocation();
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(player.getUniqueId());

            if (n == 0){
                gamePlayer.setCage1(location);
            }

            if (n == 1){
                gamePlayer.setCage2(location);
            }

            XSound.play(player, String.valueOf(XSound.BLOCK_LAVA_POP.parseSound()));
            player.sendMessage(c("&a&l\u2714 &fYou have set the &9#" + (n + 1)));
        }
    }

    public boolean compareItem(ItemStack itemStack, ItemStack itemStack2) { return itemStack != null && itemStack2 != null && itemStack.getType().equals(itemStack2.getType()) && itemStack.getItemMeta().equals(itemStack2.getItemMeta()); }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
