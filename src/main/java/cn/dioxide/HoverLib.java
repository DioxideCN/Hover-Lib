package cn.dioxide;

import cn.dioxide.command.MainCommand;
import cn.dioxide.extension.Config;
import cn.dioxide.service.*;
import cn.dioxide.util.ColorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class HoverLib extends JavaPlugin {

    @Getter
    public static HoverLib instance;

    @Override
    public void onEnable() {
        instance = this;
        send(ColorUtil.formatNotice("&ahas enabled"));
        Config.init();
        registerCommand();
        registerEvent();
        registerScheduler();
    }

    @Override
    public void onDisable() {
        send(ColorUtil.formatNotice("&chas been disabled"));
    }

    // 注册指令
    private void registerCommand() {
        Objects.requireNonNull(Bukkit.getPluginCommand("display")).setExecutor(new MainCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("display")).setTabCompleter(new MainCommand());
    }

    // 注册事件
    private void registerEvent() {
        if (Config.get().feature.isProtectTerrain()) {
            ProtectTerrainEvent protectTerrainEvent = new ProtectTerrainEvent();
            Bukkit.getPluginManager().registerEvents(protectTerrainEvent, this);
        }
        if (Config.get().feature.isTrimUpgrade()) {
            EquipEvent equipEvent = new EquipEvent();
            Bukkit.getPluginManager().registerEvents(equipEvent, this);
        }
        if (Config.get().feature.isStupidVillager()) {
            StupidVillagerEvent stupidVillagerEvent = new StupidVillagerEvent();
            Bukkit.getPluginManager().registerEvents(stupidVillagerEvent, this);
        }
        if (Config.get().feature.isIronGolem()) {
            GolemEvent golemEvent = new GolemEvent();
            Bukkit.getPluginManager().registerEvents(golemEvent, this);
        }
        if (Config.get().feature.isCraftingTable()) {
            LootEvent lootEvent = new LootEvent();
            Bukkit.getPluginManager().registerEvents(lootEvent, this);
        }

        // 其它注册事件
        BookPlaceEvent bookPlaceEvent = new BookPlaceEvent();
        ClickItemFrameEvent clickItemFrameEvent = new ClickItemFrameEvent();

        Bukkit.getPluginManager().registerEvents(bookPlaceEvent, this);
        Bukkit.getPluginManager().registerEvents(clickItemFrameEvent, this);
    }

    // 注册定时任务
    private void registerScheduler() {
        if (Config.get().feature.isTrimUpgrade()) {
            getServer().getScheduler().runTaskTimer(this, LoopTaskEvent::spreadWaterBreath, 0L, 20L);
        }
    }

    public void send(String msg) {
        this.getLogger().info(ColorUtil.format(msg));
    }
}
