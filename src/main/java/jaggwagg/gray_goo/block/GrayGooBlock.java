package jaggwagg.gray_goo.block;

import jaggwagg.gray_goo.GrayGoo;
import jaggwagg.gray_goo.block.entity.GrayGooBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GrayGooBlock extends Block implements BlockEntityProvider {
    static BooleanProperty ACTIVATED = BooleanProperty.of("activated");
    public GrayGooBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(ACTIVATED, false));
    }

    public void grow(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ArrayList<BlockPos> positions = this.getGrowableBlocks(world, pos);

        if (!GrayGoo.CONFIG.getAllowGooSpread()) {
            return;
        }

        if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity) {
            HashSet<Block> blocks = new HashSet<>();

            if (grayGooBlockEntity.getTrait("biological")) {
                GrayGoo.CONFIG.getBiologicalBlocks().forEach(value -> blocks.add(Registry.BLOCK.get(new Identifier(value))));
            }

            if (grayGooBlockEntity.getTrait("fluid")) {
                GrayGoo.CONFIG.getFluidBlocks().forEach(value -> {
                    System.out.println(value);
                    blocks.add(Registry.BLOCK.get(new Identifier(value)));
                });
            }

            if (grayGooBlockEntity.getTrait("solid")) {
                GrayGoo.CONFIG.getSolidBlocks().forEach(value -> blocks.add(Registry.BLOCK.get(new Identifier(value))));
            }

            if (grayGooBlockEntity.getTrait("selfdestruct")) {
                blocks.add(GrayGooBlocks.Blocks.GRAY_GOO.block);
            }

            if (grayGooBlockEntity.getTrait("linear")) {
                ArrayList<BlockPos> growableBlocks = new ArrayList<>(List.of(
                        pos.south(), pos.north(), pos.west(), pos.east(), pos.up(), pos.down()
                ));

                for (int i = 0; i < growableBlocks.size(); i++) {
                    if (world.getBlockState(growableBlocks.get(i)).isOf(this)) {
                        int index = ((i & 1) == 0) ? i + 1 : i - 1;

                        if (blocks.contains(world.getBlockState(growableBlocks.get(index)).getBlock())) {
                            world.setBlockState(growableBlocks.get(index), Blocks.AIR.getDefaultState());
                            world.setBlockState(growableBlocks.get(index), this.getDefaultState().with(ACTIVATED, true));
                            world.setBlockState(pos, this.getDefaultState().with(ACTIVATED, true));

                            BlockEntity newBlockEntity = world.getBlockEntity(growableBlocks.get(index));

                            if (newBlockEntity instanceof GrayGooBlockEntity newGrayGooBlockEntity) {
                                newGrayGooBlockEntity.setAllTraits(grayGooBlockEntity.getAllTraits());
                            }
                        }
                    }
                }
            } else {
                for (BlockPos blockPos : positions) {
                    if (blocks.contains(world.getBlockState(blockPos).getBlock())) {
                        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
                        world.setBlockState(blockPos, this.getDefaultState().with(ACTIVATED, true));
                        world.setBlockState(pos, this.getDefaultState().with(ACTIVATED, true));

                        BlockEntity newBlockEntity = world.getBlockEntity(blockPos);

                        if (newBlockEntity instanceof GrayGooBlockEntity newGrayGooBlockEntity) {
                            newGrayGooBlockEntity.setAllTraits(grayGooBlockEntity.getAllTraits());
                        }
                    }
                }
            }

            // Corrupted trait section
            if (grayGooBlockEntity.getTrait("corrupted")) {
                ArrayList<Block> randomBlocks = new ArrayList<>(List.of(
                        Blocks.GRASS_BLOCK, Blocks.STONE, Blocks.DIRT, Blocks.DEEPSLATE, Blocks.BLACKSTONE, Blocks.NETHERRACK, Blocks.BASALT,
                        Blocks.WATER, Blocks.LAVA
                ));
                java.util.Random random = new java.util.Random();
                int randomInt = random.nextInt(50);

                if (randomInt < randomBlocks.size()) {
                    world.setBlockState(pos, randomBlocks.get(randomInt).getDefaultState());
                }
            }
        }
    }

    public ArrayList<BlockPos> getGrowableBlocks(World world, BlockPos pos) {
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        ArrayList<BlockPos> positions = new ArrayList<>();
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity) {
            int growthSize = grayGooBlockEntity.getGrowthSize();

            for (int x = posX - growthSize; x <= posX + growthSize; x++) {
                for (int y = posY - growthSize; y <= posY + growthSize; y++) {
                    for (int z = posZ - growthSize; z <= posZ + growthSize; z++) {
                        double distance = (posX - x) * (posX - x) + ((posZ - z) * (posZ - z)) + ((posY - y) * (posY - y));

                        if (distance < growthSize) {
                            positions.add(new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }

        return positions;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.grow(world, pos);

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity)  {
            if (grayGooBlockEntity.getTrait("broken")) {
                if (world.getBlockState(pos).get(ACTIVATED)) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }
            }

            if (grayGooBlockEntity.getTrait("explosive")) {
                int randomExplosionChance = random.nextInt(50);

                if (randomExplosionChance == 0) {
                    world.createExplosion(null, DamageSource.GENERIC, null, pos.getX(), pos.getY(), pos.getZ(), 2.0f, true, Explosion.DestructionType.DESTROY);
                }
            }
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GrayGooBlockEntity(pos, state);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity) {
            if (grayGooBlockEntity.getTrait("tainted")) {
                this.grow(world, pos);
                entity.damage(DamageSource.CACTUS, 2);

                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(Objects.requireNonNull(StatusEffect.byRawId(17)), 200));
                    livingEntity.addStatusEffect(new StatusEffectInstance(Objects.requireNonNull(StatusEffect.byRawId(19)), 200));
                }
            }
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            this.grow(world, pos);

            return ActionResult.CONSUME;
        }
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity) {
            if (!world.isClient && !player.isCreative() && !world.getBlockState(pos).get(ACTIVATED)) {
                ItemStack itemStack = new ItemStack(this);
                blockEntity.setStackNbt(itemStack);

                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            }

            if (grayGooBlockEntity.getTrait("tainted")) {
                player.addStatusEffect(new StatusEffectInstance(Objects.requireNonNull(StatusEffect.byRawId(17)), 200));
                player.addStatusEffect(new StatusEffectInstance(Objects.requireNonNull(StatusEffect.byRawId(19)), 200));
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient()) {
            java.util.Random random = new java.util.Random();

            if (Objects.requireNonNull(world.getServer()).getGameRules().getInt(GameRules.RANDOM_TICK_SPEED) != 0) {
                BlockEntity blockEntity = world.getBlockEntity(pos);

                if (blockEntity instanceof GrayGooBlockEntity grayGooBlockEntity) {
                    if (grayGooBlockEntity.getTrait("rapid")) {
                        world.createAndScheduleBlockTick(pos, this, (10 + random.nextInt(50)) / Objects.requireNonNull(world.getServer()).getGameRules().getInt(GameRules.RANDOM_TICK_SPEED));
                    }
                }
            }
        }

        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.grow(world, pos);
    }
}
