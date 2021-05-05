package site.travis.myclock;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import site.travis.myclock.widget.Clock;

public class MainActivity extends AppCompatActivity {

	private View mRootView;
	private Clock mClockView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);

		ViewPager pager = findViewById(R.id.view_pager);
		pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int i) {
				Fragment fragment;
				if(i==0) fragment = new FragmentClockSim();
				else fragment = new FragentClockDigit();
				return fragment;
			}

			@Override
			public int getCount() {
				return 2;
			}
		});
		mRootView = findViewById(R.id.root);
		mClockView = findViewById(R.id.clock);
	}
}

//public class ViewPagerActivity extends AppCompatActivity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_view_pager);
//		ViewPager pager = findViewById(R.id.view_pager);
//		pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//			@Override
//			public Fragment getItem(int i) {
//				return new HelloFragment();
//			}
//
//			@Override
//			public int getCount() {
//				return 3;
//			}
//		});
//	}
//}
