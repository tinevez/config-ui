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

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.scijava.ui.config.Parameters.Parameter;

/**
 * An iterator that tracks parameter groups and provides consistent group
 * transition information.
 */
public class ConfiguratorIterator implements Iterator< Parameter< ?, ? > >
{
	private final Iterator< Object > mainIterator;

	// Iteration state for the group we are currently reading "next" from

	// group for upcoming parameters (if any)
	private ParameterGroup currentGroup;

	// iterator over that group'sparameters
	private Iterator< Parameter< ?, ? > > currentGroupIterator;

	// Prepared next parameter and its group-position flags
	private Parameter< ?, ? > nextParameter;

	private boolean nextIsFirstInGroup = false;

	private boolean nextIsLastInGroup = false;

	// Info about the last parameter returned by next()
	private ParameterGroup lastGroup = null;

	private boolean lastEntered = false;

	private boolean lastExited = false;

	/**
	 * Creates a new iterator for the specified configurator.
	 * <p>
	 * The iterator yields all parameters in display order: standalone parameters
	 * and all parameters within groups (empty groups are skipped). Note that
	 * {@link SelectableParameters} groups themselves are not yielded as elements;
	 * only their member parameters are iterated. The iterator does not filter
	 * based on selection state or parameter visibility.
	 *
	 * @param configurator
	 *            the configurator to iterate over.
	 */
	public ConfiguratorIterator( final Configurator configurator )
	{
		this.mainIterator = configurator.orderedElements.iterator();
	}

	@Override
	public boolean hasNext()
	{
		if ( nextParameter != null )
			return true;

		// If we just exhausted a group, clear that state before searching
		if ( currentGroupIterator != null && !currentGroupIterator.hasNext() )
		{
			currentGroupIterator = null;
			currentGroup = null;
		}

		// If we're inside a group with remaining parameters, yield from it
		if ( currentGroupIterator != null && currentGroupIterator.hasNext() )
		{
			nextParameter = currentGroupIterator.next();
			nextIsFirstInGroup = false;
			nextIsLastInGroup = !currentGroupIterator.hasNext();
			return true;
		}

		// Otherwise iterate through ordered elements
		while ( mainIterator.hasNext() )
		{
			final Object element = mainIterator.next();

			if ( element instanceof ParameterGroup )
			{
				// Start iterating this group (skip if empty)
				final ParameterGroup group = ( ParameterGroup ) element;
				final Iterator< Parameter< ?, ? > > it = group.parameters.iterator();
				if ( !it.hasNext() )
					continue; // empty group: ignore

				currentGroup = group;
				currentGroupIterator = it;

				nextParameter = currentGroupIterator.next();
				nextIsFirstInGroup = true;
				nextIsLastInGroup = !currentGroupIterator.hasNext();
				return true;
			}
			else if ( element instanceof Parameter )
			{
				// Standalone parameter
				nextParameter = ( Parameter< ?, ? > ) element;
				nextIsFirstInGroup = false;
				nextIsLastInGroup = false;
				return true;
			}
		}

		return false;
	}

	@Override
	public Parameter< ?, ? > next()
	{
		if ( !hasNext() )
			throw new NoSuchElementException( "No more parameters available" );

		final Parameter< ?, ? > result = nextParameter;

		// Record "last returned" state for caller queries
		lastGroup = currentGroup; // null if standalone parameter
		lastEntered = ( currentGroup != null ) && nextIsFirstInGroup;
		lastExited = ( currentGroup != null ) && nextIsLastInGroup;

		// Consume prepared next
		nextParameter = null;

		return result;
	}

	/**
	 * Returns {@code true} if the last returned parameter was the first in its
	 * group.
	 *
	 * @return {@code true} if entering a group, {@code false} otherwise.
	 */
	public boolean groupEntered()
	{
		return lastEntered;
	}

	/**
	 * Returns {@code true} if the last returned parameter was the last in its
	 * group.
	 *
	 * @return {@code true} if exiting a group, {@code false} otherwise.
	 */
	public boolean groupExited()
	{
		return lastExited;
	}

	/**
	 * Returns {@code true} if the last returned parameter belonged to a group.
	 *
	 * @return {@code true} if inside a group, {@code false} if the last
	 *         parameter was standalone.
	 */
	public boolean inGroup()
	{
		return lastGroup != null;
	}

	/**
	 * Returns the group containing the last returned parameter, or {@code null}
	 * if the last parameter was standalone.
	 *
	 * @return the current parameter group, or {@code null}.
	 */
	public ParameterGroup getCurrentGroup()
	{
		return lastGroup;
	}
}
