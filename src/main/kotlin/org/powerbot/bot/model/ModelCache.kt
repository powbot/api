package org.powerbot.bot.model

import kotlinx.coroutines.*

import org.powerbot.bot.rt4.client.internal.IRenderable
import org.powerbot.script.ModelRenderListener
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Model
import java.util.concurrent.ConcurrentHashMap

data class TimedModelWrapper(var model: Model?, var animated: Boolean, var mirror: Boolean, var lastRequestTime: Long)

class ModelCache(val ctx: ClientContext) : ModelRenderListener {

    val cache = ConcurrentHashMap<IRenderable, TimedModelWrapper>()

    init {
        GlobalScope.launch {
            while (true) {
                cleanup()
                delay(60000)
            }
        }
    }

    private fun cleanup() {
        cache.forEachEntry(10) {
            if (System.currentTimeMillis() - it.value.lastRequestTime > 30000) {
                cache.remove(it.key)
            }
        }
    }

    fun getModel(renderable: IRenderable, animated: Boolean, mirror: Boolean): Model? {
        if (cache.containsKey(renderable)) {
            val wrapper = cache[renderable]
            wrapper?.lastRequestTime = System.currentTimeMillis()

            return wrapper?.model
        }

        val wrapper = TimedModelWrapper(null, animated, mirror, System.currentTimeMillis())

        cache.put(renderable, wrapper)

        return wrapper.model
    }

    override fun onRender(renderable: IRenderable, verticesX: IntArray?, verticesY: IntArray?, verticesZ: IntArray?, indicesX: IntArray?, indicesY: IntArray?, indicesZ: IntArray?) {
        val wrapper = cache[renderable]
        if (wrapper != null && (wrapper.model == null || wrapper.animated)) {
            wrapper.model = if (wrapper.model != null)
                wrapper.model!!.update(verticesX, verticesY, verticesZ, indicesX, indicesY, indicesZ, wrapper.mirror)
            else
                Model(ctx, verticesX, verticesY, verticesZ, indicesX, indicesY, indicesZ, wrapper.mirror)
        }
    }
}
