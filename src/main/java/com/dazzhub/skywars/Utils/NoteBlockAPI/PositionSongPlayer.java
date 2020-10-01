package com.dazzhub.skywars.Utils.NoteBlockAPI;

import org.bukkit.entity.Player;

public class PositionSongPlayer extends SongPlayer {

    public PositionSongPlayer(Song song) {
        super(song);
    }

    @Override
    public void playTick(Player p, int tick) {
        for (Layer l : song.getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) {
                continue;
            }

            Instrument.getInstrument(note.getInstrument()).play(p);
        }
    }
}
