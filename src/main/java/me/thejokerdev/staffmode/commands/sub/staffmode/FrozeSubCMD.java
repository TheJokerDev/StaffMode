package me.thejokerdev.staffmode.commands.sub.staffmode;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.enums.SenderTypes;
import me.thejokerdev.staffmode.type.DataPlayer;
import me.thejokerdev.staffmode.type.SubCMD;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class FrozeSubCMD extends SubCMD {
    public FrozeSubCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "froze";
    }

    @Override
    public String getPermission() {
        return "staffmode.staff";
    }

    @Override
    public SenderTypes getSenderType() {
        return SenderTypes.BOTH;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("congelar", "frozz");
    }

    @Override
    public boolean onCommand(CommandSender sender, String alias, String[] args) {
        if (args.length == 0){
            //Open menu
            return true;
        }
        if (args.length == 1){
            if (sender instanceof Player sp){
                if (!getPlugin().getDataManager().getDataPlayer(sp).isInStaff()){
                    sendMSG(sender, "messages.staff.need");
                    return true;
                }
            }
            String name = args[0];
            Player p = getPlugin().getServer().getPlayer(name);
            if (p == null){
                sendMSG(sender, "general.playerNotExists");
                return true;
            }
            DataPlayer dp = getPlugin().getDataManager().getDataPlayer(p);
            boolean status = !dp.isFrozen();
            if (dp.isInStaff()){
                sendMSG(sender, "messages.interact.isStaff");
                return true;
            }
            String staff = getPlugin().getUtils().getKey("commands.main.froze."+(status ? "frozen" : "unfrozen")+".staff");
            staff = staff.replace("{player}", dp.getName());
            String player = getPlugin().getUtils().getKey("commands.main.froze."+(status ? "frozen" : "unfrozen")+".player");
            sendMSG(dp.getPlayer(), player);
            sendMSG(sender, staff);
            dp.setFrozen(status);
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
