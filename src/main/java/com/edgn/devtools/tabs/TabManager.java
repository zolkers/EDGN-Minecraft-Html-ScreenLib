// FILE: src/main/java/com/edgn/devtools/tabs/TabManager.java
package com.edgn.devtools.tabs;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.screen.EdmlScreen;

import java.util.ArrayList;
import java.util.List;

public final class TabManager {
    private final List<IConsoleTab> tabs = new ArrayList<>();
    private int activeTabIndex = 0;

    public TabManager(EdmlScreen screen) {
        tabs.add(new SettingsTab());
        tabs.add(new DebugTab(screen));

        tabs.getFirst().onActivated();

        HTMLMyScreen.LOGGER.info("TabManager initialized with {} tabs", tabs.size());
    }
    public IConsoleTab getActiveTab() {
        if (activeTabIndex >= 0 && activeTabIndex < tabs.size()) {
            return tabs.get(activeTabIndex);
        }
        return null;
    }

    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTab(int index) {
        if (index < 0 || index >= tabs.size()) {
            return;
        }

        if (activeTabIndex == index) {
            return;
        }

        if (activeTabIndex >= 0 && activeTabIndex < tabs.size()) {
            tabs.get(activeTabIndex).onDeactivated();
        }

        activeTabIndex = index;
        tabs.get(activeTabIndex).onActivated();
    }

    public int getTabCount() {
        return tabs.size();
    }

    public String getTabName(int index) {
        if (index >= 0 && index < tabs.size()) {
            return tabs.get(index).getName();
        }
        return "";
    }

    public boolean handleClick(double mouseX, double mouseY, int button) {

        IConsoleTab activeTab = getActiveTab();
        if (activeTab != null) {
            return activeTab.handleClick(mouseX, mouseY, button);
        }
        return false;
    }

    public boolean handleScroll(double mouseX, double mouseY, double amount) {
        IConsoleTab activeTab = getActiveTab();
        if (activeTab != null) {
            return activeTab.handleScroll(mouseX, mouseY, amount);
        }
        return false;
    }
}