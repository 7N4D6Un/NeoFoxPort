package com.fletime.neofoxport;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.Colors;

/**
 * NeoFoxPort - 在启动界面添加转圈的小狐狸动画
 */
public class NeoFoxPort implements ClientModInitializer {
	public static final String MOD_ID = "neofoxport";
	
	private static final Identifier[] FOX_TEXTURES = new Identifier[28];
	private static final int TOTAL_FRAMES = 28;
	private static final int DISPLAY_WIDTH = 91;  // 151 * 0.6
	private static final int DISPLAY_HEIGHT = 77; // 128 * 0.6
	private static final int MARGIN_RIGHT = 10;
	private static final int MARGIN_BOTTOM = 16;
	
	private static long lastFrameTime = 0;
	private static int currentFrame = 0;
	private static boolean texturesRegistered = false;
	
	static {
		for (int i = 0; i < 28; i++) {
			FOX_TEXTURES[i] = Identifier.of("neofoxport", "textures/fox_running_" + (i + 1) + ".png");
		}
	}
	
	@Override
	public void onInitializeClient() {
		// 客户端初始化完成
	}
	
	public static void registerTextures(TextureManager textureManager) {
		if (!texturesRegistered) {
			for (int i = 0; i < TOTAL_FRAMES; i++) {
				textureManager.registerTexture(FOX_TEXTURES[i], new BuiltinResourceTexture(FOX_TEXTURES[i]));
			}
			texturesRegistered = true;
		}
	}
	
	public static void render(MinecraftClient client, DrawContext context, float delta) {
		registerTextures(client.getTextureManager());
		
		// 每60ms更新一帧
		long currentTime = System.nanoTime() / 1_000_000;
		if (lastFrameTime == 0) {
			lastFrameTime = currentTime;
		}
		
		if (currentTime - lastFrameTime >= 60) {
			currentFrame = (currentFrame + 1) % TOTAL_FRAMES;
			lastFrameTime = currentTime;
		}
		
		// 计算位置（右下角）
		int screenWidth = client.getWindow().getScaledWidth();
		int screenHeight = client.getWindow().getScaledHeight();
		int foxX = screenWidth - DISPLAY_WIDTH - MARGIN_RIGHT;
		int foxY = screenHeight - DISPLAY_HEIGHT - MARGIN_BOTTOM;
		
		// 绘制狐狸
		context.drawTexture(
			RenderPipelines.GUI_TEXTURED,
			FOX_TEXTURES[currentFrame],
			foxX, foxY,
			0, 0,
			DISPLAY_WIDTH, DISPLAY_HEIGHT,
			DISPLAY_WIDTH, DISPLAY_HEIGHT,
			ColorHelper.withAlpha(1.0f, Colors.WHITE)
		);
	}
	
	private static class BuiltinResourceTexture extends net.minecraft.client.texture.ResourceTexture {
		public BuiltinResourceTexture(Identifier location) {
			super(location);
		}
		
		@Override
		public net.minecraft.client.texture.TextureContents loadContents(net.minecraft.resource.ResourceManager resourceManager) {
			final String path = "assets/" + getId().getNamespace() + "/" + getId().getPath();
			
			try (java.io.InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
				if (input != null) {
					return new net.minecraft.client.texture.TextureContents(
						net.minecraft.client.texture.NativeImage.read(input), 
						new net.minecraft.client.resource.metadata.TextureResourceMetadata(true, true)
					);
				}
			} catch (java.io.IOException e) {
				// 忽略错误
			}
			return net.minecraft.client.texture.TextureContents.createMissing();
		}
	}
}