package com.example.testdisasterevent.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.testdisasterevent.R;

// RegisterActivity class, which extends AppCompatActivity
public class RegisterActivity extends AppCompatActivity {

    // onCreate method called when the activity is created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call the superclass onCreate method
        super.onCreate(savedInstanceState);
        // Set the content view of the activity
        setContentView(R.layout.register_activity);
        // Check if savedInstanceState is null (i.e., the activity is being created for the first time)
        if (savedInstanceState == null) {
            // Begin a new fragment transaction to replace the container with an instance of RegisterFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RegisterFragment.newInstance())
                    .commitNow();
        }
    }

    // Method to listen for the return (home) button on the top bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if the item's ID is equal to the home button's ID
        if(item.getItemId() == android.R.id.home) {
            // Finish the activity and return to the previous one
            finish();
        }
        // Call the superclass onOptionsItemSelected method
        return super.onOptionsItemSelected(item);
    }
}
