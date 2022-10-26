package jaggwagg.gray_goo.block;

import jaggwagg.gray_goo.GrayGoo;
import jaggwagg.gray_goo.block.entity.GrayGooBlockEntity;
import jaggwagg.gray_goo.item.GrayGooItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class EMPSwitchBlock extends Block {
    public static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");

    public EMPSwitchBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(TRIGGERED, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());
        boolean triggered = state.get(TRIGGERED);

        if (powered && !triggered) {
            int posX = pos.getX();
            int posY = pos.getY();
            int posZ = pos.getZ();
            int length = GrayGoo.CONFIG.getEmpRadius();

            world.setBlockState(pos, state.with(TRIGGERED, true), 4);

            for (int x = posX - length; x < posX + length; x++) {
                for (int y = posY - length; y < posY + length; y++) {
                    for (int z = posZ - length; z < posZ + length; z++) {
                        BlockPos blockPos = new BlockPos(x, y, z);
                        if (world.getBlockState(blockPos).isOf(GrayGooBlocks.Blocks.GRAY_GOO.block)) {
                            BlockEntity blockEntity = world.getBlockEntity(blockPos);
                            if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity) {
                                Map<String, Boolean> traits = new HashMap<>();
                                GrayGooItems.TRAIT_KEYS.forEach(key -> traits.put(key, false));
                                traits.put("broken", true);
                                grayGooBlockEntity.setAllTraits(traits);
                            }
                        }
                    }
                }
            }
        } else if (!powered && triggered) {
            world.setBlockState(pos, state.with(TRIGGERED, false), 4);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRIGGERED);
    }
}
