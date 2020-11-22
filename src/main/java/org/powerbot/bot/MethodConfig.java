package org.powerbot.bot;

import java.util.List;
import java.util.Objects;

public class MethodConfig {

	private final String identifiedName;

	private final String parent;
	private final String name;
	private final String desc;
	private final String eventClass;
	private final List<Integer> callbackVariables;
	private final int dispatchEventIndex;

	public MethodConfig(final String identifiedName, final String parent, final String name, final String desc, final String eventClass, final List<Integer> callbackVariables, final int dispatchEventIndex) {
		this.identifiedName = identifiedName;
		this.parent = parent;
		this.name = name;
		this.desc = desc;
		this.eventClass = eventClass;
		this.callbackVariables = callbackVariables;
		this.dispatchEventIndex = dispatchEventIndex;
	}

	public String getDesc() {
		return desc;
	}

	public String getIdentifiedName() {
		return identifiedName;
	}

	public String getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}

	public List<Integer> getCallbackVariables() {
		return callbackVariables;
	}

	public String getEventClass() {
		return eventClass;
	}

	public int getDispatchEventIndex() {
		return dispatchEventIndex;
	}

	@Override
	public String toString() {
		return String.format("MethodConfig[parent=%s;name=%s;desc=%s;eventClass=%s,callbackVariables=%s,dispatchEventIndex=%d]", parent, name, desc, eventClass, callbackVariables.toString(), dispatchEventIndex);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		MethodConfig that = (MethodConfig) o;
		return dispatchEventIndex == that.dispatchEventIndex &&
			Objects.equals(identifiedName, that.identifiedName) &&
			Objects.equals(parent, that.parent) &&
			Objects.equals(name, that.name) &&
			Objects.equals(desc, that.desc) &&
			Objects.equals(eventClass, that.eventClass) &&
			Objects.equals(callbackVariables, that.callbackVariables);
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifiedName, parent, name, desc, eventClass, callbackVariables, dispatchEventIndex);
	}
}
