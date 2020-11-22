package com.dazzhub.skywars.Utils.soulWell;

import com.cryptomorin.xseries.XMaterial;
import com.dazzhub.skywars.Listeners.Lobby.onSoulWell;
import com.dazzhub.skywars.Main;
import com.dazzhub.skywars.MySQL.utils.GamePlayer;
import com.dazzhub.skywars.Utils.NoteBlockAPI.NBSDecoder;
import com.dazzhub.skywars.Utils.NoteBlockAPI.lSong;
import com.dazzhub.skywars.Utils.inventory.Icon;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class SoulWell {

    private Main main;

    /* SOUL WELL */
    private int row;
    private String type;

    private String name;
    private String rarity;
    private int chance;

    private Icon item;

    private List<String> commands;

    /* MENU */
    private Random r;

    private List<Integer> slots;
    private List<Integer> backgroundSlots;
    private Icon pane_itemstack;

    private int duration;

    private Configuration config;

    public SoulWell(int row, String type) {
        /* SOUL WELL */
        this.main = Main.getPlugin();
        this.config = main.getConfigUtils().getConfig(Main.getPlugin(), "SoulWell");

        this.row = row;
        this.type = type;

        String path = "Soul." + type + "." + row;
        this.name = config.getString(path + ".NAME");
        this.rarity = config.getString(path + ".RARITY");
        this.chance = config.getInt(path + ".CHANCE");

        Material material;

        if (main.checkVersion()) {
            material = config.isInt(path + ".ICON.ICON-ITEM") ? Material.getMaterial(config.getInt(path + ".ICON.ICON-ITEM")) : Material.getMaterial(path + ".ICON.ICON-ITEM");
        } else {
            material = Material.getMaterial(path + ".ICON.ICON-ITEM");
        }

        if (material == null) {
            material = Material.BEDROCK;
        }

        this.item = new Icon(XMaterial.matchXMaterial(material), 1, (short) config.getInt(path + ".ICON.DATA-VALUE")).setName(name).setLore(config.getStringList(path + ".ICON.DESCRIPTION"));

        this.commands = config.getStringList(path + ".ACTION");

        /* MENU */
        this.r = new Random();
        this.slots = Arrays.asList(4, 13, 22, 31, 40);
        this.backgroundSlots = new ArrayList<>();
        IntStream.range(0, 45).filter(i -> !this.slots.contains(i) && i != 21 && i != 23).forEach(i -> this.backgroundSlots.add(i));

        if (main.checkVersion()){
            this.pane_itemstack = new Icon(XMaterial.matchXMaterial(Material.STAINED_GLASS_PANE), 1, (short) 7).setName("&r");
        }else {
            this.pane_itemstack = new Icon(XMaterial.matchXMaterial(XMaterial.BLACK_STAINED_GLASS_PANE.parseMaterial()), 1, (short) 0).setName("&r");
        }
        this.duration = 30;
    }

    public void openSoulWell(GamePlayer gamePlayer) {
        Player p = gamePlayer.getPlayer();

        Inventory inv = Bukkit.createInventory(p, 45, c(gamePlayer.getLangMessage().getString("Messages.SoulWell.NameMenu")));

        ItemStack build = new Icon(XMaterial.matchXMaterial(Material.STAINED_GLASS), 1, (short) 15).setName("&r").build();

        ItemStack clone = pane_itemstack.build(p).clone();
        IntStream.range(0, 45).forEach(j -> inv.setItem(j, clone));

        inv.setItem(21, build);
        inv.setItem(23, build);
        ItemStack itemStack = item.build(p);

        ItemStack icon = new ItemStack(XMaterial.matchXMaterial(Material.AIR).parseItem());
        slots.forEach(slot -> inv.setItem(slot, icon));

        p.openInventory(inv);
        onSoulWell.using = true;

        if (gamePlayer.getLangMessage().getBoolean("Messages.SoulWell.StartScroll.Music.enable")) {
            File f = new File(Main.getPlugin().getDataFolder(), gamePlayer.getLangMessage().getString("Messages.SoulWell.StartScroll.Music.namefile"));
            main.play(p, new lSong(NBSDecoder.parse(f)));
        }

        new BukkitRunnable() {
            public void run() {
                backgroundSlots.forEach(backgroundSlot -> inv.getItem(backgroundSlot).setDurability((short) (r.nextInt(6) + 1)));

                if (duration <= 1) {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(main, 0, 1);

        new BukkitRunnable() {
            public void run() {

                if (gamePlayer.getLangMessage().getBoolean("Messages.SoulWell.StartScroll.Sound.enable")) {
                    gamePlayer.playSound(gamePlayer.getLangMessage().getString("Messages.SoulWell.StartScroll.Sound.type"));
                }

                for (int i = slots.size() - 1; i > 0; --i) {
                    inv.setItem(slots.get(i), inv.getItem(slots.get(i - 1)));
                }

                inv.setItem(slots.get(0), main.getSoulManager().getItemStacks().get(r.nextInt(Main.getPlugin().getSoulManager().getItemStacks().size())));

                if (duration <= 1) {
                    this.cancel();
                    Main.getPlugin().stopPlaying(p);

                    IntStream.range(0, 45).filter(i1 -> !slots.contains(i1) && i1 != 21 && i1 != 23).forEach(i1 -> inv.setItem(i1, icon));

                    slots.forEach(i -> {
                        if (i == 22) {
                            inv.setItem(22, itemStack);
                            return;
                        }

                        inv.setItem(i, icon);
                    });

                    message(gamePlayer);
                    reward(gamePlayer);

                    onSoulWell.using = false;
                    duration = 30;
                }

                duration--;
            }
        }.runTaskTimerAsynchronously(main, 0, 3);
    }

    private void reward(GamePlayer gamePlayer) {
        List<String> message = getCommands().stream().map(cmd -> cmd.replace("%player%", gamePlayer.getName())).collect(Collectors.toList());

        for (String cmd : message) {
            if (cmd.startsWith("console:")) {
                String consoleCommand = cmd.substring(8);
                if (consoleCommand.startsWith(" ")) {
                    consoleCommand = consoleCommand.substring(1);
                }

                String finalConsoleCommand = consoleCommand;
                Bukkit.getScheduler().runTask(main, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalConsoleCommand));

            } else if (cmd.startsWith("coin:")) {
                String coins = cmd.substring(5);
                if (coins.startsWith(" ")) {
                    coins = coins.substring(1);
                }

                gamePlayer.addCoins(Integer.parseInt(coins));
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

    private void message(GamePlayer gamePlayer){
        if (gamePlayer.getLangMessage().getBoolean("Messages.SoulWell.EndScroll.Sound.enable")) {
            gamePlayer.playSound(gamePlayer.getLangMessage().getString("Messages.SoulWell.EndScroll.Sound.type"));
        }

        if (gamePlayer.getLangMessage().getBoolean("Messages.SoulWell.EndScroll.FireWorks.enable")) {
            Bukkit.getScheduler().runTask(main, () -> gamePlayer.getTypeWin("fireworks"));
        }

        if (gamePlayer.getLangMessage().getBoolean("Messages.SoulWell.EndScroll.Effects.enable")) {
            ParticleEffect.valueOf(gamePlayer.getLangMessage().getString("Messages.SoulWell.EndScroll.Effects.type")).display(gamePlayer.getPlayer().getLocation(),0f, 0f, 0f, 0f, 10, null);
        }

        if (gamePlayer.getLangMessage().getBoolean("Messages.SoulWell.EndScroll.Announcement.enable")) {

            List<String> message = gamePlayer.getLangMessage().getStringList("Messages.SoulWell.EndScroll.Announcement.lines").stream().map(s -> s
                    .replace("%name%", c(name))
                    .replace("%rarity%", c(rarity))
                    .replace("%chance%", c(String.valueOf(chance)))
                    .replace("%commands%", commands.toString().replace("[", "")).replace("]", "")
                    .replace("%type%", c(type))).collect(Collectors.toList());


            gamePlayer.sendMessage(message);
        }
    }

    public String c(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}

