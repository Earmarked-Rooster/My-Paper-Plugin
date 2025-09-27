package io.github.EarmarkedRooster.examplePluginWithMinecraft.events;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLevelUpEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private final Player player;
    private final int newLevel;
    private final int oldLevel;

    /**
     * Constructor for our custom event.
     *
     * @param player   The player who leveled up.
     * @param oldLevel The player's previous level.
     * @param newLevel The player's new level.
     */
    public PlayerLevelUpEvent(Player player, int oldLevel, int newLevel) {
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    /**
     * Gets the player who leveled up.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the player's new level.
     *
     * @return The new level.
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * Gets the player's old level.
     *
     * @return The old level.
     */
    public int getOldLevel() {
        return oldLevel;
    }
}
