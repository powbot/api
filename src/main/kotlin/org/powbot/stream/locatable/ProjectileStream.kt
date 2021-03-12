package org.powbot.stream.locatable

import org.powbot.stream.SimpleStream
import org.powbot.stream.ops.IdentifiableOps
import org.powbot.stream.ops.LocatableOps
import org.powerbot.bot.rt4.client.internal.IProjectile
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Projectile
import org.powerbot.script.rt4.Projectiles
import java.util.stream.*

class ProjectileStream(ctx: ClientContext, stream: Stream<IProjectile>) :
    SimpleStream<Projectile, IProjectile, ProjectileStream>(ctx, stream, { Projectile(org.powerbot.script.ClientContext.ctx(), it)}),
    IdentifiableOps<Projectile, IProjectile, ProjectileStream>,
    LocatableOps<Projectile, IProjectile, ProjectileStream> {

    override fun nil(): IProjectile {
        return Projectiles.NIL
    }

    override fun nilWrapped(): Projectile {
        return Projectile.NIL
    }

}
