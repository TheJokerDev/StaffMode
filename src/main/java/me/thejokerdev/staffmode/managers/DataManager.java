package me.thejokerdev.staffmode.managers;

import lombok.Getter;
import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.type.DataPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class DataManager implements Listener {
    private Main plugin;
    @Getter private HashMap<UUID, DataPlayer> players;

    public DataManager(Main plugin){
        this.plugin = plugin;
        plugin.listener(this);
        players = new HashMap<>();
    }

    public DataPlayer getDataPlayer(Player p){
        DataPlayer dataPlayer = players.get(p.getUniqueId());
        if (dataPlayer == null){
            dataPlayer = new DataPlayer(p.getUniqueId());
            players.put(p.getUniqueId(), dataPlayer);
        }
        return dataPlayer;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e){
        DataPlayer dp = new DataPlayer(e.getPlayer());
        players.put(e.getPlayer().getUniqueId(), dp);
        if (!dp.isInStaff() && e.getPlayer().hasPermission("staffmode.staff")){
            dp.setInStaff(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(PlayerQuitEvent e){
        if (players.containsKey(e.getPlayer().getUniqueId())){
            DataPlayer player = players.get(e.getPlayer().getUniqueId());
            if (player.isInStaff()){
                player.setInStaff(false);
            }
            players.remove(e.getPlayer().getUniqueId());
        }
    }

}
