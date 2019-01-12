package com.example.anton.aaroom.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.SystemClock;
import android.support.v4.app.Fragment;

import com.example.anton.aaroom.R;
import com.example.anton.aaroom.databinding.MainFragmentBinding;
import com.example.anton.aaroom.ui.PopupActivity_;
import com.example.anton.aaroom.ui.main.cache.CacheEntry;

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

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        long started = SystemClock.elapsedRealtime();

        viewModel.setValue("1", new Item("John", "NY", 1), "1");
        viewModel.setValue("2", new Item("Sam", "CA", 2), "1");

        for (int i = 3; i <= 5000; i++) {
            viewModel.setValue("" + i, new Item("Mike", "SPB", i), "2");
        }

//        CacheEntry entry = viewModel.getEntry("222");
//        if (entry != null) {
//            viewModel.deleteEntry(entry);
//        }

        Item item = viewModel.getValue("222");

        long finished = SystemClock.elapsedRealtime() - started;

        Timber.tag("TEST1").d(item == null ? "null" : item.toString());
        Timber.tag("TEST1").d("time = " + finished);

        viewModel.deleteAll();
        viewModel.destroy();
    }
}
