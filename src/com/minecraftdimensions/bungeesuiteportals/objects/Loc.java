package com.minecraftdimensions.bungeesuiteportals.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Loc {
	
	String world;
	double x;
	double y;
	double z;
	

	public Loc(String world, double x, double y, double z){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;		
	}
	
	public Location getLocation(){
		return new Location(Bukkit.getWorld(world),x,y,z);
	}
	
	public Block getBlock(){
		return getLocation().getBlock();
	}
	
	public boolean equals(Location loc){
		if(!loc.getWorld().getName().equals(world)){
			return false;
		}
		if(loc.getBlockX()!=x){
			return false;
		}
		if(loc.getBlockY()!=y){
			return false;
		}
		if(loc.getBlockZ()!=z){
			return false;
		}
		return true;
	}
	
	public boolean equals(Block block){
		Location loc = block.getLocation();
		if(!loc.getWorld().getName().equals(world)){
			return false;
		}
		if(loc.getBlockX()!=x){
			return false;
		}
		if(loc.getBlockY()!=y){
			return false;
		}
		if(loc.getBlockZ()!=z){
			return false;
		}
		return true;
	}
}
