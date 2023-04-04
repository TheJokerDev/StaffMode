package me.thejokerdev.staffmode.listeners;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.type.Button;
import me.thejokerdev.staffmode.type.DataPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class StaffListeners implements Listener {
    private Main plugin;

    public StaffListeners(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if ((dp.isInStaff() || dp.isFrozen()) && !dp.isVanished()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractWithEntity(PlayerInteractAtEntityEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isInStaff()){
            e.setCancelled(true);
            if (e.getRightClicked() instanceof Player t){
                DataPlayer dp2 = plugin.getDataManager().getDataPlayer(t);
                if (!e.getHand().equals(EquipmentSlot.HAND)){
                    return;
                }
                if (dp2.isInStaff()){
                    plugin.getUtils().sendMSG(p, "messages.interact.isStaff");
                    return;
                }
                Button b = dp.getItems().get("invsee");
                if (b != null){
                    ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                    if (item != null){
                        if (item.isSimilar(b.getItem().build(p))){
                            for (String s : b.rightClickActions()){
                                if (s.startsWith("[invsee]")){
                                    s = s.replace("[invsee]", "");
                                    s = s.replace("{clicked}", t.getName());
                                    plugin.getUtils().actions(p, new ArrayList<>(Arrays.asList(s)));
                                    String msg = plugin.getUtils().getKey("messages.interact.opening");
                                    msg = msg.replace("{clicked}", t.getName());
                                    plugin.getUtils().sendMSG(p, msg);
                                    break;
                                }
                            }
                        }
                    }
                }
                b = dp.getItems().get("froze");
                if (b != null){
                    ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                    if (item != null){
                        if (item.isSimilar(b.getItem().build(p))){
                            for (String s : b.rightClickActions()){
                                if (s.startsWith("[froze]")){
                                    s = s.replace("[froze]", "");
                                    s = s.replace("{clicked}", t.getName());
                                    plugin.getUtils().actions(p, new ArrayList<>(Arrays.asList(s)));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void swapItem(PlayerSwapHandItemsEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isInStaff() || dp.isFrozen()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void clickInventory(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        ItemStack item = e.getCurrentItem();
        boolean check = dp.getItems().size() == 0;
        if (!check && dp.isInStaff() && item != null){
            for (Button b : dp.getItems().values()) {
                if (item.isSimilar(b.getItem().build(p))){
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if ((dp.isInStaff() && check) || dp.isFrozen()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isFrozen()){
            boolean check = e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ();
            if (dp.isFrozeCheck()){
                if (check){
                    e.setCancelled(true);
                }
            } else {
                if (p.isOnGround()){
                    dp.setFrozeCheck(true);
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isInStaffChat()){
            e.setCancelled(true);
            plugin.getUtils().sendStaffMSG(p, e.getMessage());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isInStaff()){
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void dragItem(InventoryDragEvent e){
        Player p = (Player) e.getWhoClicked();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        ItemStack item = e.getCursor();
        boolean check = dp.getItems().size() == 0;
        if (!check && dp.isInStaff() && item != null){
            for (Button b : dp.getItems().values()) {
                if (item.isSimilar(b.getItem().build(p))){
                    e.setCancelled(true);
                    return;
                }
            }
        }
        if ((dp.isInStaff() && check) || dp.isFrozen()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void noXP(PlayerExpChangeEvent e) {
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isInStaff() || dp.isFrozen()) {
            e.setAmount(0);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isInStaff()) {
            p.setAllowFlight(true);
            p.setFlying(true);
            if (dp.isVanished()){
                dp.setVanished(true);
            }
        }
    }

    @EventHandler
    public void hitEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player p){
            DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
            if (dp.isInStaff() || dp.isFrozen()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void pickItem(PlayerPickupItemEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isInStaff() || dp.isFrozen()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if (dp.isInStaff() || dp.isFrozen()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if ((dp.isInStaff() || dp.isFrozen()) && !dp.isVanished()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
        if ((dp.isInStaff() || dp.isFrozen()) && !dp.isVanished()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player p){
            if (p.getPlayer() == null){
                return;
            }
            DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
            if (dp==null){
                return;
            }
            if (dp.getPlayer()==null){
                return;
            }
            if (dp.isInStaff() || dp.isFrozen()){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void blockBreak(FoodLevelChangeEvent e){
        if (e.getEntity() instanceof Player p){
            DataPlayer dp = plugin.getDataManager().getDataPlayer(p);
            if (dp.isInStaff() || dp.isFrozen()){
                e.setCancelled(true);
            }
        }
    }
}