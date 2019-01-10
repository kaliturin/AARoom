package com.example.anton.aaroom.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

import com.example.anton.aaroom.R;
import com.example.anton.aaroom.databinding.MainFragmentBinding;
import com.example.anton.aaroom.ui.PopupActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.BindingObject;
import org.androidannotations.annotations.DataBound;
import org.androidannotations.annotations.EFragment;

import timber.log.Timber;

@DataBound
@EFragment(R.layout.main_fragment)
public class MainFragment extends Fragment {

    @BindingObject
    protected MainFragmentBinding binding;

    public static MainFragment newInstance() {
        return MainFragment_.builder().build();
    }

    @AfterViews
    protected void onAfterViews() {

        binding.button.setOnClickListener(v -> PopupActivity_.intent(getContext()).start());

        MainViewModel mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mViewModel.init(getContext());

        long started = SystemClock.elapsedRealtime();

        mViewModel.upsert("1", new Item("John", "NY", 1));
        mViewModel.upsert("2", new Item("Sam", "CA", 2));
        mViewModel.upsert("3", new Item("Mike", "SPB", 3));

        Item item = mViewModel.select("2");

        long finished = SystemClock.elapsedRealtime() - started;

        Timber.tag("TEST1").d(item == null ? "null" : item.toString());
        Timber.tag("TEST1").d("time = " + finished);

        mViewModel.clear();
        mViewModel.destroy(getContext());
    }
}
