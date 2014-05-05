package android.car.control.ui_components;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

@SuppressLint("ValidFragment") public class ListDialogFragment extends DialogFragment{
	
	private CharSequence[] values;
	
	
	public ListDialogFragment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ListDialogFragment(ArrayList<String> values){
		this.values = (CharSequence[])values.toArray(new String[0]);
	}


	 /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ListDialogListener {
        public void onDialogSelect(DialogFragment dialog, int which);
    }
    
    // Use this instance of the interface to deliver action events
    private  ListDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ListDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    builder.setTitle("Select device to connect")
		    .setItems(this.values, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogSelect(ListDialogFragment.this, which);
				}
			});
		    return  builder.create();
	}
}
