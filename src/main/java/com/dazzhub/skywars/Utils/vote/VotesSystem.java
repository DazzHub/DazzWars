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
    private Arena arena;
    private List<UUID> basicChests;
    private List<UUID> normalChests;
    private List<UUID> opChests;
    private List<UUID> customChests;
    private List<UUID> dayTime;
    private List<UUID> sunsetTime;
    private List<UUID> nightTime;
    private List<UUID> normalLife;
    private List<UUID> doubleLife;
    private List<UUID> tripleLife;
    private List<UUID> noclean;
    private List<UUID> nofall;
    private List<UUID> noprojectil;
    private List<UUID> dragon;
    private List<UUID> border;
    private List<UUID> dropParty;
    private List<UUID> tntfall;
    private List<UUID> storm;

    private HashMap<String, Integer> checkChest;
    private HashMap<Enums.TypeVotes, Integer> checkTime;
    private HashMap<Enums.TypeVotes, Integer> checkLife;
    private HashMap<Enums.TypeVotes, Integer> checkScenarios;
    private HashMap<Enums.TypeVotes, Integer> checkEvent;

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
        }
    }

    public void setTypes() {
        this.checkChests();
        this.checkHealth();
        this.checkTime();
        //this.checkScenarios();
        //this.checkEvents();
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

    public boolean containsVote(Player p2, String Type) {
        UUID p = p2.getUniqueId();
        if (Type.equals("noclean") || Type.equals("nofall") || Type.equals("noprojectile")) {
            return this.noclean.contains(p) || this.nofall.contains(p) || this.noprojectil.contains(p);
        }
        if (Type.equals("dragon") || Type.equals("border") || Type.equals("dropparty") || Type.equals("tntfall") || Type.equals("storm")) {
            return this.dragon.contains(p) || this.border.contains(p) || this.dropParty.contains(p) || this.tntfall.contains(p) || this.storm.contains(p);
        }
        if (Type.equalsIgnoreCase("basic") || Type.equalsIgnoreCase("normal") || Type.equalsIgnoreCase("op")) {
            return this.basicChests.contains(p) || this.normalChests.contains(p) || this.opChests.contains(p);
        }
        if (Type.equalsIgnoreCase("day") || Type.equalsIgnoreCase("sunset") || Type.equalsIgnoreCase("night")) {
            return this.dayTime.contains(p) || this.sunsetTime.contains(p) || this.nightTime.contains(p);
        }
        if (Type.equalsIgnoreCase("heart10") || Type.equalsIgnoreCase("heart20") || Type.equalsIgnoreCase("heart30")) {
            return (this.normalLife.contains(p) || this.doubleLife.contains(p) || this.tripleLife.contains(p));
        }

        return false;
    }

    public void removeFromVotes(Player pl) {
        UUID p = pl.getUniqueId();
        this.customChests.remove(p);
        this.basicChests.remove(p);
        this.normalChests.remove(p);
        this.opChests.remove(p);
        this.dayTime.remove(p);
        this.sunsetTime.remove(p);
        this.nightTime.remove(p);
        this.normalLife.remove(p);
        this.doubleLife.remove(p);
        this.tripleLife.remove(p);
        this.noclean.remove(p);
        this.nofall.remove(p);
        this.noprojectil.remove(p);
        this.dragon.remove(p);
        this.border.remove(p);
        this.dropParty.remove(p);
        this.tntfall.remove(p);
        this.storm.remove(p);
    }



    public int getVotes(String Type) {
        Type = Type.toLowerCase();
        switch (Type) {
            case "basic": {
                return this.basicChests.size();
            }
            case "normal": {
                return this.normalChests.size();
            }
            case "op": {
                return this.opChests.size();
            }
            case "day": {
                return this.dayTime.size();
            }
            case "heart10": {
                return this.normalLife.size();
            }
            case "heart20": {
                return this.doubleLife.size();
            }
            case "heart30": {
                return this.tripleLife.size();
            }
            case "sunset": {
                return this.sunsetTime.size();
            }
            case "night": {
                return this.nightTime.size();
            }
            case "noclean": {
                return this.noclean.size();
            }
            case "nofall": {
                return this.nofall.size();
            }
            case "noprojectile": {
                return this.noprojectil.size();
            }
            case "dragon": {
                return this.dragon.size();
            }
            case "border": {
                return this.border.size();
            }
            case "dropparty": {
                return this.dropParty.size();
            }
            case "tntfall": {
                return this.tntfall.size();
            }
            case "storm": {
                return this.storm.size();
            }
            default: {
                return 404;
            }
        }
    }
}
