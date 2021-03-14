package org.powerbot.bot.rt4.client.internal;

import org.powerbot.script.rt4.ClientContext;

public interface IPlayer extends IActor {

	int getCombatLevel();

	IPlayerComposite getComposite();

	IStringRecord getName();

	int getTeam();

	default int combatLevel() {
		return getCombatLevel();
	}

	@Override
	default String[] actions() {
		return new String[0];
	}

	@Override
	default int id() {
		return -1;
	}

	@Override
	default String name() {
		final String str = getName() != null ? getName().getValue() : "";
		return str != null ? str : "";
	}

	@Override
	default int[] modelIds() {
		return null;
	}

	default int[] appearance() {
		final IPlayerComposite composite = getComposite();
		int[] arr = composite != null ? composite.getAppearance() : new int[0];
		if (arr == null) {
			arr = new int[0];
		}
		arr = arr.clone();
		for (int index = 0; index < arr.length; ++index) {
			arr[index] = arr[index] < 512 ? -1 : arr[index] - 512;
		}
		return arr;
	}

	@Override
	default int healthPercent() {
		if (!valid()) {
			return -1;
		}

		if (this == ClientContext.ctx().client().getPlayer()) {
			return ClientContext.ctx().combat.healthPercent();
		}

		return IActor.super.healthPercent();
	}

	@Override
	default boolean valid() {
		final IClient client = ClientContext.ctx().client();
		if (client == null) {
			return false;
		}
		final IPlayer[] arr = client.getPlayers();
		for (final IPlayer a : arr) {
			if (this.equals(a)) {
				return true;
			}
		}
		return false;
	}

	@Override
	default long getModelCacheId() {
		final IPlayerComposite composite = getComposite();
		if (composite == null) {
			return -1;
		}

		return composite.getUid();
	}


	@Override
	default ICache getModelCache() {
		final IClient client = ClientContext.ctx().client();
		if (client == null) {
			return null;
		}

		return client.getPlayerModelCache();
	}
}
