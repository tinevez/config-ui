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
package org.scijava.ui.config.visitors;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.scijava.ui.config.Configurator;
import org.scijava.ui.config.Configurator.SelectableParameters;
import org.scijava.ui.config.ParameterVisitor;
import org.scijava.ui.config.Parameters.BooleanParam;
import org.scijava.ui.config.Parameters.ChoiceParam;
import org.scijava.ui.config.Parameters.DoubleParam;
import org.scijava.ui.config.Parameters.EnumParam;
import org.scijava.ui.config.Parameters.IntParam;
import org.scijava.ui.config.Parameters.PathParam;
import org.scijava.ui.config.Parameters.StringParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Serializes and deserializes a Configurator instance to and from a JSON file.
 */
public class JSon
{

	/** Prevent instantiation of utility class. */
	private JSon()
	{}

	/**
	 * Deserializes a configurator from a JSON file.
	 * 
	 * @param <C>
	 *            configurator type
	 * @param path
	 *            path to the JSON file
	 * @param configurator
	 *            the configurator to populate with data read from JSon.
	 */
	public static < C extends Configurator > void deserialize( final String path, final C configurator )
	{
		try
		{
			final String str = Files.lines( Paths.get( path ) ).collect( Collectors.joining( System.lineSeparator() ) );
			fromJson( str, configurator );
		}
		catch ( final IOException e )
		{
			System.err.println( "Could not read the " + configurator.getClass().getSimpleName() + " from JSON file at " + path );
			e.printStackTrace();
		}
	}

	private static < C extends Configurator > void fromJson( final String str, final C configurator )
	{
		final Type mapType = new TypeToken< Map< String, Object > >()
		{}.getType();
		final Map< String, Object > jsonMap = getGson().fromJson( str, mapType );
		if ( jsonMap == null )
			System.err.println( "Deserializing " + configurator.getClass().getSimpleName() + " from JSON: "
					+ "Could not read the " + configurator.getClass().getSimpleName() + " from JSON" );

		final Set< String > keySet = jsonMap.keySet();
		if ( keySet.size() != 1 )
			System.err.println( "Deserializing " + configurator.getClass().getSimpleName() + " from JSON: "
					+ "Expected a single key in the JSON map, but found " + keySet.size() );
		final String key = keySet.iterator().next();
		if ( !key.equals( configurator.getClass().getSimpleName() ) )
			System.err.println( "Deserializing " + configurator.getClass().getSimpleName() + " from JSON: "
					+ "Expected the key in the JSON map to be " + configurator.getClass().getSimpleName() + ", but found " + key );

		final Object obj = jsonMap.get( key );
		if ( !( obj instanceof Map ) )
		{
			System.err.println( "Deserializing " + configurator.getClass().getSimpleName() + " from JSON: "
					+ "Expected the value in the JSON map to be a Map, but found " + obj.getClass().getSimpleName() + ". Giving up." );
			return;
		}

		@SuppressWarnings( "unchecked" )
		final Map< String, Object > valueMapMap = ( Map< String, Object > ) obj;
		final DeserializeVisitor visitor = new DeserializeVisitor( valueMapMap );
		configurator.forEach( p -> {
			try
			{
				p.accept( visitor );
			}
			catch ( final IllegalArgumentException e )
			{
				System.err.println( "Deserializing " + configurator.getClass().getSimpleName() + " from JSON: "
						+ "Could not set the value of parameter " + p.getKey()
						+ ": " + e.getMessage() );
			}
		} );
		configurator.getSelectables().forEach( s -> s.accept( visitor ) );
	}

	/**
	 * Serializes a configurator to a JSON file.
	 * 
	 * @param <C>
	 *            configurator type
	 * @param path
	 *            path to the JSON file
	 * @param configurator
	 *            the configurator to serialize
	 */
	public static < C extends Configurator > void serialize( final String path, final C configurator )
	{
		final String str = toJson( configurator );
		if ( !Files.exists( Paths.get( path ) ) )
		{
			try
			{
				Files.createFile( Paths.get( path ) );
			}
			catch ( final IOException e )
			{
				System.err.println( "Could not create the " + configurator.getClass().getSimpleName() + " JSON file at " + path );
				e.printStackTrace();
			}
		}
		try (FileWriter writer = new FileWriter( path ))
		{
			writer.append( str );
		}
		catch ( final IOException e )
		{
			System.err.println( "Could not write the " + configurator.getClass().getSimpleName() + " to " + path );
			e.printStackTrace();
		}
	}

	/**
	 * Converts a configurator to a JSON string.
	 * 
	 * @param <C>
	 *            configurator type
	 * @param configurator
	 *            the configurator to serialize
	 * @return JSON string representation
	 */
	public static < C extends Configurator > String toJson( final C configurator )
	{
		final Map< String, Object > valuesMap = new HashMap<>();
		configurator.forEach( p -> valuesMap.put( p.getKey(), p.getValue() ) );
		configurator.getSelectables().forEach( s -> valuesMap.put( s.getKey(), s.getSelection().getKey() ) );
		final Map< String, Object > jsonMap = Map.of( configurator.getClass().getSimpleName(), valuesMap );
		return getGson().toJson( jsonMap );
	}

	private static Gson getGson()
	{
		final GsonBuilder builder = new GsonBuilder();
		return builder.setPrettyPrinting().create();
	}

	private static class DeserializeVisitor implements ParameterVisitor
	{
		private final Map< String, Object > valuesMap;

		public DeserializeVisitor( final Map< String, Object > valuesMap )
		{
			this.valuesMap = valuesMap;
		}

		@Override
		public void visit( final BooleanParam param )
		{
			if ( valuesMap.containsKey( param.getKey() ) )
				param.set( ( Boolean ) valuesMap.get( param.getKey() ) );
		}

		@Override
		public void visit( final ChoiceParam choiceParam )
		{
			if ( valuesMap.containsKey( choiceParam.getKey() ) )
				choiceParam.set( ( String ) valuesMap.get( choiceParam.getKey() ) );
		}

		@Override
		public void visit( final DoubleParam doubleParam )
		{
			if ( valuesMap.containsKey( doubleParam.getKey() ) )
				doubleParam.set( ( Double ) valuesMap.get( doubleParam.getKey() ) );
		}

		@Override
		public < E extends Enum< E > > void visit( final EnumParam< E > enumParam )
		{
			if ( valuesMap.containsKey( enumParam.getKey() ) )
			{
				final String value = ( String ) valuesMap.get( enumParam.getKey() );
				final E enumValue = Enum.valueOf( enumParam.getEnumClass(), value );
				enumParam.set( enumValue );
			}
		}

		@Override
		public void visit( final IntParam intParam )
		{
			if ( valuesMap.containsKey( intParam.getKey() ) )
				intParam.set( ( ( Number ) valuesMap.get( intParam.getKey() ) ).intValue() );
		}

		@Override
		public void visit( final PathParam pathParam )
		{
			if ( valuesMap.containsKey( pathParam.getKey() ) )
				pathParam.set( ( String ) valuesMap.get( pathParam.getKey() ) );
		}

		@Override
		public void visit( final SelectableParameters selectable )
		{
			if ( valuesMap.containsKey( selectable.getKey() ) )
				selectable.select( ( String ) valuesMap.get( selectable.getKey() ) );
		}

		@Override
		public void visit( final StringParam stringParam )
		{
			if ( valuesMap.containsKey( stringParam.getKey() ) )
				stringParam.set( ( String ) valuesMap.get( stringParam.getKey() ) );
		}
	}
}
