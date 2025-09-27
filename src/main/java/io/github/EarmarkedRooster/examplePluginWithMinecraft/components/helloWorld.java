package io.github.EarmarkedRooster.examplePluginWithMinecraft.components;

import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.TextColor.color;

public class helloWorld {
    public static Component helloWorld()
    {
        return text()
                .content("Hello").color(color(0x2D5BF8))
                .append(text(" world!", BLUE))
                .build();
    }
}
