package cn.dioxide.service;

import cn.dioxide.util.ColorUtil;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Objects;

public class ClickBlockEvent implements Listener {

    @EventHandler
    public synchronized void playerPutBook2Wall(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        ItemStack awaitBook = e.getPlayer().getInventory().getItemInMainHand().clone(); // 成书
        if (
                !awaitBook.getType().equals(Material.WRITTEN_BOOK) ||
                awaitBook.getItemMeta() == null ||
                awaitBook.getItemMeta().getLore() == null ||
                awaitBook.getItemMeta().getLore().size() < 1
        )
            return;

        if (!awaitBook.getItemMeta().getLore().get(awaitBook.getItemMeta().getLore().size() - 1).contains("右键方块可以在指定位置可以放置该书本的展示框"))
            return;

        if (e.getClickedBlock() == null)
            return;
        if (Objects.equals(e.getClickedBlock().getType(), Material.AIR))
            return;

        if (e.getPlayer().hasPermission("hoverlib.place")) {
            e.getPlayer().sendMessage(ColorUtil.format("&c[HoverLib] 你没有放置该书本的权限"));
            return;
        }

        Block clickedBlock = e.getClickedBlock();      // 方块类型
        BlockFace clickedBlockFace = e.getBlockFace(); // 方块朝向

        // 初始化位置
        int itemFramePositionX = clickedBlock.getX();
        int itemFramePositionY = clickedBlock.getY();
        int itemFramePositionZ = clickedBlock.getZ();
        BlockFace itemFrameFacing = BlockFace.UP;
        switch (clickedBlockFace) {
            case UP -> itemFramePositionY++;
            case DOWN -> {
                itemFramePositionY--;
                itemFrameFacing = BlockFace.DOWN;
            }
            case WEST -> {
                itemFramePositionX--;
                itemFrameFacing = BlockFace.WEST;
            }
            case EAST -> {
                itemFramePositionX++;
                itemFrameFacing = BlockFace.EAST;
            }
            case NORTH -> {
                itemFramePositionZ--;
                itemFrameFacing = BlockFace.NORTH;
            }
            case SOUTH -> {
                itemFramePositionZ++;
                itemFrameFacing = BlockFace.SOUTH;
            }
        }

        BookMeta awaitBookMeta = (BookMeta) awaitBook.getItemMeta();
        if (awaitBookMeta == null)
            return;
        awaitBookMeta.setDisplayName(ColorUtil.format("&b" + awaitBookMeta.getTitle()));
        awaitBookMeta.getLore().remove(awaitBookMeta.getLore().size() - 1);
        awaitBook.setItemMeta(awaitBookMeta);

        e.getPlayer().sendMessage(clickedBlockFace.toString());
        Location location = new Location(clickedBlock.getWorld(), itemFramePositionX, itemFramePositionY, itemFramePositionZ);
        ItemFrame itemFrame = (ItemFrame) clickedBlock.getWorld().spawnEntity(location, EntityType.ITEM_FRAME);
        itemFrame.setFixed(true);
        itemFrame.setItem(awaitBook);
        itemFrame.setFacingDirection(itemFrameFacing);
    }

    @EventHandler
    public void onPlayerInteract(SignChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        // 检查玩家手持的是否是纸
        if(event.getBlock().getState() instanceof Sign sign) {
            if (itemInHand.getType() == Material.PAPER) {
                // 取消编辑事件
                event.setCancelled(true);
                SignSide side = (sign).getSide(event.getSide());
                // 移除告示牌的标签
                side.setGlowingText(false);
                side.setColor(DyeColor.BLACK);
                // 从玩家的物品栏中移除一张纸
                itemInHand.setAmount(itemInHand.getAmount() - 1);
                sign.update();
                // 播放声音
                player.playSound(player, Sound.BLOCK_CALCITE_HIT, 1.0F, 1.0F);
            }
        }
    }

}
