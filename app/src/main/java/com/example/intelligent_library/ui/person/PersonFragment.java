package com.example.intelligent_library.ui.person;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.intelligent_library.R;
import com.example.intelligent_library.databinding.FragmentHomeBinding;

public class PersonFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PersonViewModel personViewModel =
                new ViewModelProvider(this).get(PersonViewModel.class);

        View root = inflater.inflate(R.layout.personal, container, false);

        TextView name = (TextView) root.findViewById(R.id.person_name);
        TextView account = (TextView) root.findViewById(R.id.person_account);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String n = getPrefs.getString("username","");
        String a = getPrefs.getString("useraccount","");

        name.setText(n);
        account.setText(a);

        Button bor = (Button) root.findViewById(R.id.person_borrow_book);
        Button re = (Button) root.findViewById(R.id.person_return_book);

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

