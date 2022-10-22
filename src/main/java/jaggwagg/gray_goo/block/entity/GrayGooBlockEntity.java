package jaggwagg.gray_goo.block.entity;

import jaggwagg.gray_goo.block.GrayGooBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class GrayGooBlockEntity extends BlockEntity {
    private int age = 0;
    private int growthSize = 2;
    private NbtCompound traits = new NbtCompound();

    public static ArrayList<String> traitKeys = new ArrayList<>(List.of(
            "biological", "broken", "corrupted", "explosive", "fluid",
            "gravitational", "linear", "rapid", "solid", "tainted"
    ));

    public GrayGooBlockEntity(BlockPos pos, BlockState state) {
        super(GrayGooBlocks.GRAY_GOO_BLOCK_ENTITY, pos, state);

        traitKeys.forEach(key -> this.traits.putBoolean("key", false));
    }

    public int getGrowthSize() {
        return this.growthSize;
    }

    public boolean getTrait(String key) {
        return this.traits.getBoolean(key);
    }

    public NbtCompound getAllTraits() {
        return this.traits;
    }

    public void setAllTraits(NbtCompound traits) {
        this.traits = traits;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        age = tag.getInt("age");
        growthSize = tag.getInt("growthSize");
        traits = tag.getCompound("traits");
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        tag.putInt("age", age);
        tag.putInt("growthSize", growthSize);
        tag.put("traits", traits);

        super.writeNbt(tag);
    }
}
