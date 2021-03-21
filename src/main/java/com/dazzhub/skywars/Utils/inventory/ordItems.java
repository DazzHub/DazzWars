package com.dazzhub.skywars.Utils.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ordItems {

    private Icon icon;
    private Integer slot;
    private String command;
    private String permission;
    private Integer price;

    public ordItems(Icon icon, Integer slot, String command, String permission, Integer price){
        this.icon = icon;
        this.slot = slot;
        this.command = command;
        this.permission = permission;
        this.price = price;
    }
}
