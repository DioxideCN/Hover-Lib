package cn.dioxide.service;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;

/**
 * @author Dioxide.CN
 * @date 2023/6/8
 * @since 1.0
 */
public class VillagerEvent implements Listener {

    @EventHandler
    public void onVillagerReplenishTrade(VillagerReplenishTradeEvent event) {
        // 取消村民的交易刷新事件
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        if (event.getEntity().getType() == EntityType.VILLAGER) {
            // 如果繁殖的实体是村民，则取消繁殖事件
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (event.getEntity().getType() == EntityType.VILLAGER) {
            // 如果进入传送门的实体是村民，则取消事件
            event.setCancelled(true);
        }
//        if (event.getEntity().getType() == EntityType.PLAYER) {
//            event.getEntity().sendMessage(ColorUtil.format("&c[&e重要提示&c] &7下届仍在更新，现在无法进入，请各位玩家谅解。"));
//        }
//        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntityType() == EntityType.IRON_GOLEM) {
            // 如果死亡的实体是铁傀儡，则清除掉落的铁锭
            event.getDrops().removeIf(item -> item.getType() == Material.IRON_INGOT);
        }
    }

}
