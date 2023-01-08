package com.example.intelligent_library.ui.borrow;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intelligent_library.BorrowDialogFragment;
import com.example.intelligent_library.chat.MainActivity_chat;
import com.example.intelligent_library.db_sqlite.DBHelperMenu;
import com.example.intelligent_library.R;
import com.example.intelligent_library.bookList;
import com.example.intelligent_library.databinding.FragmentBorrowBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class BorrowFragment_Test extends Fragment {

    private FragmentBorrowBinding binding;

    Spinner dropdown;

    RecyclerView mRecycle;
    bookList booklist;

    int n_order;

    private Context context;
    private ListView listView = null;
    private ImageButton fab;
    private DBHelperMenu dbHelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter adapter;
    private SimpleCursorAdapter item_adapter;
    private Cursor maincursor; // 記錄目前資料庫查詢指標
    private Cursor maincursor_item;
    MyListAdapter myListAdapter;

    private DBHelperMenu item_dbHelper;
    private SQLiteDatabase item_db;

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    private static final int NOTIFICATION_ID = 0;

    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";

    private NotificationManager mNotifyManager;

    private NotificationReceiver mReceiver = new NotificationReceiver();

    private Button button_notify;
    private Button button_cancel;
    private Button button_update;
    private long db_id;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_borrow_final, container, false);

        dropdown = root.findViewById(R.id.spinner);
        initspinnerfooter(root);

        int id = getResources().getIdentifier("button1", "id", getContext().getPackageName());
        View eventView = root.findViewById(id);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        n_order = getPrefs.getInt("order",0);
        SharedPreferences.Editor editor = getPrefs.edit();



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
                item_dbHelper = new DBHelperMenu(getContext());
                item_db = item_dbHelper.getWritableDatabase();
                mRecycle = root.findViewById(R.id.borrow_recycle_xml);
                mRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecycle.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                switch (pos) {
                    case 0:
                            // 1.取得查詢所有資料的cursor
                            maincursor_item = item_db.rawQuery(
                                    "SELECT _id, bookname, bookauthor,bookcategory,image,borrow_state FROM book_list WHERE bookcategory = '文學小說'", null);
                            // 2.設定ListAdapter適配器(使用SimpleCursorAdapter)
                            myListAdapter = new MyListAdapter(maincursor_item);
                            // 3.注入適配器
                            mRecycle.setAdapter(myListAdapter);
                        break;
                    case 1:
                            // 1.取得查詢所有資料的cursor
                            maincursor_item = item_db.rawQuery(
                                    "SELECT _id, bookname, bookauthor,bookcategory,image,borrow_state FROM book_list WHERE bookcategory = '童話故事書'", null);
                            // 2.設定ListAdapter適配器(使用SimpleCursorAdapter)
                            myListAdapter = new MyListAdapter(maincursor_item);
                            // 3.注入適配器
                            mRecycle.setAdapter(myListAdapter);
                        break;
                    case 2:
                            // 1.取得查詢所有資料的cursor
                            maincursor_item = item_db.rawQuery(
                                    "SELECT _id, bookname, bookauthor,bookcategory,image,borrow_state FROM book_list WHERE bookcategory = '世界經典文學'", null);
                            // 2.設定ListAdapter適配器(使用SimpleCursorAdapter)
                            myListAdapter = new MyListAdapter(maincursor_item);
                            // 3.注入適配器
                            mRecycle.setAdapter(myListAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
    }


    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroyView();
        binding = null;
    }

    public void refresh(String cate) {
        maincursor_item = item_db.rawQuery(
                "SELECT _id, bookname, bookauthor,bookcategory,image,borrow_state FROM book_list WHERE bookcategory = '"+cate+"'", null);
        // 2.設定ListAdapter適配器(使用SimpleCursorAdapter)
        myListAdapter = new MyListAdapter(maincursor_item);
        // 3.注入適配器
        mRecycle.setAdapter(myListAdapter);
    }


    private class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
        private Activity activity;
        //private List<bookList> myData;
        private Cursor mycursor;


        //Iterator<addOrder> a = cart.iterator();
        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView p_img1,p_img2,p_img3;
            private Button p_b,p_b2,p_b3;
            private LinearLayout l2,l3;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                p_img1 = (ImageView) itemView.findViewById(R.id.borrow_recycle_img1);
                p_b = (Button) itemView.findViewById(R.id.borrow_recycle_button1);

                p_img2 = (ImageView) itemView.findViewById(R.id.borrow_recycle_img2);
                p_b2 = (Button) itemView.findViewById(R.id.borrow_recycle_button2);

                p_img3 = (ImageView) itemView.findViewById(R.id.borrow_recycle_img3);
                p_b3 = (Button) itemView.findViewById(R.id.borrow_recycle_button3);

                l2 = (LinearLayout) itemView.findViewById(R.id.borrow_recycle_linear2);
                l3 = (LinearLayout) itemView.findViewById(R.id.borrow_recycle_linear3);
            }
        }

        public MyListAdapter(Cursor cursor) {
            //this.activity = activity;
            this.mycursor = cursor;
        }

        @NonNull
        @Override
        public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_borrow,parent,false);
            return new MyListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            mycursor.moveToPosition(3*position);
            System.out.println(position);
            holder.p_img1.setImageResource(mycursor.getInt(4));
            mycursor.moveToPosition(3*position);
            if(mycursor.getInt(5)== 1){
                holder.p_b.setEnabled(true);
                holder.p_b.setBackgroundColor(getResources().getColor(R.color.purple_200));
                holder.p_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        View v1 = getLayoutInflater().inflate(R.layout.dialog_borrow,null);

                        ImageView img = (ImageView) v1.findViewById(R.id.dialog_borrow_img);
                        TextView name = (TextView) v1.findViewById(R.id.dialog_borrow_bookname);
                        TextView author = (TextView) v1.findViewById(R.id.dialog_borrow_author);

                        mycursor.moveToPosition(3*position);
                        int bookid = mycursor.getInt(0);
                        mycursor.moveToPosition(3*position);
                        String bookname = mycursor.getString(1);
                        mycursor.moveToPosition(3*position);
                        String bookauthor = mycursor.getString(2);
                        mycursor.moveToPosition(3*position);
                        String bookcate = mycursor.getString(3);
                        mycursor.moveToPosition(3*position);
                        int bookimg = mycursor.getInt(4);

                        name.setText("書名："+ bookname);
                        author.setText("作者："+bookauthor);
                        img.setImageResource(bookimg);

                        mycursor.moveToPosition(position);
                        builder.setPositiveButton("借閱", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                        Boolean state = getPrefs.getBoolean("state",false);
                                        if(state==true){
                                            //
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            String strStr = sdf.format(new Date());
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(new Date());
                                            c.add(Calendar.DAY_OF_WEEK,14);
                                            String reStr = sdf.format(c.getTime());
                                            ContentValues cv = new ContentValues();
                                            cv.put("useraccount",getPrefs.getString("useraccount",""));
                                            cv.put("book_id",bookid);
                                            cv.put("borrow_time",strStr);
                                            cv.put("return_time",reStr);
                                            db_id = item_db.insert("borrow_record",null,cv);
                                            //
                                            //
                                            ContentValues upcv = new ContentValues();
                                            upcv.put("_id",bookid);
                                            upcv.put("bookname",bookname);
                                            upcv.put("bookauthor",bookauthor);
                                            upcv.put("bookcategory",bookcate);
                                            upcv.put("image",bookimg);
                                            upcv.put("borrow_state",false);
                                            db_id = item_db.update("book_list",upcv,"_id="+bookid,null);

                                            //
                                            refresh(bookcate);
                                            sendNotification(bookname,bookauthor,bookimg);
                                            dialog.dismiss();
                                        }else {
                                            Toast.makeText(getContext(),"請先登入",Toast.LENGTH_LONG).show();
                                        }

                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });


                        builder.setView(v1);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }else{
                holder.p_b.setEnabled(false);
                holder.p_b.setBackgroundColor(getResources().getColor(R.color.colorDarkGray));
            }

            try {
                mycursor.moveToPosition(1+3*position);
                holder.p_img2.setImageResource(mycursor.getInt(4));;
                System.out.println(mycursor.getInt(4));
                mycursor.moveToPosition(1+3*position);
                if(mycursor.getInt(5)==1){
                    holder.p_b2.setEnabled(true);
                    holder.p_b2.setBackgroundColor(getResources().getColor(R.color.purple_200));
                    holder.p_b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View v1 = getLayoutInflater().inflate(R.layout.dialog_borrow,null);

                            ImageView img = (ImageView) v1.findViewById(R.id.dialog_borrow_img);
                            TextView name = (TextView) v1.findViewById(R.id.dialog_borrow_bookname);
                            TextView author = (TextView) v1.findViewById(R.id.dialog_borrow_author);

                            mycursor.moveToPosition(1+3*position);
                            int bookid = mycursor.getInt(0);
                            mycursor.moveToPosition(1+3*position);
                            String bookname = mycursor.getString(1);
                            mycursor.moveToPosition(1+3*position);
                            String bookauthor = mycursor.getString(2);
                            mycursor.moveToPosition(1+3*position);
                            String bookcate = mycursor.getString(3);
                            mycursor.moveToPosition(1+3*position);
                            int bookimg = mycursor.getInt(4);

                            name.setText("書名："+ bookname);
                            author.setText("作者："+bookauthor);
                            img.setImageResource(bookimg);

                            mycursor.moveToPosition(position);
                            builder.setPositiveButton("借閱", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                            Boolean state = getPrefs.getBoolean("state",false);
                                            if(state==true){
                                                //
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                String strStr = sdf.format(new Date());
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(new Date());
                                                c.add(Calendar.DAY_OF_WEEK,14);
                                                String reStr = sdf.format(c.getTime());
                                                ContentValues cv = new ContentValues();
                                                cv.put("useraccount",getPrefs.getString("useraccount",""));
                                                cv.put("book_id",bookid);
                                                cv.put("borrow_time",strStr);
                                                cv.put("return_time",reStr);
                                                db_id = item_db.insert("borrow_record",null,cv);
                                                //
                                                //
                                                ContentValues upcv = new ContentValues();
                                                upcv.put("_id",bookid);
                                                upcv.put("bookname",bookname);
                                                upcv.put("bookauthor",bookauthor);
                                                upcv.put("bookcategory",bookcate);
                                                upcv.put("image",bookimg);
                                                upcv.put("borrow_state",false);
                                                db_id = item_db.update("book_list",upcv,"_id="+bookid,null);

                                                //
                                                refresh(bookcate);
                                                sendNotification(bookname,bookauthor,bookimg);
                                                dialog.dismiss();
                                            }else {
                                                Toast.makeText(getContext(),"請先登入",Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });


                            builder.setView(v1);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }else{
                    holder.p_b2.setEnabled(false);
                    holder.p_b2.setBackgroundColor(getResources().getColor(R.color.colorDarkGray));
                }

            }catch (IndexOutOfBoundsException e){
                holder.l2.setVisibility(View.INVISIBLE);
            }


            try {
                mycursor.moveToPosition(2+3*position);
                holder.p_img3.setImageResource(mycursor.getInt(4));
                mycursor.moveToPosition(2+3*position);
                if(mycursor.getInt(5)==1){
                    holder.p_b3.setEnabled(true);
                    holder.p_b3.setBackgroundColor(getResources().getColor(R.color.purple_200));
                    holder.p_b3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            View v1 = getLayoutInflater().inflate(R.layout.dialog_borrow,null);

                            ImageView img = (ImageView) v1.findViewById(R.id.dialog_borrow_img);
                            TextView name = (TextView) v1.findViewById(R.id.dialog_borrow_bookname);
                            TextView author = (TextView) v1.findViewById(R.id.dialog_borrow_author);

                            mycursor.moveToPosition(2+3*position);
                            int bookid = mycursor.getInt(0);
                            mycursor.moveToPosition(2+3*position);
                            String bookname = mycursor.getString(1);
                            mycursor.moveToPosition(2+3*position);
                            String bookauthor = mycursor.getString(2);
                            mycursor.moveToPosition(2+3*position);
                            String bookcate = mycursor.getString(3);
                            mycursor.moveToPosition(2+3*position);
                            int bookimg = mycursor.getInt(4);

                            name.setText("書名："+ bookname);
                            author.setText("作者："+bookauthor);
                            img.setImageResource(bookimg);

                            mycursor.moveToPosition(position);
                            builder.setPositiveButton("借閱", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                                            Boolean state = getPrefs.getBoolean("state",false);
                                            if(state==true){
                                                //
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                String strStr = sdf.format(new Date());
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(new Date());
                                                c.add(Calendar.DAY_OF_WEEK,14);
                                                String reStr = sdf.format(c.getTime());
                                                ContentValues cv = new ContentValues();
                                                cv.put("useraccount",getPrefs.getString("useraccount",""));
                                                cv.put("book_id",bookid);
                                                cv.put("borrow_time",strStr);
                                                cv.put("return_time",reStr);
                                                db_id = item_db.insert("borrow_record",null,cv);
                                                //
                                                //
                                                ContentValues upcv = new ContentValues();
                                                upcv.put("_id",bookid);
                                                upcv.put("bookname",bookname);
                                                upcv.put("bookauthor",bookauthor);
                                                upcv.put("bookcategory",bookcate);
                                                upcv.put("image",bookimg);
                                                upcv.put("borrow_state",false);
                                                db_id = item_db.update("book_list",upcv,"_id="+bookid,null);

                                                //
                                                refresh(bookcate);
                                                sendNotification(bookname,bookauthor,bookimg);
                                                dialog.dismiss();
                                            }else {
                                                Toast.makeText(getContext(),"請先登入",Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });


                            builder.setView(v1);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }else{
                    holder.p_b3.setEnabled(false);
                    holder.p_b3.setBackgroundColor(getResources().getColor(R.color.colorDarkGray));
                }
                getContext().registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));

                createNotificationChannel();
            }catch (IndexOutOfBoundsException e){
                holder.l3.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public int getItemCount() {

            return (int)Math.ceil(mycursor.getCount()/3.0);
        }

       /* public void swapCursor(Cursor newCursor) {
            if (mycursor != null) {
                mycursor.close();
            }

            mycursor = newCursor;

            if (newCursor != null) {
                notifyDataSetChanged();
            }
        }*/


    }

    //以下Notification

    public void sendNotification(String name,String author,int img) {
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (getContext(), NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);
        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(),img);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        //notifyBuilder.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent);
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle(name)).setContentText("成功借閱該本書。");
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());


    }

    public void updateNotification() {

        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(),R.drawable.mascot_1);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"));
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());


    }

    public void cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID);

    }


    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(getContext(),BorrowFragment_Test.class );

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(getContext(),
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(getContext(), PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_android)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        return notifyBuilder;
    }

    public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getActivity().getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);

        }
    }


    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the notification
        }
    }
}