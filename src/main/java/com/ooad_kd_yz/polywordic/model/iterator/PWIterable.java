package com.ooad_kd_yz.polywordic.model.iterator;

// Iterator Pattern - Iterable Interface
// Classes implementing this interface can provide an iterator to traverse their elements.

public interface PWIterable<T> {
    PWIterator<T> createIterator();
}