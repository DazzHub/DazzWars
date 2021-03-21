package com.dazzhub.skywars.Utils.achievements;

import com.cryptomorin.xseries.messages.Titles;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class IAchievement {

    private final int lvl;
    private final String description;
    private final String typereward;
    private final List<String> executedCommand;

    public IAchievement(int lvl, String description, String typereward, List<String> executedCommand) {
        this.lvl = lvl;
        this.description = description;
        this.typereward = typereward;
        this.executedCommand = new ArrayList<>();
        this.executedCommand.addAll(executedCommand);
    }

    public void sendReward(Player p){
        GamePlayer gamePlayer = Main.getPlugin().getPlayerManager().getPlayer(p.getUniqueId());

        Configuration config = gamePlayer.getLangMessage();

        if (!config.getBoolean("Messages.Achievements.UseAdvertising")){
            return;
        }

        if (config.getBoolean("Messages.Achievements.FireWorks")){
            spawnFireworks(p.getLocation());
        }

        if (config.getBoolean("Messages.Achievements.Sound.Use")){
            String sound = config.getString("Messages.Achievements.Sound.Type");
            gamePlayer.playSound(sound);
        }

        if (config.getBoolean("Messages.Achievements.Title.Use")){
            Titles.sendTitle(p,
                    config.getInt("Messages.Achievements.Title.Fade"),
                    config.getInt("Messages.Achievements.Title.Stay"),
                    config.getInt("Messages.Achievements.Title.Out"),
                    c(config.getString("Messages.Achievements.Title.Info").split(";")[0]).replace("%player%", p.getName()).replace("%description%", description).replace("%typereward%", typereward).replace("%lvl%", String.valueOf(lvl)),
                    c(config.getString("Messages.Achievements.Title.Info").split(";")[1]).replace("%player%", p.getName()).replace("%description%", description).replace("%typereward%", typereward).replace("%lvl%", String.valueOf(lvl))
            );
        }

        if (config.getBoolean("Messages.Achievements.Message.Use")) {
            List<String> message1 = config.getStringList("Messages.Achievements.Message.Announcement");
            List<String> message = new ArrayList<>();

            for (String s : message1) {
                message.add(s
                        .replace("%description%", description)
                        .replace("%typereward%", typereward)
                        .replace("%lvl%", String.valueOf(lvl))
                );
            }

            for (String str : message) {
                gamePlayer.sendMessage(str);
            }
        }

        if (config.getBoolean("Messages.Achievements.RewardsUse")) {
            List<String> message = new ArrayList<>();

            for (String cmd : getExecutedCommand()) {
                message.add(cmd.replace("%player%", p.getName()));
            }

            for (String cmd : message) {
                if (cmd.startsWith("console:")) {
                    String consoleCommand = cmd.substring(8);
                    if (consoleCommand.startsWith(" ")) {
                        consoleCommand = consoleCommand.substring(1);
                    }

                    String finalConsoleCommand = consoleCommand;
                    Bukkit.getScheduler().runTask(Main.getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalConsoleCommand));

                } else if (cmd.startsWith("coin:")) {
                    String coins = cmd.substring(5);
                    if (coins.startsWith(" ")) {
                        coins = coins.substring(1);
                    }

                    gamePlayer.addCoins(Double.parseDouble(coins));
                } else if (cmd.startsWith("cage:")) {
                    String cage = cmd.substring(5);
                    if (cage.startsWith(" ")) {
                        cage = cage.substring(1);
                    }

                    String[] action = cage.split("/");

                    if (action[1].equalsIgnoreCase("SOLO")) {
                        if (!gamePlayer.getCagesSoloList().contains(action[0])) {
                            gamePlayer.getCagesSoloList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("TEAM")) {
                        if (!gamePlayer.getCagesTeamList().contains(action[0])) {
                            gamePlayer.getCagesTeamList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("RANKED")) {
                        if (!gamePlayer.getCagesRankedList().contains(action[0])) {
                            gamePlayer.getCagesRankedList().add(action[0]);
                        }
                    }

                } else if (cmd.startsWith("trail:")) {
                    String trail = cmd.substring(6);
                    if (trail.startsWith(" ")) {
                        trail = trail.substring(1);
                    }

                    String[] action = trail.split("/");

                    if (action[1].equalsIgnoreCase("SOLO")) {
                        if (!gamePlayer.getTrailsSoloList().contains(action[0])) {
                            gamePlayer.getTrailsSoloList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("TEAM")) {
                        if (!gamePlayer.getTrailsTeamList().contains(action[0])) {
                            gamePlayer.getTrailsTeamList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("RANKED")) {
                        if (!gamePlayer.getTrailsRankedList().contains(action[0])) {
                            gamePlayer.getTrailsRankedList().add(action[0]);
                        }
                    }
                } else if (cmd.startsWith("kit:")) {
                    String kit = cmd.substring(4);
                    if (kit.startsWith(" ")) {
                        kit = kit.substring(1);
                    }

                    String[] action = kit.split("/");

                    if (action[1].equalsIgnoreCase("SOLO")) {
                        if (!gamePlayer.getKitSoloList().contains(action[0])) {
                            gamePlayer.getKitSoloList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("TEAM")) {
                        if (!gamePlayer.getKitTeamList().contains(action[0])) {
                            gamePlayer.getKitTeamList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("RANKED")) {
                        if (!gamePlayer.getKitRankedList().contains(action[0])) {
                            gamePlayer.getKitRankedList().add(action[0]);
                        }
                    }
                } else if (cmd.startsWith("wineffect:")) {
                    String win = cmd.substring(10);
                    if (win.startsWith(" ")) {
                        win = win.substring(1);
                    }

                    String[] action = win.split("/");

                    if (action[1].equalsIgnoreCase("SOLO")) {
                        if (!gamePlayer.getWinEffectsSoloList().contains(action[0])) {
                            gamePlayer.getWinEffectsSoloList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("TEAM")) {
                        if (!gamePlayer.getWinEffectsTeamList().contains(action[0])) {
                            gamePlayer.getWinEffectsTeamList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("RANKED")) {
                        if (!gamePlayer.getWinEffectsRankedList().contains(action[0])) {
                            gamePlayer.getWinEffectsRankedList().add(action[0]);
                        }
                    }
                } else if (cmd.startsWith("killeffect:")) {
                    String killeffects = cmd.substring(11);
                    if (killeffects.startsWith(" ")) {
                        killeffects = killeffects.substring(1);
                    }

                    String[] action = killeffects.split("/");

                    if (action[1].equalsIgnoreCase("SOLO")) {
                        if (!gamePlayer.getKillEffectsSoloList().contains(action[0])) {
                            gamePlayer.getKillEffectsSoloList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("TEAM")) {
                        if (!gamePlayer.getKillEffectsTeamList().contains(action[0])) {
                            gamePlayer.getKillEffectsTeamList().add(action[0]);
                        }
                    } else if (action[1].equalsIgnoreCase("RANKED")) {
                        if (!gamePlayer.getKillEffectsRankedList().contains(action[0])) {
                            gamePlayer.getKillEffectsRankedList().add(action[0]);
                        }
                    }
                }
            }
        }
    }

    public int getLvl() {
        return lvl;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getExecutedCommand() {
        return executedCommand;
    }

    private void spawnFireworks(Location location){
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder()
                .withColor(Color.LIME)
                .withColor(Color.FUCHSIA)
                .withColor(Color.RED)
                .flicker(true)
                .trail(true)
                .withFade(Color.GREEN)
                .build()
        );
        fw.setFireworkMeta(fwm);

        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), fw::detonate, 5);
    }

    private String c(String c) {
        return ChatColor.translateAlternateColorCodes('&', c);
    }
}
