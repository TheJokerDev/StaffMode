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
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.UUID;

public class DataManager implements Listener {
    private Main plugin;
    @Getter private HashMap<UUID, DataPlayer> players;
    HashMap<UUID, PermissionAttachment> perms = new HashMap<>();

    public DataManager(Main plugin){
        this.plugin = plugin;
        plugin.listener(this);
        players = new HashMap<>();
    }

    public HashMap<UUID, PermissionAttachment> getPerms() {
        return perms;
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
        boolean check = players.get(e.getPlayer().getUniqueId())!=null;
        DataPlayer dp = getDataPlayer(e.getPlayer());
        dp.setPlayer(e.getPlayer());
        if (dp.isInStaff()){
            dp.setInStaff(false);
        } else {
            players.values().forEach(dataPlayer -> {
                if (dataPlayer.isVanished()){
                    e.getPlayer().hidePlayer(plugin, dataPlayer.getPlayer());
                }
            });
        }
        if (!check){
            players.put(e.getPlayer().getUniqueId(), dp);
        }
        if (!dp.isInStaff() && e.getPlayer().hasPermission("staffmode.join.autostaff")){
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
