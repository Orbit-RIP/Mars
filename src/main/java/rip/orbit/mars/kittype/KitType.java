package rip.orbit.mars.kittype;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.enchantments.Enchantment;
import rip.orbit.mars.Mars;
import rip.orbit.mars.util.MongoUtils;
import cc.fyre.proton.Proton;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.libs.com.google.gson.annotations.SerializedName;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Denotes a type of Kit, under which players can queue, edit kits,
 * have elo, etc.
 */
// This class purposely uses qLib Gson (as we want to actualy serialize
// the fields within a KitType instead of pretending it's an enum) instead of ours.
public final class KitType {

    private static final String MONGO_COLLECTION_NAME = "kitTypes";
    @Getter private static final List<KitType> allTypes = new ArrayList<>();

    public static KitType teamFight = new KitType();
    public static KitType nodebuff = new KitType();
    public static KitType debuff = new KitType();
    public static KitType archer = new KitType();
    public static KitType combo = new KitType();
    public static KitType builduchc = new KitType();
    public static KitType sumo = new KitType();
    public static KitType spleef = new KitType();

    public static KitType TRAPPER_VIPER = new KitType();
    public static KitType TRAPPER_CAVE = new KitType();
    public static KitType TRAPPER_ORBIT = new KitType();
    public static KitType TRAPPER_NORMAL = new KitType();

    public static KitType baseraiding = new KitType();
    public static KitType orbitBaseRaiding = new KitType();
    public static KitType caveBaseRaiding = new KitType();
    public static KitType viperBaseRaiding = new KitType();
    public static KitType hcf = new KitType();
    public static KitType hcfbard = new KitType();
    public static KitType hcfarcher = new KitType();
    public static KitType hcfdiamond = new KitType();
    public static KitType soup = new KitType();
    public static KitType boxing = new KitType();
    public static KitType skywars = new KitType();
//    public static KitType wizard = new KitType();
    public static KitType bridges = new KitType();
    public static KitType pearlFight = new KitType();

    static {
        MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);

        collection.find().iterator().forEachRemaining(doc -> {
            allTypes.add(Proton.PLAIN_GSON.fromJson(doc.toJson(), KitType.class));
        });

        teamFight.icon = new MaterialData(Material.BEACON);
        teamFight.id = "TeamFight";
        teamFight.displayName = "HCF Team Fight";
        teamFight.displayColor = ChatColor.GOLD;

        nodebuff.icon = new MaterialData(Material.DIAMOND_SWORD);
        nodebuff.id = "NoDebuff";
        nodebuff.displayName = "No Debuff";
        nodebuff.displayColor = ChatColor.GOLD;

        debuff.icon = new MaterialData(Material.POTION);
        debuff.id = "Debuff";
        debuff.displayName = "Debuff";
        debuff.displayColor = ChatColor.GOLD;

        pearlFight.icon = new MaterialData(Material.ENDER_PEARL);
        pearlFight.id = "PearlFight";
        pearlFight.displayName = "PearlFight";
        pearlFight.displayColor = ChatColor.GOLD;

        archer.icon = new MaterialData(Material.BOW);
        archer.id = "Archer";
        archer.displayName = "Archer";
        archer.displayColor = ChatColor.GOLD;

        combo.icon = new MaterialData(Material.RED_ROSE);
        combo.id = "Combo";
        combo.displayName = "Combo";
        combo.displayColor = ChatColor.GOLD;

        builduchc.icon = new MaterialData(Material.GOLDEN_APPLE);
        builduchc.id = "BuildUHC";
        builduchc.displayName = "Build UHC";
        builduchc.displayColor = ChatColor.GOLD;

        TRAPPER_CAVE.icon = new MaterialData(Material.INK_SACK, (byte) 12);
        TRAPPER_CAVE.displayColor = ChatColor.DARK_RED;
        TRAPPER_CAVE.displayName = "Cave Trapper";
        TRAPPER_CAVE.id = "Cave-Trapper";
        TRAPPER_CAVE.hidden = true;

        TRAPPER_ORBIT.icon = new MaterialData(Material.INK_SACK, (byte) 12);
        TRAPPER_ORBIT.displayColor = ChatColor.GOLD;
        TRAPPER_ORBIT.displayName = "Orbit Trapper";
        TRAPPER_ORBIT.id = "Orbit-Trapper";
        TRAPPER_ORBIT.hidden = true;

        TRAPPER_VIPER.icon = new MaterialData(Material.INK_SACK, (byte) 12);
        TRAPPER_VIPER.displayColor = ChatColor.DARK_PURPLE;
        TRAPPER_VIPER.displayName = "Viper Trapper";
        TRAPPER_VIPER.id = "Viper-Trapper";
        TRAPPER_VIPER.hidden = true;

        TRAPPER_NORMAL.icon = new MaterialData(Material.INK_SACK, (byte) 12);
        TRAPPER_NORMAL.displayColor = ChatColor.GOLD;
        TRAPPER_NORMAL.displayName = "Normal Trapper";
        TRAPPER_NORMAL.id = "Normal-Trapper";
        TRAPPER_NORMAL.hidden = true;

        sumo.icon = new MaterialData(Material.LEASH);
        sumo.id = "Sumo";
        sumo.displayName = "Sumo";
        sumo.displayColor = ChatColor.GOLD;

        baseraiding.icon = new MaterialData(Material.BLAZE_POWDER);
        baseraiding.id = "BaseRaiding";
        baseraiding.displayName = "Base Raiding";
        baseraiding.displayColor = ChatColor.GOLD;

        viperBaseRaiding.icon = new MaterialData(Material.SPIDER_EYE);
        viperBaseRaiding.id = "Viper-BaseRaiding";
        viperBaseRaiding.displayName = "Viper BaseRaiding";
        viperBaseRaiding.displayColor = ChatColor.GOLD;

        caveBaseRaiding.icon = new MaterialData(Material.INK_SACK, (byte) 1);
        caveBaseRaiding.id = "Cave-BaseRaiding";
        caveBaseRaiding.displayName = "Cave BaseRaiding";
        caveBaseRaiding.displayColor = ChatColor.DARK_RED;

        orbitBaseRaiding.icon = new MaterialData(Material.INK_SACK, (byte) 14);
        orbitBaseRaiding.id = "Orbit-BaseRaiding";
        orbitBaseRaiding.displayName = "Orbit BaseRaiding";
        orbitBaseRaiding.displayColor = ChatColor.GOLD;

        hcf.icon = new MaterialData(Material.DIAMOND_HELMET);
        hcf.id = "HCF";
        hcf.displayName = "HCF";
        hcf.displayColor = ChatColor.GOLD;

        spleef.icon = new MaterialData(Material.DIAMOND_SPADE);
        spleef.id = "Spleef";
        spleef.displayName = "Spleef";
        spleef.displayColor = ChatColor.GOLD;

        hcfdiamond.icon = new MaterialData(Material.DIAMOND_CHESTPLATE);
        hcfdiamond.id = "DIAMOND_HCF";
        hcfdiamond.displayName = "Diamond Class";
        hcfdiamond.displayColor = ChatColor.GOLD;

        hcfarcher.icon = new MaterialData(Material.LEATHER_CHESTPLATE);
        hcfarcher.id = "ARCHER_HCF";
        hcfarcher.displayName = "Archer Class";
        hcfarcher.displayColor = ChatColor.GOLD;

        hcfbard.icon = new MaterialData(Material.GOLD_CHESTPLATE);
        hcfbard.id = "BARD_HCF";
        hcfbard.displayName = "Bard Class";
        hcfbard.displayColor = ChatColor.GOLD;

        boxing.icon = new MaterialData(Material.STICK);
        boxing.id = "Boxing";
        boxing.displayName = "Boxing";
        boxing.displayColor = ChatColor.GOLD;

        skywars.icon = new MaterialData(Material.GRASS);
        skywars.id = "SkyWars";
        skywars.displayName = "SkyWars";
        skywars.displayColor = ChatColor.GOLD;

        bridges.icon = new MaterialData(Material.STAINED_CLAY);
        bridges.id = "Bridges";
        bridges.displayName = "Bridges";
        bridges.displayColor = ChatColor.GOLD;

        MaterialData data = new MaterialData(Material.MUSHROOM_SOUP);
        data.toItemStack().addUnsafeEnchantment(Enchantment.DURABILITY, 10);
        soup.icon = data;
        soup.id = "Soup";
        soup.displayName = "Soup";
        soup.displayColor = ChatColor.GOLD;
        soup.setHealingMethod(HealingMethod.SOUP);

//        wizard.icon = new MaterialData(Material.EYE_OF_ENDER);
//        wizard.id = "WIZARD";
//        wizard.displayName = "Wizard";
//        wizard.displayColor = ChatColor.GOLD;
//
//        if (!allTypes.contains(byId(wizard.id))) {
//            allTypes.add(wizard);
//        }
        if (!allTypes.contains(byId(builduchc.id))) {
            allTypes.add(builduchc);
        }
        if (!allTypes.contains(byId(hcf.id))) {
            allTypes.add(hcf);
        }
        if (!allTypes.contains(byId(pearlFight.id))) {
            allTypes.add(pearlFight);
        }
        if (!allTypes.contains(byId(TRAPPER_NORMAL.id))) {
            allTypes.add(TRAPPER_NORMAL);
        }
        if (!allTypes.contains(byId(TRAPPER_CAVE.id))) {
            allTypes.add(TRAPPER_CAVE);
        }
        if (!allTypes.contains(byId(TRAPPER_ORBIT.id))) {
            allTypes.add(TRAPPER_ORBIT);
        }
        if (!allTypes.contains(byId(TRAPPER_VIPER.id))) {
            allTypes.add(TRAPPER_VIPER);
        }
        if (!allTypes.contains(byId(spleef.id))) {
            allTypes.add(spleef);
        }

        if (!allTypes.contains(byId(combo.id))) {
            allTypes.add(combo);
        }

        if (!allTypes.contains(byId(sumo.id))) {
            allTypes.add(sumo);
        }

        if (!allTypes.contains(byId(nodebuff.id))) {
            allTypes.add(nodebuff);
        }

        if (!allTypes.contains(byId(debuff.id))) {
            allTypes.add(debuff);
        }

        if (!allTypes.contains(byId(baseraiding.id))) {
            allTypes.add(baseraiding);
        }

        if (!allTypes.contains(byId(orbitBaseRaiding.id))) {
            allTypes.add(orbitBaseRaiding);
        }

        if (!allTypes.contains(byId(viperBaseRaiding.id))) {
            allTypes.add(viperBaseRaiding);
        }

        if (!allTypes.contains(byId(caveBaseRaiding.id))) {
            allTypes.add(caveBaseRaiding);
        }

        if (!allTypes.contains(byId(archer.id))) {
            allTypes.add(archer);
        }

        if (!allTypes.contains(byId("DIAMOND_HCF"))) {
            allTypes.add(hcfdiamond);
        }

        if (!allTypes.contains(byId("BARD_HCF"))) {
            allTypes.add(hcfbard);
        }

        if (!allTypes.contains(byId("ARCHER_HCF"))) {
            allTypes.add(hcfarcher);
        }

        if (!allTypes.contains(byId(boxing.id))) {
            allTypes.add(boxing);
        }

        if (!allTypes.contains(byId(soup.id))) {
            allTypes.add(soup);
        }

        if (!allTypes.contains(byId(skywars.id))) {
            allTypes.add(skywars);
        }

        if (!allTypes.contains(byId(bridges.id))) {
            allTypes.add(bridges);
        }
        allTypes.sort(Comparator.comparing(KitType::getSort));
    }

    /**
     * Id of this KitType, will be used when serializing the KitType for
     * database storage. Ex: "WIZARD", "NO_ENCHANTS", "SOUP"
     */
    @Getter @Setter @SerializedName("_id") private String id;

    /**
     * Display name of this KitType, will be used when communicating a KitType
     * to playerrs. Ex: "Wizard", "No Enchants", "Soup"
     */
    @Setter private String displayName;

    /**
     * Display color for this KitType, will be used in messages
     * or scoreboards sent to players.
     */
    @Getter @Setter private ChatColor displayColor;

    /**
     * Material info which will be used when rendering this
     * kit in selection menus and such.
     */
    @Setter private MaterialData icon;

    /**
     * Items which will be available for players to grab in the kit
     * editor, when making kits for this kit type.
     */
    @Getter @Setter private ItemStack[] editorItems = new ItemStack[0];

    /**
     * The armor that will be applied to players for this kit type.
     * Currently players are not allowed to edit their armor, they are
     * always given this armor.
     */
    @Setter private ItemStack[] defaultArmor = new ItemStack[0];

    /**
     * The default inventory that will be applied to players for this kit type.
     * Players are always allowed to rearange this inventory, so this only serves
     * as a default (in contrast to defaultArmor)
     */
    @Setter private ItemStack[] defaultInventory = new ItemStack[0];

    /**
     * Determines if players are allowed to spawn in items while editing their kits.
     * For some kit types (ex archer and axe) players can only rearange items in kits,
     * whereas some kit types (ex HCTeams and soup) allow spawning in items as well.
     */
    @Getter @Setter private boolean editorSpawnAllowed = true;

    /**
     * Determines if normal, non-admin players should be able to see this KitType.
     */
    @Getter @Setter private boolean hidden = false;

    /**
     * Determines how players regain health in matches using this KitType.
     * This is used primarily for applying logic for souping + rendering
     * heals remaining in the post match inventory
     */
    @Getter @Setter private HealingMethod healingMethod = HealingMethod.POTIONS;

    /**
     * Determines if players are allowed to build in matches using this KitType.
     */
    @Getter @Setter private boolean buildingAllowed = false;

    /**
     * Determines if health is shown below the player's name-tags in matches using this KitType.
     */
    @Getter @Setter private boolean healthShown = false;

    /**
     * Determines if natural health regeneration should happen in matches using this KitType.
     */
    @Getter @Setter private boolean hardcoreHealing = false;

    /**
     * Determines if players playing a match using this KitType should take damage when their ender pearl lands.
     */
    @Getter @Setter private boolean pearlDamage = true;

    /**
     * Determines the order used when displaying lists of KitTypes to players.
     * (Lowest to highest)
     */
    @Getter @Setter private int sort = 0;

    @Getter @Setter private boolean supportsRanked = false;

    public static KitType byId(String id) {
        for (KitType kitType : allTypes) {
            if (kitType.getId().equalsIgnoreCase(id)) {
                return kitType;
            }
        }
        return null;
    }

    public void deleteAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(Mars.getInstance(), () -> {
            MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
            collection.deleteOne(new Document("_id", id));
        });
    }

    public String getColoredDisplayName() {
        return displayColor + displayName;
    }

    public void saveAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(Mars.getInstance(), () -> {
            MongoCollection<Document> collection = MongoUtils.getCollection(MONGO_COLLECTION_NAME);
            Document kitTypeDoc = Document.parse(Proton.PLAIN_GSON.toJson(this));
            kitTypeDoc.remove("_id"); // upserts with an _id field is weird.

            Document query = new Document("_id", id);
            Document kitUpdate = new Document("$set", kitTypeDoc);

            collection.updateOne(query, kitUpdate, MongoUtils.UPSERT_OPTIONS);
        });
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MaterialData getIcon() {
        return icon;
    }

    public ItemStack[] getDefaultArmor() {
        return defaultArmor;
    }

    public ItemStack[] getDefaultInventory() {
        return defaultInventory;
    }

}