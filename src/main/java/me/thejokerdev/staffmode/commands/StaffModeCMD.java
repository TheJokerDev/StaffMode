package me.thejokerdev.staffmode.commands;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.commands.sub.staffmode.FrozeSubCMD;
import me.thejokerdev.staffmode.commands.sub.staffmode.RandomTPSubCMD;
import me.thejokerdev.staffmode.commands.sub.staffmode.ReloadSubCMD;
import me.thejokerdev.staffmode.commands.sub.staffmode.StaffChatSubCMD;
import me.thejokerdev.staffmode.type.CMD;
import me.thejokerdev.staffmode.type.DataPlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffModeCMD extends CMD {

    public StaffModeCMD(Main plugin) {
        super(plugin);
        addSubCMD(new ReloadSubCMD(plugin));
        addSubCMD(new RandomTPSubCMD(plugin));
        addSubCMD(new FrozeSubCMD(plugin));
        addSubCMD(new StaffChatSubCMD(plugin));
    }

    @Override
    public String getName() {
        return "staffmode";
    }

    @Override
    public String getDescription() {
        return "Staff mode main command.";
    }

    @Override
    public String getPermission() {
        return "staffmode.staff";
    }

    @Override
    public String getPermissionError() {
        return getPlugin().getUtils().getKey("general.noPermission");
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("staff", "modostaff");
    }

    @Override
    public boolean isTabComplete() {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && sender instanceof Player p){
            DataPlayer dp = getPlugin().getDataManager().getDataPlayer(p);
            boolean status = !dp.isInStaff();
            String msg = getPlugin().getUtils().getKey("commands.main.toggle.msg");
            String activated = getPlugin().getUtils().getKey("commands.main.toggle.activate");
            String deactivated = getPlugin().getUtils().getKey("commands.main.toggle.deactivate");
            msg = msg.replace("{status}", status ? activated : deactivated);
            dp.setInStaff(status);
            p.playSound(p, status ? Sound.ENTITY_PLAYER_LEVELUP : Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            sendMSG(sender, msg);
            return true;
        }
        return executeCMD(sender, label, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0){
            return execute(sender, label, args);
        }
        return new ArrayList<>();
    }
}
