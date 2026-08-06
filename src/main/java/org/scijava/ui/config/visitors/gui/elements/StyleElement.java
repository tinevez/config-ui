package org.scijava.ui.config.visitors.gui.elements;

/**
 * A style element is a configuration parameter that can be visited by a
 * {@link StyleElementVisitor} to create a GUI component for it.
 * <p>
 * Based on a design by Tobias Pietzsch in Mastodon.
 */
public interface StyleElement
{
	public default void update()
	{}

	public void accept( StyleElementVisitor visitor );
}
