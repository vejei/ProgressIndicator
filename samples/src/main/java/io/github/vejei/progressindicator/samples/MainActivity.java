package io.github.vejei.progressindicator.samples;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        ViewPager2 pager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        pager.setAdapter(new PageAdapter(this));
        new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Indeterminate");
            } else if (position == 1) {
                tab.setText("Determinate");
            }
        }).attach();
    }
}