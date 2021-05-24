package com.dazzhub.skywars.Utils.Runnable;


import com.dazzhub.skywars.Utils.Runnable.utils.Tick;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskAsync extends BukkitRunnable {

    private static final Queue<Tick> tickList;

    static {
        tickList = new ConcurrentLinkedQueue<>();
    }

    public static Queue<Tick> getTickList() {
        return tickList;
    }

    @Override
    public void run() {

        // Calling tick items
        for (Tick tick : tickList) tick.onTick();

    }

}
