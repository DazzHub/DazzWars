package com.dazzhub.skywars.Utils;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.Serializable;
import java.util.*;

@Data
public class Cuboid implements Iterable<Block>, Serializable {

    private String w;
    private int xmax;
    private int ymax;
    private int zmax;

    private int xmin;
    private int ymin;
    private int zmin;


    //new
    public Cuboid(Cuboid cuboid){
        this.w = cuboid.getWorld();
        this.xmax = cuboid.getXmax();
        this.ymax = cuboid.getYmax();
        this.zmax = cuboid.getZmax();

        this.xmin = cuboid.getXmin();
        this.ymin = cuboid.getYmin();
        this.zmin = cuboid.getZmin();
    }

    //create
    public Cuboid(Location l1, Location l2){
        if(l1.getWorld().getName().equals(l2.getWorld().getName())){
            this.w = l1.getWorld().getName();
            this.xmax = Math.max(l1.getBlockX(), l2.getBlockX());
            this.ymax = Math.max(l1.getBlockY(), l2.getBlockY());
            this.zmax = Math.max(l1.getBlockZ(), l2.getBlockZ());

            this.xmin = Math.min(l1.getBlockX(), l2.getBlockX());
            this.ymin = Math.min(l1.getBlockY(), l2.getBlockY());
            this.zmin = Math.min(l1.getBlockZ(), l2.getBlockZ());
        }
    }

    public Cuboid(String world, int xmax, int ymax, int zmax, int xmin, int ymin, int zmin){
        this.w = world;
        this.xmax = xmax;
        this.ymax = ymax;
        this.zmax = zmax;

        this.xmin = xmin;
        this.ymin = ymin;
        this.zmin = zmin;
    }

    public Cuboid(String s) {
        String[] split = s.split(", ");
        this.w = Bukkit.getServer().getWorld(split[0]).getName();
        this.xmax = Integer.parseInt(split[1]);
        this.ymax = Integer.parseInt(split[2]);
        this.zmax = Integer.parseInt(split[3]);

        this.xmin = Integer.parseInt(split[4]);
        this.ymin = Integer.parseInt(split[5]);
        this.zmin = Integer.parseInt(split[6]);
    }

    public String getWorld() {
        return w;
    }

    @Override
    public Iterator<Block> iterator() {
        return new CuboidIterator(new Cuboid(this.w, this.xmax, this.ymax, this.zmax, this.xmin, this.ymin, this.zmin));
    }

    public boolean isIn(Location loc){
        return xmin <= loc.getX() && xmax >= loc.getX() &&
                ymin <= loc.getY() && ymax >= loc.getY()
                && zmin <= loc.getZ() && zmax >= loc.getX()
                && w.equals(loc.getWorld().getName()
        );
    }

    public boolean hasLocInside(Location loc) {
        boolean locXMax = Math.floor(loc.getX()) >= getXmin();
        boolean locXMin = Math.floor(loc.getX()) <= getXmax();

        boolean locZMax = Math.floor(loc.getZ()) <= getZmax();
        boolean locZMin = Math.floor(loc.getZ()) >= getZmin();

        boolean locYMax = Math.floor(loc.getY()) <= getYmax();
        boolean locYMin = Math.floor(loc.getY()) >= getYmin();


        return locXMin && locXMax && locYMin && locYMax && locZMin && locZMax;
    }

    public boolean hasBlock(Block block){
        Location loc = block.getLocation();
        return xmin <= loc.getX() && xmax >= loc.getX() && ymin <= loc.getY() && ymax >= loc.getY() && zmin <= loc.getZ() && zmax >= loc.getX() && w.equals(loc.getWorld().getName());
    }

    @Override
    public String toString() {
        return this.w
                + ", " + this.xmax
                + ", " + this.ymax
                + ", " + this.zmax
                + ", " + this.xmin
                + ", " + this.ymin
                + ", " + this.zmin;
    }

    @Data
    public class CuboidIterator implements Iterator<Block>{

        private Cuboid cci;
        private String wci;
        private int baseX;
        private int baseY;
        private int baseZ;
        private int sizeX;
        private int sizeY;
        private int sizeZ;
        private int x, y, z;
        private ArrayList<Block> blocks;
        private Map<Location, Material> blocks2;
        private ArrayList<Location> blocks3;

        public CuboidIterator(Cuboid c){
            this.cci = c;
            this.wci = c.getWorld();
            this.baseX = getXmin();
            this.baseY = getYmin();
            this.baseZ = getZmin();
            this.sizeX = Math.abs(getXmax() - getXmin()) + 1;
            this.sizeY = Math.abs(getYmax() - getYmin()) + 1;
            this.sizeZ = Math.abs(getZmax() - getZmin()) + 1;
            this.x = this.y = this.z = 0;
        }

        public Cuboid getCuboid(){
            return cci;
        }

        public boolean hasNext() {
            return x < sizeX && y < sizeY && z < sizeZ;
        }

        public Block next() {
            Block b = Bukkit.getWorld(w).getBlockAt(baseX + x, baseY + y, baseZ + z);
            if (++x >= sizeX) {
                x = 0;
                if (++y >= sizeY) {
                    y = 0;
                    ++z;
                }
            }
            return b;
        }

        public Map<Location, Material> getBlockAtLocations(){
            blocks2 = new HashMap<>();
            for(int x = cci.getXmin(); x <= cci.getXmax(); x++){
                for(int y = cci.getYmin(); y <= cci.getYmax(); y++){
                    for(int z = cci.getZmin(); z <= cci.getZmax(); z++){
                        blocks2.put(new Location(Bukkit.getWorld(w), x, y, z), Bukkit.getWorld(getWorld()).getBlockAt(x, y, z).getType());
                    }
                }
            }
            return blocks2;
        }

        public Collection<Location> getLocations(){
            blocks3 = new ArrayList<>();
            for(int x = cci.getXmin(); x <= cci.getXmax(); x++){
                for(int y = cci.getYmin(); y <= cci.getYmax(); y++){
                    for(int z = cci.getZmin(); z <= cci.getZmax(); z++){
                        blocks3.add(new Location(Bukkit.getWorld(getWorld()), x, y, z));
                    }
                }
            }
            return blocks3;
        }

        public Collection<Block> iterateBlocks(){
            blocks = new ArrayList<>();
            for(int x = cci.getXmin(); x <= cci.getXmax(); x++){
                for(int y = cci.getYmin(); y <= cci.getYmax(); y++){
                    for(int z = cci.getZmin(); z <= cci.getZmax(); z++){
                        blocks.add(Bukkit.getWorld(cci.getWorld()).getBlockAt(x, y, z));
                    }
                }
            }
            return blocks;
        }

    }

    @Override
    public Cuboid clone() {
        return new Cuboid(this);
    }
}
