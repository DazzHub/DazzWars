package com.dazzhub.skywars.Utils.effects.wins;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import com.dazzhub.skywars.Utils.effects.getTypeWins;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class fallingblocks implements getTypeWins {

    private Arena arena;
    private GamePlayer gamePlayer;

    public fallingblocks(GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.arena = gamePlayer.getArena();
    }

    @Override
    public void playWinEffect() {
        Player p = gamePlayer.getPlayer();

        new BukkitRunnable(){
            int timer = arena.getFinishedGame()*2;
            @Override
            public void run() {
                playEffects(p);playEffects(p);playEffects(p);

                if (timer == 0){
                    this.cancel();
                }
                timer--;
            }
        }.runTaskTimer(Main.getPlugin(), 0, 10);
    }

    public static void playEffects(Player p) {
        Random r = new Random();

        if (!Main.getPlugin().checkVersion()){
            Console.warning("Win effect FallingBlocks only works for 1.8");
            return;
        }

        FallingBlock localFallingBlock = p.getWorld().spawnFallingBlock(p.getEyeLocation(), Material.WOOL, (byte) randomNumI(0, 15));
        localFallingBlock.setDropItem(false);
        localFallingBlock.setVelocity(new Vector(randomNum(0.5, -0.5), r.nextDouble() + 0.5, randomNum(0.5, -0.5)));
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), localFallingBlock::remove, 10);
    }

    public static double randomNum(double r1, double r2) {
        return r1 + (r2 - r1) * new Random().nextDouble();
    }

    public static double randomNumI(int r1, int r2) {
        return r1 + (r2 - r1) * new Random().nextDouble();
    }

}
