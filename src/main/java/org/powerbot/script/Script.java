package org.powerbot.script;

import org.powerbot.util.ScriptBundle;

import java.lang.annotation.*;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Script
 * The base interface of a script.
 */
public interface Script extends EventListener {
	/**
	 * Returns the execution queue.
	 *
	 * @param state the state being invoked
	 * @return a sequence of {@link java.lang.Runnable} items to process
	 */
	List<Runnable> getExecQueue(State state);

	/**
	 * State
	 * All possible runtime states for a script.
	 */
	enum State {
		START, SUSPEND, RESUME, STOP
	}

	/**
	 * Controller
	 * A controller for a {@link Script} which invokes and determines state changes.
	 */
	interface Controller extends Suspendable, Stoppable {
		/**
		 * Adds a {@link java.lang.Runnable} to the executor.
		 *
		 * @param e a runnable to be executed
		 * @return {@code true} if the runnable was added, otherwise {@code false}
		 */
		boolean offer(Runnable e);

		/**
		 * Returns the primary {@link org.powerbot.script.AbstractScript} running with this {@link org.powerbot.script.Script.Controller}.
		 *
		 * @return the primary {@link org.powerbot.script.AbstractScript}
		 */
		AbstractScript script();

		/**
		 * Returns the associated {@link ScriptBundle}.
		 *
		 * @return the associated {@link ScriptBundle}
		 */
		ScriptBundle bundle();

		/**
		 * Returns the timings.
		 *
		 * @return the {@link System#nanoTime()} of each {@link State}
		 */
		AtomicLong[] times();

		/**
		 * Returns the list of daemon {@link Script}s.
		 *
		 * @return the collection
		 */
		List<Class<? extends Script>> daemons();
	}

	/**
	 * Manifest
	 * A {@link Script} descriptor.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@interface Manifest {
		/**
		 * The human-friendly name.
		 *
		 * @return the name
		 */
		String name();

		/**
		 * The description, which should be 140 characters or less.
		 *
		 * @return the description
		 */
		String description();

		/**
		 * The human-readable version number.
		 *
		 * @return the version
		 */
		String version() default "";

		/**
		 * Number of days allowed for a trial
		 *
		 * @return number of days
		 */
		long trialDays() default 3L;

		/**
		 * The name of a markdown file in the repository, which should be used as content for the script page
		 *
		 * @return name of a markdown file
		 */
		String markdownFileName() default "";

		/**
		 * Whether the script is private or public
		 *
		 * @return private/public
		 */
		boolean priv() default false;

		/**
		 * A series of key=value pairs separated by semicolons (;) or newlines,
		 * e.g. {@code "hidden=true;topic=1234"}.
		 *
		 * @return the properties
		 */
		String properties() default "";

		boolean mobileReady() default false;

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Repeatable(ScriptConfiguration.List.class)
	@interface ScriptConfiguration {


		/**
		 * The name of the configuration option, should be unique per script
		 *
		 * @return string
		 */
		String name();

		/**
		 * A description of the configuration option to present to the end user configuring it
		 *
		 * @return string
		 */
		String description();

		/**
		 * The data type of the configuration option
		 *
		 * @return OptionType
		 */
		ScriptConfigurationOption.OptionType optionType() default ScriptConfigurationOption.OptionType.STRING;

		/**
		 * The default value for this configuration option
		 *
		 * @return string
		 */
		String defaultValue() default "";

		String[] allowedValues() default {};

		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.TYPE})
		@interface List {
			ScriptConfiguration[] value();
		}
	}
}
