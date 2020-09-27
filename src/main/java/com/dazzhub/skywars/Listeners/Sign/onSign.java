package com.dazzhub.skywars.Listeners.Sign;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.JoinEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Enums;
import com.cryptomorin.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class onSign implements Listener {

    private Main main;

    public onSign(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onChangeSign(SignChangeEvent event) {
        String[] lines = event.getLines();
        if (lines[0].equalsIgnoreCase("[sw]")) {
            Player player = event.getPlayer();

            Block block = event.getBlock();
            Sign sign = (Sign) block.getState();

            if (!player.hasPermission("skywars.admin")) {
                player.sendMessage("§cYou don't have permission to do that!");
                block.breakNaturally();
                return;
            }

            if (lines[1].equals("")) {
                player.sendMessage("§eUsage: \n §7line 1 = [sw] \n §7line 2 = name of the arena");
                block.breakNaturally();
                return;
            }

            Arena arena = main.getArenaManager().getArenas().get(lines[1]);
            if (arena == null) {
                player.sendMessage("§cThe arena " + lines[1] + " does not exist!");
                block.breakNaturally();
                return;
            }

            main.getiSignManager().createSigns(player, sign, arena);
        }
    }

    @EventHandler
    public void onBreakSign(BlockBreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getBlock().getState();

        if (!(state instanceof Sign)) {
            return;
        }

        Arena arena = main.getiSignManager().getSigns().get(state.getLocation());
        if (arena == null) return;

        if (!player.hasPermission("skywars.admin")) {
            player.sendMessage(c("&c&l\u2718 &fYou don't have permission to do that!"));
            XSound.play(player, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
            return;
        }

        main.getiSignManager().removeSign(player, arena, state.getLocation());
    }

    @EventHandler
    public void onInteractName(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) && e.hasBlock() && e.getClickedBlock().getState() instanceof Sign) {
            BlockState state = e.getClickedBlock().getState();

            if (!(state instanceof Sign)) {
                return;
            }

            Arena arena = main.getiSignManager().getSigns().get(state.getLocation());
            if (arena == null) return;

            Bukkit.getPluginManager().callEvent(new JoinEvent(e.getPlayer(), arena, Enums.JoinCause.SIGN));
        }

    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
