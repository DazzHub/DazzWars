package com.dazzhub.skywars.MySQL.utils;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.ArenaTeam;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Party.Party;
import com.dazzhub.skywars.Utils.CenterMessage;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Tools;
import com.dazzhub.skywars.Utils.effects.getTypeKills;
import com.dazzhub.skywars.Utils.effects.getTypeWins;
import com.dazzhub.skywars.Utils.effects.kills.*;
import com.dazzhub.skywars.Utils.effects.wins.*;
import com.dazzhub.skywars.Utils.hologram.Holograms;
import com.cryptomorin.xseries.XSound;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public class GamePlayer {

    private UUID uuid;
    private String name;

    private int WinsSolo;
    private int WinsTeam;
    private int WinsRanked;

    private int KillsSolo;
    private int KillsTeam;
    private int KillsRanked;

    private int DeathsSolo;
    private int DeathsTeam;
    private int DeathsRanked;

    private int GamesSolo;
    private int GamesTeam;
    private int GamesRanked;

    private int ShotsSolo;
    private int ShotsTeam;
    private int ShotsRanked;

    private int HitsSolo;
    private int HitsTeam;
    private int HitsRanked;

    private int Coins;
    private int Souls;
    private int LvlRanked;

    private int BlockPlaced;
    private int BlockBroken;
    private int ItemsEnchanted;
    private int ItemsCrafted;
    private double DistanceWalked;

    private String CageSolo;
    private String CageTeam;
    private String CageRanked;
    private List<String> cagesSoloList;
    private List<String> cagesTeamList;
    private List<String> cagesRankedList;

    private String WinEffectSolo;
    private String WinEffectTeam;
    private String WinEffectRanked;
    private List<String> WinEffectsSoloList;
    private List<String> WinEffectsTeamList;
    private List<String> WinEffectsRankedList;

    private String KillEffectSolo;
    private String KillEffectTeam;
    private String KillEffectRanked;
    private List<String> KillEffectsSoloList;
    private List<String> KillEffectsTeamList;
    private List<String> KillEffectsRankedList;

    private String KillSoundSolo;
    private String KillSoundTeam;
    private String KillSoundRanked;
    private List<String> KillSoundsSoloList;
    private List<String> KillSoundsTeamList;
    private List<String> KillSoundsRankedList;

    private String TrailSolo;
    private String TrailTeam;
    private String TrailRanked;
    private List<String> TrailsSoloList;
    private List<String> TrailsTeamList;
    private List<String> TrailsRankedList;

    private String kitSolo;
    private String kitTeam;
    private String kitRanked;
    private List<String> kitSoloList;
    private List<String> kitTeamList;
    private List<String> kitRankedList;

    private Holograms holograms;

    private boolean isSpectating;
    private Arena Arena;
    private ArenaTeam arenaTeam;
    private int killsStreak;
    private int taskId;
    private boolean isLobby;
    private boolean editMode;

    private Party party;
    private GamePlayer ownerParty;

    private String lang;

    private Location cage1;
    private Location cage2;

    private List<Projectile> projectilesList;

    public GamePlayer(
            UUID uuid,
            String name,
            int winsSolo,
            int winsTeam,
            int winsRanked,

            int killsSolo,
            int killsTeam,
            int killsRanked,

            int deathsSolo,
            int deathsTeam,
            int deathsRanked,

            int gamesSolo,
            int gamesTeam,
            int gamesRanked,

            int shotsSolo,
            int shotsTeam,
            int shotsRanked,

            int hitsSolo,
            int hitsTeam,
            int hitsRanked,

            int blockPlaced,
            int blockBroken,
            int itemsEnchanted,
            int itemsCrafted,
            double distanceWalked,

            int coins,
            int souls,
            int lvlRanked,

            String cageSolo,
            String cageTeam,
            String cageRanked,

            String winEffectSolo,
            String winEffectTeam,
            String winEffectRanked,

            String killEffectSolo,
            String killEffectTeam,
            String killEffectRanked,

            String killSoundSolo,
            String killSoundTeam,
            String killSoundRanked,

            String trailSolo,
            String trailTeam,
            String trailRanked,

            String kitSolo,
            String kitTeam,
            String kitRanked,

            String lang
    ) {

        this.uuid = uuid;
        this.name = name;

        this.WinsSolo = winsSolo;
        this.WinsTeam = winsTeam;
        this.WinsRanked = winsRanked;

        this.KillsSolo = killsSolo;
        this.KillsTeam = killsTeam;
        this.KillsRanked = killsRanked;

        this.DeathsSolo = deathsSolo;
        this.DeathsTeam = deathsTeam;
        this.DeathsRanked = deathsRanked;

        this.GamesSolo = gamesSolo;
        this.GamesTeam = gamesTeam;
        this.GamesRanked = gamesRanked;

        this.ShotsSolo = shotsSolo;
        this.ShotsTeam = shotsTeam;
        this.ShotsRanked = shotsRanked;

        this.HitsSolo = hitsSolo;
        this.HitsTeam = hitsTeam;
        this.HitsRanked = hitsRanked;

        this.BlockPlaced = blockPlaced;
        this.BlockBroken = blockBroken;
        this.ItemsEnchanted = itemsEnchanted;
        this.ItemsCrafted = itemsCrafted;
        this.DistanceWalked = distanceWalked;

        this.Coins = coins;
        this.Souls = souls;
        this.LvlRanked = lvlRanked;

        this.CageSolo = cageSolo;
        this.CageTeam = cageTeam;
        this.CageRanked = cageRanked;
        this.cagesSoloList = new ArrayList<>();
        this.cagesTeamList = new ArrayList<>();
        this.cagesRankedList = new ArrayList<>();

        this.WinEffectSolo = winEffectSolo;
        this.WinEffectTeam = winEffectTeam;
        this.WinEffectRanked = winEffectRanked;
        this.WinEffectsSoloList = new ArrayList<>();
        this.WinEffectsTeamList = new ArrayList<>();
        this.WinEffectsRankedList = new ArrayList<>();

        this.KillEffectSolo = killEffectSolo;
        this.KillEffectTeam = killEffectTeam;
        this.KillEffectRanked = killEffectRanked;
        this.KillEffectsSoloList = new ArrayList<>();
        this.KillEffectsTeamList = new ArrayList<>();
        this.KillEffectsRankedList = new ArrayList<>();

        this.KillSoundSolo = killSoundSolo;
        this.KillSoundTeam = killSoundTeam;
        this.KillSoundRanked = killSoundRanked;
        this.KillSoundsSoloList = new ArrayList<>();
        this.KillSoundsTeamList = new ArrayList<>();
        this.KillSoundsRankedList = new ArrayList<>();

        this.TrailSolo = trailSolo;
        this.TrailTeam = trailTeam;
        this.TrailRanked = trailRanked;

        this.TrailsSoloList = new ArrayList<>();
        this.TrailsTeamList = new ArrayList<>();
        this.TrailsRankedList = new ArrayList<>();

        this.kitSolo = kitSolo;
        this.kitTeam = kitTeam;
        this.kitRanked = kitRanked;

        this.kitSoloList = new ArrayList<>();
        this.kitTeamList = new ArrayList<>();
        this.kitRankedList = new ArrayList<>();

        this.isSpectating = false;
        this.Arena = null;
        this.arenaTeam = null;
        this.killsStreak = 0;
        this.isLobby = true;
        this.editMode = false;

        this.party = null;
        this.ownerParty = null;

        this.lang = lang;

        this.cage1 = null;
        this.cage2 = null;

        this.projectilesList = new ArrayList<>();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUuid());
    }

    /* LANG */
    public Configuration getLangMessage() {
        return Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(), "Messages/Lang/" + this.lang);
    }
    public Configuration getScoreboardMessage() {
        return Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(), "Messages/Scoreboards/" + this.lang);
    }
    public Configuration getHologramMessage() {
        return Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(), "Messages/Holograms/" + this.lang);
    }

    protected void typeMessage(boolean useFor, String msgSimple, List<String> lines) {
        if (useFor) {
            lines.forEach(msg -> this.getPlayer().sendMessage(PlaceholderAPI.setPlaceholders(this.getPlayer(), c(msg.startsWith("%center%") ? CenterMessage.centerMessage(msg.replace("%center%", "")) : c(msg)))));
        } else {
            this.getPlayer().sendMessage(PlaceholderAPI.setPlaceholders(this.getPlayer(), c(msgSimple.startsWith("%center%") ? CenterMessage.centerMessage(msgSimple.replace("%center%", "")) : c(msgSimple))));
        }
    }

    public void sendMessage(String simple) {
        this.typeMessage(false, simple, null);
    }
    public void sendMessage(List<String> lines) {
        this.typeMessage(true, null, lines);
    }

    /* WINS */
    public void addWinsSolo() {
        this.setWinsSolo(this.getWinsSolo() + 1);
    }
    public void addWinsTeam() {
        this.setWinsTeam(this.getWinsTeam() + 1);
    }
    public void addWinsRanked() {
        this.setWinsRanked(this.getWinsRanked() + 1);
    }

    /* KILLS */
    public void addKillsSolo() {
        this.setKillsSolo(this.getKillsSolo() + 1);
    }
    public void addKillsTeam() {
        this.setKillsTeam(this.getKillsTeam() + 1);
    }
    public void addKillsRanked() {
        this.setKillsRanked(this.getKillsRanked() + 1);
    }

    /* DEATHS */
    public void addDeathsSolo() {
        this.setDeathsSolo(this.getDeathsSolo() + 1);
    }
    public void addDeathsTeam() {
        this.setDeathsTeam(this.getDeathsTeam() + 1);
    }
    public void addDeathsRanked() {
        this.setDeathsRanked(this.getDeathsRanked() + 1);
    }

    /* GAMES */
    public void addGamesSolo() {
        this.setGamesSolo(this.getGamesSolo() + 1);
    }
    public void addGamesTeam() {
        this.setGamesTeam(this.getGamesTeam() + 1);
    }
    public void addGamesRanked() {
        this.setGamesRanked(this.getGamesRanked() + 1);
    }

    /* SHOTS */
    public void addShotsSolo() {
        this.setShotsSolo(this.getShotsSolo() + 1);
    }
    public void addShotsTeam() {
        this.setShotsTeam(this.getShotsTeam() + 1);
    }
    public void addShotsRanked() {
        this.setShotsRanked(this.getShotsRanked() + 1);
    }

    /* HITS */
    public void addHitsSolo() {
        this.setHitsSolo(this.getHitsSolo() + 1);
    }
    public void addHitsTeam() {
        this.setHitsTeam(this.getHitsTeam() + 1);
    }
    public void addHitsRanked() {
        this.setHitsRanked(this.getHitsRanked() + 1);
    }

    /* MISSIONS */
    public void addBlockPlaced() {
        this.setBlockPlaced(this.getBlockPlaced() + 1);
    }
    public void addBlockBroken() {
        this.setBlockBroken(this.getBlockBroken() + 1);
    }
    public void addItemEnchanted() {
        this.setItemsEnchanted(this.getItemsEnchanted() + 1);
    }
    public void addItemCrafted() {
        this.setItemsCrafted(this.getItemsCrafted() + 1);
    }

    /* POINTS */
    public void addCoins(int amount) {
        this.setCoins(this.getCoins() + amount);
    }
    public void addSouls(int amount) {
        this.setSouls(this.getSouls() + amount);
    }
    public void addRanked(int amount) {
        this.setLvlRanked(this.getLvlRanked() + amount);
    }
    public void removeCoins(int amount) {
        this.setCoins(this.getCoins() - amount);
    }
    public void removeLvlRanked(int amount) {
        this.setLvlRanked(this.getLvlRanked() - amount);
    }

    public boolean isInArena() {
        return this.Arena != null;
    }
    public void addKillsArena() {
        this.setKillsStreak(this.getKillsStreak() + 1);
    }

    public void addSpectating() {
        Player p = this.getPlayer();

        this.setInvClear();

        p.setGameMode(GameMode.ADVENTURE);

        p.setFallDistance(0);
        p.setHealthScale(20.0D);
        p.setHealth(20.0D);
        p.setFoodLevel(20);

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
        p.spigot().setCollidesWithEntities(false);

        p.setAllowFlight(true);
        p.setFlying(true);
    }

    public void resetPlayer(boolean setMaxHealth) {
        Player p = this.getPlayer();

        this.setInvClear();

        p.getActivePotionEffects().forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
        p.spigot().setCollidesWithEntities(true);

        p.setGameMode(GameMode.SURVIVAL);
        Bukkit.getOnlinePlayers().forEach(online -> online.getPlayer().showPlayer(p));

        if (setMaxHealth) {
            p.setHealthScale(20.0D);
        }

        p.setHealth(20.0D);
        p.setFoodLevel(20);
        p.setExp(0.0f);
        p.setLevel(0);
        p.setFlying(false);
        p.setAllowFlight(false);
        p.updateInventory();
        p.closeInventory();
    }

    private void setInvClear() {
        Player p = getPlayer();
        if (p == null || !p.isOnline()) return;

        ItemStack[] emptyinv = new ItemStack[p.getInventory().getContents().length];
        p.getInventory().setContents(emptyinv);

        ItemStack[] emptyinv2 = new ItemStack[p.getInventory().getArmorContents().length];
        p.getInventory().setArmorContents(emptyinv2);
    }

    public void playSound(String sound) {
        XSound.play(getPlayer(), String.valueOf(XSound.valueOf(sound).parseSound()));
    }

    public getTypeKills getTypeKill(String mode) {
        mode = mode.toLowerCase();

        if (mode.equalsIgnoreCase("none")) return null;

        getTypeKills getType;

        switch (mode) {
            case "dropsoup": {
                getType = new dropSoup(this);
                break;
            }
            case "frostflame": {
                getType = new frostFlame(this);
                break;
            }
            case "headexplode": {
                getType = new headExplode(this);
                break;
            }
            case "heart": {
                getType = new heart(this);
                break;
            }
            case "redstone": {
                getType = new redstone(this);
                break;
            }
            case "satan": {
                getType = new satan(this);
                break;
            }
            case "squid": {
                getType = new squid(this);
                break;
            }
            case "twister": {
                getType = new twister(this);
                break;
            }
            case "wave": {
                getType = new wave(this);
                break;
            }
            default: {
                if (mode.equalsIgnoreCase("none")) return null;
                getType = null;
                Console.error("KillEffect: " + mode + " not exist");
                break;
            }
        }

        return getType;
    }

    public getTypeWins getTypeWin(String mode) {
        mode = mode.toLowerCase();
        getTypeWins getType;

        switch (mode) {
            case "antigravity": {
                getType = new antigravity(this);
                break;
            }
            case "chickens": {
                getType = new chickens(this);
                break;
            }
            case "fallingblocks": {
                getType = new fallingblocks(this);
                break;
            }
            case "fallingsheep": {
                getType = new fallingsheep(this);
                break;
            }
            case "parachute": {
                getType = new parachute(this);
                break;
            }

            case "default":
            case "fireworks": {
                getType = new fireworks(this);
                break;
            }

            default: {
                if (mode.equalsIgnoreCase("none")) return null;
                getType = null;
                Console.error("WinEffect: " + mode + " not exist");
                break;
            }
        }

        return getType;
    }

    public void getTrail(String mode, Projectile proj) {
        if (!mode.equalsIgnoreCase("none")) {
            projectilesList.add(proj);

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i < projectilesList.size(); i++) {
                        Projectile arrows = projectilesList.get(i);
                        ParticleEffect.valueOf(mode).display(arrows.getLocation(), 0f, 0f, 0f, 0f, 10, null);

                        if (i == projectilesList.size()) {
                            this.cancel();
                        }
                    }
                }
            }.runTaskTimerAsynchronously(Main.getPlugin(), 0, 1);
        }

    }

    protected String c(String cmd) {
        return ChatColor.translateAlternateColorCodes('&', cmd);
    }

}
