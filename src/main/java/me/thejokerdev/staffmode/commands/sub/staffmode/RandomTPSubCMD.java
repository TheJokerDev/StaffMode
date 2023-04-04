package me.thejokerdev.staffmode.commands.sub.staffmode;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.enums.SenderTypes;
import me.thejokerdev.staffmode.type.DataPlayer;
import me.thejokerdev.staffmode.type.Menu;
import me.thejokerdev.staffmode.type.SubCMD;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomTPSubCMD extends SubCMD {

    public RandomTPSubCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "rtp";
    }

    @Override
    public String getPermission() {
        return "staffmode.staff";
    }

    @Override
    public SenderTypes getSenderType() {
        return SenderTypes.PLAYER;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("randomteleport", "randomtp");
    }

    @Override
    public boolean onCommand(CommandSender sender, String alias, String[] args) {
        Player p = (Player) sender;
        DataPlayer dp = getPlugin().getDataManager().getDataPlayer(p);
        if (!dp.isInStaff()){
            sendMSG(sender, "messages.staff.need");
            return true;
        }
        if (args.length == 1 ) {
            if (args[0].equalsIgnoreCase("menu")) {
                dp.setSelector("rtp");
                Menu menu = getPlugin().getMenusManager().getPlayerMenu(p, "selector");
                if (menu != null) {
                    p.openInventory(menu.getInventory());
                }
                return true;
            }
            if (getPlugin().getServer().getPlayer(args[0]) != null){
                Player t = getPlugin().getServer().getPlayer(args[0]);
                DataPlayer dp2 = getPlugin().getDataManager().getDataPlayer(t);
                if (dp2.isInStaff()){
                    sendMSG(sender, "messages.interact.isStaff");
                    return true;
                }
                String msg = getPlugin().getUtils().getKey("commands.main.rtp.success");
                msg = msg.replace("{player}", t.getName());
                sendMSG(sender, msg);
                p.teleport(t);
                return true;
            }
        }
        List<Player> players = new ArrayList<>();
        for (Player t : Bukkit.getOnlinePlayers()){
            DataPlayer dp2 = getPlugin().getDataManager().getDataPlayer(t);
            if (t == p || dp.getTeleported().contains(t.getName())){
                continue;
            }
            if (!dp2.isInStaff()){
                players.add(t);
            }
        }
        if (players.size() == 0){
            if (dp.getTeleported().size() > 0){
                dp.getTeleported().clear();
                for (Player t : Bukkit.getOnlinePlayers()){
                    DataPlayer dp2 = getPlugin().getDataManager().getDataPlayer(t);
                    if (t == p || dp.getTeleported().contains(t.getName())){
                        continue;
                    }
                    if (!dp2.isInStaff()){
                        players.add(t);
                    }
                }
            }
            if (players.size() == 0){
                sendMSG(sender, "commands.main.rtp.noPlayer");
                return true;
            }
        }
        Player t = players.get(ThreadLocalRandom.current().nextInt(players.size()));
        String msg = getPlugin().getUtils().getKey("commands.main.rtp.success");
        msg = msg.replace("{player}", t.getName());
        sendMSG(sender, msg);
        p.teleport(t);
        dp.getTeleported().add(t.getName());
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        if (args.length == 1){
            return StringUtil.copyPartialMatches(args[0], List.of("menu"), new ArrayList<>());
        }
        return null;
    }
}
