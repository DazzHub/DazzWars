package com.dazzhub.skywars.Utils;

import com.dazzhub.skywars.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class Enums {

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

        BORDER,
        DRAGON,
        DROPPARTY,
        STORM,
        TNTFALL,

        NOCLEAN,
        NOFALL,
        NOPROJECTILE,

        NONE;
    }

    public enum AchievementType
    {
        KILLS,
        WINS,
        PROJECTILES_HIT,
        PROJECTILES_SHOT,
        BLOCKS_PLACED,
        BLOCKS_BROKEN,
        ITEMS_ENCHANTED,
        ITEMS_CRAFTED
    }

    public enum Mode {
        SOLO("SOLO", 1),
        TEAM("TEAM", 2),
        RANKED("RANKED", 1);

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

    public enum ScoreboardType {
        LOBBY,

        STARTING,
        STARTINGTEAM,
        STARTINGRANKED,

        INGAME,
        INGAMETEAM,
        INGAMERANKED,

        FINISHED,
        FINISHEDTEAM,
        FINISHEDRANKED,

        SPECTATOR;
    }

    public enum ResetArena {
        RESETWORLD,
        RESETCHUNK,
        SLIMEWORLDMANAGER
    }
}
