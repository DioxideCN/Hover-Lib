package cn.dioxide.command;

import cn.dioxide.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements TabExecutor {

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return false;
        }
        if (!label.equals("display")) {
            return false;
        }
        // display help
        if (args.length == 1 && "help".equals(args[0])) {
            return this.pluginHelper((Player) sender);
        }

        BookCommand bookCommand = new BookCommand();
        ItemCommand itemCommand = new ItemCommand();
        // display <module> <action>
        if (args.length == 2) {
            if ("book".equals(args[0])) {
                switch (args[1]) {
                    case "enable" -> { // display book enable
                        return bookCommand.enableHoverBook(player);
                    }
                    case "disable" -> { // display book disable
                        return bookCommand.disableHoverBook(player);
                    }
                    case "help" -> { // display book help
                        return bookCommand.pluginHelper(player);
                    }
                    default -> { // display book ?
                        return false;
                    }
                }
            }
            if ("item".equals(args[0])) { // display item help
                if ("help".equals(args[1])) {
                    return itemCommand.pluginHelper(player);
                }
                if ("recycle".equals(args[1])) {
                    return itemCommand.recycleItemDisplay(player);
                }
            }
        }
        // display item <x y z> <rx ry rz> <s> <type> <true/false>
        if (args.length >= 9 && args.length <= 10 && "item".equals(args[0])) {
            return itemCommand.placeItemDisplay(player, args, args.length != 10 || Boolean.parseBoolean(args[9]));
        }
        return false;
    }

    private boolean pluginHelper(Player p) {
        p.sendMessage(ColorUtil.formatNotice("&7插件指南"));
        p.sendMessage(ColorUtil.format("&b/display book help &8- &7图书创建指南"));
        p.sendMessage(ColorUtil.format("&b/display item help &8- &7物品展示指南"));
        p.sendMessage(ColorUtil.format("&b/display block help &8- &7方块展示指南"));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> tabHelper = new ArrayList<>();
        ArrayList<String> bookHelper = new ArrayList<>();
        ArrayList<String> itemHelper = new ArrayList<>();

        tabHelper.add("help");
        tabHelper.add("book");
        tabHelper.add("item");
        tabHelper.add("block");
        // display <?>
        if (args.length == 1) return tabHelper;

        // display book <?>
        bookHelper.add("disable");
        bookHelper.add("enable");
        bookHelper.add("help");

        if (args.length == 2) {
            // display book <?>
            if ("book".equals(args[0])) {
                return bookHelper;
            }
        }
        // display item [x/help] [y] [z] [r1] [r2] [s] [t]
        if (args.length >= 2 && "item".equals(args[0])) {
            Player player = (Player) sender;
            String x = String.format("%.2f", player.getLocation().getX());
            String y = String.format("%.2f", player.getLocation().getY());
            String z = String.format("%.2f", player.getLocation().getZ());

            // [x/help]
            if (args.length == 2) {
                itemHelper.add("help");
                itemHelper.add("recycle");
                itemHelper.add("~");
                itemHelper.add("~ ~");
                itemHelper.add("~ ~ ~");
                itemHelper.add(x);
                itemHelper.add(x + " " + y);
                itemHelper.add(x + " " + y + " " + z);
            }
            if (!"help".equals(args[1])) {
                switch (args.length) {
                    case 3 -> { // [y]
                        itemHelper.add("~");
                        itemHelper.add("~ ~");
                        itemHelper.add(y);
                        itemHelper.add(y + " " + z);
                    }
                    case 4 -> { // [z]
                        itemHelper.add("~");
                        itemHelper.add(z);
                    }
                    case 9 -> itemHelper.addAll( // [t]
                            Arrays.asList(
                                    "none",
                                    "thirdperson_lefthand",
                                    "thirdperson_righthand",
                                    "firstperson_lefthand",
                                    "firstperson_righthand",
                                    "head",
                                    "gui",
                                    "ground",
                                    "fixed"));
                    case 10 -> {
                        itemHelper.add("true");
                        itemHelper.add("false");
                    }
                    default -> {}
                }
            }
            return itemHelper;
        }
        return null;
    }

}
