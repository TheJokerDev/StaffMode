package me.thejokerdev.staffmode;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.thejokerdev.staffmode.type.DataPlayer;
import org.bukkit.entity.Player;

public class Placeholders extends PlaceholderExpansion {
    private Main plugin;

    public Placeholders(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player != null){
            DataPlayer dp = plugin.getDataManager().getDataPlayer(player);
            if (params.equalsIgnoreCase("chatstatus")){
                boolean status = dp.isInStaffChat();
                return plugin.getUtils().formatMSG(player, "messages.staff-chat."+(status ? "on" : "off"));
            }
            if (params.equalsIgnoreCase("staffstatus")){
                boolean status = dp.isInStaff();
                return plugin.getUtils().formatMSG(player, "messages.staff-mode."+(status ? "on" : "off"));
            }
            if (params.equalsIgnoreCase("vanished")){
                boolean status = dp.isVanished();
                return status+"";
            }
            if (params.equalsIgnoreCase("status")){
                boolean status = dp.isInStaff();
                return !status ? "" : plugin.getUtils().formatMSG(player, "messages.staff-tag");
            }
        }
        return super.onPlaceholderRequest(player, params);
    }
}
