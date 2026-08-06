package org.scijava.ui.config.visitors.gui.elements;

import org.scijava.ui.config.visitors.gui.elements.StyleElements.BooleanElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.BoundedDoubleElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.ColorElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.ColormapElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.DoubleElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.EnumElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.FontElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.IntElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.LabelElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.ListElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.Separator;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.StringElement;

/**
 * A visitor for {@link StyleElement}s. It has a default implementation for each
 * type of element that throws an {@link UnsupportedOperationException}. This
 * allows to implement only the methods that are needed.
 * <p>
 * Based on a design by Tobias Pietzsch in Mastodon.
 */
public interface StyleElementVisitor
{
	public default void visit( final Separator element )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final LabelElement label )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final ColorElement colorElement )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final BooleanElement booleanElement )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final BoundedDoubleElement doubleElement )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final DoubleElement doubleElement )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final IntElement intElement )
	{
		throw new UnsupportedOperationException();
	}

	public default < E > void visit( final EnumElement< E > enumElement )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final ColormapElement element )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final StringElement stringElement )
	{
		throw new UnsupportedOperationException();
	}

	public default < E > void visit( final ListElement< E > listElement )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final FontElement element )
	{
		throw new UnsupportedOperationException();
	}
}
