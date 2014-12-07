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

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class BlockListener implements Listener {

public static StrictWGUseFlag plugin;
public WorldGuardPlugin wg;

	public BlockListener(StrictWGUseFlag instance) {
		plugin = instance;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerInteract (PlayerInteractEvent event)
	{
        if(!event.hasBlock())return;
        Block clicked = event.getClickedBlock();
        World world = clicked.getWorld();
        LocalPlayer localPlayer = wg.wrapPlayer(event.getPlayer());
        Location loc = clicked.getLocation();
        RegionManager rm = wg.getRegionManager(world);
        ApplicableRegionSet ars = rm.getApplicableRegions(loc);
        if(plugin.isExempted(clicked.getTypeId()))return;
        if(!ars.allows(DefaultFlag.USE,localPlayer) && !wg.getGlobalRegionManager().hasBypass(localPlayer,world)) {
            if(!ars.canBuild(localPlayer)) {
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
            }
		}
	}
	

	
	
	
	
}
