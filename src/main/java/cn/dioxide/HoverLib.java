package cn.dioxide;

import cn.dioxide.command.MainCommand;
import cn.dioxide.service.ClickBlockEvent;
import cn.dioxide.service.ClickItemFrameEvent;
import cn.dioxide.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
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
        Bukkit.getPluginManager().registerEvents(new ClickBlockEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ClickItemFrameEvent(), this);
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
