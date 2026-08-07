package org.scijava.ui.config;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

import org.scijava.Cancelable;
import org.scijava.command.Previewable;
import org.scijava.ui.config.Parameters.BooleanParam;
import org.scijava.ui.config.Parameters.ChoiceParam;
import org.scijava.ui.config.Parameters.DoubleParam;
import org.scijava.ui.config.Parameters.EnumParam;
import org.scijava.ui.config.Parameters.IntParam;
import org.scijava.ui.config.Parameters.PathParam;
import org.scijava.ui.config.visitors.Strings;
import org.scijava.ui.config.visitors.gui.FrameBuilder;
import org.scijava.ui.config.visitors.gui.FrameBuilder.ConfigFrame.Progress;
import org.scijava.ui.config.visitors.gui.FrameBuilder.UserTask;
import org.scijava.ui.config.visitors.gui.GuiBuilder;
import org.scijava.ui.config.visitors.gui.GuiBuilder.ConfigPanel;

/**
 * Demo used in the README.
 */
public class DemoDoc
{

	public static class MyAlgorithmConfig extends Configurator
	{

		public final DoubleParam threshold;

		public final IntParam maxIterations;

		public final BooleanParam useAdvancedMode;

		public final ChoiceParam method;

		public MyAlgorithmConfig()
		{
			this( "My Algorithm", "Configure the parameters for my algorithm." );
		}

		protected MyAlgorithmConfig( final String name, final String help )
		{
			super( name, help );

			// Bounded double parameter with slider
			this.threshold = addDoubleParameter()
					.key( "THRESHOLD" )
					.name( "Threshold" )
					.help( "The detection threshold. Higher values are more strict." )
					.defaultValue( 0.5 )
					.min( 0.0 )
					.max( 1.0 )
					.get();

			// Integer parameter with bounds
			this.maxIterations = addIntParameter()
					.key( "MAX_ITER" )
					.name( "Max iterations" )
					.help( "Maximum number of iterations." )
					.defaultValue( 100 )
					.min( 1 )
					.max( 1000 )
					.units( "iterations" )
					.get();

			// Boolean flag
			this.useAdvancedMode = addBooleanParameter()
					.key( "ADVANCED" )
					.name( "Advanced mode" )
					.help( "Enable advanced processing options." )
					.defaultValue( false )
					.get();

			// Choice from discrete values
			this.method = addChoiceParameter()
					.key( "METHOD" )
					.name( "Processing method" )
					.help( "Select the algorithm to use." )
					.addChoice( "FAST", "Fast but less accurate" )
					// Second parameter is used for display in the GUI.
					.addChoice( "ACCURATE", "Slower but more accurate" )
					.addChoice( "BALANCED", "A balance between speed and accuracy" )
					.defaultValue( "BALANCED" )
					.get();
		}
	}

	public static void main( final String[] args )
	{
		/*
		 * Create the configuration
		 */
		final MyAlgorithmConfig config = new MyAlgorithmConfig();

		/*
		 * Pretty print it.
		 */
		System.out.println( Strings.echo( config ) );

		/*
		 * Show a GUI in a JPanel.
		 */
		final ConfigPanel panel = GuiBuilder.build( config );
		final JFrame frame = new JFrame( "Algorithm Configuration" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().add( panel );
		frame.pack();
		frame.setLocationRelativeTo( null );
		frame.setVisible( true );

		/*
		 * Use the FrameBuilder to bind an action to the config.
		 */

		// The task to run.`
		class MyTask implements UserTask
		{

			/* We will use it later in the cancelable example. */
			protected final AtomicBoolean cancelRequested = new AtomicBoolean( false );

			@Override
			public void run( final Progress progress ) throws Exception
			{
				cancelRequested.set( false );
				progress.indeterminate( false, "Processing..." );
				for ( int i = 0; i <= config.maxIterations.getValue(); i++ )
				{
					if ( cancelRequested.get() )
					{
						System.out.println( "I have been canceled!" );
						return;
					}
					// Do work...
					progress.set( ( double ) i / config.maxIterations.getValue(), "Step " + i );
					Thread.sleep( 50 );
				}
				progress.message( "Done!" );
			}
		};
		// Default values for the config, used to reset the form.
		final MyAlgorithmConfig defaultValues = new MyAlgorithmConfig();
		FrameBuilder.build( config, new MyTask(), defaultValues ).setVisible( true );

		/*
		 * Integrating Cancelable and Previewable
		 */

		class MyCancelableAndPreviewableTask extends MyTask implements Cancelable, Previewable
		{

			@Override
			public void preview()
			{
				// Implement preview logic here, e.g., update a preview display
				// based on current config values.
				System.out.println( "Previewing with threshold: " + config.threshold.getValue() +
						", max iterations: " + config.maxIterations.getValue() +
						", advanced mode: " + config.useAdvancedMode.getValue() +
						", method: " + config.method.getValue() );
			}

			@Override
			public void cancel( final String reason )
			{
				cancelRequested.set( true );
			}

			@Override
			public boolean isCanceled()
			{
				return cancelRequested.get();
			}

			@Override
			public void cancel()
			{
				System.out.println( "Canceling preview" );
			}

			@Override
			public String getCancelReason()
			{
				return "User requested cancellation.";
			}
		}
		// Build a new UI with the cancelable and previewable task.
		FrameBuilder.build( config, new MyCancelableAndPreviewableTask(), defaultValues ).setVisible( true );

		/*
		 * Parameter Groups and Collapsible Sections
		 */

		class MyAdvancedConfig extends MyAlgorithmConfig
		{
			public final BooleanParam enableLogging;

			public final IntParam logLevel;

			public MyAdvancedConfig()
			{
				this( "My advanced algorithm", "Configure the parameters for my advanced algorithm." );
			}

			protected MyAdvancedConfig( final String name, final String help )
			{
				super( name, help );
				this.enableLogging = addBooleanParameter()
						.key( "ENABLE_LOGGING" )
						.name( "Enable Logging" )
						.help( "Enable detailed logging." )
						.defaultValue( false )
						.get();

				this.logLevel = addIntParameter()
						.key( "LOG_LEVEL" )
						.name( "Log Level" )
						.help( "Set the logging level (0-5)." )
						.defaultValue( 3 )
						.min( 0 )
						.max( 5 )
						.get();

				// This will create a collapsible group in the GUI where these
				// two parameters will be placed.
				addGroup( "Advanced Options" )
						.add( enableLogging )
						.add( logLevel )
						.collapsed( false )
						.get();
			}
		}
		FrameBuilder.build( new MyAdvancedConfig(), new MyTask(), new MyAdvancedConfig() ).setVisible( true );

		/*
		 * Mutually Exclusive Parameters
		 */	
		
		class MyExclusiveConfig extends MyAdvancedConfig
		{
			
			enum ModelType
			{
				MODEL_A,
				MODEL_B,
				MODEL_C
			}

			private final PathParam customModelPath;

			private final EnumParam< ModelType > builtinModel;

			public MyExclusiveConfig()
			{
				super( "My exclusive algorithm", "Configure the parameters for my exclusive algorithm." );

				// Define parameters first
				this.builtinModel = addEnumParameter( ModelType.class )
						.key( "BUILTIN_MODEL" )
						.name( "Built-in model" )
						.get();

				this.customModelPath = addPathParameter()
						.key( "CUSTOM_PATH" )
						.name( "Custom model path" )
						.get();

				addGroup( "Model Selection" )
						.add( builtinModel )
						.add( customModelPath )
						.collapsed( false )
						.get();

				// Then group them as mutually exclusive
				addSelectableParameters()
						.key( "MODEL_SOURCE" )
						.add( builtinModel )
						.add( customModelPath )
						.get();
			}
		}
		final MyExclusiveConfig exclusiveConfig = new MyExclusiveConfig();
		FrameBuilder.build( exclusiveConfig, new MyTask(), new MyExclusiveConfig() ).setVisible( true );
		
		/*
		 * Display translators
		 */
		
		class MyTranslatedConfig extends MyAlgorithmConfig
		{
			private final DoubleParam diameterPixels;

			public MyTranslatedConfig()
			{
				super( "My translated algorithm", "Configure the parameters for my translated algorithm." );

				this.diameterPixels = addDoubleParameter()
						.key( "DIAMETER" )
						.name( "Diameter" )
						.units( "µm" )
						.defaultValue( 30.0 )
						.get();

				// Store pixels, display in physical units
				final double pixelSize = 0.2; // µm/pixel
				setDisplayTranslator(
						diameterPixels,
						v -> v * pixelSize, // Display: pixels -> µm
						v -> v / pixelSize // Store: µm -> pixels
				);
			}
		}
		final MyTranslatedConfig translatedConfig = new MyTranslatedConfig();
		FrameBuilder.build( translatedConfig, new MyTask(), new MyTranslatedConfig() ).setVisible( true );
		

	}
}
