package com.dazzhub.skywars.Utils.scoreboard;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Party.Party;
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

        Party party = gamePlayer.getParty();
        Arena arena = gamePlayer.getArena();

        switch (identifier) {
            case "wins": {
                return Integer.toString(gamePlayer.getWinsSolo() + gamePlayer.getWinsTeam() + gamePlayer.getWinsRanked());
            }

            case "wins_solo": {
                return Integer.toString(gamePlayer.getWinsSolo());
            }

            case "wins_team": {
                return Integer.toString(gamePlayer.getWinsTeam());
            }

            case "wins_ranked": {
                return Integer.toString(gamePlayer.getWinsRanked());
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

            case "kills_ranked": {
                return Integer.toString(gamePlayer.getKillsRanked());
            }

            case "deaths": {
                return Integer.toString(gamePlayer.getDeathsSolo() + gamePlayer.getDeathsTeam() + gamePlayer.getDeathsRanked());
            }

            case "deaths_solo": {
                return Integer.toString(gamePlayer.getDeathsSolo());
            }

            case "deaths_team": {
                return Integer.toString(gamePlayer.getDeathsTeam());
            }

            case "deaths_ranked": {
                return Integer.toString(gamePlayer.getDeathsRanked());
            }

            case "games": {
                return Integer.toString(gamePlayer.getGamesSolo() + gamePlayer.getGamesTeam() + gamePlayer.getGamesRanked());
            }

            case "games_solo": {
                return Integer.toString(gamePlayer.getGamesSolo());
            }

            case "games_team": {
                return Integer.toString(gamePlayer.getGamesTeam());
            }

            case "games_ranked": {
                return Integer.toString(gamePlayer.getGamesRanked());
            }

            case "shots": {
                return Integer.toString(gamePlayer.getShotsSolo() + gamePlayer.getShotsTeam() + gamePlayer.getShotsRanked());
            }

            case "shots_solo": {
                return Integer.toString(gamePlayer.getShotsSolo());
            }

            case "shots_team": {
                return Integer.toString(gamePlayer.getShotsTeam());
            }

            case "shots_ranked": {
                return Integer.toString(gamePlayer.getShotsRanked());
            }

            case "hits": {
                return Integer.toString(gamePlayer.getHitsSolo() + gamePlayer.getHitsTeam() + gamePlayer.getHitsRanked());
            }

            case "hits_solo": {
                return Integer.toString(gamePlayer.getHitsSolo());
            }

            case "hits_team": {
                return Integer.toString(gamePlayer.getHitsTeam());
            }

            case "hits_ranked": {
                return Integer.toString(gamePlayer.getHitsRanked());
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

            case "kit_ranked": {
                return gamePlayer.getKitRanked();
            }

            case "coins": {
                return Integer.toString(gamePlayer.getCoins());
            }

            case "souls": {
                return Integer.toString(gamePlayer.getSouls());
            }

            case "lvlranked": {
                return Integer.toString(gamePlayer.getLvlRanked());
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

            case "cageranked": {
                return gamePlayer.getCageRanked();
            }

            case "wineffect_solo": {
                return gamePlayer.getWinEffectSolo();
            }

            case "wineffect_team": {
                return gamePlayer.getWinEffectTeam();
            }

            case "wineffect_ranked": {
                return gamePlayer.getWinEffectRanked();
            }

            case "party_owner": {
                if (party == null) return "";
                return party.getOwner().getName();
            }

            case "party_members": {
                if (party == null) return "";
                return party.getMembers().toString().replace("[","").replace("]", "");
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
                return String.valueOf(arena.getVotesSystem().getBasicChests().size());
            }
            case "arena_vote_chest_normal": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNormalChests().size());
            }
            case "arena_vote_chest_op": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getOpChests().size());
            }
            case "arena_vote_chest_custom": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCustomChests().size());
            }

            /* VOTE TIME */
            case "arena_vote_time_day": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getDayTime().size());
            }
            case "arena_vote_time_sunset": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getSunsetTime().size());
            }
            case "arena_vote_time_night": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNightTime().size());
            }

            /* VOTE HEART */
            case "arena_vote_heart_normal": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNormalLife().size());
            }
            case "arena_vote_heart_double": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getDoubleLife().size());
            }
            case "arena_vote_heart_triple": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getTripleLife().size());
            }

            /* VOTE SCENARIOS */
            case "arena_vote_scenario_noclean": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNoclean().size());
            }
            case "arena_vote_scenario_nofall": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNofall().size());
            }
            case "arena_vote_scenario_noprojectil": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getNoprojectil().size());
            }

            /* VOTE EVENTS */
            case "arena_vote_event_dragon": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getDragon().size());
            }
            case "arena_vote_event_border": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getBorder().size());
            }
            case "arena_vote_event_dropParty": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getDropParty().size());
            }
            case "arena_vote_event_tntfall": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getTntfall().size());
            }
            case "arena_vote_event_storm": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getStorm().size());
            }

            /* TOTAL */
            case "arena_vote_chest_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckChest().size());
            }
            case "arena_vote_time_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckTime().size());
            }
            case "arena_vote_heart_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckLife().size());
            }
            case "arena_vote_scenarios_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckScenarios().size());
            }
            case "arena_vote_event_total": {
                if (arena == null) return "Error.";
                return String.valueOf(arena.getVotesSystem().getCheckEvent().size());
            }

            default: {
                return "Error.";
            }
        }
    }
}
