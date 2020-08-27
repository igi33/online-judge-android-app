package com.example.onlinejudge.ui;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinejudge.R;
import com.example.onlinejudge.adapters.TagAdapter;
import com.example.onlinejudge.auth.SessionManager;
import com.example.onlinejudge.databinding.ActivityMainBinding;
import com.example.onlinejudge.helpers.RecyclerItemClickListener;
import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.ui.fragments.LoginFragment;
import com.example.onlinejudge.viewmodels.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main";
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private ActionBarDrawerToggle toggle;
    private TagAdapter tagAdapter;
    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarTop);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.isLoggedIn().setValue(sessionManager.isLoggedIn());
        viewModel.getUser().setValue(sessionManager.isLoggedIn() ? sessionManager.getUserDetails() : null);

        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbarTop, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING && !binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // this where Drawer start opening
                    viewModel.setLoading(true);
                    viewModel.observeTags()
                            .subscribe(result -> viewModel.getTags().setValue(result),
                                    error -> {
                                        Log.e(TAG, "observeTags: " + error.getMessage());
                                    },
                                    () -> viewModel.setLoading(false));
                }
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        binding.drawerLayout.addDrawerListener(toggle);

        viewModel.getFragment().observe(this, fragment -> getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit,
                        R.anim.fragment_open_enter, R.anim.fragment_open_exit)
                .replace(R.id.fragment_container_view, fragment)
                .commit());

        viewModel.getToastMessage().observe(this, msg -> {
                if (msg != null && !msg.isEmpty()) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
            });

        viewModel.getTags().observe(this, tags -> {
            Log.e(TAG, "tags->onChanged: " + tags.size());
            tagAdapter.updateList(tags);
        });

        tagAdapter = new TagAdapter(this, new ArrayList<>());
        RecyclerView recyclerView = binding.navigationDrawer.getHeaderView(0).findViewById(R.id.recycler_view_drawer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tagAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView tv = view.findViewById(R.id.text_view_tag_name);
                        viewModel.setToastMessage("tag name " + tv.getText() + "tag id: " + tv.getTag());
                        binding.drawerLayout.closeDrawers();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {}
                })
        );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        viewModel.isLoggedIn().observe(this, isLoggedIn -> {
            binding.toolbarTop.getMenu().findItem(R.id.item_login).setVisible(!isLoggedIn);
            binding.toolbarTop.getMenu().findItem(R.id.item_profile).setVisible(isLoggedIn);
            binding.toolbarTop.getMenu().findItem(R.id.item_logout).setVisible(isLoggedIn);
        });

        binding.toolbarTop.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_login:
                    viewModel.setFragment(new LoginFragment());
                    return true;
                case R.id.item_logout:
                    sessionManager.logoutUser();
                    viewModel.isLoggedIn().setValue(false);
                    viewModel.getUser().setValue(null);
                    return true;
                default:
                    return false;
            }
        });
        return true;
    }
}
