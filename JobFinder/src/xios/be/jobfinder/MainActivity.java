package xios.be.jobfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button newSearch;
	private Button savedSearches;
	private Button favorites;
	private Button settings;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        newSearch = (Button) findViewById(R.id.btn_new_search);
        savedSearches = (Button) findViewById(R.id.btn_saved_searches);
        favorites = (Button) findViewById(R.id.btn_favorites);
        settings = (Button) findViewById(R.id.btn_settings);
        
        newSearch.setOnClickListener(new NewSearchHandler());
        savedSearches.setOnClickListener(new SavedSearchesHandler());
        favorites.setOnClickListener(new FavoritesHandler());
        settings.setOnClickListener(new SettingsHandler());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private class NewSearchHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent newSearch = new Intent(getApplicationContext(), SearchParams.class);
			startActivity(newSearch);
		}
    }
    
    private class SavedSearchesHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent savedSearches = new Intent(getApplicationContext(), SavedSearchActivity.class);
			startActivity(savedSearches);
		}
    }
    
    private class FavoritesHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent favorites = new Intent(getApplicationContext(), FavoritesActivity.class);
			startActivity(favorites);
		}
    }
    
    private class SettingsHandler implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent settings = new Intent(getApplicationContext(), SearchParams.class);
			startActivity(settings);
		}
    }
    
}