package diamondwalker.sscary.script.variable;

import diamondwalker.sscary.registry.SScaryScriptVariables;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class BlockPosVariable extends ScriptVariable<BlockPos, ScriptVariable<BlockPos, ?>> {
    protected BlockPosVariable(BlockPos originalValue, boolean shouldSync, String saveKey) {
        super(originalValue, shouldSync, saveKey);
    }

    @Override
    protected void writeToNBT(CompoundTag tag) {
        tag.putInt(saveKey + "X", get().getX());
        tag.putInt(saveKey + "Y", get().getY());
        tag.putInt(saveKey + "Z", get().getZ());
    }

    @Override
    protected void readFromNBT(CompoundTag tag) {
        set(new BlockPos(tag.getInt(saveKey + "X"), tag.getInt(saveKey + "Y"), tag.getInt(saveKey + "Z")));
    }

    @Override
    public Update getUpdate(int id) {
        return new Update(id, value);
    }

    public static class Update extends ScriptVariable.Update<BlockPos> {
        private Update(int id, BlockPos data) {
            super(id, data);
        }

        public Update(ByteBuf buf) {
            super(buf);
        }

        @Override
        public void writeToBuffer(ByteBuf buffer) {
            super.writeToBuffer(buffer);
            buffer.writeInt(data.getX());
            buffer.writeInt(data.getY());
            buffer.writeInt(data.getZ());
        }

        @Override
        public void readFromBuffer(ByteBuf buffer) {
            super.readFromBuffer(buffer);
            data = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }

        @Override
        public ScriptVariableType getType() {
            return SScaryScriptVariables.BLOCK_POS.get();
        }
    }

    public static ScriptVariable.Builder<BlockPos, BlockPosVariable> create() {
        return new ScriptVariable.Builder<>(BlockPos.ZERO, BlockPosVariable::new);
    }
}
