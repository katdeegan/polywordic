package com.ooad_kd_yz.polywordic.model.iterator;

// Iterator Pattern - Custom Iterator implementation for PolywordicWord

import com.ooad_kd_yz.polywordic.model.PolywordicLetter;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PolywordicWordIterator implements PWIterator<PolywordicLetter> {
    private final List<PolywordicLetter> letters;
    private int position;

    public PolywordicWordIterator(List<PolywordicLetter> letters) {
        this.letters = new ArrayList<>(letters);
        this.position = 0;
    }

    @Override
    public boolean hasNext() {
        return position < letters.size();
    }

    @Override
    public PolywordicLetter next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more letters in the word");
        }
        return letters.get(position++);
    }

    @Override
    public void reset() {
        position = 0;
    }
}
