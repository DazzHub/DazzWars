package com.dazzhub.skywars.Arena.comparables;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Arena.comparables.compare.arenaComparator;

import java.util.List;

public class comparator {

    public static void checkArenaPlayer(List<Arena> arenas){
        arenas.sort(new arenaComparator());
    }
}
