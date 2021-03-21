package com.dazzhub.skywars.Listeners.Custom;

import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClickMenu extends Event
{
    private static HandlerList handlerList;

    private final ordItems ordItems;
    private final GamePlayer gamePlayer;

    public ClickMenu(GamePlayer gamePlayer, ordItems ordItems) {
        this.ordItems = ordItems;
        this.gamePlayer = gamePlayer;
    }

    public static HandlerList getHandlerList() {
        return ClickMenu.handlerList;
    }

    public HandlerList getHandlers() {
        return ClickMenu.handlerList;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public ordItems getOrdItems() {
        return ordItems;
    }

    static {
        ClickMenu.handlerList = new HandlerList();
    }
}
