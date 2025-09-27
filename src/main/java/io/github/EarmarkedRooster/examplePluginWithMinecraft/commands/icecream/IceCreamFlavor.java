package io.github.EarmarkedRooster.examplePluginWithMinecraft.commands.icecream;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum IceCreamFlavor {
    VANILLA,
    CHOCOLATE,
    STRAWBERRY;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}