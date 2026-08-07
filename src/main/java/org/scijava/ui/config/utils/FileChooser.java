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
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * A utility class for opening file chooser dialogs. It automatically selects
 * between {@link JFileChooser} and {@link java.awt.FileDialog} based on the
 * platform, unless explicitly overridden.
 */
public class FileChooser
{

	/** Prevent instantiation of utility class. */
	private FileChooser()
	{}

	/**
	 * Flag to force the use of {@link JFileChooser} instead of the native
	 * {@link java.awt.FileDialog}. By default, this is {@code false} on macOS
	 * and {@code true} on other platforms.
	 */
	public static boolean useJFileChooser = !isMac();

	/**
	 * Enumerates the type of file chooser dialog to display.
	 */
	public enum DialogType
	{
		/** Open/load file dialog. */
		LOAD,
		/** Save file dialog. */
		SAVE
	}

	/**
	 * Enumerates the selection mode for the file chooser.
	 */
	public enum SelectionMode
	{
		/** Allow selection of files only. */
		FILES_ONLY,
		/** Allow selection of directories only. */
		DIRECTORIES_ONLY,
		/** Allow selection of both files and directories. */
		FILES_AND_DIRECTORIES
	}

	/**
	 * Opens a file chooser dialog with the specified parent, initial file, and
	 * dialog type.
	 *
	 * @param parent
	 *            the parent component.
	 * @param selectedFile
	 *            the initially selected file path, or {@code null}.
	 * @param dialogType
	 *            whether to open a load or save dialog.
	 * @return the selected file, or {@code null} if the dialog was canceled.
	 */
	public static File chooseFile(
			final Component parent,
			final String selectedFile,
			final DialogType dialogType )
	{
		return chooseFile( parent, selectedFile, null, null, dialogType );
	}

	/**
	 * Opens a file chooser dialog with the specified parent, initial file, file
	 * filter, title, and dialog type.
	 *
	 * @param parent
	 *            the parent component.
	 * @param selectedFile
	 *            the initially selected file path, or {@code null}.
	 * @param fileFilter
	 *            the file filter to apply, or {@code null}.
	 * @param dialogTitle
	 *            the dialog title, or {@code null} for a default title.
	 * @param dialogType
	 *            whether to open a load or save dialog.
	 * @return the selected file, or {@code null} if the dialog was canceled.
	 */
	public static File chooseFile(
			final Component parent,
			final String selectedFile,
			final FileFilter fileFilter,
			final String dialogTitle,
			final DialogType dialogType )
	{
		return chooseFile( parent, selectedFile, fileFilter, dialogTitle, dialogType, SelectionMode.FILES_ONLY );
	}

	/**
	 * Opens a file chooser dialog with the specified parent, initial file, file
	 * filter, title, dialog type, and selection mode.
	 *
	 * @param parent
	 *            the parent component.
	 * @param selectedFile
	 *            the initially selected file path, or {@code null}.
	 * @param fileFilter
	 *            the file filter to apply, or {@code null}.
	 * @param dialogTitle
	 *            the dialog title, or {@code null} for a default title.
	 * @param dialogType
	 *            whether to open a load or save dialog.
	 * @param selectionMode
	 *            what can be selected: files, directories, or both.
	 * @return the selected file, or {@code null} if the dialog was canceled.
	 */
	public static File chooseFile(
			final Component parent,
			final String selectedFile,
			final FileFilter fileFilter,
			final String dialogTitle,
			final DialogType dialogType,
			final SelectionMode selectionMode )
	{
		return chooseFile( useJFileChooser, parent, selectedFile, fileFilter, dialogTitle, dialogType, selectionMode );
	}

	/**
	 * Opens a file chooser dialog with full control over the dialog type, file
	 * filter, title, selection mode, and whether to use JFileChooser.
	 *
	 * @param useJFileChooser
	 *            if {@code true}, use {@link JFileChooser}; otherwise use
	 *            {@link java.awt.FileDialog}.
	 * @param parent
	 *            the parent component.
	 * @param selectedFile
	 *            the initially selected file path, or {@code null}.
	 * @param fileFilter
	 *            the file filter to apply, or {@code null}.
	 * @param dialogTitle
	 *            the dialog title, or {@code null} for a default title.
	 * @param dialogType
	 *            whether to open a load or save dialog.
	 * @param selectionMode
	 *            what can be selected: files, directories, or both.
	 * @return the selected file, or {@code null} if the dialog was canceled.
	 */
	public static File chooseFile(
			boolean useJFileChooser,
			final Component parent,
			final String selectedFile,
			final FileFilter fileFilter,
			final String dialogTitle,
			final DialogType dialogType,
			final SelectionMode selectionMode )
	{
		final boolean isSaveDialog = dialogType == DialogType.SAVE;
		final boolean isDirectoriesOnly = selectionMode == SelectionMode.DIRECTORIES_ONLY;

		if ( isSaveDialog && isDirectoriesOnly )
			useJFileChooser = true; // FileDialog cannot handle this

		/*
		 * Determine dialog title:
		 *
		 * If a dialogTitle is given, just use that.
		 *
		 * Otherwise, use "Open" or "Save", depending on DialogType. If a
		 * FileFilter is provided, append the FileFilter description, leading to
		 * "Open xml files" or similar.
		 */
		String title = dialogTitle;
		if ( title == null )
			title = ( isSaveDialog ? "Save" : "Open" )
					+ ( fileFilter == null ? "" : " " + fileFilter.getDescription() );

		File file = null;
		if ( useJFileChooser )
		{
			final JFileChooser fileChooser = new JFileChooser();

			fileChooser.setDialogTitle( title );

			if ( selectedFile != null )
				fileChooser.setSelectedFile( new File( selectedFile ) );

			switch ( selectionMode )
			{
			case FILES_ONLY:
				fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
				break;
			case DIRECTORIES_ONLY:
				fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				break;
			case FILES_AND_DIRECTORIES:
				fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
				break;
			}

			fileChooser.setFileFilter( fileFilter );

			final int returnVal = isSaveDialog
					? fileChooser.showSaveDialog( parent )
					: fileChooser.showOpenDialog( parent );
			if ( returnVal == JFileChooser.APPROVE_OPTION )
				file = fileChooser.getSelectedFile();
		}
		else // use FileDialog
		{
			final int fdMode = isSaveDialog ? FileDialog.SAVE : FileDialog.LOAD;

			/*
			 * If provided parent is a Frame or a Dialog, we can use it.
			 * Otherwise use null as parent.
			 */
			final FileDialog fd;
			if ( parent != null && parent instanceof Frame )
				fd = new FileDialog( ( Frame ) parent, title, fdMode );
			else if ( parent != null && parent instanceof Dialog )
				fd = new FileDialog( ( Dialog ) parent, title, fdMode );
			else
				fd = new FileDialog( ( Frame ) null, title, fdMode );

			/*
			 * If a selectedFile path was provided, set it.
			 */
			if ( selectedFile != null )
			{
				if ( isDirectoriesOnly )
				{
					fd.setDirectory( selectedFile );
					fd.setFile( null );
				}
				else
				{
					fd.setDirectory( new File( selectedFile ).getParent() );
					fd.setFile( new File( selectedFile ).getName() );
				}
			}

			/*
			 * Handle SelectionMode DIRECTORIES_ONLY.
			 */
			System.setProperty( "apple.awt.fileDialogForDirectories", isDirectoriesOnly ? "true" : "false" );

			/*
			 * Try with a FilenameFilter (may silently fail).
			 */
			final AtomicBoolean workedWithFilenameFilter = new AtomicBoolean( false );
			if ( fileFilter != null )
			{
				final FilenameFilter filenameFilter = new FilenameFilter()
				{
					private boolean firstTime = true;

					@Override
					public boolean accept( final File dir, final String name )
					{
						if ( firstTime )
						{
							workedWithFilenameFilter.set( true );
							firstTime = false;
						}

						return fileFilter.accept( new File( dir, name ) );
					}
				};
				fd.setFilenameFilter( filenameFilter );
				fd.setVisible( true );
			}
			if ( fileFilter == null || ( isMac() && !workedWithFilenameFilter.get() ) )
			{
				fd.setFilenameFilter( null );
				fd.setVisible( true );
			}

			final String filename = fd.getFile();
			if ( filename != null )
			{
				file = new File( fd.getDirectory() + filename );
			}
		}

		return file;
	}

	private static boolean isMac()
	{
		final String OS = System.getProperty( "os.name", "generic" ).toLowerCase( Locale.ENGLISH );
		return ( OS.indexOf( "mac" ) >= 0 ) || ( OS.indexOf( "darwin" ) >= 0 );
	}
}
