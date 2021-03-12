package org.powerbot.script.action;

import org.powerbot.script.rt4.ClientContext;

public interface Emittable<T extends AbstractAction> {

	default T createAction(String action) {
		return createAction(action, false);
	}

	T createAction(String action, boolean async);

	default boolean emit(String action) {
		return emit(action, false);
	}

	default boolean emit(String action, boolean async) {
		final T a = createAction(action, async);
		if (action == null) {
			return false;
		}

		ClientContext.ctx().actionEmitter.emit(a);
		return true;
	}

}
