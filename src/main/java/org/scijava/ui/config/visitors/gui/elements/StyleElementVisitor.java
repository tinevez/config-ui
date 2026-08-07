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

import org.scijava.ui.config.visitors.gui.elements.StyleElements.BooleanElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.BoundedDoubleElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.ColorElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.ColormapElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.DoubleElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.EnumElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.FontElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.IntElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.LabelElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.ListElement;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.Separator;
import org.scijava.ui.config.visitors.gui.elements.StyleElements.StringElement;

/**
 * A visitor for {@link StyleElement}s. It has a default implementation for each
 * type of element that throws an {@link UnsupportedOperationException}. This
 * allows to implement only the methods that are needed.
 * <p>
 * Based on a design by Tobias Pietzsch in Mastodon.
 */
public interface StyleElementVisitor
{
	default void visit( final Separator element )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final LabelElement label )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final ColorElement colorElement )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final BooleanElement booleanElement )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final BoundedDoubleElement doubleElement )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final DoubleElement doubleElement )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final IntElement intElement )
	{
		throw new UnsupportedOperationException();
	}

	default < E > void visit( final EnumElement< E > enumElement )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final ColormapElement element )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final StringElement stringElement )
	{
		throw new UnsupportedOperationException();
	}

	default < E > void visit( final ListElement< E > listElement )
	{
		throw new UnsupportedOperationException();
	}

	default void visit( final FontElement element )
	{
		throw new UnsupportedOperationException();
	}
}
