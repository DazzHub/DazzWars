package com.dazzhub.skywars.Utils.cages;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Cuboid;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.inventory.menu.IMenu;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import com.dazzhub.skywars.Utils.kits.IKit;
import com.dazzhub.skywars.Utils.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
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

        configCreate.get().setup(main, "Cages/solo/Default");
        configCreate.get().setup(main, "Cages/team/Default");

        cagesolo.forEach(this::importSchematicSolo);
        cageteam.forEach(this::importSchematicTeam);

        createMenuSolo();
        createMenuTeam();

        Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &eLoaded cages solo: &a"+getCagesSolo().size()));
        Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &eLoaded cages team: &a"+getCagesTeam().size()));
    }

    public void createCage(Integer price, String nameCage, String mode, Cuboid cuboid){

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

        File file = this.main.getConfigUtils().getFile(this.main, "Cages/" + mode + "/" + nameCage);
        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Cages/" + mode + "/"+ nameCage);

        setTemplate(price, file, config);

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

        Material material = config.isInt("ITEM.ICON-ITEM") ? Material.getMaterial(config.getInt("ITEM.ICON-ITEM")) : Material.getMaterial(config.getString("ITEM.ICON-ITEM"));

        if (mode.equals("solo")){

            cagesSolo.put(nameCage,
                    new ICage(nameCage, xDiff, yDiff, zDiff, blockList,
                        config.getString("ITEM.ACTION"),
                        config.getInt("ITEM.PRICE"),
                        config.getString("ITEM.NAME"),
                        config.getStringList("ITEM.DESCRIPTION"),
                        config.getStringList("ITEM.DESCRIPTION-PURCHASED"),
                        config.getStringList("ITEM.DESCRIPTION-SELECTED"),
                        material,
                        (short) config.getInt("ITEM.DATA-VALUE"),
                        config.getString("ITEM.PERMISSION"),
                        Main.getRelativePosition(config.getInt("ITEM.POSITION-X"), config.getInt("ITEM.POSITION-Y"))
                    )
            );

        } else if (mode.equals("team")){
            cagesTeam.put(nameCage, new ICage(nameCage, xDiff, yDiff, zDiff, blockList,
                            config.getString("ITEM.ACTION"),
                            config.getInt("ITEM.PRICE"),
                            config.getString("ITEM.NAME"),
                            config.getStringList("ITEM.DESCRIPTION"),
                            config.getStringList("ITEM.DESCRIPTION-PURCHASED"),
                            config.getStringList("ITEM.DESCRIPTION-SELECTED"),
                            material,
                            (short) config.getInt("ITEM.DATA-VALUE"),
                            config.getString("ITEM.PERMISSION"),
                            Main.getRelativePosition(config.getInt("ITEM.POSITION-X"), config.getInt("ITEM.POSITION-Y"))
                    )
            );
        } else {
            Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cCage error create: " + nameCage));
        }


    }


    private void importSchematicSolo(String name) {
        File sf = new File(this.main.getDataFolder() + "/Cages/solo", name + ".yml");

        if (!sf.exists()) {
            Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cCage File does not exist " + name));
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);
        Material material = sc.isInt("ITEM.ICON-ITEM") ? Material.getMaterial(sc.getInt("ITEM.ICON-ITEM")) : Material.getMaterial(sc.getString("ITEM.ICON-ITEM"));

        cagesSolo.put(sc.getString("Name"), new ICage(sc.getString("Name"), sc.getInt("Diff.x"), sc.getInt("Diff.y"), sc.getInt("Diff.z"), sc.getStringList("Blocks"),
                        sc.getString("ITEM.ACTION"),
                        sc.getInt("ITEM.PRICE"),
                        sc.getString("ITEM.NAME"),
                        sc.getStringList("ITEM.DESCRIPTION"),
                        sc.getStringList("ITEM.DESCRIPTION-PURCHASED"),
                        sc.getStringList("ITEM.DESCRIPTION-SELECTED"),
                        material,
                        (short) sc.getInt("ITEM.DATA-VALUE"),
                        sc.getString("ITEM.PERMISSION"),
                        Main.getRelativePosition(sc.getInt("ITEM.POSITION-X"), sc.getInt("ITEM.POSITION-Y"))
                )
        );
    }

    private void importSchematicTeam(String name) {
        File sf = new File(this.main.getDataFolder() + "/Cages/team", name + ".yml");

        if (!sf.exists()) {
            Bukkit.getConsoleSender().sendMessage(c("&9SkyWars &8> &cCage File does not exist " + name));
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);
        Material material = sc.isInt("ITEM.ICON-ITEM") ? Material.getMaterial(sc.getInt("ITEM.ICON-ITEM")) : Material.getMaterial(sc.getString("ITEM.ICON-ITEM"));

        cagesTeam.put(sc.getString("Name"), new ICage(sc.getString("Name"), sc.getInt("Diff.x"), sc.getInt("Diff.y"), sc.getInt("Diff.z"), sc.getStringList("Blocks"),
                        sc.getString("ITEM.ACTION"),
                        sc.getInt("ITEM.PRICE"),
                        sc.getString("ITEM.NAME"),
                        sc.getStringList("ITEM.DESCRIPTION"),
                        sc.getStringList("ITEM.DESCRIPTION-PURCHASED"),
                        sc.getStringList("ITEM.DESCRIPTION-SELECTED"),
                        material,
                        (short) sc.getInt("ITEM.DATA-VALUE"),
                        sc.getString("ITEM.PERMISSION"),
                        Main.getRelativePosition(sc.getInt("ITEM.POSITION-X"), sc.getInt("ITEM.POSITION-Y"))
                )
        );
    }

    private void setTemplate(int price, File file, FileConfiguration config){
        Configuration configcage = this.main.getConfigUtils().getConfig(this.main, "Cages/cages");

        config.set("ITEM.PRICE", price);
        config.set("ITEM.ACTION", "buycage: %cage%");
        config.set("ITEM.NAME", configcage.getString("templateCage.NAME"));
        config.set("ITEM.DESCRIPTION", configcage.getStringList("templateCage.DESCRIPTION"));
        config.set("ITEM.DESCRIPTION-PURCHASED", configcage.getStringList("templateCage.DESCRIPTION-PURCHASED"));
        config.set("ITEM.DESCRIPTION-SELECTED", configcage.getStringList("templateCage.DESCRIPTION-SELECTED"));
        config.set("ITEM.ICON-ITEM", 0);
        config.set("ITEM.DATA-VALUE", 0);
        config.set("ITEM.POSITION-X", 0);
        config.set("ITEM.POSITION-Y", 0);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createMenuSolo() {
        Configuration config = main.getConfigUtils().getConfig(this.main, "Cages/cages");
        if (cagesSolo.isEmpty()) return;

        HashMap<Integer, ordItems> items = new HashMap<>();
        for (String solo : cagesSolo.keySet()) {
            ICage iCage = cagesSolo.get(solo);
            items.put(iCage.getSlot(), new ordItems(iCage.icon(), iCage.getSlot(), iCage.getActionCage().replace("%cage%", iCage.getName() + "/" + "SOLO" + "/" + iCage.getPrice()), iCage.getPermission(), null));
        }

        String menuName = config.getString("menu-settings.name").replace("{type}", "Solo");
        int menuRows = config.getInt("menu-settings.rows");
        String menuCommand = config.getString("menu-settings.command").replace("{type}", "solo");

        menuName = "§r" + menuName.replace("&", "§");
        if (menuName.length() > 32) {
            menuName = "§rError, name too long!";
        }

        IMenu iMenu = new IMenu(menuName, menuRows, menuCommand, items);

        main.getMenuManager().getMenuFileName().put("cagesolo", iMenu);
        main.getMenuManager().getMenuTileName().put(menuName, iMenu);
        main.getMenuManager().getMenuCommand().put(menuCommand, "cagesolo");
    }

    private void createMenuTeam() {
        Configuration config = main.getConfigUtils().getConfig(this.main, "Cages/cages");
        if (cagesTeam.isEmpty()) return;

        HashMap<Integer, ordItems> items = new HashMap<>();
        for (String solo : cagesTeam.keySet()) {
            ICage iCage = cagesTeam.get(solo);
            items.put(iCage.getSlot(), new ordItems(iCage.icon(), iCage.getSlot(), iCage.getActionCage().replace("%cage%", iCage.getName() + "/" + "TEAM" + "/" + iCage.getPrice()), iCage.getPermission(), null));
        }

        String menuName = config.getString("menu-settings.name").replace("{type}", "Team");
        int menuRows = config.getInt("menu-settings.rows");
        String menuCommand = config.getString("menu-settings.command").replace("{type}", "team");

        menuName = "§r" + menuName.replace("&", "§");
        if (menuName.length() > 32) {
            menuName = "§rError, name too long!";
        }

        IMenu iMenu = new IMenu(menuName, menuRows, menuCommand, items);

        main.getMenuManager().getMenuFileName().put("cageteam", iMenu);
        main.getMenuManager().getMenuTileName().put(menuName, iMenu);
        main.getMenuManager().getMenuCommand().put(menuCommand, "cageteam");
    }

    public String c(String msg){ return ChatColor.translateAlternateColorCodes('&', msg); }
}
