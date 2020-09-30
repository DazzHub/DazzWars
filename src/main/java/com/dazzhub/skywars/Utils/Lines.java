package com.dazzhub.skywars.Utils;

import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
                addDefaultLang("Messages.EndTime", "&e&l⚠&e Game time over", lang);
                addDefaultLang("Messages.item-deny", "&e&l⚠&e You do not have permission", lang);
                addDefaultLang("Messages.menu-deny", "&e&l⚠&e You do not have permission", lang);
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

                List<String> infogame = new ArrayList<>();
                infogame.add("%center%&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                infogame.add("%center%&9&lSkyWars");
                infogame.add("");
                infogame.add("%center%&fChest type &8» &a%chest%");
                infogame.add("%center%&fType of time &8» &6%time%");
                infogame.add("%center%&fKind of hearts &8» &e%heart%");
                infogame.add("%center%&fType of Events &8» &9%event%");
                infogame.add("%center%&fType of scenario &8» &d%scenario%");
                infogame.add("");
                infogame.add("%center%&f▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                infogame.add("&6Teaming is not allowed on Solo mode!");
                infogame.add("");
                addDefaultLang("Messages.InfoGame", infogame, lang);

                addDefaultLang("Messages.LuckTitle.Fade", 10, lang);
                addDefaultLang("Messages.LuckTitle.Stay", 40, lang);
                addDefaultLang("Messages.LuckTitle.Out", 10, lang);
                addDefaultLang("Messages.LuckTitle.Info", "&cTo fight!;&eLuck ㋡", lang);

                List<String> winnerGame = new ArrayList<>();
                winnerGame.add("");
                winnerGame.add("%center%&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                winnerGame.add("%center%&9&lSkyWars");
                winnerGame.add("");
                winnerGame.add("%center%&eWinner &7- %winner%");
                winnerGame.add("");
                winnerGame.add("%center%&e1st Killer&7 - %player1% &7- %kills1%");
                winnerGame.add("%center%&62nd Killer&7 - %player2% &7- %kills2%");
                winnerGame.add("%center%&c3rd Killer&7 - %player3% &7- %kills3%");
                winnerGame.add("%center%&a▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                winnerGame.add("&aYou won! &eWant to play again? &b&lCLICK HERE!");

                addDefaultLang("Messages.WinnerGame", winnerGame, lang);

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
                addDefaultLang("Messages.ScoreBoard.EndGame", "End of game", lang);
                addDefaultLang("Messages.ScoreBoard.None", "None", lang);
                addDefaultLang("Messages.ScoreBoard.Team.killCount", true, lang);
                addDefaultLang("Messages.ScoreBoard.Team.Spectator", "&7:&f", lang);
                addDefaultLang("Messages.ScoreBoard.Team.Game", "&c:&f", lang);
                addDefaultLang("Messages.ScoreBoard.Team.TeamsFriends", "&a:&f", lang);

                List<String> arenadesc = new ArrayList<>();
                arenadesc.add("&9▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                arenadesc.add("");
                arenadesc.add("&fClick to join &e%arena%");
                arenadesc.add("&fState &8» &6%state%");
                arenadesc.add("&fMode &8» &a%mode%");
                arenadesc.add("&fPlayers &8» &a%online%/%maxPlayers%");
                arenadesc.add("");
                arenadesc.add("&9▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                addDefaultLang("Messages.MenuArena.TITLE", "&8&l&nArenas", lang);
                addDefaultLang("Messages.MenuArena.ROWS", 6, lang);
                addDefaultLang("Messages.MenuArena.FIREWORK.NAME", "&e%arena%", lang);
                addDefaultLang("Messages.MenuArena.FIREWORK.DESCRIPTION", arenadesc, lang);

                List<String> customdesc = new ArrayList<>();
                customdesc.add("&9▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                customdesc.add("");
                customdesc.add("&fClick to join &e%arena%");
                customdesc.add("&fState &8» &6%state%");
                customdesc.add("&fMode &8» &a%mode%");
                customdesc.add("&fPlayers &8» &a%online%/%maxPlayers%");
                customdesc.add("");
                customdesc.add("&9▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                addDefaultLang("Messages.MenuArena.CustomItem.Use", false, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.WAITTING.ICON-ITEM", 95, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.WAITTING.DATA-VALUE", 5, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.WAITTING.NAME", "&l&n%arena%", lang);
                addDefaultLang("Messages.MenuArena.CustomItem.WAITTING.DESCRIPTION", customdesc, lang);

                addDefaultLang("Messages.MenuArena.CustomItem.STARTING.ICON-ITEM", 95, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.STARTING.DATA-VALUE", 4, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.STARTING.NAME", "&l&n%arena%", lang);
                addDefaultLang("Messages.MenuArena.CustomItem.STARTING.DESCRIPTION", customdesc, lang);

                addDefaultLang("Messages.MenuArena.CustomItem.INGAME.ICON-ITEM", 95, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.INGAME.DATA-VALUE", 14, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.INGAME.NAME", "&l&n%arena%", lang);
                addDefaultLang("Messages.MenuArena.CustomItem.INGAME.DESCRIPTION", customdesc, lang);

                addDefaultLang("Messages.MenuArena.CustomItem.RESTARTING.ICON-ITEM", 95, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.RESTARTING.DATA-VALUE", 11, lang);
                addDefaultLang("Messages.MenuArena.CustomItem.RESTARTING.NAME", "&l&n%arena%", lang);
                addDefaultLang("Messages.MenuArena.CustomItem.RESTARTING.DESCRIPTION", customdesc, lang);

                List<String> closedesc = new ArrayList<>();
                closedesc.add("&9▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                closedesc.add("");
                closedesc.add("&fClick to &eclose");
                closedesc.add("");
                closedesc.add("&9▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                addDefaultLang("Messages.MenuArena.Close.ICON-ITEM", "ENDER_PEARL", lang);
                addDefaultLang("Messages.MenuArena.Close.DATA-VALUE", 0, lang);
                addDefaultLang("Messages.MenuArena.Close.NAME", "&eCerrar", lang);
                addDefaultLang("Messages.MenuArena.Close.DESCRIPTION", closedesc, lang);
                addDefaultLang("Messages.MenuArena.Close.POSITION-X", 5, lang);
                addDefaultLang("Messages.MenuArena.Close.POSITION-Y", 6, lang);


                addDefaultLang("Messages.MenuSpectator.TITLE", "&8&l&nIn Game", lang);
                addDefaultLang("Messages.MenuSpectator.ROWS", 4, lang);
                addDefaultLang("Messages.MenuSpectator.Player.NAME", "&6&n%player%", lang);
                addDefaultLang("Messages.MenuSpectator.Player.DESCRIPTION", "", lang);

                addDefaultLang("Messages.MenuSpectator.Close.NAME", "&eClose", lang);
                addDefaultLang("Messages.MenuSpectator.Close.ICON-ITEM", "ENDER_PEARL", lang);
                addDefaultLang("Messages.MenuSpectator.Close.DATA-VALUE", 0, lang);
                addDefaultLang("Messages.MenuSpectator.Close.DESCRIPTION", closedesc, lang);
                addDefaultLang("Messages.MenuSpectator.Close.POSITION-X", 5, lang);
                addDefaultLang("Messages.MenuSpectator.Close.POSITION-Y", 3, lang);

                List<String> sw = new ArrayList<>();
                sw.add("%center%&8-=[&a&l*&8]=- --------   [ &9SkyWars &8]   -------- -=[&a&l*&8]=-");
                sw.add("");
                sw.add("%center%&7/join <arena>/<solo/team> &8| &eJoin to arena");
                sw.add("%center%&7/leave &8| &eExit to arena");
                sw.add("%center%&7/sw lang <idioma> &8| &eCambiar idioma");
                sw.add("");
                sw.add("%center%&8-=[&a&l*&8]=- --------   [ &9SkyWars &8]   -------- -=[&a&l*&8]=-");

                List<String> swadmin = new ArrayList<>();
                swadmin.add("%center%&8-=[&a&l*&8]=- --------   [ &bSkyWars &8]   -------- -=[&a&l*&8]=-");
                swadmin.add("");
                swadmin.add("%center%&7/sw setlobby &8| &eSet lobby");
                swadmin.add("%center%&7/sw world &8| &eEdit world before create arena");
                swadmin.add("%center%&7/sw create &8| &eCreate arena");
                swadmin.add("%center%&7/sw addspawn &8| &eAdd spawn points");
                swadmin.add("%center%&7/sw setmin &8| &eSet min player to start");
                swadmin.add("%center%&7/sw setmax &8| &eSet max player in arena");
                swadmin.add("%center%&7/sw setspectator &8| &eSet spectator spawn");
                swadmin.add("%center%&7/sw enable &8| &eActive arena");
                swadmin.add("%center%&7/sw coins <arena> &8| &eManager coins");
                swadmin.add("%center%&7/sw wandc &8| &eMark corners for create cages");
                swadmin.add("%center%&7/sw addcage &8| &eAdd cages");
                swadmin.add("%center%&7/sw addkit &8| &eAdd kits from inventory");
                swadmin.add("%center%&7/sw addhologram &8| &eAdd holograms");
                swadmin.add("%center%&7/sw reload &8| &eReload configurations");
                swadmin.add("");
                swadmin.add("%center%&7/sw join <arena> &8| &eJoin to arena");
                swadmin.add("%center%&7/sw leave &8| &eExit to arena");
                swadmin.add("%center%&7/sw lang <idioma> &8| &eChange language");
                swadmin.add("");
                swadmin.add("%center%&8-=[&a&l*&8]=- --------   [ &bSkyWars &8]   -------- -=[&a&l*&8]=-");

                addDefaultLang("Messages.Cmd.error", "&c&l➥ &cError command", lang);
                addDefaultLang("Messages.Cmd.sw", sw, lang);
                addDefaultLang("Messages.Cmd.swadmin", swadmin, lang);

                infogame.clear();
                winnerGame.clear();
                arenadesc.clear();
                customdesc.clear();
                closedesc.clear();
                sw.clear();
                swadmin.clear();
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
            addDefaultSettings("Default.Cage.Solo", "Default");
            addDefaultSettings("Default.Cage.Team", "Default");
            addDefaultSettings("Default.WinEffect.Solo", "fireworks");
            addDefaultSettings("Default.WinEffect.Team", "fireworks");
            addDefaultSettings("Default.KillEffect.Solo", "none");
            addDefaultSettings("Default.KillEffect.Team", "none");
            addDefaultSettings("Default.Trail.Solo", "none");
            addDefaultSettings("Default.Trail.Team", "none");
            addDefaultSettings("Default.KillSound.Solo", "BLOCK_NOTE_BLOCK_PLING");
            addDefaultSettings("Default.KillSound.Team", "BLOCK_NOTE_BLOCK_PLING");
            addDefaultSettings("Default.Lang", "en-EN");

            addDefaultSettings("Coins.DeathSolo", 8);
            addDefaultSettings("Coins.DeathTeam", 8);
            addDefaultSettings("Coins.FirstKillSolo", 30);
            addDefaultSettings("Coins.FirstKillTeam", 30);
            addDefaultSettings("Coins.KillSolo", 16);
            addDefaultSettings("Coins.KillTeam", 16);
            addDefaultSettings("Coins.WinSolo", 50);
            addDefaultSettings("Coins.WinTeam", 50);

            addDefaultSettings("ReTeleportCage", 2);
            addDefaultSettings("TopUpdate", 600);
            addDefaultSettings("ListLanguage", Arrays.asList("en-EN", "es-ES"));
            addDefaultSettings("Holograms", "");

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

            addDefaultSettings("Inventory.Lobby", "lobby");
            addDefaultSettings("Inventory.Arena.Solo", "arenasolo");
            addDefaultSettings("Inventory.Arena.Team", "arenateam");
            addDefaultSettings("Inventory.Arena.Spectator", "spectator");

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
