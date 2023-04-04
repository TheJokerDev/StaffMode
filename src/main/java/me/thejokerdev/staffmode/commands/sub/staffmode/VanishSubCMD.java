package me.thejokerdev.staffmode.commands.sub.staffmode;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.enums.SenderTypes;
import me.thejokerdev.staffmode.type.DataPlayer;
import me.thejokerdev.staffmode.type.SubCMD;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.List;

public class VanishSubCMD extends SubCMD {
    public VanishSubCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "vanish";
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
        return Arrays.asList("desaparecer", "invisible", "v");
    }

    @Override
    public boolean onCommand(CommandSender sender, String alias, String[] args) {
        if (!sender.hasPermission(getPermission())){
            sender.sendMessage(getPlugin().getUtils().getKey("general.noPermission"));
            return true;
        }
        if (args.length == 0 && sender instanceof Player p){
            DataPlayer dataPlayer = getPlugin().getDataManager().getDataPlayer(p);
            dataPlayer.setVanished(!dataPlayer.isVanished());
            String msg = getPlugin().getUtils().getKey("commands.main.vanish.msg");
            String status = getPlugin().getUtils().getKey("commands.main.vanish." + (dataPlayer.isVanished() ? "activate" : "deactivate"));
            msg = msg.replace("{status}", status);
            getPlugin().getUtils().sendMSG(p, msg);
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTab(CommandSender sender, String alias, String[] args) {
        return null;
    }
}
