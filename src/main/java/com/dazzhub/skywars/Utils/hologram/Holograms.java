package com.dazzhub.skywars.Utils.hologram;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.locUtils;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class Holograms {

    private Main main;
    private GamePlayer gamePlayer;

    private HashMap<String, Hologram> listHolograms;

    public Holograms(Main main, GamePlayer gamePlayer) {
        this.main = main;
        this.gamePlayer = gamePlayer;
        this.listHolograms = new HashMap<>();
    }

    public void createHologram(String typeHologram) {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            return;
        }

        if (listHolograms.containsKey(typeHologram)) {
            listHolograms.get(typeHologram).delete();
            listHolograms.remove(typeHologram);
        }

        Location loc2 = locUtils.stringToLoc(main.getSettings().getString("Holograms." + typeHologram));

        if (loc2 == null) return;

        loc2.add(0, 3.5, 0);

        Hologram hologram = HologramsAPI.createHologram(main, loc2);

        List<String> list = gamePlayer.getHologramMessage().getStringList("TypeHolograms." + typeHologram);

        lines(list, gamePlayer.getPlayer(), hologram);

        VisibilityManager visibilityManager = hologram.getVisibilityManager();
        visibilityManager.showTo(gamePlayer.getPlayer());
        visibilityManager.setVisibleByDefault(false);

        listHolograms.put(typeHologram, hologram);

        gamePlayer.setHolograms(this);
    }

    public void reloadHologram() {

        if (gamePlayer == null) {
            return;
        }

        for (String typeHologram : listHolograms.keySet()) {
            createHologram(typeHologram);
        }
    }


    public void deleteHologram() {
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            return;
        }

        for (Hologram hologram : listHolograms.values()) {
            hologram.delete();
        }

        gamePlayer.setHolograms(null);
    }

    private void lines(List<String> lines, Player p, Hologram holo) {
        Configuration config = gamePlayer.getHologramMessage();

        if (config == null){
            return;
        }

        String transformed = "";

        for (String line : lines) {
            transformed = c(line);

            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                transformed = PlaceholderAPI.setPlaceholders(p, transformed);
            }

            if (!transformed.startsWith("%topkillssolo%")) {
                if (!transformed.startsWith("%topkillsteam%")) {
                    if (!transformed.startsWith("%topdeathssolo%")) {
                        if (!transformed.startsWith("%topdeathsteam%")) {
                            if (!transformed.startsWith("%topwinssolo%")) {
                                if (!transformed.startsWith("%topwinsteam%")) {
                                    holo.appendTextLine(transformed);
                                }
                            }
                        }
                    }
                }
            }

        }

        if (transformed.equalsIgnoreCase("%topkillssolo%")) {
            String[] top = main.getPlayerDB().TopKillsSolo();
            for (String topPlayers : top) {

                if (topPlayers.startsWith("error")){
                    holo.appendTextLine(c(config.getString("format.error")));
                    continue;
                }

                String[] TOPSpli = topPlayers.split(":");
                holo.appendTextLine(c(config.getString("format.top")).replace("{0}", TOPSpli[0]).replace("{1}", TOPSpli[1]));
            }
        } else if (transformed.equalsIgnoreCase("%topkillsteam%")) {
            String[] top = main.getPlayerDB().TopKillsTeam();
            for (String topPlayers : top) {

                if (topPlayers.startsWith("error")){
                    holo.appendTextLine(c(config.getString("format.error")));
                    continue;
                }

                String[] TOPSpli = topPlayers.split(":");
                holo.appendTextLine(c(config.getString("format.top")).replace("{0}", TOPSpli[0]).replace("{1}", TOPSpli[1]));
            }
        } else if (transformed.equalsIgnoreCase("%topdeathssolo%")) {
            String[] top = main.getPlayerDB().TopDeathsSolo();
            for (String topPlayers : top) {

                if (topPlayers.startsWith("error")){
                    holo.appendTextLine(c(config.getString("format.error")));
                    continue;
                }

                String[] TOPSpli = topPlayers.split(":");
                holo.appendTextLine(c(config.getString("format.top")).replace("{0}", TOPSpli[0]).replace("{1}", TOPSpli[1]));
            }
        } else if (transformed.equalsIgnoreCase("%topdeathsteam%")) {
            String[] top = main.getPlayerDB().TopDeathsTeam();
            for (String topPlayers : top) {

                if (topPlayers.startsWith("error")){
                    holo.appendTextLine(c(config.getString("format.error")));
                    continue;
                }

                String[] TOPSpli = topPlayers.split(":");
                holo.appendTextLine(c(config.getString("format.top")).replace("{0}", TOPSpli[0]).replace("{1}", TOPSpli[1]));
            }
        } else if (transformed.equalsIgnoreCase("%topwinssolo%")) {
            String[] top = main.getPlayerDB().TopWinsSolo();
            for (String topPlayers : top) {

                if (topPlayers.startsWith("error")){
                    holo.appendTextLine(c(config.getString("format.error")));
                    continue;
                }

                String[] TOPSpli = topPlayers.split(":");
                holo.appendTextLine(c(config.getString("format.top")).replace("{0}", TOPSpli[0]).replace("{1}", TOPSpli[1]));
            }
        } else if (transformed.equalsIgnoreCase("%topwinsteam%")) {
            String[] top = main.getPlayerDB().TopWinsTeam();
            for (String topPlayers : top) {

                if (topPlayers.startsWith("error")){
                    holo.appendTextLine(c(config.getString("format.error")));
                    continue;
                }

                String[] TOPSpli = topPlayers.split(":");
                holo.appendTextLine(c(config.getString("format.top")).replace("{0}", TOPSpli[0]).replace("{1}", TOPSpli[1]));
            }
        }
    }

    private String c(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
