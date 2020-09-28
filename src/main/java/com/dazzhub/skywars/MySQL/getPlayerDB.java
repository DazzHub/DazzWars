package com.dazzhub.skywars.MySQL;

import java.util.UUID;

public interface getPlayerDB {

    void loadPlayer(UUID uuid);

    void savePlayer(UUID uuid);

    void loadMySQL();

    String[] TopKillsSolo(int top);
    String[] TopKillsTeam(int top);

    String[] TopDeathsSolo(int top);
    String[] TopDeathsTeam(int top);

    String[] TopWinsSolo(int top);
    String[] TopWinsTeam(int top);
}
