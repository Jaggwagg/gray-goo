package jaggwagg.gray_goo.block;

import jaggwagg.gray_goo.block.entity.GrayGooBlockEntity;
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
            world.setBlockState(pos, state.with(TRIGGERED, true), 4);

            int posX = pos.getX();
            int posY = pos.getY();
            int posZ = pos.getZ();

            int breadth = 64;

            for (int x = posX - breadth; x < posX + breadth; x++) {
                for (int y = posY - breadth; y < posY + breadth; y++) {
                    for (int z = posZ - breadth; z < posZ + breadth; z++) {
                        BlockPos blockPos = new BlockPos(x, y, z);
                        if (world.getBlockState(blockPos).isOf(GrayGooBlocks.Blocks.GRAY_GOO.block)) {
                            BlockEntity blockEntity = world.getBlockEntity(blockPos);
                            if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity) {
                                Map<String, Boolean> traits = new HashMap<>();
                                GrayGooBlockEntity.traitKeys.forEach(key -> traits.put(key, false));
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
