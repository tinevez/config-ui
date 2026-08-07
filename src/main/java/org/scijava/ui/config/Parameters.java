/*-
 * #%L
 * A Java library to facilitate building user-interfaces configuring algorithms.
 * %%
 * Copyright (C) 2026 Institut Pasteur
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Institut Pasteur nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package org.scijava.ui.config;

import java.util.ArrayList;
import java.util.List;

import org.scijava.ui.config.utils.StringUtils;

/**
 * Container class for parameter types used by {@link Configurator}.
 * <p>
 * This class defines various parameter types such as {@link BooleanParam},
 * {@link IntParam}, {@link DoubleParam}, {@link StringParam}, {@link PathParam},
 * {@link ChoiceParam}, and {@link EnumParam}, as well as the base classes
 * {@link Parameter} and {@link BoundedValueParameter}.
 */
public class Parameters
{

	/**
	 * Base class for parameters.
	 *
	 * @param <T>
	 *            the implementing type of the parameter.
	 * @param <O>
	 *            the type of value this parameter accepts.
	 */
	@SuppressWarnings( "unchecked" )
	public static abstract class Parameter< T extends Parameter< T, O >, O >
	{

		protected boolean visible = true;

		protected String name;

		protected String help;

		private String key;

		private O value;

		private O defaultValue;

		private String units;

		private transient UpdateListener updateListener;

		T units( final String units )
		{
			this.units = units;
			return ( T ) this;
		}

		public String getUnits()
		{
			return units;
		}

		T defaultValue( final O defaultValue )
		{
			this.defaultValue = defaultValue;
			return ( T ) this;
		}

		public O getDefaultValue()
		{
			return defaultValue;
		}

		/**
		 * Sets the value of this parameter.
		 *
		 * @param value
		 *            the new value.
		 */
		public void set( final O value )
		{
			if ( value == null && this.value != null || value != null && this.value == null || !value.equals( this.value ) )
			{
				this.value = value;
				notifyUpdateListener();
			}
		}

		/**
		 * Returns the value of this parameter, or the default value if no value
		 * has been set.
		 *
		 * @return the value of this parameter, or the default value if no value
		 *         has been set.
		 */
		public O getValue()
		{
			return ( value == null ) ? defaultValue : value;
		}

		/**
		 * If <code>false</code>, this parameter won't be shown in UIs. It will
		 * be used for the command line builder nonetheless.
		 *
		 * @param visible
		 *            whether this parameter should be visible in the UI or not.
		 *            By default: <code>true</code>.
		 * @see CliGuiBuilder
		 * @return the parameter.
		 */
		T visible( final boolean visible )
		{
			this.visible = visible;
			return ( T ) this;
		}

		public boolean isVisible()
		{
			return visible;
		}

		T name( final String name )
		{
			this.name = name;
			return ( T ) this;
		}

		T help( final String help )
		{
			this.help = help;
			return ( T ) this;
		}

		/**
		 * Sets the String key to of this parameter, which will be used mainly
		 * for de / serialization purposes. The key must be unique within a
		 * config.
		 *
		 *
		 * @param key
		 *            the key to use.
		 * @return the parameter.
		 */
		T key( final String key )
		{

			this.key = key;
			return ( T ) this;
		}

		T updateListener( final UpdateListener updateListener )
		{
			this.updateListener = updateListener;
			return ( T ) this;
		}

		protected void notifyUpdateListener()
		{
			if ( updateListener != null )
				updateListener.parameterUpdated();
		}

		public String getName()
		{
			return name;
		}

		/**
		 * Returns the help text for this parameter.
		 *
		 * @return the help text, or {@code null} if none.
		 */
		public String getHelp()
		{
			return help;
		}

		/**
		 * Returns the key used to persist this parameter.
		 *
		 * @return the parameter key.
		 */
		public String getKey()
		{
			return key;
		}

		/**
		 * Accepts a visitor to process this parameter.
		 *
		 * @param visitor
		 *            the visitor to accept.
		 */
		public abstract void accept( final ParameterVisitor visitor );

		@Override
		public String toString()
		{
			return getName()
					+ " (" + this.getClass().getSimpleName() + ")\n"
					+ ( ( getHelp() == null )
							? " - no help\n"
							: " - help: " + getHelp() + "\n" )
					+ " - key: " + getKey() + "\n"
					+ " - value: " + getValue() + "\n"
					+ ( ( getUnits() == null )
							? ""
							: " - units: " + getUnits() + "\n" )
					+ " - default value: " + getDefaultValue() + "\n"
					+ " - visible: " + isVisible() + "\n";
		}
	}

	/**
	 * A boolean parameter type.
	 */
	public static class BooleanParam extends Parameter< BooleanParam, Boolean >
	{
		BooleanParam()
		{}

		/**
		 * Sets this flag parameter to <code>true</code>.
		 */
		public void set()
		{
			if ( getValue() == null || !getValue() )
			{
				set( true );
				notifyUpdateListener();
			}
		}

		@Override
		public void accept( final ParameterVisitor visitor )
		{
			visitor.visit( this );
		}
	}

	/**
	 * Specialization of {@link StringParam} to be used to enter file of folder
	 * path in a GUI.
	 */
	public static class PathParam extends AbstractStringParam< PathParam >
	{
		PathParam()
		{}

		@Override
		public void accept( final ParameterVisitor visitor )
		{
			visitor.visit( this );
		}
	}

	/**
	 * A string parameter type.
	 */
	public static class StringParam extends AbstractStringParam< StringParam >
	{
		StringParam()
		{}

		@Override
		public void accept( final ParameterVisitor visitor )
		{
			visitor.visit( this );
		}
	}

	/**
	 * Base class for parameters that use a string as internal value.
	 *
	 * @param <T>
	 *            the type of the parameter, used for chaining builders.
	 */
	public static abstract class AbstractStringParam< T extends AbstractStringParam< T > > extends Parameter< T, String >
	{}

	/**
	 * An integer parameter type with optional bounds.
	 */
	public static class IntParam extends BoundedValueParameter< IntParam, Integer >
	{
		IntParam()
		{}

		@Override
		public void accept( final ParameterVisitor visitor )
		{
			visitor.visit( this );
		}
	}

	/**
	 * A double parameter type with optional bounds.
	 */
	public static class DoubleParam extends BoundedValueParameter< DoubleParam, Double >
	{
		@Override
		public void accept( final ParameterVisitor visitor )
		{
			visitor.visit( this );
		}
	}

	/**
	 * A parameter type that accepts a value from a discrete list of choices.
	 */
	public static class ChoiceParam extends Parameter< ChoiceParam, String >
	{

		private final List< String > choices = new ArrayList<>();

		private final List< String > displays = new ArrayList<>();

		private int selected = -1; // -1 means no selection.;

		ChoiceParam()
		{}

		ChoiceParam addChoice( final String choice, final String displayed )
		{
			if ( !choices.contains( choice ) )
			{
				choices.add( choice );
				displays.add( displayed );
			}
			return this;
		}

		/**
		 * The list of the display strings corresponding to the possible
		 * choices.
		 *
		 * @return The list of the display strings.
		 */
		public List< String > getDisplays()
		{
			return displays;
		}

		@Override
		public void accept( final ParameterVisitor visitor )
		{
			visitor.visit( this );
		}

		@Override
		public String getValue()
		{
			if ( selected < 0 )
				return getDefaultValue();
			return choices.get( selected );
		}

		/**
		 * Returns the index of the currently selected choice.
		 *
		 * @return the selected index, or the index of the default value if no
		 *         selection has been made.
		 */
		public int getSelectedIndex()
		{
			if ( selected < 0 )
				return choices.indexOf( getDefaultValue() );
			return selected;
		}

		@Override
		public void set( final String choice )
		{
			final int sel = choices.indexOf( choice );
			if ( sel < 0 )
				throw new IllegalArgumentException( "Unknown selection '" + choice + "' for parameter '"
						+ name + "'. Must be one of: [ " + StringUtils.join( choices, ", " ) + " ]." );

			if ( sel != selected )
			{
				this.selected = sel;
				notifyUpdateListener();
			}
		}

		public void set( final int selected )
		{
			if ( selected < 0 || selected >= choices.size() )
				throw new IllegalArgumentException( "Invalid index for selection of parameter '"
						+ name + "'. Must be in scale " + 0 + " to " + ( choices.size() - 1 ) + " (among "
						+ StringUtils.join( choices, ", " ) + "), but was " + selected );
			if ( selected != this.selected )
			{
				this.selected = selected;
				notifyUpdateListener();
			}
		}

		@Override
		ChoiceParam defaultValue( final String defaultChoice )
		{
			final int sel = choices.indexOf( defaultChoice );
			if ( sel < 0 )
				throw new IllegalArgumentException( "Unknown selection '" + defaultChoice + "' for parameter '"
						+ name + "'. Must be one of " + StringUtils.join( choices, ", " ) + "." );
			super.defaultValue( defaultChoice );
			return this;
		}

		ChoiceParam defaultValue( final int selected )
		{
			if ( selected < 0 || selected >= choices.size() )
				throw new IllegalArgumentException( "Invalid index for selection of parameter '"
						+ name + "'. Must be in scale " + 0 + " to " + ( choices.size() - 1 ) + " in "
						+ StringUtils.join( choices, ", " ) + "." );
			super.defaultValue( choices.get( selected ) );
			return this;
		}

		@Override
		public String toString()
		{
			final String str = super.toString();
			return str
					+ " - choices: " + choices + "\n"
					+ " - display strings: " + displays + "\n";
		}
	}

	/**
	 * A parameter type that accepts a value from a Java enum.
	 *
	 * @param <E>
	 *            the type of the enum.
	 */
	public static class EnumParam< E extends Enum< E > > extends Parameter< EnumParam< E >, E >
	{

		private final Class< E > enumClass;

		/**
		 * Creates an enum parameter for the specified enum class.
		 *
		 * @param enumClass
		 *            the class of the enum type.
		 */
		EnumParam( final Class< E > enumClass )
		{
			this.enumClass = enumClass;
		}

		@Override
		public void accept( final ParameterVisitor visitor )
		{
			visitor.visit( this );
		}

		public Class< E > getEnumClass()
		{
			return enumClass;
		}
	}

	/**
	 * Base class for parameters that accept values that can be bounded by a min
	 * and max.
	 *
	 * @param <T>
	 *            the implementing type of the parameter.
	 * @param <O>
	 *            the type of value this parameter accepts.
	 */
	@SuppressWarnings( "unchecked" )
	public static abstract class BoundedValueParameter< T extends BoundedValueParameter< T, O >, O extends Comparable< O > > extends Parameter< T, O >
	{

		private BoundedValueParameter()
		{}

		private O min;

		private O max;

		@Override
		public void set( final O value )
		{
			if ( hasMax() && value.compareTo( max ) > 0 )
				throw new IllegalArgumentException( "Value " + value + " is higher than the maximum of " + max + "." );
			else if ( hasMin() && value.compareTo( min ) < 0 )
				throw new IllegalArgumentException( "Value " + value + " is lower than the minimum of " + min + "." );
			else
				super.set( value );
		}

		T min( final O min )
		{
			this.min = min;
			return ( T ) this;
		}

		/**
		 * Returns the maximum allowed value for this parameter.
		 *
		 * @return the maximum value, or {@code null} if no maximum is set.
		 */
		public O getMax()
		{
			return max;
		}

		T max( final O max )
		{
			this.max = max;
			return ( T ) this;
		}

		/**
		 * Returns the minimum allowed value for this parameter.
		 *
		 * @return the minimum value, or {@code null} if no minimum is set.
		 */
		public O getMin()
		{
			return min;
		}

		/**
		 * Returns whether this parameter has a minimum value set.
		 *
		 * @return {@code true} if a minimum is set, {@code false} otherwise.
		 */
		public boolean hasMin()
		{
			return min != null;
		}

		/**
		 * Returns whether this parameter has a maximum value set.
		 *
		 * @return {@code true} if a maximum is set, {@code false} otherwise.
		 */
		public boolean hasMax()
		{
			return max != null;
		}

		@Override
		public String toString()
		{
			final String str = super.toString();
			return str
					+ " - has min: " + hasMin() + "\n"
					+ ( hasMin()
							? " - min: " + getMin() + "\n"
							: "" )
					+ " - has max: " + hasMax() + "\n"
					+ ( hasMax()
							? " - max: " + getMax() + "\n"
							: "" );
		}
	}

	/**
	 * Listener interface for receiving notifications when a parameter value
	 * changes.
	 */
	public interface UpdateListener
	{
		/**
		 * Called when a parameter value has been updated.
		 */
		void parameterUpdated();
	}
}
