package org.scijava.ui.config.visitors;

import java.util.LinkedHashMap;
import java.util.Map;

import org.scijava.ui.config.Configurator;
import org.scijava.ui.config.Configurator.SelectableParameters;
import org.scijava.ui.config.Parameters.Parameter;

public class Maps
{

	/**
	 * Convert a Configurator to a Map of key to value.
	 * 
	 * @param config
	 *            the Configurator to convert.
	 * @return a new Map containing the keys and values of the Configurator's
	 *         parameters.
	 */
	public static Map< String, Object > toMap( final Configurator config )
	{
		final Map< String, Object > map = new LinkedHashMap<>();
		config.forEach( p -> map.put( p.getKey(), p.getValue() ) );
		config.getSelectables().forEach( s -> map.put( s.getKey(), s.getSelection().getKey() ) );
		return map;
	}

	/**
	 * Fills the specified Configurator with the values from the specified Map,
	 * by matching the keys of the Map to the keys of the Configurator's
	 * parameters.
	 * 
	 * @param map
	 *            the Map containing the keys and values to set in the
	 *            Configurator.
	 * @param config
	 *            the Configurator to fill with the values from the Map.
	 */
	public static final void fromMap( final Map< String, Object > map, final Configurator config )
	{
		config.getParameters().forEach( arg -> fromMap( map, arg ) );
		config.getSelectables().forEach( selectable -> fromMap( map, selectable ) );
	}

	private static < O > void fromMap( final Map< String, Object > map, final Parameter< ?, O > param )
	{
		final String key = param.getKey();
		final Object val = map.get( key );
		if ( val != null )
		{
			@SuppressWarnings( "unchecked" )
			final O castVal = ( O ) val;
			param.set( castVal );
		}
	}

	private static void fromMap( final Map< String, ? > map, final SelectableParameters selectable )
	{
		final Object val = map.get( selectable.getKey() );
		if ( val != null )
			selectable.select( ( String ) val );
	}
}
