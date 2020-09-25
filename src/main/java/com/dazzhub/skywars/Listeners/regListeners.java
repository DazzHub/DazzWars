package com.dazzhub.skywars.Listeners;

import com.dazzhub.skywars.Listeners.Arena.onArena.*;
import com.dazzhub.skywars.Listeners.Arena.onSettings.addPlayer;
import com.dazzhub.skywars.Listeners.Arena.onSettings.addSpectator;
import com.dazzhub.skywars.Listeners.Arena.onSettings.removePlayer;
import com.dazzhub.skywars.Listeners.Arena.onSettings.removeSpectator;
import com.dazzhub.skywars.Listeners.Bukkit.onCorner;
import com.dazzhub.skywars.Listeners.Bukkit.onGlobal;
import com.dazzhub.skywars.Listeners.Bukkit.onJoinServer;
import com.dazzhub.skywars.Listeners.Bukkit.onLeftServer;
import com.dazzhub.skywars.Listeners.Inventory.onItem;
import com.dazzhub.skywars.Listeners.Inventory.onMenu;
import com.dazzhub.skywars.Listeners.Sign.onSign;
import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class regListeners {

    private Main main;

    public regListeners(Main main) {
        this.main = main;
    }

    public void onReg(){
        /* BUKKIT */
        this.onLoad(new onJoinServer(main));
        this.onLoad(new onLeftServer(main));

        this.onLoad(new onCorner(main));
        this.onLoad(new onGlobal(main));

        /* CUSTOM */
        this.onLoad(new onArena(main));
        this.onLoad(new onWin(main));
        this.onLoad(new onDeath(main));
        this.onLoad(new onJoin(main));
        this.onLoad(new onLeft(main));

        this.onLoad(new addPlayer(main));
        this.onLoad(new removePlayer(main));
        this.onLoad(new addSpectator(main));
        this.onLoad(new removeSpectator(main));

        /* SIGNS */
        this.onLoad(new onSign(main));

        /* INVENTORY */
        this.onLoad(new onItem(main));
        this.onLoad(new onMenu(main));
    }

    private void onLoad(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, main);
    }
}
