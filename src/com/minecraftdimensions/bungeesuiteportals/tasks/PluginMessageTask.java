package com.minecraftdimensions.bungeesuiteportals.tasks;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.minecraftdimensions.bungeesuiteportals.BungeeSuitePortals;

public class PluginMessageTask extends BukkitRunnable {

	private final ByteArrayOutputStream bytes;

	public PluginMessageTask(ByteArrayOutputStream bytes) {
		this.bytes = bytes;
	}
	
	public void run() {
			getOnlinePlayers().get(0).sendPluginMessage(
					BungeeSuitePortals.INSTANCE,
					BungeeSuitePortals.OUTGOING_PLUGIN_CHANNEL,
					bytes.toByteArray());
	}
	
	private List<Player> getOnlinePlayers() {
		List<Player> back = new ArrayList<>();
		for (Player all : Bukkit.getOnlinePlayers()) { back.add(all); }
		return back;
	}

}