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
package org.scijava.ui.config.visitors.gui.elements.colormap;

import java.awt.Color;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Loat LUTS for {@link Colormap}. Code adapted from what we did in Mastodon.
 *
 * @author Jean-Yves Tinevez 2019
 */
public class ColormapIO
{

	private static final List< URI > LUT_FOLDERS = new ArrayList<>();
	static
	{
		try
		{
			final URI BUILTIN_LUT_FOLDER = ColormapIO.class.getResource( "/luts" ).toURI();
			LUT_FOLDERS.add( BUILTIN_LUT_FOLDER );
		}
		catch ( final URISyntaxException e )
		{
			e.printStackTrace();
		}
	}

	static List< Colormap > getLUTs()
	{
		return loadLUTs();
	}

	private static List< Colormap > loadLUTs()
	{
		final List< Colormap > luts = new ArrayList<>();
		for ( final URI lutFolder : LUT_FOLDERS )
		{
			try
			{
				luts.addAll( loadLUTs( lutFolder ) );
			}
			catch ( final IOException e )
			{
				e.printStackTrace();
			}
		}
		return luts;
	}

	private static List< Colormap > loadLUTs( final URI folder ) throws IOException
	{
		if ( folder.getScheme().equals( "jar" ) )
		{
			// Try to read from within the jar file.
			final String[] array = folder.toString().split( "!" );
			try (FileSystem fileSystem = FileSystems.newFileSystem( URI.create( array[ 0 ] ), Collections.emptyMap() ))
			{
				final Path folderPath = fileSystem.getPath( array[ 1 ] );
				return loadLUTs( folderPath );
			}
		}
		else
		{
			// Read from a standard folder.
			final Path folderPath = Paths.get( folder );
			return loadLUTs( folderPath );
		}
	}

	private static List< Colormap > loadLUTs( final Path folderPath ) throws IOException
	{
		final List< Colormap > luts = new ArrayList<>();
		if ( Files.exists( folderPath ) )
		{
			final String glob = "*.lut";
			try (final DirectoryStream< Path > folderStream = Files.newDirectoryStream( folderPath, glob ))
			{
				for ( final Path path : folderStream )
				{

					final Colormap lut = importLUT( path );
					if ( null == lut )
						System.err.println( "Could not read LUT file: " + path + ". Skipping." );
					else
						luts.add( lut );
				}
			}
		}
		return luts;
	}

	private static final Colormap importLUT( final Path path ) throws IOException
	{
		final String fileName = path.getFileName().toString();
		final String lutName = fileName.substring( 0, fileName.indexOf( '.' ) );

		try (final Scanner scanner = new Scanner( path ))
		{

			final List< Color > colors = new ArrayList<>();
			final List< Integer > intAlphas = new ArrayList<>();
			final AtomicInteger nLines = new AtomicInteger( 0 );

			final Colormap ips = new Colormap( lutName, 0., 1. );
			while ( scanner.hasNext() )
			{
				if ( !scanner.hasNextInt() )
				{
					scanner.next();
					continue;
				}
				intAlphas.add( scanner.nextInt() );
				final Color color = new Color( scanner.nextInt(), scanner.nextInt(), scanner.nextInt() );
				colors.add( color );
				nLines.incrementAndGet();
			}

			if ( nLines.get() < 2 )
				return null;

			final double[] alphas = new double[ intAlphas.size() ];
			for ( int i = 0; i < alphas.length; i++ )
			{
				final double alpha = ( double ) intAlphas.get( i ) / ( nLines.get() - 1 );
				final Color color = colors.get( i );
				ips.add( alpha, color );
			}

			return ips;
		}
	}
}
