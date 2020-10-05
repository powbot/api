package org.powerbot.script;

public class Break implements Comparable<Break> {

	private final int breakTime, length, logoutType;

	public Break(final int breakTime, final int length, final int logoutType) {
		this.breakTime = (int) (breakTime * Random.nextDouble(0.9, 1.1));
		this.length = length; //already has variance within due to Sleep()
		this.logoutType = logoutType;
	}

	/**
	 * 1 full, 2 lobby, 3 idle
	 *
	 * @return the logout type
	 */
	public int getLogoutType() {
		return this.logoutType;
	}

	public int getBreakTime() {
		return this.breakTime;
	}

	public int getLength() {
		return this.length;
	}

	public String toString() {
		return "Break after " + getBreakTime() + (getBreakTime() > 1 ? " minutes" : " minute") + ", for " + (getLength() > 1 ? (getLength() + " minutes") : getLength() == 0 ? "ever" : "1 minute. ") + (getLogoutType() == 1 ? "(Full Logout)" : getLogoutType() == 2 ? "(Lobby)" : "(AFK)");
	}

	@Override
	public int compareTo(final Break o) {
		return Integer.compare(this.getBreakTime(), o.getBreakTime());
	}
}
