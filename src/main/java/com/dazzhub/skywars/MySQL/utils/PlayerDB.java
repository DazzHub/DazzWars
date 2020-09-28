package com.dazzhub.skywars.MySQL.utils;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.MySQL;
import com.dazzhub.skywars.MySQL.getPlayerDB;
import com.dazzhub.skywars.Utils.Base64;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerDB implements getPlayerDB {

    private Main main;

    public PlayerDB(Main main) {
        this.main = main;
    }

    @Override
    public void loadPlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);

        if (p == null){
            return;
        }
        if (!playerExist(p)) {
            String CageSolo = main.getSettings().getString("Default.Cage.Solo", "Default");
            String CageTeam = main.getSettings().getString("Default.Cage.Team", "Default");

            String WinEffectSolo = main.getSettings().getString("Default.WinEffect.Solo", "Default");
            String WinEffectTeam = main.getSettings().getString("Default.WinEffect.Team", "Default");

            String KillEffectSolo = main.getSettings().getString("Default.KillEffect.Solo", "Default");
            String KillEffectTeam = main.getSettings().getString("Default.KillEffect.Team", "Default");

            String TrailSolo = main.getSettings().getString("Default.Trail.Solo", "Default");
            String TrailTeam = main.getSettings().getString("Default.Trail.Team", "Default");

            String KillSoundSolo = main.getSettings().getString("Default.KillSound.Solo", "Default");
            String KillSoundTeam = main.getSettings().getString("Default.KillSound.Solo", "Default");

            String KitSolo = main.getSettings().getString("Default.Kit.Solo", "Default");
            String KitTeam = main.getSettings().getString("Default.Kit.Team", "Default");

            String Lang = main.getSettings().getString("Default.Lang", "Default");

            GamePlayer gamePlayer = new GamePlayer(
                    p.getUniqueId(), p.getName(),
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0,
                    0, 0, 0, 0, 0,
                    0, 0,
                    CageSolo, CageTeam,
                    WinEffectSolo, WinEffectTeam,
                    KillEffectSolo, KillEffectTeam,
                    KillSoundSolo, KillSoundTeam,
                    TrailSolo, TrailTeam,
                    KitSolo, KitTeam,
                    Lang);

            gamePlayer.getCagesSoloList().add(CageSolo);
            gamePlayer.getCagesTeamList().add(CageTeam);

            gamePlayer.getWinEffectsSoloList().add(WinEffectSolo);
            gamePlayer.getWinEffectsTeamList().add(WinEffectTeam);

            gamePlayer.getKillEffectsSoloList().add(KillEffectSolo);
            gamePlayer.getKillEffectsTeamList().add(KillEffectTeam);

            gamePlayer.getKillSoundsSoloList().add(KillSoundSolo);
            gamePlayer.getKillSoundsTeamList().add(KillSoundTeam);

            gamePlayer.getTrailsSoloList().add(TrailSolo);
            gamePlayer.getTrailsTeamList().add(TrailTeam);

            gamePlayer.getKitSoloList().add(KitSolo);
            gamePlayer.getKitTeamList().add(KitTeam);

            MySQL.update("INSERT INTO Statistics (" +
                    "UUID, " + "Name, " +
                    "BlockPlaced, " + "BlockBroken, " + "ItemEnchanted, " + "ItemCrafted, " + "DistanceWalked, " +
                    "Coins, " + "Souls, " +
                    "Lang" +

                    ") VALUES (" +

                    "'" + p.getUniqueId() + "', " + "'" + p.getName() + "', " +
                    "'0', " + "'0', " + "'0', " + "'0', " + "'0', " +
                    "'0', " + "'0', " +

                    "'" + Lang + "'" +
                    ");");

            addNewPlayerSolo(p, gamePlayer);
            addNewPlayerTeam(p, gamePlayer);

            main.getPlayerManager().addPlayer(p.getUniqueId(), gamePlayer);
        } else {
            alreadyExistPlayer(p);
        }
    }

    private void addNewPlayerSolo(Player p, GamePlayer gamePlayer) {
        String CageSolo = main.getSettings().getString("Default.Cage.Solo", "Default");
        String WinEffectSolo = main.getSettings().getString("Default.WinEffect.Solo", "Default");
        String KillEffectSolo = main.getSettings().getString("Default.KillEffect.Solo", "Default");
        String TrailSolo = main.getSettings().getString("Default.Trail.Solo", "Default");
        String KillSoundSolo = main.getSettings().getString("Default.KillSound.Solo", "Default");
        String KitSolo = main.getSettings().getString("Default.Kit.Solo", "Default");

        MySQL.update("INSERT INTO Statistics_Solo (" +
                "UUID, " + "Name, " +

                "WinsSolo, " +
                "KillsSolo, " +
                "DeathsSolo, " +
                "GamesSolo, " +
                "ShotsSolo, " +
                "HitsSolo, " +

                "CageSolo, " +
                "WinEffectSolo, " +
                "KillEffectSolo, " +
                "KillSoundSolo, " +
                "TrailSolo, " +
                "KitSolo, " +

                "CagesSolo, " +
                "WinEffectsSolo, " +
                "KillEffectsSolo, " +
                "KillSoundsSolo, " +
                "TrailsSolo, " +
                "KitsSolo" +

                ") VALUES (" +

                "'" + p.getUniqueId() + "', " + "'" + p.getName() + "', " +

                "'0', " +
                "'0', " +
                "'0', " +
                "'0', " +
                "'0', " +
                "'0', " +

                "'" + CageSolo + "', " +
                "'" + WinEffectSolo + "', " +
                "'" + KillEffectSolo + "', " +
                "'" + KillSoundSolo + "', " +
                "'" + TrailSolo + "', " +
                "'" + KitSolo + "', " +

                "'" + Base64.toBase64(gamePlayer.getCagesSoloList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getWinEffectsSoloList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getKillEffectsSoloList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getKillSoundsSoloList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getTrailsSoloList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getKitSoloList())+ "'" +

                ");");
    }

    private void addNewPlayerTeam(Player p, GamePlayer gamePlayer) {
        String CageTeam = main.getSettings().getString("Default.Cage.Team", "Default");
        String WinEffectTeam = main.getSettings().getString("Default.WinEffect.Team", "Default");
        String KillEffectTeam = main.getSettings().getString("Default.KillEffect.Team", "Default");
        String TrailTeam = main.getSettings().getString("Default.Trail.Team", "Default");
        String KillSoundTeam = main.getSettings().getString("Default.KillSound.Solo", "Default");
        String KitTeam = main.getSettings().getString("Default.Kit.Team", "Default");

        MySQL.update("INSERT INTO Statistics_Team (" +
                "UUID, " + "Name, " +

                "WinsTeam, " +
                "KillsTeam, " +
                "DeathsTeam, " +
                "GamesTeam, " +
                "ShotsTeam, " +
                "HitsTeam, " +

                "CageTeam, " +
                "WinEffectTeam, " +
                "KillEffectTeam, " +
                "KillSoundTeam, " +
                "TrailTeam, " +
                "KitTeam, " +

                "CagesTeam, " +
                "WinEffectsTeam, " +
                "KillEffectsTeam, " +
                "KillSoundsTeam, " +
                "TrailsTeam, " +
                "KitsTeam" +

                ") VALUES (" +

                "'" + p.getUniqueId() + "', " + "'" + p.getName() + "', " +
                "'0', " +
                "'0', " +
                "'0', " +
                "'0', " +
                "'0', " +
                "'0', " +

                "'" + CageTeam + "', " +
                "'" + WinEffectTeam + "', " +
                "'" + KillEffectTeam + "', " +
                "'" + KillSoundTeam + "', " +
                "'" + TrailTeam + "', " +
                "'" + KitTeam + "', " +

                "'" + Base64.toBase64(gamePlayer.getCagesTeamList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getWinEffectsTeamList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getKillEffectsTeamList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getKillSoundsTeamList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getTrailsTeamList()) + "', " +
                "'" + Base64.toBase64(gamePlayer.getKitTeamList()) + "'" +

                ");");
    }

    private void alreadyExistPlayer(Player p){
        GamePlayer gamePlayer = new GamePlayer(
                p.getUniqueId(), p.getName(),
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0,
                0, 0, 0, 0, 0,
                0, 0,
                "", "",
                "", "",
                "", "",
                "", "",
                "", "",
                "", "",
                "");

        ResultSet infoResult;
        ResultSet infoResult_solo;
        ResultSet infoResult_team;

        try {
            infoResult = MySQL.query("SELECT * FROM Statistics WHERE UUID='" + p.getUniqueId().toString() + "'");
            infoResult_solo = MySQL.query("SELECT * FROM Statistics_Solo WHERE UUID='" + p.getUniqueId().toString() + "'");
            infoResult_team = MySQL.query("SELECT * FROM Statistics_Team WHERE UUID='" + p.getUniqueId().toString() + "'");
        } catch (SQLException e) {
            infoResult = null;
            infoResult_solo = null;
            infoResult_team = null;
            e.printStackTrace();
        }

        try {
            if (infoResult != null && infoResult.next()) {
                gamePlayer.setBlockPlaced(infoResult.getInt("BlockPlaced"));
                gamePlayer.setBlockBroken(infoResult.getInt("BlockBroken"));
                gamePlayer.setItemsEnchanted(infoResult.getInt("ItemEnchanted"));
                gamePlayer.setItemsCrafted(infoResult.getInt("ItemCrafted"));
                gamePlayer.setDistanceWalked(infoResult.getInt("DistanceWalked"));
                gamePlayer.setCoins(infoResult.getInt("Coins"));
                gamePlayer.setSouls(infoResult.getInt("Souls"));
                gamePlayer.setLang(infoResult.getString("Lang"));
            }

            if (infoResult_solo != null && infoResult_solo.next()) {
                gamePlayer.setWinsSolo(infoResult_solo.getInt("WinsSolo"));
                gamePlayer.setKillsSolo(infoResult_solo.getInt("KillsSolo"));
                gamePlayer.setDeathsSolo(infoResult_solo.getInt("DeathsSolo"));
                gamePlayer.setGamesSolo(infoResult_solo.getInt("GamesSolo"));
                gamePlayer.setShotsSolo(infoResult_solo.getInt("ShotsSolo"));
                gamePlayer.setHitsSolo(infoResult_solo.getInt("HitsSolo"));
                gamePlayer.setCageSolo(infoResult_solo.getString("CageSolo"));
                gamePlayer.setWinEffectSolo(infoResult_solo.getString("WinEffectSolo"));
                gamePlayer.setKillEffectSolo(infoResult_solo.getString("KillEffectSolo"));
                gamePlayer.setKillSoundSolo(infoResult_solo.getString("KillSoundSolo"));
                gamePlayer.setTrailSolo(infoResult_solo.getString("TrailSolo"));
                gamePlayer.setKitSolo(infoResult_solo.getString("KitSolo"));
                gamePlayer.setCagesSoloList((List<String>) Base64.fromBase64(infoResult_solo.getString("CagesSolo")));
                gamePlayer.setWinEffectsSoloList((List<String>) Base64.fromBase64(infoResult_solo.getString("WinEffectsSolo")));
                gamePlayer.setKillEffectsSoloList((List<String>) Base64.fromBase64(infoResult_solo.getString("KillEffectsSolo")));
                gamePlayer.setKillSoundsSoloList((List<String>) Base64.fromBase64(infoResult_solo.getString("KillSoundsSolo")));
                gamePlayer.setTrailsSoloList((List<String>) Base64.fromBase64(infoResult_solo.getString("TrailsSolo")));
                gamePlayer.setKitSoloList((List<String>) Base64.fromBase64(infoResult_solo.getString("KitsSolo")));
            }

            if (infoResult_team != null && infoResult_team.next()) {
                gamePlayer.setWinsTeam(infoResult_team.getInt("WinsTeam"));
                gamePlayer.setKillsTeam(infoResult_team.getInt("KillsTeam"));
                gamePlayer.setDeathsTeam(infoResult_team.getInt("DeathsTeam"));
                gamePlayer.setGamesTeam(infoResult_team.getInt("GamesTeam"));
                gamePlayer.setShotsTeam(infoResult_team.getInt("ShotsTeam"));
                gamePlayer.setHitsTeam(infoResult_team.getInt("HitsTeam"));
                gamePlayer.setCageTeam(infoResult_team.getString("CageTeam"));
                gamePlayer.setWinEffectTeam(infoResult_team.getString("WinEffectTeam"));
                gamePlayer.setKillEffectTeam(infoResult_team.getString("KillEffectTeam"));
                gamePlayer.setKillSoundTeam(infoResult_team.getString("KillSoundTeam"));
                gamePlayer.setTrailTeam(infoResult_team.getString("TrailTeam"));
                gamePlayer.setKitTeam(infoResult_team.getString("KitTeam"));
                gamePlayer.setCagesTeamList((List<String>) Base64.fromBase64(infoResult_team.getString("CagesTeam")));
                gamePlayer.setWinEffectsTeamList((List<String>) Base64.fromBase64(infoResult_team.getString("WinEffectsTeam")));
                gamePlayer.setKillEffectsTeamList((List<String>) Base64.fromBase64(infoResult_team.getString("KillEffectsTeam")));
                gamePlayer.setKillSoundsTeamList((List<String>) Base64.fromBase64(infoResult_team.getString("KillSoundsTeam")));
                gamePlayer.setTrailsTeamList((List<String>) Base64.fromBase64(infoResult_team.getString("TrailsTeam")));
                gamePlayer.setKitTeamList((List<String>) Base64.fromBase64(infoResult_team.getString("KitsTeam")));

            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        main.getPlayerManager().addPlayer(p.getUniqueId(), gamePlayer);
    }

    @Override
    public void savePlayer(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);

        if (p == null){
            return;
        }

        if (playerExist(p)) {
            GamePlayer gamePlayer = main.getPlayerManager().getPlayer(p.getUniqueId());

            MySQL.update("UPDATE Statistics SET " +
                    "BlockPlaced='" + gamePlayer.getBlockPlaced() + "', " +
                    "BlockBroken='" + gamePlayer.getBlockBroken() + "', " +
                    "ItemEnchanted='" + gamePlayer.getItemsEnchanted() + "', " +
                    "ItemCrafted='" + gamePlayer.getItemsCrafted() + "', " +
                    "DistanceWalked='" + gamePlayer.getDistanceWalked() + "', " +
                    "Coins='" + gamePlayer.getCoins() + "', " +
                    "Souls='" + gamePlayer.getSouls() + "', " +

                    "Lang='" + gamePlayer.getLang() + "' " +

                    "WHERE UUID='" + p.getUniqueId() + "'"
            );

            MySQL.update("UPDATE Statistics_Solo SET " +
                    "WinsSolo='" + gamePlayer.getWinsSolo() + "', " +
                    "KillsSolo='" + gamePlayer.getKillsSolo() + "', " +
                    "DeathsSolo='" + gamePlayer.getDeathsSolo() + "', " +
                    "GamesSolo='" + gamePlayer.getGamesSolo() + "', " +
                    "ShotsSolo='" + gamePlayer.getShotsSolo() + "', " +
                    "HitsSolo='" + gamePlayer.getHitsSolo() + "', " +

                    "CageSolo='" + gamePlayer.getCageSolo() + "', " +
                    "WinEffectSolo='" + gamePlayer.getWinEffectSolo() + "', " +
                    "KillEffectSolo='" + gamePlayer.getKillEffectSolo() + "', " +
                    "KillSoundSolo='" + gamePlayer.getKillSoundSolo() + "', " +
                    "TrailSolo='" + gamePlayer.getTrailSolo() + "', " +
                    "KitSolo='" + gamePlayer.getKitSolo() + "', " +

                    "CagesSolo='" + Base64.toBase64(gamePlayer.getCagesSoloList()) + "', " +
                    "WinEffectsSolo='" + Base64.toBase64(gamePlayer.getWinEffectsSoloList()) + "', " +
                    "KillEffectsSolo='" + Base64.toBase64(gamePlayer.getKillEffectsSoloList()) + "', " +
                    "KillSoundsSolo='" + Base64.toBase64(gamePlayer.getKillSoundsSoloList()) + "', " +
                    "TrailsSolo='" + Base64.toBase64(gamePlayer.getTrailsSoloList()) + "', " +
                    "KitsSolo='" + Base64.toBase64(gamePlayer.getKitSoloList()) + "' " +


                    "WHERE UUID='" + p.getUniqueId() + "'"
            );

            MySQL.update("UPDATE Statistics_Team SET " +
                    "WinsTeam='" + gamePlayer.getWinsTeam() + "', " +
                    "KillsTeam='" + gamePlayer.getKillsTeam() + "', " +
                    "DeathsTeam='" + gamePlayer.getDeathsTeam() + "', " +
                    "GamesTeam='" + gamePlayer.getGamesTeam() + "', " +
                    "ShotsTeam='" + gamePlayer.getShotsTeam() + "', " +
                    "HitsTeam='" + gamePlayer.getHitsTeam() + "', " +

                    "CageTeam='" + gamePlayer.getCageTeam() + "', " +
                    "WinEffectTeam='" + gamePlayer.getWinEffectTeam() + "', " +
                    "KillEffectTeam='" + gamePlayer.getKillEffectTeam() + "', " +
                    "KillSoundTeam='" + gamePlayer.getKillSoundTeam() + "', " +
                    "TrailTeam='" + gamePlayer.getTrailTeam() + "', " +
                    "KitTeam='" + gamePlayer.getKitTeam() + "', " +

                    "CagesTeam='" + Base64.toBase64(gamePlayer.getCagesTeamList()) + "', " +
                    "WinEffectsTeam='" + Base64.toBase64(gamePlayer.getWinEffectsTeamList()) + "', " +
                    "KillEffectsTeam='" + Base64.toBase64(gamePlayer.getKillEffectsTeamList()) + "', " +
                    "KillSoundsTeam='" + Base64.toBase64(gamePlayer.getKillSoundsTeamList()) + "', " +
                    "TrailsTeam='" + Base64.toBase64(gamePlayer.getTrailsTeamList()) + "', " +
                    "KitsTeam='" + Base64.toBase64(gamePlayer.getKitTeamList()) + "' " +

                    "WHERE UUID='" + p.getUniqueId() + "'"
            );
            main.getPlayerManager().removePlayer(p.getUniqueId());
        }
    }

    @Override
    public void loadMySQL() {
        Configuration config = this.main.getConfigUtils().getConfig(this.main, "Settings");
        MySQL.host = config.getString("MySQL.hostname");
        MySQL.port = config.getInt("MySQL.port");
        MySQL.database = config.getString("MySQL.database");
        MySQL.username = config.getString("MySQL.username");
        MySQL.password = config.getString("MySQL.password");
        MySQL.isEnabled = config.getBoolean("MySQL.enabled");
        MySQL.connect(this.main);
        if (config.getBoolean("MySQL.enabled")) {
            MySQL.update("CREATE TABLE IF NOT EXISTS Statistics (`ID` INT PRIMARY KEY AUTO_INCREMENT, " +
                "`UUID` VARCHAR(100), " +
                "`Name` VARCHAR(100), " +

                "`BlockPlaced` Integer, " +
                "`BlockBroken` Integer, " +
                "`ItemEnchanted` Integer, " +
                "`ItemCrafted` Integer, " +
                "`DistanceWalked` Integer, " +

                "`Coins` Integer, " +
                "`Souls` Integer, " +

                "`Lang` VARCHAR(100)" +
            ")");

            MySQL.update("CREATE TABLE IF NOT EXISTS Statistics_Solo (`ID` INT PRIMARY KEY AUTO_INCREMENT, " +
                "`UUID` VARCHAR(100), " +
                "`Name` VARCHAR(100), " +

                "`WinsSolo` Integer, " +
                "`KillsSolo` Integer, " +
                "`DeathsSolo` Integer, " +
                "`GamesSolo` Integer, " +
                "`ShotsSolo` Integer, " +
                "`HitsSolo` Integer, " +

                "`CageSolo` TEXT, " +
                "`WinEffectSolo` TEXT, " +
                "`KillEffectSolo` TEXT, " +
                "`KillSoundSolo` TEXT, " +
                "`TrailSolo` TEXT, " +
                "`KitSolo` TEXT, " +

                "`CagesSolo` TEXT, " +
                "`WinEffectsSolo` TEXT, " +
                "`KillEffectsSolo` TEXT, " +
                "`KillSoundsSolo` TEXT, " +
                "`TrailsSolo` TEXT, " +
                "`KitsSolo` TEXT" +

             ")");

            MySQL.update("CREATE TABLE IF NOT EXISTS Statistics_Team (`ID` INT PRIMARY KEY AUTO_INCREMENT, " +
                "`UUID` VARCHAR(100), " +
                "`Name` VARCHAR(100), " +

                "`WinsTeam` Integer, " +
                "`KillsTeam` Integer, " +
                "`DeathsTeam` Integer, " +
                "`GamesTeam` Integer, " +
                "`ShotsTeam` Integer, " +
                "`HitsTeam` Integer, " +

                "`CageTeam` TEXT, " +
                "`WinEffectTeam` TEXT, " +
                "`KillEffectTeam` TEXT, " +
                "`KillSoundTeam` TEXT, " +
                "`TrailTeam` TEXT, " +
                "`KitTeam` TEXT, " +

                "`CagesTeam` TEXT, " +
                "`WinEffectsTeam` TEXT, " +
                "`KillEffectsTeam` TEXT, " +
                "`KillSoundsTeam` TEXT, " +
                "`TrailsTeam` TEXT, " +
                "`KitsTeam` TEXT" +
            ")");
        } else {
            MySQL.update("CREATE TABLE IF NOT EXISTS Statistics (" +
                "`UUID` VARCHAR(100), " +
                "`Name` VARCHAR(100), " +

                "`BlockPlaced` Integer, " +
                "`BlockBroken` Integer, " +
                "`ItemEnchanted` Integer, " +
                "`ItemCrafted` Integer, " +
                "`DistanceWalked` Integer, " +

                "`Coins` Integer, " +
                "`Souls` Integer, " +

                "`Lang` VARCHAR(100)" +
            ")");

            MySQL.update("CREATE TABLE IF NOT EXISTS Statistics_Solo (" +
                "`UUID` VARCHAR(100), " +
                "`Name` VARCHAR(100), " +

                "`WinsSolo` Integer, " +
                "`KillsSolo` Integer, " +
                "`DeathsSolo` Integer, " +
                "`GamesSolo` Integer, " +
                "`ShotsSolo` Integer, " +
                "`HitsSolo` Integer, " +

                "`CageSolo` TEXT, " +
                "`WinEffectSolo` TEXT, " +
                "`KillEffectSolo` TEXT, " +
                "`KillSoundSolo` TEXT, " +
                "`TrailSolo` TEXT, " +
                "`KitSolo` TEXT, " +

                "`CagesSolo` TEXT, " +
                "`WinEffectsSolo` TEXT, " +
                "`KillEffectsSolo` TEXT, " +
                "`KillSoundsSolo` TEXT, " +
                "`TrailsSolo` TEXT, " +
                "`KitsSolo` TEXT" +

            ")");

            MySQL.update("CREATE TABLE IF NOT EXISTS Statistics_Team (" +
                "`UUID` VARCHAR(100), " +
                "`Name` VARCHAR(100), " +

                "`WinsTeam` Integer, " +
                "`KillsTeam` Integer, " +
                "`DeathsTeam` Integer, " +
                "`GamesTeam` Integer, " +
                "`ShotsTeam` Integer, " +
                "`HitsTeam` Integer, " +

                "`CageTeam` TEXT, " +
                "`WinEffectTeam` TEXT, " +
                "`KillEffectTeam` TEXT, " +
                "`KillSoundTeam` TEXT, " +
                "`TrailTeam` TEXT, " +
                "`KitTeam` TEXT, " +

                "`CagesTeam` TEXT, " +
                "`WinEffectsTeam` TEXT, " +
                "`KillEffectsTeam` TEXT, " +
                "`KillSoundsTeam` TEXT, " +
                "`TrailsTeam` TEXT, " +
                "`KitsTeam` TEXT" +
            ")");
        }
    }

    @Override
    public String[] TopKillsSolo(int top) {
        String[] result = new String[top];
        try {

            ResultSet infoResult = MySQL.query("SELECT `Name`, `KillsSolo` FROM `Statistics_Solo` ORDER BY `KillsSolo` DESC LIMIT "+top);
            assert infoResult != null;
            for (int i = 0; i < top; ++i) {
                if (infoResult.next()) {
                    result[i] = infoResult.getString("Name") + ":" + infoResult.getInt("KillsSolo");
                } else {
                    result[i] = "error";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String[] TopKillsTeam(int top) {
        String[] result = new String[top];
        try {

            ResultSet infoResult = MySQL.query("SELECT `Name`, `KillsTeam` FROM `Statistics_Team` ORDER BY `KILLS` DESC LIMIT "+top);
            assert infoResult != null;
            for (int i = 0; i < top; ++i) {
                if (infoResult.next()) {
                    result[i] = infoResult.getString("Name") + ":" + infoResult.getInt("KillsTeam");
                } else {
                    result[i] = "error";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String[] TopDeathsSolo(int top) {
        String[] result = new String[top];
        try {

            ResultSet infoResult = MySQL.query("SELECT `Name`, `DeathsSolo` FROM `Statistics_Solo` ORDER BY `DeathsSolo` DESC LIMIT "+top);
            assert infoResult != null;
            for (int i = 0; i < top; ++i) {
                if (infoResult.next()) {
                    result[i] = infoResult.getString("Name") + ":" + infoResult.getInt("DeathsSolo");
                } else {
                    result[i] = "error";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String[] TopDeathsTeam(int top) {
        String[] result = new String[top];
        try {

            ResultSet infoResult = MySQL.query("SELECT `Name`, `DeathsTeam` FROM `Statistics_Team` ORDER BY `DeathsTeam` DESC LIMIT "+top);
            assert infoResult != null;
            for (int i = 0; i < top; ++i) {
                if (infoResult.next()) {
                    result[i] = infoResult.getString("Name") + ":" + infoResult.getInt("DeathsTeam");
                } else {
                    result[i] = "error";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String[] TopWinsSolo(int top) {
        String[] result = new String[top];
        try {

            ResultSet infoResult = MySQL.query("SELECT `Name`, `WinsSolo` FROM `Statistics_Solo` ORDER BY `WinsSolo` DESC LIMIT "+top);
            assert infoResult != null;
            for (int i = 0; i < top; ++i) {
                if (infoResult.next()) {
                    result[i] = infoResult.getString("Name") + ":" + infoResult.getInt("WinsSolo");
                } else {
                    result[i] = "error";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String[] TopWinsTeam(int top) {
        String[] result = new String[top];
        try {

            ResultSet infoResult = MySQL.query("SELECT `Name`, `WinsTeam` FROM `Statistics_Team` ORDER BY `WinsTeam` DESC LIMIT "+top);
            assert infoResult != null;
            for (int i = 0; i < top; ++i) {
                if (infoResult.next()) {
                    result[i] = infoResult.getString("Name") + ":" + infoResult.getInt("WinsTeam");
                } else {
                    result[i] = "error";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean playerExist(Player p) {
        try {
            ResultSet rs = MySQL.query("SELECT * FROM Statistics WHERE UUID='" + p.getUniqueId().toString() + "'");
            assert rs != null;
            return rs.next() && rs.getString("UUID") != null;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
