package org.powbot.stream

import org.powerbot.script.rt4.ClientContext
import java.util.*
import java.util.function.*
import java.util.function.Function
import java.util.stream.*


open class SimpleStream<T, S : SimpleStream<T, S>>(override val ctx: ClientContext, override var stream: Stream<T>) :
    WrappedStream<T, S> {

    /**
     * {@inheritDoc}
     */
    override fun iterator(): MutableIterator<T> {
        return stream.iterator()
    }

    /**
     * {@inheritDoc}
     */
    override fun spliterator(): Spliterator<T> {
        return stream.spliterator()
    }

    /**
     * {@inheritDoc}
     */
    override fun isParallel(): Boolean {
        return stream.isParallel
    }

    /**
     * {@inheritDoc}
     */
    override fun sequential(): S {
        this.stream = stream.sequential()

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun parallel(): S {
        this.stream = stream.parallel()

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun unordered(): S {
        this.stream = stream.unordered()

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun onClose(closeHandler: Runnable): Stream<T> {
        return stream.onClose(closeHandler)
    }

    /**
     * {@inheritDoc}
     */
    override fun filter(predicate: Predicate<in T>): S {
        this.stream = stream.filter(predicate)

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> map(mapper: Function<in T, out R>): Stream<R> {
        return stream.map(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun mapToInt(mapper: ToIntFunction<in T>): IntStream {
        return stream.mapToInt(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun mapToLong(mapper: ToLongFunction<in T>): LongStream {
        return stream.mapToLong(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun mapToDouble(mapper: ToDoubleFunction<in T>): DoubleStream {
        return stream.mapToDouble(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> flatMap(mapper: Function<in T, out Stream<out R>>): Stream<R> {
        return stream.flatMap(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun flatMapToInt(mapper: Function<in T, out IntStream>): IntStream {
        return stream.flatMapToInt(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun flatMapToLong(mapper: Function<in T, out LongStream>): LongStream {
        return stream.flatMapToLong(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun flatMapToDouble(mapper: Function<in T, out DoubleStream>): DoubleStream {
        return stream.flatMapToDouble(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun distinct(): S {
        this.stream = stream.distinct()

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun sorted(): S {
        this.stream = stream.sorted()

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun sorted(comparator: Comparator<in T>): S {
        this.stream = stream.sorted(comparator)

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun peek(action: Consumer<in T>): S {
        this.stream = stream.peek(action)

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun limit(maxSize: Long): S {
        this.stream = stream.limit(maxSize)

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun skip(n: Long): S {
        this.stream = stream.skip(n)

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun forEach(action: Consumer<in T>) {
        this.stream.forEach(action)
    }

    /**
     * {@inheritDoc}
     */
    override fun forEachOrdered(action: Consumer<in T>) {
        this.stream.forEachOrdered(action)
    }

    /**
     * {@inheritDoc}
     */
    override fun toArray(): Array<Any> {
        return stream.toArray()
    }

    /**
     * {@inheritDoc}
     */
    override fun <A : Any?> toArray(generator: IntFunction<Array<A>>): Array<A> {
        return stream.toArray(generator)
    }

    /**
     * {@inheritDoc}
     */
    override fun reduce(identity: T, accumulator: BinaryOperator<T>): T {
        return stream.reduce(identity, accumulator)
    }

    /**
     * {@inheritDoc}
     */
    override fun reduce(accumulator: BinaryOperator<T>): Optional<T> {
        return stream.reduce(accumulator)
    }

    /**
     * {@inheritDoc}
     */
    override fun <U : Any?> reduce(identity: U, accumulator: BiFunction<U, in T, U>?, combiner: BinaryOperator<U>): U {
        return stream.reduce(identity, accumulator, combiner)
    }

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> collect(
        supplier: Supplier<R>,
        accumulator: BiConsumer<R, in T>,
        combiner: BiConsumer<R, R>
    ): R {
        return stream.collect(supplier, accumulator, combiner)
    }

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?, A : Any?> collect(collector: Collector<in T, A, R>): R {
        return stream.collect(collector)
    }

    /**
     * {@inheritDoc}
     */
    override fun min(comparator: Comparator<in T>): Optional<T> {
        return stream.min(comparator)
    }

    /**
     * {@inheritDoc}
     */
    override fun max(comparator: Comparator<in T>): Optional<T> {
        return stream.max(comparator)
    }

    /**
     * {@inheritDoc}
     */
    override fun anyMatch(predicate: Predicate<in T>): Boolean {
        return stream.anyMatch(predicate)
    }

    /**
     * {@inheritDoc}
     */
    override fun allMatch(predicate: Predicate<in T>): Boolean {
        return stream.allMatch(predicate)
    }

    /**
     * {@inheritDoc}
     */
    override fun noneMatch(predicate: Predicate<in T>): Boolean {
        return stream.noneMatch(predicate)
    }

    /**
     * {@inheritDoc}
     */
    override fun findFirst(): Optional<T> {
        return stream.findFirst()
    }

    /**
     * {@inheritDoc}
     */
    override fun findAny(): Optional<T> {
        return stream.findAny()
    }

    /**
     * {@inheritDoc}
     */
    override fun close() {
        return stream.close()
    }

    /**
     * {@inheritDoc}
     */
    override fun count(): Long {
        return stream.count()
    }
}
