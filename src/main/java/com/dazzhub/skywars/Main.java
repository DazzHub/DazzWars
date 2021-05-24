package com.dazzhub.skywars;

import com.dazzhub.skywars.Arena.ArenaManager;
import com.dazzhub.skywars.Commands.adminCmd;
import com.dazzhub.skywars.Commands.others.JoinArena;
import com.dazzhub.skywars.Commands.others.LeftArena;
import com.dazzhub.skywars.Commands.others.Party;
import com.dazzhub.skywars.Listeners.regListeners;
import com.dazzhub.skywars.MySQL.MySQL;
import com.dazzhub.skywars.MySQL.getPlayerDB;
import com.dazzhub.skywars.MySQL.utils.PlayerDB;
import com.dazzhub.skywars.MySQL.utils.PlayerManager;
import com.dazzhub.skywars.Party.PartyManager;
import com.dazzhub.skywars.Runnables.taskGlobal;
import com.dazzhub.skywars.Utils.*;
import com.dazzhub.skywars.Utils.NoteBlockAPI.SongPlayer;
import com.dazzhub.skywars.Utils.NoteBlockAPI.lSong;
import com.dazzhub.skywars.Utils.Runnable.TaskAsync;
import com.dazzhub.skywars.Utils.Runnable.TaskSync;
import com.dazzhub.skywars.Utils.achievements.IAchievementManager;
import com.dazzhub.skywars.Utils.cages.ICageManager;
import com.dazzhub.skywars.Utils.chests.IChestManager;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.configuration.configUtils;
import com.dazzhub.skywars.Utils.hologram.HologramsManager;
import com.dazzhub.skywars.Utils.inventory.Item.IItemManager;
import com.dazzhub.skywars.Utils.inventory.menu.IMenuManager;
import com.dazzhub.skywars.Utils.kits.IKitManager;
import com.dazzhub.skywars.Utils.pluginutils.Metrics;
import com.dazzhub.skywars.Utils.pluginutils.SpigotUpdater;
import com.dazzhub.skywars.Utils.resetWorld.resetWorld;
import com.dazzhub.skywars.Utils.resetWorld.resetWorldSlime;
import com.dazzhub.skywars.Utils.scoreboard.Placeholders;
import com.dazzhub.skywars.Utils.scoreboard.ScoreBoardAPI;
import com.dazzhub.skywars.Utils.signs.arena.ISignManager;
import com.dazzhub.skywars.Utils.signs.top.ITopManager;
import com.dazzhub.skywars.Utils.soulWell.SoulManager;
import com.dazzhub.skywars.Utils.vault.EconomyAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JavaPlugin {

    private static Main plugin;

    /* CONFIG */
    private final configUtils configUtils;

    /* EVENTS */
    private final regListeners regListeners;

    /* ARENA */
    private final ArenaManager arenaManager;
    private final resetWorld resetWorld;
    private final itemsCustom itemsCustom;
    private final resetWorldSlime resetWorldSlime;

    /* PARTY */
    private final PartyManager partyManager;

    /* SIGNS */
    private final ISignManager iSignManager;
    private final HologramsManager hologramsManager;

    /* CAGE MANAGER */
    private final ICageManager cageManager;

    /* CHEST MANAGER */
    private final IChestManager chestManager;

    /* ACHIEVEMENTS MANAGER */
    private final IAchievementManager achievementManager;

    /* INVENTORY MANAGER */
    private final IItemManager iItemManager;
    private final IMenuManager iMenuManager;

    /* KIT MANAGER */
    private final IKitManager iKitManager;

    /* MYSQL */
    private final getPlayerDB getPlayerDB;
    private final PlayerManager playerManager;

    /* LOBBY MANAGER */
    private final lobbyManager lobbyManager;
    private final ScoreBoardAPI scoreBoardAPI;

    /* TOP SIGNS */
    private final ITopManager iTopManager;

    /* SOUL MANAGER */
    private final SoulManager soulManager;

    private String version;

    /* NOTE BLOCK API*/
    public HashMap<String, ArrayList<SongPlayer>> playingSongs;

    private boolean isVaultEnable;
    private MVWorldManager mvWorldManager;

    public Main(){
        this.configUtils = new configUtils();

        this.regListeners = new regListeners(this);

        this.arenaManager = new ArenaManager(this);
        this.resetWorld = new resetWorld(this);
        this.itemsCustom = new itemsCustom(this);
        this.resetWorldSlime = new resetWorldSlime(this);

        this.partyManager = new PartyManager();

        this.cageManager = new ICageManager(this);

        this.chestManager = new IChestManager(this);

        this.achievementManager = new IAchievementManager(this);

        this.iSignManager = new ISignManager(this);
        this.hologramsManager = new HologramsManager(this);

        this.iItemManager = new IItemManager(this);
        this.iMenuManager = new IMenuManager(this);

        this.iKitManager = new IKitManager(this);

        this.getPlayerDB = new PlayerDB(this);

        this.playerManager = new PlayerManager();
        this.scoreBoardAPI = new ScoreBoardAPI(this);

        this.iTopManager = new ITopManager(this);

        this.soulManager = new SoulManager(this);

        this.lobbyManager = new lobbyManager(this);

        this.playingSongs = new HashMap<>();
    }

    @Override
    public void onEnable() {
        plugin = this;

        configCreate.get().setup(this, "Settings");
        configCreate.get().setup(this, "SoulWell");

        configCreate.get().setup(this, "Achievements");

        configCreate.get().setup(this, "Messages/Lang/es-ES");
        configCreate.get().setup(this, "Messages/Lang/en-EN");

        configCreate.get().setup(this, "Messages/Scoreboards/es-ES");
        configCreate.get().setup(this, "Messages/Scoreboards/en-EN");

        configCreate.get().setup(this, "Messages/Holograms/es-ES");
        configCreate.get().setup(this, "Messages/Holograms/en-EN");

        configCreate.get().setup(this, "Cages/cages");

        configCreate.get().setup(this, "Chests/ChestType");

        configCreate.get().setup(this, "Signs");

        configCreate.get().setupCustom(this, "Scrolling.nbs");

        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        this.version = version.substring(0, version.lastIndexOf("_"));

        new Lines(this).loadConfig();

        this.regListeners.onReg();
        this.arenaManager.loadArenas();
        this.cageManager.loadCages();
        this.chestManager.loadChests();

        this.iItemManager.loadFiles();
        this.iMenuManager.loadFiles();
        this.iKitManager.loadKits();
        this.soulManager.loadSoulWells();
        this.achievementManager.loadAchievements();

        this.getPlayerDB.loadMySQL();

        this.iTopManager.loadSign();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                getPlayerDB().loadPlayer(p.getUniqueId());
            }

            Bukkit.getScheduler().runTask(this, () -> Bukkit.getOnlinePlayers().forEach(p -> getScoreBoardAPI().setScoreBoard(p, Enums.ScoreboardType.LOBBY,false,false,false,false)));
        });

        new TaskAsync().runTaskTimerAsynchronously(this, 0, 20);
        new TaskSync().runTaskTimer(this, 0, 20);
        new taskGlobal(this).run();

        new Placeholders(this).register();

        this.getCommand("skywars").setExecutor(new adminCmd(this));
        this.getCommand("join").setExecutor(new JoinArena(this));
        this.getCommand("leave").setExecutor(new LeftArena(this));
        this.getCommand("party").setExecutor(new Party(this));

        new Metrics(this, 8943);

        this.isVaultEnable = (Bukkit.getPluginManager().getPlugin("Vault") != null);

        if (isVaultEnable){
            new EconomyAPI(this);
        }

        if (Bukkit.getPluginManager().getPlugin("Multiverse-Core") != null) {
            MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
            this.mvWorldManager = core.getMVWorldManager();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(p -> getPlayerDB().savePlayer(p.getUniqueId()));

        arenaManager.getArenas().values().stream().filter(arena -> arena.getResetArena().equals(Enums.ResetArena.SLIMEWORLDMANAGER)).forEach(arena -> getResetWorldSlime().unloadworld(arena.getUuid()));

        Bukkit.getScheduler().cancelTasks(this);
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

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public ITopManager getTopManager() {
        return iTopManager;
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

    public resetWorldSlime getResetWorldSlime() {
        return resetWorldSlime;
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

    public IKitManager getKitManager() {
        return iKitManager;
    }

    public ScoreBoardAPI getScoreBoardAPI() {
        return scoreBoardAPI;
    }

    public ISignManager getSignManager() {
        return iSignManager;
    }

    public SoulManager getSoulManager() {
        return soulManager;
    }

    public IAchievementManager getAchievementManager() {
        return achievementManager;
    }

    public MVWorldManager getMvWorldManager() {
        return mvWorldManager;
    }

    public String getVersion() {
        return version;
    }

    public void play(Player player, lSong song) {
        song.play(player);
    }

    public void stopPlaying(Player p) {
        if (plugin.playingSongs.get(p.getName()) == null) {
            return;
        }
        for (SongPlayer s : plugin.playingSongs.get(p.getName())) {
            s.removePlayer(p);
        }
    }

    public boolean isVaultEnable() {
        return isVaultEnable;
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

    public double getMoneyFormat(final double balance) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("#.#####", symbols);

        try {
            return format.parse(format.format(balance)).doubleValue();
        } catch (ParseException e) {
            return 0.0;
        }

    }
}
