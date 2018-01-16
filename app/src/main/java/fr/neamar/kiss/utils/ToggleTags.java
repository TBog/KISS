package fr.neamar.kiss.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.util.ArrayList;

import fr.neamar.kiss.KissApplication;
import fr.neamar.kiss.R;
import fr.neamar.kiss.TagsHandler;

/**
 * Created by TBog on 1/11/2018.
 */

public class ToggleTags
{
	private final View              toggleBarView;
	private final ViewGroup         toggleContainer;
	private final ArrayList<String> hiddenTagsList;
	private final ToggleUpdatedListener toggleUpdatedListener;
	private final CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener()
	{
		@Override
		public void onCheckedChanged( CompoundButton buttonView, boolean isChecked )
		{
			String tag = ((ToggleButton)buttonView).getTextOff().toString();
			if( isChecked )
			{
				// add the tag to the hidden list only once
				if( !hiddenTagsList.contains( tag ) )
				{
					hiddenTagsList.add( tag );
					toggleUpdatedListener.OnToggleUpdated();
				}
			}
			else
			{
				hiddenTagsList.remove( tag );
				toggleUpdatedListener.OnToggleUpdated();
			}
		}
	};

	public interface ToggleUpdatedListener
	{
		void OnToggleUpdated();
	}

	public ToggleTags( View toggleBar, ToggleUpdatedListener listener )
	{
		super();

		toggleBarView = toggleBar;
		toggleContainer = (ViewGroup)toggleBar.findViewById( R.id.tags_toggle_list );
		hiddenTagsList = new ArrayList<>( 5 );
		toggleUpdatedListener = listener;
		RefreshContainer();
	}

	private void RefreshContainer()
	{
		toggleContainer.removeAllViews();
		// get some tags, this should be a list created by the user
		TagsHandler tagsHandler = KissApplication.getApplication(toggleBarView.getContext()).getDataHandler().getTagsHandler();
		String[] list = tagsHandler.getAllTagsAsArray();
		for( int i = 0; i < 5; i += 1 )
		{
			ToggleButton button = (ToggleButton)LayoutInflater.from( toggleContainer.getContext() )
															  .inflate( R.layout.tags_toggle_item, toggleContainer, false );
			button.setTextOn( "\u2013" + list[i] ); // U+2013 en dash
			button.setTextOff( list[i] );
			button.setChecked( false );
			button.setOnCheckedChangeListener( checkedChangeListener );

			toggleContainer.addView( button );
		}
	}

	public void showBar()
	{
		toggleBarView.setVisibility( View.VISIBLE );
	}

	public void hideBar()
	{
		toggleBarView.setVisibility( View.GONE );
	}

	public ArrayList<String> getHiddenTags()
	{
		return hiddenTagsList;
	}
}
