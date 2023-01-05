package com.example.intelligent_library.ui.borrow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.intelligent_library.R;
import com.example.intelligent_library.databinding.FragmentBorrowBinding;

public class BorrowFragment extends Fragment {

    private FragmentBorrowBinding binding;

    Spinner dropdown;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_borrow, container, false);

        dropdown = root.findViewById(R.id.spinner);
        initspinnerfooter(root);
        return root;
    }

    private void initspinnerfooter(View root) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.menu_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                ImageView img1 = (ImageView) root.findViewById(R.id.imageView4);
                ImageView img2 = (ImageView) root.findViewById(R.id.imageView5);
                ImageView img3 = (ImageView) root.findViewById(R.id.imageView6);
                ImageView img4 = (ImageView) root.findViewById(R.id.imageView7);
                ImageView img5 = (ImageView) root.findViewById(R.id.imageView8);
                ImageView img6 = (ImageView) root.findViewById(R.id.imageView9);
                ImageView img7 = (ImageView) root.findViewById(R.id.imageView10);
                ImageView img8 = (ImageView) root.findViewById(R.id.imageView11);
                ImageView img9 = (ImageView) root.findViewById(R.id.imageView12);

                switch (pos) {
                    case 0:
                        img1.setImageResource(R.drawable.a1);
                        img2.setImageResource(R.drawable.a2);
                        img3.setImageResource(R.drawable.a3);
                        img4.setImageResource(R.drawable.a4);
                        img5.setImageResource(R.drawable.a5);
                        img6.setImageResource(R.drawable.a6);
                        img7.setImageResource(R.drawable.a7);
                        img8.setImageResource(R.drawable.a8);
                        img9.setImageResource(R.drawable.a9);
                        break;
                    case 1:
                        img1.setImageResource(R.drawable.b1);
                        img2.setImageResource(R.drawable.b2);
                        img3.setImageResource(R.drawable.b3);
                        img4.setImageResource(R.drawable.b4);
                        img5.setImageResource(R.drawable.b5);
                        img6.setImageResource(R.drawable.b6);
                        img7.setImageResource(R.drawable.b7);
                        img8.setImageResource(R.drawable.b8);
                        img9.setImageResource(R.drawable.b9);
                        break;
                    case 2:
                        img1.setImageResource(R.drawable.c1);
                        img2.setImageResource(R.drawable.c2);
                        img3.setImageResource(R.drawable.c3);
                        img4.setImageResource(R.drawable.c4);
                        img5.setImageResource(R.drawable.c5);
                        img6.setImageResource(R.drawable.c6);
                        img7.setImageResource(R.drawable.c7);
                        img8.setImageResource(R.drawable.c8);
                        img9.setImageResource(R.drawable.c9);
                        break;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}