

package io.github.EarmarkedRooster.examplePluginWithMinecraft.events;



import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PaperIsCoolEvent extends Event implements Cancellable {

    //Creates a new class HandlerList that contains all the listeners for the event
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Component message;
    private boolean cancelled;

    //constructor to initialize message
    public PaperIsCoolEvent(Component message) {
        this.message = message;
        this.cancelled = false;
    }

    public Component getMessage() {
        return message;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    // required for event to work IT'S NOT WORKING!!!!!!
    public static HandlerList getHandlerList(){
        return HANDLER_LIST;
    }

    // overridden from Event class for proper functionality
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    // allows event to be canceled at a certain point
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
