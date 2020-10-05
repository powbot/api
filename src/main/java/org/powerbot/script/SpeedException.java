package org.powerbot.script;

/**
 * SpeedException
 * A class responsible for conveying any speed related edge cases.
 */
class SpeedException extends RuntimeException {

	public static final int MINIMUM_SPEED = Input.MINIMUM_SPEED;
	public static final int MAXIMUM_SPEED = Input.MAXIMUM_SPEED;
	private final int speed;

	private SpeedException(int speed) {
		super(getRuntimeError(speed));
		this.speed = speed;
	}

	/**
	 * Returns the speed that caused this exception.
	 *
	 * @return the speed that caused this exception.
	 */
	public int getInputSpeed() {
		return speed;
	}

	/**
	 * Generates the runtime error to be displayed in the log.
	 *
	 * @param speed the speed causing the exception.
	 * @return the runtime error as String.
	 */
	private static String getRuntimeError(int speed) {
		StringBuilder errorBuilder = new StringBuilder();
		errorBuilder.append("The given speed is out of bounds.");
		if (speed < MINIMUM_SPEED) {
			errorBuilder.append("Speed should at least be ").append(MINIMUM_SPEED);
		} else if (speed > MAXIMUM_SPEED) {
			errorBuilder.append("Speed should at most be ").append(MAXIMUM_SPEED);
		} else {
			throw new RuntimeException("Given speed is not out of bounds. SpeedException shouldn't have been raised.");
		}
		errorBuilder.append(".\n");
		return errorBuilder.toString();
	}

	/**
	 * Generates and throws a speed exception error for the given speed.
	 *
	 * @param speed the speed causing the exception.
	 */
	static void forSpeed(int speed) {
		throw new SpeedException(speed);
	}

}
