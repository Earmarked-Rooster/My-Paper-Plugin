package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.events.PlayerLevelUpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelListener implements Listener {
    /*
    method listens for our custom event
    learn more about handler list
     */
    @EventHandler
    public void onPlayerLevelUp(PlayerLevelUpEvent event) {
        // get data abt our new player (event object)
        Player player = event.getPlayer();
        int newLevel = event.getNewLevel();

        player.sendMessage("You leveled up to level " + newLevel);
        player.getWorld().strikeLightningEffect(player.getLocation());
    }
}
