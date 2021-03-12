package org.powbot.stream

import org.powerbot.script.rt4.ClientContext
import java.util.*
import java.util.function.*
import java.util.function.Function
import java.util.stream.*
import java.util.UUID

import java.util.IdentityHashMap





abstract class SimpleStream<T, I, S : SimpleStream<T, I, S>>(override val ctx: ClientContext, override var stream: Stream<I>, val wrap: (I) -> T) :
    WrappedStream<T, I, S> {

    /**
     * {@inheritDoc}
     */
    override fun iterator(): MutableIterator<I> {
        return stream.iterator()
    }

    /**
     * {@inheritDoc}
     */
    override fun spliterator(): Spliterator<I> {
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
    override fun onClose(closeHandler: Runnable): Stream<I> {
        return stream.onClose(closeHandler)
    }

    /**
     * {@inheritDoc}
     */
    override fun filter(predicate: Predicate<in I>): S {
        this.stream = stream.filter(predicate)

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> map(mapper: Function<in I, out R>): Stream<R> {
        return stream.map(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun mapToInt(mapper: ToIntFunction<in I>): IntStream {
        return stream.mapToInt(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun mapToLong(mapper: ToLongFunction<in I>): LongStream {
        return stream.mapToLong(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun mapToDouble(mapper: ToDoubleFunction<in I>): DoubleStream {
        return stream.mapToDouble(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> flatMap(mapper: Function<in I, out Stream<out R>>): Stream<R> {
        return stream.flatMap(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun flatMapToInt(mapper: Function<in I, out IntStream>): IntStream {
        return stream.flatMapToInt(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun flatMapToLong(mapper: Function<in I, out LongStream>): LongStream {
        return stream.flatMapToLong(mapper)
    }

    /**
     * {@inheritDoc}
     */
    override fun flatMapToDouble(mapper: Function<in I, out DoubleStream>): DoubleStream {
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
    override fun sorted(comparator: Comparator<in I>): S {
        this.stream = stream.sorted(comparator)

        return this as S
    }

    /**
     * {@inheritDoc}
     */
    override fun peek(action: Consumer<in I>): S {
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
    override fun forEach(action: Consumer<in I>) {
        this.stream.forEach(action)
    }

    /**
     * {@inheritDoc}
     */
    override fun forEachOrdered(action: Consumer<in I>) {
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
    override fun reduce(identity: I, accumulator: BinaryOperator<I>): I {
        return stream.reduce(identity, accumulator)
    }

    /**
     * {@inheritDoc}
     */
    override fun reduce(accumulator: BinaryOperator<I>): Optional<I> {
        return stream.reduce(accumulator)
    }

    /**
     * {@inheritDoc}
     */
    override fun <U : Any?> reduce(identity: U, accumulator: BiFunction<U, in I, U>?, combiner: BinaryOperator<U>): U {
        return stream.reduce(identity, accumulator, combiner)
    }

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> collect(
        supplier: Supplier<R>,
        accumulator: BiConsumer<R, in I>,
        combiner: BiConsumer<R, R>
    ): R {
        return stream.collect(supplier, accumulator, combiner)
    }

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?, A : Any?> collect(collector: Collector<in I, A, R>): R {
        return stream.collect(collector)
    }

    /**
     * {@inheritDoc}
     */
    override fun min(comparator: Comparator<in I>): Optional<I> {
        return stream.min(comparator)
    }

    /**
     * {@inheritDoc}
     */
    override fun max(comparator: Comparator<in I>): Optional<I> {
        return stream.max(comparator)
    }

    /**
     * {@inheritDoc}
     */
    override fun anyMatch(predicate: Predicate<in I>): Boolean {
        return stream.anyMatch(predicate)
    }

    /**
     * {@inheritDoc}
     */
    override fun allMatch(predicate: Predicate<in I>): Boolean {
        return stream.allMatch(predicate)
    }

    /**
     * {@inheritDoc}
     */
    override fun noneMatch(predicate: Predicate<in I>): Boolean {
        return stream.noneMatch(predicate)
    }

    /**
     * {@inheritDoc}
     */
    override fun findFirst(): Optional<I> {
        return stream.findFirst()
    }

    /**
     * {@inheritDoc}
     */
    override fun findAny(): Optional<I> {
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

    /**
     * nil value of the entity
     *
     * @return nil
     */
    abstract fun nil(): I

    /**
     * nil value of the entity
     *
     * @return nil
     */
    abstract fun nilWrapped(): T

    /**
     * Get the first entity from the stream
     *
     * @return the entity or a nil object of the entity
     */
    fun first(): I {
        return findFirst().orElse(nil())
    }

    /**
     * Get the first entity from the stream
     *
     * @return the entity or a nil object of the entity
     */
    fun firstWrapped(): T {
        return findFirst().map { wrap(it) }.orElse(nilWrapped())
    }


    /**
     * Get any entity from the stream
     *
     * @return the entity or a nil object of the entity
     */
    fun any(): I {
        return findAny().orElse(nil())
    }

    /**
     * Get any entity from the stream
     *
     * @return the entity or a nil object of the entity
     */
    fun anyWrapped(): T {
        return findAny().map { wrap(it) }.orElse(nilWrapped())
    }


    /**
     * Check if the stream contains any of the provided entities
     *
     * @return boolean
     */
    fun contains(t: Collection<I>): Boolean {
        return t.toList().any { _t -> this.stream.anyMatch { it == _t } }
    }

    /**
     * Check if the stream contains any of the provided entities
     *
     * @return boolean
     */
    fun contains(vararg t: T): Boolean {
        return t.toList().any { _t -> this.stream.anyMatch { it == _t } }
    }

    /**
     * Check if the stream is empty or not
     *
     * @return boolean
     */
    fun isEmpty(): Boolean {
        return this.stream.count() == 0L
    }

    /**
     * Check if the stream is NOT empty or not
     *
     * @return boolean
     */
    fun isNotEmpty(): Boolean {
        return this.stream.count() > 0
    }

    fun list(): List<I> {
        return this.stream.collect(Collectors.toList())
    }

    fun wrappedList(): List<T> {
        return this.stream.map { wrap(it) }.collect(Collectors.toList())
    }

    private fun <I> shuffler(): Comparator<I> {
        val uniqueIds: MutableMap<I, UUID> = IdentityHashMap()
        return Comparator.comparing { e ->
            uniqueIds.computeIfAbsent(e,
                { k: I -> UUID.randomUUID() })
        }
    }

    fun shuffle(): S {
        this.stream = stream.sorted(shuffler())

        return this as S
    }
}
