package com.dazzhub.skywars.Party;

import com.dazzhub.skywars.MySQL.utils.GamePlayer;

public class PartyManager {

    public void createParty(GamePlayer gamePlayer){
        if (gamePlayer.getParty() == null) {
            Party party = new Party(gamePlayer);
            gamePlayer.setParty(party);
            gamePlayer.setOwnerParty(gamePlayer);
            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.Create"));
        } else {
            gamePlayer.sendMessage(gamePlayer.getLangMessage().getString("Messages.Party.AlreadyCreate"));
        }
    }

    public void joinPartyPublic(GamePlayer owner, GamePlayer target){
        Party party = owner.getParty();

        if (party != null) {
            if (owner.getOwnerParty().equals(owner)) {
                if (owner != target) {
                    if (!party.getMembers().contains(target)) {

                        party.joinPartyPublic(target);

                    } else {
                        owner.sendMessage(owner.getLangMessage().getString("Messages.Party.TargetHasParty"));
                    }
                } else {
                    owner.sendMessage(owner.getLangMessage().getString("Messages.Party.InviteMe"));
                }
            } else {
                owner.sendMessage(owner.getLangMessage().getString("Messages.Party.IsNotOwner"));
            }
        } else {
            owner.sendMessage(owner.getLangMessage().getString("Messages.Party.NoParty"));
        }
    }

    public void invitePlayer(GamePlayer owner, GamePlayer target){
        Party party = owner.getParty();

        if (party != null) {
            if (owner.getOwnerParty().equals(owner)) {
                if (party.getTempCheck().contains(target)) {
                    owner.sendMessage(owner.getLangMessage().getString("Messages.Party.AlreadySent"));
                    return;
                }
                if (owner != target) {
                    if (!party.getMembers().contains(target)) {
                        party.invitePlayer(target);
                    } else {
                        owner.sendMessage(owner.getLangMessage().getString("Messages.Party.TargetHasParty"));
                    }
                } else {
                    owner.sendMessage(owner.getLangMessage().getString("Messages.Party.InviteMe"));
                }
            } else {
                owner.sendMessage(owner.getLangMessage().getString("Messages.Party.IsNotOwner"));
            }
        } else {
            owner.sendMessage(owner.getLangMessage().getString("Messages.Party.NoParty"));
        }
    }

    public void acceptParty(GamePlayer owner, GamePlayer target){
        if (owner != null && owner.getParty() != null) {
            if (!owner.getParty().isInvite(target)) {
                target.sendMessage(target.getLangMessage().getString("Messages.Party.NoHaveInvitation"));
            } else {
                if (target.getParty() == null) {
                    owner.getParty().acceptInvite(target);
                } else {
                    target.sendMessage(target.getLangMessage().getString("Messages.Party.TargetHasParty"));
                }
            }
        } else {
            target.sendMessage(target.getLangMessage().getString("Messages.Party.NoParty"));
        }
    }

    public void leaveParty(GamePlayer target){
        Party party = target.getParty();
        if (party != null) {
            if (target.getOwnerParty() != target) {
                party.leaveParty(target);
            } else {
                target.sendMessage(target.getLangMessage().getString("Messages.Party.NoOwnerLeave").replace("%target%", target.getName()));
            }
        } else {
            target.sendMessage(target.getLangMessage().getString("Messages.Party.NoParty").replace("%target%", target.getName()));
        }
    }

    public void kickParty(GamePlayer owner, GamePlayer target){
        Party party = owner.getParty();
        if (party != null) {
            if (owner.getOwnerParty() == owner) {
                if (owner != target) {
                    party.kickMember(target);
                } else {
                    owner.sendMessage(owner.getLangMessage().getString("Messages.Party.NoOwnerLeave"));
                }
            } else {
                owner.sendMessage(owner.getLangMessage().getString("Messages.Party.IsNotOwner"));
            }
        } else {
            owner.sendMessage(owner.getLangMessage().getString("Messages.Party.NoParty"));
        }
    }

    public void removeParty(GamePlayer owner){
        if (owner.getParty() != null) {
            if (owner.getOwnerParty() == owner){
                owner.getParty().disaban();
            } else {
                owner.sendMessage(owner.getLangMessage().getString("Messages.Party.IsNotOwner"));
            }
        } else {
            owner.sendMessage(owner.getLangMessage().getString("Messages.Party.NoParty"));
        }
    }

}
