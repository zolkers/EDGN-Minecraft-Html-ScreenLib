package com.edgn.edml.dom.components.containers;

import com.edgn.edml.annotations.KeepEmpty;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.dom.EdmlEnum;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.utils.ColorUtils;

import java.util.Set;

public final class ArticleComponent extends BaseContainer {

    private static final Set<String> ARTICLE_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(),
            TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(),
            TagAttribute.ROLE.getProperty()
    );

    public ArticleComponent() {
        super(EdmlEnum.ARTICLE.getTagName(), ARTICLE_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        if (backgroundColor == 0x00000000) {
            if (hasClass(TagAttribute.DARK_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#2c3e50"));
            } else if (hasClass(TagAttribute.LIGHT_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#ffffff"));
            }
        }
    }

    @Override
    @KeepEmpty
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {}
}