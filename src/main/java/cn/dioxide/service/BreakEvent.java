package cn.dioxide.service;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * @author Dioxide.CN
 * @date 2023/6/8
 * @since 1.0
 */
public class BreakEvent implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // 检查是否是工作台
        if (event.getBlock().getType() == Material.CRAFTING_TABLE) {
            // 阻止默认的方块掉落
            event.setDropItems(false);
            // 掉落1-3个橡木木板
            int amount = random.nextInt(3) + 1; // random.nextInt(3) 会返回0，1，2，所以我们需要加1
            ItemStack planks = new ItemStack(Material.OAK_PLANKS, amount);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), planks);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        switch (event.getEntityType()) {
            // 取消爆炸破坏地形，但保留爆炸造成的伤害
            case CREEPER, GHAST -> event.blockList().clear();
            default -> {}
        }
    }

}
