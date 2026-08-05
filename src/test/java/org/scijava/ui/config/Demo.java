package org.scijava.ui.config;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.scijava.Cancelable;
import org.scijava.command.Previewable;
import org.scijava.ui.config.visitors.Maps;
import org.scijava.ui.config.visitors.Strings;
import org.scijava.ui.config.visitors.gui.FrameBuilder;
import org.scijava.ui.config.visitors.gui.FrameBuilder.ConfigFrame;
import org.scijava.ui.config.visitors.gui.FrameBuilder.ConfigFrame.Progress;
import org.scijava.ui.config.visitors.gui.FrameBuilder.UserTask;

/**
 * Demo with a UI that would configure Cellpose 3.
 */
public class Demo
{

	public static void main( final String[] args )
	{
		final int nChannels = 3;
		final double pixelSize = 0.2;
		final String units = "µm";

		final Cellpose3Config config = new Cellpose3Config( nChannels, pixelSize, units );

		config.builtinModel.set( Cellpose3BuiltinModels.CYTO3 );
		config.builtinOrCustom.select( config.builtinModel );
		config.chan1.set( 2 );
		config.chan2.set( 1 );
		config.diameter.set( 40. );

		System.out.println( "------------------------------" );
		System.out.println( "Original config" );
		System.out.println( "------------------------------" );
		System.out.println( config );
		System.out.println( "------------------------------" );

		System.out.println();
		System.out.println( "------------------------------" );
		System.out.println( "As a map:" );
		System.out.println( "------------------------------" );
		final Map< String, Object > map = Maps.toMap( config );
		map.forEach( ( k, v ) -> System.out.println( " - " + k + " -> " + v ) );
		System.out.println( "------------------------------" );

		// Modify the map.
		map.put( "CUSTOM_MODEL_PATH", "Trololo" );
		map.put( "BUILTIN_OR_CUSTOM", "CUSTOM_MODEL_PATH" );
		map.put( "CHAN2", 0 );

		// Re-read the map into a new config.
		final Cellpose3Config config2 = new Cellpose3Config( nChannels, pixelSize, units );
		Maps.fromMap( map, config2 );
		System.out.println();
		System.out.println( "------------------------------" );
		System.out.println( "After modifying the map" );
		System.out.println( "------------------------------" );
		System.out.println( Strings.echo( config2 ) );
		System.out.println( "------------------------------" );

		/*
		 * GUI
		 */

		final DummyRunner dummyRunner = new DummyRunner( config2 );
		final Configurator defaultValues = new Cellpose3Config( nChannels, pixelSize, units );

		final ConfigFrame frame = FrameBuilder.build( config2, dummyRunner, defaultValues );

		frame.setVisible( true );
	}

	private static class DummyRunner implements UserTask, Cancelable, Previewable
	{

		private final Cellpose3Config config;

		private final AtomicBoolean cancelRequested = new AtomicBoolean( false );

		private String cancelReason;

		public DummyRunner( final Cellpose3Config config )
		{
			this.config = config;
		}

		@Override
		public void run( final Progress p ) throws Exception
		{
			cancelRequested.set( false );
			p.indeterminate( false, "Preparing..." );
			Thread.sleep( 500 );
			final int steps = 20;
			for ( int i = 1; i <= steps; i++ )
			{
				if ( isCanceled() )
				{
					p.message( "Canceled:" + getCancelReason() );
					return;
				}
				Thread.sleep( 100 );
				p.set( i / ( double ) steps, "Running " + config.builtinModel.getValue() );
			}
			p.message( "Model run finished." );
		}

		@Override
		public boolean isCanceled()
		{
			return cancelRequested.get();
		}

		@Override
		public void cancel( final String reason )
		{
			this.cancelReason = reason;
			cancelRequested.set( true );
		}

		@Override
		public String getCancelReason()
		{
			return cancelReason;
		}

		@Override
		public void preview()
		{
			cancelRequested.set( false );
			System.out.println( "Previewing with config: " + config );
			try
			{
				Thread.sleep( 2500 );
			}
			catch ( final InterruptedException e )
			{
				e.printStackTrace();
			}
			if ( cancelRequested.get() )
			{
				System.out.println( "Preview was canceled." );
				return;
			}
			System.out.println( "Preview done." );
		}

		@Override
		public void cancel()
		{
			cancelRequested.set( true );
			System.out.println( "Preview canceled." );
		}
	}
}
