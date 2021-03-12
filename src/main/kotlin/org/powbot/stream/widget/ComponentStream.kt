package org.powbot.stream.widget

import org.powbot.stream.locatable.interactive.InteractiveStream
import org.powbot.stream.ops.IdentifiableOps
import org.powbot.stream.ops.TextOps
import org.powerbot.bot.rt4.client.internal.IWidget
import org.powerbot.script.Client
import org.powerbot.script.Filter
import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Component
import org.powerbot.script.rt4.Components
import org.powerbot.script.rt4.Widgets
import java.awt.Rectangle
import java.util.regex.Pattern
import java.util.stream.Stream

open class ComponentStream(ctx: ClientContext, stream: Stream<IWidget>) :
    InteractiveStream<Component, IWidget, ComponentStream>(ctx, stream, { Component(ClientContext.ctx(), null, null, it.id, it) }),
    TextOps<Component, IWidget, ComponentStream>,
    IdentifiableOps<Component, IWidget, ComponentStream>{

    /**
     * Filters the current query's contents to only include components that are currently visible and in the viewport
     *
     * @return filtered query
     */
    override fun viewable(): ComponentStream {
        return super.viewable().filter(Filter { obj: IWidget -> obj.visible() })
    }

    /**
     * Filters for components which are containers of other components (parents of children components).
     *
     * @return {@code this} for the purpose of chaining.
     */
    open fun parents(): ComponentStream {
        return filter(Filter { c: IWidget -> c.children != null && c.children.isNotEmpty() })
    }

    /**
     * Filters the current query's contents to only include components who match the specified widget index
     *
     * @param index index to find
     * @return filtered query
     */
    open fun widget(index: Int): ComponentStream {
        return filter(Filter { component: IWidget ->
            (component.parentId() shr 16) == index
        })
    }

    /**
     * Filters the current query's contents to only include components whose tooltip contains at least one (1) of the parameter strings.
     * Comparisons are NOT case sensitive.
     *
     * @param text [varargs] the strings to compare the component tooltip to
     * @return filtered query
     */
    open fun tooltipContains(vararg texts: String): ComponentStream {
        val textsLower = texts.map { it.toLowerCase() }
        return filter(Filter { t: IWidget ->
            val text1 = t.tooltip.toLowerCase().trim()
            textsLower.any { s -> text1.contains(s) }
        })
    }

    /**
     * Filters the current query's contents to only include components whose tooltip equals to at least one (1) of the parameter strings.
     * Comparisons ARE case sensitive.
     *
     * @param strings [varargs] the strings to compare the component tooltip to
     * @return filtered query
     */
    open fun tooltip(vararg texts: String): ComponentStream {
        val textsLower = texts.map { it.toLowerCase() }
        return filter(Filter { t: IWidget ->
            val text1 = t.tooltip.toLowerCase().trim()
            textsLower.any { s -> text1.equals(s) }
        })
    }

    /**
     * Filters the current query's contents to only include components whose tooltip matches the parameter Pattern
     *
     * @param pattern the Pattern to compare component tooltip to
     * @return filtered query
     */
    open fun tooltip(pattern: Pattern): ComponentStream {
        return filter(Filter { component: IWidget ->
            pattern.matcher(
                component.tooltip.trim()
            ).find()
        })
    }

    /**
     * Filters the current query's contents to only include components whose content type matches at least one (1) of the parameter ints
     *
     * @param types the content types to compare component to
     * @return filtered query
     */
    open fun contentType(vararg types: Int): ComponentStream {
        return filter(Filter { component: IWidget ->
            types.any { it == component.contentType }
        })
    }

    /**
     * Filters the current query's contents to only include components whose model id matches at least one (1) of the parameter ints
     *
     * @param ids the model ids to compare component to
     * @return filtered query
     */
    open fun modelId(vararg ids: Int): ComponentStream {
        return filter(Filter { component: IWidget ->
            ids.any {it == component.modelId }
        })
    }

    /**
     * Filters the current query's contents to only include components whose item id matches at least one (1) of the parameter ints
     *
     * @param ids the item ids to compare component to
     * @return filtered query
     */
    open fun itemId(vararg ids: Int): ComponentStream {
        return filter(Filter { component: IWidget ->
            ids.any { it == component.itemId }
        })
    }

    /**
     * Filters the current query's contents to only include components whose texture id matches at least one (1) of the parameter ints
     *
     * @param textures the texture ids to compare component to
     * @return filtered query
     */
    open fun texture(vararg textures: Int): ComponentStream {
        return filter(Filter { component: IWidget ->
            textures.any { it == component.textureId }
        })
    }

    /**
     * Filters the current query's contents to only include components whose width and height bounds match at least one (1) of the parameter rectangles
     *
     * @param rectangles the rectangle bounds to compare component to
     * @return filtered query
     */
    open fun bounds(vararg rectangles: Rectangle): ComponentStream {
        return filter(Filter { component: IWidget ->
            rectangles.any { it.width == component.width && it.height == component.height }
        })
    }

    /**
     * Filters the current query's contents to only include components whose width matches at least one (1) of the parameter ints
     *
     * @param widths the widths to compare component to
     * @return filtered query
     */
    open fun width(vararg widths: Int): ComponentStream {
        return filter(Filter { c: IWidget ->
            widths.any { it == c.width }
        })
    }

    /**
     * Filters the current query's contents to only include components whose height matches at least one (1) of the parameter ints
     *
     * @param heights the heights to compare component to
     * @return filtered query
     */
    open fun height(vararg heights: Int): ComponentStream {
        return filter(Filter { c: IWidget ->
            heights.any { it == c.height }
        })
    }

    /**
     * Filters the current query's contents to only include components whose scroll width matches at least one (1) of the parameter ints
     *
     * @param widths the scroll widths to compare component to
     * @return filtered query
     */
    open fun scrollWidth(vararg widths: Int): ComponentStream {
        return filter(Filter { c: IWidget ->
            widths.any { it == c.scrollWidth }
        })
    }

    /**
     * Filters the current query's contents to only include components whose scroll height matches at least one (1) of the parameter ints
     *
     * @param heights the scroll heights to compare component to
     * @return filtered query
     */
    open fun scrollHeight(vararg heights: Int): ComponentStream {
        return filter(Filter { c: IWidget ->
            heights.any { it == c.scrollHeight }
        })
    }

    override fun nil(): IWidget {
        return Widgets.NIL
    }

    override fun nilWrapped(): Component {
        return Component.NIL
    }
}
