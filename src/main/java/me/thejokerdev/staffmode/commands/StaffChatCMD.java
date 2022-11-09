package me.thejokerdev.staffmode.commands;

import me.thejokerdev.staffmode.Main;
import me.thejokerdev.staffmode.type.CMD;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StaffChatCMD extends CMD {

    public StaffChatCMD(Main plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "staffchat";
    }

    @Override
    public String getDescription() {
        return "Chat for staff members";
    }

    @Override
    public String getPermission() {
        return "staffmode.staff";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("sc");
    }

    @Override
    public String getPermissionError() {
        return null;
    }

    @Override
    public boolean isTabComplete() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p){
            StringBuilder sb = new StringBuilder();
            if (args.length > 0){
                sb.append(" ");
                for (int i = 0; i < args.length; i++) {
                    sb.append(args[i]);
                    if (i != args.length - 1) {
                        sb.append(" ");
                    }
                }
            }
            p.chat("/staff chat"+ sb);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
