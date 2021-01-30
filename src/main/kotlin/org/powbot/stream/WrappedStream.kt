import org.powbot.stream.SimpleStream
import org.powerbot.script.rt4.ClientContext
import java.util.*
import java.util.function.*
import java.util.function.Function
import java.util.stream.*

interface WrappedStream<T, S : SimpleStream<T, S>> : Stream<T> {
    val ctx: ClientContext
    var stream: Stream<T>

    /**
     * {@inheritDoc}
     */
    override fun iterator(): MutableIterator<T>

    /**
     * {@inheritDoc}
     */
    override fun spliterator(): Spliterator<T>

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
    override fun onClose(closeHandler: Runnable): Stream<T>

    /**
     * {@inheritDoc}
     */
    override fun filter(predicate: Predicate<in T>): S

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> map(mapper: Function<in T, out R>): Stream<R>

    /**
     * {@inheritDoc}
     */
    override fun mapToInt(mapper: ToIntFunction<in T>): IntStream

    /**
     * {@inheritDoc}
     */
    override fun mapToLong(mapper: ToLongFunction<in T>): LongStream

    /**
     * {@inheritDoc}
     */
    override fun mapToDouble(mapper: ToDoubleFunction<in T>): DoubleStream

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> flatMap(mapper: Function<in T, out Stream<out R>>): Stream<R>

    /**
     * {@inheritDoc}
     */
    override fun flatMapToInt(mapper: Function<in T, out IntStream>): IntStream

    /**
     * {@inheritDoc}
     */
    override fun flatMapToLong(mapper: Function<in T, out LongStream>): LongStream

    /**
     * {@inheritDoc}
     */
    override fun flatMapToDouble(mapper: Function<in T, out DoubleStream>): DoubleStream

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
    override fun sorted(comparator: Comparator<in T>): S

    /**
     * {@inheritDoc}
     */
    override fun peek(action: Consumer<in T>): S

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
    override fun forEach(action: Consumer<in T>)

    /**
     * {@inheritDoc}
     */
    override fun forEachOrdered(action: Consumer<in T>)

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
    override fun reduce(identity: T, accumulator: BinaryOperator<T>): T

    /**
     * {@inheritDoc}
     */
    override fun reduce(accumulator: BinaryOperator<T>): Optional<T>

    /**
     * {@inheritDoc}
     */
    override fun <U : Any?> reduce(identity: U, accumulator: BiFunction<U, in T, U>?, combiner: BinaryOperator<U>): U

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?> collect(
        supplier: Supplier<R>,
        accumulator: BiConsumer<R, in T>,
        combiner: BiConsumer<R, R>
    ): R

    /**
     * {@inheritDoc}
     */
    override fun <R : Any?, A : Any?> collect(collector: Collector<in T, A, R>): R

    /**
     * {@inheritDoc}
     */
    override fun min(comparator: Comparator<in T>): Optional<T>

    /**
     * {@inheritDoc}
     */
    override fun max(comparator: Comparator<in T>): Optional<T>

    /**
     * {@inheritDoc}
     */
    override fun anyMatch(predicate: Predicate<in T>): Boolean

    /**
     * {@inheritDoc}
     */
    override fun allMatch(predicate: Predicate<in T>): Boolean

    /**
     * {@inheritDoc}
     */
    override fun noneMatch(predicate: Predicate<in T>): Boolean

    /**
     * {@inheritDoc}
     */
    override fun findFirst(): Optional<T>

    /**
     * {@inheritDoc}
     */
    override fun findAny(): Optional<T>

    /**
     * {@inheritDoc}
     */
    override fun close()

    /**
     * {@inheritDoc}
     */
    override fun count(): Long
}
