package org.projects.shoppinglist.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import org.projects.shoppinglist.R;


//You can pretty much reuse this class in your own project
//if you want you can modify some of the text shown below.
//of course if it was for a multilingual app you would put
//the actual text that is now hardcoded inside the strings.xml file
public class YNDialog extends DialogFragment {

    /*
     * Call back reference for positive (yes) click.
     */
    private OnPositiveListener pCallback = null;

    /*
     * Call back reference for negative (no) click.
     */
    private OnNegativeListener nCallback = null;

    /*
     * Text to use for dialog title.
     */
    private String dlgTitle = "";

    /*
     * Text to use as dialog question.
     */
    private String dlgQuestion = "";


    /**
     * Default constructor.
     */
    public YNDialog() { }

    /**
     * Define interface to implement if user of
     * dialog wants to handle positive clicks.
     */
    public interface OnPositiveListener {
        void onPositiveClicked();
    }

    /**
     * Define interface to implement if user of
     * dialog wants to handle negative clicks.
     */
    public interface OnNegativeListener {
        void onNegativeClicked();
    }

    /**
     * Attach event listeners to the activity if implemented
     * in the activity.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnPositiveListener) {
            pCallback = (OnPositiveListener) activity;
        }

        if (activity instanceof OnNegativeListener) {
            nCallback = (OnNegativeListener) activity;
        }
    }

    /**
     * Build the dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create alert dialog and attach to current activity.
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        // Set dialog texts.
        alert.setTitle(dlgTitle.isEmpty() ? getResources().getString(R.string.confirmation) : dlgTitle);
        alert.setMessage(dlgQuestion.isEmpty() ? getResources().getString(R.string.areYouSure) : dlgQuestion);

        // Attach button click handlers.
        alert.setPositiveButton(R.string.yes, pListener);
        alert.setNegativeButton(R.string.no, nListener);

        return alert.create();
    }

    /*
     * Button click handler for the positive/yes button. Simply calls the positiveClick()
     * method.
     */
    DialogInterface.OnClickListener pListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            positiveClick();
        }
    };

    /*
     * Button click handler for the negative/no button. Simply calls the negativeClick()
     * method.
     */
    DialogInterface.OnClickListener nListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            negativeClick();
        }
    };

    /**
     * Calls the positive (yes) click handler in the activity using
     * this dialog.
     */
    protected void positiveClick()
    {
        if(pCallback != null) pCallback.onPositiveClicked();
    }

    /**
     * Calls the negative (no) click handler in the activity using
     * this dialog.
     */
    protected void negativeClick()
    {
        if(nCallback != null) nCallback.onNegativeClicked();
    }

    /**
     * Set the title of the dialog.
     * @param dlgTitle
     */
    public void setDlgTitle(String dlgTitle) {
        this.dlgTitle = dlgTitle;
    }

    /**
     * Set the dialog question.
     * @param dlgQuestion
     */
    public void setDlgQuestion(String dlgQuestion) {
        this.dlgQuestion = dlgQuestion;
    }
}
