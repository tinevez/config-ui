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

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class Icons
{

	public static int DEFAULT_ICON_SIZE = 16;

	public static final ImageIcon RELOAD = get( "cached" );

	public static final ImageIcon COMMENT = get( "comment" );

	public static final ImageIcon HELP = get( "help_outline" );

	public static final ImageIcon PLAY = get( "play_circle_filled_white" );

	public static final ImageIcon STOP = get( "stop" );

	public static final ImageIcon RESET = get( "settings_backup_restore" );

	public static final ImageIcon STORE = get( "turned_in_not" );

	public static final ImageIcon PREVIEW = get( "preview" );

	public static final ImageIcon STOP_PREVIEW = get( "preview_off" );

	public static final ImageIcon FONT_SELECT = get( "type_specimen_64dp_1F1F1F_FILL0_wght400_GRAD0_opsz48" );

	private static ImageIcon get( final String name )
	{
		return resize( load( "/icons/" + name + ".png" ), DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE );
	}

	private static ImageIcon resize( final ImageIcon icon, final int width, final int height )
	{
		final Image img = icon.getImage();
		final Image resizedImage = img.getScaledInstance( width, height, Image.SCALE_SMOOTH );
		return new ImageIcon( resizedImage );
	}

	private static ImageIcon load( final String path )
	{
		try
		{
			return new ImageIcon( Icons.class.getResource( path ) );
		}
		catch ( final Exception e )
		{
			System.err.println( "Could not load icon: " + path );
			// Return default "missing image" icon.
			return asImageIcon( UIManager.getIcon( "OptionPane.errorIcon" ) );
		}
	}

	private static ImageIcon asImageIcon( final Icon icon )
	{
		if ( icon instanceof ImageIcon )
			return ( ImageIcon ) icon;
		if ( icon == null )
			return new ImageIcon(
					new java.awt.image.BufferedImage( 16, 16, java.awt.image.BufferedImage.TYPE_INT_ARGB ) );
		final int w = Math.max( 1, icon.getIconWidth() );
		final int h = Math.max( 1, icon.getIconHeight() );
		final java.awt.image.BufferedImage img = new java.awt.image.BufferedImage( w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB );
		final java.awt.Graphics2D g = img.createGraphics();
		try
		{
			icon.paintIcon( null, g, 0, 0 );
		}
		finally
		{
			g.dispose();
		}
		return new ImageIcon( img );
	}
}
