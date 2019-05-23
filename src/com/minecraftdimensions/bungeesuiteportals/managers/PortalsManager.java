package com.minecraftdimensions.bungeesuiteportals.managers;

import com.minecraftdimensions.bungeesuiteportals.BungeeSuitePortals;
import com.minecraftdimensions.bungeesuiteportals.objects.Portal;
import com.minecraftdimensions.bungeesuiteportals.tasks.PluginMessageTask;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.session.ClipboardHolder;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PortalsManager {

    public static boolean RECEIVED = false;
    public static HashMap<World, ArrayList<Portal>> PORTALS = new HashMap<>();
    public static HashMap<String, Location> pendingTeleports = new HashMap<>();

    public static void deletePortal( String name, String string ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "deleteportal" );
            out.writeUTF( name );
            out.writeUTF( string );

        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuitePortals.INSTANCE );

    }

    public static void removePortal( String name ) {
        Portal p = getPortal( name );
        if ( p != null ) {
            PORTALS.get( p.getWorld() ).remove( p );
            p.clearPortal();
        }
    }

    public static Portal getPortal( String name ) {
        for ( ArrayList<Portal> list : PORTALS.values() ) {
            for ( Portal p : list ) {
                if ( p.getName().equals( name ) ) {
                    return p;
                }
            }
        }
        return null;
    }

    public static void getPortalsList( String name ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "listportals" );
            out.writeUTF( name );

        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuitePortals.INSTANCE );

    }

    public static void teleportPlayer( Player p, Portal portal ) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "teleportplayer" );
            out.writeUTF( p.getName() );
            out.writeUTF( portal.getType() );
            out.writeUTF( portal.getDestination() );
            out.writeBoolean( p.hasPermission( "bungeesuite.portals.portal." + portal.getName() ) || p.hasPermission( "bungeesuite.portals.portal.*" ) );

        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuitePortals.INSTANCE );
    }

    public static void setPortal( CommandSender sender, String name, String type, String dest, String fill ) {

        Player p = ( Player ) sender;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        
        LocalSession ls = BungeeSuitePortals.WORLDEDIT.getSession(p);
        ClipboardHolder ch;
        org.bukkit.World w = p.getWorld();
        
		try {
			ch = ls.getClipboard();
			
	        try {
	            out.writeUTF( "setportal" );
	            out.writeUTF( sender.getName() );
	            if (ch == null) {
	                out.writeBoolean( false );
	            } else {
	                out.writeBoolean( true );
	                out.writeUTF( name );
	                out.writeUTF( type );
	                out.writeUTF( dest );
	                out.writeUTF( fill );
	                Location min = vectorToLoc(w, ch.getClipboard().getMinimumPoint());
	                Location max = vectorToLoc(w, ch.getClipboard().getMaximumPoint());
	                out.writeUTF( max.getWorld().getName() );
	                out.writeDouble( max.getX() );
	                out.writeDouble( max.getY() );
	                out.writeDouble( max.getZ() );
	                out.writeUTF( min.getWorld().getName() );
	                out.writeDouble( min.getX() );
	                out.writeDouble( min.getY() );
	                out.writeDouble( min.getZ() );
	            }
	        } catch ( IOException e ) {
	            e.printStackTrace();
	        }
			
		} catch (EmptyClipboardException e1) {
			p.sendMessage("§cCreate a selection and type in //copy. Then you can use this command.");
		}
		
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuitePortals.INSTANCE );

    }

    public static void addPortal( String name, String type, String dest, String filltype, Location max, Location min ) {
        if ( max.getWorld() == null ) {
            Bukkit.getConsoleSender().sendMessage( ChatColor.RED + "World does not exist portal " + name + " will not load :(" );
            return;
        }
        Portal portal = new Portal( name, type, dest, filltype, max, min );
        ArrayList<Portal> ps = PORTALS.get( max.getWorld() );
        if ( ps == null ) {
            ps = new ArrayList<>();
            PORTALS.put( max.getWorld(), ps );
        }
        ps.add( portal );
        portal.fillPortal();
    }

    public static void requestPortals() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "requestportals" );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuitePortals.INSTANCE );

    }

    public static void sendVersion() {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream( b );
        try {
            out.writeUTF( "sendversion" );
            out.writeUTF( ChatColor.RED + "Portals - " + ChatColor.GOLD + BungeeSuitePortals.INSTANCE.getDescription().getVersion() );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        new PluginMessageTask( b ).runTaskAsynchronously( BungeeSuitePortals.INSTANCE );
    }
    
    private static Location vectorToLoc(org.bukkit.World w, Vector v) {
    	return new Location(w, v.getX(), v.getY(), v.getZ());
    }
    
}
