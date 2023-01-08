package com.example.intelligent_library;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.intelligent_library.chat.MainActivity_chat;
import com.example.intelligent_library.db_sqlite.DBHelperMenu;
import com.example.intelligent_library.ui.borrow.BorrowFragment_Test;
import com.example.intelligent_library.ui.home.HomeFragment;
import com.example.intelligent_library.ui.person.PersonFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
//import android.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.intelligent_library.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private CustomReceiver mReceiver = new CustomReceiver();
    private static final String ACTION_CUSTOM_BROADCAST =
            BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sharedPreferences
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = getPrefs.edit();
        Boolean state = getPrefs.getBoolean("state",false);
        editor.putBoolean("if_item",false).apply();
        editor.commit();
        //end-sharePreferences

        DBHelperMenu item_dbHelper = new DBHelperMenu(this);
        SQLiteDatabase item_db = item_dbHelper.getWritableDatabase();
        //item_dbHelper.onDropTable(item_db);
        //item_dbHelper.onDropTable_record(item_db);
        item_dbHelper.onDropTable_user(item_db);

        //item_dbHelper.onCreate(item_db);
        //item_dbHelper.onCreate_record(item_db);
        item_dbHelper.onCreate_user(item_db);

        creatDBtable();
        creatUser();

        System.out.println("state："+state);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this,MainActivity_chat.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_borrow_final, R.id.nav_returnbook,R.id.nav_activity,R.id.nav_person,R.id.return_book,R.id.borrow_book)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(state == true){
            View h = navigationView.getHeaderView(0);
            TextView Head_name = (TextView) h.findViewById(R.id.header_name);
            TextView Head_account = (TextView) h.findViewById(R.id.header_account);

            Head_name.setText(getPrefs.getString("username",""));
            Head_account.setText(getPrefs.getString("useraccount",""));

        }else{

        }


        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_POWER_CONNECTED);

        this.registerReceiver(mReceiver, filter);

        Intent customBroadcastIntent = new Intent(ACTION_CUSTOM_BROADCAST);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver,
                        new IntentFilter(ACTION_CUSTOM_BROADCAST));


        LocalBroadcastManager.getInstance(this).sendBroadcast(customBroadcastIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean state = getPrefs.getBoolean("state",false);
        if(state==true){
            getMenuInflater().inflate(R.menu.main_logout, menu);
        }else{
            getMenuInflater().inflate(R.menu.main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_login:
                // User chose the "Settings" item, show the app settings UI...
                DialogFragment newFragment = new SigninDialogFragment();
                newFragment.show(getSupportFragmentManager(),"login");
                //newFragment.show(getSupportFragmentManager(), "dialog_login");
                Toast.makeText(this,"Login",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_settings:
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = getPrefs.edit();
                editor.putBoolean("state",false).apply();
                editor.putString("username", "");
                editor.putString("useraccount","");
                editor.commit();
                recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    //新增商品清單到資料庫
    private void creatDBtable(){
        DBHelperMenu item_dbHelper = new DBHelperMenu(this);
        SQLiteDatabase item_db = item_dbHelper.getWritableDatabase();

        Iterator<bookList> it_order = getMenuItemList().iterator();

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = getPrefs.edit();
        boolean g_order = getPrefs.getBoolean("if_item",false);
        // 取得Editor
        if(g_order == false){
            while(it_order.hasNext()){
                bookList it = it_order.next();

                ContentValues cv = new ContentValues();
                cv.put("bookname", it.getbook_name());
                cv.put("bookauthor", it.getbook_author());
                cv.put("bookcategory", it.getbook_category());
                cv.put("image", it.getbook_image());
                cv.put("borrow_state",it.getborrow_state());

                // 執行SQL語句
                long id = item_db.insert("book_list", null, cv);
            }
            editor.putBoolean("if_item",true).apply();
            editor.commit();
        }
    }

    private void creatUser(){
        DBHelperMenu item_dbHelper = new DBHelperMenu(this);
        SQLiteDatabase item_db = item_dbHelper.getWritableDatabase();
        long id;
        ContentValues cv = new ContentValues();
        cv.put("username","TEST");
        cv.put("useraccount","user");
        cv.put("userpassword","pass");

        id = item_db.insert("user",null,cv);
    }

    private void addToDBtable(){
        DBHelperMenu item_dbHelper = new DBHelperMenu(this);
        SQLiteDatabase item_db = item_dbHelper.getWritableDatabase();

        Iterator<bookList> it_order = getMenuItemList().iterator();

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = getPrefs.edit();
        boolean g_order = getPrefs.getBoolean("add_item",false);
        // 取得Editor
        if(g_order == false){
            while(it_order.hasNext()){
                bookList it = it_order.next();

                ContentValues cv = new ContentValues();
                cv.put("bookname", it.getbook_name());
                cv.put("bookauthor", it.getbook_author());
                cv.put("bookcategory", it.getbook_category());
                cv.put("image", it.getbook_image());

                // 執行SQL語句
                long id = item_db.insert("book_list", null, cv);
            }
            editor.putBoolean("add_item",true).apply();
            editor.commit();
        }
    }

    private List<bookList> getMenuItemList() {
        List<bookList> data = new ArrayList<bookList>();
        data.add(new bookList("我喜歡我自己","潘柏霖","文學小說",R.drawable.a1,true));
        data.add(new bookList("昨天的孩子","瑪莎","文學小說", R.drawable.a2,true));
        data.add(new bookList("金魚夜夢", "林薇晨","文學小說",R.drawable.a3,true));
        data.add(new bookList("科學少女","Azy Chia","文學小說", R.drawable.a4,true));
        data.add(new bookList("餘生是你 晚點沒關係","黃山料", "文學小說", R.drawable.a5,true));
        data.add(new bookList("陽光回憶","迅清" ,"文學小說",R.drawable.a6,true));
        data.add(new bookList("時光旅人", "馬雷特\n" +
                "韓德森","文學小說",R.drawable.a7,true));
        data.add(new bookList("別來無恙", "晨羽","文學小說",R.drawable.a8,true));
        data.add(new bookList("發生的事情","巫本添 John Bon-Tien Wu","文學小說", R.drawable.a9,true));
        data.add(new bookList("你會坦然面對，每一場告別","角子","文學小說",R.drawable.a10,true));
        data.add(new bookList("米奇7號","愛德華．艾希頓（Edward Ashton）","文學小說",R.drawable.a11,true));
        data.add(new bookList("52赫茲的鯨魚們","町田苑香","文學小說",R.drawable.a12,true));
        data.add(new bookList("解鎖師","史蒂夫．漢密頓（Steve Hamilton）","文學小說",R.drawable.a13,true));
        data.add(new bookList("點燈人","艾瑪‧史東尼克斯（Emma Stonex）","文學小說",R.drawable.a14,true));
        data.add(new bookList("如此蒼白的心","哈維爾．馬利亞斯（Javier Marías）","文學小說",R.drawable.a15,true));

        data.add(new bookList("猜猜我有多愛你","山姆．麥克布雷尼 (Sam McBratney)","童話故事書", R.drawable.b1,true));
        data.add(new bookList("那些散落的星星","維多莉亞．傑米森（Victoria Jamieson）\n" +
                "歐馬．穆罕默德（Omar Mohamed）","童話故事書", R.drawable.b2,true));
        data.add(new bookList("聖誕老人的秘密基地","艾倫．史諾（Alan Snow）","童話故事書", R.drawable.b3,true));
        data.add(new bookList("謝謝你來當我的寶貝","西元洋","童話故事書", R.drawable.b4,true));
        data.add(new bookList("親愛的動物園","羅德．坎貝爾（Rod Campbell）","童話故事書", R.drawable.b5,true));
        data.add(new bookList("森林100層樓的家","岩井俊雄","童話故事書", R.drawable.b6,true));
        data.add(new bookList("我來幫你開","吉竹伸介","童話故事書", R.drawable.b7,true));
        data.add(new bookList("下雨的書店","日向理惠子","童話故事書", R.drawable.b8,true));
        data.add(new bookList("想哭就哭成一座噴水池","諾耶蜜．沃拉","童話故事書", R.drawable.b9,true));
        data.add(new bookList("奇蹟麵包店","譚雅‧葛雷諾","童話故事書",R.drawable.b10,true));
        data.add(new bookList("太熱的話，脫外套就好啦！","吉竹伸介 （Yoshitake Shinsuke）","童話故事書",R.drawable.b11,true));
        data.add(new bookList("脫不下來啊！","吉竹伸介 （Yoshitake Shinsuke）","童話故事書",R.drawable.b12,true));
        data.add(new bookList("男孩、鼴鼠、狐狸與馬","查理‧麥克斯（Charlie Mackesy）","童話故事書",R.drawable.b13,true));
        //data.add(new bookList("我要找回我的帽子","雍．卡拉森（Jon Klassen）","童話故事書",R.drawable.b14,true));
        //data.add(new bookList("帶書去旅行的熊","法蘭西斯．托斯得文（Frances Tosdevin）","童話故事書",R.drawable.b15,true));

        data.add(new bookList("小王子","安東尼．聖修伯里（Antoine de Saint-Exupéry）","世界經典文學", R.drawable.c1,true));
        data.add(new bookList("柳林風聲","肯尼斯．葛拉罕  Kenneth Grahame","世界經典文學", R.drawable.c2,true));
        data.add(new bookList("木偶奇遇記","卡洛‧科洛迪","世界經典文學", R.drawable.c3,true));
        data.add(new bookList("野性的呼喚","傑克．倫敦（Jack London）","世界經典文學", R.drawable.c4,true));
        data.add(new bookList("動物農場","喬治．奧威爾（George Orwell）","世界經典文學", R.drawable.c5,true));
        data.add(new bookList("給父親的一封信","法蘭茲．卡夫卡（Franz Kafka）","世界經典文學", R.drawable.c6,true));
        data.add(new bookList("簡愛","夏綠蒂．白朗特（Charlotte Bronte）","世界經典文學", R.drawable.c7,true));
        data.add(new bookList("雙城記","狄更斯","世界經典文學", R.drawable.c8,true));
        data.add(new bookList("美麗的約定","亞蘭·傅尼葉（Alain Fournier）","世界經典文學", R.drawable.c9,true));
        data.add(new bookList("愛麗絲漫遊奇境","路易斯．卡洛 (Lewis Carroll)","世界經典文學",R.drawable.c10,true));
        data.add(new bookList("茶花女","小仲馬 (Alexandre Dumas Fils)","世界經典文學",R.drawable.c11,true));
        data.add(new bookList("傲慢與偏見","珍．奧斯汀（Jane Austen）","世界經典文學",R.drawable.c12,true));
        data.add(new bookList("大亨小傳","費茲傑羅（F. Scott Fitzgerald）","世界經典文學",R.drawable.c13,true));
        data.add(new bookList("一九八四","喬治．歐威爾（George Orwell）","世界經典文學",R.drawable.c14,true));
        //data.add(new bookList("白鯨記","赫曼．梅爾維爾(Herman Melville)","世界經典文學",R.drawable.c15,true));

        return data;
    }

    private List<bookList> getNewMenuItemList() {
        List<bookList> data = new ArrayList<bookList>();

        data.add(new bookList("你會坦然面對，每一場告別","角子","文學小說",R.drawable.a10,true));
        data.add(new bookList("米奇7號","愛德華．艾希頓（Edward Ashton）","文學小說",R.drawable.a11,true));
        data.add(new bookList("52赫茲的鯨魚們","町田苑香","文學小說",R.drawable.a12,true));
        data.add(new bookList("解鎖師","史蒂夫．漢密頓（Steve Hamilton）","文學小說",R.drawable.a13,true));
        data.add(new bookList("點燈人","艾瑪‧史東尼克斯（Emma Stonex）","文學小說",R.drawable.a14,true));
        data.add(new bookList("如此蒼白的心","哈維爾．馬利亞斯（Javier Marías）","文學小說",R.drawable.a15,true));

        data.add(new bookList("奇蹟麵包店","譚雅‧葛雷諾","童話故事書",R.drawable.b10,true));
        data.add(new bookList("太熱的話，脫外套就好啦！","吉竹伸介 （Yoshitake Shinsuke）","童話故事書",R.drawable.b11,true));
        data.add(new bookList("脫不下來啊！","吉竹伸介 （Yoshitake Shinsuke）","童話故事書",R.drawable.b12,true));
        data.add(new bookList("男孩、鼴鼠、狐狸與馬","查理‧麥克斯（Charlie Mackesy）","童話故事書",R.drawable.b13,true));
        data.add(new bookList("我要找回我的帽子","雍．卡拉森（Jon Klassen）","童話故事書",R.drawable.b14,true));

        data.add(new bookList("愛麗絲漫遊奇境","路易斯．卡洛 (Lewis Carroll)","世界經典文學",R.drawable.c10,true));
        data.add(new bookList("茶花女","小仲馬 (Alexandre Dumas Fils)","世界經典文學",R.drawable.c11,true));
        data.add(new bookList("傲慢與偏見","珍．奧斯汀（Jane Austen）","世界經典文學",R.drawable.c12,true));
        data.add(new bookList("大亨小傳","費茲傑羅（F. Scott Fitzgerald）","世界經典文學",R.drawable.c13,true));
        data.add(new bookList("一九八四","喬治．歐威爾（George Orwell）","世界經典文學",R.drawable.c14,true));
        data.add(new bookList("白鯨記","赫曼．梅爾維爾(Herman Melville)","世界經典文學",R.drawable.c15,true));


        return data;
    }



}