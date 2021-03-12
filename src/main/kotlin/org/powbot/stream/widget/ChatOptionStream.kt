package org.powbot.stream.widget

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.TextOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.ChatOption
import java.util.stream.*

class ChatOptionStream(ctx: ClientContext, stream: Stream<ChatOption>) : SimpleStream<ChatOption, ChatOption,ChatOptionStream>(ctx, stream, { it }),
    TextOps<ChatOption, ChatOption, ChatOptionStream> {
    override fun nil(): ChatOption {
        return ChatOption.NIL
    }

    override fun nilWrapped(): ChatOption {
        return ChatOption.NIL
    }
}
