package com.dazzhub.skywars.Utils.Runnable;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class RunnableWorker {

    private Map<Runnable, Long> tasks;
    private Map<Runnable, RunnableType> type;
    private Plugin plugin;
    private long time;
    private boolean async;

    public RunnableWorker(Plugin plugin, Long time, boolean async) {
        this.tasks = new ConcurrentHashMap<>();
        this.type = new ConcurrentHashMap<>();
        this.plugin = plugin;
        this.time = time;
        this.async = async;
        startWorking();
    }

    public void remove(Runnable runnable) {
        tasks.remove(runnable);
        type.remove(runnable);
    }

    public void startWorking() {
        if (async) {
            new BukkitRunnable(){

                @Override
                public void run() {
                    for (Map.Entry<Runnable, Long> entry : tasks.entrySet()) {
                        Runnable runnable = entry.getKey();
                        try {
                            if (0 >= tasks.get(runnable)) {
                                if (type.get(entry.getKey()).equals(RunnableType.LATER)) {
                                    remove(runnable);
                                } else {
                                    tasks.put(runnable, time); // resetting time
                                }
                                runnable.run(); // running
                            } else {
                                tasks.put(entry.getKey(), entry.getValue() - 1); // decrementing time
                                //Console.debug("DECREMENTING TASK -> " + entry.getValue());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.runTaskTimerAsynchronously(plugin,0L, 1L);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Map.Entry<Runnable, Long> entry : tasks.entrySet()) {
                        Runnable runnable = entry.getKey();
                        try {
                            if (0 >= tasks.get(runnable)) {
                                if (type.get(entry.getKey()).equals(RunnableType.LATER)) {
                                    remove(runnable);
                                } else {
                                    tasks.put(runnable, time); // resetting time
                                }

                                runnable.run(); // running
                            } else {
                                tasks.put(entry.getKey(), entry.getValue() - 1); // decrementing time
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        }
    }
}
