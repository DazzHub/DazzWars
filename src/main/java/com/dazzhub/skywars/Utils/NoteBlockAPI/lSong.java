package com.dazzhub.skywars.Utils.NoteBlockAPI;

import com.dazzhub.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class lSong extends Song
{
    public lSong(Song song) {
        super(song);
    }

    public void play(Player player) {
        SongPlayer sp = new PositionSongPlayer(this);
        sp.setAutoDestroy(true);
        sp.addPlayer(player);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> sp.setPlaying(true));
    }
}

