package cn.dioxide.command;

import cn.dioxide.extension.Config;
import cn.dioxide.util.CalcUtil;
import cn.dioxide.util.ColorUtil;
import cn.dioxide.util.MatrixUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dioxide.CN
 * @date 2023/6/18
 * @since 1.0
 */
public class ItemCommand {

    protected boolean placeItemDisplay(Player p, String[] args, String type, boolean canRecycle) {
        if (!p.hasPermission("hover.display.item.place")) {
            p.sendMessage(ColorUtil.formatNotice("&c你没有放置物品展示实体的权限"));
            return true;
        }
        // display item [x/help [1]] [y [2]] [z [3]] [r1 [4]] [r2 [5]] [r3 [6]] [s [7]] [t [8]] [t/f [9]]
        double x, y, z, scale; // x,y,z可以为负
        int rx, ry, rz; // 可为负数
        try {
            // 尝试将索引 1，2，3 上的字符串解析为 double 类型
            if (args[1].startsWith("~")) {
                if (args[1].length() > 1) {
                    x = p.getLocation().getX() + Double.parseDouble(args[1].substring(1));
                } else {
                    x = p.getLocation().getX();
                }
            } else {
                x = Double.parseDouble(args[1]);
            }

            if (args[2].startsWith("~")) {
                if (args[2].length() > 1) {
                    y = p.getLocation().getY() + Double.parseDouble(args[2].substring(1));
                } else {
                    y = p.getLocation().getY();
                }
            } else {
                y = Double.parseDouble(args[2]);
            }

            if (args[3].startsWith("~")) {
                if (args[3].length() > 1) {
                    z = p.getLocation().getZ() + Double.parseDouble(args[3].substring(1));
                } else {
                    z = p.getLocation().getZ();
                }
            } else {
                z = Double.parseDouble(args[3]);
            }
            scale = Double.parseDouble(args[7]);

            // 尝试将索引 4，5 上的字符串解析为 int 类型
            rx = Integer.parseInt(args[4]);
            ry = Integer.parseInt(args[5]);
            rz = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            p.sendMessage(ColorUtil.formatNotice("&c参数类型错误，请确保你的小数和整数部分是正确的"));
            return true;
        }
        // 限制范围
        if (CalcUtil.isOutOfRange(p, x, y, z, Config.get().display.item.getPlaceRadius())) {
            p.sendMessage(ColorUtil.formatNotice("&c放置的半径不能超过"+Config.get().display.item.getPlaceRadius()+"格"));
            return true;
        }
        if (scale < 0.5 || scale > 1) {
            p.sendMessage(ColorUtil.formatNotice("&c缩放的比例不能小于0.5也不能大于1"));
            return true;
        }
        ItemDisplay.ItemDisplayTransform transformType = CalcUtil.getTransformType(type);
        if (transformType == null) {
            p.sendMessage(ColorUtil.formatNotice("&c指定的物品展示实体的模式不存在"));
            return true;
        }
        ItemStack mainHandItem = p.getInventory().getItemInMainHand();
        if (mainHandItem.getType().equals(Material.AIR)) {
            p.sendMessage(ColorUtil.formatNotice("&c手持物品不能为空气"));
            return true;
        }
        // 生成仿射变换矩阵
        Matrix4f matrix = MatrixUtil.getMatrix(rx, ry, rz, scale);
        World world = p.getWorld();
        Location location = new Location(world, x, y, z);
        ItemDisplay display = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);
        display.setTransformationMatrix(matrix);
        display.setItemDisplayTransform(transformType);
        ItemStack mainHandItem_temp = mainHandItem.clone();
        mainHandItem_temp.setAmount(1);
        display.setItemStack(mainHandItem_temp);
        // owner.true.Dioxide_CN
        display.addScoreboardTag("owner." + (p.isOp() || canRecycle) + "." + p.getName());
        p.sendMessage(ColorUtil.formatNotice("&a已放置物品展示实体"));
        if (!p.isOp() || !Config.get().display.item.isConsume()) {
            // 非OP需要扣除
            mainHandItem.setAmount(mainHandItem.getAmount() - 1);
        }
        return true;
    }

    protected boolean recycleItemDisplay(Player p) {
        if (!p.hasPermission("hover.display.item.recycle")) {
            p.sendMessage(ColorUtil.formatNotice("&c你没有回收物品展示实体的权限"));
            return true;
        }
        List<Entity> nearbyEntities = p.getNearbyEntities(2, 1, 2);
        int success = 0, fail = 0;
        for (Entity entity : nearbyEntities) {
            // 只遍历ItemDisplay
            if (entity instanceof ItemDisplay display) {
                // 这里最多只会遍历一次
                for (String tag : display.getScoreboardTags()) {
                    if (tag.contains("owner")) {
                        String[] split = tag.split("\\.");
                        if (split.length != 3) {
                            fail++;
                            break;
                        }
                        boolean canRecycle = Boolean.parseBoolean(split[1]);
                        String playerName = split[2];
                        if (p.getName().equals(playerName) || canRecycle || p.isOp()) {
                            ItemStack item = display.getItemStack();
                            if (item != null) {
                                // 掉落物品
                                display.getWorld().dropItemNaturally(display.getLocation(), item);
                            }
                            display.remove();
                            success++;
                        } else {
                            fail++;
                        }
                        break;
                    }
                }
            }
        }
        p.sendMessage(ColorUtil.formatNotice("&7已回收附近的物品展示实体 &a成功 &f" + success + " 个 &c失败 &f" + fail + " 个"));
        return true;
    }

    protected boolean pluginHelper(Player p) {
        p.sendMessage(ColorUtil.formatNotice("&fItem &7创建物品展示实体指南"));
        p.sendMessage(ColorUtil.formatCommand("display item <x y z> <rx ry rz> <s> [<type>] [<bool>] &8- &7生成物品展示实体"));
        p.sendMessage(ColorUtil.formatPermission("hover.display.item.place"));
        p.sendMessage(ColorUtil.formatCommand("display item recycle &8- &7回收距离自己" +
                Config.get().display.item.getRecycleRadius() +
                "格范围内的物品展示实体"));
        p.sendMessage(ColorUtil.formatPermission("hover.display.item.recycle"));
        return true;
    }

    public static ArrayList<String> tab(@NotNull String[] args, Player player) {
        ArrayList<String> itemHelper = new ArrayList<>();
        // display item [x/help/recycle] [y] [z] [r1] [r2] [r3] [scale] [type] [bool]
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

}
