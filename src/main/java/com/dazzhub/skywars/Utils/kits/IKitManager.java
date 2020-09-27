package com.dazzhub.skywars.Utils.kits;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Console;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import com.dazzhub.skywars.Utils.inventory.menu.IMenu;
import com.dazzhub.skywars.Utils.inventory.ordItems;
import com.cryptomorin.xseries.XSound;
import lombok.Getter;
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
import java.util.ListIterator;
import java.util.stream.Collectors;

import static com.cryptomorin.xseries.XSound.ENTITY_VILLAGER_YES;

@Getter
public class IKitManager {

    private Main main;

    private HashMap<String, IKit> kitSoloHashMap;
    private HashMap<String, IKit> kitTeamHashMap;

    public IKitManager(Main main) {
        this.main = main;
        this.kitSoloHashMap = new HashMap<>();
        this.kitTeamHashMap = new HashMap<>();
    }

    public void loadKits(){
        configCreate.get().setup(main, "Kits/kits");
        configCreate.get().setup(main, "Kits/solo/Default");
        configCreate.get().setup(main, "Kits/team/Default");

        List<String> kitsololist = main.getConfigUtils().getConfig(this.main, "Kits/kits").getStringList("kitsolo");
        List<String> kitteamlist = main.getConfigUtils().getConfig(this.main, "Kits/kits").getStringList("kitteam");

        kitsololist.forEach(this::importKitSolo);
        kitteamlist.forEach(this::importKitTeam);

        createMenuSolo();
        createMenuTeam();

        Console.info("&eLoaded kits solo: &a" + kitSoloHashMap.size());
        Console.info("&eLoaded kits team: &a" + kitTeamHashMap.size());
    }

    public void giveKit(String namekit, String mode, Player p, GamePlayer gamePlayer){
        if (mode.equalsIgnoreCase("SOLO")){
            if (getKitSoloHashMap().containsKey(namekit)){
                IKit iKit = getKitSoloHashMap().get(namekit);

                iKit.giveKit(p, mode.toLowerCase());

                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Received").replace("%kit%", namekit));
            }
        } else if (mode.equalsIgnoreCase("TEAM")){
            if (getKitTeamHashMap().containsKey(namekit)){
                IKit iKit = getKitTeamHashMap().get(namekit);

                iKit.giveKit(p, mode.toLowerCase());

                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Received").replace("%kit%", namekit));
            }
        }
    }

    public void createKit(Player p, String namekit, int price, String mode){
        if (mode.equals("solo") && kitSoloHashMap.containsKey(namekit)){
            p.sendMessage(c("&a&l\u2714 &fKit &e" + namekit + "&f already exists"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
            return;
        }

        if (mode.equals("team") && kitTeamHashMap.containsKey(namekit)){
            p.sendMessage(c("&a&l\u2714 &fKit &e" + namekit + "&f already exists"));
            XSound.play(p, String.valueOf(XSound.ENTITY_VILLAGER_NO.parseSound()));
            return;
        }

        File file = this.main.getConfigUtils().getFile(this.main, "Kits/" + mode + "/" + namekit);

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Console.error(e.getMessage());
            }
        }

        FileConfiguration config = this.main.getConfigUtils().getConfig(this.main, "Kits/" + mode + "/" + namekit);

        File file2 = this.main.getConfigUtils().getFile(this.main, "Kits/kits");
        FileConfiguration config2 = this.main.getConfigUtils().getConfig(this.main, "Kits/kits");

        switch (mode){
            case "solo":{
                setTemplate(price, file, config);
                saveInventory(p, file, config);

                importKitSolo(namekit);
                break;
            }
            case "team":{
                setTemplate(price, file, config);
                saveInventory(p, file, config);

                importKitTeam(namekit);
                break;
            }
            default:{
                break;
            }
        }

        List<String> kits = config2.getStringList("kit"+mode);
        kits.add(namekit);

        config2.set("kit"+mode, kits);

        try {
            config2.save(file2);
        } catch (IOException e) {
            Console.error(e.getMessage());
        }

        p.sendMessage(c("&a&l\u2714 &fKit &e" + namekit + "&f has been created"));
        XSound.play(p, String.valueOf(ENTITY_VILLAGER_YES.parseSound()));
    }

    private void setTemplate(int price, File file, FileConfiguration config){
        Configuration configkits = this.main.getConfigUtils().getConfig(this.main, "Kits/kits");

        config.set("ITEM.PRICE", price);
        config.set("ITEM.ACTION", "kit: %name%");
        config.set("ITEM.NAME", configkits.getString("templateKit.NAME"));
        config.set("ITEM.DESCRIPTION", configkits.getStringList("templateKit.DESCRIPTION"));
        config.set("ITEM.DESCRIPTION-PURCHASED", configkits.getStringList("templateKit.DESCRIPTION-PURCHASED"));
        config.set("ITEM.DESCRIPTION-SELECTED", configkits.getStringList("templateKit.DESCRIPTION-SELECTED"));
        config.set("ITEM.ICON-ITEM", 0);
        config.set("ITEM.DATA-VALUE", 0);
        config.set("ITEM.POSITION-X", 0);
        config.set("ITEM.POSITION-Y", 0);

        try {
            config.save(file);
        } catch (IOException e) {
            Console.error(e.getMessage());
        }
    }


    private void saveInventory(Player p, File file, FileConfiguration config) {
        List<String> tempInfo = new ArrayList<>();

        for (int i = 0; i < p.getInventory().getContents().length; ++i) {
            if (p.getInventory().getContents() != null) {
                ItemStack item = p.getInventory().getItem(i);
                if (item != null) {
                    tempInfo.add(item.getAmount() + " " + item.getType().name().replaceAll("_", ""));
                    config.set("KIT.Inventory." + i, item);
                }
            }
        }

        ArrayList<ItemStack> armor = new ArrayList<>();
        ItemStack[] contents = p.getInventory().getArmorContents();

        for (ItemStack armors : contents) {
            if (armors != null) {
                tempInfo.add(armors.getType().name().replaceAll("_", "").replaceAll("AIR", ""));
                armor.add(armors);
            }
        }

        config.set("KIT.Armor", armor);

        List<String> items = config.getStringList("ITEM.DESCRIPTION");
        tempInfo.stream().map(info -> "&7➲ &e" + info).forEach(items::add);

        config.set("ITEM.DESCRIPTION", items);

        try {
            config.save(file);
        } catch (IOException e) {
            Console.error(e.getMessage());
        }
    }

    private void importKitSolo(String name) {
        File sf = main.getConfigUtils().getFile(this.main, "Kits/solo/" + name);

        if (!sf.exists()) {
            Console.warning("&cKit File does not exist " + name);
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);

        Material material = sc.isInt("ITEM.ICON-ITEM") ? Material.getMaterial(sc.getInt("ITEM.ICON-ITEM")) : Material.getMaterial(sc.getString("ITEM.ICON-ITEM"));

        kitSoloHashMap.put(sf.getName().toLowerCase().replace(".yml", ""),
                new IKit(
                        sf.getName().replace(".yml", ""),
                        sc.getString("ITEM.ACTION"),
                        sc.getInt("ITEM.PRICE"),
                        sc.getString("ITEM.NAME"),
                        sc.getStringList("ITEM.DESCRIPTION"),
                        sc.getStringList("ITEM.DESCRIPTION-PURCHASED"),
                        sc.getStringList("ITEM.DESCRIPTION-SELECTED"),
                        material,
                        (short) sc.getInt("ITEM.DATA-VALUE"),
                        sc.getString("ITEM.PERMISSION"),
                        Main.getRelativePosition( sc.getInt("ITEM.POSITION-X"), sc.getInt("ITEM.POSITION-Y"))
                )
        );

    }

    private void importKitTeam(String name) {
        File sf = main.getConfigUtils().getFile(this.main, "Kits/team/" + name);

        if (!sf.exists()) {
            Console.warning("&cKit File does not exist " + name);
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);

        Material material = sc.isInt("ITEM.ICON-ITEM") ? Material.getMaterial(sc.getInt("ITEM.ICON-ITEM")) : Material.getMaterial(sc.getString("ITEM.ICON-ITEM"));

        kitTeamHashMap.put(sf.getName().toLowerCase().replace(".yml", ""),
                new IKit(
                        sf.getName().replace(".yml", ""),
                        sc.getString("ITEM.ACTION"),
                        sc.getInt("ITEM.PRICE"),
                        sc.getString("ITEM.NAME"),
                        sc.getStringList("ITEM.DESCRIPTION"),
                        sc.getStringList("ITEM.DESCRIPTION-PURCHASED"),
                        sc.getStringList("ITEM.DESCRIPTION-SELECTED"),
                        material,
                        (short) sc.getInt("ITEM.DATA-VALUE"),
                        sc.getString("ITEM.PERMISSION"),
                        Main.getRelativePosition( sc.getInt("ITEM.POSITION-X"), sc.getInt("ITEM.POSITION-Y"))
                )
        );

    }

    private void createMenuSolo() {
        Configuration config = main.getConfigUtils().getConfig(this.main, "Kits/kits");
        if (kitSoloHashMap.isEmpty()) return;
        HashMap<Integer, ordItems> items = new HashMap<>();
        for (String solo : kitSoloHashMap.keySet()) {
            IKit iKit = kitSoloHashMap.get(solo);
            items.put(iKit.getSlot(), new ordItems(iKit.icon(), iKit.getSlot(), iKit.getActionKit().replace("%name%", iKit.getNameKit() + "/" + "SOLO" + "/" + iKit.getPrice()), iKit.getPermission(), null));
        }

        String menuName = config.getString("menu-settings.name").replace("{type}", "Solo");
        int menuRows = config.getInt("menu-settings.rows");
        String menuCommand = config.getString("menu-settings.command").replace("{type}", "solo");

        menuName = "§r" + menuName.replace("&", "§");
        if (menuName.length() > 32) {
            menuName = "§rError, name too long!";
        }

        IMenu iMenu = new IMenu(menuName, menuRows, menuCommand, items);

        main.getMenuManager().getMenuFileName().put("kitsolo", iMenu);
        main.getMenuManager().getMenuTileName().put(menuName, iMenu);
        main.getMenuManager().getMenuCommand().put(menuCommand, "kitsolo");
    }

    private void createMenuTeam() {
        Configuration config = main.getConfigUtils().getConfig(this.main, "Kits/kits");
        if (kitTeamHashMap.isEmpty()) return;

        HashMap<Integer, ordItems> items = new HashMap<>();
        for (String team : kitTeamHashMap.keySet()) {
            IKit iKit = kitTeamHashMap.get(team);
            items.put(iKit.getSlot(), new ordItems(iKit.icon(), iKit.getSlot(), iKit.getActionKit().replace("%name%", iKit.getNameKit() + "/" + "TEAM" + "/" + iKit.getPrice()), iKit.getPermission(), null));
        }

        String menuName = config.getString("menu-settings.name").replace("{type}", "Team");
        int menuRows = config.getInt("menu-settings.rows");
        String menuCommand = config.getString("menu-settings.command").replace("{type}", "team");

        menuName = "§r" + menuName.replace("&", "§");
        if (menuName.length() > 32) {
            menuName = "§rError, name too long!";
        }

        IMenu iMenu = new IMenu(menuName, menuRows, menuCommand, items);

        main.getMenuManager().getMenuFileName().put("kitteam", iMenu);
        main.getMenuManager().getMenuTileName().put(menuName, iMenu);
        main.getMenuManager().getMenuCommand().put(menuCommand, "kitteam");
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
