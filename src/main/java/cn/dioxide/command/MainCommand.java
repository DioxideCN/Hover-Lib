package cn.dioxide.command;

import cn.dioxide.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand implements TabExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        if (!label.equals("hlib"))
            return false;

        if (args.length == 1) {
            switch (args[0]) {
                case "enable" -> {
                    return this.enableHoverBook((Player) sender);
                }
                case "disable" -> {
                    return this.disableHoverBook((Player) sender);
                }
                case "help" -> {
                    return this.pluginHelper((Player) sender);
                }
            }
        }
        return false;
    }

    private boolean enableHoverBook(Player p) {
        if (!p.hasPermission("hoverlib.enable")) {
            p.sendMessage(ColorUtil.format("&c[HoverLib] 创建失败，你不是管理员或你没有创建图书的权限！"));
            return true;
        }

        ItemStack mainHandItem = p.getInventory().getItemInMainHand();
        if (!mainHandItem.getType().equals(Material.WRITTEN_BOOK)) {
            p.sendMessage(ColorUtil.format("&c[HoverLib] 创建失败，在创建图书展示框时必须在主手手持一本成书！"));
            return true;
        }
        if (mainHandItem.getItemMeta() == null) {
            p.sendMessage(ColorUtil.format("&c[HoverLib] 创建失败，成书需要内容！"));
            return true;
        }
        BookMeta bookMeta = (BookMeta) mainHandItem.getItemMeta();

        if (!bookMeta.hasTitle()) {
            p.sendMessage(ColorUtil.format("&c[HoverLib] 创建失败，成书需要一个标题！"));
        }

        List<String> bookLoreList = bookMeta.getLore() == null ? new ArrayList<>() : bookMeta.getLore(); // 书籍注释
        bookLoreList.add(ColorUtil.format("&a右键方块可以在指定位置可以放置该书本的展示框"));
        bookMeta.setLore(bookLoreList);
        mainHandItem.setItemMeta(bookMeta);
        return true;
    }

    private boolean disableHoverBook(Player p) {
        if (!p.hasPermission("hoverlib.disable")) {
            p.sendMessage(ColorUtil.format("&c[HoverLib] 销毁失败，你不是管理员或你没有销毁图书的权限！"));
            return true;
        }
        ItemStack mainHandItem = p.getInventory().getItemInMainHand();
        if (!mainHandItem.getType().equals(Material.WRITTEN_BOOK)) {
            p.sendMessage(ColorUtil.format("&c[HoverLib] 销毁失败，在销毁图书时必须在主手手持一本成书！"));
            return true;
        }
        if (mainHandItem.getItemMeta() == null) {
            p.sendMessage(ColorUtil.format("&c[HoverLib] 销毁失败，该成书无法被销毁！"));
            return true;
        }
        BookMeta bookMeta = (BookMeta) mainHandItem.getItemMeta();
        List<String> bookLore = bookMeta.getLore();

        if (bookLore == null || bookLore.size() < 1 || !bookLore.get(bookLore.size() - 1).contains("右键方块可以在指定位置可以放置该书本的展示框")) {
            p.sendMessage(ColorUtil.format("&c[HoverLib] 销毁失败，该成书无法被销毁！"));
            return true;
        }
        bookLore.remove(bookLore.size() - 1);
        bookMeta.setLore(bookLore);
        mainHandItem.setItemMeta(bookMeta);
        return true;
    }

    private boolean pluginHelper(Player p) {
        p.sendMessage(ColorUtil.format("&a[Hover Lib] 创建图书馆 Help"));
        p.sendMessage(ColorUtil.format("&b/hlib help &8- &7插件使用帮助"));
        p.sendMessage(ColorUtil.format("&b/hlib enable &8- &7将手中的成书激活为图书创建者"));
        p.sendMessage(ColorUtil.format("&e - &7hoverlib.enable"));
        p.sendMessage(ColorUtil.format("&b/hlib disable &8- &7将手中的图书创建者禁用"));
        p.sendMessage(ColorUtil.format("&e - &7hoverlib.disable"));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> tabHelper = new ArrayList();
        tabHelper.add("disable");
        tabHelper.add("enable");
        tabHelper.add("help");
        return args.length == 1 ? tabHelper : null;
    }
}
