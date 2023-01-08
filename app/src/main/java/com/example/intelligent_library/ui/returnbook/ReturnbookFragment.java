package com.example.intelligent_library.ui.returnbook;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intelligent_library.R;
import com.example.intelligent_library.bookList;
import com.example.intelligent_library.databinding.FragmentReturnbookBinding;
import com.example.intelligent_library.db_sqlite.DBHelperMenu;
import com.example.intelligent_library.ui.borrow.BorrowFragment_Test;

import org.w3c.dom.Text;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReturnbookFragment extends Fragment {

    private FragmentReturnbookBinding binding;

    private Cursor maincursor_item;
    private Cursor maincursor;

    private DBHelperMenu item_dbHelper;
    private SQLiteDatabase item_db;

    RecyclerView mRecycle;
    bookList booklist;
    MyListAdapter myListAdapter;

    int n_order;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReturnbookViewModel returnbookViewModel =
                new ViewModelProvider(this).get(ReturnbookViewModel.class);

        binding = FragmentReturnbookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String account = getPrefs.getString("useraccount","");

        item_dbHelper = new DBHelperMenu(getContext());
        item_db = item_dbHelper.getWritableDatabase();
        mRecycle = root.findViewById(R.id.return_book_recycle);
        mRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycle.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        maincursor_item = item_db.rawQuery(
                "SELECT _id, useraccount, book_id,borrow_time,return_time FROM borrow_record WHERE useraccount = '"+account+"'", null);

        myListAdapter = new MyListAdapter(maincursor_item);
        mRecycle.setAdapter(myListAdapter);

        System.out.println("11");
        //final TextView textView = binding.textReturnbook;
        //returnbookViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
        private Cursor mycursor;

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView img;
            private Button p_b;
            private TextView name,author,return_time;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.return_img);
                p_b = (Button) itemView.findViewById(R.id.return_book_button);
                name = (TextView) itemView.findViewById(R.id.return_bookname);
                author = (TextView) itemView.findViewById(R.id.return_bookauthor);
                return_time = (TextView) itemView.findViewById(R.id.return_time);

            }
        }

        public MyListAdapter(Cursor cursor) {
            //this.activity = activity;
            this.mycursor = cursor;
        }

        @NonNull
        @Override
        public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_return,parent,false);
            return new MyListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyListAdapter.ViewHolder holder, int position) {

            System.out.println(position);
            mycursor.moveToPosition(position);
            int bookid = mycursor.getInt(2);

            maincursor = item_db.rawQuery(
                    "SELECT _id, bookname, bookauthor,bookcategory,image,borrow_state FROM book_list WHERE _id = "+bookid+"", null);
            maincursor.moveToFirst();
            String bookname = maincursor.getString(1);
            holder.name.setText("書名："+ bookname);
            maincursor.moveToFirst();
            String bookauthor = maincursor.getString(2);
            holder.author.setText("作者："+bookauthor);
            maincursor.moveToFirst();
            int bookimg = maincursor.getInt(4);
            holder.img.setImageResource(bookimg);
            maincursor.moveToFirst();
            String bookcate = maincursor.getString(3);

            mycursor.moveToPosition(position);
            holder.return_time.setText("歸還期限："+mycursor.getString(4));

            mycursor.moveToPosition(position);
            holder.p_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long db_id;
                        ContentValues upcv = new ContentValues();
                        upcv.put("_id",bookid);
                        upcv.put("bookname",bookname);
                        upcv.put("bookauthor",bookauthor);
                        upcv.put("bookcategory",bookcate);
                        upcv.put("image",bookimg);
                        upcv.put("borrow_state",true);
                        db_id = item_db.update("book_list",upcv,"_id="+bookid,null);

                        long id1;
                        id1 = item_db.delete("borrow_record","book_id="+bookid,null);

                        Toast.makeText(getContext(),"已歸還成功",Toast.LENGTH_LONG).show();
                        refres();
                    }
                });



            maincursor.close();
        }

        @Override
        public int getItemCount() {

            return mycursor.getCount();
        }



    }

    public void refres(){
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String account = getPrefs.getString("useraccount","");

        maincursor_item = item_db.rawQuery(
                "SELECT _id, useraccount, book_id,borrow_time,return_time FROM borrow_record WHERE useraccount = '"+account+"'", null);

        myListAdapter = new MyListAdapter(maincursor_item);
        mRecycle.setAdapter(myListAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        maincursor_item.close();
        binding = null;
    }


}