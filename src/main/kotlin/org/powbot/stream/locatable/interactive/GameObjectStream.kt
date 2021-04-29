package org.powbot.stream.locatable.interactive

import org.powbot.stream.WrappedStream
import org.powbot.stream.ops.*
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.GameObject
import java.util.stream.*

class GameObjectStream(ctx: ClientContext, stream: Stream<GameObject>) :
    InteractiveStream<GameObject, GameObjectStream>(ctx, stream),
    ActionableOps<GameObject, GameObjectStream>,
    IdentifiableOps<GameObject, GameObjectStream>,
    LocatableOps<GameObject, GameObjectStream>,
    NameableOps<GameObject, GameObjectStream> {
    override fun nil(): GameObject {
        return GameObject.NIL
    }

    fun originalColor(vararg ids: Int): WrappedStream<GameObject, GameObjectStream> {
        return filter { ids.any { id -> id.toShort() in it.originalColors() } }
    }

    fun modifiedColor(vararg ids: Int): WrappedStream<GameObject, GameObjectStream> {
        return filter { ids.any { id -> id.toShort() in it.modifiedColors() } }
    }
}
