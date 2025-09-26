# EDGN Data Binding Documentation

## Overview

The EDGN Data Binding system provides reactive data binding between Java objects and EDML components using a simple `{{variable}}` syntax. Data changes automatically update the UI without manual intervention.

## Core Components

### AdvancedBindingContext

The main data container that manages all bound data and notifications.

```java
AdvancedBindingContext bindingContext = new AdvancedBindingContext();
```

#### Methods

- `setValue(String path, Object value)` - Sets a value and triggers updates
- `getValue(String path)` - Retrieves a value
- `bindProperty(String path, PropertyChangeListener listener)` - Listens to changes
- `createObservableList(String path)` - Creates a reactive list

### DataBindingEngine

Processes EDML components and establishes data bindings.

```java
DataBindingEngine bindingEngine = new DataBindingEngine(bindingContext);
bindingEngine.processComponent(rootComponent);
```

## Basic Data Binding

### Setting Values

```java
// Set simple values
bindingContext.setValue("title", "My Application");
bindingContext.setValue("version", "1.0.0");
bindingContext.setValue("userCount", 42);
```

### EDML Binding Syntax

Use `{{variableName}}` in EDML attributes:

```xml
<body>
    <div data-text="{{title}}" class="app-title"></div>
    <div data-text="Version: {{version}}" class="version-info"></div>
    <div data-text="Users online: {{userCount}}" class="user-count"></div>
</body>
```

### Supported Attributes

- `data-text` - Sets text content
- `data-class` - Sets CSS classes
- `data-style` - Sets inline styles
- `data-visible` - Controls visibility

## Reactive Updates

Data binding is reactive - changes automatically update the UI:

```java
// This will immediately update all bound UI elements
bindingContext.setValue("userCount", 100);
bindingContext.setValue("title", "Updated Title");
```

### Listening to Changes

```java
bindingContext.bindProperty("userCount", event -> {
    HTMLMyScreen.LOGGER.info("User count changed: {}", event.getNewValue());
});
```

## Screen Integration

### In StandardResourceEdmlScreen

Override `initializeData()` to set up your data:

```java
public class MyScreen extends StandardResourceEdmlScreen {
    
    public MyScreen() {
        super(Text.literal("My Screen"), "my_screen", "my_styles");
    }
    
    @Override
    protected void initializeData() {
        // Set up data before binding engine processes components
        getBindingContext().setValue("appName", "My Application");
        getBindingContext().setValue("status", "Ready");
        
        // Create observable lists
        ObservableList<String> items = getBindingContext().createObservableList("items");
        items.add("Item 1");
        items.add("Item 2");
    }
}
```

### Accessing the Binding Context

```java
// Get the binding context in your screen
AdvancedBindingContext context = getBindingContext();

// Update values dynamically
Timer timer = new Timer(true);
timer.scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        context.setValue("currentTime", LocalTime.now().toString());
    }
}, 0, 1000);
```

## Performance Considerations

- Data binding updates are efficient - only affected components are updated
- Use specific variable names to avoid unnecessary updates
- ObservableList changes trigger minimal UI updates (see Virtual List documentation)
- Memory usage is proportional to number of bound variables, not data size

## Error Handling

The binding system handles errors gracefully:

- Missing variables display as empty strings
- Invalid values are logged but don't crash the UI
- Malformed binding syntax is ignored

## Examples

### Simple Counter

```java
// Java
getBindingContext().setValue("counter", 0);

Timer timer = new Timer(true);
timer.scheduleAtFixedRate(new TimerTask() {
    private int count = 0;
    @Override
    public void run() {
        getBindingContext().setValue("counter", ++count);
    }
}, 1000, 1000);
```

```xml
<!-- EDML -->
<div data-text="Count: {{counter}}" class="counter-display"></div>
```

### Dynamic Styling

```java
// Java
getBindingContext().setValue("isOnline", true);
getBindingContext().setValue("statusClass", "online");
```

```xml
<!-- EDML -->
<div data-text="{{isOnline}}" data-class="status {{statusClass}}"></div>
```

### Complex Data Updates

```java
// Multiple related updates
getBindingContext().setValue("username", "john_doe");
getBindingContext().setValue("lastLogin", "2024-01-15");
getBindingContext().setValue("loginCount", 42);
```

```xml
<!-- EDML -->
<div class="user-info">
    <div data-text="Welcome {{username}}" class="greeting"></div>
    <div data-text="Last login: {{lastLogin}}" class="last-login"></div>
    <div data-text="Total logins: {{loginCount}}" class="login-count"></div>
</div>
```

## Best Practices

1. **Initialize data before UI processing** - Use `initializeData()` hook
2. **Use descriptive variable names** - `userCount` not `count`
3. **Group related updates** - Set multiple values in sequence for atomic updates
4. **Clean up timers** - Override `close()` to cancel timers and prevent memory leaks
5. **Use ObservableList for dynamic collections** - See Virtual List documentation

## Debugging

Enable logging to debug binding issues:

```java
HTMLMyScreen.LOGGER.info("Setting value: {} = {}", "myVar", myValue);
Object retrieved = bindingContext.getValue("myVar");
HTMLMyScreen.LOGGER.info("Retrieved value: {}", retrieved);
```

The binding engine logs all binding attempts and failures with detailed information about which components are being bound.