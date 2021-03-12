package com.dazzhub.skywars.Arena;

import com.dazzhub.skywars.Arena.Menu.SpectatorMenu;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.addPlayerEvent;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.addSpectatorEvent;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.removePlayerEvent;
import com.dazzhub.skywars.Listeners.Custom.typeJoin.removeSpectatorEvent;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Party.Party;
import com.dazzhub.skywars.Runnables.RefillGame;
import com.dazzhub.skywars.Runnables.endGame;
import com.dazzhub.skywars.Runnables.inGame;
import com.dazzhub.skywars.Runnables.startingGame;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Cuboid;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Runnable.RunnableFactory;
import com.dazzhub.skywars.Utils.Runnable.RunnableType;
import com.dazzhub.skywars.Utils.Runnable.RunnableWorkerType;
import com.dazzhub.skywars.Utils.events.border.Border;
import com.dazzhub.skywars.Utils.events.border.eventBorder;
import com.dazzhub.skywars.Utils.events.dragon.Dragon;
import com.dazzhub.skywars.Utils.events.dragon.eventDragon;
import com.dazzhub.skywars.Utils.events.dropParty.dropParty;
import com.dazzhub.skywars.Utils.events.dropParty.eventParty;
import com.dazzhub.skywars.Utils.events.strom.Storm;
import com.dazzhub.skywars.Utils.events.strom.eventStorm;
import com.dazzhub.skywars.Utils.events.tntfall.TNTFall;
import com.dazzhub.skywars.Utils.events.tntfall.eventTNT;
import com.dazzhub.skywars.Utils.holoChest.IHoloChest;
import com.dazzhub.skywars.Utils.locUtils;
import com.dazzhub.skywars.Utils.signs.arena.ISign;
import com.dazzhub.skywars.Utils.vote.VotesSystem;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
public class Arena implements Cloneable {

    private Main main;

    /* Arena */
    private String nameArena;
    private String nameWorld;
    private String uuid;

    private Enums.GameStatus gameStatus;
    private Enums.Mode mode;

    /* Checks */
    private boolean isUsable;

    private int StartingGame;
    private int FinishedGame;
    private int DurationGame;

    /* Player */
    private List<GamePlayer> players;
    private List<GamePlayer> spectators;

    private int minPlayers;
    private int maxPlayers;

    /* MENU SPECTATOR */
    private SpectatorMenu spectatorMenu;

    /* Locations */
    private List<ArenaTeam> spawns;
    private Location spawnSpectator;

    private List<Location> islandChest;
    private List<Location> centerChest;
    private List<Location> centerChestCheck;

    private List<Location> chestsAddInGame;

    /* TYPE VOTES */
    private String chestType;
    private Enums.TypeVotes healthType;
    private Enums.TypeVotes timeType;
    private Enums.TypeVotes eventsType;
    private Enums.TypeVotes scenariosType;
    private VotesSystem votesSystem;

    /* EVENTS */
    private eventBorder eventBorder;
    private eventDragon eventDragon;
    private eventParty eventParty;
    private eventStorm eventStorm;
    private eventTNT eventTNT;

    /* SCENARIOS */
    private boolean NoClean;
    private boolean NoFall;
    private boolean NoProjectile;

    /* SING */
    private ISign iSign;

    /* REFILL */
    private RefillGame refillGame;
    private List<Integer> refillTime;
    private IHoloChest iHoloChest;

    /* TOP KILLERS */
    private HashMap<String, Integer> killers;

    /* RESET MODE */
    private Enums.ResetArena resetArena;

    private Configuration arenaConfig;

    private int totalTime;

    private boolean damageFallStarting;

    public Arena(String nameArena) {
        this.main = Main.getPlugin();

        /* Arena */
        this.nameArena = nameArena;
        this.uuid = UUID.randomUUID().toString();

        /* Player */
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();

        this.spawns = new ArrayList<>();

        /* Checks */
        this.gameStatus = Enums.GameStatus.DISABLED;
        this.isUsable = false;

        /* MENU SPECTATOR */
        this.spectatorMenu = new SpectatorMenu(this);

        /* LOCATIONS */
        this.islandChest = new ArrayList<>();
        this.centerChest = new ArrayList<>();
        this.centerChestCheck = new ArrayList<>();
        this.chestsAddInGame = new ArrayList<>();

        /* TYPE VOTES */
        this.chestType = Enums.TypeVotes.NORMAL.name();
        this.healthType = Enums.TypeVotes.HEART10;
        this.timeType = Enums.TypeVotes.DAY;
        this.eventsType = Enums.TypeVotes.NONE;
        this.scenariosType = Enums.TypeVotes.NONE;

        /* SCENARIOS */
        this.NoClean = false;
        this.NoFall = false;
        this.NoProjectile = false;

        this.votesSystem = new VotesSystem(this);

        /* REFILL*/
        this.refillGame = null;
        this.refillTime = new ArrayList<>();
        this.iHoloChest = new IHoloChest(this);

        /* TOP KILLERS */
        this.killers = new HashMap<>();

        /* RESET MODE*/
        this.totalTime = -1;
        this.resetArena = Enums.ResetArena.valueOf(main.getSettings().getString("ResetArena"));

        this.arenaConfig = main.getConfigUtils().getConfig(main, "Arenas/" + nameArena + "/Settings");
        if (arenaConfig != null) {
            this.nameWorld = arenaConfig.getString("Arena.world");

            this.minPlayers = arenaConfig.getInt("Arena.minPlayer");
            this.maxPlayers = arenaConfig.getInt("Arena.maxPlayer");

            this.DurationGame = arenaConfig.getInt("Arena.durationGame");
            this.StartingGame = arenaConfig.getInt("Arena.startingGame");
            this.FinishedGame = arenaConfig.getInt("Arena.finishedGame");

            this.iSign = main.getSignManager().loadSign(this);

            this.refillTime.addAll(arenaConfig.getIntegerList("Arena.refill"));

            this.mode = Enums.Mode.valueOf(arenaConfig.getString("Arena.mode"));
        }

        this.damageFallStarting = true;

        switch (resetArena){
            case RESETWORLD:
            case RESETCHUNK:{
                this.main.getResetWorld().importWorld(this, true);
                break;
            }

            case SLIMEWORLDMANAGER:{
                main.getResetWorldSlime().createworld(this);
                break;
            }
        }
    }

    public void resetArena(){
        /* Player */
        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.spawns = new ArrayList<>();

        /* Checks */
        this.totalTime = -1;
        this.gameStatus = Enums.GameStatus.DISABLED;
        this.isUsable = true;

        /* MENU SPECTATOR */
        this.spectatorMenu = new SpectatorMenu(this);

        /* LOCATIONS */
        this.islandChest = new ArrayList<>();
        this.centerChest = new ArrayList<>();
        this.centerChestCheck = new ArrayList<>();
        this.chestsAddInGame = new ArrayList<>();

        /* TYPE VOTES */
        this.chestType = Enums.TypeVotes.NORMAL.name();
        this.healthType = Enums.TypeVotes.HEART10;
        this.timeType = Enums.TypeVotes.DAY;
        this.eventsType = Enums.TypeVotes.NONE;
        this.scenariosType = Enums.TypeVotes.NONE;

        /* SCENARIOS */
        this.NoClean = false;
        this.NoFall = false;
        this.NoProjectile = false;

        this.votesSystem = new VotesSystem(this);

        /* REFILL */
        this.refillGame = null;
        this.refillTime = new ArrayList<>();

        this.iHoloChest.deleteAll();
        this.iHoloChest = new IHoloChest(this);

        if (arenaConfig != null) {
            this.refillTime.addAll(arenaConfig.getIntegerList("Arena.refill"));
        }

        /* TOP KILLERS */
        this.killers = new HashMap<>();
        this.damageFallStarting = true;

        switch (resetArena){
            case RESETCHUNK:{
                this.main.getResetWorld().importWorld(this, false);
                break;
            }

            case RESETWORLD:{
                this.main.getResetWorld().importWorld(this, true);
                break;
            }

            case SLIMEWORLDMANAGER:{
                main.getResetWorldSlime().unloadworld(uuid);

                this.uuid = UUID.randomUUID().toString();
                Main.getPlugin().getResetWorldSlime().createworld(this);
                break;
            }
        }
    }

    public void joinParty(Party party) {
        for (GamePlayer gamePlayer : party.getMembers()) {
            this.addPlayer(gamePlayer);
        }
    }

    public void addPlayer(GamePlayer gamePlayer) {
        Bukkit.getPluginManager().callEvent(new addPlayerEvent(gamePlayer, this));
        if (iSign != null) iSign.updateSign();
    }

    public void removePlayer(GamePlayer gamePlayer) {
        Bukkit.getPluginManager().callEvent(new removePlayerEvent(gamePlayer, this));
        if (iSign != null) iSign.updateSign();
    }

    public void addSpectator(GamePlayer gamePlayer) {
        Bukkit.getPluginManager().callEvent(new addSpectatorEvent(gamePlayer, this));
    }

    public void removeSpectator(GamePlayer gamePlayer, boolean goLobby) {
        Bukkit.getPluginManager().callEvent(new removeSpectatorEvent(gamePlayer, goLobby, this));
    }

    public ArenaTeam getAvailableTeam(int n) {
        for (ArenaTeam arenaTeam : this.spawns) {
            if (arenaTeam.getMembers().size() + n <= this.mode.getSize()) {
                return arenaTeam;
            }
        }
        return null;
    }

    public List<ArenaTeam> getAliveTeams() {
        return this.spawns.stream().filter(gameTeam -> gameTeam.getAliveTeams().size() > 0).collect(Collectors.toList());
    }

    public ArenaTeam getRandomTeam() {
        List<ArenaTeam> teams = new ArrayList<>();
        for (ArenaTeam gameTeam : this.spawns) {
            if (!gameTeam.isFull()) {
                if (gameTeam.getMembers().size() >= 1) {
                    teams.add(gameTeam);
                    break;
                } else {
                    return getAvailableTeam(getMode().getSize());
                }
            }
        }
        return teams.get(new Random().nextInt(teams.size()));
    }

    public int getHighest(Collection<Integer> collection, int n) {
        int n2 = 0;
        for (int intValue : collection) {
            if (intValue > n2 && intValue < n) {
                n2 = intValue;
            }
        }
        return n2;
    }

    public void checkVotes() {
        World world;

        if (getResetArena() == Enums.ResetArena.SLIMEWORLDMANAGER) {
            world = Bukkit.getWorld(uuid);
        } else {
            world = Bukkit.getWorld(nameWorld);
        }

        this.votesSystem.setTypes();

        switch (this.timeType) {
            case DAY: {
                world.setTime(1000L);
                break;
            }
            case SUNSET: {
                world.setTime(12000L);
                break;
            }
            case NIGHT: {
                world.setTime(14000L);
                break;
            }
        }

        switch (this.healthType) {
            case HEART10: {
                for (GamePlayer gamePlayer : this.players) {
                    gamePlayer.getPlayer().setHealthScale(20.0);
                }
                break;
            }
            case HEART20: {
                for (GamePlayer gamePlayer : this.players) {
                    gamePlayer.getPlayer().setHealthScale(40.0);
                }
                break;
            }
            case HEART30: {
                for (GamePlayer gamePlayer : this.players) {
                    gamePlayer.getPlayer().setHealthScale(60.0);
                }
                break;
            }
        }

        switch (this.eventsType){
            case BORDER:{
                eventBorder e = new Border(this);
                e.startEvent();
                this.eventBorder = e;
                break;
            }
            case DRAGON:{
                eventDragon e = new Dragon(this);
                e.startEvent();
                this.eventDragon = e;
                break;
            }
            case DROPPARTY:{
                dropParty e = new dropParty(this);
                e.startEvent();
                this.eventParty = e;
                break;
            }
            case STORM:{
                eventStorm e = new Storm(this);
                e.startEvent();
                this.eventStorm = e;
                break;
            }
            case TNTFALL:{
                eventTNT e = new TNTFall(this);
                e.startEvent();
                this.eventTNT = e;
                break;
            }
        }

        switch (this.scenariosType){
            case NOCLEAN:{
                this.NoClean = true;
                break;
            }
            case NOFALL:{
                this.NoFall = true;
                break;
            }
            case NOPROJECTILE:{
                this.NoProjectile = true;
                break;
            }
        }
    }

    public void fillChests() {
        switch (this.chestType) {
            case "BASIC": {
                getChests("BASIC");
                break;
            }
            case "NORMAL": {
                getChests("NORMAL");
                break;
            }
            case "OP": {
                getChests("OP");
                break;
            }
        }
    }

    private void getChests(String type) {
        for (Location location : this.islandChest) {
            if (location.getBlock().getType().equals(Material.CHEST)) {
                Chest chest = (Chest) location.getBlock().getState();
                main.getChestManager().getChestHashMap().get(type).refillChest(chest);
            }
        }
        for (Location location : this.centerChest) {
            if (location.getBlock().getType().equals(Material.CHEST)) {
                Chest chest = (Chest) location.getBlock().getState();
                main.getChestManager().getChestHashMap().get("CENTER").refillChest(chest);
            }
        }
    }

    public void getWinners(GamePlayer gamePlayer) {
        List<String> message1 = gamePlayer.getLangMessage().getStringList("Messages.WinnerGame");
        List<String> message = new ArrayList<>();
        List<String> winners = new ArrayList<>();

        if (this.killers.size() < 2) {
            this.killers.put("NONE", 0);
        }

        IntStream.range(0, 3).filter(i -> killers.size() < 3).forEachOrdered(i -> this.killers.put("NONE" + i, 0));

        for (GamePlayer winnersPlayers : this.players) {
            winners.add(winnersPlayers.getName());
        }

        String win = winners.toString().replace("[", "").replace("]", "");

        try {
            SortedMap<String, Integer> sortedMap = ImmutableSortedMap.copyOf(killers, Ordering.natural().reverse().onResultOf(Functions.forMap(killers)).compound(Ordering.natural().reverse()));

            String kills = String.valueOf(sortedMap.values()).replace("[", "").replace("]", "");
            String player = String.valueOf(sortedMap.keySet()).replace("[", "").replace("]", "");

            for (String s : message1) {
                message.add(s
                        .replace("%winner%", win)

                        .replace("%player1%", player.split(",")[0])
                        .replace("%player2%", player.split(", ")[1])
                        .replace("%player3%", player.split(", ")[2])

                        .replace("%kills1%", kills.split(",")[0])
                        .replace("%kills2%", kills.split(", ")[1])
                        .replace("%kills3%", kills.split(", ")[2]));
            }

            Bukkit.getScheduler().runTaskLater(main, () -> gamePlayer.sendMessage(message), 5);

        } catch (Exception e) {
            Console.warning("Win message no work");
        }
    }

    public void removeCage(GamePlayer gamePlayer, Enums.Mode mode, int yp) {
        if (gamePlayer == null) return;
        if (gamePlayer.getArenaTeam().getSpawn() == null) return;

        Location loc = gamePlayer.getArenaTeam().getSpawn();

        Location point1 = null;
        Location point2 = null;

        if (mode.equals(Enums.Mode.SOLO)){
            int radius = 1;
            point1 = new Location(loc.getWorld(), loc.getX() + radius, loc.getY() + yp, loc.getZ() + radius);
            point2 = new Location(loc.getWorld(), loc.getX() - radius, loc.getY() - yp, loc.getZ() - radius);
        } else if (mode.equals(Enums.Mode.TEAM)) {
            int radius = 5;
            point1 = new Location(loc.getWorld(), loc.getX() + radius, loc.getY() + yp, loc.getZ() + radius);
            point2 = new Location(loc.getWorld(), loc.getX() - radius, loc.getY() - yp, loc.getZ() - radius);
        }

        if (point1 == null) return;

        Cuboid cuboid = new Cuboid(point1, point2);

        for (Block block : cuboid){
            block.setType(Material.AIR);
        }
    }

    public boolean checkUsable() {
        return getGameStatus().equals(Enums.GameStatus.WAITING) || getGameStatus().equals(Enums.GameStatus.STARTING) || !getGameStatus().equals(Enums.GameStatus.INGAME) && !getGameStatus().equals(Enums.GameStatus.DISABLED) && !isUsable() && getPlayers().size() < getMaxPlayers();
    }

    public boolean checkStart() {
        return this.players.size() >= this.minPlayers;
    }

    public String getStatusMsg() {
        Configuration config = main.getSigns();
        if (this.gameStatus.equals(Enums.GameStatus.WAITING)) {
            return this.c(config.getString("Status.Waiting.msg"));
        }
        if (this.gameStatus.equals(Enums.GameStatus.STARTING)) {
            return this.c(config.getString("Status.Starting.msg"));
        }
        if (this.gameStatus.equals(Enums.GameStatus.INGAME)) {
            return this.c(config.getString("Status.InGame.msg"));
        }
        if (this.gameStatus.equals(Enums.GameStatus.RESTARTING)) {
            return this.c(config.getString("Status.Restarting.msg"));
        }
        if (this.players.size() >= this.maxPlayers) {
            return this.c(config.getString("Status.Full.msg"));
        }

        return "Loading...";
    }

    public XMaterial getBlockStatus() {
        Configuration config = main.getSigns();
        if (gameStatus.equals(Enums.GameStatus.WAITING)) {
            ItemStack item;
            if (main.checkVersion()){
                item = new ItemStack(Material.getMaterial(config.getString("Status.Waiting.material")),1, (short) config.getInt("Status.Waiting.id"));
            } else {
                item = new ItemStack(Material.getMaterial(config.getString("Status.Waiting.material")));
            }
            return XMaterial.matchXMaterial(item);
        } else if (gameStatus.equals(Enums.GameStatus.STARTING)) {
            ItemStack item;
            if (main.checkVersion()){
                item = new ItemStack(Material.getMaterial(config.getString("Status.Starting.material")),1, (short) config.getInt("Status.Starting.id"));
            } else {
                item = new ItemStack(Material.getMaterial(config.getString("Status.Starting.material")));
            }
            return XMaterial.matchXMaterial(item);
        } else if (gameStatus.equals(Enums.GameStatus.INGAME)) {
            ItemStack item;
            if (main.checkVersion()){
                item = new ItemStack(Material.getMaterial(config.getString("Status.InGame.material")),1, (short) config.getInt("Status.InGame.id"));
            } else {
                item = new ItemStack(Material.getMaterial(config.getString("Status.InGame.material")));
            }
            return XMaterial.matchXMaterial(item);
        } else if (gameStatus.equals(Enums.GameStatus.RESTARTING)) {
            ItemStack item;
            if (main.checkVersion()){
                item = new ItemStack(Material.getMaterial(config.getString("Status.Restarting.material")),1, (short) config.getInt("Status.Restarting.id"));
            } else {
                item = new ItemStack(Material.getMaterial(config.getString("Status.Restarting.material")));
            }
            return XMaterial.matchXMaterial(item);
        } else if (this.players.size() >= this.maxPlayers) {
            ItemStack item;
            if (main.checkVersion()){
                item = new ItemStack(Material.getMaterial(config.getString("Status.Full.material")),1, (short) config.getInt("Status.Full.id"));
            } else {
                item = new ItemStack(Material.getMaterial(config.getString("Status.Full.material")));
            }
            return XMaterial.matchXMaterial(item);
        }

        ItemStack item;
        if (main.checkVersion()){
            item = new ItemStack(Material.getMaterial(config.getString("Status.Searching.material")),1, (short)config.getInt("Status.Searching.id"));
        } else {
            item = new ItemStack(Material.getMaterial(config.getString("Status.Searching.material")));
        }
        return XMaterial.matchXMaterial(item);
    }


    public void loadSpawns(String uuidmap) {
        if (!arenaConfig.getString("Arena.spawns", "").isEmpty()) {
            switch (resetArena){
                case RESETWORLD:
                case RESETCHUNK:{
                    arenaConfig.getConfigurationSection("Arena.spawns").getKeys(false).forEach(key ->
                            spawns.add(new ArenaTeam(this, locUtils.stringToLoc(arenaConfig.getString("Arena.spawns." + key), nameWorld)))
                    );
                    break;
                }

                case SLIMEWORLDMANAGER:{
                    arenaConfig.getConfigurationSection("Arena.spawns").getKeys(false).forEach(key ->
                            spawns.add(new ArenaTeam(this, locUtils.stringToLoc(arenaConfig.getString("Arena.spawns." + key), uuidmap)))
                    );
                    break;
                }
            }
        }

        if (!arenaConfig.getString("Arena.spawnSpectator", "").isEmpty()) {

            switch (resetArena){
                case RESETWORLD:
                case RESETCHUNK:{
                    this.spawnSpectator = locUtils.stringToLoc(arenaConfig.getString("Arena.spawnSpectator"), nameWorld);
                    break;
                }

                case SLIMEWORLDMANAGER:{
                    this.spawnSpectator = locUtils.stringToLoc(arenaConfig.getString("Arena.spawnSpectator"), uuidmap);
                    break;
                }
            }
        }

        if (!arenaConfig.getString("Arena.centerChest", "").isEmpty()) {

            switch (resetArena){
                case RESETWORLD:
                case RESETCHUNK:{
                    arenaConfig.getStringList("Arena.centerChest").forEach(loc ->
                            this.centerChest.add(locUtils.stringToLoc(loc, nameWorld))
                    );
                    break;
                }

                case SLIMEWORLDMANAGER:{
                    arenaConfig.getStringList("Arena.centerChest").forEach(loc ->
                            this.centerChest.add(locUtils.stringToLoc(loc, uuidmap))
                    );
                    break;
                }
            }

        }
    }

    /*
     * ARENA
     */
    public void startingGame() {
        RunnableFactory factory = Main.getPlugin().getFactory();

        factory.registerRunnable(RunnableWorkerType.SYNC, RunnableType.TIMER, 20L, new startingGame(factory, this));

        setUsable(true);
        setGameStatus(Enums.GameStatus.STARTING);
    }

    public void inGame() {
        RunnableFactory factory = Main.getPlugin().getFactory();

        factory.registerRunnable(RunnableWorkerType.SYNC, RunnableType.TIMER, 20L, new inGame(factory, this));
        this.setGameStatus(Enums.GameStatus.INGAME);
    }


    public void endGame() {
        RunnableFactory factory = Main.getPlugin().getFactory();

        factory.registerRunnable(RunnableWorkerType.SYNC, RunnableType.TIMER, 20L, new endGame(factory, this));
    }

    public RefillGame RefillGame(int timer) {
        RunnableFactory factory = Main.getPlugin().getFactory();

        RefillGame refillGame = new RefillGame(factory, this, timer);

        factory.registerRunnable(RunnableWorkerType.SYNC, RunnableType.TIMER, 20L, refillGame);

        return refillGame;
    }

    public String timeScore(GamePlayer gamePlayer) {
        return this.getTotalTime() != this.getStartingGame() && this.getTotalTime() != -1 ? this.getTotalTime() + "" : gamePlayer.getLangMessage().getString("Messages.ScoreBoard.Waiting", "Error ScoreBoard.Waiting");
    }

    public String typeEvent(GamePlayer gamePlayer) {
        Configuration config = gamePlayer.getLangMessage();

        if (refillGame != null && refillGame.getTimer() >= 1) {
            return config.getString("Messages.ScoreBoard.Refill", "Error ScoreBoard.Refill").replace("%time%", String.valueOf(calculateTime(refillGame.getTimer())));
        } else if (refillGame == null && !refillTime.isEmpty()) {
            return config.getString("Messages.ScoreBoard.Refill", "Error ScoreBoard.Refill").replace("%time%", String.valueOf(calculateTime(0)));
        } else  if (gameStatus.equals(Enums.GameStatus.RESTARTING)) {
            return config.getString("Messages.ScoreBoard.EndGame", "Error ScoreBoard.EndGame");
        } else {
            return config.getString("Messages.ScoreBoard.None", "ScoreBoard.None");
        }
    }

    public void setGameStatus(Enums.GameStatus gameStatus) {
        if (iSign != null) iSign.updateSign();
        this.gameStatus = gameStatus;
    }

    public String calculateTime(long seconds) {
        return String.format("%02d:%02d", TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds)), TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

    @Override
    public Arena clone() throws CloneNotSupportedException {

        return (Arena) super.clone();
    }

    public String getWinnersScore() {
        List<String> winners = new ArrayList<>();
        for (GamePlayer winnersPlayers : this.players) {
            winners.add(winnersPlayers.getName());
        }

        return winners.toString().replace("[", "").replace("]", "");
    }
}
