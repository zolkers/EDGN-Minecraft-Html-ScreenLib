package com.edgn.example.screens;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.data.collections.ObservableList;
import com.edgn.edml.minecraft.screen.EdmlScreenFactory;
import com.edgn.edml.minecraft.screen.screens.StandardResourceEdmlScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public final class ScreenOpener {

    public static void openTestScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> {
                var screen = new UnifiedTestScreen();
                client.setScreen(screen);
            });
        }
    }

    private static class UnifiedTestScreen extends StandardResourceEdmlScreen {
        private Timer updateTimer;

        public UnifiedTestScreen() {
            super(Text.literal("EDGN Framework - All Tests"), "unified_test", "unified_test_styles");
        }

        @Override
        protected void initializeData() {
            HTMLMyScreen.LOGGER.info("Initializing unified test screen with all demos");

            getBindingContext().setValue("mainTitle", "EDGN Framework Test Suite");
            getBindingContext().setValue("currentTime", getCurrentTime());
            getBindingContext().setValue("status", "All systems operational");

            setupVirtualListDemo();
            setupMarginPaddingDemo();
            setupDataBindingDemo();
            setupRealtimeUpdates();
        }

        private void setupVirtualListDemo() {
            HTMLMyScreen.LOGGER.info("Setting up virtual list demo");

            getBindingContext().setValue("virtualListTitle", "Virtual List Demo (50,000 items)");

            ObservableList<String> largeList = getBindingContext().createLargeTestList("largeItems", 50000);
            HTMLMyScreen.LOGGER.info("Created large list with {} items", largeList.size());
            getBindingContext().setValue("largeItemCount", largeList.size());

            ObservableList<String> smallList = getBindingContext().createObservableList("smallItems");
            for (int i = 1; i <= 20; i++) {
                smallList.add("Quick item " + i);
            }
            HTMLMyScreen.LOGGER.info("Created small list with {} items", smallList.size());
            getBindingContext().setValue("smallItemCount", smallList.size());

            Object largeCheck = getBindingContext().getValue("largeItems");
            Object smallCheck = getBindingContext().getValue("smallItems");
            HTMLMyScreen.LOGGER.info("Verification - largeItems: {}, smallItems: {}",
                    largeCheck != null, smallCheck != null);
        }

        private void setupMarginPaddingDemo() {
            getBindingContext().setValue("spacingTitle", "Margin & Padding Properties Test");
            getBindingContext().setValue("spacingInfo", "Individual margin-top, margin-bottom, padding-left, etc.");
        }

        private void setupDataBindingDemo() {
            getBindingContext().setValue("bindingTitle", "Data Binding Demo");
            getBindingContext().setValue("counter", 0);
            getBindingContext().setValue("dynamicText", "This text updates automatically");

            ObservableList<String> notifications = getBindingContext().createObservableList("notifications");
            notifications.add("System initialized");
            notifications.add("Data binding active");
            notifications.add("Virtual lists ready");
        }

        private void setupRealtimeUpdates() {
            updateTimer = new Timer(true);

            updateTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getBindingContext().setValue("currentTime", getCurrentTime());
                }
            }, 1000, 1000);

            updateTimer.scheduleAtFixedRate(new TimerTask() {
                private int counter = 0;

                @Override
                public void run() {
                    counter++;
                    getBindingContext().setValue("counter", counter);

                    String[] statuses = {"Operational", "Processing", "Updating", "Ready"};
                    getBindingContext().setValue("status", statuses[counter % statuses.length]);

                    getBindingContext().setValue("dynamicText", "Updated " + counter + " times");

                    ObservableList<String> notifications = getBindingContext().getObservableList("notifications");
                    if (notifications != null) {
                        if (counter % 3 == 0 && notifications.size() < 15) {
                            notifications.add("Auto update " + counter);
                        } else if (counter % 7 == 0 && notifications.size() > 5) {
                            notifications.removeLast();
                        }
                    }

                    ObservableList<String> smallList = getBindingContext().getObservableList("smallItems");
                    if (smallList != null && counter % 5 == 0) {
                        if (smallList.size() > 5) {
                            smallList.set(5, "Modified item " + counter);
                        }
                    }
                }
            }, 2000, 2000);
        }

        private String getCurrentTime() {
            return java.time.LocalTime.now().toString().substring(0, 8);
        }

        @Override
        public void close() {
            if (updateTimer != null) {
                updateTimer.cancel();
                HTMLMyScreen.LOGGER.info("Unified test screen closed");
            }
            super.close();
        }
    }
}