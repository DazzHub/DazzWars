package com.dazzhub.skywars.Utils.vault;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class VaultAPI extends AbstractEconomy
{
    public boolean hasAccount(String name) {
        Player p = Bukkit.getPlayer(name);
        if (p == null) return false;

        GamePlayer gamePlayer = Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId());

        return gamePlayer != null;
    }

    public double getBalance(String name) {
        Player p = Bukkit.getPlayer(name);

        if (Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId()) == null) return 0.0;

        GamePlayer gamePlayer = Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null) return 0.0;

        return Main.getPlugin().getMoneyFormat(gamePlayer.getCoins());
    }

    public boolean has(String name, double amount) {
        return this.getBalance(name) >= Main.getPlugin().getMoneyFormat(amount);
    }

    public EconomyResponse withdrawPlayer(String name, double amount) {
        Player p = Bukkit.getPlayer(name);
        GamePlayer gamePlayer = Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null || !this.hasAccount(name)) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "The player does not have an account!");
        }

        double balance = this.getBalance(name);
        if (this.getBalance(name) < Main.getPlugin().getMoneyFormat(amount)) {
            return new EconomyResponse(0.0, balance, EconomyResponse.ResponseType.FAILURE, "The value is more than the player's balance!");
        }

        balance -= Main.getPlugin().getMoneyFormat(amount);

        gamePlayer.setCoins(balance);

        return new EconomyResponse(Main.getPlugin().getMoneyFormat(amount), Main.getPlugin().getMoneyFormat(balance), EconomyResponse.ResponseType.SUCCESS, "");
    }

    public EconomyResponse depositPlayer(String name, double amount) {
        Player p = Bukkit.getPlayer(name);
        GamePlayer gamePlayer = Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId());

        if (gamePlayer == null || !this.hasAccount(name)) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "The player does not have an account!");
        }

        if (Main.getPlugin().getMoneyFormat(amount) < 0.0) {
            return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Value is less than zero!");
        }

        gamePlayer.addCoins(amount);

        return new EconomyResponse(Main.getPlugin().getMoneyFormat(amount), 0.0, EconomyResponse.ResponseType.SUCCESS, "");
    }

    public boolean createPlayerAccount(String name) {
        return true;
    }

    public String format(double summ) {
        return String.valueOf(summ);
    }

    public boolean hasAccount(String name, String world) {
        return this.hasAccount(name);
    }

    public boolean has(String name, String world, double amount) {
        return this.has(name, amount);
    }

    public double getBalance(String name, String world) {
        return this.getBalance(name);
    }

    public EconomyResponse withdrawPlayer(String name, String world, double amount) {
        return this.withdrawPlayer(name, amount);
    }

    public EconomyResponse depositPlayer(String name, String world, double amount) {
        return this.depositPlayer(name, amount);
    }

    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    public EconomyResponse deleteBank(String s) {
        return null;
    }

    public EconomyResponse bankBalance(String s) {
        return null;
    }

    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    public boolean createPlayerAccount(String name, String world) {
        return this.createPlayerAccount(name);
    }

    public List<String> getBanks() {
        return null;
    }

    public boolean hasBankSupport() {
        return false;
    }

    public boolean isEnabled() {
        return Main.getPlugin() != null;
    }

    public String getName() {
        return "FurySkyBlock";
    }

    public int fractionalDigits() {
        return -1;
    }

    public String currencyNamePlural() {
        return "";
    }

    public String currencyNameSingular() {
        return "";
    }
}
