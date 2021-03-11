package org.powbot.stream.locatable.interactive

import org.powbot.stream.ops.ActionableOps
import org.powbot.stream.ops.LocatableOps
import org.powbot.stream.ops.NameableOps
import org.powerbot.bot.rt4.client.internal.IBasicObject
import org.powerbot.script.Nameable
import org.powerbot.script.rt4.BasicObject
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.GameObject
import java.util.function.Predicate
import java.util.regex.Pattern
import java.util.stream.*

class GameObjectStream(ctx: ClientContext, stream: Stream<IBasicObject>)
    : InteractiveStream<GameObject, IBasicObject, GameObjectStream>(ctx, stream, { GameObject(ctx, it, GameObject.getType(it))}),
    ActionableOps<GameObject, IBasicObject, GameObjectStream>,
    LocatableOps<GameObject, IBasicObject, GameObjectStream>,
    NameableOps<GameObject, IBasicObject, GameObjectStream> {
    override fun nil(): GameObject {
        return GameObject.NIL
    }

    fun type(vararg types: GameObject.Type): GameObjectStream {
        return filter {
            types.contains(GameObject.getType(it))
        }
    }
}
