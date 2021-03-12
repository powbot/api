package org.powbot.stream.widget

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.IdentifiableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Widget
import java.util.stream.Stream

open class WidgetStream(ctx: ClientContext, stream: Stream<Widget>) :
    SimpleStream<Widget, Widget, WidgetStream>(ctx, stream, { it }),
    IdentifiableOps<Widget, Widget, WidgetStream> {
    override fun nil(): Widget {
        return Widget.NIL
    }

    override fun nilWrapped(): Widget {
        return Widget.NIL
    }
}
