package jaggwagg.gray_goo.screen;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;

import jaggwagg.gray_goo.block.GrayGooBlocks;
import jaggwagg.gray_goo.item.GrayGooItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;

public class NaniteModifierScreenHandler extends ScreenHandler {
    private final ScreenHandlerContext context;
    private final List<Item> availableTraits;
    final Slot grayGooBlockInputSlot;
    final Slot naniteTraitInputSlot;
    final Slot outputSlot;
    Runnable contentsChangedListener;
    public final Inventory input;
    final CraftingResultInventory output;

    public NaniteModifierScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public NaniteModifierScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(GrayGooScreenHandlers.NANITE_ASSEMBLER, syncId);
        this.availableTraits = Lists.newArrayList();
        this.contentsChangedListener = () -> {
        };
        this.input = new SimpleInventory(2) {
            public void markDirty() {
                super.markDirty();
                NaniteModifierScreenHandler.this.onContentChanged(this);
                NaniteModifierScreenHandler.this.contentsChangedListener.run();
            }
        };
        this.output = new CraftingResultInventory();
        this.context = context;
        this.grayGooBlockInputSlot = this.addSlot(new Slot(this.input, 0, 20, 20) {
            public boolean canInsert(ItemStack stack) {
                return matches(stack);
            }

            public static boolean matches(ItemStack stack) {
                return stack.isOf(GrayGooBlocks.Blocks.GRAY_GOO.block.asItem());
            }
        });
        this.naniteTraitInputSlot = this.addSlot(new Slot(this.input, 1, 20, 48) {
            public boolean canInsert(ItemStack stack) {
                return matches(stack);
            }

            public static boolean matches(ItemStack stack) {
                for (GrayGooItems.Traits trait : GrayGooItems.Traits.values()) {
                    if (stack.isOf(trait.item)) {
                        return true;
                    }
                }

                return false;
            }
        });
        this.outputSlot = this.addSlot(new Slot(this.output, 1, 143, 33) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return true;
            }

            @Override
            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                NaniteModifierScreenHandler.this.onTakeOutput();
            }
        });

        for (int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    protected void onTakeOutput() {
        this.input.getStack(0).decrement(1);
        this.input.getStack(1).decrement(1);
        this.output.getStack(0).decrement(1);
        updateResult();
    }

    public List<Item> getAvailableTraits() {
        return this.availableTraits;
    }

    public int getAvailableRecipeCount() {
        return this.availableTraits.size();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, GrayGooBlocks.Blocks.NANITE_MODIFIER.block);
    }

    private void updateResult() {
        this.availableTraits.clear();
        this.outputSlot.setStack(ItemStack.EMPTY);

        if (this.input.getStack(0).isEmpty() || this.input.getStack(0).isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
        }

        if (this.input.getStack(0).isOf(GrayGooBlocks.Blocks.GRAY_GOO.block.asItem())) {
            NbtCompound compound = this.input.getStack(0).getSubNbt("BlockEntityTag");
            NbtCompound traits;

            if (compound == null) {
                NbtCompound newCompound = new NbtCompound();

                newCompound.put("BlockEntityTag", new NbtCompound());

                ItemStack outputStack = new ItemStack(GrayGooBlocks.Blocks.GRAY_GOO.block);
                outputStack.setNbt(newCompound);
                this.input.setStack(0, outputStack);
            } else {
                traits = compound.getCompound("traits");
                Set<String> set = traits.getKeys();

                for (GrayGooItems.Traits trait : GrayGooItems.Traits.values()) {
                    String string = trait.toString().toLowerCase();
                    int end = string.indexOf("_");
                    String traitString = string.substring(0, end);

                    if (set.contains(traitString)) {
                        this.availableTraits.add(trait.item);
                    }

                    if (this.input.getStack(1).isOf(trait.item)) {
                        if (!traits.getBoolean(traitString)) {
                            ItemStack outputStack = new ItemStack(GrayGooBlocks.Blocks.GRAY_GOO.block);
                            NbtCompound newCompound = new NbtCompound();
                            NbtCompound newTraits = new NbtCompound();
                            newCompound.copyFrom(compound);
                            newTraits.copyFrom(traits);
                            newTraits.putBoolean(traitString, true);
                            newCompound.put("traits", newTraits);
                            outputStack.setSubNbt("BlockEntityTag", newCompound);
                            this.output.setStack(0, outputStack);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return GrayGooScreenHandlers.NANITE_ASSEMBLER;
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (index == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);
            } else if (index != 0 && index != 1) {
                if (index >= 3 && index < 39) {
                    int i = 0;
                    if (!this.insertItem(itemStack2, i, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStack2, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (inventory == this.input) {
            this.updateResult();
        }
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        this.output.removeStack(1);
        this.context.run((world, pos) -> this.dropInventory(player, this.input));
    }
}
