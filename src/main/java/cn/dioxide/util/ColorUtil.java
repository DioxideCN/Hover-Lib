package cn.dioxide.util;

public class ColorUtil {
    public static String format(String msg) {
        return msg.replaceAll("&", "§");
    }

    public static String formatNotice(String msg) {
        return format("&b[&aHover&eLib&b] " + msg);
    }

    public static String formatCommand(String msg) {
        return format("&3> &a" + msg);
    }

    public static String formatPermission(String msg) {
        return format("&e  - &7" + msg);
    }
}
