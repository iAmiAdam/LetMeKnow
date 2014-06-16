package info.adamjsmith.letmeknow;

import android.app.Activity;
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
    	startActivity(new Intent("info.adamjsmith.letmeknow.SelectContact"));
    }
    
    public void mapClick(View view) {
    	startActivity(new Intent("info.adamjsmith.letmeknow.MapActivity"));
    }
}
