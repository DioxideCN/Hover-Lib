package cn.dioxide.util;

import org.bukkit.Location;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

/**
 * @author Dioxide.CN
 * @date 2023/6/19
 * @since 1.0
 */
public class CalcUtil {

    public static ItemDisplay.ItemDisplayTransform getTransformType(String s) {
        try {
            return ItemDisplay.ItemDisplayTransform.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 如果输入的字符串没有与任何枚举常量匹配，valueOf() 方法会抛出一个 IllegalArgumentException。
            return null;
        }
    }

    public static boolean isOutOfRange(Player p, double x, double y, double z, int between) {
        Location playerLocation = p.getLocation();
        double distance = Math.sqrt(Math.pow(x - playerLocation.getX(), 2) +
                Math.pow(y - playerLocation.getY(), 2) +
                Math.pow(z - playerLocation.getZ(), 2));

        return !(distance <= between);
    }

}
