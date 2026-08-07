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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A {@link JSlider} with a {@link JSpinner} next to it, both modifying the same
 * {@link BoundedValue value}.
 */
public class SliderPanel extends JPanel implements BoundedValue.UpdateListener
{
	private static final long serialVersionUID = 6444334522127424416L;

	/** Preferred size for slider panels. */
	public static final Dimension PANEL_SIZE = new Dimension( 150, 20 );

	private final JSlider slider;

	private final JSpinner spinner;

	private final BoundedValue model;

	/**
	 * Create a {@link SliderPanel} to modify a given {@link BoundedValue
	 * value}.
	 *
	 * @param name
	 *            label to show next to the slider.
	 * @param model
	 *            the value that is modified.
	 * @param spinnerStepSize
	 *            the step size for the spinner.
	 */
	public SliderPanel( final String name, final BoundedValue model, final int spinnerStepSize )
	{
		super();
		setLayout( new BorderLayout( 10, 10 ) );
		setPreferredSize( PANEL_SIZE );

		slider = new JSlider( SwingConstants.HORIZONTAL, model.getRangeMin(), model.getRangeMax(), model.getCurrentValue() );
		spinner = new JSpinner();

		final int min = model.getRangeMin();
		final int max = model.getRangeMax();
		final int val = Math.max( Math.min( model.getCurrentValue(), max ), min );
		spinner.setModel( new SpinnerNumberModel( val, min, max, spinnerStepSize ) );

		slider.addChangeListener( new ChangeListener()
		{
			@Override
			public void stateChanged( final ChangeEvent e )
			{
				final int value = slider.getValue();
				model.setCurrentValue( value );
			}
		} );

		final MouseWheelListener mwl = new MouseWheelListener()
		{

			@Override
			public void mouseWheelMoved( final MouseWheelEvent e )
			{
				final int notches = e.getWheelRotation();
				int value = slider.getValue();
				value -= notches * spinnerStepSize;
				if ( value < slider.getMinimum() )
					value = slider.getMinimum();
				else if ( value > slider.getMaximum() )
					value = slider.getMaximum();
				slider.setValue( value );
			}
		};
		slider.addMouseWheelListener( mwl );
		spinner.addMouseWheelListener( mwl );

		spinner.addChangeListener( new ChangeListener()
		{
			@Override
			public void stateChanged( final ChangeEvent e )
			{
				final int value = ( ( Integer ) spinner.getValue() ).intValue();
				model.setCurrentValue( value );
			}
		} );

		if ( name != null )
		{
			final JLabel label = new JLabel( name, SwingConstants.CENTER );
			label.setAlignmentX( CENTER_ALIGNMENT );
			add( label, BorderLayout.WEST );
		}

		add( slider, BorderLayout.CENTER );
		add( spinner, BorderLayout.EAST );

		this.model = model;
		model.setUpdateListener( this );
	}

	/**
	 * Sets the number of columns for the spinner text field.
	 *
	 * @param cols
	 *            the number of columns.
	 */
	public void setNumColummns( final int cols )
	{
		( ( JSpinner.NumberEditor ) spinner.getEditor() ).getTextField().setColumns( cols );
	}

	@Override
	public void setFont( final Font font )
	{
		super.setFont( font );
		if ( spinner != null )
			spinner.setFont( font );
		if ( slider != null )
			slider.setFont( font );
	}

	@Override
	public void setToolTipText( final String text )
	{
		super.setToolTipText( text );
		if ( spinner != null )
			spinner.setToolTipText( text );
		if ( slider != null )
			slider.setToolTipText( text );
	}

	@Override
	public void update()
	{
		final int value = model.getCurrentValue();
		final int min = model.getRangeMin();
		final int max = model.getRangeMax();
		if ( slider.getMaximum() != max || slider.getMinimum() != min )
		{
			slider.setMinimum( min );
			slider.setMaximum( max );
			final SpinnerNumberModel spinnerModel = ( SpinnerNumberModel ) spinner.getModel();
			spinnerModel.setMinimum( min );
			spinnerModel.setMaximum( max );
		}
		slider.setValue( value );
		spinner.setValue( value );
	}
}
