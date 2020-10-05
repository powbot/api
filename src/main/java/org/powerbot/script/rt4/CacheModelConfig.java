package org.powerbot.script.rt4;

import org.powerbot.bot.cache.AbstractCacheWorker;
import org.powerbot.bot.cache.Block;
import org.powerbot.bot.cache.JagexBufferStream;

import java.util.Arrays;

public class CacheModelConfig {

	public static final int CACHE_IDX = 7;

	private JagexBufferStream stream;

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

	public static CacheModelConfig load(final AbstractCacheWorker worker, final int id) {
		final Block b = worker.getBlock(CACHE_IDX, id);
		if (b == null) {
			return null;
		}
		return new CacheModelConfig(new Block.Sector(CACHE_IDX, id, b.getBlock()));
	}

	public static CacheModelConfig load(final AbstractCacheWorker worker, final int[] modelIds) {
		if (modelIds == null) {
			return null;
		}

		final CacheModelConfig[] models = new CacheModelConfig[modelIds.length];
		for (int i = 0; i < modelIds.length; i++) {
			models[i] = load(worker, modelIds[i]);
		}

		if (models.length == 1) {
			return models[0];
		} else {
			return new CacheModelConfig(models, Arrays.hashCode(modelIds));
		}
	}

	CacheModelConfig(final Block.Sector sector) {
		this.id = sector.getIdentifier();
		this.stream = new JagexBufferStream(sector.getPayload());

		read();
	}

	private void read() {
		final byte[] payload = stream.getAllBytes();
		if (payload[payload.length - 1] == -1 && payload[payload.length - 2] == -1) {
			this.loadV1();
		} else {
			this.loadV2();
		}
	}

	void loadV1() {
		final byte[] payload = stream.getAllBytes();
		final JagexBufferStream var24 = new JagexBufferStream(payload);
		final JagexBufferStream var3 = new JagexBufferStream(payload);
		final JagexBufferStream var28 = new JagexBufferStream(payload);
		final JagexBufferStream var6 = new JagexBufferStream(payload);
		final JagexBufferStream var55 = new JagexBufferStream(payload);
		final JagexBufferStream var51 = new JagexBufferStream(payload);
		stream.seek(payload.length - 23);
		final int verticeCount = stream.getUShort();
		final int indicesCount = stream.getUShort();
		final int texturedIndicesCount = stream.getUByte();
		final int var13 = stream.getUByte();
		final int modelPriority = stream.getUByte();
		final int var50 = stream.getUByte();
		final int var17 = stream.getUByte();
		final int modelTexture = stream.getUByte();
		final int modelVertexSkins = stream.getUByte();
		final int var20 = stream.getUShort();
		final int var21 = stream.getUShort();
		final int var42 = stream.getUShort();
		final int var22 = stream.getUShort();
		final int var38 = stream.getUShort();
		int textureAmount = 0;
		int var7 = 0;
		int var29 = 0;
		int position;
		if (texturedIndicesCount > 0) {
			textureRenderTypes = new byte[texturedIndicesCount];
			stream.seek(0);

			for (position = 0; position < texturedIndicesCount; ++position) {
				final byte renderType = textureRenderTypes[position] = stream.getByte();
				if (renderType == 0) {
					textureAmount++;
				}

				if (renderType >= 1 && renderType <= 3) {
					var7++;
				}

				if (renderType == 2) {
					var29++;
				}
			}
		}

		position = texturedIndicesCount + verticeCount;
		final int renderTypePos = position;
		if (var13 == 1) {
			position += indicesCount;
		}

		final int var49 = position;
		position += indicesCount;
		final int priorityPos = position;
		if (modelPriority == 255) {
			position += indicesCount;
		}

		final int indicesSkinPos = position;
		if (var17 == 1) {
			position += indicesCount;
		}

		final int var35 = position;
		if (modelVertexSkins == 1) {
			position += verticeCount;
		}

		final int alphaPos = position;
		if (var50 == 1) {
			position += indicesCount;
		}

		final int var11 = position;
		position += var22;
		final int texturePos = position;
		if (modelTexture == 1) {
			position += indicesCount * 2;
		}

		final int textureCoordPos = position;
		position += var38;
		final int colorPos = position;
		position += indicesCount * 2;
		final int var40 = position;
		position += var20;
		final int var41 = position;
		position += var21;
		final int var8 = position;
		position += var42;
		final int var43 = position;
		position += textureAmount * 6;
		final int var37 = position;
		position += var7 * 6;
		final int var48 = position;
		position += var7 * 6;
		final int var56 = position;
		position += var7 * 2;
		final int var45 = position;
		position += var7;
		final int var46 = position;
		position += var7 * 2 + var29 * 2;
		vertexCount = verticeCount;
		this.indicesCount = indicesCount;
		verticesX = new int[verticeCount];
		verticesY = new int[verticeCount];
		verticesZ = new int[verticeCount];
		indicesX = new int[indicesCount];
		indicesY = new int[indicesCount];
		indicesZ = new int[indicesCount];
		if (modelVertexSkins == 1) {
			vertexSkins = new int[verticeCount];
		}

		if (var13 == 1) {
			renderTypes = new byte[indicesCount];
		}

		if (modelPriority == 255) {
			renderPriorities = new byte[indicesCount];
		} else {
			priority = (byte) modelPriority;
		}

		if (var50 == 1) {
			alphas = new byte[indicesCount];
		}

		if (var17 == 1) {
			indicesSkins = new int[indicesCount];
		}

		if (modelTexture == 1) {
			faceTextures = new short[indicesCount];
		}

		if (modelTexture == 1 && texturedIndicesCount > 0) {
			textureCoords = new byte[indicesCount];
		}

		colors = new short[indicesCount];
		if (texturedIndicesCount > 0) {
			texturedIndiciesX = new short[texturedIndicesCount];
			texturedIndicesY = new short[texturedIndicesCount];
			texturedIndicesZ = new short[texturedIndicesCount];

			if (var29 > 0) {
				textureColors = new short[var29];
			}
		}

		stream.seek(texturedIndicesCount);
		var24.seek(var40);
		var3.seek(var41);
		var28.seek(var8);
		var6.seek(var35);
		int vX = 0;
		int vY = 0;
		int vZ = 0;

		int vertexZOffset;
		int vertexYOffset;
		int point;
		for (point = 0; point < verticeCount; ++point) {
			final int vertexFlags = stream.getUByte();
			int vertexXOffset = 0;
			if ((vertexFlags & 1) != 0) {
				vertexXOffset = var24.getSmartShort();
			}

			vertexYOffset = 0;
			if ((vertexFlags & 2) != 0) {
				vertexYOffset = var3.getSmartShort();
			}

			vertexZOffset = 0;
			if ((vertexFlags & 4) != 0) {
				vertexZOffset = var28.getSmartShort();
			}

			verticesX[point] = vX + vertexXOffset;
			verticesY[point] = vY + vertexYOffset;
			verticesZ[point] = vZ + vertexZOffset;
			vX = verticesX[point];
			vY = verticesY[point];
			vZ = verticesZ[point];
			if (modelVertexSkins == 1) {
				vertexSkins[point] = var6.getUByte();
			}
		}

		stream.seek(colorPos);
		var24.seek(renderTypePos);
		var3.seek(priorityPos);
		var28.seek(alphaPos);
		var6.seek(indicesSkinPos);
		var55.seek(texturePos);
		var51.seek(textureCoordPos);

		for (point = 0; point < indicesCount; ++point) {
			colors[point] = (short) stream.getUShort();
			if (var13 == 1) {
				renderTypes[point] = var24.getByte();
			}

			if (modelPriority == 255) {
				renderPriorities[point] = var3.getByte();
			}

			if (var50 == 1) {
				alphas[point] = var28.getByte();
			}

			if (var17 == 1) {
				indicesSkins[point] = var6.getUByte();
			}

			if (modelTexture == 1) {
				faceTextures[point] = (short) (var55.getUShort() - 1);
			}

			if (textureCoords != null && faceTextures[point] != -1) {
				textureCoords[point] = (byte) (var51.getUByte() - 1);
			}
		}

		stream.seek(var11);
		var24.seek(var49);
		int indicesPointX = 0;
		int indicesPointY = 0;
		int indicesPointZ = 0;
		vertexYOffset = 0;

		for (vertexZOffset = 0; vertexZOffset < indicesCount; ++vertexZOffset) {
			final int numFaces = var24.getUByte();
			if (numFaces == 1) {
				indicesPointX = stream.getSmartShort() + vertexYOffset;
				indicesPointY = stream.getSmartShort() + indicesPointX;
				indicesPointZ = stream.getSmartShort() + indicesPointY;
				vertexYOffset = indicesPointZ;
				indicesX[vertexZOffset] = indicesPointX;
				indicesY[vertexZOffset] = indicesPointY;
				indicesZ[vertexZOffset] = indicesPointZ;
			}

			if (numFaces == 2) {
				indicesPointY = indicesPointZ;
				indicesPointZ = stream.getSmartShort() + vertexYOffset;
				vertexYOffset = indicesPointZ;
				indicesX[vertexZOffset] = indicesPointX;
				indicesY[vertexZOffset] = indicesPointY;
				indicesZ[vertexZOffset] = indicesPointZ;
			}

			if (numFaces == 3) {
				indicesPointX = indicesPointZ;
				indicesPointZ = stream.getSmartShort() + vertexYOffset;
				vertexYOffset = indicesPointZ;
				indicesX[vertexZOffset] = indicesPointX;
				indicesY[vertexZOffset] = indicesPointY;
				indicesZ[vertexZOffset] = indicesPointZ;
			}

			if (numFaces == 4) {
				final int var57 = indicesPointX;
				indicesPointX = indicesPointY;
				indicesPointY = var57;
				indicesPointZ = stream.getSmartShort() + vertexYOffset;
				vertexYOffset = indicesPointZ;
				indicesX[vertexZOffset] = indicesPointX;
				indicesY[vertexZOffset] = var57;
				indicesZ[vertexZOffset] = indicesPointZ;
			}
		}

		stream.seek(var43);
		var24.seek(var37);
		var3.seek(var48);
		var28.seek(var56);
		var6.seek(var45);
		var55.seek(var46);

		for (int texIndex = 0; texIndex < texturedIndicesCount; ++texIndex) {
			final int type = textureRenderTypes[texIndex] & 255;
			if (type == 0) {
				texturedIndiciesX[texIndex] = (short) stream.getUShort();
				texturedIndicesY[texIndex] = (short) stream.getUShort();
				texturedIndicesZ[texIndex] = (short) stream.getUShort();
			}

			if (type == 1) {
				texturedIndiciesX[texIndex] = (short) var24.getUShort();
				texturedIndicesY[texIndex] = (short) var24.getUShort();
				texturedIndicesZ[texIndex] = (short) var24.getUShort();
				var3.getUShort();
				var3.getUShort();
				var3.getUShort();
				var28.getUShort();
				var6.getByte();
				var55.getUShort();
			}

			if (type == 2) {
				texturedIndiciesX[texIndex] = (short) var24.getUShort();
				texturedIndicesY[texIndex] = (short) var24.getUShort();
				texturedIndicesZ[texIndex] = (short) var24.getUShort();
				var3.getUShort();
				var3.getUShort();
				var3.getUShort();
				var28.getUShort();
				var6.getByte();
				var55.getUShort();
				textureColors[texIndex] = (short) var55.getUShort();
			}

			if (type == 3) {
				texturedIndiciesX[texIndex] = (short) var24.getUShort();
				texturedIndicesY[texIndex] = (short) var24.getUShort();
				texturedIndicesZ[texIndex] = (short) var24.getUShort();
				var3.getUShort();
				var3.getUShort();
				var3.getUShort();
				var28.getUShort();
				var6.getByte();
				var55.getUShort();
			}
		}

		stream.seek(position);
		vertexZOffset = stream.getUByte();
		if (vertexZOffset != 0) {
			stream.getUShort();
			stream.getUShort();
			stream.getUShort();
			stream.getInt();
		}
	}

	private void loadV2() {
		final byte[] payload = stream.getAllBytes();
		boolean var2 = false;
		boolean var43 = false;
		final JagexBufferStream var39 = new JagexBufferStream(payload);
		final JagexBufferStream var26 = new JagexBufferStream(payload);
		final JagexBufferStream var9 = new JagexBufferStream(payload);
		final JagexBufferStream var3 = new JagexBufferStream(payload);
		stream.seek(payload.length - 18);
		final int var10 = stream.getUShort();
		final int var11 = stream.getUShort();
		final int var12 = stream.getUByte();
		final int var13 = stream.getUByte();
		final int var14 = stream.getUByte();
		final int var30 = stream.getUByte();
		final int var15 = stream.getUByte();
		final int var28 = stream.getUByte();
		final int var27 = stream.getUShort();
		final int var20 = stream.getUShort();
		stream.getUShort();
		final int var23 = stream.getUShort();
		final byte var16 = 0;
		int var46 = var16 + var10;
		final int var24 = var46;
		var46 += var11;
		final int var25 = var46;
		if (var14 == 255) {
			var46 += var11;
		}

		final int var4 = var46;
		if (var15 == 1) {
			var46 += var11;
		}

		final int var42 = var46;
		if (var13 == 1) {
			var46 += var11;
		}

		final int var37 = var46;
		if (var28 == 1) {
			var46 += var10;
		}

		final int var29 = var46;
		if (var30 == 1) {
			var46 += var11;
		}

		final int var44 = var46;
		var46 += var23;
		final int var17 = var46;
		var46 += var11 * 2;
		final int var32 = var46;
		var46 += var12 * 6;
		final int var34 = var46;
		var46 += var27;
		final int var35 = var46;
		var46 += var20;
		vertexCount = var10;
		indicesCount = var11;
		texturedIndicesCount = var12;
		verticesX = new int[var10];
		verticesY = new int[var10];
		verticesZ = new int[var10];
		indicesX = new int[var11];
		indicesY = new int[var11];
		indicesZ = new int[var11];
		if (var12 > 0) {
			textureRenderTypes = new byte[var12];
			texturedIndiciesX = new short[var12];
			texturedIndicesY = new short[var12];
			texturedIndicesZ = new short[var12];
		}

		if (var28 == 1) {
			vertexSkins = new int[var10];
		}

		if (var13 == 1) {
			renderTypes = new byte[var11];
			textureCoords = new byte[var11];
			faceTextures = new short[var11];
		}

		if (var14 == 255) {
			renderPriorities = new byte[var11];
		} else {
			priority = (byte) var14;
		}

		if (var30 == 1) {
			alphas = new byte[var11];
		}

		if (var15 == 1) {
			indicesSkins = new int[var11];
		}

		colors = new short[var11];
		stream.seek(var16);
		var39.seek(var34);
		var26.seek(var35);
		var9.seek(var46);
		var3.seek(var37);
		int var41 = 0;
		int var33 = 0;
		int var19 = 0;

		int var6;
		int var7;
		int var8;
		int var18;
		int var31;
		for (var18 = 0; var18 < var10; ++var18) {
			var8 = stream.getUByte();
			var31 = 0;
			if ((var8 & 1) != 0) {
				var31 = var39.getSmartShort();
			}

			var6 = 0;
			if ((var8 & 2) != 0) {
				var6 = var26.getSmartShort();
			}

			var7 = 0;
			if ((var8 & 4) != 0) {
				var7 = var9.getSmartShort();
			}

			verticesX[var18] = var41 + var31;
			verticesY[var18] = var33 + var6;
			verticesZ[var18] = var19 + var7;
			var41 = verticesX[var18];
			var33 = verticesY[var18];
			var19 = verticesZ[var18];
			if (var28 == 1) {
				vertexSkins[var18] = var3.getUByte();
			}
		}

		stream.seek(var17);
		var39.seek(var42);
		var26.seek(var25);
		var9.seek(var29);
		var3.seek(var4);

		for (var18 = 0; var18 < var11; ++var18) {
			colors[var18] = (short) stream.getUShort();
			if (var13 == 1) {
				var8 = var39.getUByte();
				if ((var8 & 1) == 1) {
					renderTypes[var18] = 1;
					var2 = true;
				} else {
					renderTypes[var18] = 0;
				}

				if ((var8 & 2) == 2) {
					textureCoords[var18] = (byte) (var8 >> 2);
					faceTextures[var18] = colors[var18];
					colors[var18] = 127;
					if (faceTextures[var18] != -1) {
						var43 = true;
					}
				} else {
					textureCoords[var18] = -1;
					faceTextures[var18] = -1;
				}
			}

			if (var14 == 255) {
				renderPriorities[var18] = var26.getByte();
			}

			if (var30 == 1) {
				alphas[var18] = var9.getByte();
			}

			if (var15 == 1) {
				indicesSkins[var18] = var3.getUByte();
			}
		}

		stream.seek(var44);
		var39.seek(var24);
		var18 = 0;
		var8 = 0;
		var31 = 0;
		var6 = 0;

		int var21;
		int var22;
		for (var7 = 0; var7 < var11; ++var7) {
			var22 = var39.getUByte();
			if (var22 == 1) {
				var18 = stream.getSmartShort() + var6;
				var8 = stream.getSmartShort() + var18;
				var31 = stream.getSmartShort() + var8;
				var6 = var31;
				indicesX[var7] = var18;
				indicesY[var7] = var8;
				indicesZ[var7] = var31;
			}

			if (var22 == 2) {
				var8 = var31;
				var31 = stream.getSmartShort() + var6;
				var6 = var31;
				indicesX[var7] = var18;
				indicesY[var7] = var8;
				indicesZ[var7] = var31;
			}

			if (var22 == 3) {
				var18 = var31;
				var31 = stream.getSmartShort() + var6;
				var6 = var31;
				indicesX[var7] = var18;
				indicesY[var7] = var8;
				indicesZ[var7] = var31;
			}

			if (var22 == 4) {
				var21 = var18;
				var18 = var8;
				var8 = var21;
				var31 = stream.getSmartShort() + var6;
				var6 = var31;
				indicesX[var7] = var18;
				indicesY[var7] = var21;
				indicesZ[var7] = var31;
			}
		}

		stream.seek(var32);

		for (var7 = 0; var7 < var12; ++var7) {
			textureRenderTypes[var7] = 0;
			texturedIndiciesX[var7] = (short) stream.getUShort();
			texturedIndicesY[var7] = (short) stream.getUShort();
			texturedIndicesZ[var7] = (short) stream.getUShort();
		}

		if (textureCoords != null) {
			boolean var45 = false;

			for (var22 = 0; var22 < var11; ++var22) {
				var21 = textureCoords[var22] & 255;
				if (var21 != 255) {
					if ((texturedIndiciesX[var21] & '\uffff') == indicesX[var22] && (texturedIndicesY[var21] & '\uffff') == indicesY[var22] && (texturedIndicesZ[var21] & '\uffff') == indicesZ[var22]) {
						textureCoords[var22] = -1;
					} else {
						var45 = true;
					}
				}
			}

			if (!var45) {
				textureCoords = null;
			}
		}

		if (!var43) {
			faceTextures = null;
		}

		if (!var2) {
			renderTypes = null;
		}
	}

	public CacheModelConfig(final CacheModelConfig[] models, final int id) {
		this.id = id;
		boolean hasRenderTypes = false;
		boolean hasRenderPriorities = false;
		boolean hasAlphas = false;
		boolean hasIndiceSkins = false;
		boolean hasFaceTexures = false;
		boolean hasTextureCoords = false;
		this.vertexCount = 0;
		this.indicesCount = 0;
		this.priority = -1;

		for(final CacheModelConfig model : models) {
			if(model != null) {
				this.vertexCount += model.vertexCount;
				this.indicesCount += model.indicesCount;
				if(model.renderPriorities != null) {
					hasRenderPriorities = true;
				} else {
					if(this.priority == -1) {
						this.priority = model.priority;
					}

					if(this.priority != model.priority) {
						hasRenderPriorities = true;
					}
				}

				hasRenderTypes |= model.renderTypes != null;
				hasAlphas |= model.alphas != null;
				hasIndiceSkins |= model.indicesSkins != null;
				hasFaceTexures |= model.faceTextures != null;
				hasTextureCoords |= model.textureCoords != null;
			}
		}

		this.verticesX = new int[this.vertexCount];
		this.verticesY = new int[this.vertexCount];
		this.verticesZ = new int[this.vertexCount];
		this.vertexSkins = new int[this.vertexCount];
		this.indicesX = new int[this.indicesCount];
		this.indicesY = new int[this.indicesCount];
		this.indicesZ = new int[this.indicesCount];
		if(hasRenderTypes) {
			this.renderTypes = new byte[this.indicesCount];
		}

		if(hasRenderPriorities) {
			this.renderPriorities = new byte[this.indicesCount];
		}

		if(hasAlphas) {
			this.alphas = new byte[this.indicesCount];
		}

		if(hasIndiceSkins) {
			this.indicesSkins = new int[this.indicesCount];
		}

		if(hasFaceTexures) {
			this.faceTextures = new short[this.indicesCount];
		}

		if(hasTextureCoords) {
			this.textureCoords = new byte[this.indicesCount];
		}

		this.colors = new short[this.indicesCount];

		this.vertexCount = 0;
		this.indicesCount = 0;

		for(final CacheModelConfig model :  models) {
			if(model != null) {
				int var5;
				for(var5 = 0; var5 < model.indicesCount; ++var5) {
					if(hasRenderTypes && model.renderTypes != null) {
						this.renderTypes[this.indicesCount] = model.renderTypes[var5];
					}

					if(hasRenderPriorities) {
						if(model.renderPriorities != null) {
							this.renderPriorities[this.indicesCount] = model.renderPriorities[var5];
						} else {
							this.renderPriorities[this.indicesCount] = model.priority;
						}
					}

					if(hasAlphas && model.alphas != null) {
						this.alphas[this.indicesCount] = model.alphas[var5];
					}

					if(hasIndiceSkins && model.indicesSkins != null) {
						this.indicesSkins[this.indicesCount] = model.indicesSkins[var5];
					}

					if(hasFaceTexures) {
						if(model.faceTextures != null) {
							this.faceTextures[this.indicesCount] = model.faceTextures[var5];
						} else {
							this.faceTextures[this.indicesCount] = -1;
						}
					}

					this.colors[this.indicesCount] = model.colors[var5];
					this.indicesX[this.indicesCount] = this.calculateIndex(model, model.indicesX[var5]);
					this.indicesY[this.indicesCount] = this.calculateIndex(model, model.indicesY[var5]);
					this.indicesZ[this.indicesCount] = this.calculateIndex(model, model.indicesZ[var5]);
					this.indicesCount++;
				}

				if (this.textureRenderTypes != null && this.texturedIndiciesX != null && this.texturedIndicesY != null && this.texturedIndicesZ != null) {
					for (var5 = 0; var5 < model.texturedIndicesCount; ++var5) {
						final byte var12 = this.textureRenderTypes[this.texturedIndicesCount] = model.textureRenderTypes[var5];
						if (var12 == 0) {
							this.texturedIndiciesX[this.texturedIndicesCount] = (short) this.calculateIndex(model, model.texturedIndiciesX[var5]);
							this.texturedIndicesY[this.texturedIndicesCount] = (short) this.calculateIndex(model, model.texturedIndicesY[var5]);
							this.texturedIndicesZ[this.texturedIndicesCount] = (short) this.calculateIndex(model, model.texturedIndicesZ[var5]);
						}

						if (var12 >= 1 && var12 <= 3) {
							this.texturedIndiciesX[this.texturedIndicesCount] = model.texturedIndiciesX[var5];
							this.texturedIndicesY[this.texturedIndicesCount] = model.texturedIndicesY[var5];
							this.texturedIndicesZ[this.texturedIndicesCount] = model.texturedIndicesZ[var5];
						}

						this.texturedIndicesCount++;
					}
				}
			}
		}

	}


	final int calculateIndex(final CacheModelConfig model, final int idx) {
		int texturedIdx = -1;
		final int var7 = model.verticesX[idx];
		final int var5 = model.verticesY[idx];
		final int var6 = model.verticesZ[idx];

		for(int var4 = 0; var4 < this.vertexCount; ++var4) {
			if(var7 == this.verticesX[var4] && var5 == this.verticesY[var4] && var6 == this.verticesZ[var4]) {
				texturedIdx = var4;
				break;
			}
		}

		if(texturedIdx == -1) {
			this.verticesX[this.vertexCount] = var7;
			this.verticesY[this.vertexCount] = var5;
			this.verticesZ[this.vertexCount] = var6;
			if(model.vertexSkins != null) {
				this.vertexSkins[this.vertexCount] = model.vertexSkins[idx];
			}

			texturedIdx = this.vertexCount++;
		}

		return texturedIdx;
	}

}
