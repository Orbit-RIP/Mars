package rip.orbit.mars.kit;

import com.google.common.collect.ImmutableList;

import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;

import org.bukkit.inventory.ItemStack;
import rip.orbit.mars.Mars;
import rip.orbit.mars.kit.listener.KitEditorListener;
import rip.orbit.mars.kit.listener.KitItemListener;
import rip.orbit.mars.kit.listener.KitLoadListener;
import rip.orbit.mars.kittype.KitType;
import rip.orbit.mars.util.MongoUtils;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class KitHandler {

    public static final String MONGO_COLLECTION_NAME = "playerKits";
    public static final int KITS_PER_TYPE = 4;

    private final Map<UUID, List<Kit>> kitData = new ConcurrentHashMap<>();

    public KitHandler() {
        Bukkit.getPluginManager().registerEvents(new KitEditorListener(), Mars.getInstance());
        Bukkit.getPluginManager().registerEvents(new KitItemListener(), Mars.getInstance());
        Bukkit.getPluginManager().registerEvents(new KitLoadListener(), Mars.getInstance());
    }

    public List<Kit> getKits(Player player, KitType kitType) {
        List<Kit> kits = new ArrayList<>();

        for (Kit kit : kitData.getOrDefault(player.getUniqueId(), ImmutableList.of())) {
            if (kit.getType() == kitType) {
                kits.add(kit);
            }
        }

        return kits;
    }

    public Optional<Kit> getKit(Player player, KitType kitType, int slot) {
        return kitData.getOrDefault(player.getUniqueId(), ImmutableList.of())
                .stream()
                .filter(k -> k.getType() == kitType && k.getSlot() == slot)
                .findFirst();
    }

    public Optional<Kit> getKit(List<Kit> kits, KitType kitType, int slot) {
        return kits
                .stream()
                .filter(k -> k.getType() == kitType && k.getSlot() == slot)
                .findFirst();
    }

    public Kit saveDefaultKit(Player player, KitType kitType, int slot) {
        Kit created = Kit.ofDefaultKit(kitType, "Kit " + slot, slot);

        kitData.computeIfAbsent(player.getUniqueId(), i -> new ArrayList<>()).add(created);
        saveKitsAsync(player);

        return created;
    }

    public void removeKit(Player player, KitType kitType, int slot) {
        boolean removed = kitData.computeIfAbsent(player.getUniqueId(), i -> new ArrayList<>())
                .removeIf(k -> k.getType() == kitType && k.getSlot() == slot);

        if (removed) {
            saveKitsAsync(player);
        }
    }

    public void saveKitsAsync(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(Mars.getInstance(), () -> {
            MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
            List kitJson = (List) JSON.parse(Mars.getGson().toJson(kitData.getOrDefault(player.getUniqueId(), ImmutableList.of())));

            Document query = new Document("_id", player.getUniqueId().toString());
            Document kitUpdate = new Document("$set", new Document("kits", kitJson));

            collection.updateOne(query, kitUpdate, MongoUtils.UPSERT_OPTIONS);
        });
    }

    public void wipeKitsForPlayer(UUID target) {
        kitData.remove(target);

        MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        collection.deleteOne(new Document("_id", target.toString()));
    }

    public int wipeKitsWithType(KitType kitType) {
        // remove kits for online players
        for (List<Kit> playerKits : kitData.values()) {
            playerKits.removeIf(k -> k.getType() == kitType);
        }

        // then the DB query
        // ref: https://docs.mongodb.com/manual/reference/operator/update/pull/
        MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        Document typeQuery = new Document("type", kitType.getId());

        collection.updateMany(
                new Document("kits", new Document("$elemMatch", typeQuery)),
                new Document("$pull", new Document("kits", typeQuery))
        );

        return -1;
    }

    public void loadKits(UUID playerUuid) {
        MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
        Document playerKits = collection.find(new Document("_id", playerUuid.toString())).first();

        if (playerKits != null) {
            List kits = playerKits.get("kits", List.class);
            Type listKit = new TypeToken<List<Kit>>() {}.getType();

            List<Kit> kitList = new ArrayList<>(Mars.getGson().fromJson(JSON.serialize(kits), listKit));

            kitData.put(playerUuid, kitList);
        }
    }

    public void loadDefaults(String kitName, String nickName, Kit kit, int slot, ItemStack[] stacks) {
        kit.setType(kitName);
        kit.setName("Kit " + slot);
        kit.setSlot(slot);

        kit.setInventoryContents(stacks);
    }

    public void unloadKits(Player player) {
        kitData.remove(player.getUniqueId());
    }
}