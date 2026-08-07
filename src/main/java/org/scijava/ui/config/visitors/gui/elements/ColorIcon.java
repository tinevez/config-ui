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
package org.scijava.ui.config.visitors.gui.elements;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;

/**
 * A simple icon that draws a colored rounded square.
 * <p>
 * Adapted from http://stackoverflow.com/a/3072979/230513
 */
public class ColorIcon implements Icon
{
	private static final int DEFAULT_PAD = 0;

	private static final int DEFAUL_SIZE = 16;

	private final int size;

	private Color color;

	private final int pad;

	/**
	 * Creates a color icon with the specified color, size, and padding.
	 *
	 * @param color
	 *            the color of the icon.
	 * @param size
	 *            the size of the icon in pixels.
	 * @param pad
	 *            the padding around the icon.
	 */
	public ColorIcon( final Color color, final int size, final int pad )
	{
		this.color = color;
		this.size = size;
		this.pad = pad;
	}

	/**
	 * Creates a color icon with the specified color and size.
	 *
	 * @param color
	 *            the color of the icon.
	 * @param size
	 *            the size of the icon in pixels.
	 */
	public ColorIcon( final Color color, final int size )
	{
		this( color, size, DEFAULT_PAD );
	}

	/**
	 * Creates a color icon with the specified color and default size.
	 *
	 * @param color
	 *            the color of the icon.
	 */
	public ColorIcon( final Color color )
	{
		this( color, DEFAUL_SIZE );
	}

	@Override
	public void paintIcon( final Component c, final Graphics g, final int x, final int y )
	{
		final Graphics2D g2d = ( Graphics2D ) g;
		g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		g2d.setColor( color );
		final RoundRectangle2D.Float shape = new RoundRectangle2D.Float( x + pad, y + pad, size, size, 5, 5 );
		g2d.fill( shape );
		g2d.setColor( Color.BLACK );
		g2d.draw( shape );
	}

	/**
	 * Sets the color of this icon.
	 *
	 * @param color
	 *            the new color.
	 */
	public void setColor( final Color color )
	{
		this.color = color;
	}

	@Override
	public int getIconWidth()
	{
		return size + 2 * pad;
	}

	@Override
	public int getIconHeight()
	{
		return size + 2 * pad;
	}
}
