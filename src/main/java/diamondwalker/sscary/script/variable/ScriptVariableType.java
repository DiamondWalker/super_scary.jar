package diamondwalker.sscary.script.variable;

import io.netty.buffer.ByteBuf;

import java.util.function.Function;

public interface ScriptVariableType {
    ScriptVariable.Update<?> build(ByteBuf buf);
}
