package cn.dioxide.extension;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Dioxide.CN
 * @date 2023/6/8
 * @since 1.0
 */
public class CustomRecipe {

    private JavaPlugin plugin;

    public void init(JavaPlugin plugin) {
        this.plugin = plugin;
        createCustomRecipe();
    }

    public void createCustomRecipe() {
        pitcherPlantPainting();
        torchFlowerPainting();
        phantomMembranePainting();
    }

    public void phantomMembranePainting() {
        ItemStack item = new ItemStack(Material.PAINTING);
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound entityTag = nbtItem.addCompound("EntityTag");
        entityTag.setString("variant", "minecraft:wind");
        ItemStack finalItem = nbtItem.getItem();
        NamespacedKey key = new NamespacedKey(plugin, "custom_painting_wind");
        ShapedRecipe recipe = new ShapedRecipe(key, finalItem);
        recipe.shape("SMS", "MGM", "SMS");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('M', Material.PHANTOM_MEMBRANE);
        recipe.setIngredient('G', Material.LEATHER);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void pitcherPlantPainting() {
        ItemStack item = new ItemStack(Material.PAINTING);
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound entityTag = nbtItem.addCompound("EntityTag");
        entityTag.setString("variant", "minecraft:water");
        ItemStack finalItem = nbtItem.getItem();
        NamespacedKey key = new NamespacedKey(plugin, "custom_painting_water");
        ShapedRecipe recipe = new ShapedRecipe(key, finalItem);
        recipe.shape("SMS", "MGM", "SMS");
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('M', Material.PITCHER_PLANT);
        recipe.setIngredient('G', Material.LEATHER);
        Bukkit.getServer().addRecipe(recipe);
    }

    public void torchFlowerPainting() {
        // 创建一个新的ItemStack，它的物品类型是画
        ItemStack item = new ItemStack(Material.PAINTING);

        // 使用Item-NBT-API来修改ItemStack的NBT数据
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound entityTag = nbtItem.addCompound("EntityTag");
        entityTag.setString("variant", "minecraft:fire");
        ItemStack finalItem = nbtItem.getItem();

        // 创建一个新的有序合成配方
        NamespacedKey key = new NamespacedKey(plugin, "custom_painting_fire");
        ShapedRecipe recipe = new ShapedRecipe(key, finalItem);

        // 设置合成配方的形状
        recipe.shape("SMS", "MGM", "SMS");

        // 设置每个字符代表的物品
        recipe.setIngredient('S', Material.STICK);
        recipe.setIngredient('M', Material.TORCHFLOWER);
        recipe.setIngredient('G', Material.LEATHER);

        // 将这个配方添加到服务器的合成书中
        Bukkit.getServer().addRecipe(recipe);
    }

}
