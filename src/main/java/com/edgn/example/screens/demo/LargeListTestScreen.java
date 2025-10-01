package com.edgn.example.screens.demo;

import com.edgn.edml.data.binding.BindingContext;
import com.edgn.edml.data.collections.ObservableList;
import com.edgn.edml.minecraft.screen.screens.StandardResourceEdmlScreen;
import net.minecraft.text.Text;

public class LargeListTestScreen extends StandardResourceEdmlScreen {

    public LargeListTestScreen() {
        super(Text.literal("Large List Test"), "large_list_test");
    }

    @Override
    protected void initializeData() {
        BindingContext context = getBindingContext();
        
        ObservableList<String> items = context.createObservableList("testItems");
        
        System.out.println("Generating 500,000 test items...");
        for (int i = 1; i <= 500_000; i++) {
            items.add("Item #" + i);
        }
        System.out.println("Items generated!");
        
        context.setValue("itemCount", String.valueOf(items.size()));
    }
}