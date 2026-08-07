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
