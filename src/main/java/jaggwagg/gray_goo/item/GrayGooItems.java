package jaggwagg.gray_goo.item;

import jaggwagg.gray_goo.GrayGoo;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Locale;

public class GrayGooItems {
    // Nanites
    // Molecular machine parts
        // Molecular motor
        // Molecular switch
        // Molecular logic gate
        // Molecular replicator

        // Molecular propeller (fluid)

    public static void init() {
        Arrays.stream(Items.values()).forEach(value -> registerItem(value.item, value.name));
        Arrays.stream(Traits.values()).forEach(value -> registerItem(value.item, value.name));
    }

    public enum Items {
        // Molecular items
        MOLECULAR_LOGIC_GATE(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        MOLECULAR_MOTOR(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        MOLECULAR_PROPELLER(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        MOLECULAR_REPLICATOR(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        MOLECULAR_SWITCH(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)));

        public final String name;
        public final Item item;

        <T extends Item> Items(T item) {
            this.name = this.toString().toLowerCase(Locale.ROOT);
            this.item = item;
        }
    }

    public enum Traits {
        BIOLOGICAL_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        BROKEN_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        CORRUPTED_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        EXPLOSIVE_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        FLUID_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        GRAVITATIONAL_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        LINEAR_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        RAPID_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        SOLID_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
        TAINTED_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)));

        public final String name;
        public final Item item;

        <T extends Item> Traits(T item) {
            this.name = this.toString().toLowerCase(Locale.ROOT);
            this.item = item;
        }
    }

    public static void registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, new Identifier(GrayGoo.MOD_ID, name), item);
    }
}
