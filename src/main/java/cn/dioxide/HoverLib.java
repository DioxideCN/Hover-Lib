package cn.dioxide;

import cn.dioxide.command.MainCommand;
import cn.dioxide.extension.CustomRecipe;
import cn.dioxide.service.*;
import cn.dioxide.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HoverLib extends JavaPlugin {
    public static HoverLib executor;

    @Override
    public void onEnable() {
        // Plugin startup logic
        send("&a[HoverLib] has enabled");

        Objects.requireNonNull(Bukkit.getPluginCommand("hlib")).setExecutor(new MainCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("hlib")).setTabCompleter(new MainCommand());

        // 事件
        ClickBlockEvent clickBlockEvent = new ClickBlockEvent();
        ClickItemFrameEvent clickItemFrameEvent = new ClickItemFrameEvent();
        SmeltingEvent smeltingEvent = new SmeltingEvent();
        BreakEvent breakEvent = new BreakEvent();
        EquipEvent equipEvent = new EquipEvent();

        // 注册事件
        Bukkit.getPluginManager().registerEvents(clickBlockEvent, this);
        Bukkit.getPluginManager().registerEvents(clickItemFrameEvent, this);
        Bukkit.getPluginManager().registerEvents(smeltingEvent, this);
        Bukkit.getPluginManager().registerEvents(breakEvent, this);
        Bukkit.getPluginManager().registerEvents(equipEvent, this);

        // 定时任务
        getServer().getScheduler().runTaskTimer(this, LoopTaskEvent::spreadWaterBreath, 0L, 20L);

        // 合成配方
        new CustomRecipe().init(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        send("&c[HoverLib] has been disabled");
    }

    public void send(String msg) {
        this.getLogger().info(ColorUtil.format(msg));
    }
}
