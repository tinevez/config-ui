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
package org.scijava.ui.config.visitors.gui.elements;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import org.drjekyll.fontchooser.FontDialog;
import org.scijava.ui.config.utils.GuiUtils;
import org.scijava.ui.config.utils.Icons;
import org.scijava.ui.config.visitors.gui.elements.colormap.Colormap;

/**
 * Widgets and style elements for the GUI visitor of {@link StyleElement}s.
 */
public class StyleElements
{

	/** Prevent instantiation of utility class. */
	private StyleElements()
	{}

	/** Number format for displaying decimal values. */
	public static DecimalFormat format = new DecimalFormat( "#.###" );

	/** Default font for UI elements. */
	public static Font FONT = UIManager.getFont( "Label.font" );

	/** Smaller font for secondary UI elements. */
	public static Font SMALL_FONT = FONT.deriveFont( FONT.getSize() * 0.8f );

	/**
	 * Creates a separator element.
	 *
	 * @return a new separator.
	 */
	public static Separator separator()
	{
		return new Separator();
	}

	/**
	 * Creates a label element.
	 *
	 * @param label
	 *            the label text.
	 * @return a new label element.
	 */
	public static LabelElement label( final String label )
	{
		return new LabelElement( label );
	}

	/**
	 * Creates a string element with getter and setter.
	 *
	 * @param label
	 *            the label text.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new string element.
	 */
	public static StringElement stringElement( final String label, final Supplier< String > get, final Consumer< String > set )
	{
		return new StringElement( label )
		{

			@Override
			public String get()
			{
				return get.get();
			}

			@Override
			public void set( final String s )
			{
				set.accept( s );
			}
		};
	}

	/**
	 * Creates a boolean element with getter and setter.
	 *
	 * @param label
	 *            the label text.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new boolean element.
	 */
	public static BooleanElement booleanElement( final String label, final BooleanSupplier get, final Consumer< Boolean > set )
	{
		return new BooleanElement( label )
		{
			@Override
			public boolean get()
			{
				return get.getAsBoolean();
			}

			@Override
			public void set( final boolean b )
			{
				set.accept( b );
			}
		};
	}

	/**
	 * Creates a color element with getter and setter.
	 *
	 * @param label
	 *            the label text.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new color element.
	 */
	public static ColorElement colorElement( final String label, final Supplier< Color > get, final Consumer< Color > set )
	{
		return new ColorElement( label )
		{
			@Override
			public Color getColor()
			{
				return get.get();
			}

			@Override
			public void setColor( final Color c )
			{
				set.accept( c );
			}
		};
	}

	/**
	 * Creates a colormap element with getter and setter.
	 *
	 * @param label
	 *            the label text.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new colormap element.
	 */
	public static ColormapElement colormapElement( final String label, final Supplier< Colormap > get, final Consumer< Colormap > set )
	{
		return new ColormapElement( label )
		{

			@Override
			public Colormap get()
			{
				return get.get();
			}

			@Override
			public void set( final Colormap v )
			{
				set.accept( v );
			}
		};
	}

	/**
	 * Creates a bounded double element with range and getter/setter.
	 *
	 * @param label
	 *            the label text.
	 * @param rangeMin
	 *            the minimum value.
	 * @param rangeMax
	 *            the maximum value.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new bounded double element.
	 */
	public static BoundedDoubleElement boundedDoubleElement( final String label, final double rangeMin, final double rangeMax, final DoubleSupplier get, final Consumer< Double > set )
	{
		return new BoundedDoubleElement( label, rangeMin, rangeMax )
		{
			@Override
			public double get()
			{
				return get.getAsDouble();
			}

			@Override
			public void set( final double v )
			{
				set.accept( v );
			}
		};
	}

	/**
	 * Creates a double element with getter and setter.
	 *
	 * @param label
	 *            the label text.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new double element.
	 */
	public static DoubleElement doubleElement( final String label, final DoubleSupplier get, final Consumer< Double > set )
	{
		return new DoubleElement( label )
		{
			@Override
			public double get()
			{
				return get.getAsDouble();
			}

			@Override
			public void set( final double v )
			{
				set.accept( v );
			}
		};
	}

	/**
	 * Creates an integer element with range and getter/setter.
	 *
	 * @param label
	 *            the label text.
	 * @param rangeMin
	 *            the minimum value.
	 * @param rangeMax
	 *            the maximum value.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new integer element.
	 */
	public static IntElement intElement( final String label, final int rangeMin, final int rangeMax, final IntSupplier get, final Consumer< Integer > set )
	{
		return new IntElement( label, rangeMin, rangeMax )
		{
			@Override
			public int get()
			{
				return get.getAsInt();
			}

			@Override
			public void set( final int v )
			{
				set.accept( v );
			}
		};
	}

	/**
	 * Creates an enum element with getter and setter.
	 *
	 * @param <E>
	 *            the enum type.
	 * @param label
	 *            the label text.
	 * @param values
	 *            the array of possible values.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new enum element.
	 */
	public static < E > EnumElement< E > enumElement( final String label, final E[] values, final Supplier< E > get, final Consumer< E > set )
	{
		return new EnumElement< E >( label, values )
		{

			@Override
			public E getValue()
			{
				return get.get();
			}

			@Override
			public void setValue( final E e )
			{
				set.accept( e );
			}
		};
	}

	/**
	 * Creates a list element with getter and setter.
	 *
	 * @param <E>
	 *            the element type.
	 * @param label
	 *            the label text.
	 * @param values
	 *            the list of possible values.
	 * @param get
	 *            the getter function.
	 * @param set
	 *            the setter function.
	 * @return a new list element.
	 */
	public static < E > ListElement< E > listElement( final String label, final List< E > values, final Supplier< E > get, final Consumer< E > set )
	{
		return new ListElement< E >( label, values )
		{

			@Override
			public E getValue()
			{
				return get.get();
			}

			@Override
			public void setValue( final E e )
			{
				set.accept( e );
			}
		};
	}

	/*
	 *
	 * ===============================================================
	 *
	 */

	/** Separator style element. */
	public static class Separator implements StyleElement
	{
		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}
	}

	/** Label style element. */
	public static class LabelElement implements StyleElement
	{
		private final String label;

		/**
		 * Creates a label element.
		 *
		 * @param label
		 *            the label text.
		 */
		public LabelElement( final String label )
		{
			this.label = label;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}
	}

	/** String element base class. */
	public static abstract class StringElement implements StyleElement
	{

		private final ArrayList< Consumer< String > > onSet = new ArrayList<>();

		private final String label;

		private String value;

		/**
		 * Creates a string element.
		 *
		 * @param label
		 *            the label text.
		 */
		public StringElement( final String label )
		{
			this.label = label;
			this.value = "";
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Gets the current string value.
		 *
		 * @return the current value.
		 */
		public abstract String get();

		/**
		 * Sets the string value.
		 *
		 * @param s
		 *            the new value.
		 */
		public abstract void set( String s );

		/**
		 * Registers a consumer to be called when the value changes.
		 *
		 * @param set
		 *            the consumer.
		 */
		public void onSet( final Consumer< String > set )
		{
			onSet.add( set );
		}

		@Override
		public void update()
		{
			if ( get() != value )
				value = get();
			onSet.forEach( c -> c.accept( get() ) );
		}
	}

	/**
	 * Enum element base class.
	 *
	 * @param <E>
	 *            the enum type
	 */
	public static abstract class EnumElement< E > implements StyleElement
	{
		private final ArrayList< Consumer< E > > onSet = new ArrayList<>();

		private final String label;

		private final E[] values;

		/**
		 * Creates an enum element.
		 *
		 * @param label
		 *            the label text.
		 * @param values
		 *            the array of possible values.
		 */
		public EnumElement( final String label, final E[] values )
		{
			this.label = label;
			this.values = values;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Registers a consumer to be called when the value changes.
		 *
		 * @param set
		 *            the consumer.
		 */
		public void onSet( final Consumer< E > set )
		{
			onSet.add( set );
		}

		@Override
		public void update()
		{
			onSet.forEach( c -> c.accept( getValue() ) );
		}

		/**
		 * Gets the current enum value.
		 *
		 * @return the current value.
		 */
		public abstract E getValue();

		/**
		 * Sets the enum value.
		 *
		 * @param e
		 *            the new value.
		 */
		public abstract void setValue( E e );

		/**
		 * Returns the array of possible values.
		 *
		 * @return the values array.
		 */
		public E[] getValues()
		{
			return values;
		}
	}

	/**
	 * List element base class.
	 *
	 * @param <E>
	 *            the element type
	 */
	public static abstract class ListElement< E > implements StyleElement
	{
		private final ArrayList< Consumer< E > > onSet = new ArrayList<>();

		private final String label;

		private final List< E > values;

		/**
		 * Creates a list element.
		 *
		 * @param label
		 *            the label text.
		 * @param values
		 *            the list of possible values.
		 */
		public ListElement( final String label, final List< E > values )
		{
			this.label = label;
			this.values = values;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Registers a consumer to be called when the value changes.
		 *
		 * @param set
		 *            the consumer.
		 */
		public void onSet( final Consumer< E > set )
		{
			onSet.add( set );
		}

		@Override
		public void update()
		{
			onSet.forEach( c -> c.accept( getValue() ) );
		}

		/**
		 * Gets the current value.
		 *
		 * @return the current value.
		 */
		public abstract E getValue();

		/**
		 * Sets the value.
		 *
		 * @param e
		 *            the new value.
		 */
		public abstract void setValue( E e );

		/**
		 * Returns the list of possible values.
		 *
		 * @return the values list.
		 */
		public List< E > getValues()
		{
			return values;
		}
	}

	/** Color element base class. */
	public static abstract class ColorElement implements StyleElement
	{
		private final ArrayList< Consumer< Color > > onSet = new ArrayList<>();

		private final String label;

		/**
		 * Creates a color element.
		 *
		 * @param label
		 *            the label text.
		 */
		public ColorElement( final String label )
		{
			this.label = label;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Registers a consumer to be called when the value changes.
		 *
		 * @param set
		 *            the consumer.
		 */
		public void onSet( final Consumer< Color > set )
		{
			onSet.add( set );
		}

		@Override
		public void update()
		{
			onSet.forEach( c -> c.accept( getColor() ) );
		}

		/**
		 * Gets the current color.
		 *
		 * @return the current color.
		 */
		public abstract Color getColor();

		/**
		 * Sets the color.
		 *
		 * @param c
		 *            the new color.
		 */
		public abstract void setColor( Color c );
	}

	/** Boolean element base class. */
	public static abstract class BooleanElement implements StyleElement
	{
		private final String label;

		private final ArrayList< Consumer< Boolean > > onSet = new ArrayList<>();

		/**
		 * Creates a boolean element.
		 *
		 * @param label
		 *            the label text.
		 */
		public BooleanElement( final String label )
		{
			this.label = label;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Registers a consumer to be called when the value changes.
		 *
		 * @param set
		 *            the consumer.
		 */
		public void onSet( final Consumer< Boolean > set )
		{
			onSet.add( set );
		}

		@Override
		public void update()
		{
			onSet.forEach( c -> c.accept( get() ) );
		}

		/**
		 * Gets the current boolean value.
		 *
		 * @return the current value.
		 */
		public abstract boolean get();

		/**
		 * Sets the boolean value.
		 *
		 * @param b
		 *            the new value.
		 */
		public abstract void set( boolean b );
	}

	/** Bounded double element base class. */
	public static abstract class BoundedDoubleElement implements StyleElement
	{
		private final BoundedValueDouble value;

		private final String label;

		/**
		 * Creates a bounded double element.
		 *
		 * @param label
		 *            the label text.
		 * @param rangeMin
		 *            the minimum value.
		 * @param rangeMax
		 *            the maximum value.
		 */
		public BoundedDoubleElement( final String label, final double rangeMin, final double rangeMax )
		{
			final double currentValue = Math.max( rangeMin, Math.min( rangeMax, get() ) );
			value = new BoundedValueDouble( rangeMin, rangeMax, currentValue )
			{
				@Override
				public void setCurrentValue( final double value )
				{
					super.setCurrentValue( value );
					if ( get() != getCurrentValue() )
						set( getCurrentValue() );
				}
			};
			this.label = label;
		}

		/**
		 * Returns the bounded value model.
		 *
		 * @return the BoundedValueDouble.
		 */
		public BoundedValueDouble getValue()
		{
			return value;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Gets the current double value.
		 *
		 * @return the current value.
		 */
		public abstract double get();

		/**
		 * Sets the double value.
		 *
		 * @param v
		 *            the new value.
		 */
		public abstract void set( double v );

		@Override
		public void update()
		{
			if ( get() != value.getCurrentValue() )
				value.setCurrentValue( get() );
		}
	}

	/** Double element base class. */
	public static abstract class DoubleElement implements StyleElement
	{

		private final ArrayList< Consumer< Double > > onSet = new ArrayList<>();

		private double value;

		private final String label;

		/**
		 * Creates a double element.
		 *
		 * @param label
		 *            the label text.
		 */
		public DoubleElement( final String label )
		{
			value = 0.;
			this.label = label;
		}

		/**
		 * Returns the current value.
		 *
		 * @return the value.
		 */
		public double getValue()
		{
			return value;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Gets the current double value.
		 *
		 * @return the current value.
		 */
		public abstract double get();

		/**
		 * Sets the double value.
		 *
		 * @param v
		 *            the new value.
		 */
		public abstract void set( double v );

		/**
		 * Registers a consumer to be called when the value changes.
		 *
		 * @param set
		 *            the consumer.
		 */
		public void onSet( final Consumer< Double > set )
		{
			onSet.add( set );
		}

		@Override
		public void update()
		{
			if ( get() != value )
				value = get();
			onSet.forEach( c -> c.accept( get() ) );
		}
	}

	/** Integer element base class. */
	public static abstract class IntElement implements StyleElement
	{
		private final BoundedValue value;

		private final String label;

		/**
		 * Creates an integer element.
		 *
		 * @param label
		 *            the label text.
		 * @param rangeMin
		 *            the minimum value.
		 * @param rangeMax
		 *            the maximum value.
		 */
		public IntElement( final String label, final int rangeMin, final int rangeMax )
		{
			final int currentValue = Math.max( rangeMin, Math.min( rangeMax, get() ) );
			value = new BoundedValue( rangeMin, rangeMax, currentValue )
			{
				@Override
				public void setCurrentValue( final int value )
				{
					super.setCurrentValue( value );
					if ( get() != getCurrentValue() )
						set( getCurrentValue() );
				}
			};
			this.label = label;
		}

		/**
		 * Returns the bounded value model.
		 *
		 * @return the BoundedValue.
		 */
		public BoundedValue getValue()
		{
			return value;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Gets the current integer value.
		 *
		 * @return the current value.
		 */
		public abstract int get();

		/**
		 * Sets the integer value.
		 *
		 * @param v
		 *            the new value.
		 */
		public abstract void set( int v );

		@Override
		public void update()
		{
			if ( get() != value.getCurrentValue() )
				value.setCurrentValue( get() );
		}
	}

	/** Colormap element base class. */
	public static abstract class ColormapElement implements StyleElement
	{
		private final ArrayList< Consumer< Colormap > > onSet = new ArrayList<>();

		private final String label;

		/**
		 * Creates a colormap element.
		 *
		 * @param label
		 *            the label text.
		 */
		public ColormapElement( final String label )
		{
			this.label = label;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Gets the current colormap.
		 *
		 * @return the current colormap.
		 */
		public abstract Colormap get();

		/**
		 * Sets the colormap.
		 *
		 * @param v
		 *            the new colormap.
		 */
		public abstract void set( Colormap v );

		/**
		 * Registers a consumer to be called when the value changes.
		 *
		 * @param set
		 *            the consumer.
		 */
		public void onSet( final Consumer< Colormap > set )
		{
			onSet.add( set );
		}

		@Override
		public void update()
		{
			onSet.forEach( c -> c.accept( get() ) );
		}
	}

	/** Font element base class. */
	public static abstract class FontElement implements StyleElement
	{

		private final ArrayList< Consumer< Font > > onSet = new ArrayList<>();

		private Font value;

		private final String label;

		/**
		 * Creates a font element.
		 *
		 * @param label
		 *            the label text.
		 */
		public FontElement( final String label )
		{
			this.label = label;
		}

		/**
		 * Returns the current font value.
		 *
		 * @return the font.
		 */
		public Font getValue()
		{
			return value;
		}

		/**
		 * Returns the label text.
		 *
		 * @return the label.
		 */
		public String getLabel()
		{
			return label;
		}

		@Override
		public void accept( final StyleElementVisitor visitor )
		{
			visitor.visit( this );
		}

		/**
		 * Gets the current font.
		 *
		 * @return the current font.
		 */
		public abstract Font get();

		/**
		 * Sets the font.
		 *
		 * @param font
		 *            the new font.
		 */
		public abstract void set( Font font );

		/**
		 * Registers a consumer to be called when the value changes.
		 *
		 * @param set
		 *            the consumer.
		 */
		public void onSet( final Consumer< Font > set )
		{
			onSet.add( set );
		}

		@Override
		public void update()
		{
			if ( get() != value )
				value = get();
		}
	}

	/*
	 *
	 * ===============================================================
	 *
	 */

	/**
	 * Creates a label linked to a LabelElement.
	 *
	 * @param element
	 *            the label element.
	 * @return a new JLabel.
	 */
	public static JLabel linkedLabel( final LabelElement element )
	{
		return new JLabel( element.getLabel() );
	}

	/**
	 * Creates a colormap chooser linked to a ColormapElement.
	 *
	 * @param element
	 *            the colormap element.
	 * @return a new JComboBox of Colormaps.
	 */
	public static JComboBox< Colormap > linkedColormapChooser( final ColormapElement element )
	{
		final JComboBox< Colormap > cb = new JComboBox< Colormap >(
				Colormap.getAvailableLUTs().toArray( new Colormap[] {} ) );
		cb.setRenderer( new ColormapRenderer() );
		cb.setSelectedItem( element.get() );
		cb.addActionListener( e -> element.set( ( Colormap ) cb.getSelectedItem() ) );
		element.onSet( cm -> {
			if ( cm != cb.getSelectedItem() )
				cb.setSelectedItem( cm );
		} );
		return cb;
	}

	private static final class ColormapRenderer extends JPanel implements ListCellRenderer< Colormap >
	{

		private static final long serialVersionUID = 1L;

		private Colormap lut = Colormap.Jet;

		private final DefaultListCellRenderer lbl;

		public ColormapRenderer()
		{
			setPreferredSize( new Dimension( 150, 20 ) );
			final BoxLayout itemlayout = new BoxLayout( this, BoxLayout.LINE_AXIS );
			this.lbl = new DefaultListCellRenderer();
			setLayout( itemlayout );
			add( lbl );
			add( Box.createHorizontalGlue() );
			add( new JComponent()
			{

				private static final long serialVersionUID = 1L;

				@Override
				public void paint( final Graphics g )
				{

					final int width = getWidth();
					final int height = getHeight();
					for ( int i = 0; i < width; i++ )
					{
						final double beta = ( double ) i / ( width - 1 );
						g.setColor( lut.getPaint( beta ) );
						g.drawLine( i, 0, i, height );
					}
					g.setColor( this.getParent().getBackground() );
					g.drawRect( 0, 0, width, height );
				}

				@Override
				public Dimension getMaximumSize()
				{
					return new Dimension( 100, 20 );
				}

				@Override
				public Dimension getPreferredSize()
				{
					return getMaximumSize();
				}
			} );
		}

		@Override
		public Component getListCellRendererComponent(
				final JList< ? extends Colormap > list,
				final Colormap value,
				final int index,
				final boolean isSelected,
				final boolean cellHasFocus )
		{
			this.lut = value;
			lbl.getListCellRendererComponent( list, value.getName(), index, isSelected, cellHasFocus );
			setBackground( lbl.getBackground() );
			return this;
		}
	}

	/**
	 * Creates a checkbox linked to a BooleanElement.
	 *
	 * @param element
	 *            the boolean element.
	 * @param label
	 *            the checkbox label.
	 * @return a new JCheckBox.
	 */
	public static JCheckBox linkedCheckBox( final BooleanElement element, final String label )
	{
		final JCheckBox checkbox = new JCheckBox( label, element.get() );
		checkbox.addActionListener( ( e ) -> element.set( checkbox.isSelected() ) );
		element.onSet( b -> {
			if ( b != checkbox.isSelected() )
				checkbox.setSelected( b );
		} );
		return checkbox;
	}

	/**
	 * Creates a color button linked to a ColorElement.
	 *
	 * @param element
	 *            the color element.
	 * @param colorChooser
	 *            the color chooser dialog.
	 * @return a new JButton with color icon.
	 */
	public static JButton linkedColorButton( final ColorElement element, final JColorChooser colorChooser )
	{
		final ColorIcon icon = new ColorIcon( element.getColor(), 16, 0 );
		final JButton button = new JButton( icon );
		button.setOpaque( false );
		button.setContentAreaFilled( false );
		button.setBorderPainted( false );
		button.setFont( new JButton().getFont() );
		button.setMargin( new Insets( 0, 0, 0, 0 ) );
		button.setBorder( new EmptyBorder( 2, 5, 2, 2 ) );
		button.setHorizontalAlignment( SwingConstants.LEFT );
		button.addActionListener( e -> {
			colorChooser.setColor( element.getColor() );
			final JDialog d = JColorChooser.createDialog( button, "Choose a color", true, colorChooser, new ActionListener()
			{
				@Override
				public void actionPerformed( final ActionEvent arg0 )
				{
					final Color c = colorChooser.getColor();
					if ( c != null )
					{
						icon.setColor( c );
						button.repaint();
						element.setColor( c );
					}
				}
			}, null );
			d.setVisible( true );
		} );
		element.onSet( icon::setColor );
		return button;
	}

	/**
	 * Creates a slider panel linked to an IntElement.
	 *
	 * @param element
	 *            the integer element.
	 * @param tfCols
	 *            number of columns for the text field.
	 * @return a new SliderPanel.
	 */
	public static SliderPanel linkedSliderPanel( final IntElement element, final int tfCols )
	{
		final SliderPanel slider = new SliderPanel( null, element.getValue(), 1 );
		slider.setNumColummns( tfCols );
		slider.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
		return slider;
	}

	/**
	 * Creates a spinner linked to an IntElement.
	 *
	 * @param element
	 *            the integer element.
	 * @return a new JSpinner.
	 */
	public static JSpinner linkedSpinner( final IntElement element )
	{
		final BoundedValue value = element.getValue();
		final SpinnerNumberModel model = new SpinnerNumberModel( element.get(), value.getRangeMin(), value.getRangeMax(), 1 );
		final JSpinner spinner = new JSpinner( model );
		spinner.setMaximumSize( new Dimension( 80, spinner.getMaximumSize().height ) );
		model.addChangeListener( e -> element.set( ( ( Number ) model.getValue() ).intValue() ) );
		value.setUpdateListener( () -> {
			if ( value.getCurrentValue() != ( ( Number ) model.getValue() ).intValue() )
				model.setValue( value.getCurrentValue() );
		} );
		return spinner;
	}

	/**
	 * Creates a slider panel linked to a BoundedDoubleElement.
	 *
	 * @param element
	 *            the bounded double element.
	 * @param tfCols
	 *            number of columns for the text field.
	 * @return a new SliderPanelDouble.
	 */
	public static SliderPanelDouble linkedSliderPanel( final BoundedDoubleElement element, final int tfCols )
	{
		return linkedSliderPanel( element, tfCols, 1. );
	}

	/**
	 * Creates a slider panel linked to a BoundedDoubleElement with custom step
	 * size.
	 *
	 * @param element
	 *            the bounded double element.
	 * @param tfCols
	 *            number of columns for the text field.
	 * @param stepSize
	 *            the step size for the slider.
	 * @return a new SliderPanelDouble.
	 */
	public static SliderPanelDouble linkedSliderPanel( final BoundedDoubleElement element, final int tfCols, final double stepSize )
	{
		final SliderPanelDouble slider = new SliderPanelDouble( null, element.getValue(), stepSize );
		slider.setDecimalFormat( "0.####" );
		slider.setNumColummns( tfCols );
		slider.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
		return slider;
	}

	/**
	 * Creates a spinner linked to an EnumElement.
	 *
	 * @param <E>
	 *            the enum type.
	 * @param element
	 *            the enum element.
	 * @return a new JSpinner.
	 */
	@SuppressWarnings( "unchecked" )
	public static < E > JSpinner linkedSpinnerEnumSelector( final EnumElement< E > element )
	{
		final SpinnerListModel model = new SpinnerListModel( element.getValues() );
		final JSpinner spinner = new JSpinner( model );
		spinner.setFont( SMALL_FONT );
		( ( DefaultEditor ) spinner.getEditor() ).getTextField().setEditable( false );
		model.setValue( element.getValue() );
		model.addChangeListener( e -> element.setValue( ( E ) model.getValue() ) );
		element.onSet( e -> {
			if ( e != model.getValue() )
				model.setValue( e );
		} );
		return spinner;
	}

	/**
	 * Creates a combo box linked to an EnumElement.
	 *
	 * @param <E>
	 *            the enum type.
	 * @param element
	 *            the enum element.
	 * @return a new JComboBox.
	 */
	@SuppressWarnings( "unchecked" )
	public static < E > JComboBox< E > linkedComboBoxEnumSelector( final EnumElement< E > element )
	{
		final DefaultComboBoxModel< E > model = new DefaultComboBoxModel<>( element.values );
		final JComboBox< E > cb = new JComboBox<>( model );
		cb.setFont( SMALL_FONT );
		cb.addActionListener( e -> element.setValue( ( E ) model.getSelectedItem() ) );
		element.onSet( e -> {
			if ( e != model.getSelectedItem() )
				model.setSelectedItem( e );
		} );
		return cb;
	}

	/**
	 * Creates a combo box linked to a ListElement.
	 *
	 * @param <E>
	 *            the element type.
	 * @param element
	 *            the list element.
	 * @return a new JComboBox.
	 */
	@SuppressWarnings( "unchecked" )
	public static < E > JComboBox< E > linkedComboBoxSelector( final ListElement< E > element )
	{
		final DefaultComboBoxModel< E > model = new DefaultComboBoxModel<>( new Vector<>( element.values ) );
		final JComboBox< E > cb = new JComboBox<>( model );
		cb.setFont( SMALL_FONT );
		cb.addActionListener( e -> element.setValue( ( E ) model.getSelectedItem() ) );
		element.onSet( e -> {
			if ( e != model.getSelectedItem() )
				model.setSelectedItem( e );
		} );
		return cb;
	}

	/**
	 * Create a JFormattedTextField linked to a DoubleElement. The value of the
	 * text field is updated when the element is updated and vice versa. The
	 * text field commits the edit on focus lost and on enter.
	 * 
	 * @param element
	 *            the DoubleElement to link to the text field.
	 * @param min
	 *            the minimum value of the text field (inclusive). The text
	 *            field will not allow values less than this. If null, no
	 *            minimum is enforced.
	 * @param max
	 *            the maximum value of the text field (inclusive). The text
	 *            field will not allow values greater than this. If null, no
	 *            maximum is enforced.
	 * @return a JFormattedTextField linked to the given DoubleElement.
	 */
	public static JFormattedTextField linkedFormattedTextField( final DoubleElement element, final Double min, final Double max )
	{
		final JFormattedTextField ftf = new JFormattedTextField( format );
		ftf.setHorizontalAlignment( JFormattedTextField.RIGHT );
		ftf.setValue( Double.valueOf( element.get() ) );

		final Runnable validateAndSet = () -> {
			double value = ( ( Number ) ftf.getValue() ).doubleValue();
			if ( min != null && value < min )
			{
				value = min;
				ftf.setValue( Double.valueOf( value ) );
			}
			if ( max != null && value > max )
			{
				value = max;
				ftf.setValue( Double.valueOf( value ) );
			}
			element.set( value );
		};

		ftf.addActionListener( e -> validateAndSet.run() );
		ftf.addFocusListener( new FocusAdapter()
		{
			@Override
			public void focusLost( final java.awt.event.FocusEvent e )
			{
				try
				{
					ftf.commitEdit();
					validateAndSet.run();
				}
				catch ( final ParseException e1 )
				{
					// Ignore parse error - invalid input won't be committed.
				}
			}
		} );
		GuiUtils.selectAllOnFocus( ftf );
		element.onSet( d -> {
			if ( d != ( ( Number ) ftf.getValue() ).doubleValue() )
				ftf.setValue( Double.valueOf( element.value ) );
		} );

		return ftf;
	}

	/**
	 * Creates a text field linked to a StringElement.
	 *
	 * @param element
	 *            the string element.
	 * @return a new JTextField.
	 */
	public static JTextField linkedTextField( final StringElement element )
	{
		final JTextField tf = new JTextField( element.get() );
		tf.setHorizontalAlignment( JFormattedTextField.LEFT );

		tf.addActionListener( e -> element.set( tf.getText() ) );
		GuiUtils.selectAllOnFocus( tf );
		tf.addFocusListener( new FocusAdapter()
		{
			@Override
			public void focusLost( final java.awt.event.FocusEvent e )
			{
				element.set( tf.getText() );
			}
		} );
		element.onSet( d -> {
			if ( d != tf.getText() )
				tf.setText( element.value );
		} );

		return tf;
	}

	/**
	 * Creates a font selection button linked to a FontElement.
	 *
	 * @param element
	 *            the font element.
	 * @param parent
	 *            the parent window for the font dialog.
	 * @return a new JButton.
	 */
	public static JButton linkedFontButton( final FontElement element, final Window parent )
	{
		final JButton btn = new JButton( "Select font" );
		btn.setFont( element.get() );
		btn.addPropertyChangeListener( "font", e -> element.set( btn.getFont() ) );
		element.onSet( font -> {
			if ( !font.equals( btn.getFont() ) )
				btn.setFont( font );
		} );
		btn.addActionListener( e -> {
			final FontDialog dialog = new FontDialog( parent, "Select font for TrackMate display", ModalityType.APPLICATION_MODAL );
			dialog.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
			dialog.setSelectedFont( btn.getFont() );
			GuiUtils.positionWindow( dialog, parent );
			dialog.setIconImage( Icons.FONT_SELECT.getImage() );
			dialog.setVisible( true );
			if ( !dialog.isCancelSelected() )
				btn.setFont( dialog.getSelectedFont() );
		} );
		return btn;
	}

	public static FontElement fontElement( final String label, final Supplier< Font > get, final Consumer< Font > set )
	{
		return new FontElement( label )
		{

			@Override
			public void set( final Font font )
			{
				set.accept( font );
			}

			@Override
			public Font get()
			{
				return get.get();
			}
		};
	}
}
