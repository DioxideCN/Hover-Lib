package cn.dioxide.service;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;

/**
 * @author Dioxide.CN
 * @date 2023/6/8
 * @since 1.0
 */
public class StupidVillagerEvent implements Listener {

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
    }

}
