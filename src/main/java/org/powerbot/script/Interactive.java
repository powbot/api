package org.powerbot.script;

import org.powbot.input.MouseMovement;
import org.powerbot.bot.rt4.client.internal.IActor;
import org.powerbot.bot.rt4.client.internal.IBasicObject;
import org.powerbot.script.action.Emittable;
import org.powerbot.script.rt4.Actor;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Menu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.concurrent.Callable;

/**
 * Interactive
 * An entity which can be interacted with through direct clicking and menu options.
 */
public interface Interactive extends Targetable, Validatable, Viewable, Drawable {
	/**
	 * Finds the base point of the entity within the game screen.
	 *
	 * @return the base point if the entity is on the screen; null otherwise
	 */
	Point basePoint();

	/**
	 * Finds the centroid of the entity within the game screen.
	 *
	 * @return the centroid if the entity is on the screen; null otherwise
	 */
	Point centerPoint();

	Callable<Point> calculateScreenPosition();

	/**
	 * Hovers the target and compensates for movement.
	 *
	 * @return {@code true} if the mouse is within the target; otherwise {@code false}
	 */
	default boolean hover() {
		final MouseMovement movement = new MouseMovement(calculateScreenPosition(), this::valid);
		return ClientContext.ctx().input.move(movement);
	}

	/**
	 * Clicks the target and compensates for movement. Does not check intent or expected result (mouse cross-hair).
	 *
	 * @return {@code true} if the click was executed; otherwise {@code false}
	 */
	default boolean click() {
		return click(true);
	}

	/**
	 * Clicks the target and compensates for movement. Does not check intent or expected result (mouse cross-hair).
	 *
	 * @param left {@code true} to click left, {@code false} to click right
	 * @return {@code true} if the click was executed; otherwise {@code false}
	 */
	default boolean click(boolean left) {
		return click(left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
	}

	/**
	 * Clicks the target and compensates for movement. Does not check intent or expected result (mouse cross-hair).
	 *
	 * @param button the desired mouse button to press
	 * @return {@code true} if the click was executed; otherwise {@code false}
	 */
	default boolean click(int button) {
		final MouseMovement movement = new MouseMovement(calculateScreenPosition(), this::valid);
		return ClientContext.ctx().input.move(movement) && ClientContext.ctx().input.click(button);
	}

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean click(String action) {
		if (ClientContext.ctx().bot().useActionEmitter() && this instanceof Emittable) {
			ClientContext.ctx().bot().getActionEmitter().emit(((Emittable) this).createAction(action));
			return true;
		}

		return click(Menu.filter(action));
	}

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @param option the option to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean click(String action, String option) {
		if (ClientContext.ctx().bot().useActionEmitter() && this instanceof Emittable) {
			ClientContext.ctx().bot().getActionEmitter().emit(((Emittable) this).createAction(action));
			return true;
		}

		return click(Menu.filter(action, option));
	}

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param c the menu command to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean click(Filter<? super MenuCommand> c) {
		return interact(c);
	}

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean interact(String action) {
		if (ClientContext.ctx().bot().useActionEmitter() && this instanceof Emittable) {
			ClientContext.ctx().bot().getActionEmitter().emit(((Emittable) this).createAction(action));
			return true;
		}

		return interact(true, action);
	}

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @param option the option to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean interact(String action, String option) {
		if (ClientContext.ctx().bot().useActionEmitter() && this instanceof Emittable) {
			ClientContext.ctx().bot().getActionEmitter().emit(((Emittable) this).createAction(action));
			return true;
		}

		return interact(true, action, option);
	}

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click (as defined).
	 * When auto is set to false, the method forcibly right clicks before searching for menu options.
	 * This is useful when precision clicking is required and the option is always in the menu (not up-text).
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param auto   {@code true} is normal behavior, {@code false} forces right click
	 * @param action the action to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean interact(boolean auto, String action) {
		if (ClientContext.ctx().bot().useActionEmitter() && this instanceof Emittable) {
			ClientContext.ctx().bot().getActionEmitter().emit(((Emittable) this).createAction(action));
			return true;
		}

		return interact(auto, Menu.filter(action));
	}

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click (as defined).
	 * When auto is set to false, the method forcibly right clicks before searching for menu options.
	 * This is useful when precision clicking is required and the option is always in the menu (not up-text).
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param auto   {@code true} is normal behavior, {@code false} forces right click
	 * @param action the action to look for
	 * @param option the option to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean interact(boolean auto, String action, String option) {
		if (ClientContext.ctx().bot().useActionEmitter() && this instanceof Emittable) {
			ClientContext.ctx().bot().getActionEmitter().emit(((Emittable) this).createAction(action));
			return true;
		}

		return interact(auto, Menu.filter(action, option));
	}

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click.
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param c the menu command to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean interact(Filter<? super MenuCommand> c) {
		return interact(true, c);
	}

	/**
	 * Interacts with the target and compensates for movement.
	 * This method will interact (and choose it) when it finds the desired action.
	 * This method accomplishes it via left or right click (as defined).
	 * When auto is set to false, the method forcibly right clicks before searching for menu options.
	 * This is useful when precision clicking is required and the option is always in the menu (not up-text).
	 * WARNING: this method DOES NOT check intent or expected result (mouse cross-hair).
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param auto {@code true} is normal behavior, {@code false} forces right click
	 * @param c    the menu command to look for
	 * @return {@code true} if the mouse was clicked, otherwise {@code false}
	 */
	default boolean interact(boolean auto, Filter<? super MenuCommand> c) {
		if (!valid()) {
			return false;
		}

		final Callable<Point> position = calculateScreenPosition();
		final Callable<Boolean> valid = () -> {
			if (this instanceof IActor || this instanceof IBasicObject) {
				return valid() && inViewport();
			} else {
				return valid();
			}
		};

		try {
//			if (ctx.client().isMobile() && (Interactive.this instanceof Actor || Interactive.this instanceof GameObject)) {
//				// We need to do this in order to load the menu
//				final Point target = position.call();
//				ctx.input.move(target);
//				ctx.input.drag(new Point(target.x + 3, target.y + 3), false);
//				Condition.sleep(Random.nextInt(10, 20));
//			}

			final MouseMovement movement = new MouseMovement(position, valid);
			if (ClientContext.ctx().input.move(movement)) {
				return ClientContext.ctx().menu.click(c);
			}
			return false;
		} catch (Throwable t) {
			t.printStackTrace();
			return false;
		} finally {
			if (ClientContext.ctx().menu.opened()) {
				ClientContext.ctx().menu.close();
			}
		}
	}

	/**
	 * Clicks the target and compensates for movement. Does not check intent.
	 *
	 * @param result the crosshair to check against after interaction
	 * @return {@code true} if the click was executed and <code>ctx.game.crosshair() == result</code>; otherwise {@code false}
	 */
	default boolean click(Crosshair result) {
		return click() && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return org.powerbot.script.rt4.ClientContext.ctx().game.crosshair() == result;
			}
		}, 10, 5);
	}

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent.
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @param result the crosshair to check against after interaction
	 * @return {@code true} if the mouse was clicked and <code>ctx.game.crosshair() == result</code>, otherwise {@code false}
	 */
	default boolean click(String action, Crosshair result) {
		return click(action) && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return org.powerbot.script.rt4.ClientContext.ctx().game.crosshair() == result;
			}
		}, 10, 5);
	}

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent.
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param action the action to look for
	 * @param option the option to look for
	 * @param result the crosshair to check against after interaction
	 * @return {@code true} if the mouse was clicked and <code>ctx.game.crosshair() == result</code>, otherwise {@code false}
	 */
	default boolean click(String action, String option, Crosshair result) {
		return click(action, option) && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return org.powerbot.script.rt4.ClientContext.ctx().game.crosshair() == result;
			}
		}, 10, 5);
	}

	/**
	 * Clicks the target and compensates for movement.
	 * This method expects (and requires) that the given action is up-text (menu index 0).
	 * This method can be used when precision clicking is required, and the option is guaranteed to be up-text.
	 * WARNING: this method DOES NOT check intent.
	 * WARNING: The return status does not guarantee the correct action was acted upon.
	 *
	 * @param c      the menu command to look for
	 * @param result the crosshair to check against after interaction
	 * @return {@code true} if the mouse was clicked and <code>ctx.game.crosshair() == result</code>, otherwise {@code false}
	 */
	default boolean click(Filter<? super MenuCommand> c, Crosshair result) {
		return click(c) && Condition.wait(new Condition.Check() {
			@Override
			public boolean poll() {
				return ClientContext.ctx().game.crosshair() == result;
			}
		}, 10, 5);
	}

	/**
	 * Sets the boundaries of this entity utilizing an array.
	 *
	 * @param arr {x1, x2, y1, y2, z1, z2}
	 */
	default void bounds(final int[] arr) {

	}

	/**
	 * The translated model triangles.
	 *
	 * @return the translated model triangles
	 */
	default Polygon[] triangles() {
		return new Polygon[0];
	}
}
