package com.dazzhub.skywars.Utils.resetWorld;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Runnables.inGame;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Enums;
import com.dazzhub.skywars.Utils.Runnable.RunnableFactory;
import com.dazzhub.skywars.Utils.Runnable.RunnableType;
import com.dazzhub.skywars.Utils.Runnable.RunnableWorkerType;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

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

        RunnableFactory factory = Main.getPlugin().getFactory();

        factory.registerRunnable(RunnableWorkerType.ASYNC, RunnableType.LATER, 5L,
            () -> {
                File source = new File(this.main.getDataFolder(), "Arenas/" + nameArena + "/" + nameWorld);

                if (source.isDirectory()) {
                    File target = new File(this.main.getServer().getWorldContainer(), source.getName());
                    if (unLoad) {
                        unLoadWorld(nameWorld);
                    }

                    if (this.main.getMvWorldManager() != null){
                        this.main.getMvWorldManager().deleteWorld(nameWorld);
                    }

                    copyDir(source, target);

                    Bukkit.getScheduler().runTask(main, () -> {
                        main.getArenaManager().loadWorld(nameWorld);

                        arena.setGameStatus(Enums.GameStatus.WAITING);
                        arena.setUsable(false);

                        arena.loadSpawns(arena.getNameWorld());

                        if (arena.getISign() != null) arena.getISign().updateSign();
                    });
                }
            }
        );

    }

    public void importWorld(Arena arena, boolean importMap) {

        if (importMap) {
            importWorld2(arena, true);
        } else {
            if (!Bukkit.getWorlds().contains(Bukkit.getWorld(arena.getNameWorld()))) {
                main.getArenaManager().loadWorld(arena.getNameWorld());
            }

            World world = Bukkit.getWorld(arena.getNameWorld());

            for (Chunk chunk : world.getLoadedChunks()) {
                chunk.unload(false);
                chunk.load();
            }


            main.getArenaManager().loadWorld(arena.getNameWorld());
            arena.loadSpawns(arena.getNameWorld());

            arena.setGameStatus(Enums.GameStatus.WAITING);
            arena.setUsable(false);
            if (arena.getISign() != null) arena.getISign().updateSign();
        }
    }

    private void unLoadWorld(String worldName) {
        File target = new File(this.main.getServer().getWorldContainer(), worldName);
        World world = Bukkit.getWorld(worldName);

        if (target.isDirectory() && world != null) {
            if (this.main.getMvWorldManager() != null) {
                this.main.getMvWorldManager().unloadWorld(worldName, false);
            } else {
                Bukkit.getServer().unloadWorld(world, false);
                target.delete();
            }
        }
    }

    public void copyDir(File source, File target) {
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
            try {
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
            } catch (IOException e){
                Console.error(e.getMessage());
            }
        }
    }
}
