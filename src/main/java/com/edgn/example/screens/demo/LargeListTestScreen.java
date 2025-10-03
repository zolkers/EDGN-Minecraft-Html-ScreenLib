package com.edgn.example.screens.demo;

import com.edgn.edml.core.events.action.ActionRegistry;
import com.edgn.edml.data.binding.BindingContext;
import com.edgn.edml.data.collections.ObservableList;
import com.edgn.edml.screen.screens.StandardResourceEdmlScreen;
import net.minecraft.text.Text;

public class LargeListTestScreen extends StandardResourceEdmlScreen {

    public LargeListTestScreen() {
        super(Text.literal("Large List Test"), "large_list_test");
    }

    @Override
    protected void initializeData() {
        BindingContext context = getBindingContext();

        ObservableList<String> items = context.createObservableList("testItems");

        for (int i = 1; i <= 500_000; i++) {
            items.add("Item #" + i);
        }

        context.setValue("itemCount", String.valueOf(items.size()));

        ActionRegistry registry = ActionRegistry.getInstance();

        registry.registerAction("editItem", event -> {
            this.close();
        });
    }
}