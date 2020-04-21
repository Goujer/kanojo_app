// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// https://developers.google.com/protocol-buffers/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.google.tagmanager.protobuf;

import com.google.tagmanager.protobuf.FieldSet;
import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

class SmallSortedMap<K extends Comparable<K>, V> extends AbstractMap<K, V> {
	/* access modifiers changed from: private */
	public List<SmallSortedMap<K, V>.Entry> entryList;
	private boolean isImmutable;
	private volatile SmallSortedMap<K, V>.EntrySet lazyEntrySet;
	private final int maxArraySize;
	/* access modifiers changed from: private */
	public Map<K, V> overflowEntries;

	/**
	 * Creates a new instance for mapping FieldDescriptors to their values. The {@link
	 * #makeImmutable()} implementation will convert the List values of any repeated fields to
	 * unmodifiable lists.
	 *
	 * @param arraySize The size of the entry array containing the lexicographically smallest
	 *     mappings.
	 */
	static <FieldDescriptorType extends FieldSet.FieldDescriptorLite<FieldDescriptorType>>
	SmallSortedMap<FieldDescriptorType, Object> newFieldMap(int arraySize) {
		return new SmallSortedMap<FieldDescriptorType, Object>(arraySize) {
			@Override
			@SuppressWarnings("unchecked")
			public void makeImmutable() {
				if (!isImmutable()) {
					for (int i = 0; i < getNumArrayEntries(); i++) {
						final Map.Entry<FieldDescriptorType, Object> entry = getArrayEntryAt(i);
						if (entry.getKey().isRepeated()) {
							final List value = (List) entry.getValue();
							entry.setValue(Collections.unmodifiableList(value));
						}
					}
					for (Map.Entry<FieldDescriptorType, Object> entry : getOverflowEntries()) {
						if (entry.getKey().isRepeated()) {
							final List value = (List) entry.getValue();
							entry.setValue(Collections.unmodifiableList(value));
						}
					}
				}
				super.makeImmutable();
			}
		};
	}

	static <K extends Comparable<K>, V> SmallSortedMap<K, V> newInstanceForTest(int arraySize) {
		return new SmallSortedMap<>(arraySize);
	}

	private SmallSortedMap(int arraySize) {
		this.maxArraySize = arraySize;
		this.entryList = Collections.emptyList();
		this.overflowEntries = Collections.emptyMap();
	}


	/** Make this map immutable from this point forward. */
	public void makeImmutable() {
		if (!isImmutable) {
			// Note: There's no need to wrap the entryList in an unmodifiableList
			// because none of the list's accessors are exposed. The iterator() of
			// overflowEntries, on the other hand, is exposed so it must be made
			// unmodifiable.
			overflowEntries =
					overflowEntries.isEmpty()
							? Collections.<K, V>emptyMap()
							: Collections.unmodifiableMap(overflowEntries);
			isImmutable = true;
		}
	}

	public boolean isImmutable() {
		return this.isImmutable;
	}

	public int getNumArrayEntries() {
		return this.entryList.size();
	}

	public Map.Entry<K, V> getArrayEntryAt(int index) {
		return this.entryList.get(index);
	}

	public int getNumOverflowEntries() {
		return this.overflowEntries.size();
	}

	/** @return An iterable over the overflow entries. */
	public Iterable<Map.Entry<K, V>> getOverflowEntries() {
		return overflowEntries.isEmpty()
				? EmptySet.<Map.Entry<K, V>>iterable()
				: overflowEntries.entrySet();
	}

	public int size() {
		return this.entryList.size() + this.overflowEntries.size();
	}

	/**
	 * The implementation throws a {@code ClassCastException} if o is not an object of type {@code K}.
	 *
	 * <p>{@inheritDoc}
	 */
	@Override
	public boolean containsKey(Object o) {
		@SuppressWarnings("unchecked")
		final K key = (K) o;
		return binarySearchInArray(key) >= 0 || overflowEntries.containsKey(key);
	}

	/**
	 * The implementation throws a {@code ClassCastException} if o is not an object of type {@code K}.
	 *
	 * <p>{@inheritDoc}
	 */
	@Override
	public V get(Object o) {
		@SuppressWarnings("unchecked")
		final K key = (K) o;
		final int index = binarySearchInArray(key);
		if (index >= 0) {
			return entryList.get(index).getValue();
		}
		return overflowEntries.get(key);
	}

	public V put(K key, V value) {
		checkMutable();
		int index = binarySearchInArray(key);
		if (index >= 0) {
			return this.entryList.get(index).setValue(value);
		}
		ensureEntryArrayMutable();
		int insertionPoint = -(index + 1);
		if (insertionPoint >= this.maxArraySize) {
			return getOverflowEntriesMutable().put(key, value);
		}
		if (this.entryList.size() == this.maxArraySize) {
			SmallSortedMap<K, V>.Entry lastEntryInArray = this.entryList.remove(this.maxArraySize - 1);
			getOverflowEntriesMutable().put(lastEntryInArray.getKey(), lastEntryInArray.getValue());
		}
		this.entryList.add(insertionPoint, new Entry(key, value));
		return null;
	}

	public void clear() {
		checkMutable();
		if (!this.entryList.isEmpty()) {
			this.entryList.clear();
		}
		if (!this.overflowEntries.isEmpty()) {
			this.overflowEntries.clear();
		}
	}

	/**
	 * The implementation throws a {@code ClassCastException} if o is not an object of type {@code K}.
	 *
	 * <p>{@inheritDoc}
	 */
	@Override
	public V remove(Object o) {
		checkMutable();
		@SuppressWarnings("unchecked")
		final K key = (K) o;
		final int index = binarySearchInArray(key);
		if (index >= 0) {
			return removeArrayEntryAt(index);
		}
		// overflowEntries might be Collections.unmodifiableMap(), so only
		// call remove() if it is non-empty.
		if (overflowEntries.isEmpty()) {
			return null;
		} else {
			return overflowEntries.remove(key);
		}
	}

	private V removeArrayEntryAt(int index) {
		checkMutable();
		final V removed = entryList.remove(index).getValue();
		if (!overflowEntries.isEmpty()) {
			// Shift the first entry in the overflow to be the last entry in the
			// array.
			final Iterator<Map.Entry<K, V>> iterator = getOverflowEntriesMutable().entrySet().iterator();
			entryList.add(new Entry(iterator.next()));
			iterator.remove();
		}
		return removed;
	}

	private int binarySearchInArray(K key) {
		int left = 0;
		int right = this.entryList.size() - 1;
		if (right >= 0) {
			int cmp = key.compareTo(this.entryList.get(right).getKey());
			if (cmp > 0) {
				return -(right + 2);
			}
			if (cmp == 0) {
				return right;
			}
		}
		while (left <= right) {
			int mid = (left + right) / 2;
			int cmp2 = key.compareTo(this.entryList.get(mid).getKey());
			if (cmp2 < 0) {
				right = mid - 1;
			} else if (cmp2 <= 0) {
				return mid;
			} else {
				left = mid + 1;
			}
		}
		return -(left + 1);
	}

	public Set<Map.Entry<K, V>> entrySet() {
		if (this.lazyEntrySet == null) {
			this.lazyEntrySet = new EntrySet();
		}
		return this.lazyEntrySet;
	}

	/* access modifiers changed from: private */
	public void checkMutable() {
		if (this.isImmutable) {
			throw new UnsupportedOperationException();
		}
	}

	private SortedMap<K, V> getOverflowEntriesMutable() {
		checkMutable();
		if (this.overflowEntries.isEmpty() && !(this.overflowEntries instanceof TreeMap)) {
			this.overflowEntries = new TreeMap();
		}
		return (SortedMap) this.overflowEntries;
	}

	private void ensureEntryArrayMutable() {
		checkMutable();
		if (this.entryList.isEmpty() && !(this.entryList instanceof ArrayList)) {
			this.entryList = new ArrayList(this.maxArraySize);
		}
	}

	/**
	 * Entry implementation that implements Comparable in order to support binary search within the
	 * entry array. Also checks mutability in {@link #setValue()}.
	 */
	private class Entry implements Map.Entry<K, V>, Comparable<Entry> {
		private final K key;
		private V value;

		Entry(Map.Entry<K, V> copy) {
			this(copy.getKey(), copy.getValue());
		}

		Entry(K key2, V value2) {
			this.key = key2;
			this.value = value2;
		}

		public K getKey() {
			return this.key;
		}

		public V getValue() {
			return this.value;
		}

		public int compareTo(SmallSortedMap<K, V>.Entry other) {
			return getKey().compareTo(other.getKey());
		}

		public V setValue(V newValue) {
			SmallSortedMap.this.checkMutable();
			V oldValue = this.value;
			this.value = newValue;
			return oldValue;
		}

		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry<?, ?> other = (Map.Entry) o;
			if (!equals(this.key, other.getKey()) || !equals(this.value, other.getValue())) {
				return false;
			}
			return true;
		}

		public int hashCode() {
			int i = 0;
			int hashCode = this.key == null ? 0 : this.key.hashCode();
			if (this.value != null) {
				i = this.value.hashCode();
			}
			return hashCode ^ i;
		}

		public String toString() {
			return this.key + "=" + this.value;
		}

		private boolean equals(Object o1, Object o2) {
			if (o1 == null) {
				return o2 == null;
			}
			return o1.equals(o2);
		}
	}

	private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
		private EntrySet() {
		}

		public Iterator<Map.Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		public int size() {
			return SmallSortedMap.this.size();
		}

		public boolean contains(Object o) {
			Map.Entry<K, V> entry = (Map.Entry) o;
			V existing = SmallSortedMap.this.get(entry.getKey());
			V value = entry.getValue();
			return existing == value || (existing != null && existing.equals(value));
		}

		@Override
		public boolean add(Map.Entry<K, V> entry) {
			if (!contains(entry)) {
				put(entry.getKey(), entry.getValue());
				return true;
			}
			return false;
		}

		public boolean remove(Object o) {
			Map.Entry<K, V> entry = (Map.Entry) o;
			if (!contains(entry)) {
				return false;
			}
			SmallSortedMap.this.remove(entry.getKey());
			return true;
		}

		public void clear() {
			SmallSortedMap.this.clear();
		}
	}

	private class EntryIterator implements Iterator<Map.Entry<K, V>> {
		private Iterator<Map.Entry<K, V>> lazyOverflowIterator;
		private boolean nextCalledBeforeRemove;
		private int pos;

		private EntryIterator() {
			this.pos = -1;
		}

		public boolean hasNext() {
			return this.pos + 1 < SmallSortedMap.this.entryList.size() || getOverflowIterator().hasNext();
		}

		public Map.Entry<K, V> next() {
			this.nextCalledBeforeRemove = true;
			int i = this.pos + 1;
			this.pos = i;
			if (i < SmallSortedMap.this.entryList.size()) {
				return (Map.Entry) SmallSortedMap.this.entryList.get(this.pos);
			}
			return (Map.Entry) getOverflowIterator().next();
		}

		public void remove() {
			if (!this.nextCalledBeforeRemove) {
				throw new IllegalStateException("remove() was called before next()");
			}
			this.nextCalledBeforeRemove = false;
			SmallSortedMap.this.checkMutable();
			if (this.pos < SmallSortedMap.this.entryList.size()) {
				SmallSortedMap smallSortedMap = SmallSortedMap.this;
				int i = this.pos;
				this.pos = i - 1;
				Object unused = smallSortedMap.removeArrayEntryAt(i);
				return;
			}
			getOverflowIterator().remove();
		}

		private Iterator<Map.Entry<K, V>> getOverflowIterator() {
			if (this.lazyOverflowIterator == null) {
				this.lazyOverflowIterator = SmallSortedMap.this.overflowEntries.entrySet().iterator();
			}
			return this.lazyOverflowIterator;
		}
	}

	private static class EmptySet {
		private static final Iterable<Object> ITERABLE = new Iterable<Object>() {
			public Iterator<Object> iterator() {
				return EmptySet.ITERATOR;
			}
		};
		/* access modifiers changed from: private */
		public static final Iterator<Object> ITERATOR = new Iterator<Object>() {
			public boolean hasNext() {
				return false;
			}

			public Object next() {
				throw new NoSuchElementException();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};

		private EmptySet() {
		}

		@SuppressWarnings("unchecked")
		static <T> Iterable<T> iterable() {
			return (Iterable<T>) ITERABLE;
		}
	}
}
