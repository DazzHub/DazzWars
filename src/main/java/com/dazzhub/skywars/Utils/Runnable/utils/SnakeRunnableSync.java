package com.dazzhub.skywars.Utils.Runnable.utils;

import com.dazzhub.skywars.Utils.Runnable.TaskSync;

public abstract class SnakeRunnableSync implements Tick {

    @Override
    public abstract void onTick();

    public void run() {
        TaskSync.getTickList().add(this);
    }

    public void cancel() {
        TaskSync.getTickList().remove(this);
    }

}
