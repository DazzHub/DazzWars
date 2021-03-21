package com.dazzhub.skywars.Utils.vault;

import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.Utils.Console;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public class EconomyAPI {

    private static Economy economy = null;

    public EconomyAPI(Main main) {
        main.getServer().getServicesManager().register((Class) Economy.class, (Object) new VaultAPI(), main, ServicePriority.Normal);
        economy = new VaultAPI();
    }

    public static Economy getEconomy() {
        if (economy == null){
            Console.error("You are trying to use Vault, but it is not in the plugins folder or it does not load");
            return null;
        } else {
            return economy;
        }
    }
}
