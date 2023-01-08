package com.example.intelligent_library;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.intelligent_library.db_sqlite.DBHelperMenu;


public class SigninDialogFragment extends DialogFragment {

    private DBHelperMenu dbHelper;
    private SQLiteDatabase db;
    private Cursor maincursor;

    private MainActivity m;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login, null,
                false);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layo
        Button login = (Button) view.findViewById(R.id.login_login);
        Button reg = (Button) view.findViewById(R.id.login_register);
        Button cancel = (Button) view.findViewById(R.id.login_cancel);

        EditText ed_name = (EditText) view.findViewById(R.id.username);
        EditText ed_password = (EditText) view.findViewById(R.id.password);

        builder.setView(view);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelperMenu item_dbHelper = new DBHelperMenu(getActivity());
                SQLiteDatabase item_db = item_dbHelper.getWritableDatabase();
                maincursor = item_db.rawQuery(
                        "SELECT _id, username,useraccount, userpassword FROM user WHERE useraccount='"+ed_name.getText().toString()+"' AND userpassword='"+ed_password.getText().toString()+"'", null);
                if(maincursor != null){
                    SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = getPrefs.edit();
                    editor.putBoolean("state",true).apply();
                    System.out.println("state："+getPrefs.getBoolean("state",false));
                    maincursor.moveToFirst();
                    editor.putString("username", maincursor.getString(1));
                    maincursor.moveToFirst();
                    editor.putString("useraccount",maincursor.getString(2));
                    editor.commit();
                    SigninDialogFragment.this.getDialog().dismiss();
                    getActivity().recreate();
                    //Intent intent = getActivity().getIntent();
                    //getActivity().finish();
                    //startActivity(intent);
                    Toast.makeText(view.getContext(), "登入成功!", Toast.LENGTH_SHORT).show();
                }
                maincursor.close();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SigninDialogFragment.this.getDialog().dismiss();
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SigninDialogFragment.this.getDialog().dismiss();
            }
        });

        return builder.create();
    }
}