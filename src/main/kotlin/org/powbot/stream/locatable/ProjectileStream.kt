package org.powbot.stream.locatable

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.IdentifiableOps
import org.powbot.stream.ops.LocatableOps
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Projectile
import java.util.stream.*

class ProjectileStream(ctx: ClientContext, stream: Stream<Projectile>) : SimpleStream<Projectile, ProjectileStream>(ctx, stream),
    IdentifiableOps<Projectile, ProjectileStream>,
    LocatableOps<Projectile, ProjectileStream> {

    override fun nil(): Projectile {
        return Projectile.NIL
    }

}
