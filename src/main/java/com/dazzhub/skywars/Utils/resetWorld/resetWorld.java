package com.dazzhub.skywars.Utils.resetWorld;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Enums;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.io.*;
import java.util.Arrays;

public class resetWorld {

    private Main main;

    public resetWorld(Main main) {
        this.main = main;
    }

    public void importWorld2(Arena arena, boolean unLoad) {
        String nameArena = arena.getNameArena();
        String nameWorld = arena.getNameWorld();

        File source = new File(this.main.getDataFolder(), "Arenas/" + nameArena + "/" + nameWorld);

        if (source.isDirectory()) {
            File target = new File(this.main.getServer().getWorldContainer(), source.getName());
            if (unLoad) {
                unLoadWorld(nameWorld);
            }
            try {
                copyDir(source, target);

                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                    main.getArenaManager().loadWorld(nameWorld);

                    arena.setGameStatus(Enums.GameStatus.WAITING);
                    arena.setUsable(false);

                    arena.loadSpawns();

                    if (arena.getISign() != null) arena.getISign().updateSign();
                },5);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to reset world \"" + source.getName() + "\" - could not import the world from backup.");
            }

        }
    }

    public void importWorld(Arena arena, boolean importMap) {
        if (importMap){
            importWorld2(arena, true);
        } else {
            if (!Bukkit.getWorlds().contains(Bukkit.getWorld(arena.getNameWorld()))) {
                main.getArenaManager().loadWorld(arena.getNameWorld());
            }

            World world = Bukkit.getWorld(arena.getNameWorld());

            Bukkit.getScheduler().runTask(main, () -> Arrays.stream(world.getLoadedChunks()).forEach(chunk -> {
                chunk.unload(false);
                chunk.load();
            }));

            Bukkit.getScheduler().runTaskLater(main, () -> {
                main.getArenaManager().loadWorld(arena.getNameWorld());

                arena.setGameStatus(Enums.GameStatus.WAITING);
                arena.setUsable(false);

                arena.loadSpawns();

                if (arena.getISign() != null) arena.getISign().updateSign();
            }, 5);
        }
    }

    public void unLoadWorld(String worldName) {
        File target = new File(this.main.getServer().getWorldContainer(), worldName);
        World world = Bukkit.getWorld(worldName);

        if (target.isDirectory()) {
            if (world != null) {
                Bukkit.getServer().unloadWorld(world, false);
                target.delete();
            }
        }
    }

    public void copyDir(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.exists()) {
                target.mkdir();
            }
            String[] files = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(target, file);
                copyDir(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }
}
