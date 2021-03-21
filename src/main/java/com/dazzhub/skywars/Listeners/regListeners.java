package com.dazzhub.skywars.Listeners;

import com.dazzhub.skywars.Listeners.Arena.onArena.*;
import com.dazzhub.skywars.Listeners.Arena.onSettings.*;
import com.dazzhub.skywars.Listeners.Bukkit.onCorner;
import com.dazzhub.skywars.Listeners.Bukkit.onGlobal;
import com.dazzhub.skywars.Listeners.Bukkit.onJoinServer;
import com.dazzhub.skywars.Listeners.Bukkit.onLeftServer;
import com.dazzhub.skywars.Listeners.Inventory.onMenu;
import com.dazzhub.skywars.Listeners.Lobby.*;
import com.dazzhub.skywars.Listeners.Sign.onSignArena;
import com.dazzhub.skywars.Listeners.Sign.onSignTop;
import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class regListeners {

    private final Main main;

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
        this.onLoad(new onSignArena(main));
        this.onLoad(new onSignTop(main));

        /* INVENTORY */
        this.onLoad(new onMenu(main));

        /* LOBBIES */
        this.onLoad(new onBreak(main));
        this.onLoad(new onDamage(main));
        this.onLoad(new onMobs(main));
        this.onLoad(new onPlace(main));
        this.onLoad(new onPlayer(main));
        this.onLoad(new onTime(main));
        this.onLoad(new onSoulWell(main));
    }

    private void onLoad(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, main);
    }
}
