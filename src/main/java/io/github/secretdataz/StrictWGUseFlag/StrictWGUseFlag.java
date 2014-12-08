//    Copyright (C) 2014 Jittapan Pluemsumran
//
//    This file is part of StrictWGUseFlag.
//
//    StrictWGUseFlag is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    StrictWGUseFlag is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with StrictWGUseFlag.  If not, see <http://www.gnu.org/licenses/>.

package io.github.secretdataz.StrictWGUseFlag;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;




public class StrictWGUseFlag extends JavaPlugin {
	public static StrictWGUseFlag plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final BlockListener listener = new BlockListener(this);
    private List<Integer> exempted = null;
	String msg = ChatColor.DARK_RED + "You don't have permission for this area.";

	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " is now disabled.");
	}

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(listener, this);
		Plugin worldGuard = pm.getPlugin("WorldGuard");
		listener.wg = (WorldGuardPlugin)worldGuard; 
		try {
			loadConfiguration();
		} catch (IOException e) {
			logger.info("[StrictWGUseFlag] Can't load config!");
		}
		
	}

	public void onReload() {
        try {
            loadConfiguration();
        } catch (IOException e) {
            logger.info("[WGStrictUseFlag] Can not load config.");
        }
    }
	public void loadConfiguration() throws IOException {
		File cfgFile = new File(this.getDataFolder() + "/config.yml");
		Integer[] temp;
		YamlConfiguration config = YamlConfiguration.loadConfiguration(cfgFile);
		temp = new Integer[] {137};
		config.addDefault("exemptedList", Arrays.asList(temp));
		config.addDefault("msg", "&4You don't have permission for this area.");
		config.options().copyDefaults(true);
		config.save(cfgFile);
        config = YamlConfiguration.loadConfiguration(cfgFile); // TODO : Better solution?
        exempted = config.getIntegerList("exemptedList");
        msg = config.getString("msg");
        msg = msg.replaceAll("&([0-9a-f])", "\u00A7$1");
	}
	public boolean isExempted(int id) {
		return exempted.contains(id);
	}
}