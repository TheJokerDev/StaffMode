package me.thejokerdev.staffmode.commands.sub.staffmode;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.enums.SenderTypes;
import me.thejokerdev.staffmode.type.DataPlayer;
import me.thejokerdev.staffmode.type.Menu;
import me.thejokerdev.staffmode.type.SubCMD;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvseeCMD extends SubCMD {
    public InvseeCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "invsee";
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
    public boolean onCommand(CommandSender sender, String alias, String[] args) {
        Player p = (Player) sender;
        DataPlayer dp = getPlugin().getDataManager().getDataPlayer(p);
        if (!dp.isInStaff()){
            sendMSG(sender, "messages.staff.need");
            return true;
        }
        if (args.length == 1) {
            String name = args[0];
            Player target = getPlugin().getServer().getPlayer(name);
            if (target == null) {
                sendMSG(sender, "general.playerNotExists");
                return true;
            }
            DataPlayer dp2 = getPlugin().getDataManager().getDataPlayer(target);
            if (dp2.isInStaff()){
                sendMSG(sender, "messages.interact.isStaff");
                return true;
            }
            String cmd = "[cmd=OP]openinv " + target.getName();
            getPlugin().getUtils().actions(p, new ArrayList<>(List.of(cmd)));
        }
        dp.setSelector("invsee");
        Menu menu = getPlugin().getMenusManager().getPlayerMenu(p, "selector");
        if (menu != null) {
            p.openInventory(menu.getInventory());
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
