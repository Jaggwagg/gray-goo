package jaggwagg.gray_goo.block.entity;

import jaggwagg.gray_goo.block.GrayGooBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class GrayGooBlockEntity extends BlockEntity {
    private int age = 0;
    private int growthSize = 2;
    private Map<String, Boolean> traits = new HashMap<>();
    public static ArrayList<String> traitKeys = new ArrayList<>(List.of(
            "biological", "broken", "corrupted", "explosive", "fluid",
            "linear", "rapid", "selfdestruct", "solid", "tainted"
    ));

    public GrayGooBlockEntity(BlockPos pos, BlockState state) {
        super(GrayGooBlocks.GRAY_GOO_BLOCK_ENTITY, pos, state);

        this.resetAllTraits();
    }

    public int getGrowthSize() {
        return this.growthSize;
    }

    public boolean getTrait(String key) {
        return this.traits.get(key);
    }

    public Map<String, Boolean> getAllTraits() {
        return this.traits;
    }

    public void setAllTraits(Map<String, Boolean> traits) {
        this.traits = traits;
    }

    public void resetAllTraits() {
        traitKeys.forEach(key -> this.traits.put(key, false));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        age = nbt.getInt("age");
        growthSize = nbt.getInt("growthSize");

        traitKeys.forEach(key -> this.traits.put(key, nbt.getBoolean(key)));
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("age", age);
        nbt.putInt("growthSize", growthSize);
        traits.forEach(nbt::putBoolean);

        super.writeNbt(nbt);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
