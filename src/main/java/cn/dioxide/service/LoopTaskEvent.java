package cn.dioxide.service;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Dioxide.CN
 * @date 2023/6/8
 * @since 1.0
 */
public class LoopTaskEvent {

    // 穿戴镶有潮汐盔甲纹饰的全套装备可以持续提供水下呼吸
    public static void spreadWaterBreath() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isWearingFullNetherite(player)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 20*5, 0, true, false, false));
            }
        }
    }

    private static boolean isWearingFullNetherite(Player player) {
        PlayerInventory inv = player.getInventory();
        return isTideArmorTrim(inv.getHelmet())
                && isTideArmorTrim(inv.getChestplate())
                && isTideArmorTrim(inv.getLeggings())
                && isTideArmorTrim(inv.getBoots());
    }

    private static boolean isTideArmorTrim(ItemStack item) {
        if (item == null || item.getItemMeta() == null) {
            return false;
        }
        if (item.getItemMeta() instanceof ArmorMeta itemMeta) {
            return itemMeta.getTrim() != null &&
                   itemMeta.getTrim().getPattern() == TrimPattern.TIDE;
        }
        return false;
    }

}
