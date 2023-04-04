package me.thejokerdev.staffmode.menus;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.type.Button;
import me.thejokerdev.staffmode.type.Menu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerSelector extends Menu {
    int page = 0;
    public BukkitTask task;
    private HashMap<ItemStack, String> players = new HashMap<>();
    private Button nextPage;
    private Button previousPage;
    private Button noPlayers;
    public PlayerSelector(Main plugin, Player var1) {
        super(plugin, var1, "selector", true);

        updateLang();
    }

    @Override
    public void onOpen(InventoryOpenEvent var1) {
        update();
        if (getConfig().get("settings.update")==null){
            return;
        }
        int delay = getConfig().getInt("settings.update");
        if (delay == -1){
            return;
        }
        if (task != null){
            task.cancel();
            task = null;
        }
        task = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, delay);
    }

    @Override
    public void onClose(InventoryCloseEvent var1) {
        plugin.getDataManager().getDataPlayer(getPlayer()).setSelector(null);
        if (task != null){
            task.cancel();
            task = null;
        }
    }

    @Override
    public void onClick(InventoryClickEvent var1) {
        for (Button b : buttons){
            if (b.getSlot().contains(var1.getSlot()) && plugin.getUtils().compareItems(var1.getCurrentItem(), b.getItem().build(getPlayer()))){
                if (!b.canView()){
                    continue;
                }
                if (!b.executeItemInMenuActions(var1)){
                    return;
                }
            }
        }

        ItemStack item = var1.getCurrentItem();
        if (item == null || item.getType() == Material.AIR){
            return;
        }
        if (nextPage.getSlot().contains(var1.getSlot()) && plugin.getUtils().compareItems(item, nextPage.getItem().build(getPlayer()))){
            page++;
            nextPage.executeItemInMenuActions(var1);
            update();
            return;
        }

        if (previousPage.getSlot().contains(var1.getSlot()) && plugin.getUtils().compareItems(item, previousPage.getItem().build(getPlayer()))){
            page--;
            previousPage.executeItemInMenuActions(var1);
            update();
            return;
        }
        String target = players.get(item);
        if (target == null){
            getPlayer().closeInventory();
            return;
        }
        Player p = plugin.getServer().getPlayer(target);
        if (p == null){
            getPlayer().closeInventory();
            return;
        }
        String selector = plugin.getDataManager().getDataPlayer(getPlayer()).getSelector();
        if (selector == null){
            getPlayer().closeInventory();
            return;
        }
        plugin.getDataManager().getDataPlayer(getPlayer()).setSelector(null);
        switch (selector.toLowerCase()){
            case "froze" -> {
                getPlayer().chat("/staff froze " + p.getName());
                getPlayer().closeInventory();
            }
            case "rtp" -> {
                getPlayer().chat("/staff rtp " + p.getName());
                getPlayer().closeInventory();
            }
            case "invsee" -> getPlayer().chat("/staff invsee " + p.getName());
        }
    }

    @Override
    public void update() {
        List<Integer> notUsed = new ArrayList<>();
        for (Button b : buttons){
            if (!b.canView()){
                continue;
            }
            setItem(b);
            notUsed.addAll(b.getSlot());
        }
        List<Player> players = new ArrayList<>();
        for (Player p : plugin.getServer().getOnlinePlayers()){
            if (plugin.getDataManager().getDataPlayer(p).isInStaff()){
                continue;
            }
            players.add(p);
        }

        if (page > 0 && players.size() < page*28+1){
            page = 0;
            update();
            return;
        }

        if (page > 0 && previousPage!=null){
            setItem(previousPage);
            notUsed.addAll(previousPage.getSlot());
        }
        if (players.size() > (page+1)*28 && nextPage!=null){
            setItem(nextPage);
            notUsed.addAll(nextPage.getSlot());
        }
        if (players.size() > 28){
            players = players.subList(page*28, Math.min(page*28+28, players.size()));
        }
        if (players.size() > 0){
            for (int i = 0; i < players.size(); i++){
                int slot = 10+i;
                if (slot >= 17){
                    slot += 2;
                }
                if (slot >= 26){
                    slot += 2;
                }
                if (slot >= 35){
                    slot += 2;
                }
                Player p = players.get(i);
                Button b = getPlayerHead(p);
                ItemStack item = b.getItem().build(p);
                setItem(slot, item);
                notUsed.add(slot);
                this.players.put(item, p.getName());
            }
        } else {
            if (noPlayers != null) {
                setItem(noPlayers);
                notUsed.addAll(noPlayers.getSlot());
            }
        }

        for (int i = 0; i < getInventory().getSize(); i++){
            if (notUsed.contains(i)){
                continue;
            }
            setItem(i, new ItemStack(Material.AIR));
        }
    }

    public Button getPlayerHead(Player player){
        return new Button(player, getConfig(), "items.player-head");
    }

    @Override
    public void updateLang() {
        setTitle(getConfig().getString("settings.title"));
        buttons.clear();
        if (getConfig().get("extra-items")!=null){
            for (String key : getConfig().getConfigurationSection("extra-items").getKeys(false)){
                key = "extra-items."+key;
                buttons.add(new Button(getPlayer(), getConfig(), key));
            }
        }
        if (getConfig().get("items.next-page")!=null){
            nextPage = new Button(getPlayer(), getConfig(), "items.next-page");
        }
        if (getConfig().get("items.previous-page")!=null){
            previousPage = new Button(getPlayer(), getConfig(), "items.previous-page");
        }
        if (getConfig().get("items.no-players")!=null){
            noPlayers = new Button(getPlayer(), getConfig(), "items.no-players");
        }
    }
}
