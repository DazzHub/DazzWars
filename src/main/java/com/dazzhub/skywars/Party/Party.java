package com.dazzhub.skywars.Party;

import com.dazzhub.skywars.Arena.Arena;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.Cooldown;
import lombok.Data;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@Data
public class Party {

    private Main main;

    private GamePlayer owner;

    private List<GamePlayer> members;
    private List<GamePlayer> tempCheck;

    private boolean publicJoin;
    private Cooldown cooldown;

    public Party(GamePlayer owner) {
        this.main = Main.getPlugin();

        this.owner = owner;
        this.members = new ArrayList<>();
        this.tempCheck = new ArrayList<>();

        this.members.add(owner);

        this.publicJoin = false;
        this.cooldown = new Cooldown(30);
    }

    /* OWNER INVITE MEMBERS */
    public void invitePlayer(GamePlayer target){

        if (cooldown.checkCooldown(target)){
            owner.sendMessage(owner.getLangMessage().getString("Messages.Party.AlreadyHasParty", "Error Party.AlreadyHasParty").replace("%target%", target.getName()).replace("%owner%", owner.getName()));
            return;
        }

        cooldown.addCooldown(target);
        this.tempCheck.remove(target);
        this.tempCheck.add(target);

        target.sendMessage(target.getLangMessage().getString("Messages.Party.ReceiveInvitation", "Error Party.ReceiveInvitation").replace("%owner%", owner.getName()).replace("%target%", target.getName()));
        broadcast("Messages.Party.NotifyInvite", owner.getName(), target.getName());

        this.owner.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.SendInvitation", "Error Party.SendInvitation").replace("%target%", target.getPlayer().getName())));
    }

    /* JOIN PARTY PUBLIC */
    public void joinPartyPublic(GamePlayer target){
        if (publicJoin && !this.members.contains(target)) {
            target.setParty(this);
            target.setOwnerParty(owner);

            target.sendMessage(c(target.getLangMessage().getString("Messages.Party.JoinTarget", "Error Party.JoinTarget").replace("%target%", target.getPlayer().getName())));

            this.members.add(target);
            broadcast("Messages.Party.NotifyJoin", owner.getName(), target.getName());
        }
    }

    /* ACCEPT INVITE */
    public void acceptInvite(GamePlayer target) {
        if (!this.members.contains(target)) {

            tempCheck.remove(target);
            cooldown.getCooldowns().remove(target);

            this.members.add(target);

            target.setParty(this);
            target.setOwnerParty(owner);

            target.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.JoinTarget", "Error Party.JoinTarget").replace("%target%", target.getPlayer().getName())));
            owner.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.Join", "Error Party.Join").replace("%target%", target.getPlayer().getName())));
            broadcast("Messages.Party.NotifyJoin", owner.getName(), target.getPlayer().getName());
        }
    }

    /* KICK MEMBERS */
    public void kickMember(GamePlayer gamePlayer){
        if (this.members.contains(gamePlayer)) {
            this.members.remove(gamePlayer);

            owner.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.KickOwner", "Error Party.KickOwner"))
                    .replace("%target%", gamePlayer.getPlayer().getName())
            );

            gamePlayer.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.KickTarget", "Error Party.KickTarget"))
                    .replace("%owner%", owner.getPlayer().getName())
            );


            broadcast("Messages.Party.NotifyKick", owner.getPlayer().getName(), gamePlayer.getName());
        }
    }

    /* PLAYER LEFT PARTY */
    public void leaveParty(GamePlayer target){
        if (this.members.contains(target)) {

            members.remove(target);
            target.setOwnerParty(null);
            target.setParty(null);

            broadcast("Messages.Party.NotifyLeave",owner.getName(), target.getName());
            this.owner.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.NotifyLeave", "Party.NotifyLeave").replace("%target%", target.getPlayer().getName())));
            target.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.Leave", "Error Party.Leave").replace("%target%", target.getPlayer().getName())));
        } else {
            target.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.TargetNoMember", "Error Party.TargetNoMember").replace("%target%", target.getPlayer().getName())));
        }
    }

    /* DELETE PARTY */
    public void disaban(){
        for (GamePlayer gamePlayer : getMembers()) {
            gamePlayer.getPlayer().sendMessage(c(gamePlayer.getLangMessage().getString("Messages.Party.NotifyRemove", "Error Party.NotifyRemove").replace("%owner%", owner.getPlayer().getName())));
            gamePlayer.setOwnerParty(null);
            gamePlayer.setParty(null);
        }

        owner.setParty(null);
        owner.setOwnerParty(null);
        owner.getPlayer().sendMessage(c(owner.getLangMessage().getString("Messages.Party.Remove", "Error Party.Remove").replace("%owner%", owner.getPlayer().getName())));
    }

    public void joinArena(GamePlayer gamePlayer, Arena arena) {

        if (!this.getOwner().getPlayer().getName().equalsIgnoreCase(gamePlayer.getPlayer().getName())) {
            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.JoinArenaNoOwner", "Error Party.JoinArenaNoOwner"));
            return;
        }

        if (this.getMembers().size() > arena.getMaxPlayers()) {
            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.JoinArenaFull", "Error Party.JoinArenaFull"));
            return;
        }

        arena.joinParty(this);
    }

    //broacast
    public void broadcast(String msg, String player, String target) {
        if (!this.members.isEmpty()) {
            for (GamePlayer gamePlayer : this.members) {
                gamePlayer.sendMessage(gamePlayer.getLangMessage().getString(msg)
                        .replace("%owner%", player)
                        .replace("%target%", target)
                );
            }
        }
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }

    public void sendParty(GamePlayer from, String message){
        if (from.getPlayer().hasPermission("skywars.party.color")){
            for (GamePlayer gamePlayer : getMembers()) {
                gamePlayer.sendMessage(c(chat(from, c(gamePlayer.getLangMessage().getString("Messages.Chat.Party")), message)));
            }
        } else {
            for (GamePlayer gamePlayer : getMembers()) {
                gamePlayer.sendMessage(chat(from, c(gamePlayer.getLangMessage().getString("Messages.Chat.Party")), message));
            }
        }
    }

    private String chat(GamePlayer gamePlayer, String message, String chat) {
        if (gamePlayer == null){
            return "Error";
        }
        message = message.replace("%player%", gamePlayer.getName());
        message = message.replace("%msg%", chat);
        message = message.replace("%coins%", String.valueOf(gamePlayer.getCoins()));

        return message;
    }

    public boolean isInvite(GamePlayer target){
        return this.tempCheck.contains(target) && this.members.size() <= 4;
    }
}
