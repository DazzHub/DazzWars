package com.dazzhub.skywars.Utils.cages;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Cuboid;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class ICageManager {

    private Main main;

    private HashMap<String, ICage> cagesSolo;
    private HashMap<String, ICage> cagesTeam;

    public ICageManager(Main main) {
        this.main = main;
        this.cagesSolo = new HashMap<>();
        this.cagesTeam = new HashMap<>();
    }

    public void loadCages(){
        List<String> cagesolo = main.getConfigUtils().getConfig(this.main, "Cages/cages").getStringList("cagesolo");
        List<String> cageteam = main.getConfigUtils().getConfig(this.main, "Cages/cages").getStringList("cageteam");

        configCreate.get().setupSchematic(main, "Cages/solo/Default");
        configCreate.get().setupSchematic(main, "Cages/team/Default");

        cagesolo.forEach(this::importSchematicSolo);
        cageteam.forEach(this::importSchematicTeam);

        Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &eLoaded cages solo: &a"+getCagesSolo().size()));
        Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &eLoaded cages team: &a"+getCagesTeam().size()));
    }

    public void createCage(String nameCage, String mode, Cuboid cuboid){

        mode = mode.toLowerCase();

        int xDiff = Math.abs(cuboid.getXmax() - cuboid.getXmin());
        int yDiff = Math.abs(cuboid.getYmax() - cuboid.getYmin());
        int zDiff = Math.abs(cuboid.getZmax() - cuboid.getZmin());

        File file2 = this.main.getConfigUtils().getFile(this.main, "Cages/cages");
        FileConfiguration config2 = this.main.getConfigUtils().getConfig(this.main, "Cages/cages");

        try {
            List<String> list = this.main.getConfigUtils().getConfig(this.main, "Cages/cages").getStringList("cage" + mode);
            list.add(nameCage);

            config2.set("cage" + mode, list);
            config2.save(file2);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cError: " + e.getMessage()));
        }

        List<String> blockList = new ArrayList<>();

        cuboid.iterator().forEachRemaining(block -> {

            if (main.checkVersion()) {
                XMaterial material = XMaterial.matchXMaterial(block.getType().name(), block.getData()).get();
                blockList.add(material.name());
            } else {
                blockList.add(XMaterial.getMajorVersion(block.getType().name()));
            }
        });

        File file = this.main.getConfigUtils().getFileSchematic(this.main, "Cages/" + mode + "/" + nameCage);
        FileConfiguration config = this.main.getConfigUtils().getConfigSchem(this.main, "Cages/" + mode + "/"+ nameCage);

        config.set("Name", nameCage);
        config.set("Diff.x", xDiff);
        config.set("Diff.y", yDiff);
        config.set("Diff.z", zDiff);
        config.set("Blocks", blockList);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mode.equals("solo")){
            cagesSolo.put(nameCage, new ICage(nameCage, xDiff, yDiff, zDiff, blockList));
        } else if (mode.equals("team")){
            cagesTeam.put(nameCage, new ICage(nameCage, xDiff, yDiff, zDiff, blockList));
        } else {
            Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cCage error create: " + nameCage));
        }
    }


    private void importSchematicSolo(String name) {
        File sf = new File(this.main.getDataFolder() + "/Cages/solo", name + ".schematic");

        if (!sf.exists()) {
            Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cCage File does not exist " + name));
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);
        cagesSolo.put(sc.getString("Name"), new ICage(sc.getString("Name"), sc.getInt("Diff.x"), sc.getInt("Diff.y"), sc.getInt("Diff.z"), sc.getStringList("Blocks")));
    }

    private void importSchematicTeam(String name) {
        File sf = new File(this.main.getDataFolder() + "/Cages/team", name + ".schematic");

        if (!sf.exists()) {
            Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cCage File does not exist " + name));
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);
        cagesTeam.put(sc.getString("Name"), new ICage(sc.getString("Name"), sc.getInt("Diff.x"), sc.getInt("Diff.y"), sc.getInt("Diff.z"), sc.getStringList("Blocks")));
    }

    public String c(String msg){ return ChatColor.translateAlternateColorCodes('&', msg); }
}
