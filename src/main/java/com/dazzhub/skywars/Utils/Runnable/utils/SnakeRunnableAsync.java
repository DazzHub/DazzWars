package com.dazzhub.skywars.Utils.Runnable.utils;

import com.dazzhub.skywars.Utils.Runnable.TaskAsync;

public abstract class SnakeRunnableAsync implements Tick {

    @Override
    public abstract void onTick();

    public void run() {
        TaskAsync.getTickList().add(this);
    }

    public void cancel() {
        TaskAsync.getTickList().remove(this);
    }

}
