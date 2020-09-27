package com.dazzhub.skywars.Utils.signs;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.Configuration;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

import java.util.List;

@Getter
@Setter
public class ISign {

    private Main main;

    private Sign sign;
    private Arena arena;

    public ISign(Sign sign, Arena arena) {
        this.main = Main.getPlugin();

        this.sign = sign;
        this.arena = arena;
    }

    public void updateSign() {
        Configuration config = main.getSigns();
        List<String> lines = config.getStringList("Format");

        for (int i = 0; i < 4; ++i) {
            this.sign.setLine(i, c(replaceLines(lines.get(i))));
        }

        Bukkit.getScheduler().runTaskLater(main, () -> {
            this.sign.update();
            this.updateAttached();
        },5);
    }

    private void updateAttached() {
        Block block = this.getAttachedBlock(sign.getLocation().getBlock());

        if (main.checkVersion()) {
            block.setType(arena.getBlockStatus().parseItem().getType());
            block.setData(arena.getBlockStatus().parseItem().getData().getData());
        } else {
            block.setType(arena.getBlockStatus().parseMaterial());
        }

        block.getState().update(true);
    }

    private String replaceLines(String s){
        s = s.replace("%status%", arena.getStatusMsg());
        s = s.replace("%map%", format(arena.getNameArena()));
        s = s.replace("%online%", String.valueOf(arena.getPlayers().size()));
        s = s.replace("%max%", String.valueOf(arena.getMaxPlayers()));
        return s;
    }

    public Block getAttachedBlock(Block b) {
        MaterialData m = b.getState().getData();
        BlockFace face = BlockFace.DOWN;

        if (m instanceof Directional) {
            face = ((Directional)m).getFacing().getOppositeFace();
        }
        return b.getRelative(face);
    }

    private String format(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private String c(final String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
