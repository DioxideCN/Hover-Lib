package cn.dioxide.service;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Dioxide.CN
 * @date 2023/6/8
 * @since 1.0
 */
public class SmeltingEvent implements Listener {

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Material sourceType = event.getSource().getType();

        // 如果熔炼的原材料是铁矿石或深层铁矿石
        if (sourceType == Material.IRON_ORE || sourceType == Material.DEEPSLATE_IRON_ORE || sourceType == Material.RAW_IRON) {
            // 改变熔炉的冶炼结果为铁粒
            event.setResult(new ItemStack(Material.IRON_NUGGET));
        }

        // 如果熔炼的原材料是金矿石或深层金矿石
        if (sourceType == Material.GOLD_ORE || sourceType == Material.DEEPSLATE_GOLD_ORE || sourceType == Material.RAW_GOLD) {
            // 改变熔炉的冶炼结果为金粒
            event.setResult(new ItemStack(Material.GOLD_NUGGET));
        }
    }

}
