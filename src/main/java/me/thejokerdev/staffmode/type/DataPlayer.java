package me.thejokerdev.staffmode.type;

import com.cryptomorin.xseries.XMaterial;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import me.thejokerdev.staffmode.Main;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

@Getter
public class DataPlayer {
    private final Main plugin = Main.getPlugin();
    private Player player;
    private String name;
    private UUID uuid;
    @Setter private HashMap<String, Button> items = new HashMap<>();

    //Staff utils
    private boolean inStaff = false;
    @Setter private boolean inStaffChat = false;
    private ItemStack[] storage;
    private Collection<PotionEffect> potionEffects;
    private double health;
    private int foodLevel;
    private int level;
    private float exp;
    private boolean fly;
    private GameMode gameMode;
    private ItemStack helmet;
    private List<String> teleported = new ArrayList<>();

    private boolean isFrozen = false;
    @Setter private boolean frozeCheck = false;

    public DataPlayer(Player player){
        this.player = player;
        loadItems();

    }

    public void setFrozen(boolean frozen) {
        if (frozen) {
            if (getPlayer().getInventory().getHelmet() != null){
                helmet = getPlayer().getInventory().getHelmet();
            }
            getPlayer().getInventory().setHelmet(XMaterial.ICE.parseItem());
        } else {
            if (helmet != null){
                getPlayer().getInventory().setHelmet(helmet);
                helmet = null;
            } else {
                getPlayer().getInventory().setHelmet(XMaterial.AIR.parseItem());
            }
            frozeCheck = false;
        }
        isFrozen = frozen;
    }

    public void setInStaff(boolean inStaff) {
        Player p = getPlayer();
        plugin.getServer().dispatchCommand(p, "co i");
        if (inStaff) {
            if (!inStaffChat) {
                inStaffChat = true;
            }
            storage = p.getInventory().getContents();
            p.getInventory().clear();
            potionEffects = p.getActivePotionEffects();
            potionEffects.forEach(potionEffect -> p.removePotionEffect(potionEffect.getType()));
            health = p.getHealth();
            p.setHealth(20);
            foodLevel = p.getFoodLevel();
            p.setFoodLevel(20);
            level = p.getLevel();
            p.setLevel(0);
            exp = p.getExp();
            p.setExp(0);
            fly = p.getAllowFlight();
            gameMode = p.getGameMode();
            p.setGameMode(GameMode.SURVIVAL);
            p.setAllowFlight(true);
            p.setFlying(true);
            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
            if (items.size() > 0){
                for (Button b : items.values()) {
                    for (Integer i : b.getSlot()) {
                        p.getInventory().setItem(i, b.getItem().build(p));
                    }
                }
            }
        } else {
            p.getInventory().setContents(storage);
            storage = null;
            p.getActivePotionEffects().forEach(enchant -> p.removePotionEffect(enchant.getType()));
            p.addPotionEffects(potionEffects);
            potionEffects = null;
            p.setHealth(health < 20 ? health : 20);
            health = 0;
            p.setFoodLevel(foodLevel);
            foodLevel = 0;
            p.setLevel(level);
            level = 0;
            p.setExp(exp);
            exp = 0.0f;
            p.setAllowFlight(fly);
            fly = false;
            p.setGameMode(gameMode);
            gameMode = null;
        }
        plugin.getServer().dispatchCommand(p, "v "+(inStaff ? "on" : "off"));
        String cmd = inStaff ? "set tab.staff true" : "unset tab.staff";
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user "+p.getUniqueId()+" permission "+cmd);
        cmd = inStaff ? "set worldedit.navigation.jumpto.tool true" : "unset worldedit.navigation.jumpto.tool";
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user "+p.getUniqueId()+" permission "+cmd);
        cmd = inStaff ? "set worldedit.navigation.thru.tool true" : "unset worldedit.navigation.thru.tool";
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "lp user "+p.getUniqueId()+" permission "+cmd);
        this.inStaff = inStaff;
    }

    public DataPlayer(String name){
        this.name = name;
        player = plugin.getServer().getPlayer(name);
        if (player != null){
            uuid = player.getUniqueId();
        }
        loadItems();
    }

    public DataPlayer(UUID uuid){
        this.uuid = uuid;
        player = plugin.getServer().getPlayer(uuid);
        if (player != null){
            name = player.getName();
        }
        loadItems();
    }

    public void loadItems(){
        boolean replace = false;
        List<String> replacing = new ArrayList<>();
        if (items.size() > 0){
            for (ItemStack item : getPlayer().getInventory().getContents()){
                if (item == null) continue;
                for (Map.Entry<String, Button> b : items.entrySet()){
                    if (item.isSimilar(b.getValue().getItem().build(getPlayer()))){
                        replace = true;
                        replacing.add(b.getKey());
                    }
                }
            }
        }
        items.clear();
        if (plugin.getConfigUtil().getItems().getKeys(false).size() > 0){
            for (String key : plugin.getConfigUtil().getItems().getKeys(false)){
                Button b = new Button(getPlayer(), plugin.getConfigUtil().getItems(), key);
                items.put(key, b);
                if (replace && replacing.contains(key)){
                    b.getSlot().forEach(integer -> getPlayer().getInventory().setItem(integer, b.getItem().build(getPlayer())));
                }
            }
        }

    }
}
