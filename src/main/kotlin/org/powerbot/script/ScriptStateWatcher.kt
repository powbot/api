package org.powerbot.script

class ScriptStateWatcher: ScriptStateListener {
    var state: ScriptState = ScriptState.Unknown

    override fun onChange(evt: ScriptStateChangedEvent) {
        state = evt.newState
    }
}
