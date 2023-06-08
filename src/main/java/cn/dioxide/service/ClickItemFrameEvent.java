package cn.dioxide.service;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ClickItemFrameEvent implements Listener {
    @EventHandler
    public void playerClickItemFrame2ReadBook(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() != EntityType.ITEM_FRAME)
            return;

        ItemFrame itemFrame = (ItemFrame) e.getRightClicked();
        if (itemFrame.getItem().getType() != Material.WRITTEN_BOOK) {
            return;
        }

        e.getPlayer().openBook(itemFrame.getItem());
    }
}
