package org.powbot.stream.item

import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Item
import java.util.stream.*

class EquipmentItemStream(ctx: ClientContext, stream: Stream<Item>) : ItemStream<Item, EquipmentItemStream>(ctx, stream) {
    override fun nil(): Item {
        return Item.NIL
    }
}
