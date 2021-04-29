package org.powbot.walking.local.nodes

import org.powbot.walking.model.Interaction
import org.powerbot.script.Tile

class LocalInteractionEdge(interaction: Interaction, parent: LocalEdge, destination: Tile) :
    LocalTileEdge(parent, destination) {
}
