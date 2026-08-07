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
import java.awt.Container;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Purpose: To recursively disable (and later re-enable) all components of a
 * container, e.g. if you want to clearly show that a program is busy or if you
 * want to prevent clicks and other inputs that piled up meanwhile to affect a
 * window once the program becomes responsive again. Though the solution for
 * that would be simpler: Just disable the window and then, in a
 * SwingUtilities.invokeLater(), re-enable it. This makes sure that before this
 * happens, all input events are eaten.
 * <p>
 * JYT: Changed it so that it can remember what was the enabled state and
 * restore this state. Taken from TrackMate.
 */
final public class EverythingDisablerAndReenabler
{ // v[1, 2016-12-05 14!30 UTC] by dreamspace-president.com

	final private Container rootContainerForWhatShouldBeDisabled;

	final private Class< ? >[] componentClassesToBeIgnored;

	final private List< WeakReference< Component > > componentsToReenable = new ArrayList<>();

	private boolean disableHasBeenCalled = false;
	// Order is strictly upheld via IllegalStateException!

	/**
	 * Creates a new instance of this class, which can be used to disable and
	 * re-enable all components of a given container, except for the specified
	 * classes.
	 * 
	 * @param rootContainerForWhatShouldBeDisabled
	 *            NOT NULL! The Container whose components are to be recursively
	 *            disabled. The container itself will not be disabled.
	 * @param componentClassesToBeIgnored
	 *            null or an array of classes (e.g. containing JLabel.class)
	 *            that should be excluded from disabling. Adding a Container
	 *            here does not affect the recursive process.
	 * @throws IllegalArgumentException
	 *             if the container argument is null. In case someone wonders
	 *             why I don't use {@link NullPointerException} here: Null can
	 *             be a perfectly legal argument in other places, but here, it
	 *             is not. If an argument does not check out, the choice of
	 *             Exception, of course, is IllegalArgument, not NullPointer.
	 */
	public EverythingDisablerAndReenabler( final Container rootContainerForWhatShouldBeDisabled, final Class< ? >[] componentClassesToBeIgnored )
	{

		if ( rootContainerForWhatShouldBeDisabled == null )
			throw new IllegalArgumentException();

		this.rootContainerForWhatShouldBeDisabled = rootContainerForWhatShouldBeDisabled;
		this.componentClassesToBeIgnored = componentClassesToBeIgnored;
	}

	/**
	 * Convenience method that calls {@link #reenable()} or {@link #disable()}
	 * depending on the specified boolean flag.
	 *
	 * @param enable
	 *            whether to re-enable (<code>true</code>) or disable
	 *            (<code>false</code>) the root container and descendants.
	 */
	public void setEnabled( final boolean enable )
	{
		if ( enable && disableHasBeenCalled )
			reenable();
		else if ( !enable && !disableHasBeenCalled )
			disable();
	}

	/**
	 * Disables everything recursively, except the excluded types.
	 *
	 * @throws IllegalStateException
	 *             if called twice in a row.
	 */
	public void disable()
	{
		if ( disableHasBeenCalled )
			throw new IllegalStateException();

		disableHasBeenCalled = true;
		componentsToReenable.clear();
		disableEverythingInsideThisHierarchically( rootContainerForWhatShouldBeDisabled );
	}

	/**
	 * Re-enables everything that was disabled by the last call to
	 * {@link #disable()}.
	 * 
	 * @throws IllegalStateException
	 *             if called twice in a row or if disable() had not been called
	 *             yet.
	 */
	public void reenable()
	{

		if ( !disableHasBeenCalled )
			throw new IllegalStateException();

		disableHasBeenCalled = false;

		for ( int i = componentsToReenable.size() - 1; i >= 0; i-- )
			Optional.ofNullable( componentsToReenable.get( i ).get() ).ifPresent( c -> c.setEnabled( true ) );

		componentsToReenable.clear();
	}

	/**
	 * Returns whether {@link #disable()} has been called and not yet followed
	 * by a call to {@link #reenable()}.
	 *
	 * @return {@code true} if the components are currently disabled,
	 *         {@code false} otherwise.
	 */
	public boolean disableHasBeenCalled()
	{
		return disableHasBeenCalled;
	}

	private void disableEverythingInsideThisHierarchically( final Container container )
	{

		final Component[] components = container.getComponents();
		for ( final Component component : components )
		{

			if ( component != null )
			{

				// RECURSION FIRST
				if ( component instanceof Container )
				{
					disableEverythingInsideThisHierarchically( ( Container ) component );
				}

				// AND THEN DEAL WITH THE ELEMENTS
				if ( component.isEnabled() )
				{
					boolean found = false;
					if ( componentClassesToBeIgnored != null )
					{
						for ( final Class< ? > cls : componentClassesToBeIgnored )
						{
							if ( component.getClass() == cls )
							{
								found = true;
								break;
							}
						}
					}
					if ( !found && component.isEnabled() )
					{
						component.setEnabled( false );
						componentsToReenable.add( new WeakReference<>( component ) );
					}
				}
			}
		}
	}
}
