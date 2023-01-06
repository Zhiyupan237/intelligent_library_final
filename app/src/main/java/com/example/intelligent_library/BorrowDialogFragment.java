package com.example.intelligent_library;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class BorrowDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.borrow, null,
                false);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layo
        builder.setView(inflater.inflate(R.layout.borrow, null))
                // Add action buttons
                .setPositiveButton(R.string.borrow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(view.getContext(), "借閱成功!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancelborrow, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(view.getContext(), "取消借閱", Toast.LENGTH_SHORT).show();
                        BorrowDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}