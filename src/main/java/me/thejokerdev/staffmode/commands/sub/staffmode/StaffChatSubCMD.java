package me.thejokerdev.staffmode.commands.sub.staffmode;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.enums.SenderTypes;
import me.thejokerdev.staffmode.type.DataPlayer;
import me.thejokerdev.staffmode.type.SubCMD;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StaffChatSubCMD extends SubCMD {

    public StaffChatSubCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "chat";
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
        if (args.length == 0){
            boolean status = !dp.isInStaffChat();
            String msg = getPlugin().getUtils().getKey("commands.main.staff-chat.msg");
            String activated = getPlugin().getUtils().getKey("commands.main.staff-chat.activate");
            String deactivated = getPlugin().getUtils().getKey("commands.main.staff-chat.deactivate");
            msg = msg.replace("{status}", status ? activated : deactivated);
            dp.setInStaffChat(status);
            p.playSound(p, Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
            sendMSG(sender, msg);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i]);
                if (i != args.length - 1) {
                    sb.append(" ");
                }
            }
            getPlugin().getUtils().sendStaffMSG(p, sb.toString());
        }
        return false;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
