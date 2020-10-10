package com.dazzhub.skywars.Utils.cages;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.Cuboid;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.inventory.menu.IMenu;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import com.dazzhub.skywars.Utils.kits.IKit;
import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.cryptomorin.xseries.XSound.ENTITY_VILLAGER_YES;

@Getter
@Setter
public class ICageManager {

    private Main main;

    private HashMap<String, ICage> cagesSolo;
    private HashMap<String, ICage> cagesTeam;
    private HashMap<String, ICage> cagesRanked;

    public ICageManager(Main main) {
        this.main = main;
        this.cagesSolo = new HashMap<>();
        this.cagesTeam = new HashMap<>();
        this.cagesRanked = new HashMap<>();
    }

    public void loadCages(){
        List<String> cagesolo = main.getConfigUtils().getConfig(this.main, "Cages/cages").getStringList("cagesolo");
        List<String> cageteam = main.getConfigUtils().getConfig(this.main, "Cages/cages").getStringList("cageteam");
        List<String> cageranked = main.getConfigUtils().getConfig(this.main, "Cages/cages").getStringList("cageranked");

        configCreate.get().setup(main, "Cages/solo/Default");
        configCreate.get().setup(main, "Cages/team/Default");
        configCreate.get().setup(main, "Cages/ranked/Default");

        for (String s : cagesolo) {
            importSchematicSolo(s);
        }

        for (String s : cageteam) {
            importSchematicTeam(s);
        }

        for (String s : cageranked) {
            importSchematicRanked(s);
        }

        Console.info("&eLoaded cages solo: &a"+this.cagesSolo.size());
        Console.info("&eLoaded cages team: &a"+this.cagesTeam.size());
        Console.info("&eLoaded cages ranked: &a"+this.cagesRanked.size());
    }

    public void createCage(Player p, String nameCage, String mode, Cuboid cuboid){
        mode = mode.toLowerCase();

        if (mode.equals("solo") && cagesSolo.containsKey(nameCage)){
            p.sendMessage(c("&a&l\u2714 &fKit &e" + nameCage + "&f already exists"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
            return;
        }

        if (mode.equals("team") && cagesTeam.containsKey(nameCage)){
            p.sendMessage(c("&a&l\u2714 &fKit &e" + nameCage + "&f already exists"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
            return;
        }

        if (mode.equals("ranked") && cagesRanked.containsKey(nameCage)){
            p.sendMessage(c("&a&l\u2714 &fKit &e" + nameCage + "&f already exists"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
            return;
        }

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
            Console.error(e.getMessage());
        }

        List<String> blockList = new ArrayList<>();

        cuboid.iterator().forEachRemaining(block -> {

            if (main.checkVersion()) {
                blockList.add(block.getType().name() + ":" + block.getData());
            } else {
                blockList.add(XMaterial.getMajorVersion(block.getType().name()));
            }
        });

        File file = this.main.getConfigUtils().getFile(this.main, "Cages/" + mode + "/" + nameCage);
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Cages/" + mode + "/"+ nameCage);

        config.set("Name", nameCage);
        config.set("Diff.x", xDiff);
        config.set("Diff.y", yDiff);
        config.set("Diff.z", zDiff);
        config.set("Blocks", blockList);

        try {
            config.save(file);
        } catch (IOException e) {
            Console.error(e.getMessage());
        }

        switch (mode) {
            case "solo":
                cagesSolo.put(nameCage, new ICage(nameCage, xDiff, yDiff, zDiff, blockList));
                break;
            case "team":
                cagesTeam.put(nameCage, new ICage(nameCage, xDiff, yDiff, zDiff, blockList));
                break;
            case "ranked":
                cagesRanked.put(nameCage, new ICage(nameCage, xDiff, yDiff, zDiff, blockList));
                break;
            default:
                Console.warning("&cCage error create: " + nameCage);
                break;
        }

        p.sendMessage(c("&a&l\u2714 &fCage &e" + nameCage + "&f created successfully"));
        XSound.play(p, String.valueOf(ENTITY_VILLAGER_YES.parseSound()));
    }


    private void importSchematicSolo(String name) {
        File sf = new File(this.main.getDataFolder(), "/Cages/solo/" + name + ".yml");

        if (!sf.exists()) {
            Console.warning("&cCage File does not exist " + name);
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);

        cagesSolo.put(sc.getString("Name"), new ICage(sc.getString("Name"), sc.getInt("Diff.x"), sc.getInt("Diff.y"), sc.getInt("Diff.z"), sc.getStringList("Blocks")));
    }

    private void importSchematicTeam(String name) {
        File sf = new File(this.main.getDataFolder(), "/Cages/team/" + name + ".yml");

        if (!sf.exists()) {
            Console.warning("&cCage File does not exist " + name);
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);

        cagesTeam.put(sc.getString("Name"), new ICage(sc.getString("Name"), sc.getInt("Diff.x"), sc.getInt("Diff.y"), sc.getInt("Diff.z"), sc.getStringList("Blocks")));
    }

    private void importSchematicRanked(String name) {
        File sf = new File(this.main.getDataFolder(), "/Cages/ranked/" + name + ".yml");

        if (!sf.exists()) {
            Console.warning("&cCage File does not exist " + name);
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);

        cagesRanked.put(sc.getString("Name"), new ICage(sc.getString("Name"), sc.getInt("Diff.x"), sc.getInt("Diff.y"), sc.getInt("Diff.z"), sc.getStringList("Blocks")));
    }

    public String c(String msg){ return ChatColor.translateAlternateColorCodes('&', msg); }
}
