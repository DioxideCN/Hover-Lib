package cn.dioxide.command;

import cn.dioxide.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (args.length == 0) {
            return this.pluginHelper(player);
        }

        BookCommand bookCommand = new BookCommand();
        ItemCommand itemCommand = new ItemCommand();
        BlockCommand blockCommand = new BlockCommand();
        if (args.length == 1) {
            if ("help".equals(args[0])) {
                return this.pluginHelper(player);
            }
            if ("block".equals(args[0])) {
                return blockCommand.pluginHelper(player);
            }
            if ("item".equals(args[0])) {
                itemCommand.pluginHelper(player);
            }
            if ("book".equals(args[0])) {
                return bookCommand.pluginHelper(player);
            }
        }
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
            if ("help".equals(args[1])) {
                if ("item".equals(args[0])) {
                    return itemCommand.pluginHelper(player);
                }
                if ("block".equals(args[0])) {
                    return blockCommand.pluginHelper(player);
                }
            }
            if ("recycle".equals(args[1])) {
                if ("item".equals(args[0])) {
                    return itemCommand.recycleItemDisplay(player);
                }
                if ("block".equals(args[0])) {
                    return blockCommand.recycleItemDisplay(player);
                }
            }
        }
        if (args.length >= 8 && args.length <= 10) {
            // display item <x y z> <rx ry rz> <s> <type> <true/false>
            if ("item".equals(args[0])) {
                return itemCommand.placeItemDisplay(
                        player,
                        args,
                        args.length == 9 ? args[8] : "fixed",
                        args.length == 10 && Boolean.parseBoolean(args[9]));
            }
            // display block <x y z> <rx ry rz> <s> <type> <true/false>
            if ("block".equals(args[0]) && args.length <= 9) {
                return blockCommand.placeItemDisplay(
                        player,
                        args,
                        args.length != 9 && Boolean.parseBoolean(args[8]));
            }
        }
        return false;
    }

    private boolean pluginHelper(Player p) {
        p.sendMessage(ColorUtil.formatNotice("&7插件指南"));
        p.sendMessage(ColorUtil.formatCommand("display book help &8- &7图书创建指南"));
        p.sendMessage(ColorUtil.formatCommand("display item help &8- &7物品展示指南"));
        p.sendMessage(ColorUtil.formatCommand("display block help &8- &7方块展示指南"));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabHelper = Arrays.asList("help", "book", "item", "block");
        List<String> bookHelper = Arrays.asList("disable", "enable", "help");
        // display <?>
        if (args.length == 1) {
            return tabHelper;
        }
        if (args.length == 2) {
            // display book <?>
            if ("book".equals(args[0])) {
                return bookHelper;
            }
        }
        if (args.length >= 2) {
            if ("item".equals(args[0])) {
                return ItemCommand.tab(args, (Player) sender);
            }
            if ("block".equals(args[0])) {
                return BlockCommand.tab(args, (Player) sender);
            }
        }
        return null;
    }

}
