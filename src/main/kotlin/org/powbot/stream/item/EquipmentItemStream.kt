package org.powbot.stream.item

import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Item
import java.util.stream.*

class EquipmentItemStream(ctx: ClientContext, stream: Stream<Item>) : ItemStream<Item, Item, EquipmentItemStream>(ctx, stream, { it }) {
    override fun nil(): Item {
        return Item.NIL
    }

    override fun nilWrapped(): Item {
        return Item.NIL
    }
}
