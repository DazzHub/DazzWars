package com.dazzhub.skywars.Arena.comparables.compare;


import com.dazzhub.skywars.Arena.Arena;

import java.util.Comparator;

public class arenaComparator implements Comparator<Arena> {

    @Override
    public int compare(Arena arena1, Arena arena2) {
        return arena2.getPlayers().size() - arena1.getPlayers().size();
    }
}
