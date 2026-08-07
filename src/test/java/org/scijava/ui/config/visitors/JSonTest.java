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
package org.scijava.ui.config.visitors;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.scijava.ui.config.Cellpose3BuiltinModels;
import org.scijava.ui.config.Cellpose3Config;

public class JSonTest
{

	// Use a temporary folder for testing
	@org.junit.Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void test() throws IOException
	{
		final int nChannels = 100;

		final Cellpose3Config original = new Cellpose3Config( nChannels, 0.5, "um" );
		original.builtinModel().set( Cellpose3BuiltinModels.NUCLEI );
		original.builtinOrCustom().select( original.customModel() );
		original.customModel().set( "TROLOLO path" );
		original.chan1().set( 11 );
		original.chan2().set( 22 );
		original.diameter().set( 33. );
		original.flowThreshold().set( 0.005 );
		original.cellprobThreshold().set( .15 );
		original.exportFlows().set( true );
		original.exportLabels().set( false );
		original.exportROIs().set( true );

		final String targetPath = tempFolder.newFile( "test.json" ).getAbsolutePath();
		JSon.serialize( targetPath, original );
		final Cellpose3Config deserialized = new Cellpose3Config( nChannels, 0.5, "um" );
		JSon.deserialize( targetPath, deserialized );

		// Test for equality of parameter values
		final Map< String, Object > originalParameters = new HashMap<>();
		original.forEach( p -> originalParameters.put( p.getKey(), p.getValue() ) );
		original.getSelectables().forEach( s -> originalParameters.put( s.getKey(), s.getSelection().getKey() ) );

		final Map< String, Object > deserializedParameters = new HashMap<>();
		deserialized.forEach( p -> deserializedParameters.put( p.getKey(), p.getValue() ) );
		deserialized.getSelectables().forEach( s -> deserializedParameters.put( s.getKey(), s.getSelection().getKey() ) );
		
		assertThat( deserializedParameters ).containsExactlyInAnyOrderEntriesOf( originalParameters );
	}
}
