package io.github.EarmarkedRooster.examplePluginWithMinecraft.listeners;

import io.github.EarmarkedRooster.examplePluginWithMinecraft.items.LongRangeFirework;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FireworkListener implements Listener {

    private final Plugin plugin;

    public FireworkListener(Plugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();

        if (item.getType() != org.bukkit.Material.FIREWORK_ROCKET) {
            return;
        }

        FireworkMeta fireworkMeta = (FireworkMeta) item.getItemMeta();
        NamespacedKey key = new NamespacedKey("examplepluginwithminecraft", "long_range_firework");

        if (fireworkMeta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            //event.setCancelled(true);

            Block block = event.getBlockPlaced();
            Location launchLocation = block.getLocation().add(0.5, 0.5, 0.5);

            Firework firework = player.getWorld().spawn(launchLocation, Firework.class);
            firework.setFireworkMeta(fireworkMeta);
            firework.setVelocity(new Vector(10, 5, 0));

            item.setAmount(item.getAmount() - 1);
        }
    }


    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Firework) {
            Firework firework = (Firework) event.getEntity();
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            NamespacedKey key = new NamespacedKey("examplepluginwithminecraft", "long_range_firework");

            if (fireworkMeta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                final Location initialLocation = firework.getLocation();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (firework.isDead() || firework.isDetonated()) {
                            this.cancel();
                            return;
                        }

                        if (firework.getLocation().distance(initialLocation) > 700) {
                            firework.detonate();
                            this.cancel();
                            return;
                        }

                        firework.getLocation().getChunk().load();
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            }
        }
    }

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        Firework firework = event.getEntity();
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        NamespacedKey key = new NamespacedKey("examplepluginwithminecraft", "long_range_firework");

        if (fireworkMeta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            //event.setCancelled(true);
            //firework.getWorld().createExplosion(firework.getLocation(), 10f, true, true);
            plugin.getLogger().info("LongRangeFirework has hit its target!");
        }
    }
}