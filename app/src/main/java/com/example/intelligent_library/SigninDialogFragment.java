package com.example.intelligent_library;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SigninDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sign_in, null,
                false);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layo
        builder.setView(inflater.inflate(R.layout.sign_in, null))
                // Add action buttons
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(view.getContext(), "登入成功!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(view.getContext(), "登入取消", Toast.LENGTH_SHORT).show();
                        SigninDialogFragment.this.getDialog().cancel();
                    }
                })
                .setNeutralButton(R.string.clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(view.getContext(), "清除登入資訊", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        onCreateDialog(null);
                    }
                });
        return builder.create();
    }
}