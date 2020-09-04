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
import com.example.onlinejudge.helpers.JwtTokenInterceptor;
import com.example.onlinejudge.helpers.RecyclerItemClickListener;
import com.example.onlinejudge.models.Tag;
import com.example.onlinejudge.models.User;
import com.example.onlinejudge.ui.fragments.HomeFragment;
import com.example.onlinejudge.ui.fragments.LoginFragment;
import com.example.onlinejudge.ui.fragments.RegisterFragment;
import com.example.onlinejudge.ui.fragments.TaskFormFragment;
import com.example.onlinejudge.viewmodels.MainViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private ActionBarDrawerToggle toggle;
    private TagAdapter tagAdapter;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    SessionManager sessionManager;
    @Inject
    JwtTokenInterceptor jwtTokenInterceptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarTop);

        // initialize and bind view model
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        // set logged in state based on session manager
        viewModel.isLoggedIn().setValue(sessionManager.isLoggedIn());
        if (sessionManager.isLoggedIn()) {
            User user = sessionManager.getUserDetails();
            viewModel.getUser().setValue(user);
            jwtTokenInterceptor.setToken(user.getToken());
        }

        // load tags when the drawer is opening
        toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbarTop, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING && !binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // this where Drawer start opening
                    viewModel.setLoading(true);
                    compositeDisposable.add(viewModel.observeTags()
                            .subscribe(result -> {
                                result.add(0, new Tag(0, "All", ""));
                                viewModel.getTags().setValue(result);
                            },
                            error -> {
                                Log.e(TAG, "observeTags: " + error.getMessage());
                            },
                            () -> viewModel.setLoading(false)));
                }
            }
        };
        binding.drawerLayout.addDrawerListener(toggle);

        // update fragment in the view to reflect view model's assigned fragment
        viewModel.getFragment().observe(this, fragment -> getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fragment_open_enter, R.anim.fragment_open_exit,
                        R.anim.fragment_open_enter, R.anim.fragment_open_exit)
                .replace(R.id.fragment_container_view, fragment)
                .commit());

        // show toast message when the view model's message changed
        viewModel.getToastMessage().observe(this, msg -> {
                if (msg != null && !msg.isEmpty()) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
            });

        // update tag adapter list when view model's tags changed
        viewModel.getTags().observe(this, tags -> {
            Log.e(TAG, "tags->onChanged: " + tags.size());
            tagAdapter.updateList(tags);
        });

        // initialize tag adapter, recycler view and set click actions
        tagAdapter = new TagAdapter(this, new ArrayList<>());
        RecyclerView recyclerView = binding.navigationDrawer.getHeaderView(0).findViewById(R.id.recycler_view_drawer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(tagAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView tv = view.findViewById(R.id.text_view_tag_name);
                        int tagId = Integer.parseInt(tv.getTag().toString());
                        viewModel.setTitle(tagId > 0 ? "Tagged " + tv.getText() : getResources().getString(R.string.app_name));
                        viewModel.setFragment(HomeFragment.newInstance(tagId));
                        binding.drawerLayout.closeDrawers();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {}
                })
        );

        viewModel.getFabOnClickListener().observe(this, onClickListener -> {
            binding.floatingActionButton.setOnClickListener(onClickListener);
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // sync the toggle state after onRestoreInstanceState has occurred
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu, this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        viewModel.isLoggedIn().observe(this, isLoggedIn -> {
            binding.toolbarTop.getMenu().findItem(R.id.item_login).setVisible(!isLoggedIn);
            binding.toolbarTop.getMenu().findItem(R.id.item_profile).setVisible(isLoggedIn);
            binding.toolbarTop.getMenu().findItem(R.id.item_logout).setVisible(isLoggedIn);
            binding.toolbarTop.getMenu().findItem(R.id.item_register).setVisible(!isLoggedIn);
            binding.toolbarTop.getMenu().findItem(R.id.item_create_task).setVisible(isLoggedIn);
        });

        // set on click actions for the options in the menu at the right
        binding.toolbarTop.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_login:
                    viewModel.setFragment(LoginFragment.newInstance());
                    return true;
                case R.id.item_logout:
                    sessionManager.logoutUser();
                    jwtTokenInterceptor.setToken(null);
                    viewModel.isLoggedIn().setValue(false);
                    viewModel.getUser().setValue(null);
                    viewModel.setFragment(HomeFragment.newInstance(0));
                    return true;
                case R.id.item_register:
                    viewModel.setFragment(RegisterFragment.newInstance());
                    return true;
                case R.id.item_create_task:
                    viewModel.setFragment(TaskFormFragment.newInstance(0));
                    return true;
                default:
                    return false;
            }
        });
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
