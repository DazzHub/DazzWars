package com.dazzhub.skywars.MySQL;

import java.util.UUID;

public interface getPlayerDB {

    void loadPlayer(UUID uuid);

    void savePlayer(UUID uuid);

    void loadMySQL();

    String[] TopKillsSolo();
    String[] TopKillsTeam();

    String[] TopDeathsSolo();
    String[] TopDeathsTeam();

    String[] TopWinsSolo();
    String[] TopWinsTeam();
}
