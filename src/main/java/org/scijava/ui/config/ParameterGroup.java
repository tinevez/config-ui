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
package org.scijava.ui.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.scijava.ui.config.Parameters.Parameter;

/**
 * Holds an ordered list of parameters, and possibly sub-groups of parameters.
 * <p>
 * Has fields to store a name, help text and sets whether the group is folded or
 * not when displayed in a UI.
 */
public class ParameterGroup implements Iterable< Parameter< ?, ? > >
{

	private String name = toString();

	private boolean collapsed = true;

	protected List< Parameter< ?, ? > > parameters = new ArrayList<>();

	/**
	 * Sets the name of this parameter group.
	 *
	 * @param name
	 *            the group name.
	 * @return this group.
	 */
	ParameterGroup name( final String name )
	{
		this.name = name;
		return this;
	}

	/**
	 * Returns the name of this parameter group.
	 *
	 * @return the group name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set whether the group is folded (collapsed, default) or unfolded
	 * (expanded) when displayed in a UI.
	 *
	 * @param collapsed
	 *            whether the group is collapsed.
	 * @return this parameter group.
	 */
	ParameterGroup collapsed( final boolean collapsed )
	{
		this.collapsed = collapsed;
		return this;
	}

	/**
	 * Returns whether this group is collapsed in the UI.
	 *
	 * @return {@code true} if collapsed, {@code false} if expanded.
	 */
	public boolean isCollapsed()
	{
		return collapsed;
	}

	/**
	 * Adds a parameter to this group.
	 *
	 * @param param
	 *            the parameter to add.
	 */
	void add( final Parameter< ?, ? > param )
	{
		this.parameters.add( param );
	}

	/**
	 * Returns an iterator over the parameters in this group.
	 *
	 * @return an iterator over the parameters.
	 */
	@Override
	public Iterator< Parameter< ?, ? > > iterator()
	{
		return parameters.iterator();
	}

	@Override
	public String toString()
	{
		return getName()
				+ " (" + this.getClass().getSimpleName() + ")\n"
				+ " - collapsed: " + isCollapsed() + "\n";
	}
}
