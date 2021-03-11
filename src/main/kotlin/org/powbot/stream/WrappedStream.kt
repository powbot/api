package org.powbot.stream

import org.powerbot.script.rt4.ClientContext
import java.util.*
import java.util.function.*
import java.util.function.Function
import java.util.stream.*

interface WrappedStream<T, I, S : SimpleStream<T, I, S>> : Stream<I> {
    val ctx: ClientContext
    var stream: Stream<I>

    /**
     * {@inheritDoc}
     */
    override fun iterator(): MutableIterator<I>

    /**
     * {@inheritDoc}
     */
    override fun spliterator(): Spliterator<I>

    /**
     * {@inheritDoc}
     */
    override fun isParallel(): Boolean

    /**
     * {@inheritDoc}
     */
    override fun sequential(): S

    /**
     * {@inheritDoc}
     */
    override fun parallel(): S

    /**
     * {@inheritDoc}
     */
    override fun unordered(): S

    /**
     * {@inheritDoc}
     */
    override fun onClose(closeHandler: Runnable): Stream<I>

    /**
     * {@inheritDoc}
     */
    override fun filter(predicate: Predicate<in I>): S

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> map(mapper: Function<in I, out R>): Stream<R>

    /**
     * {@inheritDoc}
     */
    override fun mapToInt(mapper: ToIntFunction<in I>): IntStream

    /**
     * {@inheritDoc}
     */
    override fun mapToLong(mapper: ToLongFunction<in I>): LongStream

    /**
     * {@inheritDoc}
     */
    override fun mapToDouble(mapper: ToDoubleFunction<in I>): DoubleStream

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> flatMap(mapper: Function<in I, out Stream<out R>>): Stream<R>

    /**
     * {@inheritDoc}
     */
    override fun flatMapToInt(mapper: Function<in I, out IntStream>): IntStream

    /**
     * {@inheritDoc}
     */
    override fun flatMapToLong(mapper: Function<in I, out LongStream>): LongStream

    /**
     * {@inheritDoc}
     */
    override fun flatMapToDouble(mapper: Function<in I, out DoubleStream>): DoubleStream

    /**
     * {@inheritDoc}
     */
    override fun distinct(): S

    /**
     * {@inheritDoc}
     */
    override fun sorted(): S

    /**
     * {@inheritDoc}
     */
    override fun sorted(comparator: Comparator<in I>): S

    /**
     * {@inheritDoc}
     */
    override fun peek(action: Consumer<in I>): S

    /**
     * {@inheritDoc}
     */
    override fun limit(maxSize: Long): S

    /**
     * {@inheritDoc}
     */
    override fun skip(n: Long): S

    /**
     * {@inheritDoc}
     */
    override fun forEach(action: Consumer<in I>)

    /**
     * {@inheritDoc}
     */
    override fun forEachOrdered(action: Consumer<in I>)

    /**
     * {@inheritDoc}
     */
    override fun toArray(): Array<Any>

    /**
     * {@inheritDoc}
     */
    override fun <A : Any?> toArray(generator: IntFunction<Array<A>>): Array<A>

    /**
     * {@inheritDoc}
     */
    override fun reduce(identity: I, accumulator: BinaryOperator<I>): I

    /**
     * {@inheritDoc}
     */
    override fun reduce(accumulator: BinaryOperator<I>): Optional<I>

    /**
     * {@inheritDoc}
     */
    override fun <U : Any?> reduce(identity: U, accumulator: BiFunction<U, in I, U>?, combiner: BinaryOperator<U>): U

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> collect(
        supplier: Supplier<R>,
        accumulator: BiConsumer<R, in I>,
        combiner: BiConsumer<R, R>
    ): R

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?, A : Any?> collect(collector: Collector<in I, A, R>): R

    /**
     * {@inheritDoc}
     */
    override fun min(comparator: Comparator<in I>): Optional<I>

    /**
     * {@inheritDoc}
     */
    override fun max(comparator: Comparator<in I>): Optional<I>

    /**
     * {@inheritDoc}
     */
    override fun anyMatch(predicate: Predicate<in I>): Boolean

    /**
     * {@inheritDoc}
     */
    override fun allMatch(predicate: Predicate<in I>): Boolean

    /**
     * {@inheritDoc}
     */
    override fun noneMatch(predicate: Predicate<in I>): Boolean

    /**
     * {@inheritDoc}
     */
    override fun findFirst(): Optional<I>

    /**
     * {@inheritDoc}
     */
    override fun findAny(): Optional<I>

    /**
     * {@inheritDoc}
     */
    override fun close()

    /**
     * {@inheritDoc}
     */
    override fun count(): Long
}
