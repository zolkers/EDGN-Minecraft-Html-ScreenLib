package com.edgn.edml.data.collections;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ObservableList<T> implements List<T> {
    private final List<T> items = new ArrayList<>();
    private final List<ListChangeListener<T>> listeners = new CopyOnWriteArrayList<>();

    public interface ListChangeListener<T> {
        void onItemAdded(int index, T item);
        void onItemRemoved(int index, T item);
        void onItemChanged(int index, T oldItem, T newItem);
        void onListCleared();
        void onRangeAdded(int startIndex, Collection<T> items);
        void onRangeRemoved(int startIndex, int count);
    }

    public void addListener(ListChangeListener<Object> listener) {
        listeners.add((ListChangeListener<T>) listener);
    }

    public void removeListener(ListChangeListener<T> listener) {
        listeners.remove(listener);
    }

    @Override
    public boolean add(T item) {
        items.add(item);
        int index = items.size() - 1;
        listeners.forEach(l -> l.onItemAdded(index, item));
        return true;
    }

    @Override
    public void add(int index, T item) {
        items.add(index, item);
        listeners.forEach(l -> l.onItemAdded(index, item));
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> collection) {
        int startIndex = items.size();
        boolean result = items.addAll(collection);
        if (result) {
            listeners.forEach(l -> l.onRangeAdded(startIndex, (Collection<T>) collection));
        }
        return result;
    }

    @Override
    public T remove(int index) {
        T item = items.remove(index);
        listeners.forEach(l -> l.onItemRemoved(index, item));
        return item;
    }

    @Override
    public boolean remove(Object o) {
        int index = items.indexOf(o);
        if (index >= 0) {
            T item = items.remove(index);
            listeners.forEach(l -> l.onItemRemoved(index, item));
            return true;
        }
        return false;
    }

    @Override
    public T set(int index, T item) {
        T oldItem = items.set(index, item);
        listeners.forEach(l -> l.onItemChanged(index, oldItem, item));
        return oldItem;
    }

    @Override
    public void clear() {
        items.clear();
        listeners.forEach(ListChangeListener::onListCleared);
    }

    public void addRange(Collection<T> items) {
        int startIndex = this.items.size();
        this.items.addAll(items);
        listeners.forEach(l -> l.onRangeAdded(startIndex, items));
    }

    public void removeRange(int startIndex, int count) {
        for (int i = 0; i < count && startIndex < items.size(); i++) {
            items.remove(startIndex);
        }
        listeners.forEach(l -> l.onRangeRemoved(startIndex, count));
    }

    @Override public int size() { return items.size(); }
    @Override public boolean isEmpty() { return items.isEmpty(); }
    @Override public boolean contains(Object o) { return items.contains(o); }
    @Override public @NotNull Iterator<T> iterator() { return new ArrayList<>(items).iterator(); }
    @Override public Object @NotNull [] toArray() { return items.toArray(); }
    @Override public <T1> T1 @NotNull [] toArray(T1 @NotNull [] a) { return items.toArray(a); }
    @Override public boolean containsAll(@NotNull Collection<?> c) { return new HashSet<>(items).containsAll(c); }
    @Override public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        boolean result = items.addAll(index, c);
        if (result) listeners.forEach(l -> l.onRangeAdded(index, (Collection<T>) c));
        return result;
    }
    @Override public boolean removeAll(Collection<?> c) { 
        boolean result = items.removeAll(c);
        if (result) listeners.forEach(ListChangeListener::onListCleared);
        return result;
    }
    @Override public boolean retainAll(Collection<?> c) { 
        boolean result = items.retainAll(c);
        if (result) listeners.forEach(ListChangeListener::onListCleared);
        return result;
    }
    @Override public T get(int index) { return items.get(index); }
    @Override public int indexOf(Object o) { return items.indexOf(o); }
    @Override public int lastIndexOf(Object o) { return items.lastIndexOf(o); }
    @Override public @NotNull ListIterator<T> listIterator() { return new ArrayList<>(items).listIterator(); }
    @Override public @NotNull ListIterator<T> listIterator(int index) { return new ArrayList<>(items).listIterator(index); }
    @Override public @NotNull List<T> subList(int fromIndex, int toIndex) { return new ArrayList<>(items.subList(fromIndex, toIndex)); }
}