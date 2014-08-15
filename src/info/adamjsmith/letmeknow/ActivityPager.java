package info.adamjsmith.letmeknow;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ActivityPager extends FragmentActivity {
	private final List<Fragment> mFragments = new ArrayList<Fragment>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initPaging();
	}
	
	private void initPaging() {
		Fragment instanceList = new InstanceListFragment();
		Fragment messageList = new MessageListFragment();
		Fragment locationList = new LocationListFragment();
		
		PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
		pagerAdapter.addFragment(instanceList);
		pagerAdapter.addFragment(messageList);
		pagerAdapter.addFragment(locationList);
		
		ViewPager viewPager = new ViewPager(this);
		viewPager.setId(R.id.viewPager);
		viewPager.setAdapter(pagerAdapter);
		setContentView(viewPager);
		viewPager.setCurrentItem(0);
		
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				switch(pos) {
				case 0:
					setTitle("Let Me Know");
					break;
				case 1:
					setTitle("Messages");
					break;
				case 2:
					setTitle("Locations");
					break;
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});
	}
	
	private class PagerAdapter extends FragmentPagerAdapter {

		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		public void addFragment(Fragment fragment) {
			mFragments.add(fragment);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
		
	}
}
