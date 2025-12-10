package de.scholle.itemClear;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public final class ItemClear extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private Random random = new Random();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("ItemClear enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ItemClear disabled!");
    }

    // When a player picks up an item
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        scheduleInventoryItemClear(event.getPlayer(), event.getItem().getItemStack());
    }

    // When a player crafts an item
    @EventHandler
    public void onItemCraft(CraftItemEvent event) {
        ItemStack result = event.getCurrentItem();
        if (result != null && result.getType() != Material.AIR) {
            // Schedule the crafted item for deletion
            Player player = (Player) event.getWhoClicked();
            scheduleInventoryItemClear(player, result);
        }
    }

    private void scheduleInventoryItemClear(Player player, ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        long delayTicks = getDelayTicks();
        if (delayTicks <= 0) return;

        // Clone the item to track the specific stack
        ItemStack trackedItem = itemStack.clone();

        new BukkitRunnable() {
            @Override
            public void run() {
                // Remove the tracked item from player's inventory
                ItemStack[] contents = player.getInventory().getContents();
                for (int i = 0; i < contents.length; i++) {
                    ItemStack current = contents[i];
                    if (current != null && current.isSimilar(trackedItem)) {
                        // Reduce amount accordingly
                        int remaining = current.getAmount() - trackedItem.getAmount();
                        if (remaining > 0) {
                            current.setAmount(remaining);
                            player.getInventory().setItem(i, current);
                        } else {
                            player.getInventory().setItem(i, null);
                        }
                        break;
                    }
                }
            }
        }.runTaskLater(this, delayTicks);
    }

    private long getDelayTicks() {
        String mode = config.getString("mode", "exact").toLowerCase();
        if (mode.equals("exact")) {
            int seconds = config.getInt("exactTimeSeconds", 60);
            return seconds * 20L;
        } else if (mode.equals("random")) {
            int min = config.getInt("randomMinSeconds", 30);
            int max = config.getInt("randomMaxSeconds", 120);
            int randomSeconds = min + random.nextInt(max - min + 1);
            return randomSeconds * 20L;
        }
        return 0;
    }
}
