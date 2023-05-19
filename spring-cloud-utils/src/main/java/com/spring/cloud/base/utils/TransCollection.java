package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.crypto.IterUtil;
import com.spring.cloud.base.utils.utils.SpliteratorUtil;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @Author: ls
 * @Description: 使用给定的转换函数转换源集合为新类型的集合
 * @Date: 2023/4/13 16:11
 */
public class TransCollection<F, T> extends AbstractCollection<T> {

    private final Collection<F> fromCollection;
    private final Function<? super F, ? extends T> function;

    /**
     * 构造
     *
     * @param fromCollection 源集合
     * @param function       转换函数
     */
    public TransCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
        this.fromCollection = Assert.notNull(fromCollection);
        this.function = Assert.notNull(function);
    }

    @Override
    public Iterator<T> iterator() {
        return IterUtil.trans(fromCollection.iterator(), function);
    }

    @Override
    public void clear() {
        fromCollection.clear();
    }

    @Override
    public boolean isEmpty() {
        return fromCollection.isEmpty();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Assert.notNull(action);
        fromCollection.forEach((f) -> action.accept(function.apply(f)));
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        Assert.notNull(filter);
        return fromCollection.removeIf(element -> filter.test(function.apply(element)));
    }

    @Override
    public Spliterator<T> spliterator() {
        return SpliteratorUtil.trans(fromCollection.spliterator(), function);
    }

    @Override
    public int size() {
        return fromCollection.size();
    }
}