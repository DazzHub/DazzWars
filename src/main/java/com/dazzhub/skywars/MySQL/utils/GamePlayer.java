package com.dazzhub.skywars.MySQL.utils;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.ArenaTeam;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Party.Party;
import com.dazzhub.skywars.Utils.CenterMessage;
import com.dazzhub.skywars.Utils.hologram.Holograms;
import com.dazzhub.skywars.Utils.xseries.XSound;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GamePlayer {

    private UUID uuid;
    private String name;

    private int WinsSolo;
    private int WinsTeam;

    private int KillsSolo;
    private int KillsTeam;

    private int DeathsSolo;
    private int DeathsTeam;

    private int GamesSolo;
    private int GamesTeam;

    private int ShotsSolo;
    private int ShotsTeam;

    private int HitsSolo;
    private int HitsTeam;

    private int Coins;
    private int Souls;

    private int BlockPlaced;
    private int BlockBroken;
    private int ItemsEnchanted;
    private int ItemsCrafted;
    private double DistanceWalked;

    private String CageSolo;
    private String CageTeam;
    private List<String> cagesSoloList;
    private List<String> cagesTeamList;

    private String WinEffectSolo;
    private String WinEffectTeam;
    private List<String> WinEffectsSoloList;
    private List<String> WinEffectsTeamList;

    private String KillEffectSolo;
    private String KillEffectTeam;
    private List<String> KillEffectsSoloList;
    private List<String> KillEffectsTeamList;

    private String KillSoundSolo;
    private String KillSoundTeam;
    private List<String> KillSoundsSoloList;
    private List<String> KillSoundsTeamList;

    private String TrailSolo;
    private String TrailTeam;
    private List<String> TrailsSoloList;
    private List<String> TrailsTeamList;

    private String kitSolo;
    private String kitTeam;
    private List<String> kitSoloList;
    private List<String> kitTeamList;

    private Holograms holograms;

    private boolean isSpectating;
    private Arena Arena;
    private ArenaTeam arenaTeam;
    private int killsStreak;
    private int taskId;

    private Party party;
    private GamePlayer ownerParty;

    private String lang;

    private Location cage1;
    private Location cage2;

    public GamePlayer(
                      UUID uuid,
                      String name,
                      int winsSolo,
                      int winsTeam,

                      int killsSolo,
                      int killsTeam,

                      int deathsSolo,
                      int deathsTeam,

                      int gamesSolo,
                      int gamesTeam,

                      int shotsSolo,
                      int shotsTeam,

                      int hitsSolo,
                      int hitsTeam,

                      int blockPlaced,
                      int blockBroken,
                      int itemsEnchanted,
                      int itemsCrafted,
                      double distanceWalked,

                      int coins,
                      int souls,

                      String cageSolo,
                      String cageTeam,

                      String winEffectSolo,
                      String winEffectTeam,

                      String killEffectSolo,
                      String killEffectTeam,

                      String killSoundSolo,
                      String killSoundTeam,

                      String trailSolo,
                      String trailTeam,

                      String kitSolo,
                      String kitTeam,

                      String lang
        ) {

        this.uuid = uuid;
        this.name = name;

        this.WinsSolo = winsSolo;
        this.WinsTeam = winsTeam;

        this.KillsSolo = killsSolo;
        this.KillsTeam = killsTeam;

        this.DeathsSolo = deathsSolo;
        this.DeathsTeam = deathsTeam;

        this.GamesSolo = gamesSolo;
        this.GamesTeam = gamesTeam;

        this.ShotsSolo = shotsSolo;
        this.ShotsTeam = shotsTeam;

        this.HitsSolo = hitsSolo;
        this.HitsTeam = hitsTeam;

        this.BlockPlaced = blockPlaced;
        this.BlockBroken = blockBroken;
        this.ItemsEnchanted = itemsEnchanted;
        this.ItemsCrafted = itemsCrafted;
        this.DistanceWalked = distanceWalked;

        this.Coins = coins;
        this.Souls = souls;

        this.CageSolo = cageSolo;
        this.CageTeam = cageTeam;
        this.cagesSoloList = new ArrayList<>();
        this.cagesTeamList = new ArrayList<>();

        this.WinEffectSolo = winEffectSolo;
        this.WinEffectTeam = winEffectTeam;
        this.WinEffectsSoloList = new ArrayList<>();
        this.WinEffectsTeamList = new ArrayList<>();

        this.KillEffectSolo = killEffectSolo;
        this.KillEffectTeam = killEffectTeam;
        this.KillEffectsSoloList = new ArrayList<>();
        this.KillEffectsTeamList = new ArrayList<>();

        this.KillSoundSolo = killSoundSolo;
        this.KillSoundTeam = killSoundTeam;
        this.KillSoundsSoloList = new ArrayList<>();
        this.KillSoundsTeamList = new ArrayList<>();

        this.TrailSolo = trailSolo;
        this.TrailTeam = trailTeam;

        this.TrailsSoloList = new ArrayList<>();
        this.TrailsTeamList = new ArrayList<>();

        this.kitSolo = kitSolo;
        this.kitTeam = kitTeam;
        this.kitSoloList = new ArrayList<>();
        this.kitTeamList = new ArrayList<>();

        this.isSpectating = false;
        this.Arena = null;
        this.arenaTeam = null;
        this.killsStreak = 0;

        this.party = null;
        this.ownerParty = null;

        this.lang = lang;

        this.cage1 = null;
        this.cage2 = null;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUuid());
    }

    /* LANG */
    public Configuration getLangMessage() {
        return Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(), "Messages/Lang/"+this.lang);
    }

    public Configuration getScoreboardMessage() {
        return Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(), "Messages/Scoreboards/"+this.lang);
    }

    public Configuration getHologramMessage() {
        return Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(), "Messages/Holograms/"+this.lang);
    }

    protected void typeMessage(boolean useFor, String msgSimple, List<String> lines){
        if (useFor){
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

    public void addWinsSolo() {
        this.setWinsSolo(this.getWinsSolo() + 1);
    }

    public void addWinsTeam() {
        this.setWinsTeam(this.getWinsTeam() + 1);
    }

    public void addKillsSolo() {
        this.setKillsSolo(this.getKillsSolo() + 1);
    }

    public void addKillsTeam() {
        this.setKillsTeam(this.getKillsTeam() + 1);
    }

    public void addDeathsSolo() {
        this.setDeathsSolo(this.getDeathsSolo() + 1);
    }

    public void addDeathsTeam() {
        this.setDeathsTeam(this.getDeathsTeam() + 1);
    }

    public void addGamesSolo() {
        this.setGamesSolo(this.getGamesSolo() + 1);
    }

    public void addGamesTeam() {
        this.setGamesTeam(this.getGamesTeam() + 1);
    }

    public void addShotsSolo() {
        this.setShotsSolo(this.getShotsSolo() + 1);
    }

    public void addShotsTeam() {
        this.setShotsTeam(this.getShotsTeam() + 1);
    }

    public void addHitsSolo() {
        this.setHitsSolo(this.getHitsSolo() + 1);
    }

    public void addHitsTeam() {
        this.setHitsTeam(this.getHitsTeam() + 1);
    }

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

    public void addKillsArena() {
        this.setKillsStreak(this.getKillsStreak() + 1);
    }

    public void removeCoins(int amount) {
        this.setCoins(Math.max((this.getCoins() - amount), 0));
    }

    protected String c(String cmd){
        return ChatColor.translateAlternateColorCodes('&', cmd);
    }

    public boolean isInArena() {
        return this.Arena != null;
    }

    public void playSound(String sound) {
        XSound.playSoundFromString(getPlayer(), XSound.valueOf(sound).toString());
    }

    public void addSpectating(){
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
        ItemStack[] emptyinv = new ItemStack[getPlayer().getInventory().getContents().length];
        getPlayer().getInventory().setContents(emptyinv);

        ItemStack[] emptyinv2 = new ItemStack[getPlayer().getInventory().getArmorContents().length];
        getPlayer().getInventory().setArmorContents(emptyinv2);
    }

    public void addCoins(int amount) {
        this.setCoins(this.getCoins() + amount);
    }
}
