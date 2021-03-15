package org.powerbot.script.action;

public interface Emittable<T extends AbstractAction> {

	T createAction(String action);

	T createAction(String action, boolean async);

}
