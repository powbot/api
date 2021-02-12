package org.powerbot.script.rt4;

import org.powerbot.bot.cache.AbstractCacheWorker;
import org.powerbot.bot.cache.Block;
import org.powerbot.bot.cache.JagexBufferStream;
import org.powerbot.script.Validatable;

import java.util.Arrays;


public class CacheModelConfig implements Validatable {



	public interface Loader {

		CacheModelConfig byId(int id);

		CacheModelConfig byIds(int[] ids);

	}

	public static CacheModelConfig load(AbstractCacheWorker worker, int id) {
		return worker.modelConfigLoader().byId(id);
	}

	public static CacheModelConfig load(AbstractCacheWorker worker, int[] ids) {
		return worker.modelConfigLoader().byIds(ids);
	}

	public int id;

	public byte[] alphas;
	public short[] colors;
	public byte[] renderPriorities;
	public byte[] renderTypes;

	public int vertexCount;
	public int[] verticesX;
	public int[] verticesY;
	public int[] verticesZ;

	public int indicesCount;
	public int[] indicesX;
	public int[] indicesY;
	public int[] indicesZ;

	public int texturedIndicesCount;
	public short[] texturedIndiciesX;
	public short[] texturedIndicesY;
	public short[] texturedIndicesZ;

	public short[] textureColors;
	public short[] faceTextures;
	public byte[] textureCoords;
	public byte[] textureRenderTypes;

	public int[] vertexSkins;
	public int[] indicesSkins;

	public byte priority;

	public CacheModelConfig(int id) {
		this.id = id;
	}

	@Override
	public boolean valid() {
		return id > 0 && vertexCount > 0 && verticesX != null && indicesX != null;
	}

}
