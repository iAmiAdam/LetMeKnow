package info.adamjsmith.letmeknow;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class InstancePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Instance> mInstances;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mInstances = InstanceHolder.get(this).getInstances();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mInstances.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Instance instance = mInstances.get(pos);
				return InstanceFragment.newInstance(instance.getId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				@SuppressWarnings("unused")
				Instance instance = mInstances.get(pos);
			}
			
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {	}
			
			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});
		
		UUID instanceId = (UUID)getIntent()
				.getSerializableExtra(InstanceFragment.EXTRA_INSTANCE_ID);
		for (int i = 0; i < mInstances.size(); i++) {
			if(mInstances.get(i).getId().equals(instanceId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
