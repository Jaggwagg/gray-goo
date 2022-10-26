package jaggwagg.gray_goo.client;

import jaggwagg.gray_goo.block.GrayGooBlocks;
import jaggwagg.gray_goo.block.entity.GrayGooBlockEntity;
import jaggwagg.gray_goo.item.GrayGooItems;
import jaggwagg.gray_goo.screen.GrayGooScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

@Environment(EnvType.CLIENT)
public class GrayGooClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        GrayGooScreenHandlers.initClient();
        registerColorProviders();
    }

    private static void registerColorProviders() {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            int color = 0xffffff;

            if (world != null) {
                BlockEntity blockEntity = world.getBlockEntity(pos);

                if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity) {
                    NbtCompound nbt = grayGooBlockEntity.createNbt();

                    for (GrayGooItems.Traits trait : GrayGooItems.Traits.values()) {
                        String string = trait.toString().toLowerCase();
                        int end = string.indexOf("_");
                        String traitString = string.substring(0, end);

                        if (nbt.getBoolean(traitString)) {
                            color += trait.color;
                        }
                    }
                }
            }

            return color;
        }, GrayGooBlocks.Blocks.GRAY_GOO.block);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            Integer color = 0xffffff;
            NbtCompound nbt = stack.getSubNbt("BlockEntityTag");

            if (nbt != null) {
                for (GrayGooItems.Traits trait : GrayGooItems.Traits.values()) {
                    String string = trait.toString().toLowerCase();
                    int end = string.indexOf("_");
                    String traitString = string.substring(0, end);

                    if (nbt.getBoolean(traitString)) {
                        color += trait.color;
                    }
                }
            }

            return color;
        }, GrayGooBlocks.Blocks.GRAY_GOO.block.asItem());
    }
}
