
package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.events.PaperIsCoolEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PaperIsCoolListener implements Listener {

    @EventHandler
    public void onPaperIsCool(PaperIsCoolEvent event) {
        HandlerList handlers = event.getHandlerList();
        event.setCancelled(true);


    }
}
