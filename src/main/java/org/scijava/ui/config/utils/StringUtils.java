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
package org.scijava.ui.config.utils;

import java.util.Iterator;

public class StringUtils
{
	public static String join( final Object[] array, String separator )
	{
		if ( array == null )
			return "";
		if ( separator == null )
			separator = "";

		final StringBuilder sb = new StringBuilder();
		for ( int i = 0; i < array.length; i++ )
		{
			if ( i > 0 )
				sb.append( separator );
			sb.append( array[ i ] != null ? array[ i ].toString() : "" );
		}
		return sb.toString();
	}

	public static String join( final Iterable< ? > iterable, final String separator )
	{
		if ( iterable == null )
			return "";
		return join( iterable.iterator(), separator );
	}

	public static String join( final Iterator< ? > iterator, String separator )
	{
		if ( iterator == null )
			return "";
		if ( separator == null )
			separator = "";

		final StringBuilder sb = new StringBuilder();
		while ( iterator.hasNext() )
		{
			if ( sb.length() > 0 )
				sb.append( separator );
			final Object obj = iterator.next();
			sb.append( obj != null ? obj.toString() : "" );
		}
		return sb.toString();
	}
}
