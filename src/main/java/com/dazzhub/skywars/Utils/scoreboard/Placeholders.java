package com.dazzhub.skywars.Utils.scoreboard;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {

    private Main plugin;

    public Placeholders(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "sw";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        GamePlayer gamePlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());

        if (gamePlayer == null) {
            return "";
        }

        Arena arena = gamePlayer.getArena();

        switch (identifier) {
            case "wins": {
                return Integer.toString(gamePlayer.getWinsSolo() + gamePlayer.getWinsTeam());
            }

            case "wins_solo": {
                return Integer.toString(gamePlayer.getWinsSolo());
            }

            case "wins_team": {
                return Integer.toString(gamePlayer.getWinsTeam());
            }

            case "kills": {
                return Integer.toString(gamePlayer.getKillsSolo() + gamePlayer.getKillsTeam());
            }

            case "kills_solo": {
                return Integer.toString(gamePlayer.getKillsSolo());
            }

            case "kills_team": {
                return Integer.toString(gamePlayer.getKillsTeam());
            }

            case "deaths": {
                return Integer.toString(gamePlayer.getDeathsSolo() + gamePlayer.getDeathsTeam());
            }

            case "deaths_solo": {
                return Integer.toString(gamePlayer.getDeathsSolo());
            }

            case "deaths_team": {
                return Integer.toString(gamePlayer.getDeathsTeam());
            }

            case "games": {
                return Integer.toString(gamePlayer.getGamesSolo() + gamePlayer.getGamesTeam());
            }

            case "games_solo": {
                return Integer.toString(gamePlayer.getGamesSolo());
            }

            case "games_team": {
                return Integer.toString(gamePlayer.getGamesTeam());
            }

            case "shots": {
                return Integer.toString(gamePlayer.getShotsSolo() + gamePlayer.getShotsTeam());
            }

            case "shots_solo": {
                return Integer.toString(gamePlayer.getShotsSolo());
            }

            case "shots_team": {
                return Integer.toString(gamePlayer.getShotsTeam());
            }

            case "hits": {
                return Integer.toString(gamePlayer.getHitsSolo() + gamePlayer.getHitsTeam());
            }

            case "hits_solo": {
                return Integer.toString(gamePlayer.getHitsSolo());
            }

            case "hits_team": {
                return Integer.toString(gamePlayer.getHitsTeam());
            }

            case "blocksplaced": {
                return Integer.toString(gamePlayer.getBlockPlaced());
            }

            case "blocksbroken": {
                return Integer.toString(gamePlayer.getBlockBroken());
            }

            case "items_enchanted": {
                return Integer.toString(gamePlayer.getItemsEnchanted());
            }

            case "items_crafted": {
                return Integer.toString(gamePlayer.getItemsCrafted());
            }

            case "distance_walked": {
                return Double.toString(gamePlayer.getDistanceWalked());
            }

            case "kit_solo": {
                return gamePlayer.getKitSolo();
            }

            case "kit_team": {
                return gamePlayer.getKitTeam();
            }

            case "coins": {
                return Integer.toString(gamePlayer.getCoins());
            }

            case "player": {
                return gamePlayer.getPlayer().getName();
            }

            case "cagesolo": {
                return gamePlayer.getCageSolo();
            }

            case "cageteam": {
                return gamePlayer.getCageTeam();
            }

            case "wineffect_solo": {
                return gamePlayer.getWinEffectSolo();
            }

            case "wineffect_team": {
                return gamePlayer.getWinEffectTeam();
            }

            case "arena_start": {
                if (arena == null) return "Error.";
                return arena.timeScore(gamePlayer);
            }

            case "arena_on": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getPlayers().size());
            }

            case "arena_status": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getGameStatus());
            }

            case "arena_mode": {
                if (arena == null) return "Error.";
                return arena.getMode().toString();
            }

            case "arena_max": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getMaxPlayers());
            }

            case "arena_teams": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getAliveTeams());
            }

            case "arena_map": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getNameArena());
            }

            case "arena_typeevent": {
                if (arena == null) return "Error.";
                return arena.typeEvent(gamePlayer);
            }

            case "arena_kills": {
                if (arena == null) return "Error.";
                return String.valueOf(gamePlayer.getKillsStreak());
            }

            /* VOTE CHESTS */
            case "arena_vote_chest_basic": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getBasicChests());
            }
            case "arena_vote_chest_normal": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNormalChests());
            }
            case "arena_vote_chest_op": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getOpChests());
            }
            case "arena_vote_chest_custom": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCustomChests());
            }

            /* VOTE TIME */
            case "arena_vote_time_day": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getDayTime());
            }
            case "arena_vote_time_sunset": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getSunsetTime());
            }
            case "arena_vote_time_night": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNightTime());
            }

            /* VOTE HEART */
            case "arena_vote_heart_normal": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNormalLife());
            }
            case "arena_vote_heart_double": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getDoubleLife());
            }
            case "arena_vote_heart_triple": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getTripleLife());
            }

            /* VOTE SCENARIOS */
            case "arena_vote_scenario_noclean": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNoclean());
            }
            case "arena_vote_scenario_nofall": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNofall());
            }
            case "arena_vote_scenario_noprojectil": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNoprojectil());
            }

            /* VOTE EVENTS */
            case "arena_vote_event_dragon": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getDragon());
            }
            case "arena_vote_event_border": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getBorder());
            }
            case "arena_vote_event_dropParty": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getDropParty());
            }
            case "arena_vote_event_tntfall": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getTntfall());
            }
            case "arena_vote_event_storm": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getStorm());
            }

            /* TOTAL */
            case "arena_vote_chest_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckChest());
            }
            case "arena_vote_time_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckTime());
            }
            case "arena_vote_heart_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckLife());
            }
            case "arena_vote_scenarios_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckScenarios());
            }
            case "arena_vote_event_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckEvent());
            }

            default: {
                return "Error.";
            }
        }
    }
}
