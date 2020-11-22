package com.dazzhub.skywars.Arena;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.locUtils;
import com.cryptomorin.xseries.XSound;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.cryptomorin.xseries.XSound.*;

public class ArenaManager {

    private final Main main;

    private final HashMap<String, Arena> arenas;
    private final List<Arena> arenaList;

    /* temp arenas */
    private final HashMap<String, Arena> temp_arenas;

    public ArenaManager(Main main) {
        this.main = main;

        this.arenas = new HashMap<>();
        this.arenaList = new ArrayList<>();

        this.temp_arenas = new HashMap<>();
    }

    public void loadArenas(){
        this.arenas.clear();
        this.arenaList.clear();

        List<String> arenaslist = main.getConfigUtils().getConfig(this.main, "Arenas/Arenas").getStringList("Arenas");

        arenaslist.forEach(nameArena -> {
            File file = main.getConfigUtils().getFile(this.main, "Arenas/" + nameArena + "/Settings");
            if (file.exists()) {
                if (!arenas.containsKey(nameArena)) {
                    Arena arena = new Arena(nameArena);

                    this.arenas.put(nameArena, arena);
                    this.arenaList.add(arena);
                }
            } else {
                Console.error("&eError in the arena: &4" + nameArena + " &ethe &4Settings.yml&e file is missing");
            }
        });

        this.arenas.forEach((s, arena) -> {
            //arena.clone();
        });

        Console.info("&eLoaded arenas: &a"+getArenas().size());
    }

    public void enableArena(String name){
        if (arenas.containsKey(name)) {
            Arena arena = arenas.get(name);

            for (GamePlayer gamePlayer : arena.getPlayers()) {
                arena.removePlayer(gamePlayer);
            }

            main.getArenaManager().getArenas().remove(arena.getNameArena());
            main.getArenaManager().getArenaList().remove(arena);

            Bukkit.getScheduler().runTaskLater(main, () -> {
                Arena arena2 = new Arena(name);

                this.arenas.put(name, arena2);
                this.arenaList.add(arena2);
            },5);
        }
    }

    public void createArena(Player p, String nameArena, String nameWorld) {

        if (getArenas().containsKey(nameArena)) {
            p.sendMessage(c("&c&l\u2718 &fThat arena already exists"));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
            return;
        }

        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + nameArena + "/Settings");
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + nameArena + "/Settings");
        File file2 = this.main.getConfigUtils().getFile(this.main, "Arenas/Arenas");
        FileConfiguration config2 = this.main.getConfigUtils().getConfig(this.main, "Arenas/Arenas");

        List<Integer> refilltimes = new ArrayList<>();
        refilltimes.add(180);
        refilltimes.add(160);
        refilltimes.add(60);

        config.set("Arena.name", nameArena);
        config.set("Arena.world", nameWorld);
        config.set("Arena.enabled", true);
        config.set("Arena.minPlayer", 2);
        config.set("Arena.maxPlayer", 12);
        config.set("Arena.spawns", "");
        config.set("Arena.spawnSpectator", "");
        config.set("Arena.centerChest", "");
        config.set("Arena.sign", "");
        config.set("Arena.durationGame", 900);
        config.set("Arena.startingGame", 15);
        config.set("Arena.finishedGame", 10);
        config.set("Arena.refill", refilltimes);


        /* EVENT BORDER */
        config.set("Arena.border.SpawnDefault", false);
        config.set("Arena.border.TimeSpawn", 120);
        config.set("Arena.border.Settings.Size", 100);
        config.set("Arena.border.Settings.Damage.inborder", 1);
        config.set("Arena.border.Settings.Damage.border", 1);

        /* EVENT DRAGON */
        config.set("Arena.dragon.TimeSpawn", 120);
        config.set("Arena.dragon.Name", "&bDRAGON");

        /* EVENT DROPPARTY */
        config.set("Arena.dropparty.TimeStarting", 120);
        config.set("Arena.dropparty.TimeDrop", 50);
        config.set("Arena.dropparty.Chest", "DROPPARTY");
        config.set("Arena.dropparty.AmountDrop", 1);
        config.set("Arena.dropparty.Radio", 35);

        /* EVENT STORM */
        config.set("Arena.storm.TimeSpawn", 120);
        config.set("Arena.storm.TimeDuration", 50);
        config.set("Arena.storm.AmountLight", 2);
        config.set("Arena.storm.Radio", 35);

        /* EVENT TNTFALL */
        config.set("Arena.tntfall.TimeSpawn", 120);
        config.set("Arena.tntfall.TimeDuration", 50);
        config.set("Arena.tntfall.AmountFallTnT", 2);
        config.set("Arena.tntfall.Radio", 35);

        config.set("Arena.mode", "SOLO");
        try {
            config.save(file);
        } catch (IOException e) {
            Console.error(e.getMessage());
        }

        try {
            List<String> list = this.main.getConfigUtils().getConfig(this.main, "Arenas/Arenas").getStringList("Arenas");
            list.add(nameArena);
            config2.set("Arenas", list);
            config2.save(file2);
        } catch (IOException e) {
            Console.error(e.getMessage());
        }


        switch (Enums.ResetArena.valueOf(main.getSettings().getString("ResetArena"))) {
            case RESETWORLD:
            case RESETCHUNK: {
                File map = new File(main.getServer().getWorldContainer(), nameWorld);
                File foldermaps = new File(main.getDataFolder(), "Arenas/" + nameArena + "/" + nameWorld);

                main.getResetWorld().copyDir(map, foldermaps);

                Bukkit.getScheduler().runTaskLater(main, () -> {
                    loadWorld(nameWorld);
                    p.teleport(Objects.requireNonNull(Bukkit.getWorld(nameWorld)).getSpawnLocation());
                }, 5);
                break;
            }

            case SLIMEWORLDMANAGER: {
                loadWorld(nameWorld);
                Bukkit.getScheduler().runTaskLater(main, () -> p.teleport(Bukkit.getWorld(nameWorld).getSpawnLocation()), 5);
                break;
            }
        }

        p.sendMessage(c("&a&l\u2714 &fArena &e" + nameArena + "&f created successfully"));
        XSound.play(p, String.valueOf(ENTITY_VILLAGER_YES.parseSound()));

        p.getInventory().clear();

        p.setGameMode(GameMode.CREATIVE);
        p.setAllowFlight(true);

        p.getInventory().addItem(main.getItemsCustom().addSpawn(nameArena));
        p.getInventory().addItem(main.getItemsCustom().addChestCenter(nameArena));
        p.getInventory().addItem(main.getItemsCustom().setSpectator(nameArena));
    }

    public void addSpawn(Player p, Location location, String name, boolean useitem) {
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
        if (file.exists()) {
            FileConfiguration arenaConfig = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");
            int spawn;
            try {
                spawn = arenaConfig.getConfigurationSection("Arena.spawns").getKeys(false).size() + 1;
            }
            catch (Exception ex) {
                spawn = 1;
            }
            if (useitem) {
                Location loc = new Location(location.getWorld(), location.getX(), location.getY() + 7, location.getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                arenaConfig.set("Arena.spawns." + spawn, locUtils.locToStringNoWorld(loc));
            } else {
                arenaConfig.set("Arena.spawns." + spawn, locUtils.locToStringNoWorld(p.getLocation()));
            }
            try {
                arenaConfig.save(file);
            }
            catch (IOException e) {
                Console.error(e.getMessage());
            }
            p.sendMessage(this.c("&a&l\u2714 &fSpawn has been added &9#" + spawn + "&f in the arena &9" + name + "&f."));
            XSound.play(p, String.valueOf(ENTITY_CHICKEN_EGG.parseSound()));
        }
        else {
            p.sendMessage(this.c("&c&l\u2718 &fThe arena does not exist."));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public void addChestCenter(Player p, Location location, String name) {
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
        if (file.exists()) {
            FileConfiguration arenaConfig = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");

            if (arenaConfig.getStringList("Arena.centerChest").contains(locUtils.locToStringNoWorld(location))){
                p.sendMessage(this.c("&c&l\u2718 &fThis location has already been added."));
                XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
                return;
            }

            List<String> locs = arenaConfig.getStringList("Arena.centerChest");
            locs.add(locUtils.locToStringNoWorld(location));

            arenaConfig.set("Arena.centerChest", locs);

            try {
                arenaConfig.save(file);
            } catch (IOException e) {
                Console.error(e.getMessage());
            }

            p.sendMessage(this.c("&a&l\u2714 &fChest has been added&f in the arena &9" + name + "&f."));
            XSound.play(p, String.valueOf(ENTITY_CHICKEN_EGG.parseSound()));
        } else {
            p.sendMessage(this.c("&c&l\u2718 &fThe arena does not exist."));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public void setSpectator(Player p, Location location, String name, boolean useitem) {
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
        if (file.exists()) {
            FileConfiguration arenaConfig = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");
            if (useitem) {
                Location loc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
                arenaConfig.set("Arena.spawnSpectator", locUtils.locToStringNoWorld(loc));
            } else {
                arenaConfig.set("Arena.spawnSpectator", locUtils.locToStringNoWorld(p.getLocation()));
            }
            try {
                arenaConfig.save(file);
            }
            catch (IOException e) {
                Console.error(e.getMessage());
            }
            p.sendMessage(this.c("&a&l\u2714 &fSpectator set for arena &9" + name + "&f."));
            XSound.play(p, String.valueOf(ENTITY_CHICKEN_EGG.parseSound()));
        }
        else {
            p.sendMessage(this.c("&c&l\u2718 &fThe arena does not exist."));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public void removeSpawn(Player p, String name) {
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
        if (file.exists()) {
            FileConfiguration arenaConfig = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");
            int spawn;
            try {
                spawn = arenaConfig.getConfigurationSection("Arena.spawns").getKeys(false).size();
            }
            catch (Exception ex) {
                spawn = 0;
            }
            arenaConfig.set("Arena.spawns." + spawn, null);
            try {
                arenaConfig.save(file);
            }
            catch (IOException e) {
                Console.error(e.getMessage());
            }
            p.sendMessage(this.c("&a&l\u2714 &fPrevious spawn has remove for arena &9" + name + " &f(&9#"+spawn+"&f)."));
            XSound.play(p, String.valueOf(BLOCK_STONE_BREAK.parseSound()));
        }
        else {
            p.sendMessage(this.c("&c&l\u2718 &fThe arena does not exist."));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public void removeChestCenter(Player p, Location location, String name) {
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
        if (file.exists()) {
            FileConfiguration arenaConfig = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");
            arenaConfig.set("Arena.centerChest." + locUtils.locToStringNoWorld(location), null);
            try {
                arenaConfig.save(file);
            }
            catch (IOException e) {
                Console.error(e.getMessage());
            }
            p.sendMessage(this.c("&a&l\u2714 &fPrevious chest has remove for arena &9" + name + " &f."));
            XSound.play(p, String.valueOf(BLOCK_STONE_BREAK.parseSound()));
        }
        else {
            p.sendMessage(this.c("&c&l\u2718 &fThe arena does not exist."));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public void removeSpectator(Player p, String name) {
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
        if (file.exists()) {
            FileConfiguration arenaConfig = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");
            arenaConfig.set("Arena.spawnSpectator", null);
            try {
                arenaConfig.save(file);
            }
            catch (IOException e) {
                Console.error(e.getMessage());
            }
            p.sendMessage(this.c("&a&l\u2714 &fSpectator spawn remove for arena &9" + name + "&f."));
            XSound.play(p, String.valueOf(BLOCK_GRASS_BREAK.parseSound()));
        }
        else {
            p.sendMessage(this.c("&c&l\u2718 &fThe arena does not exist."));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public void maxPlayer(Player p, int max, String name) {
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
        if (file.exists()) {
            FileConfiguration arenaConfig = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");
            arenaConfig.set("Arena.maxPlayer", max);
            try {
                arenaConfig.save(file);
            }
            catch (IOException e) {
                Console.error(e.getMessage());
            }
            p.sendMessage(this.c("&a&l\u2714 &fYou have set the max players in the arena &9" + name + "&f."));
            XSound.play(p, String.valueOf(ENTITY_CHICKEN_EGG.parseSound()));
        }
        else {
            p.sendMessage(this.c("&c&l\u2718 &fThe arena does not exist."));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public void minPlayer(Player p, int min, String name) {
        File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
        if (file.exists()) {
            FileConfiguration arenaConfig = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");
            arenaConfig.set("Arena.minPlayer", min);
            try {
                arenaConfig.save(file);
            }
            catch (IOException e) {
                Console.error(e.getMessage());
            }
            p.sendMessage(this.c("&a&l\u2714 &fYou have set the min players in the arena &9" + name + "&f."));
            XSound.play(p, String.valueOf(ENTITY_CHICKEN_EGG.parseSound()));
        }
        else {
            p.sendMessage(this.c("&c&l\u2718 &fThe arena does not exist."));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public void setMode(Player p, String name) {
        if (getArenas().containsKey(name)) {
            File file = this.main.getConfigUtils().getFile(this.main, "Arenas/" + name + "/Settings");
            FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Arenas/" + name + "/Settings");

            config.set("Arena.mode", "TEAM");
            config.set("Arena.minPlayer", config.getInt("Arena.minPlayer")*2);
            config.set("Arena.maxPlayer", config.getInt("Arena.maxPlayer")*2);

            try {
                config.save(file);
            } catch (IOException e) {
                Console.error(e.getMessage());
            }

            p.sendMessage(c("&a&l\u2714 &fArena &e" + name + "&f changed to team"));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_YES.parseSound()));

        } else {
            p.sendMessage(c("&c&l\u2718 &fThat arena already exists"));
            XSound.play(p, String.valueOf(ENTITY_VILLAGER_NO.parseSound()));
        }
    }

    public HashMap<String, Arena> getArenas() {
        return arenas;
    }

    public List<Arena> getArenaList() {
        return arenaList;
    }

    public void loadWorld(String arena)
    {
        if(new File(main.getDataFolder(),arena).exists())
        {
            File f = new File(Bukkit.getWorldContainer(), arena);
            f.mkdirs();
            FileUtil.copy(new File(main.getDataFolder(), arena), f);
        }

        WorldCreator wc = new WorldCreator(arena);

        wc.type (WorldType.FLAT);
        wc.generatorSettings("36");
        wc.generateStructures(false);

        Bukkit.createWorld(wc);
        WorldBorder wb = Bukkit.getWorld(arena).getWorldBorder();
        wb.reset();

        Bukkit.getWorld(arena).setAutoSave(false);
    }

    public String c(String msg){ return ChatColor.translateAlternateColorCodes('&', msg); }

}
