package com.example.intelligent_library.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intelligent_library.MainActivity;
import com.example.intelligent_library.R;
import com.example.intelligent_library.databinding.FragmentHomeBinding;
import com.example.intelligent_library.databinding.FragmentReturnbookBinding;
import com.example.intelligent_library.ui.returnbook.ReturnbookViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button bor = (Button) root.findViewById(R.id.borrow_book);
        Button re = (Button) root.findViewById(R.id.return_book);

        bor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                int id = navController.getCurrentDestination().getId();
                navController.popBackStack(id,true);
                navController.navigate(R.id.nav_borrow_final);
            }
        });

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                int id = navController.getCurrentDestination().getId();
                navController.popBackStack(id,true);
                navController.navigate(R.id.nav_returnbook);
            }
        });




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
        }

