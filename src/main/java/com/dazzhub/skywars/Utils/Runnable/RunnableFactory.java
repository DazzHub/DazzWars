package com.dazzhub.skywars.Utils.Runnable;

import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class RunnableFactory {

    private final Map<Runnable, RunnableWorker> syncTasks;
    private final Map<Runnable, RunnableWorker> asyncTasks;

    private final Plugin plugin;

    public RunnableFactory(Plugin plugin) {
        this.syncTasks = new HashMap<>();
        this.asyncTasks = new HashMap<>();

        this.plugin = plugin;
    }

    public void registerRunnable(RunnableWorkerType runnableWorkerType, RunnableType runnableType, Long time, Runnable runnable) {
        RunnableWorker worker = RegisterWorker(time, runnableWorkerType, runnable);

        if (worker == null) return;

        worker.getTasks().put(runnable, time);
        worker.getType().put(runnable, runnableType);
    }

    private RunnableWorker RegisterWorker(Long time, RunnableWorkerType runnableWorkerType, Runnable runnable) {
        switch (runnableWorkerType) {
            case ASYNC: {
                return AsyncRunnableWorker(time, runnable);
            }

            case SYNC: {
                return SyncRunnableWorker(time, runnable);
            }

            default: {
                return null;
            }
        }
    }

    public RunnableWorker getRunnableWorker(Runnable runnable, boolean async){
        if (async) {
            return asyncTasks.get(runnable);
        } else {
            return syncTasks.get(runnable);
        }
    }

    private RunnableWorker AsyncRunnableWorker(Long time, Runnable runnable){
        RunnableWorker worker = new RunnableWorker(plugin, time, true);
        this.asyncTasks.put(runnable, worker);

        return worker;
    }

    private RunnableWorker SyncRunnableWorker(Long time, Runnable runnable){
        RunnableWorker worker = new RunnableWorker(plugin, time, false);
        this.syncTasks.put(runnable, worker);

        return worker;
    }

    public Map<Runnable, RunnableWorker> getAsyncTasks() {
        return asyncTasks;
    }

    public Map<Runnable, RunnableWorker> getSyncTasks() {
        return syncTasks;
    }
}
