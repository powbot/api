package org.powerbot.script

class ScriptStateWatcher: ScriptStateListener {
    var state: ScriptState = ScriptState.Unknown
    var script: AbstractScript<*>? = null

    override fun onChange(evt: ScriptStateChangedEvent) {
        state = evt.newState
        script = ClientContext.ctx().controller.script()
    }
}
