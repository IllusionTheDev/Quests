package me.pikamug.quests.util.stack.impl;

import me.pikamug.quests.util.stack.BlockItemStack;
import me.pikamug.quests.util.stack.BlockItemStackFactory;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;

public class ModernBlockItemStack implements BlockItemStack {

    public static final BlockItemStackFactory FACTORY = new Factory();

    private final BlockData blockData;
    private int amount;
    private final short durability;

    private ModernBlockItemStack(final BlockData blockData, final int amount, final short durability) {
        this.blockData = blockData;
        this.amount = amount;
        this.durability = durability;
    }


    private static short getDurability(BlockData data) {
        if (data instanceof Ageable) {
            return (short) ((Ageable)data).getAge();
        }

        if (data instanceof Powerable) {
            return (short) (((Powerable)data).isPowered() ? 1 : 0);
        }

        return 0;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public short getDurability() {
        return durability;
    }

    @Override
    public Material getType() {
        return blockData.getMaterial();
    }

    @Override
    public boolean matches(BlockItemStack other) {
        if (other == null) {
            return false;
        }

        final BlockData blockData = Factory.getBlockData(other);
        return this.blockData.matches(blockData) && (durability == other.getDurability());
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public static class Factory implements BlockItemStackFactory {

        @Override
        public BlockItemStack of(final Block block) {
            return new ModernBlockItemStack(block.getBlockData(), 1, getDurability(block.getBlockData()));
        }

        @Override
        public BlockItemStack of(final Material type, final int amount, final short durability) {
            return new ModernBlockItemStack(type.createBlockData(), amount, durability);
        }

        @Override
        public BlockItemStack clone(final BlockItemStack original, final int amount) {
            final BlockData data = getBlockData(original);
            return new ModernBlockItemStack(data, amount, original.getDurability());
        }

        @Override
        public BlockItemStack clone(final BlockItemStack original, final int amount, final short durability) {
            final BlockData data = getBlockData(original);
            return new ModernBlockItemStack(data, amount, durability);
        }

        private static BlockData getBlockData(final BlockItemStack stack) {
            if (stack instanceof ModernBlockItemStack) {
                return ((ModernBlockItemStack)stack).getBlockData();
            }

            return stack.getType().createBlockData();
        }
    }
}
