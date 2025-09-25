package com.edgn.edml.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DataSource<T> {
    CompletableFuture<List<T>> loadRange(int offset, int limit);
    CompletableFuture<Integer> getTotalCount();
    void invalidate();
}
