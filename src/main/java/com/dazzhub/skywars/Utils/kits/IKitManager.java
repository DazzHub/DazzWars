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
    private HashMap<String, IKit> kitRankedHashMap;

    public IKitManager(Main main) {
        this.main = main;
        this.kitSoloHashMap = new HashMap<>();
        this.kitTeamHashMap = new HashMap<>();
        this.kitRankedHashMap = new HashMap<>();
    }

    public void loadKits(){
        configCreate.get().setup(main, "Kits/kits");

        configCreate.get().setup(main, "Kits/solo/Default");
        configCreate.get().setup(main, "Kits/solo/Vip");
        configCreate.get().setup(main, "Kits/solo/Builder");

        configCreate.get().setup(main, "Kits/team/Default");
        configCreate.get().setup(main, "Kits/ranked/Default");

        List<String> kitsololist = main.getConfigUtils().getConfig(this.main, "Kits/kits").getStringList("kitsolo");
        List<String> kitteamlist = main.getConfigUtils().getConfig(this.main, "Kits/kits").getStringList("kitteam");
        List<String> kitrankedlist = main.getConfigUtils().getConfig(this.main, "Kits/kits").getStringList("kitranked");

        kitsololist.forEach(this::importKitSolo);
        kitteamlist.forEach(this::importKitTeam);
        kitrankedlist.forEach(this::importKitRanked);

        Console.info("&eLoaded kits solo: &a" + kitSoloHashMap.size());
        Console.info("&eLoaded kits team: &a" + kitTeamHashMap.size());
        Console.info("&eLoaded kits ranked: &a" + kitRankedHashMap.size());
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
        } else if (mode.equalsIgnoreCase("RANKED")){
            if (getKitRankedHashMap().containsKey(namekit)){
                IKit iKit = getKitRankedHashMap().get(namekit);

                iKit.giveKit(p, mode.toLowerCase());

                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Kit.Received").replace("%kit%", namekit));
            }
        }
    }

    public void createKit(Player p, String namekit, String mode){
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

        if (mode.equals("ranked") && kitRankedHashMap.containsKey(namekit)){
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
                saveInventory(p, file, config);

                importKitSolo(namekit);
                break;
            }
            case "team":{
                saveInventory(p, file, config);

                importKitTeam(namekit);
                break;
            }
            case "ranked":{
                saveInventory(p, file, config);

                importKitRanked(namekit);
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

    private void saveInventory(Player p, File file, FileConfiguration config) {
        for (int i = 0; i < p.getInventory().getContents().length; ++i) {
            if (p.getInventory().getContents() != null) {
                ItemStack item = p.getInventory().getItem(i);
                if (item != null) {
                    config.set("KIT.Inventory." + i, item);
                }
            }
        }

        ArrayList<ItemStack> armor = new ArrayList<>();
        ItemStack[] contents = p.getInventory().getArmorContents();

        for (ItemStack armors : contents) {
            if (armors != null) {
                armor.add(armors);
            }
        }
        config.set("KIT.Armor", armor);

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

        kitSoloHashMap.put(sf.getName().toLowerCase().replace(".yml", ""), new IKit(sf.getName().replace(".yml", "")));
    }

    private void importKitTeam(String name) {
        File sf = main.getConfigUtils().getFile(this.main, "Kits/team/" + name);

        if (!sf.exists()) {
            Console.warning("&cKit File does not exist " + name);
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);

        kitTeamHashMap.put(sf.getName().toLowerCase().replace(".yml", ""), new IKit(sf.getName().replace(".yml", "")));
    }

    private void importKitRanked(String name) {
        File sf = main.getConfigUtils().getFile(this.main, "Kits/ranked/" + name);

        if (!sf.exists()) {
            Console.warning("&cKit File does not exist " + name);
            return;
        }

        FileConfiguration sc = YamlConfiguration.loadConfiguration(sf);

        kitRankedHashMap.put(sf.getName().toLowerCase().replace(".yml", ""), new IKit(sf.getName().replace(".yml", "")));
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

}
