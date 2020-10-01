package com.dazzhub.skywars.Utils.NoteBlockAPI;

import com.cryptomorin.xseries.XSound;

public class Instrument
{
    public static XSound getInstrument(int instrument) {
        switch (instrument) {
            case 1: {
                return XSound.BLOCK_NOTE_BLOCK_BASS;
            }
            case 2: {
                return XSound.BLOCK_NOTE_BLOCK_BASEDRUM;
            }
            case 3: {
                return XSound.BLOCK_NOTE_BLOCK_SNARE;
            }
            case 4: {
                return XSound.BLOCK_NOTE_BLOCK_HAT;
            }
            case 5: {
                return XSound.BLOCK_NOTE_BLOCK_GUITAR;
            }
            case 6: {
                return XSound.BLOCK_NOTE_BLOCK_FLUTE;
            }
            case 7: {
                return XSound.BLOCK_NOTE_BLOCK_BELL;
            }
            case 8: {
                return XSound.BLOCK_NOTE_BLOCK_CHIME;
            }
            case 9: {
                return XSound.BLOCK_NOTE_BLOCK_XYLOPHONE;
            }
            case 10: {
                return XSound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE;
            }
            case 11: {
                return XSound.BLOCK_NOTE_BLOCK_COW_BELL;
            }
            case 12: {
                return XSound.BLOCK_NOTE_BLOCK_DIDGERIDOO;
            }
            case 13: {
                return XSound.BLOCK_NOTE_BLOCK_BIT;
            }
            case 14: {
                return XSound.BLOCK_NOTE_BLOCK_BANJO;
            }
            case 15: {
                return XSound.BLOCK_NOTE_BLOCK_PLING;
            }
            default: {
                return XSound.BLOCK_NOTE_BLOCK_HARP;
            }
        }
    }

}
