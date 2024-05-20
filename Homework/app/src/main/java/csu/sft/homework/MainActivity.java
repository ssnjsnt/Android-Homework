package csu.sft.homework;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(FriendListFragment.newInstance(), "Friends");
        viewPagerAdapter.addFragment(LoadingFragment.newInstance(), "Loading");
        viewPager.setAdapter(viewPagerAdapter);
        String[] originalTabTexts = new String[tabLayout.getTabCount()];
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            originalTabTexts[i] = tabLayout.getTabAt(i).getText().toString();
        }
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {tab.setText(originalTabTexts[position]);
                }
        ).attach();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}