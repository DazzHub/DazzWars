package com.dazzhub.skywars;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.ArenaManager;
import com.dazzhub.skywars.Arena.Menu.ArenasMenu;
import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.others.JoinArena;
import com.dazzhub.skywars.Commands.others.LeftArena;
import com.dazzhub.skywars.Listeners.regListeners;
import com.dazzhub.skywars.MySQL.MySQL;
import com.dazzhub.skywars.MySQL.getPlayerDB;
import com.dazzhub.skywars.MySQL.utils.PlayerDB;
import com.dazzhub.skywars.MySQL.utils.PlayerManager;
import com.dazzhub.skywars.Utils.CenterMessage;
import com.dazzhub.skywars.Utils.cages.ICageManager;
import com.dazzhub.skywars.Utils.chests.IChestManager;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.configuration.configUtils;
import com.dazzhub.skywars.Utils.hologram.HologramsManager;
import com.dazzhub.skywars.Utils.inventory.Item.IItemManager;
import com.dazzhub.skywars.Utils.inventory.menu.IMenuManager;
import com.dazzhub.skywars.Utils.itemsCustom;
import com.dazzhub.skywars.Utils.kits.IKitManager;
import com.dazzhub.skywars.Utils.lobbyManager;
import com.dazzhub.skywars.Utils.pluginutils.Metrics;
import com.dazzhub.skywars.Utils.pluginutils.SpigotUpdater;
import com.dazzhub.skywars.Utils.resetWorld.resetWorld;
import com.dazzhub.skywars.Utils.scoreboard.Placeholders;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import com.dazzhub.skywars.Utils.signs.ISignManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin {

    private static Main plugin;

    /* CONFIG */
    private configUtils configUtils;

    /* EVENTS */
    private regListeners regListeners;

    /* ARENA */
    private ArenaManager arenaManager;
    private resetWorld resetWorld;
    private itemsCustom itemsCustom;

    private ArenasMenu arenasMenu;

    private ISignManager iSignManager;
    private HologramsManager hologramsManager;

    /* CAGE MANAGER */
    private ICageManager cageManager;

    /* CHEST MANAGER */
    private IChestManager chestManager;

    /* INVENTORY MANAGER */
    private IItemManager iItemManager;
    private IMenuManager iMenuManager;

    /* KIT MANAGER */
    private IKitManager iKitManager;

    /* MYSQL */
    private getPlayerDB getPlayerDB;
    private PlayerManager playerManager;

    /* LOBBY MANAGER */
    private lobbyManager lobbyManager;
    private List<UUID> playerLobby;

    private ScoreBoardAPI scoreBoardAPI;

    private String version;

    public Main(){
        this.configUtils = new configUtils();

        this.regListeners = new regListeners(this);

        this.arenaManager = new ArenaManager(this);
        this.resetWorld = new resetWorld(this);
        this.itemsCustom = new itemsCustom(this);

        this.cageManager = new ICageManager(this);

        this.chestManager = new IChestManager(this);
        this.arenasMenu = new ArenasMenu(this);

        this.iSignManager = new ISignManager(this);
        this.hologramsManager = new HologramsManager(this);

        this.iItemManager = new IItemManager(this);
        this.iMenuManager = new IMenuManager(this);

        this.iKitManager = new IKitManager(this);

        this.getPlayerDB = new PlayerDB(this);
        this.playerManager = new PlayerManager();

        this.scoreBoardAPI = new ScoreBoardAPI(this);

        this.lobbyManager = new lobbyManager(this);
        this.playerLobby = new ArrayList<>();

    }

    @Override
    public void onEnable() {
        plugin = this;

        configCreate.get().setup(this, "Settings");

        configCreate.get().setup(this, "Arenas/Arenas");

        configCreate.get().setup(this, "Messages/Lang/es-ES");
        configCreate.get().setup(this, "Messages/Lang/en-EN");

        configCreate.get().setup(this, "Messages/Scoreboards/es-ES");
        configCreate.get().setup(this, "Messages/Scoreboards/en-EN");

        configCreate.get().setup(this, "Messages/Holograms/es-ES");
        configCreate.get().setup(this, "Messages/Holograms/en-EN");

        configCreate.get().setup(this, "Cages/cages");

        configCreate.get().setup(this, "Chests/ChestType");

        configCreate.get().setup(this, "Signs");

        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        this.version = version.substring(0, version.lastIndexOf("_"));

        this.regListeners.onReg();
        this.arenaManager.loadArenas();
        this.cageManager.loadCages();
        this.chestManager.loadChests();

        this.iItemManager.loadFiles();
        this.iMenuManager.loadFiles();
        this.iKitManager.loadKits();

        this.getPlayerDB.loadMySQL();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> Bukkit.getOnlinePlayers().forEach(p -> {
            getPlayerDB().loadPlayer(p.getUniqueId());
            playerLobby.add(p.getUniqueId());
        }));

        new Placeholders(this).register();

        Bukkit.getOnlinePlayers().forEach(p -> getScoreBoardAPI().setScoreBoard(p, ScoreBoardAPI.ScoreboardType.LOBBY));

        this.getCommand("skywars").setExecutor(new adminCmd(this));
        this.getCommand("join").setExecutor(new JoinArena(this));
        this.getCommand("leave").setExecutor(new LeftArena(this));

        new Metrics(this, 8943);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(p -> getPlayerDB().savePlayer(p.getUniqueId()));

        arenaManager.getArenas().values().forEach(arena -> Bukkit.getServer().unloadWorld(arena.getNameWorld(), false));

        MySQL.disconnect();
    }

    public Configuration getSettings() {
        return getConfigUtils().getConfig(this, "Settings");
    }

    public Configuration getSigns() {
        return getConfigUtils().getConfig(this, "Signs");
    }

    public boolean checkVersion(){
        return getVersion().contains("v1_8") || getVersion().contains("v1_9") || getVersion().contains("v1_10") || getVersion().contains("v1_11") || getVersion().contains("v1_12");
    }

    public void checkVersionPlayer(Player p) {
        SpigotUpdater spigotUpdater = new SpigotUpdater(this, 73468);
        try {
            if (spigotUpdater.checkForUpdates()) {
                p.sendMessage(CenterMessage.centerMessage("&8-=[&a&l*&8]=- --------   [ &9SkyWars &8]   -------- -=[&a&l*&8]=-"));
                p.sendMessage("");
                p.sendMessage(CenterMessage.centerMessage("&d&l➠ &fAn update was found! New version: &6" + spigotUpdater.getLatestVersion()));
                p.sendMessage(CenterMessage.centerMessage("&d&l➠ &fDownload link: &6" + spigotUpdater.getResourceURL()));
                p.sendMessage("");
                p.sendMessage(CenterMessage.centerMessage("&8-=[&a&l*&8]=- --------   [ &9SkyWars &8]   -------- -=[&a&l*&8]=-"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public ArenasMenu getArenasMenu() {
        return arenasMenu;
    }

    public HologramsManager getHologramsManager() {
        return hologramsManager;
    }

    public configUtils getConfigUtils() {
        return configUtils;
    }

    public resetWorld getResetWorld() {
        return resetWorld;
    }

    public itemsCustom getItemsCustom() {
        return itemsCustom;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public getPlayerDB getPlayerDB() {
        return getPlayerDB;
    }

    public lobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public ICageManager getCageManager() {
        return cageManager;
    }

    public IChestManager getChestManager() {
        return chestManager;
    }

    public IItemManager getItemManager() {
        return iItemManager;
    }

    public IMenuManager getMenuManager() {
        return iMenuManager;
    }

    public IKitManager getiKitManager() {
        return iKitManager;
    }

    public List<UUID> getPlayerLobby() {
        return playerLobby;
    }

    public ScoreBoardAPI getScoreBoardAPI() {
        return scoreBoardAPI;
    }

    public ISignManager getiSignManager() {
        return iSignManager;
    }

    public String getVersion() {
        return version;
    }

    public static Main getPlugin() {
        return plugin;
    }


    public static int getRelativePosition(int x, int y) {
        --x;
        --y;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        int r = y * 9 + x;
        if (r < 0) {
            r = 0;
        }
        return r;
    }
}
