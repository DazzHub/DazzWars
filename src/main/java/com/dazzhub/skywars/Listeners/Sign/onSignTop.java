package com.dazzhub.skywars.Listeners.Sign;

import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class onSignTop implements Listener {

    private Main main;

    public onSignTop(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onChangeSign(SignChangeEvent event) {
        String[] lines = event.getLines();
        if (lines[0].equalsIgnoreCase("[top]")) {
            Player player = event.getPlayer();

            Block block = event.getBlock();
            Sign sign = (Sign) block.getState();

            if (!player.hasPermission("skywars.admin")) {
                player.sendMessage("§cYou don't have permission to do that!");
                block.breakNaturally();
                return;
            }

            if (lines[3].equals("")) {
                player.sendMessage("§eUsage: \n §7line 1 = [top] \n §7line 2 = rank \n §7line 3 = %top% \n  §7line 4 = mod");
                block.breakNaturally();
                return;
            }

            Bukkit.getScheduler().runTaskLater(main, () -> main.getTopManager().createSigns(player, sign, lines, true),5);
        }
    }

}
