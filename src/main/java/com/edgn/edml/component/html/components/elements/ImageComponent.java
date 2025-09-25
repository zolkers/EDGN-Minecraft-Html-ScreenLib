package com.edgn.edml.component.html.components.elements;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.component.html.EdmlEnum;
import com.edgn.edml.component.html.components.EdssAwareComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.edml.minecraft.context.FabricRenderContext;
import net.minecraft.util.Identifier;

import java.util.Set;

public final class ImageComponent extends EdssAwareComponent implements SelfSizingComponent {
    private static final Set<String> IMAGE_ATTRIBUTES = Set.of("src", "alt", "width", "height");
    private String src = "";
    private String alt = "";
    private int imageWidth = 0;
    private int imageHeight = 0;
    private int rotation = 0;
    private boolean flipHorizontal = false;
    private int tintColor = 0xFFFFFFFF;
    private Identifier textureId = null;
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;

    public ImageComponent() {
        super(EdmlEnum.IMG.getTagName(), IMAGE_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        src = getAttribute(TagAttribute.SRC.getProperty(), "");
        alt = getAttribute(TagAttribute.ALT.getProperty(), "");
        
        String widthAttr = getAttribute(TagAttribute.WIDTH.getProperty(), "");
        String heightAttr = getAttribute(TagAttribute.HEIGHT.getProperty(), "");
        
        if (!widthAttr.isEmpty()) {
            try {
                imageWidth = Integer.parseInt(widthAttr);
            } catch (NumberFormatException e) {
                imageWidth = 0;
            }
        }
        
        if (!heightAttr.isEmpty()) {
            try {
                imageHeight = Integer.parseInt(heightAttr);
            } catch (NumberFormatException e) {
                imageHeight = 0;
            }
        }

        loadTexture();
    }

    private void loadTexture() {
        if (src.isEmpty()) return;

        try {
            if (src.startsWith("http://") || src.startsWith("https://")) {
                System.out.println("External URL detected: " + src + " - showing alt text fallback");
                textureId = null;
                return;
            }
            if (src.startsWith("/")) {
                textureId = Identifier.of(HTMLMyScreen.MOD_ID, "img" + src);
            } else {
                textureId = Identifier.of(HTMLMyScreen.MOD_ID, "img/" + src);
            }

        } catch (Exception e) {
            System.err.println("Failed to load image: " + src + " - " + e.getMessage());
            textureId = null;
        }
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        if (textureId != null && context instanceof FabricRenderContext fabricContext) {
            fabricContext.drawImage(textureId, x, y, x + width, y + height, rotation, flipHorizontal, tintColor);
        } else if (textureId == null && !alt.isEmpty()) {
            context.drawText(alt, x + 5, y + height/2 - 8, 0xFF666666);
        }
    }

    @Override
    public int calculateOptimalWidth(MinecraftRenderContext context) {
        if (imageWidth > 0) return imageWidth;
        return DEFAULT_WIDTH;
    }

    @Override
    public int calculateOptimalHeight(MinecraftRenderContext context, int availableWidth) {
        if (imageHeight > 0) return imageHeight;
        
        if (imageWidth > 0 && availableWidth > 0) {
            return (DEFAULT_HEIGHT * availableWidth) / imageWidth;
        }
        
        return DEFAULT_HEIGHT;
    }
    
    public void setRotation(int rotation) {
        this.rotation = ((rotation % 360) + 360) % 360;
    }
    
    public int getRotation() {
        return rotation;
    }
    
    public void setFlipHorizontal(boolean flip) {
        this.flipHorizontal = flip;
    }
    
    public boolean isFlipHorizontal() {
        return flipHorizontal;
    }
    
    public void setTintColor(int color) {
        this.tintColor = color;
    }
    
    public int getTintColor() {
        return tintColor;
    }
    
    public String getSrc() {
        return src;
    }
    
    public String getAlt() {
        return alt;
    }

    @Override
    public void addChild(EdmlComponent child) {
        throw new UnsupportedOperationException("ImageComponent cannot have children");
    }
}