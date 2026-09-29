package com.fletime.neofoxport;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.ReloadableTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Util;

public class NeoFoxPort {
	public static final String MOD_ID = "neofoxport";

	private static final Identifier FOX_TEXTURE = Identifier.fromNamespaceAndPath(MOD_ID, "textures/fox_running.png");
	private static final int FRAME_COUNT = 28;
	private static final int DISPLAY_WIDTH = 151;
	private static final int DISPLAY_HEIGHT = 128;
	private static final int LAYOUT_WIDTH = 854;
	private static final int LAYOUT_HEIGHT = 480;
	private static final int MARGIN_RIGHT = 10;
	private static final int REM_SIZE = 34;
	private static final int MARGIN_BOTTOM = REM_SIZE;
	private static final long FRAME_TIME_MS = 50;

	public static void registerTextures(TextureManager textureManager) {
		textureManager.registerAndLoad(FOX_TEXTURE, new FoxTexture(FOX_TEXTURE));
	}

	public static void render(GuiGraphicsExtractor graphics) {
		int frame = (int) (Util.getMillis() / FRAME_TIME_MS % FRAME_COUNT);
		float vUnit = 1.0f / FRAME_COUNT;
		float v0 = frame * vUnit;
		float v1 = (frame + 1) * vUnit;

		int guiWidth = graphics.guiWidth();
		int guiHeight = graphics.guiHeight();
		float scale;
		float offsetX;
		float offsetY;
		if (guiWidth / (float) guiHeight > LAYOUT_WIDTH / (float) LAYOUT_HEIGHT) {
			scale = guiHeight / (float) LAYOUT_HEIGHT;
			offsetX = (guiWidth - LAYOUT_WIDTH * scale) / 2f;
			offsetY = 0f;
		} else {
			scale = guiWidth / (float) LAYOUT_WIDTH;
			offsetX = 0f;
			offsetY = (guiHeight - LAYOUT_HEIGHT * scale) / 2f;
		}

		int foxX = Math.round(offsetX + (LAYOUT_WIDTH - MARGIN_RIGHT - DISPLAY_WIDTH) * scale);
		int foxY = Math.round(offsetY + (LAYOUT_HEIGHT - MARGIN_BOTTOM - DISPLAY_HEIGHT) * scale);
		int foxWidth = Math.round(DISPLAY_WIDTH * scale);
		int foxHeight = Math.round(DISPLAY_HEIGHT * scale);

		graphics.blit(FOX_TEXTURE, foxX, foxY, foxX + foxWidth, foxY + foxHeight, 0.0f, 1.0f, v0, v1);
	}

	private static class FoxTexture extends ReloadableTexture {
		public FoxTexture(Identifier resourceId) {
			super(resourceId);
		}

		@Override
		public TextureContents loadContents(ResourceManager resourceManager) throws IOException {
			final String path = "assets/" + resourceId().getNamespace() + "/" + resourceId().getPath();

			try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
				if (input != null) {
					return new TextureContents(
						NativeImage.read(input),
						new TextureMetadataSection(false, false, MipmapStrategy.MEAN, 0.0f)
					);
				}
			}
			return TextureContents.createMissing();
		}
	}
}
