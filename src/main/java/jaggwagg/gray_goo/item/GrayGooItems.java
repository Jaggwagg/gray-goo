package jaggwagg.gray_goo.item;

import jaggwagg.gray_goo.GrayGoo;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Locale;

public class GrayGooItems {
    public static void init() {
        Arrays.stream(Items.values()).forEach(value -> registerItem(value.item, value.name));
        Arrays.stream(Traits.values()).forEach(value -> registerItem(value.item, value.name));
    }

    public enum Items {
        MOLECULAR_LOGIC_GATE(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP))),
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
        BIOLOGICAL_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xddffdd),
        BROKEN_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xdddddd),
        CORRUPTED_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xcccccc),
        EXPLOSIVE_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xffdddd),
        FLUID_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xddddff),
        LINEAR_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xffeecc),
        RAPID_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xffcccc),
        SELFDESTRUCT_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xffcccc),
        SOLID_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xffeedd),
        TAINTED_NANITE_TRAIT(new Item(new FabricItemSettings().group(GrayGoo.ITEM_GROUP)), 0xffddff);

        public final String name;
        public final Item item;
        public final Integer colorHex;

        <T extends Item> Traits(T item, Integer colorHex) {
            this.name = this.toString().toLowerCase(Locale.ROOT);
            this.item = item;
            this.colorHex = colorHex;
        }
    }

    public static void registerItem(Item item, String name) {
        Registry.register(Registry.ITEM, new Identifier(GrayGoo.MOD_ID, name), item);
    }
}
