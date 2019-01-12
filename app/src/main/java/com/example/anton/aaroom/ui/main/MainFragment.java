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

        int i = 0;
        viewModel.setValue("" + ++i, new Item("John", "NY", i), "1");
        viewModel.setValue("" + ++i, new Item("Sam", "CA", i), "1");

        while (i<2000) {
            viewModel.setValue("" + ++i, new Item("Tony", "SPB", i), "2");
        }

        while (i<4000) {
            viewModel.setValue("" + ++i, new Item("Mike", "MSK", i), "3");
        }

        viewModel.deleteBy("2");

        Item item = viewModel.getValue("2222");
        Timber.tag("TEST1").d(item == null ? "null" : item.toString());

        viewModel.deleteBy("3");

        item = viewModel.getValue("2222");

        long finished = SystemClock.elapsedRealtime() - started;

        Timber.tag("TEST1").d(item == null ? "null" : item.toString());
        Timber.tag("TEST1").d("time = " + finished);

//        viewModel.deleteAll();
//        viewModel.destroy();
    }
}
