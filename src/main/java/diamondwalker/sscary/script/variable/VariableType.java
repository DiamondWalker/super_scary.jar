package diamondwalker.sscary.script.variable;

import io.netty.buffer.ByteBuf;

import java.util.function.Function;

enum VariableType {
    BOOL(BooleanVariable.Update::new),
    INT(IntegerVariable.Update::new);

    final Function<ByteBuf, ScriptVariable.Update<?>> constructor;

    VariableType(Function<ByteBuf, ScriptVariable.Update<?>> constructor) {
        this.constructor = constructor;
    }
}
