# EDGN Virtual List Data Binding Documentation

## Overview

Virtual List Data Binding provides high-performance rendering of large collections (50,000+ items) by rendering only visible items and automatically handling scroll events. The system integrates with ObservableList for reactive updates and includes built-in scroll management.

## Core Architecture

### ObservableList

Reactive list container that triggers UI updates when data changes.

```java
ObservableList<String> items = bindingContext.createObservableList("myItems");
```

#### Key Methods

- `add(T item)` - Add single item with UI notification
- `addRange(Collection<T> items)` - Efficient bulk addition
- `set(int index, T item)` - Update item with automatic re-render
- `remove(int index)` - Remove with UI update
- `clear()` - Clear all items
- `size()` - Get current item count

### VirtualListBinding

Internal system that manages virtualization, scrolling, and template processing. Created automatically when `data-for` attributes are detected.

## EDML Syntax

### Basic Virtual List Structure

```xml
<div id="product-list" 
     data-for="product in products"
     data-template="&lt;div class='product-item'&gt;&lt;h3&gt;{{product}}&lt;/h3&gt;&lt;span&gt;Index: {{index}}&lt;/span&gt;&lt;/div&gt;"
     data-item-height="60"
     class="scrollable-list">
</div>
```

### Required Attributes

- **`data-for`** - Binding syntax: `"itemVariable in listPath"`
- **`data-template`** - HTML template for each item (HTML-encoded)
- **`data-item-height`** - Fixed height per item in pixels

### Optional Attributes

- **`id`** - Unique identifier for programmatic control
- **`class`** - CSS styling classes
- **`style`** - Inline CSS styles

## Template System

### Available Variables

- **`{{item}}`** - Current item value from the list
- **`{{index}}`** - Zero-based index of the current item

### Template Examples

```xml
<!-- Simple text display -->
data-template="&lt;div&gt;{{item}}&lt;/div&gt;"

<!-- With index counter -->
data-template="&lt;div&gt;Item {{index}}: {{item}}&lt;/div&gt;"

<!-- Complex layout with CSS classes -->
data-template="&lt;div class='list-row'&gt;&lt;div class='content'&gt;{{item}}&lt;/div&gt;&lt;div class='badge'&gt;#{{index}}&lt;/div&gt;&lt;/div&gt;"

<!-- Product listing example -->
data-template="&lt;div class='product'&gt;&lt;h4&gt;{{item}}&lt;/h4&gt;&lt;p&gt;Product ID: {{index}}&lt;/p&gt;&lt;/div&gt;"
```

### HTML Entity Encoding

Templates in EDML attributes must use HTML entities:
- `<` becomes `&lt;`
- `>` becomes `&gt;`
- `"` becomes `&quot;`
- `&` becomes `&amp;`

## Data Setup

### Creating Observable Lists

```java
@Override
protected void initializeData() {
    // Small reactive list
    ObservableList<String> notifications = getBindingContext().createObservableList("notifications");
    notifications.add("System started");
    notifications.add("Loading data...");
    
    // Large performance list
    ObservableList<String> products = getBindingContext().createLargeTestList("products", 50000);
    
    // Custom object list
    ObservableList<User> users = getBindingContext().createObservableList("users");
    users.add(new User("Alice", "Administrator"));
    users.add(new User("Bob", "Editor"));
}
```

### Efficient Bulk Operations

```java
// Efficient bulk addition
List<String> newProducts = Arrays.asList("Product A", "Product B", "Product C");
productList.addRange(newProducts);

// Avoid multiple individual adds for performance
// WRONG: productList.add("A"); productList.add("B"); productList.add("C");
// CORRECT: productList.addRange(Arrays.asList("A", "B", "C"));
```

## Performance Characteristics

### Virtualization Benefits

- **Constant Memory**: Only 10-20 DOM components regardless of list size
- **Constant Render Time**: O(visible_items) not O(total_items)
- **Smooth Scrolling**: 60fps scrolling through any list size
- **Efficient Updates**: Only visible items re-render on data changes

### Real Performance Numbers

```java
// 50,000 items test case
ObservableList<String> bigList = getBindingContext().createLargeTestList("test", 50000);

// Memory usage: ~15 components (not 50,000)
// Initial render: <50ms regardless of list size
// Scroll performance: 60fps through entire list
// Data update: Only visible items affected
```

## Scroll Integration

### Automatic Scroll Management

Virtual lists automatically register with the ScrollManager for independent scrolling:

```java
// Scroll is handled automatically by the framework
// Each virtual list gets its own scroll area
// Page scroll and list scroll work independently
```

### Programmatic Scroll Control

```java
// Scroll specific list by ID
DataBindingEngine bindingEngine = getBindingEngine();
bindingEngine.scrollList("product-list", scrollOffset);

// Get scroll position
VirtualListBinding binding = bindingEngine.getVirtualListBinding("product-list");
int currentOffset = binding.getScrollOffset();
```

### Mouse Scroll Events

Built-in integration with mouse scroll in StandardResourceEdmlScreen:

```java
// Automatically handled - no additional code needed
// Mouse over virtual list → scrolls that list
// Mouse elsewhere → scrolls page body
```

## Reactive Data Updates

### Real-time List Modifications

```java
ObservableList<String> items = getBindingContext().getObservableList("items");

// Add item - automatically appears in UI
items.add("New product " + System.currentTimeMillis());

// Update existing - automatically re-renders visible item
if (items.size() > 10) {
    items.set(10, "UPDATED: " + items.get(10));
}

// Remove item - automatically disappears from UI
if (items.size() > 0) {
    items.remove(0);
}

// Bulk operations trigger single UI update
items.addRange(Arrays.asList("Bulk 1", "Bulk 2", "Bulk 3"));
```

### Change Event Handling

```java
items.addListener(new ObservableList.ListChangeListener<String>() {
    @Override
    public void onItemAdded(int index, String item) {
        HTMLMyScreen.LOGGER.info("Added at {}: {}", index, item);
        updateItemCountDisplay();
    }
    
    @Override
    public void onItemChanged(int index, String oldItem, String newItem) {
        HTMLMyScreen.LOGGER.info("Changed at {}: {} → {}", index, oldItem, newItem);
    }
    
    @Override
    public void onRangeAdded(int startIndex, Collection<String> items) {
        HTMLMyScreen.LOGGER.info("Bulk added {} items starting at {}", items.size(), startIndex);
    }
    
    // ... other events
});
```

## Complete Implementation Example

### Java Screen Setup

```java
public class ProductCatalogScreen extends StandardResourceEdmlScreen {
    
    public ProductCatalogScreen() {
        super(Text.literal("Product Catalog"), "product_catalog", "catalog_styles");
    }
    
    @Override
    protected void initializeData() {
        // Main product list - large dataset
        ObservableList<String> products = getBindingContext().createObservableList("products");
        for (int i = 1; i <= 25000; i++) {
            products.add("Product " + i + " - Category " + (i % 10) + " - $" + (i * 12.99));
        }
        
        // Recent orders - small dynamic list
        ObservableList<String> recentOrders = getBindingContext().createObservableList("recentOrders");
        recentOrders.add("Order #1001 - Processing");
        recentOrders.add("Order #1002 - Shipped");
        recentOrders.add("Order #1003 - Delivered");
        
        // Set UI metadata
        getBindingContext().setValue("productCount", products.size());
        getBindingContext().setValue("catalogTitle", "Product Catalog - " + products.size() + " items");
        getBindingContext().setValue("lastUpdate", LocalTime.now().toString());
        
        // Real-time updates simulation
        Timer updateTimer = new Timer(true);
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            private int orderCounter = 1004;
            
            @Override
            public void run() {
                // Add new order
                recentOrders.add("Order #" + orderCounter + " - New");
                orderCounter++;
                
                // Remove old orders (keep list manageable)
                if (recentOrders.size() > 10) {
                    recentOrders.remove(0);
                }
                
                // Update timestamp
                getBindingContext().setValue("lastUpdate", LocalTime.now().toString());
                
                // Occasionally modify product (simulate price update)
                if (orderCounter % 5 == 0 && products.size() > 100) {
                    int randomIndex = (int) (Math.random() * 100);
                    String currentProduct = products.get(randomIndex);
                    String updatedProduct = currentProduct.replaceAll("\\$[0-9.]+", "$" + (Math.random() * 100 + 10));
                    products.set(randomIndex, "SALE: " + updatedProduct);
                }
            }
        }, 3000, 3000);
    }
}
```

### EDML Template

```xml
<body class="catalog-screen">
    <header class="catalog-header">
        <div data-text="{{catalogTitle}}" class="main-title"></div>
        <div data-text="Last updated: {{lastUpdate}}" class="update-time"></div>
    </header>
    
    <main class="catalog-content">
        <!-- Main product list - large virtual list -->
        <section class="products-section">
            <div data-text="All Products" class="section-title"></div>
            <div id="products-list"
                 data-for="product in products"
                 data-template="&lt;div class='product-card'&gt;&lt;div class='product-name'&gt;{{product}}&lt;/div&gt;&lt;div class='product-position'&gt;Position {{index}}&lt;/div&gt;&lt;/div&gt;"
                 data-item-height="80"
                 class="products-virtual-list">
            </div>
        </section>
        
        <!-- Recent orders - small dynamic list -->
        <aside class="orders-sidebar">
            <div data-text="Recent Orders" class="section-title"></div>
            <div id="orders-list"
                 data-for="order in recentOrders"
                 data-template="&lt;div class='order-item'&gt;&lt;span class='order-text'&gt;{{order}}&lt;/span&gt;&lt;span class='order-time'&gt;{{index}}m ago&lt;/span&gt;&lt;/div&gt;"
                 data-item-height="40"
                 class="orders-virtual-list">
            </div>
        </aside>
    </main>
</body>
```

### CSS Styling

```css
.catalog-screen {
    background-color: #f8f9fa;
    margin: 0;
    padding: 0;
}

.catalog-header {
    background: linear-gradient(90deg, #4a90e2, #357abd);
    color: white;
    padding: 20px;
    text-align: center;
}

.catalog-content {
    display: flex;
    padding: 20px;
    gap: 20px;
}

.products-section {
    flex: 1;
}

.products-virtual-list {
    height: 500px;
    border: 1px solid #dee2e6;
    border-radius: 8px;
    overflow-y: auto;
    background-color: white;
}

.product-card {
    height: 80px;
    padding: 15px;
    border-bottom: 1px solid #f1f3f4;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.product-card:hover {
    background-color: #f8f9fa;
}

.product-name {
    font-weight: 500;
    color: #212529;
}

.product-position {
    font-size: 12px;
    color: #6c757d;
    background-color: #e9ecef;
    padding: 4px 8px;
    border-radius: 12px;
}

.orders-sidebar {
    width: 300px;
}

.orders-virtual-list {
    height: 300px;
    border: 1px solid #dee2e6;
    border-radius: 8px;
    overflow-y: auto;
    background-color: white;
}

.order-item {
    height: 40px;
    padding: 10px 15px;
    border-bottom: 1px solid #f1f3f4;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.order-text {
    font-size: 14px;
    color: #495057;
}

.order-time {
    font-size: 12px;
    color: #868e96;
}
```

## Troubleshooting

### Common Issues

**No items displayed**
- Verify data is created in `initializeData()` before binding engine runs
- Check console logs for "Looking for list data at path..." messages
- Ensure ObservableList is properly registered in binding context

**HTML showing instead of content**
- Templates must use HTML entities: `&lt;div&gt;` not `<div>`
- Use `decodeHtmlEntities()` method processes templates correctly

**Poor scroll performance**
- Ensure `data-item-height` matches actual rendered height
- Avoid complex CSS in item templates
- Use fixed heights for optimal virtualization

**Memory issues with large lists**
- Use `addRange()` for bulk operations instead of individual `add()` calls
- Limit rendered components by setting appropriate container heights

### Debug Techniques

```java
// Verify list creation
ObservableList<String> list = getBindingContext().getObservableList("myList");
HTMLMyScreen.LOGGER.info("List exists: {}, size: {}", list != null, list != null ? list.size() : 0);

// Monitor virtualization
HTMLMyScreen.LOGGER.info("Visible range: {} to {}", visibleStart, visibleEnd);
HTMLMyScreen.LOGGER.info("Rendered components: {}", renderedItems.size());

// Track performance
long start = System.currentTimeMillis();
list.addRange(largeCollection);
HTMLMyScreen.LOGGER.info("Bulk add took: {}ms", System.currentTimeMillis() - start);
```

### Performance Monitoring

```java
// Memory usage tracking
Runtime runtime = Runtime.getRuntime();
long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
// ... create large list
long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
HTMLMyScreen.LOGGER.info("Memory delta: {}KB", (memoryAfter - memoryBefore) / 1024);
```

## Best Practices

1. **Consistent item heights** - Use fixed `data-item-height` for smooth scrolling
2. **Bulk operations** - Always use `addRange()` for multiple items
3. **Simple templates** - Keep item templates lightweight for better performance
4. **Unique list IDs** - Use distinct `id` attributes for programmatic control
5. **Timer cleanup** - Cancel timers in `close()` to prevent memory leaks
6. **Event handling** - Use ObservableList listeners for complex data synchronization
7. **CSS optimization** - Avoid expensive CSS properties in item templates
8. **Data validation** - Validate list data before adding to prevent rendering errors