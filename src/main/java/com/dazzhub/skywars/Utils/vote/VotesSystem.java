package com.dazzhub.skywars.Utils.vote;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Utils.Enums;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class VotesSystem
{
    private final Arena arena;

    private final List<UUID> basicChests;
    private final List<UUID> normalChests;
    private final List<UUID> opChests;
    private final List<UUID> customChests;

    private final List<UUID> dayTime;
    private final List<UUID> sunsetTime;
    private final List<UUID> nightTime;

    private final List<UUID> normalLife;
    private final List<UUID> doubleLife;
    private final List<UUID> tripleLife;

    private final List<UUID> noclean;
    private final List<UUID> nofall;
    private final List<UUID> noprojectil;

    private final List<UUID> dragon;
    private final List<UUID> border;
    private final List<UUID> dropParty;
    private final List<UUID> tntfall;
    private final List<UUID> storm;

    private final HashMap<String, Integer> checkChest;
    private final HashMap<Enums.TypeVotes, Integer> checkTime;
    private final HashMap<Enums.TypeVotes, Integer> checkLife;
    private final HashMap<Enums.TypeVotes, Integer> checkScenarios;
    private final HashMap<Enums.TypeVotes, Integer> checkEvent;

    public VotesSystem(Arena arena) {
        this.arena = arena;
        this.basicChests = new ArrayList<>();
        this.normalChests = new ArrayList<>();
        this.opChests = new ArrayList<>();
        this.customChests = new ArrayList<>();
        this.dayTime = new ArrayList<>();
        this.sunsetTime = new ArrayList<>();
        this.nightTime = new ArrayList<>();
        this.normalLife = new ArrayList<>();
        this.doubleLife = new ArrayList<>();
        this.tripleLife = new ArrayList<>();
        this.noclean = new ArrayList<>();
        this.nofall = new ArrayList<>();
        this.noprojectil = new ArrayList<>();
        this.dragon = new ArrayList<>();
        this.border = new ArrayList<>();
        this.dropParty = new ArrayList<>();
        this.tntfall = new ArrayList<>();
        this.storm = new ArrayList<>();

        this.checkChest = new HashMap<>();
        this.checkTime = new HashMap<>();
        this.checkLife = new HashMap<>();
        this.checkScenarios = new HashMap<>();
        this.checkEvent = new HashMap<>();
    }

    public void addCustomChests(Player p, String Type) {
        this.customChests.add(p.getUniqueId());
        this.checkChest.put(Type, customChests.size());
    }

    public void addVote(Player p, Enums.TypeVotes Type) {
        switch (Type) {
            case BASIC: {
                this.basicChests.add(p.getUniqueId());
                this.checkChest.put(Type.name(), basicChests.size());
                break;
            }
            case NORMAL: {
                this.normalChests.add(p.getUniqueId());
                this.checkChest.put(Type.name(), normalChests.size());
                break;
            }
            case OP: {
                this.opChests.add(p.getUniqueId());
                this.checkChest.put(Type.name(), opChests.size());
                break;
            }
            case HEART10: {
                this.normalLife.add(p.getUniqueId());
                this.checkLife.put(Type, normalLife.size());
                break;
            }
            case HEART20: {
                this.doubleLife.add(p.getUniqueId());
                this.checkLife.put(Type, doubleLife.size());
                break;
            }
            case HEART30: {
                this.tripleLife.add(p.getUniqueId());
                this.checkLife.put(Type, tripleLife.size());
                break;
            }
            case DAY: {
                this.dayTime.add(p.getUniqueId());
                this.checkTime.put(Type, dayTime.size());
                break;
            }
            case SUNSET: {
                this.sunsetTime.add(p.getUniqueId());
                this.checkTime.put(Type, sunsetTime.size());
                break;
            }
            case NIGHT: {
                this.nightTime.add(p.getUniqueId());
                this.checkTime.put(Type, nightTime.size());
                break;
            }
            case BORDER: {
                this.border.add(p.getUniqueId());
                this.checkEvent.put(Type, border.size());
                break;
            }
            case DRAGON: {
                this.dragon.add(p.getUniqueId());
                this.checkEvent.put(Type, dragon.size());
                break;
            }
            case DROPPARTY: {
                this.dropParty.add(p.getUniqueId());
                this.checkEvent.put(Type, dropParty.size());
                break;
            }
            case STORM: {
                this.storm.add(p.getUniqueId());
                this.checkEvent.put(Type, storm.size());
                break;
            }
            case TNTFALL: {
                this.tntfall.add(p.getUniqueId());
                this.checkEvent.put(Type, tntfall.size());
                break;
            }
            case NOCLEAN: {
                this.noclean.add(p.getUniqueId());
                this.checkScenarios.put(Type, noclean.size());
                break;
            }
            case NOFALL: {
                this.nofall.add(p.getUniqueId());
                this.checkScenarios.put(Type, nofall.size());
                break;
            }
            case NOPROJECTILE: {
                this.noprojectil.add(p.getUniqueId());
                this.checkScenarios.put(Type, nofall.size());
                break;
            }
        }
    }

    public void setTypes() {
        this.checkChests();
        this.checkHealth();
        this.checkTime();
        this.checkEvents();
        this.checkScenarios();
    }

    private void checkHealth() {
        while (this.checkLife.size() < 1) {
            this.checkLife.put(Enums.TypeVotes.NONE, 0);
        }

        SortedMap<Enums.TypeVotes, Comparable<Integer>> sortedMap = ImmutableSortedMap.copyOf(checkLife, Ordering.natural().reverse().onResultOf(Functions.forMap(checkLife)).compound(Ordering.natural().reverse()));

        String votes = String.valueOf(sortedMap.values()).replace("[", "").replace("]", "");
        String type = String.valueOf(sortedMap.keySet()).replace("[", "").replace("]", "");

        if (!votes.split(",")[0].equals("0")){
            this.arena.setHealthType(Enums.TypeVotes.valueOf(type.split(",")[0]));
        } else {
            this.arena.setHealthType(Enums.TypeVotes.HEART10);
        }
    }

    private void checkTime() {
        while (this.checkTime.size() < 1) {
            this.checkTime.put(Enums.TypeVotes.NONE, 0);
        }

        SortedMap<Enums.TypeVotes, Comparable<Integer>> sortedMap = ImmutableSortedMap.copyOf(checkTime, Ordering.natural().reverse().onResultOf(Functions.forMap(checkTime)).compound(Ordering.natural().reverse()));

        String votes = String.valueOf(sortedMap.values()).replace("[", "").replace("]", "");
        String type = String.valueOf(sortedMap.keySet()).replace("[", "").replace("]", "");

        if (!votes.split(",")[0].equals("0")){
            this.arena.setTimeType(Enums.TypeVotes.valueOf(type.split(",")[0]));
        } else {
            this.arena.setTimeType(Enums.TypeVotes.DAY);
        }
    }

    private void checkChests() {
        while (this.checkChest.size() < 1) {
            this.checkChest.put(Enums.TypeVotes.NONE.name(), 0);
        }

        SortedMap<String, Comparable<Integer>> sortedMap = ImmutableSortedMap.copyOf(checkChest, Ordering.natural().reverse().onResultOf(Functions.forMap(checkChest)).compound(Ordering.natural().reverse()));

        String votes = String.valueOf(sortedMap.values()).replace("[", "").replace("]", "");
        String type = String.valueOf(sortedMap.keySet()).replace("[", "").replace("]", "");

        if (!votes.split(",")[0].equals("0")){
            this.arena.setChestType(type.split(",")[0]);
        } else {
            this.arena.setChestType(Enums.TypeVotes.NORMAL.name());
        }
    }

    private void checkScenarios() {
        while (this.checkScenarios.size() < 1) {
            this.checkScenarios.put(Enums.TypeVotes.NONE, 0);
        }

        SortedMap<Enums.TypeVotes, Comparable<Integer>> sortedMap = ImmutableSortedMap.copyOf(checkScenarios, Ordering.natural().reverse().onResultOf(Functions.forMap(checkScenarios)).compound(Ordering.natural().reverse()));

        String votes = String.valueOf(sortedMap.values()).replace("[", "").replace("]", "");
        String type = String.valueOf(sortedMap.keySet()).replace("[", "").replace("]", "");

        if (!votes.split(",")[0].equals("0")){
            this.arena.setScenariosType(Enums.TypeVotes.valueOf(type.split(",")[0]));
        } else {
            this.arena.setScenariosType(Enums.TypeVotes.NONE);
        }
    }

    private void checkEvents() {
        while (this.checkEvent.size() < 1) {
            this.checkEvent.put(Enums.TypeVotes.NONE, 0);
        }

        SortedMap<Enums.TypeVotes, Comparable<Integer>> sortedMap = ImmutableSortedMap.copyOf(checkEvent, Ordering.natural().reverse().onResultOf(Functions.forMap(checkEvent)).compound(Ordering.natural().reverse()));

        String votes = String.valueOf(sortedMap.values()).replace("[", "").replace("]", "");
        String type = String.valueOf(sortedMap.keySet()).replace("[", "").replace("]", "");

        if (!votes.split(",")[0].equals("0")){
            this.arena.setEventsType(Enums.TypeVotes.valueOf(type.split(",")[0]));
        } else {
            this.arena.setEventsType(Enums.TypeVotes.NONE);
        }
    }

    public boolean containsVote(Player p, Enums.TypeVotes Type) {
        switch (Type) {

            case BASIC:
            case NORMAL:
            case OP: {
                return this.normalChests.contains(p.getUniqueId()) || this.basicChests.contains(p.getUniqueId()) || this.opChests.contains(p.getUniqueId());
            }

            case DAY:
            case SUNSET:
            case NIGHT: {
                return this.nightTime.contains(p.getUniqueId()) || this.sunsetTime.contains(p.getUniqueId()) || this.dayTime.contains(p.getUniqueId());
            }

            case HEART10:
            case HEART20:
            case HEART30: {
                return this.normalLife.contains(p.getUniqueId()) || this.doubleLife.contains(p.getUniqueId()) || this.tripleLife.contains(p.getUniqueId());
            }

            case BORDER:
            case DRAGON:
            case DROPPARTY:
            case STORM:
            case TNTFALL: {
                return this.tntfall.contains(p.getUniqueId()) || this.border.contains(p.getUniqueId()) || this.dragon.contains(p.getUniqueId()) || this.dropParty.contains(p.getUniqueId()) || this.storm.contains(p.getUniqueId());
            }

            case NOCLEAN:
            case NOFALL:
            case NOPROJECTILE: {
                return this.nofall.contains(p.getUniqueId()) || this.noclean.contains(p.getUniqueId()) || this.noprojectil.contains(p.getUniqueId());
            }

            default: {
                return false;
            }
        }

    }

    public int getVotes(Enums.TypeVotes Type) {
        switch (Type) {

            case BASIC: {
                return this.basicChests.size();
            }
            case NORMAL: {
                return this.normalChests.size();
            }
            case OP: {
                return this.opChests.size();
            }

            case HEART10: {
                return this.normalLife.size();
            }
            case HEART20: {
                return this.doubleLife.size();
            }
            case HEART30: {
                return this.tripleLife.size();
            }

            case DAY: {
                return this.dayTime.size();
            }
            case SUNSET: {
                return this.sunsetTime.size();
            }
            case NIGHT: {
                return this.nightTime.size();
            }

            case NOCLEAN: {
                return this.noclean.size();
            }
            case NOFALL: {
                return this.nofall.size();
            }
            case NOPROJECTILE: {
                return this.noprojectil.size();
            }

            case DRAGON: {
                return this.dragon.size();
            }
            case BORDER: {
                return this.border.size();
            }
            case DROPPARTY: {
                return this.dropParty.size();
            }
            case TNTFALL: {
                return this.tntfall.size();
            }
            case STORM: {
                return this.storm.size();
            }

            default: {
                return 404;
            }
        }
    }
}
