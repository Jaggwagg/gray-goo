package jaggwagg.gray_goo.block;

import jaggwagg.gray_goo.GrayGoo;
import jaggwagg.gray_goo.block.entity.GrayGooBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.Locale;

public class GrayGooBlocks {
    public static BlockEntityType<GrayGooBlockEntity> GRAY_GOO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(GrayGoo.MOD_ID, "gray_goo_block_entity"), FabricBlockEntityTypeBuilder.create(GrayGooBlockEntity::new, Blocks.GRAY_GOO.block).build(null));

    public static void init() {
        FabricItemSettings gooSettings = new FabricItemSettings().group(GrayGoo.ITEM_GROUP);
        Arrays.stream(Blocks.values()).forEach(value -> registerBlock(value.block, value.name, gooSettings));
    }

    public enum Blocks {
        EMP_SWITCH(new EMPSwitchBlock(FabricBlockSettings.of(Material.METAL).strength(2.0f).requiresTool())),
        GRAY_GOO(new GrayGooBlock(FabricBlockSettings.of(Material.METAL).ticksRandomly().breakInstantly())),
        NANITE_MODIFIER(new NaniteModifierBlock(FabricBlockSettings.of(Material.METAL).strength(2.0f).requiresTool()));

        public final String name;
        public final Block block;

        <T extends Block> Blocks(T block) {
            this.name = this.toString().toLowerCase(Locale.ROOT);
            this.block = block;
        }
    }

    public static void registerBlock(Block block, String name, FabricItemSettings settings) {
        Registry.register(Registry.BLOCK, new Identifier(GrayGoo.MOD_ID, name), block);
        Registry.register(Registry.ITEM, new Identifier(GrayGoo.MOD_ID, name), new BlockItem(block, settings));
    }
}
