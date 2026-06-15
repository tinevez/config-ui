package org.scijava.ui.config;

import org.scijava.ui.config.Configurator.SelectableParameters;
import org.scijava.ui.config.Parameters.BooleanParam;
import org.scijava.ui.config.Parameters.ChoiceParam;
import org.scijava.ui.config.Parameters.DoubleParam;
import org.scijava.ui.config.Parameters.EnumParam;
import org.scijava.ui.config.Parameters.IntParam;
import org.scijava.ui.config.Parameters.PathParam;
import org.scijava.ui.config.Parameters.StringParam;

/**
 * Visitor interface for {@link Parameter} objects.
 */
public interface ParameterVisitor
{
	public default void visit( final BooleanParam booleanParam )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final StringParam stringParam )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final DoubleParam doubleParam )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final IntParam intParam )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final ChoiceParam choiceParam )
	{
		throw new UnsupportedOperationException();
	}

	public default < E extends Enum< E > > void visit( final EnumParam< E > enumParam )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final PathParam pathParam )
	{
		throw new UnsupportedOperationException();
	}

	public default void visit( final SelectableParameters selectable )
	{
		throw new UnsupportedOperationException();
	}
}
