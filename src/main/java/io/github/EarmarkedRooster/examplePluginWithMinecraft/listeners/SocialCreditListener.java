package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.database.SocialCreditDatabase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SocialCreditListener implements Listener {

    private final SocialCreditDatabase socialCreditDatabase;

    public SocialCreditListener(SocialCreditDatabase socialCreditDatabase) {
        this.socialCreditDatabase = socialCreditDatabase;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            // we check to see if the player has a specific item held in their hand called in the registry "social credit item"
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();
            if (item != null && item.hasItemMeta()) {
                NamespacedKey key = new NamespacedKey("examplepluginwithminecraft", "social_credit_item");
                if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    int currentScore = socialCreditDatabase.getSocialCreditScores().getOrDefault(player.getUniqueId(), 50);
                    socialCreditDatabase.getSocialCreditScores().put(player.getUniqueId(), currentScore + 1);
                    player.sendMessage(createRainbowMessage("Your social credit has increased by 1!"));
                    // we take the item away from the player once they use it
                    item.setAmount(item.getAmount() - 1);
                }
            }
        }
    }
    // we create a rainbow message to display to the player
    private Component createRainbowMessage(String text) {
        Component component = Component.empty();
        NamedTextColor[] colors = {NamedTextColor.RED, NamedTextColor.GOLD, NamedTextColor.YELLOW, NamedTextColor.GREEN, NamedTextColor.AQUA, NamedTextColor.BLUE, NamedTextColor.LIGHT_PURPLE};
        int colorIndex = 0;
        for (char c : text.toCharArray()) {
            component = component.append(Component.text(c).color(colors[colorIndex]).decorate(TextDecoration.BOLD));
            // cycling through all the colors in our color index
            colorIndex = (colorIndex + 1) % colors.length;
        }
        return component;
    }
}
