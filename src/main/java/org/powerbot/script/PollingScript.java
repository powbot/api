package org.powerbot.script;

import org.powerbot.script.rt4.Player;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * PollingScript
 * An implementation of {@link AbstractScript} which polls (or "loops") indefinitely.
 *
 * @param <C> the type of client
 */
public abstract class PollingScript<C extends ClientContext> extends AbstractScript<C> {
	/**
	 * Blocks other {@link org.powerbot.script.PollingScript}s with a lower {@link #priority} value.
	 */
	protected static final NavigableSet<PollingScript> threshold = new ConcurrentSkipListSet<>(Comparator.comparingInt(o -> o.priority.get()));
	/**
	 * The priority of this {@link org.powerbot.script.PollingScript} with respect to others.
	 */
	public final AtomicInteger priority;

	/**
	 * Creates an instance of a {@link PollingScript}.
	 */
	public PollingScript() {
		priority = new AtomicInteger(0);
		getExecQueue(State.START).add(() -> {
			try {
				start();
			} catch (final Throwable e) {
				ctx.controller.stop();
				log.log(Level.SEVERE, null, e);
			}
		});
		getExecQueue(State.STOP).add(this::stop);
		getExecQueue(State.SUSPEND).add(this::suspend);
		getExecQueue(State.RESUME).add(this::resume);

		getExecQueue(State.START).add(new Runnable() {
			@Override
			public void run() {
				if (threshold.isEmpty() || threshold.first().priority.get() <= priority.get()) {
					try {
						poll();
					} catch (final Throwable e) {
						ctx.controller.stop();
						log.log(Level.SEVERE, null, e);
					}
				}

				try {
					Thread.sleep(10);
				} catch (final InterruptedException ignored) {
					Thread.yield();
				}

				if (!Thread.interrupted() && !ctx.controller.isStopping()) {
					ctx.controller.offer(this);
				}
			}
		});
	}

	/**
	 * The main body of this {@link PollingScript}, which is called in a single-threaded loop.
	 */
	public abstract void poll();

	/**
	 * Called on {@link Script.State#START}.
	 */
	public void start() {
	}

	/**
	 * Called on {@link Script.State#STOP}.
	 */
	public void stop() {
	}

	/**
	 * Called on {@link Script.State#SUSPEND}.
	 */
	public void suspend() {
	}

	/**
	 * Called on {@link Script.State#RESUME}.
	 */
	public void resume() {
	}

	/**
	 * Called if the user specified run duration has been reached, triggerig {@link Script.State#STOP} if true is returned.
	 * @return true if script can safely be stopped
	 */
	public boolean canBreak() {

		if (ctx instanceof org.powerbot.script.rt4.ClientContext) {
			final org.powerbot.script.rt4.ClientContext rt4ctx = (org.powerbot.script.rt4.ClientContext) ctx;
			final Player p = rt4ctx.players.local();

			return (rt4ctx.npcs.select().within(5d).select(npc -> npc.interacting().equals(p) && npc.healthBarVisible()).isEmpty()) && !p.healthBarVisible();
		} else {
			return false;
		}
	}
}
