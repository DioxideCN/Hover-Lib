package cn.dioxide.service;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.util.Vector;

/**
 * @author Dioxide.CN
 * @date 2023/6/8
 * @since 1.0
 */
public class EquipEvent implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (!hasSpireTrim(player)) {
            return;
        }
        // Check if the damage is caused by an entity
        if (event instanceof EntityDamageByEntityEvent damageByEntityEvent) {
            Entity damager = damageByEntityEvent.getDamager();
            // Check if the damager is a hostile mob
            if (damager instanceof Monster) {
                // Use the player's facing direction as the force direction, you can adjust this vector as needed
                Vector velocity = player.getLocation().getDirection().normalize().multiply(5);
                // Set the y velocity to 1
                velocity.setY(1);
                player.setVelocity(velocity);
            }
        }
    }

    private boolean hasSpireTrim(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item == null || !item.getType().name().contains("NETHERITE") || item.getItemMeta() == null) {
                return false;
            }
            if (item.getItemMeta() instanceof ArmorMeta itemMeta) {
                // 全套高塔盔甲纹饰下界合金装备
                itemMeta = (ArmorMeta) item.getItemMeta();
                if (itemMeta == null ||
                        itemMeta.getTrim() == null ||
                        itemMeta.getTrim().getPattern() != TrimPattern.SPIRE) {
                    System.out.println("false");
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    @EventHandler
    public void onPiglinTarget(EntityTargetEvent event) {
        if (event.getEntityType() != EntityType.PIGLIN) {
            return;
        }

        if (!(event.getTarget() instanceof Player player)) {
            return;
        }

        if (hasGoldTrim(player)) {
            event.setCancelled(true);
        }
    }

    private boolean hasGoldTrim(Player player) {
        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) {
                Material material = item.getType();
                String materialName = material.name();
                // 金质装备
                if (materialName.contains("GOLDEN")) {
                    return true;
                }
                // 镶金下界合金装备、镶金钻石装备
                if (materialName.contains("NETHERITE") ||
                        materialName.contains("DIAMOND")) {
                    if (item.getItemMeta() == null) {
                        return false;
                    }
                    if (item.getItemMeta() instanceof ArmorMeta itemMeta) {
                        itemMeta = (ArmorMeta) item.getItemMeta();
                        if (itemMeta != null &&
                                itemMeta.getTrim() != null &&
                                itemMeta.getTrim().getMaterial() == TrimMaterial.GOLD) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
