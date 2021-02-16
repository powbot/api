package org.powbot.stream.locatable.interactive

import org.powbot.stream.ops.ActionableOps
import org.powbot.stream.ops.LocatableOps
import org.powbot.stream.ops.NameableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.GameObject
import java.util.stream.*

class GameObjectStream(ctx: ClientContext, stream: Stream<GameObject>) : InteractiveStream<GameObject, GameObjectStream>(ctx, stream),
    ActionableOps<GameObject, GameObjectStream>,
    LocatableOps<GameObject, GameObjectStream>,
    NameableOps<GameObject, GameObjectStream> {
    override fun nil(): GameObject {
        return GameObject.NIL
    }
}
