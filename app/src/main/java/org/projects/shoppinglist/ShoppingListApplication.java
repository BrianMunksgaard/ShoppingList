package org.projects.shoppinglist;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class ShoppingListApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        if(!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(this);
            Log.d( "firebase" , "persistance enabled" );
        } else {
            Log.d( "firebase" , "persistance not enabled" );
        }
    }
}
