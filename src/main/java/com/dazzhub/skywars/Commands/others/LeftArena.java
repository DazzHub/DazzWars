package com.dazzhub.skywars.Commands.others;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Listeners.Custom.LeftEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeftArena implements CommandExecutor {

    private Main main;

    public LeftArena(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("leave") || cmd.getName().equalsIgnoreCase("quit") || cmd.getName().equalsIgnoreCase("salir") && sender instanceof Player) {
            Player p = (Player) sender;

            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            if (gamePlayer == null){
                return false;
            }

            Arena arena = gamePlayer.getArena();

            if (arena != null) {
                LeftEvent leftEvent = new LeftEvent(p, arena, Enums.LeftCause.COMMAND);
                Bukkit.getPluginManager().callEvent(leftEvent);
            }
        }
        return false;
    }
}
