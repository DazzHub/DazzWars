package com.dazzhub.skywars.Utils.signs.top;

import com.dazzhub.skywars.Main;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.configuration.Configuration;

import java.util.List;

@Getter
@Setter
public class ITop {

    private Main main;

    private List<String> exlines;
    private String owner;
    private Sign sign;

    public ITop(Sign sign, String top, List<String> exlines) {
        this.main = Main.getPlugin();

        this.exlines = exlines;
        this.owner = top;
        this.sign = sign;
    }

    public void updateSign() {
        Configuration config = main.getSigns();
        List<String> lines = config.getStringList("FormatTop");

        for (int i = 0; i < 4; ++i) {
            this.sign.setLine(i, c(replaceLines(lines.get(i))));
        }

        Bukkit.getScheduler().runTaskLater(main, () -> {
            this.sign.update();
            this.updateAttached();
        },5);
    }

    private void updateAttached() {
        String owner = this.owner.split(":")[0];

        if (owner.contains("error") || owner.contains("NO_PLAYER")) {
            owner = "MHF_Question";
        }

        Block block = null;
        Location[] array;
        for (int length = (array = new Location[] {sign.getLocation().add(0.0, 1.0, 0.0), sign.getBlock().getRelative(((org.bukkit.material.Sign)sign.getData()).getAttachedFace()).getLocation().add(0.0, 1.0, 0.0) }).length, i = 0; i < length; ++i) {
            final Location location = array[i];
            if (location.getBlock().getType().equals(Material.SKULL)) {
                block = location.getBlock();
            }
        }

        if (block != null) {
            Skull skull = (Skull)block.getState();
            skull.setSkullType(SkullType.PLAYER);
            skull.setOwner(owner);
            skull.update();
        }
    }

    private String replaceLines(String s){
        if (owner.contains("error")) return s;

        String[] owner = this.owner.split(":");

        s = s.replace("%rank%", exlines.get(1));
        s = s.replace("%top%", format(exlines.get(2).replace("%", "")));
        s = s.replace("%player%", owner[0]);
        s = s.replace("%count%", owner[1]);
        return s;
    }

    private String format(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private String c(final String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
