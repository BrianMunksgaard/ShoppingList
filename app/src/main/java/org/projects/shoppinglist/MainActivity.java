package org.projects.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.projects.shoppinglist.domain.Product;
import org.projects.shoppinglist.fragment.YNDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements YNDialog.OnPositiveListener, YNDialog.OnNegativeListener {

    /*
     * The current context (this).
     */
    private Context context;

    /*
     * Return code from preferences.
     */
    private final int RESULT_CODE_PREFERENCES = 1;

    /*
     * The number of the currently checked item.
     */
    private int currentCheckedItem;

    private ArrayAdapter<Product> adapter;
    private ListView listView;
    private ArrayList<Product> bag = new ArrayList<>();

    /*
     * A flag used to indicate whether or an item should really be removed from the bag.
     */
    private boolean reallyRemoveItem = true;

    /*
     * For the quantity spinner.
     */
    private String[] spinnerItems = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    /**
     * @return The adapter used for the product list (shopping bag).
     */
    public ArrayAdapter getMyAdapter()
    {
        return adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        //Needed to get the toolbar to work on older versions
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null)
        {
            ArrayList<Product> _bag = savedInstanceState.getParcelableArrayList("shoppingBag");
            if(_bag != null) {
                bag = _bag;
            }
        }

        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview
        adapter =  new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_checked,bag );

        //setting the adapter on the listview
        listView.setAdapter(adapter);
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button addButton =  findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBag_onClick(v);
            }
        });

        findViewById(R.id.deleteItemButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteItemFromBag_onClick(v);
            }
        });

        findViewById(R.id.deleteAllButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 clearBag_onClick(v);
            }
        });

        ((ListView)findViewById(R.id.list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentCheckedItem = i;
            }
        });

        // Put number in the quantity spinner.
        Spinner spinnerQuantity = findViewById(R.id.spinnerQuantity);
        ArrayAdapter<String> spinnerQuantityAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        spinnerQuantity.setAdapter(spinnerQuantityAdapter);


        //add some stuff to the list so we have something
        // to show on app startup
        boolean autofill = ShoppingAppSettingsFragment.shouldAutoFill(this);

        if(bag.isEmpty() && autofill) {
            bag.add(new Product("Bananas", 1));
            bag.add(new Product("Apples", 1));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivityForResult(intent, RESULT_CODE_PREFERENCES);
                break;
            case R.id.action_clearAll:
                adapter.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD - To be nice!
        super.onSaveInstanceState(outState);

		/* Here we put code now to save the state */
        outState.putParcelableArrayList("shoppingBag", bag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CODE_PREFERENCES) //the code means we came back from settings
        {
            //I can can these methods like this, because they are static
            boolean autoFill = ShoppingAppSettingsFragment.shouldAutoFill(this);
            String message = "Should we autofill: " + autoFill;
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addToBag_onClick(View view) {

        // Get product item.
        EditText itemRef = findViewById(R.id.item);
        String itemText = itemRef.getText().toString();

        // Only retrieve quantity if there is
        // an item.
        if(!itemText.isEmpty()) {

            // Get quantity from spinner.
            Spinner spinnerQuantity = findViewById(R.id.spinnerQuantity);
            int quantityFromSpinner = spinnerQuantity.getSelectedItemPosition() + 1;

            // Get quantity from edit text.
            EditText quantityRef = findViewById(R.id.itemQuantity);
            String quantityText = quantityRef.getText().toString();

            int noOfItems = quantityFromSpinner;
            if(!quantityText.isEmpty()) {
                noOfItems = Integer.valueOf(quantityText);
            }

            adapter.add(new Product(itemText, noOfItems));
            itemRef.getText().clear();
            quantityRef.getText().clear();
            spinnerQuantity.setSelection(0);
        }
    }

    public void deleteItemFromBag_onClick(View view) {

        final String productName = bag.get(currentCheckedItem).getName();

        // Hide the keyboard.
        final View parent = findViewById(R.id.layout);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);

        // Use snackbar to confirm deletion.
        reallyRemoveItem = true;
        Snackbar snackbar = Snackbar
                .make(parent, "Really remove " + productName + "!", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reallyRemoveItem = false;
                        Snackbar snackbar = Snackbar.make(parent, "Remove of " + productName + " cancelled!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });

        snackbar.addCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (reallyRemoveItem) {
                    bag.remove(currentCheckedItem);
                    adapter.notifyDataSetChanged();
                }
            }

        });
        snackbar.show();
    }

    public void clearBag_onClick(View view) {

        // Use dialog to confirm clear bag.
        YNDialog dialog = new YNDialog();
        dialog.show(getFragmentManager(), "YNFragment");
    }

    @Override
    public void onNegativeClicked() {

    }

    @Override
    public void onPositiveClicked() {

        // Clear the bag.
        adapter.clear();

        //Do your update stuff here to the listview
        //and the bag etc
        //just to show how to get arguments from the bag.
        /*
        Toast toast = Toast.makeText(context,
                "positive button clicked", Toast.LENGTH_LONG);
        toast.show();
        bag.clear(); //here you can do stuff with the bag and
        */
        //adapter etc.
    }
}
