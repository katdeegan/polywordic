package com.ooad_kd_yz.polywordic.model.iterator;

// Iterator Pattern - Iterator Interface
// This interface defines a contract for iterating over a collection of elements without exposing the internal representation.

public interface PWIterator<T> {
    boolean hasNext();
    T next();

    // Optional operation to reset iterator to beginning of collection
    default void reset() {
        throw new UnsupportedOperationException("Reset operation not supported");
    }
}
