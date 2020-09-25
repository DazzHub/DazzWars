package com.dazzhub.skywars.Utils.resetWorld;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class EmptyChunk extends ChunkGenerator {

    private static EmptyChunk instance = new EmptyChunk();

    private byte[] buf = new byte[0x10000];

    public static EmptyChunk getInstance() {
        return EmptyChunk.instance;
    }

    public byte[] generate(World world, Random random, int x, int z) {
        return buf;
    }
}
