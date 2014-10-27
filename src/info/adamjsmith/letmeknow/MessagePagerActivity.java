package info.adamjsmith.letmeknow;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class MessagePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Message> mMessages;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mMessages = InstanceHolder.get(this).getMessages();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mMessages.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Message message = mMessages.get(pos);
				return MessageFragment.newInstance(message.getId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int pos) {
				@SuppressWarnings("unused")
				Message message = mMessages.get(pos);
			}
			
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {	}
			
			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});
		
		UUID crimeId = (UUID)getIntent()
				.getSerializableExtra(MessageFragment.EXTRA_MESSAGE_ID);
		for (int i = 0; i < mMessages.size(); i++) {
			if(mMessages.get(i).getId().equals(crimeId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
