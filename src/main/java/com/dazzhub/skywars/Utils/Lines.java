package com.dazzhub.skywars.Utils;

import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class Lines {

    private Main main;

    public Lines(Main main) {
        this.main = main;
    }

    public void loadConfig() {

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            for (String lang : main.getSettings().getStringList("ListLanguage")) {

                addDefaultLang("Messages.inGame", "&c&l✘ &fArena in game", lang);
                addDefaultLang("Messages.Full", "&c&l✘ &fArena is full", lang);
                addDefaultLang("Messages.alredyInGame", "&c&l✘ &fYou are already at game", lang);
                addDefaultLang("Messages.JoinMessage", "&8» &9%player% &fhas joined &8(&2%playing%&8/&2%max%&8)&f!", lang);
                addDefaultLang("Messages.LeaveMessage", "&8» &9%player%&f has quit!", lang);
                addDefaultLang("Messages.JoinSpectator", "&8» &fYou joined spectator mode", lang);
                addDefaultLang("Messages.LeaveSpectator", "&8» &fYou left spectator mode", lang);
                addDefaultLang("Messages.Arena-Not-Available", "&c&l✘ &fYou can''t join the arena now", lang);
                addDefaultLang("Messages.NoUseChat", "&e&l⚠&e You can''t chat until the game starts", lang);
                addDefaultLang("Messages.forceStartMinPlayer", "&e&l⚠&e Not enough players to force the arena to start", lang);
                addDefaultLang("Messages.EndTime", "&e&l⚠&e Game time over", lang);
                addDefaultLang("Messages.item-deny", "&e&l⚠&e You do not have permission", lang);
                addDefaultLang("Messages.menu-deny", "&e&l⚠&e You do not have permission", lang);
                addDefaultLang("Messages.GiveCoins", "&d&l➠ &f+%coins% coins", lang);
                addDefaultLang("Messages.InsufficientCoins", "&c&l✘ &fYou need %coins% coins, to be able to buy it", lang);
                addDefaultLang("Messages.Language.change", "&a&l✔ &fYour language was translated into English", lang);
                addDefaultLang("Messages.Language.error", "&c&l✘ &fThe selected language is not in the list", lang);

                addDefaultLang("Messages.Kit.Received", "&8» &fThe kit %kit% was received", lang);
                addDefaultLang("Messages.Kit.Selected", "&a&l✔&f You choose the kit: &9%kit%", lang);
                addDefaultLang("Messages.Kit.Buy", "&a&l✔&f You bought the kit: &9%cage%", lang);

                addDefaultLang("Messages.Cage.Selected", "&a&l✔&f You choose the cage: &9%cage%", lang);
                addDefaultLang("Messages.Cage.Buy", "&a&l✔&f You bought the cage: &9%cage%", lang);

                addDefaultLang("Messages.WinEffect.Selected", "&a&l✔&f You choose the effect: &9%win%", lang);
                addDefaultLang("Messages.WinEffect.Buy", "&a&l✔&f You bought the effect: &9%win%", lang);

                addDefaultLang("Messages.KillEffect.Selected", "&a&l✔&f You choose the effect: &9%kill%", lang);
                addDefaultLang("Messages.KillEffect.Buy", "&a&l✔&f You bought the effect: &9%kill%", lang);

                addDefaultLang("Messages.TrailEffect.Selected", "&a&l✔&f You choose the effect: &9%trail%", lang);
                addDefaultLang("Messages.TrailEffect.Buy", "&a&l✔&f You bought the effect: &9%trail%", lang);

                addDefaultLang("Messages.JoinTitle.Info", "&b&lSkyWars;&fModo %mode%", lang);
                addDefaultLang("Messages.JoinTitle.Fade", 10, lang);
                addDefaultLang("Messages.JoinTitle.Stay", 40, lang);
                addDefaultLang("Messages.JoinTitle.Out", 10, lang);

                addDefaultLang("Messages.Sounds.Join.enable", true, lang);
                addDefaultLang("Messages.Sounds.Join.sound", "BLOCK_NOTE_BLOCK_PLING", lang);
                addDefaultLang("Messages.Sounds.Cancelled", "BLOCK_NOTE_BLOCK_PLING", lang);
                addDefaultLang("Messages.Sounds.Starting.5", "UI_BUTTON_CLICK", lang);
                addDefaultLang("Messages.Sounds.Starting.4", "UI_BUTTON_CLICK", lang);
                addDefaultLang("Messages.Sounds.Starting.3", "UI_BUTTON_CLICK", lang);
                addDefaultLang("Messages.Sounds.Starting.2", "UI_BUTTON_CLICK", lang);
                addDefaultLang("Messages.Sounds.Starting.1", "UI_BUTTON_CLICK", lang);

                addDefaultLang("Messages.Timer.Cancelled", "&c&l✘ &fNot enought players.", lang);
                addDefaultLang("Messages.Timer.Starting.15", "&c&l➥ &fThe game starts in &9%seconds%&f seconds!", lang);
                addDefaultLang("Messages.Timer.Starting.10", "&c&l➥ &fThe game starts in &9%seconds%&f seconds!", lang);
                addDefaultLang("Messages.Timer.Starting.5", "&c&l➥ &fThe game starts in &9%seconds%&f seconds!", lang);
                addDefaultLang("Messages.Timer.Starting.4", "&c&l➥ &fThe game starts in &9%seconds%&f seconds!", lang);
                addDefaultLang("Messages.Timer.Starting.3", "&c&l➥ &fThe game starts in &9%seconds%&f seconds!", lang);
                addDefaultLang("Messages.Timer.Starting.2", "&c&l➥ &fThe game starts in &9%seconds%&f seconds!", lang);
                addDefaultLang("Messages.Timer.Starting.1", "&c&l➥ &fThe game starts in &9%seconds%&f seconds!", lang);

                addDefaultLang("Messages.Title.Fade", 10, lang);
                addDefaultLang("Messages.Title.Stay", 40, lang);
                addDefaultLang("Messages.Title.Out", 10, lang);
                addDefaultLang("Messages.Title.Starting.15", "&a%seconds%&e seconds;&cFor start", lang);
                addDefaultLang("Messages.Title.Starting.10", "&e%seconds% seconds;&cChoose a kit!", lang);
                addDefaultLang("Messages.Title.Starting.5", "&c%seconds%;&ePrepare to fight!", lang);
                addDefaultLang("Messages.Title.Starting.4", "&c%seconds%;&ePrepare to fight!", lang);
                addDefaultLang("Messages.Title.Starting.3", "&c%seconds%;&ePrepare to fight!", lang);
                addDefaultLang("Messages.Title.Starting.2", "&c%seconds%;&ePrepare to fight!", lang);
                addDefaultLang("Messages.Title.Starting.1", "&c%seconds%;&ePrepare to fight!", lang);

                addDefaultLang("Messages.InfoGame", Arrays.asList("%center%&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "%center%&9&lSkyWars", "", "%center%&fChest type &8» &a%chest%", "%center%&fType of time &8» &6%time%", "%center%&fKind of hearts &8» &e%heart%", "%center%&fType of Events &8» &9%event%", "%center%&fType of scenario &8» &d%scenario%", "", "%center%&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "&6Teaming is not allowed on Solo mode!", ""), lang);

                addDefaultLang("Messages.LuckTitle.Fade", 10, lang);
                addDefaultLang("Messages.LuckTitle.Stay", 40, lang);
                addDefaultLang("Messages.LuckTitle.Out", 10, lang);
                addDefaultLang("Messages.LuckTitle.Info", "&cTo fight!;&eLuck ㋡", lang);

                addDefaultLang("Messages.WinnerGame", Arrays.asList("", "%center%&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "%center%&9&lSkyWars", "", "%center%&eWinner &7- %winner%", "", "%center%&e1st Killer&7 - %player1% &7- %kills1%", "%center%&62nd Killer&7 - %player2% &7- %kills2%", "%center%&c3rd Killer&7 - %player3% &7- %kills3%", "%center%&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"), lang);

                addDefaultLang("Messages.WantAgain", "%s &7● %s &7● %s", lang);

                addDefaultLang("Messages.ChatClick.play.display", "&6&lJUGAR", lang);
                addDefaultLang("Messages.ChatClick.play.hover", "&cCLICK HERE", lang);
                addDefaultLang("Messages.ChatClick.play.action", "cmd", lang);
                addDefaultLang("Messages.ChatClick.play.value", "/sw join again", lang);

                addDefaultLang("Messages.ChatClick.auto.display", "&6&lJUGAR", lang);
                addDefaultLang("Messages.ChatClick.auto.hover", "&cCLICK HERE", lang);
                addDefaultLang("Messages.ChatClick.auto.action", "cmd", lang);
                addDefaultLang("Messages.ChatClick.auto.value", "/sw join auto", lang);

                addDefaultLang("Messages.ChatClick.leave.display", "&b&lLEAVE", lang);
                addDefaultLang("Messages.ChatClick.leave.hover", "&cCLICK HERE", lang);
                addDefaultLang("Messages.ChatClick.leave.action", "cmd", lang);
                addDefaultLang("Messages.ChatClick.leave.value", "/leave", lang);

                addDefaultLang("Messages.WinnerTitle.Fade", 10, lang);
                addDefaultLang("Messages.WinnerTitle.Stay", 40, lang);
                addDefaultLang("Messages.WinnerTitle.Out", 10, lang);
                addDefaultLang("Messages.WinnerTitle.Info", "&e&lVICTORY!;&7You were the last man standing!", lang);

                addDefaultLang("Messages.RefillTitle.ChatAlert", "&e&l⚠&f Alert, all chests have been &brefill&f!", lang);
                addDefaultLang("Messages.RefillTitle.Info", "&9&lREFILL CHESTS;&e㋡", lang);
                addDefaultLang("Messages.RefillTitle.Fade", 10, lang);
                addDefaultLang("Messages.RefillTitle.Stay", 40, lang);
                addDefaultLang("Messages.RefillTitle.Out", 10, lang);

                addDefaultLang("Messages.TypeEvent.Dragon.Starting.240", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Dragon.Starting.120", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Dragon.Starting.60", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Dragon.Starting.10", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Dragon.Starting.5", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Dragon.Starting.4", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Dragon.Starting.3", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Dragon.Starting.2", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Dragon.Starting.1", "&e&l⚠&7 The dragon appears in &9%seconds%", lang);

                addDefaultLang("Messages.TypeEvent.Border.Starting.120", "&e&l⚠&7 The border will start to shrink &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Border.Starting.60", "&e&l⚠&7 The border will start to shrink &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Border.Starting.10", "&e&l⚠&7 The border will start to shrink &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Border.Starting.5", "&e&l⚠&7 The border will start to shrink &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Border.Starting.4", "&e&l⚠&7 The border will start to shrink &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Border.Starting.3", "&e&l⚠&7 The border will start to shrink &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Border.Starting.2", "&e&l⚠&7 The border will start to shrink &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Border.Starting.1", "&e&l⚠&7 The border will start to shrink &5%seconds%", lang);

                addDefaultLang("Messages.TypeEvent.DropParty.Starting.200", "&e&l⚠&7 The dropParty start in &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.DropParty.Starting.60", "&e&l⚠&7 The dropParty start in &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.DropParty.Starting.10", "&e&l⚠&7 The dropParty start in &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.DropParty.Starting.5", "&e&l⚠&7 The dropParty start in &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.DropParty.Starting.4", "&e&l⚠&7 The dropParty start in &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.DropParty.Starting.3", "&e&l⚠&7 The dropParty start in &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.DropParty.Starting.2", "&e&l⚠&7 The dropParty start in &5%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.DropParty.Starting.1", "&e&l⚠&7 The dropParty start in &5%seconds%", lang);

                addDefaultLang("Messages.TypeEvent.Storm.Starting.120", "&e&l⚠&7 The Storm start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Storm.Starting.60", "&e&l⚠&7 The Storm start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Storm.Starting.10", "&e&l⚠&7 The Storm start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Storm.Starting.5", "&e&l⚠&7 The Storm start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Storm.Starting.4", "&e&l⚠&7 The Storm start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Storm.Starting.3", "&e&l⚠&7 The Storm start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Storm.Starting.2", "&e&l⚠&7 The Storm start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.Storm.Starting.1", "&e&l⚠&7 The Storm start in &9%seconds%", lang);

                addDefaultLang("Messages.TypeEvent.TntFall.Starting.200", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.TntFall.Starting.120", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.TntFall.Starting.60", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.TntFall.Starting.10", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.TntFall.Starting.5", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.TntFall.Starting.4", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.TntFall.Starting.3", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.TntFall.Starting.2", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);
                addDefaultLang("Messages.TypeEvent.TntFall.Starting.1", "&e&l⚠&7 The TnTFall start in &9%seconds%", lang);

                addDefaultLang("Messages.TypeVote.VoteFor", "&8» &f%player% voted for &a%vote% (%votes%)", lang);
                addDefaultLang("Messages.TypeVote.AlreadyVote", "&8» &fYou already voted", lang);
                addDefaultLang("Messages.TypeVote.Chest.basic", "Basic", lang);
                addDefaultLang("Messages.TypeVote.Chest.normal", "Normal", lang);
                addDefaultLang("Messages.TypeVote.Chest.op", "OP", lang);
                addDefaultLang("Messages.TypeVote.Time.day", "Day", lang);
                addDefaultLang("Messages.TypeVote.Time.sunset", "Sunset", lang);
                addDefaultLang("Messages.TypeVote.Time.night", "Night", lang);
                addDefaultLang("Messages.TypeVote.Heart.10h", "10 ❤", lang);
                addDefaultLang("Messages.TypeVote.Heart.20h", "20 ❤", lang);
                addDefaultLang("Messages.TypeVote.Heart.30h", "30 ❤", lang);
                addDefaultLang("Messages.TypeVote.Events.border", "Border", lang);
                addDefaultLang("Messages.TypeVote.Events.dragon", "Dragon", lang);
                addDefaultLang("Messages.TypeVote.Events.dropparty", "DropParty", lang);
                addDefaultLang("Messages.TypeVote.Events.storm", "Storm", lang);
                addDefaultLang("Messages.TypeVote.Events.tntfall", "TntFall", lang);
                addDefaultLang("Messages.TypeVote.Scenario.noclean", "NoClean", lang);
                addDefaultLang("Messages.TypeVote.Scenario.nofall", "NoFall", lang);
                addDefaultLang("Messages.TypeVote.Scenario.noprojectile", "NoProjectile", lang);
                addDefaultLang("Messages.TypeVote.none", "None", lang);

                addDefaultLang("Messages.notifyVotes.Projectile.enabled", true, lang);
                addDefaultLang("Messages.notifyVotes.Projectile.isProjectile", "&e&l⚠&f The scenario Proj is enabled!", lang);

                addDefaultLang("Messages.ScoreBoard.Waiting", "Waiting", lang);
                addDefaultLang("Messages.ScoreBoard.Refill", "Refill: %time%", lang);

                addDefaultLang("Messages.ScoreBoard.Border", "Border: %time%", lang);
                addDefaultLang("Messages.ScoreBoard.Dragon", "Dragon: %time%", lang);
                addDefaultLang("Messages.ScoreBoard.Party", "Party: %time%", lang);
                addDefaultLang("Messages.ScoreBoard.Storm", "Storm: %time%", lang);
                addDefaultLang("Messages.ScoreBoard.TNT", "TNT: %time%", lang);

                addDefaultLang("Messages.ScoreBoard.EndGame", "End of game", lang);
                addDefaultLang("Messages.ScoreBoard.None", "None", lang);
                addDefaultLang("Messages.ScoreBoard.Team.enabled", true, lang);
                addDefaultLang("Messages.ScoreBoard.Team.killCount", true, lang);
                addDefaultLang("Messages.ScoreBoard.Team.Spectator", "&7:&f", lang);
                addDefaultLang("Messages.ScoreBoard.Team.Game", "&c:&f", lang);
                addDefaultLang("Messages.ScoreBoard.Team.TeamsFriends", "&a:&f", lang);

                addDefaultLang("SoulWell.SoulsToOpen", 1, lang);
                addDefaultLang("SoulWell.InsufficientSouls", "&c&l✘ &fYou need %soul% souls, to use it", lang);
                addDefaultLang("SoulWell.NeedSouls", "&c&l✘ &fYou don't have enough Souls", lang);
                addDefaultLang("SoulWell.NoPermission", "&c&l✘ &fYou don''t have permission", lang);
                addDefaultLang("SoulWell.inUsing", "&c&l✘ &fSoulWell is in use, wait a minute", lang);
                addDefaultLang("SoulWell.Added", "+ %amount% Souls", lang);
                addDefaultLang("SoulWell.NameMenu", "&8&l&nUnlocking...", lang);
                addDefaultLang("SoulWell.StartScroll.Sound.enable", false, lang);
                addDefaultLang("SoulWell.StartScroll.Sound.type", "UI_BUTTON_CLICK", lang);
                addDefaultLang("SoulWell.StartScroll.Music.enable", true, lang);
                addDefaultLang("SoulWell.StartScroll.Music.namefile", "Scrolling.nbs", lang);

                addDefaultLang("SoulWell.EndScroll.Sound.enable", false, lang);
                addDefaultLang("SoulWell.EndScroll.Sound.type", "UI_BUTTON_CLICK", lang);
                addDefaultLang("SoulWell.EndScroll.FireWorks.enable", false, lang);
                addDefaultLang("SoulWell.EndScroll.Effects.enable", true, lang);
                addDefaultLang("SoulWell.EndScroll.Effects.type", "FLAME", lang);
                addDefaultLang("SoulWell.EndScroll.Announcement.enable", true, lang);
                addDefaultLang("SoulWell.EndScroll.Announcement.lines", Arrays.asList("%center%&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬","","%center%&eCongratulations, you have just won: &9%name%","%center%&eWith a rarity: &9%rarity%","","%center%&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"), lang);

                addDefaultLang("Messages.MenuSpectator.TITLE", "&8&l&nIn Game", lang);
                addDefaultLang("Messages.MenuSpectator.ROWS", 4, lang);
                addDefaultLang("Messages.MenuSpectator.Player.NAME", "&6&n%player%", lang);
                addDefaultLang("Messages.MenuSpectator.Player.DESCRIPTION", "", lang);

                addDefaultLang("Messages.MenuSpectator.Close.NAME", "&eClose", lang);
                addDefaultLang("Messages.MenuSpectator.Close.ICON-ITEM", "ENDER_PEARL", lang);
                addDefaultLang("Messages.MenuSpectator.Close.DATA-VALUE", 0, lang);
                addDefaultLang("Messages.MenuSpectator.Close.DESCRIPTION", Arrays.asList("&9▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "", "&fClick to &eclose", "", "&9▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"), lang);
                addDefaultLang("Messages.MenuSpectator.Close.POSITION-X", 5, lang);
                addDefaultLang("Messages.MenuSpectator.Close.POSITION-Y", 3, lang);

                addDefaultLang("Messages.Achievements.RewardsUse", true, lang);
                addDefaultLang("Messages.Achievements.UseAdvertising", true, lang);
                addDefaultLang("Messages.Achievements.FireWorks", false, lang);

                addDefaultLang("Messages.Achievements.Sound.Use", true, lang);
                addDefaultLang("Messages.Achievements.Sound.Type", "ORB_PICKUP", lang);

                addDefaultLang("Messages.Achievements.Title.Use", false, lang);
                addDefaultLang("Messages.Achievements.Title.Fade", 10, lang);
                addDefaultLang("Messages.Achievements.Title.Stay", 40, lang);
                addDefaultLang("Messages.Achievements.Title.Out", 10, lang);
                addDefaultLang("Messages.Achievements.Title.Info", "&e&lCONGRATULATIONS!;&7Challenge: %description%", lang);

                addDefaultLang("Messages.Achievements.Message.Use", true, lang);
                addDefaultLang("Messages.Achievements.Message.Announcement", Arrays.asList("%center%&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬", "", "%center%&eCongratulations you made the challenge: &9%description%","%center%&eThis is your reward: &9%typereward%","","%center%&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"), lang);

                addDefaultLang("Messages.DeathMessages.Player", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 &7was slain by &c%killer%&7[&f%k_kill_count%&7]", lang);
                addDefaultLang("Messages.DeathMessages.Void", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 fell into void", lang);
                addDefaultLang("Messages.DeathMessages.Fall", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 fell from the sky", lang);
                addDefaultLang("Messages.DeathMessages.Burned", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 was burned!", lang);
                addDefaultLang("Messages.DeathMessages.Explosion", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 exploded in battle", lang);
                addDefaultLang("Messages.DeathMessages.Lava", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 tried to swim in the lava", lang);
                addDefaultLang("Messages.DeathMessages.Water", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 drowned slowly", lang);
                addDefaultLang("Messages.DeathMessages.Suffocation", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 it suffocated and died slowly", lang);
                addDefaultLang("Messages.DeathMessages.Arrow", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 was shot by &c%killer%&7[&f%k_kill_count%&7]&7 from &9%blocks%&7 blocks.", lang);
                addDefaultLang("Messages.DeathMessages.EnderPearl", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 was shot (using EnderPearl) by &c%killer%&7[&f%k_kill_count%&7] from &9%blocks%&7 blocks.", lang);
                addDefaultLang("Messages.DeathMessages.Lightning", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 was slain by the storm", lang);
                addDefaultLang("Messages.DeathMessages.UnknownCause", "&f&l➥ &9%player%&7[&f%p_kill_count%&7]&7 did by unknown cause", lang);

                addDefaultLang("Messages.Party.InvalidArgument", "&d&lPARTY &f➠ &cError", lang);
                addDefaultLang("Messages.Party.TargetNoExist", "&d&lPARTY &f➠ &9%target%&f invalid player", lang);
                addDefaultLang("Messages.Party.Create", "&d&lPARTY &f➠ You successfully created the party", lang);
                addDefaultLang("Messages.Party.AlreadyCreate", "&d&lPARTY &f➠ You have already created a party", lang);
                addDefaultLang("Messages.Party.AlreadySent", "&d&lPARTY &f➠ You''ve already sent an invitation", lang);
                addDefaultLang("Messages.Party.AlreadyHasParty", "&d&lPARTY &f➠ You''re already at a party with: &9%owner%", lang);
                addDefaultLang("Messages.Party.NotifyInvite", "&d&lPARTY &f➠ &9%owner%&f a guest to &a%target%", lang);
                addDefaultLang("Messages.Party.NotifyJoin", "&d&lPARTY &f➠ &9%target%&f has joined", lang);
                addDefaultLang("Messages.Party.NotifyLeave", "&d&lPARTY &f➠ &9%target%&f came out of the party.", lang);
                addDefaultLang("Messages.Party.NotifyKick", "&d&lPARTY &f➠ &9%owner%&f has removed &c%target%&f from the party.", lang);
                addDefaultLang("Messages.Party.NotifyRemove", "&d&lPARTY &f➠ &9%owner%&f removed the party", lang);
                addDefaultLang("Messages.Party.NoHaveInvitation", "&d&lPARTY &f➠ You don''t have any invitations.", lang);
                addDefaultLang("Messages.Party.NoOwnerLeave", "&d&lPARTY &f➠ You can''t leave your own party, try: /party disband", lang);
                addDefaultLang("Messages.Party.NoParty", "&d&lPARTY &f➠ You''re not at a party.", lang);
                addDefaultLang("Messages.Party.IsNotOwner", "&d&lPARTY &f➠ You''re not the owner of this party.", lang);
                addDefaultLang("Messages.Party.InviteMe", "&d&lPARTY &f➠ You can''t invite yourself, :V", lang);
                addDefaultLang("Messages.Party.SendInvitation", "&d&lPARTY &f➠ You just invited player &9%target%", lang);
                addDefaultLang("Messages.Party.JoinTarget", "&d&lPARTY &f➠ You''ve joined correctly.", lang);
                addDefaultLang("Messages.Party.Join", "&d&lPARTY &f➠ &9%target%&f accept your application to join", lang);
                addDefaultLang("Messages.Party.JoinArenaNoOwner", "&d&lPARTY &f➠ You don''t owner the party", lang);
                addDefaultLang("Messages.Party.JoinArenaFull", "&d&lPARTY &f➠ Your party is too big for the arena", lang);
                addDefaultLang("Messages.Party.Leave", "&d&lPARTY &f➠ You came out of the party.", lang);
                addDefaultLang("Messages.Party.TargetNoMember", "&d&lPARTY &f➠ &9%target%&f is not a member of the party", lang);
                addDefaultLang("Messages.Party.TargetHasParty", "&d&lPARTY &f➠ &9%target%''s&f already at a party", lang);
                addDefaultLang("Messages.Party.KickOwner", "&d&lPARTY &f➠ You removed &9%target%&f from the party.", lang);
                addDefaultLang("Messages.Party.KickTarget", "&d&lPARTY &f➠ You were kicketed by &9%owner%&f.", lang);
                addDefaultLang("Messages.Party.Remove", "&d&lPARTY &f➠ You successfully eliminated the party.", lang);
                addDefaultLang("Messages.Party.ReceiveInvitation", "&d&lPARTY &f➠ &9%owner%&f has invited you to his party; &d&l• &fTo join use the command &a/party join %owner%", lang);
                addDefaultLang("Messages.Party.MemberDisconnect", "&d&lPARTY &f➠ &9%target%''s&f was disconnected", lang);
                addDefaultLang("Messages.Party.OwnerDisconnect", "&d&lPARTY &f➠ &9%owner%''s&f was disconnected, so the party was eliminated.", lang);

                addDefaultLang("Messages.Cmd.error", "&c&l➥ &cError command", lang);
                addDefaultLang("Messages.Cmd.sw", Arrays.asList("%center%&8-=[&a&l*&8]=- --------   [ &9SkyWars &8]   -------- -=[&a&l*&8]=-", "", "%center%&7/join <arena>/<solo/team> &8| &eJoin to arena", "%center%&7/leave &8| &eExit to arena", "%center%&7/sw lang <idioma> &8| &eCambiar idioma", "", "%center%&8-=[&a&l*&8]=- --------   [ &9SkyWars &8]   -------- -=[&a&l*&8]=-"), lang);
                addDefaultLang("Messages.Cmd.swadmin", Arrays.asList("%center%&8-=[&a&l*&8]=- --------   [ &bSkyWars &8]   -------- -=[&a&l*&8]=-", "", "%center%&7/sw setlobby &8| &eSet lobby", "%center%&7/sw world &8| &eEdit world before create arena", "%center%&7/sw create &8| &eCreate arena", "%center%&7/Sw setsize &8| &eSet size arena", "%center%&7/sw addspawn &8| &eAdd spawn points", "%center%&7/sw setmin &8| &eSet min player to start", "%center%&7/sw setspectator &8| &eSet spectator spawn", "%center%&7/sw enable &8| &eActive arena", "%center%&7/sw coins <player> &8| &eManager coins", "%center%&7/sw souls <player> &8| &eManager souls", "%center%&7/sw wandc &8| &eMark corners for create cages", "%center%&7/sw wands &8| &eMark Locations for SoulWell", "%center%&7/sw addcage &8| &eAdd cages", "%center%&7/sw addkit &8| &eAdd kits from inventory", "%center%&7/sw addhologram &8| &eAdd holograms", "%center%&7/sw reload &8| &eReload configurations", "", "%center%&7/sw join <arena> &8| &eJoin to arena", "%center%&7/sw leave &8| &eExit to arena", "%center%&7/sw lang <idioma> &8| &eChange language", "", "%center%&8-=[&a&l*&8]=- --------   [ &bSkyWars &8]   -------- -=[&a&l*&8]=-"), lang);
            }

            /* SETTINGS */
            addDefaultSettings("MySQL.enabled", false);
            addDefaultSettings("MySQL.hostname","127.0.0.1");
            addDefaultSettings("MySQL.port", 3306);
            addDefaultSettings("MySQL.database", "SkyWars");
            addDefaultSettings("MySQL.username", "root");
            addDefaultSettings("MySQL.password", "");

            addDefaultSettings("checkVersion", "true");

            addDefaultSettings("Default.Kit.Solo", "Default");
            addDefaultSettings("Default.Kit.Team", "Default");
            addDefaultSettings("Default.Kit.Ranked", "Default");
            addDefaultSettings("Default.Cage.Solo", "Default");
            addDefaultSettings("Default.Cage.Team", "Default");
            addDefaultSettings("Default.Cage.Ranked", "Default");
            addDefaultSettings("Default.WinEffect.Solo", "fireworks");
            addDefaultSettings("Default.WinEffect.Team", "fireworks");
            addDefaultSettings("Default.WinEffect.Ranked", "fireworks");
            addDefaultSettings("Default.KillEffect.Solo", "none");
            addDefaultSettings("Default.KillEffect.Team", "none");
            addDefaultSettings("Default.KillEffect.Ranked", "none");
            addDefaultSettings("Default.Trail.Solo", "none");
            addDefaultSettings("Default.Trail.Team", "none");
            addDefaultSettings("Default.Trail.Ranked", "none");
            addDefaultSettings("Default.KillSound.Solo", "BLOCK_NOTE_BLOCK_PLING");
            addDefaultSettings("Default.KillSound.Team", "BLOCK_NOTE_BLOCK_PLING");
            addDefaultSettings("Default.KillSound.Ranked", "BLOCK_NOTE_BLOCK_PLING");
            addDefaultSettings("Default.Lang", "en-EN");

            addDefaultSettings("Coins.DeathSolo", 8);
            addDefaultSettings("Coins.DeathTeam", 8);
            addDefaultSettings("Coins.DeathRanked", 8);
            addDefaultSettings("Coins.FirstKillSolo", 30);
            addDefaultSettings("Coins.FirstKillTeam", 30);
            addDefaultSettings("Coins.FirstKillRanked", 30);
            addDefaultSettings("Coins.KillSolo", 16);
            addDefaultSettings("Coins.KillTeam", 16);
            addDefaultSettings("Coins.KillRanked", 16);
            addDefaultSettings("Coins.WinSolo", 50);
            addDefaultSettings("Coins.WinTeam", 50);
            addDefaultSettings("Coins.WinRanked", 50);
            addDefaultSettings("Coins.lvlRanked", 50);
            addDefaultSettings("Coins.lvlRankedLosed", 50);

            addDefaultSettings("Inventory.Lobby", "lobby");
            addDefaultSettings("Inventory.Arena.Solo", "arenasolo");
            addDefaultSettings("Inventory.Arena.Team", "arenateam");
            addDefaultSettings("Inventory.Arena.Ranked", "arenaranked");
            addDefaultSettings("Inventory.Arena.Spectator", "spectator");

            addDefaultSettings("ResetArena", "RESETWORLD");
            addDefaultSettings("SWM-TYPE", "file");

            addDefaultSettings("ReTeleportCage", 2);
            addDefaultSettings("TopUpdate", 600);
            addDefaultSettings("ListLanguage", Arrays.asList("en-EN", "es-ES"));
            addDefaultSettings("Holograms", "");

            addDefaultSettings("refillHolo.line1", "&a%time%");
            addDefaultSettings("refillHolo.line2", "&cEmpty");

            addDefaultSettings("lobbies.onScoreboard", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onItemJoin", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onBreak", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onPlace", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onTime", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onPortal", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onDrop", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onVoidTP", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onDamage", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onExplosion", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onFlow", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onFeed", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onHealth", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onResetGameMode", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onMonsters", Collections.singletonList("world"));
            addDefaultSettings("lobbies.onAnimals", Collections.singletonList("world"));

        });
    }

    public void addDefaultLang(String s, Object o, String lang) {
        File file = this.main.getConfigUtils().getFile(this.main, "Messages/Lang/" + lang);
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Messages/Lang/" + lang);

        if (!config.contains(s)) {
            config.set(s, o);

            try {
                config.save(file);
            } catch (IOException e) {
                Console.error(e.getMessage());
            }
        }
    }

    public void addDefaultSettings(String s, Object o) {
        File file = this.main.getConfigUtils().getFile(this.main, "Settings");
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Settings");

        if (!config.contains(s)) {
            config.set(s, o);

            try {
                config.save(file);
            } catch (IOException e) {
                Console.error(e.getMessage());
            }
        }
    }
}
