package rip.orbit.mars.match.listener;

import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockDropItemsEvent;


public class MatchBlockPickupListener implements Listener {

    /*@EventHandler
    public void onBlockDropItems(BlockDropItemsEvent event) {
        Player recipient = event.getPlayer();
        if (recipient == null) return;

        Match match = Mars.getInstance().getMatchHandler().getMatchPlaying(recipient);
        if (match == null) return;

        if (!match.getKitType().getId().equals("Spleef")) return;

        List<Item> items = event.getToDrop();
        for (Item item : items) {
            ItemStack stack = item.getItemStack();
            stack.setType(Material.SNOW_BALL);
            recipient.getInventory().addItem(stack);
        }

        items.clear();
    }*/
}
