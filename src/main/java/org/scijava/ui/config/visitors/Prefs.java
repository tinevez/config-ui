package org.scijava.ui.config.visitors;

import org.scijava.prefs.DefaultPrefService;
import org.scijava.ui.config.Configurator;
import org.scijava.ui.config.Configurator.SelectableParameters;
import org.scijava.ui.config.ParameterVisitor;
import org.scijava.ui.config.Parameters.BooleanParam;
import org.scijava.ui.config.Parameters.ChoiceParam;
import org.scijava.ui.config.Parameters.DoubleParam;
import org.scijava.ui.config.Parameters.EnumParam;
import org.scijava.ui.config.Parameters.IntParam;
import org.scijava.ui.config.Parameters.Parameter;
import org.scijava.ui.config.Parameters.PathParam;
import org.scijava.ui.config.Parameters.StringParam;

/**
 * Serializes and deserializes Configurator values to and from a PrefService.
 */
public class Prefs
{

	/**
	 * Saves the values of the specified Configurator to the PrefService.
	 * 
	 * @param <C>
	 *            the type of the Configurator to serialize.
	 * @param config
	 *            the Configurator to save the values of.
	 */
	public static < C extends Configurator > void serialize( final C config )
	{
		final PrefSerializeVisitor< C > visitor = new PrefSerializeVisitor<>( config );
		config.getParameters().forEach( p -> p.accept( visitor ) );
		config.getSelectables().forEach( s -> s.accept( visitor ) );
	}

	/**
	 * Reloads the values of the specified Configurator from the PrefService.
	 * 
	 * @param <C>
	 *            the type of the Configurator to deserialize.
	 * @param config
	 *            the Configurator to fill with the values from the PrefService.
	 */
	public static < C extends Configurator > void deserialize( final C config )
	{
		final PrefDeserializeVisitor< C > visitor = new PrefDeserializeVisitor<>( config );
		config.getParameters().forEach( p -> p.accept( visitor ) );
		config.getSelectables().forEach( s -> s.accept( visitor ) );
	}

	private static class PrefSerializeVisitor< C extends Configurator > implements ParameterVisitor
	{
		private final DefaultPrefService prefs;

		private final C config;

		public PrefSerializeVisitor( final C config )
		{
			this.config = config;
			this.prefs = new DefaultPrefService();
		}

		@Override
		public < E extends Enum< E > > void visit( final EnumParam< E > enumParam )
		{
			prefs.put( config.getClass(), enumParam.getKey(), enumParam.getValue().name() );
		}

		@Override
		public void visit( final BooleanParam booleanParam )
		{
			prefs.put( config.getClass(), booleanParam.getKey(), booleanParam.getValue() );
		}

		@Override
		public void visit( final DoubleParam doubleParam )
		{
			prefs.put( config.getClass(), doubleParam.getKey(), doubleParam.getValue() );
		}

		@Override
		public void visit( final IntParam intParam )
		{
			prefs.put( config.getClass(), intParam.getKey(), intParam.getValue() );
		}

		@Override
		public void visit( final ChoiceParam choiceParam )
		{
			prefs.put( config.getClass(), choiceParam.getKey(), choiceParam.getValue() );
		}

		@Override
		public void visit( final StringParam stringParam )
		{
			prefs.put( config.getClass(), stringParam.getKey(), stringParam.getValue() );
		}

		@Override
		public void visit( final PathParam pathParam )
		{
			prefs.put( config.getClass(), pathParam.getKey(), pathParam.getValue() );
		}

		@Override
		public void visit( final SelectableParameters selectable )
		{
			prefs.put( config.getClass(), selectable.getKey(), selectable.getSelection().getKey() );
		}
	}

	private static class PrefDeserializeVisitor< C extends Configurator > implements ParameterVisitor
	{
		private final DefaultPrefService prefs;

		private final C config;

		public PrefDeserializeVisitor( final C config )
		{
			this.config = config;
			this.prefs = new DefaultPrefService();
		}

		@Override
		public < E extends Enum< E > > void visit( final EnumParam< E > enumParam )
		{
			final String str = prefs.get( config.getClass(), enumParam.getKey() );
			if ( str == null )
				return; // no value saved for this parameter, skip it
			try
			{
				@SuppressWarnings( { "unchecked", "rawtypes" } )
				final E enumVal = ( E ) Enum.valueOf( ( Class< ? extends Enum > ) enumParam.getEnumClass(), str );
				enumParam.set( enumVal );
			}
			catch ( final IllegalArgumentException exc )
			{
				System.err.println( "Couldn't parse enum value " + str + " for parameter " + enumParam.getKey() + " of type " + enumParam.getEnumClass().getName() );
				exc.printStackTrace();
			}
		}

		@Override
		public void visit( final BooleanParam booleanParam )
		{
			booleanParam.set( prefs.getBoolean( config.getClass(), booleanParam.getKey(), booleanParam.getDefaultValue() ) );
		}

		@Override
		public void visit( final DoubleParam doubleParam )
		{
			doubleParam.set( prefs.getDouble( config.getClass(), doubleParam.getKey(), doubleParam.getDefaultValue() ) );
		}

		@Override
		public void visit( final IntParam intParam )
		{
			intParam.set( prefs.getInt( config.getClass(), intParam.getKey(), intParam.getDefaultValue() ) );
		}

		private void visitStringParam( final Parameter< ?, String > param )
		{
			param.set( prefs.get( config.getClass(), param.getKey(), param.getDefaultValue() ) );
		}

		@Override
		public void visit( final ChoiceParam choiceParam )
		{
			visitStringParam( choiceParam );
		}

		@Override
		public void visit( final StringParam stringParam )
		{
			visitStringParam( stringParam );
		}

		@Override
		public void visit( final PathParam pathParam )
		{
			visitStringParam( pathParam );
		}

		@Override
		public void visit( final SelectableParameters selectable )
		{
			final String defaultSelection = selectable.getParameters().get( 0 ).getKey();
			selectable.select( prefs.get( config.getClass(), selectable.getKey(), defaultSelection ) );
		}
	}
}
