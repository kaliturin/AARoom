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

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        long started = SystemClock.elapsedRealtime();

        viewModel.setValue("1", new Item("John", "NY", 1));
        viewModel.setValue("2", new Item("Sam", "CA", 2));
        viewModel.setValue("3", new Item("Mike", "SPB", 3));

        Item item = viewModel.getValue("2");

        long finished = SystemClock.elapsedRealtime() - started;

        Timber.tag("TEST1").d(item == null ? "null" : item.toString());
        Timber.tag("TEST1").d("time = " + finished);

        //viewModel.deleteAll();
        //viewModel.destroy();
    }
}
