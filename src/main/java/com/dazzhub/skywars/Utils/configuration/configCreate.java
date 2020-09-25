package com.dazzhub.skywars.Utils.configuration;

import org.bukkit.plugin.Plugin;

import java.io.File;

public class configCreate
{
    private static configCreate instance;
    private File configFile;

    public static configCreate get() {
        if (instance == null) {
            instance = new configCreate();
        }
        return instance;
    }

    public void setup(Plugin p, String configname) {
        File pluginDir = p.getDataFolder();
        this.configFile = new File(pluginDir, configname + ".yml");
        if (!this.configFile.exists()) {
            p.saveResource(configname + ".yml", false);
        }
    }
    //s
    public void setupSchematic(Plugin p, String configname) {
        File pluginDir = p.getDataFolder();
        this.configFile = new File(pluginDir, configname + ".schematic");
        if (!this.configFile.exists()) {
            p.saveResource(configname + ".schematic", false);
        }
    }
}
