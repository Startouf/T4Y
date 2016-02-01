package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import de.tum.score.transport4you.mobile.R;
import de.tum.score.transport4you.mobile.application.applicationcontroller.IMainApplication;
import de.tum.score.transport4you.mobile.application.applicationcontroller.impl.ApplicationSingleton;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicketType;

public class ETicketListScreen extends Activity implements IPresentation{
    private IMainApplication mainApplication;

	private Context currentContext;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		currentContext = this;
        setContentView(R.layout.eticketlist);

       //creating dummy ticket objects
        ETicket aETicket = new ETicket();
		ETicket bETicket = new ETicket();
		ETicket cETicket = new ETicket();

		aETicket.setCustomerId("Arpit");
		aETicket.setId(123);
		bETicket.setCustomerId("Cyril");
		bETicket.setId(547);
		cETicket.setCustomerId("Samit");
		cETicket.setId(789);

		aETicket.setValidUntil(new Date(116, 11, 2));
		aETicket.setInvalidatedAt(new Date(116, 11, 2));
		bETicket.setValidUntil(new Date(116,11,21));
		bETicket.setInvalidatedAt(new Date(116, 11, 28));
		cETicket.setValidUntil(new Date(115,10,21));
		cETicket.setInvalidatedAt(new Date(115, 10, 15));


		mainApplication = ApplicationSingleton.getApplicationController();
        mainApplication.registerActivity(this);
        
		BlobEntry entry = mainApplication.getStoredBlobEntry();
       /*
		ArrayList<ETicket> tickets = new ArrayList<ETicket>();
		
		//get etickets from blob
        if (entry != null) {
        	tickets = entry.geteTicketList();
        }
        
        // Create the adapter to convert the array to views
        TicketAdapter adapter = new TicketAdapter(this, 0, tickets);
        */
		//adding the object to the list and defining the listener
		ArrayList<ETicket> tickets = new ArrayList<ETicket>();
		tickets.add(aETicket);
		tickets.add(bETicket);
		tickets.add(cETicket);
		final TicketAdapter adapter = new TicketAdapter(this, 0, tickets);
        // Attach the adapter to a ListView
        final ListView listView = (ListView) findViewById(R.id.list_etickets);
        listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


				Intent newIntent = new Intent(currentContext, QR.class);
				Object listItem = listView.getItemAtPosition(position);
				//passing the object as a String to QR.java
				newIntent.putExtra("message", listItem.toString());
				startActivity(newIntent);

				//TODO select right etickettype name

			}
		});

		//
    }
	
    public void onDestroy() {
    	super.onDestroy();
    }

	@Override
	public void shutdown() {
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.mn_exit:
	        mainApplication.shutdownSystem();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void updateProgessDialog(String title, String message, boolean visible, Integer increment) {
		// TODO Auto-generated method stub
		
	}
}