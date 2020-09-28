package com.dazzhub.skywars.Utils.signs.top;

import com.cryptomorin.xseries.XSound;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.signs.ISignLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class ITopManager {

    private Main main;
    private HashMap<Location, ITop> tops;
    private int task;

    public ITopManager(Main main){
        this.main = main;
        this.tops = new HashMap<>();
    }

    public void loadSign() {
        Configuration signs = main.getSigns();

        if (!signs.getString("Top", "").isEmpty()) {

            signs.getConfigurationSection("Top").getKeys(false).forEach(key ->{

                String[] type = signs.getString("Top." + key).split(";");

                Location location = ISignLocation.getLocation(type[0]);

                String[] lines = Stream.of(type[1].split(",")).toArray(String[]::new);

                Block block = location.getWorld().getBlockAt(location);
                Sign sign = (Sign) block.getState();

                createSigns(null, sign, lines, false);
            });
        }

        updateTop();
    }

    public void createSigns(Player p, Sign sign, String[] lines, boolean save) {
        String[] owner = null;

        if (lines[3].equalsIgnoreCase("solo")){
            if (lines[2].equalsIgnoreCase("%kills%")){
                owner = main.getPlayerDB().TopKillsSolo(10);
            } else if (lines[2].equalsIgnoreCase("%deaths%")){
                owner = main.getPlayerDB().TopDeathsSolo(10);
            } else if (lines[2].equalsIgnoreCase("%wins%")){
                owner = main.getPlayerDB().TopDeathsSolo(10);
            }
        } else if (lines[3].equalsIgnoreCase("team")){
            if (lines[2].equalsIgnoreCase("%kills%")){
                owner = main.getPlayerDB().TopKillsTeam(10);
            } else if (lines[2].equalsIgnoreCase("%deaths%")){
                owner = main.getPlayerDB().TopDeathsTeam(10);
            } else if (lines[2].equalsIgnoreCase("%wins%")){
                owner = main.getPlayerDB().TopDeathsTeam(10);
            }
        }

        if (owner == null) {
            Console.warning("Create sign top error in lines!");
            return;
        }

        if (Integer.parseInt(lines[1]) == 0){
            Console.warning("Create sign top error in lines!");
            return;
        }

        if (tops.containsKey(sign.getLocation())) {
            if (p != null) {
                p.sendMessage(c("&a&l\u2714 &fThe top sign already exist in location!"));
                XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
                return;
            }
        }

        File file = this.main.getConfigUtils().getFile(this.main, "Signs");
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Signs");
        Location location = ISignLocation.getLocation(ISignLocation.getString(sign.getLocation(), false));

        int total = Integer.parseInt(lines[1]) - 1;
        ITop top = new ITop(sign, owner[total], Arrays.asList(lines));
        top.updateSign();

        if (save){
            int amount;
            try {
                amount = config.getConfigurationSection("Top").getKeys(false).size() + 1;
            }
            catch (Exception ex) {
                amount = 1;
            }

            config.set("Top."+amount, ISignLocation.getString(sign.getLocation(), false) + ";" + Arrays.toString(lines).replace("[", "").replace("]","").replace(" ", ""));

            try {
                config.save(file);
            } catch (IOException e) {
                Console.error(e.getMessage());
            }
        }

        tops.put(location, top);

        if (p != null) {
            p.sendMessage(c("&a&l\u2714 &fThe top sign created successfully!"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_YES.parseSound()));
        }
    }

    private void updateTop(){
        this.task = new BukkitRunnable() {
            int timer = main.getSettings().getInt("TopUpdate");

            @Override
            public void run() {

                if (timer <= 1) {
                    for (ITop iTop : tops.values()) {
                        iTop.updateSign();
                    }
                    timer = main.getSettings().getInt("TopUpdate");
                }

                timer--;
            }
        }.runTaskTimerAsynchronously(main, 0,20).getTaskId();
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

    public HashMap<Location, ITop> getTops() {
        return tops;
    }
}
