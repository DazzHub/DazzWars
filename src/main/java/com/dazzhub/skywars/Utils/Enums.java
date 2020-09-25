package com.dazzhub.skywars.Utils;

import com.dazzhub.skywars.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class Enums {

    private static FileConfiguration config = Main.getPlugin().getConfigUtils().getConfig(Main.getPlugin(), Main.getPlugin().getSettings().getString("Default.Lang"));

    public enum GameStatus
    {
        WAITING,
        STARTING,
        INGAME,
        RESTARTING,
        DISABLED
    }

    public enum JoinCause
    {
        COMMAND,
        INTERACT,
        SIGN,
        MENU,
        PARTY
    }

    public enum LeftCause
    {
        COMMAND,
        INTERACT,
        DISCONNECT,
        DISCONNECTSPECTATOR,
        SPECTATOR,
        INTERACTSPECTATOR,
    }

    public enum TypeVotes
    {
        BASIC,
        NORMAL,
        OP,

        DAY,
        SUNSET,
        NIGHT,

        HEART10,
        HEART20,
        HEART30,

        NONE;
    }


    public enum Mode {
        SOLO("SOLO", 1),
        TEAM("TEAM", 2);

        private String name;
        private int size;

        public static Mode get(String s) {
            Mode[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                Mode mode = values[i];
                if (mode.name().toLowerCase().equals(s.toLowerCase())) {
                    return mode;
                }
            }
            return null;
        }

        public String getName() {
            return this.name;
        }

        public int getSize() {
            return this.size;
        }

        Mode(String name, int size) {
            this.name = name;
            this.size = size;
        }
    }

}