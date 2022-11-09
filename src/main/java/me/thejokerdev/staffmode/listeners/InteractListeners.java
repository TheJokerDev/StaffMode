package me.thejokerdev.staffmode.listeners;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.type.Button;
import me.thejokerdev.staffmode.type.DataPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InteractListeners implements Listener {
    private Main plugin;
    private final HashMap<UUID, HashMap<Button, Long>> time = new HashMap();

    public InteractListeners(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        ItemStack item = e.getItem();

        if (item == null){
            return;
        }
        if (item.getType() == Material.AIR){
            return;
        }
        if (dp.getItems().keySet().size()>0){
            for (Button b : dp.getItems().values()) {
                if (!b.canView()){
                    continue;
                }
                if (b.getItem().build(p).isSimilar(item)) {
                    if (b.getCooldown() > 0) {
                        if (this.canUseItem(b, p.getUniqueId(), b.getCooldown() * 1000)) {
                            HashMap<Button, Long> preMap;
                            if (this.time.containsKey(p.getUniqueId())) {
                                preMap = this.time.get(p.getUniqueId());
                            } else {
                                preMap = new HashMap<>();
                            }
                            preMap.put(b, System.currentTimeMillis());
                            this.time.put(p.getUniqueId(), preMap);

                            b.executePhysicallyItemsActions(e);
                            if (b.canInteract()){
                                e.setCancelled(false);
                            }
                            return;
                        } else {
                            String msg = plugin.getUtils().getKey("general.cooldown");
                            double timeleft = Math.round((float) (((Long) ((HashMap<?, ?>) this.time.get(p.getUniqueId())).get(b) + (b.getCooldown() * 1000L) - System.currentTimeMillis()) / 1000L * 100L));
                            plugin.getUtils().sendMSG(p, msg.replace("{time}", String.valueOf((int)(timeleft / 100.0D))));
                        }
                    } else {
                        b.executePhysicallyItemsActions(e);
                        if (b.canInteract()){
                            e.setCancelled(false);
                        }
                    }
                }
            }
        }
    }

    private boolean canUseItem(Button b, UUID uuid, int cooldown) {
        if (this.time.containsKey(uuid)) {
            long current = System.currentTimeMillis();
            if (this.time.get(uuid).containsKey(b)) {
                return (Long)((HashMap<?, ?>)this.time.get(uuid)).get(b) + (long)cooldown <= current;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
