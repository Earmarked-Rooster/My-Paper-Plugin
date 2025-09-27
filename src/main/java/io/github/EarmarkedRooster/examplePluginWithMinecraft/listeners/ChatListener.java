package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import net.kyori.adventure.audience.Audience;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener, ChatRenderer {

    // Listen for the AsyncChatEvent
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(this); // Tell the event to use our renderer
    }

    @Override
    public Component render(Player source, Component sourceDisplayName, Component message, Audience viewer) {
        return sourceDisplayName
                .append(Component.text(":"))
                .append(message);

    }
}
