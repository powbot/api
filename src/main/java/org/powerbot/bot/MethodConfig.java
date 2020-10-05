package org.powerbot.bot;

import java.util.List;

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

}
