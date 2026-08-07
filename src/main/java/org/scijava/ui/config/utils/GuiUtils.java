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

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GuiUtils
{

	private static final FocusListener selectAllFocusListener = new FocusListener()
	{

		@Override
		public void focusLost( final FocusEvent e )
		{}

		@Override
		public void focusGained( final FocusEvent fe )
		{
			if ( !( fe.getSource() instanceof JTextField ) )
				return;
			final JTextField txt = ( JTextField ) fe.getSource();
			SwingUtilities.invokeLater( () -> txt.selectAll() );
		}
	};

	public static final void selectAllOnFocus( final JTextField tf )
	{
		tf.addFocusListener( selectAllFocusListener );
	}

	public static final void setFont( final JComponent panel, final Font font )
	{
		for ( final Component c : panel.getComponents() )
			c.setFont( font );
	}

	public static boolean isLikelyUrl( final String s )
	{
		if ( s == null )
			return false;
		final String t = s.trim().toLowerCase();
		return t.startsWith( "http://" ) || t.startsWith( "https://" ) || t.startsWith( "file:" );
	}

	public static void openInBrowser( final String url, final Component parent )
	{
		try
		{
			if ( Desktop.isDesktopSupported() )
			{
				Desktop.getDesktop().browse( new java.net.URI( url.trim() ) );
			}
			else
			{
				JOptionPane.showMessageDialog( parent,
						"Desktop browsing not supported.\n" + url,
						"Help", JOptionPane.INFORMATION_MESSAGE );
			}
		}
		catch ( final Exception ex )
		{
			JOptionPane.showMessageDialog( parent,
					"Could not open:\n" + url + "\n" + ex.getMessage(),
					"Help", JOptionPane.ERROR_MESSAGE );
		}
	}

	/**
	 * Positions a JFrame more or less cleverly next a {@link Component}.
	 *
	 * @param gui
	 *            the window to position.
	 * @param component
	 *            the component to position next to.
	 */
	public static void positionWindow( final Window gui, final Component component )
	{

		if ( null != component )
		{
			// Get total size of all screens
			final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			final GraphicsDevice[] gs = ge.getScreenDevices();
			int screenWidth = 0;
			for ( int i = 0; i < gs.length; i++ )
			{
				final DisplayMode dm = gs[ i ].getDisplayMode();
				screenWidth += dm.getWidth();
			}

			final Point windowLoc = component.getLocation();
			final Dimension windowSize = component.getSize();
			final Dimension guiSize = gui.getSize();
			if ( guiSize.width > windowLoc.x )
			{
				if ( guiSize.width > screenWidth - ( windowLoc.x + windowSize.width ) )
				{
					gui.setLocationRelativeTo( null ); // give up
				}
				else
				{
					// put it to the right
					gui.setLocation( windowLoc.x + windowSize.width, windowLoc.y );
				}
			}
			else
			{
				// put it to the left
				gui.setLocation( windowLoc.x - guiSize.width, windowLoc.y );
			}

		}
		else
		{
			gui.setLocationRelativeTo( null );
		}
	}
}
