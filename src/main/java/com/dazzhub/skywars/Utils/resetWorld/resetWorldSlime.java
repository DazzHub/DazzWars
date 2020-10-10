package com.dazzhub.skywars.Utils.resetWorld;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

public class resetWorldSlime {

    private Main main;

    public resetWorldSlime(Main main) {
        this.main = main;
    }

    @SneakyThrows
    public void createworld(Arena arena) {
        String mapname = arena.getNameWorld();
        String uuid = arena.getUuid();

        SlimePlugin plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SlimeLoader loader = plugin.getLoader(main.getSettings().getString("SWM-TYPE"));

        SlimePropertyMap props = new SlimePropertyMap();

        props.setString(SlimeProperties.DIFFICULTY, "normal");
        props.setInt(SlimeProperties.SPAWN_X, 0);
        props.setInt(SlimeProperties.SPAWN_Y, 60);
        props.setInt(SlimeProperties.SPAWN_Z, 0);
        props.setBoolean(SlimeProperties.ALLOW_ANIMALS, false);
        props.setBoolean(SlimeProperties.ALLOW_MONSTERS, false);
        props.setBoolean(SlimeProperties.PVP, true);

        SlimeWorld slimeWorld = plugin.loadWorld(loader, mapname, false, props).clone(uuid);
        plugin.generateWorld(slimeWorld);

        Bukkit.getScheduler().runTaskLater(main, () -> {
            arena.setGameStatus(Enums.GameStatus.WAITING);
            arena.setUsable(false);

            arena.loadSpawns(uuid);

            if (arena.getISign() != null) arena.getISign().updateSign();
        }, 5);
    }

    public void unloadworld(String name) {
        Bukkit.unloadWorld(name, false);
    }

}
