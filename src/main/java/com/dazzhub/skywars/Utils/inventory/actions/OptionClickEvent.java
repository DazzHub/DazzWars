package com.dazzhub.skywars.Utils.inventory.actions;

import com.dazzhub.skywars.Utils.inventory.Icon;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class OptionClickEvent {

    private Player player;
    private String target;

    private Integer position;

    private Icon icon;
    private String name;
    private String cmd;
    private String permission;

    private String interact;

    private Integer price;

    public OptionClickEvent(Player p, String target, Icon icon, Integer position, String command, String permission, String interact, Integer price) {
        this.player = p;
        this.target = target;
        this.icon = icon;
        this.position = position;
        this.cmd = command;
        this.permission = permission;
        this.interact = interact;
        this.price = price;
    }
}
