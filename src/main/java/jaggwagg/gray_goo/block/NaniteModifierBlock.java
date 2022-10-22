package jaggwagg.gray_goo.block;

import jaggwagg.gray_goo.GrayGoo;
import jaggwagg.gray_goo.screen.NaniteModifierScreenHandler;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NaniteModifierBlock extends Block {
    private static final Text TITLE = Text.translatable("container." + GrayGoo.MOD_ID + ".nanite_modifier");

    public NaniteModifierBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecated")
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            player.openHandledScreen(createScreenHandlerFactory(state, world, pos));
            return ActionResult.CONSUME;
        }

        return ActionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("deprecated")
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((syncId, inventory, player) -> new NaniteModifierScreenHandler(syncId, inventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }

    @Override
    @SuppressWarnings("deprecated")
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}
