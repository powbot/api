package org.powerbot.script;

import org.powbot.input.MouseMovement;
import org.powerbot.bot.AbstractMouseSpline;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Input
 * A utility class for generating input to the canvas and retrieving information from the canvas.
 */
public abstract class Input {

	public static final int MINIMUM_SPEED = 1;
	public static final int MAXIMUM_SPEED = 100;

	protected final AtomicBoolean blocking, keyboard;
	private final AbstractMouseSpline spline;
	private final AtomicInteger speed;

	protected Input(final AbstractMouseSpline spline) {
		blocking = new AtomicBoolean(false);
		keyboard = new AtomicBoolean(false);
		this.spline = spline;
		speed = new AtomicInteger(MINIMUM_SPEED);
	}

	public abstract Rectangle inputBounds();

	public abstract boolean isTargetInViewport(Point target);

	/**
	 * Returns the target component.
	 *
	 * @return the target {@link Component}
	 */
	public abstract Component getComponent();

	/**
	 * Returns the current mouse speed.
	 *
	 * @return the current mouse speed.
	 */
	public int speed() {
		return speed.get();
	}

	/**
	 * Set the relative speed for mouse movements.
	 * This is a sensitive function and should be used in exceptional circumstances for a short period of time only.
	 *
	 * @param s the new speed.
	 * @return the speed, which can be different to the value requested.
	 */
	public int speed(final int s) {
		if (s < MINIMUM_SPEED || s > MAXIMUM_SPEED) SpeedException.forSpeed(s);
		speed.set(s);
		return speed();
	}

	// TODO: remove boolean return values for input methods

	/**
	 * Whether or not user input is being relayed to the client instance.
	 *
	 * @return {@code true} if user input is being blocked, otherwise,
	 * {@code false}.
	 */
	public boolean blocking() {
		return blocking.get();
	}

	/**
	 * Sets whether or not to block user input from being relayed to the
	 * client instance.
	 *
	 * @param b Will block user input if set to {@code true}.
	 */
	public void blocking(final boolean b) {
		blocking.set(b);
	}

	/**
	 * Whether or not keyboard events are allowed.
	 *
	 * @return {@code true} if keybaord events are allowed.
	 */
	public boolean keyboard() {
		return keyboard.get();
	}

	/**
	 * Allow keyboard events to be relayed regardless of {@link #blocking(boolean)}.
	 *
	 * @param b {@code true} to allow keyboard events
	 */
	public void keyboard(final boolean b) {
		keyboard.set(b);
	}

	public boolean isFocused() {
		return isFocused(getComponent());
	}

	protected boolean isFocused(final Component c) {
		return c != null && c.isFocusOwner() && c.isShowing();
	}

	/**
	 * Simulates the client focusing.
	 */
	public abstract void focus();


	/**
	 * Simulates the client de-focusing.
	 */
	public abstract void defocus();

	/**
	 * Sends keys to the client, represented in a String. Virtual keys are also
	 * acceptable input, for example, <code>{VK_ENTER}</code>.
	 * See {@link java.awt.event.KeyEvent} for the list of Virtual Keys.
	 *
	 * @param s the text to send
	 * @return whether or not the keys were successfully sent.
	 */
	public abstract boolean send(final String s);

	/**
	 * Sends keys to the client, represented in a String, and a new-line at the
	 * end. Virtual keys are also acceptable input, for example,
	 * <code>{VK_ENTER}</code>.
	 * See {@link java.awt.event.KeyEvent} for the list of Virtual Keys.
	 *
	 * @param s the text to send
	 * @return whether or not the keys were successfully sent.
	 */
	public boolean sendln(final String s) {
		return send(s + "\n");
	}

	/**
	 * Gets the location of the simulated mouse.
	 *
	 * @return The location as a {@link Point}.
	 */
	public abstract Point getLocation();

	/**
	 * Gets the location of where the simulated mouse has pressed last.
	 *
	 * @return The location as a {@link Point}.
	 */
	public abstract Point getPressLocation();

	/**
	 * Represents when the simulated mouse was pressed.
	 *
	 * @return The point of which the mouse was pressed, measured in
	 * milliseconds, between the current time and midnight,
	 * January 1, 1970 UTC.
	 */
	public abstract long getPressWhen();

	/**
	 * Simulates the specified mouse button being pressed.
	 *
	 * @param button The index of the button to press.
	 * @return Whether or not that button press has been successfully
	 * simulated.
	 */
	public abstract boolean press(final int button);


	/**
	 * Simulates the specified mouse button being released.
	 *
	 * @param button The index of the button to press.
	 * @return Whether or not that button release has been successfully
	 * simulated.
	 */
	public abstract boolean release(final int button);

	protected abstract boolean setLocation(final Point p);

	public abstract void setMouseLocation(Point p);

	/**
	 * Simulates the mouse clicking the specified button at the specified
	 * location.
	 *
	 * @param x      The point which lies on the x-axis of where to click.
	 * @param y      The point which lies on the y-axis of where to click.
	 * @param button The button index to simulate.
	 * @return Whether or not the mouse click has been successfully simulated.
	 */
	public boolean click(final int x, final int y, final int button) {
		return click(new Point(x, y), button);
	}

	/**
	 * Simulates the mouse clicking the specified button at the specified
	 * location.
	 *
	 * @param x    The point which lies on the x-axis of where to click.
	 * @param y    The point which lies on the y-axis of where to click.
	 * @param left Whether or not to click the left mouse button, otherwise, it
	 *             will click the right.
	 * @return Whether or not the mouse click has been successfully simulated.
	 */
	public boolean click(final int x, final int y, final boolean left) {
		return click(new Point(x, y), left);
	}

	/**
	 * Simulates the mouse clicking the specified button at the specified
	 * location.
	 *
	 * @param point  The point of where to move the mouse and click.
	 * @param button The button index to simulate.
	 * @return Whether or not the mouse click has been successfully simulated.
	 */
	public boolean click(final Point point, final int button) {
		return move(point) && click(button);
	}

	/**
	 * Simulates the mouse clicking the specified button at the specified
	 * location.
	 *
	 * @param point The point of where to move the mouse and click.
	 * @param left  Whether not to click the left mouse button, otherwise, it
	 *              will click the right.
	 * @return Whether or not the mouse click has been successfully simulated.
	 */
	public boolean click(final Point point, final boolean left) {
		return move(point) && click(left);
	}

	/**
	 * Simulates the mouse clicking the specified button.
	 *
	 * @param left Whether or not to click the left mouse button, otherwise, it
	 *             will click the right.
	 * @return Whether or not the mouse click has been successfully simulated.
	 */
	public boolean click(final boolean left) {
		return click(left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
	}

	/**
	 * Simulates the mouse clicking the specified button.
	 *
	 * @param button The button index to simulate.
	 * @return Whether or not the mouse click has been successfully simulated.
	 */
	public boolean click(final int button) {
		press(button);
		Condition.sleep(spline.getPressDuration());
		release(button);
		Condition.sleep(spline.getPressDuration());
		return true;
	}

	/**
	 * Holds down the left or right button, and drags the mouse to the
	 * specified location.
	 *
	 * @param p    The point of where to drag the mouse to.
	 * @param left Whether or not to click the left mouse button, otherwise, it
	 *             will click the right.
	 * @return Whether or not the mouse click has been successfully simulated.
	 */
	public boolean drag(final Point p, final boolean left) {
		return drag(p, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
	}


	/**
	 * Holds down the specified button, and drags the mouse to the specified
	 * location.
	 *
	 * @param p      The point of where to drag the mouse to.
	 * @param button the button
	 * @return Whether or not the mouse click has been successfully simulated.
	 */
	public boolean drag(final Point p, final int button) {
		press(button);
		Condition.sleep(spline.getPressDuration());
		final boolean b = move(p);
		Condition.sleep(spline.getPressDuration());
		release(button);
		Condition.sleep(spline.getPressDuration());
		return b;
	}

	/**
	 * Sets the mouse position to the specified Point.
	 *
	 * @param p The point to move the mouse to.
	 * @return {@code true} if the point is within bounds and was
	 * successfully moved.
	 */
	public boolean hop(final Point p) {
		return setLocation(p);
	}

	/**
	 * Sets the mouse position to the specified {@link Point}.
	 *
	 * @param x The point on the x-axis to move the mouse to.
	 * @param y The point on the y-axis to move the mouse to.
	 * @return {@code true} if the point is within bounds and was
	 * successfully moved.
	 */
	public boolean hop(final int x, final int y) {
		return hop(new Point(x, y));
	}


	/**
	 * Moves the mouse position in a smooth and human-like manner
	 * to the specified point.
	 *
	 * @param x The point on the x-axis to move the mouse to.
	 * @param y The point on the y-axis to move the mouse to.
	 * @return {@code true} if the point is within bounds and was
	 * successfully moved.
	 */
	public boolean move(final int x, final int y) {
		return move(new MouseMovement(() -> new Point(x, y), () -> true));
	}

	/**
	 * Moves the mouse position in a smooth and human-like manner
	 * to the specified {@link Point}.
	 *
	 * @param p The point of where to move the mouse to.
	 * @return {@code true} if the point is within bounds and was
	 * successfully moved.
	 */
	public boolean move(final Point p) {
		return move(new MouseMovement(() -> p, () -> true));
	}

	@Deprecated
	public boolean apply(final Targetable targetable, final Filter<Point> filter) {
		final Point targetPoint = targetable.nextPoint();
		return move(new MouseMovement(() -> targetPoint, () -> true));
	}

	/**
	 * Simulates the mouse wheel scrolling down.
	 *
	 * @return {@code true} if the mouse wheel has been successfully
	 * simulated, otherwise, {@code false}.
	 */
	public boolean scroll() {
		return scroll(true);
	}

	/**
	 * Simulates the mouse wheel scrolling.
	 *
	 * @param down Whether or not to scroll up or down.
	 * @return {@code true} if the mouse wheel has been successfully
	 * simulated, otherwise, {@code false}.
	 */
	public abstract boolean scroll(final boolean down);

	/**
	 * Queues a {@link MouseMovement} and blocks until it's completed or cancelled
	 *
	 * @param command A mouse command
	 * @return {@code true} if the command was successfully executed, otherwise {@code false}
	 */
	public abstract boolean move(MouseMovement command);

	public abstract boolean isTouchScreen();

	public abstract Dimension getComponentSize();

	public Point scale(Point x) {
		return x;
	}

}
