package com.dazzhub.skywars.Utils.signs;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ISignLocation {

    public static Location Center(Location location) {
        return new Location(location.getWorld(),
                getRelativeCoord(location.getBlockX()),
                getRelativeCoord(location.getBlockY()),
                getRelativeCoord(location.getBlockZ())
        );
    }

    private static double getRelativeCoord(int n) {
        return ((double) n < 0.0) ? ((double) n + 0.5) : ((double) n + 0.5);
    }

    public static String getString(Location location, boolean b) {
        if (b) {
            return location.getWorld().getName()
                    + ":" + Center(location).getX()
                    + ":" + location.getY()
                    + ":" + Center(location).getZ()
                    + ":" + 0
                    + ":" + location.getYaw();
        }
        return location.getWorld().getName()
                + ":" + location.getX()
                + ":" + location.getY()
                + ":" + location.getZ()
                + ":" + location.getPitch()
                + ":" + location.getYaw();
    }

    public static Location getLocation(String s) {
        final String[] split = s.split(":");
        return new Location(
                Bukkit.getWorld(split[0]),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Float.parseFloat(split[5]),
                Float.parseFloat("0"));
    }

}
