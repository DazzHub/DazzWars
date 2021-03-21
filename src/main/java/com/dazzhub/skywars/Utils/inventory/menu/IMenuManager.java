package com.dazzhub.skywars.Utils.inventory.menu;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.configuration.configCreate;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;

public class IMenuManager {

    private final Main main;

    private final HashMap<String, IMenuLang> menuLangs;

    public IMenuManager(Main main) {
        this.main = main;

        this.menuLangs = new HashMap<>();
    }

    public void loadFiles() {
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            loadDefaultFiles();

            File[] fileList = new File(main.getDataFolder(), "Inventory/Menu").listFiles();

            if (fileList == null) return;

            for (File file : fileList) {
                if (file.isDirectory()) {
                    menuLangs.put(file.getName(), new IMenuLang(main, file.getName()));
                }
            }
        });
    }

    private void loadDefaultFiles(){
        File menuFolder = new File(main.getDataFolder(), "Inventory/Menu");
        if (!menuFolder.exists()) {
            // es-ES
                configCreate.get().setup(main, "Inventory/Menu/es-ES/arenas");

                configCreate.get().setup(main, "Inventory/Menu/es-ES/vote");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/vote-chest");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/vote-heart");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/vote-time");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/vote-events");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/vote-scenarios");

                configCreate.get().setup(main, "Inventory/Menu/es-ES/shop");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/shop-solo");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/shop-team");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/shop-ranked");

                configCreate.get().setup(main, "Inventory/Menu/es-ES/cage-solo");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/cage-team");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/cage-ranked");

                configCreate.get().setup(main, "Inventory/Menu/es-ES/killeffect-solo");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/killeffect-team");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/killeffect-ranked");

                configCreate.get().setup(main, "Inventory/Menu/es-ES/kit-solo");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/kit-team");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/kit-ranked");

                configCreate.get().setup(main, "Inventory/Menu/es-ES/wineffect-solo");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/wineffect-team");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/wineffect-ranked");

                configCreate.get().setup(main, "Inventory/Menu/es-ES/traileffect-solo");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/traileffect-team");
                configCreate.get().setup(main, "Inventory/Menu/es-ES/traileffect-ranked");

                configCreate.get().setup(main, "Inventory/Menu/es-ES/lang-change");

                // en-EN
                configCreate.get().setup(main, "Inventory/Menu/en-EN/arenas");

                configCreate.get().setup(main, "Inventory/Menu/en-EN/vote");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/vote-chest");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/vote-heart");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/vote-time");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/vote-events");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/vote-scenarios");

                configCreate.get().setup(main, "Inventory/Menu/en-EN/shop");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/shop-solo");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/shop-team");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/shop-ranked");

                configCreate.get().setup(main, "Inventory/Menu/en-EN/cage-solo");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/cage-team");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/cage-ranked");

                configCreate.get().setup(main, "Inventory/Menu/en-EN/killeffect-solo");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/killeffect-team");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/killeffect-ranked");

                configCreate.get().setup(main, "Inventory/Menu/en-EN/kit-solo");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/kit-team");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/kit-ranked");

                configCreate.get().setup(main, "Inventory/Menu/en-EN/wineffect-solo");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/wineffect-team");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/wineffect-ranked");

                configCreate.get().setup(main, "Inventory/Menu/en-EN/traileffect-solo");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/traileffect-team");
                configCreate.get().setup(main, "Inventory/Menu/en-EN/traileffect-ranked");

                configCreate.get().setup(main, "Inventory/Menu/en-EN/lang-change");
        }

    }

    public HashMap<String, IMenuLang> getMenuLangs() {
        return menuLangs;
    }
}