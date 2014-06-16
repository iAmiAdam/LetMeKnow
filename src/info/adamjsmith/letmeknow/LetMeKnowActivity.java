package info.adamjsmith.letmeknow;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LetMeKnowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void contactClick(View view) {
    	int request_Code = 1;
    	startActivity(new Intent("info.adamjsmith.letmeknow.SelectContact"));
    }
    
    public void mapClick(View view) {
    	FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		setContentView(R.layout.map);
		Map fragment1 = new Map();
		fragmentTransaction.replace(R.id.mapView, fragment1);
		fragmentTransaction.commit();
		
    }
}
