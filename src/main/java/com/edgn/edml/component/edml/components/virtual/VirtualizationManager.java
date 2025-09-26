package com.edgn.edml.component.edml.components.virtual;

import com.edgn.edml.minecraft.MinecraftRenderContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class VirtualizationManager {
    private static final VirtualizationManager INSTANCE = new VirtualizationManager();
    
    private final List<VirtualComponent> virtualComponents = new CopyOnWriteArrayList<>();
    private boolean virtualizationEnabled = true;
    private int cullingDistance = 100;
    
    public static VirtualizationManager getInstance() {
        return INSTANCE;
    }
    
    public void registerComponent(VirtualComponent component) {
        virtualComponents.add(component);
    }
    
    public void unregisterComponent(VirtualComponent component) {
        virtualComponents.remove(component);
    }

    @SuppressWarnings("unused")
    public void updateAll(MinecraftRenderContext context) {
        if (!virtualizationEnabled) return;

        for (VirtualComponent component : virtualComponents) {
            component.updateVirtualization(context);
        }
    }

    @SuppressWarnings("unused")
    public void setVirtualizationEnabled(boolean enabled) {
        this.virtualizationEnabled = enabled;
    }

    @SuppressWarnings("unused")
    public void setCullingDistance(int distance) {
        this.cullingDistance = distance;
    }
    
    public int getCullingDistance() {
        return cullingDistance;
    }
}