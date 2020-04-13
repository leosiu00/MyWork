package hkucs.freeden;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hkucs.freeden.adapter.PostThreadListAdapter;
import hkucs.freeden.database.DBMaster;
import hkucs.freeden.model.PostThread;
import hkucs.freeden.utils.DrawableUtils;
import hkucs.freeden.utils.DrawerMaster;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public DBMaster db; // access databases
    DrawerMaster drawer; // store drawers
    Toolbar toolbar;
    SharedPreferences pref;
    private static final String TAG = MainActivity.class.getSimpleName();
    PopUpPreview popup;

    ListView list_view;
    SwipeRefreshLayout swipe_refresh_view;
    PostThreadListAdapter adapter;
    List<PostThread> post_thread_list;
    int current_subforum;
    protected OkHttpClient client = new OkHttpClient();
    String URL = "";
    String resStr = "";
    private int PAGE = 1;
    final ExecutorService service = Executors.newSingleThreadExecutor(); // okhttp needs thread to run
    int update_count = 0;
    static boolean is_updating;
    static boolean die_connection;
//    int count = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        db = new DBMaster(this); // make database handlers
        drawer = new DrawerMaster(this); // make drawer

        list_view = (ListView) findViewById(R.id.main_list_view);
        swipe_refresh_view = (SwipeRefreshLayout) findViewById(R.id.main_swipe_refresh_view);
        init();
    }

    protected  void startIntentToOpenPost(int thread_id, boolean modify_db_record) { startIntentToOpenPost(thread_id, 1, modify_db_record); }
    protected void startIntentToOpenPost(int thread_id, int page, boolean modify_db_record ) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://lihkg.com/thread/%d/page/%d", thread_id, page)));
        if(modify_db_record) {
            db.addHistoryPostThread(thread_id);
            db.unwatchLaterPostThread(thread_id);
            adapter.addHistoryRecordToHistorySet(thread_id);
            adapter.removeWatchLaterRecordFromWatchLaterSet(thread_id);
        }
        startActivity(intent);
    }

    protected void refresh() {
        FetchPosts(pref.getInt("default_subforum", 1), true);
        swipe_refresh_view.setRefreshing(false);
    }

    protected void init() {
        post_thread_list = new ArrayList<PostThread>();
        is_updating = false;
        die_connection = false;

        // set default subforum
        pref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        current_subforum = pref.getInt("default_subforum", 1);
//        Toast.makeText(this, String.valueOf(current_subforum), Toast.LENGTH_SHORT).show(); //debug
        drawer.right().setSelection(current_subforum);

//        FetchPosts( current_subforum ); // fetching done on setSelection()

        swipe_refresh_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PostThread p = (PostThread) adapterView.getItemAtPosition(i);
//                Toast.makeText(view.getContext(), String.format("https://lihkg.com/thread/%d/page/1", p.getId(), Toast.LENGTH_SHORT).show();
                if(!pref.getBoolean("incognito_mode", false)) {
                    startIntentToOpenPost(p.getId(), true);
                    adapter.getView(i, view, adapterView); // draw the list item again
                } else {
                    startIntentToOpenPost(p.getId(), false);
                }
            }
        });
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int i, long l) {
//                Toast.makeText(view.getContext(), String.format("https://lihkg.com/api_v1_1/thread/%d/page/1", ((PostThread) adapterView.getItemAtPosition(i)).getId()), Toast.LENGTH_SHORT).show();
                popup = null;
                final PostThread p = (PostThread) adapterView.getItemAtPosition(i);
                // use PopUpWindow class
                popup = new PopUpPreview(MainActivity.this, p, adapter.isWatchLaterRecord(i), adapter.isFavouriteRecord(i), adapter.isBlockedUser(i));
                popup.show(findViewById(R.id.main_list_view), 0, 0);

                popup.popup_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startIntentToOpenPost(p.getId(), !pref.getBoolean("incognito_mode", false));
                        popup.dismiss();
                    }
                });
                popup.save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.saveOrUnsavePostThread(p.getId());
                        ((PostThreadListAdapter) adapterView.getAdapter()).updateFavouriteSet();
                        popup.dismiss();
                    }
                });
                popup.watch_later_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.watchLaterOrUnwatchLaterPostThread(p.getId());
                        ((PostThreadListAdapter) adapterView.getAdapter()).updateWatchLaterSet();
                        popup.dismiss();
                    }
                });
                popup.block_user_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.blockOrUnblockUser(p.getUser());
                        ((PostThreadListAdapter) adapterView.getAdapter()).updateBlockedUserIdSet();
                        popup.dismiss();
                    }
                });
                popup.last_page_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startIntentToOpenPost(p.getId(), p.getTotalPage(), !pref.getBoolean("incognito_mode", false));
                        popup.dismiss();
                    }
                });
                popup.incognito_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startIntentToOpenPost(p.getId(), false);
                        popup.dismiss();
                    }
                });
                popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        popup = null;
                        adapterView.getAdapter().getView(i, view, adapterView); // draw the list item again
                        adapter.notifyDataSetChanged();
                    }
                });

                // Coded by Leo // deprecated
//                Intent popup_intent = new Intent(getApplicationContext(), Preview.class);
////                popup_intent.putExtra("URL", String.format("https://lihkg.com/api_v1_1/thread/%d/page/1", ((PostThread) adapterView.getItemAtPosition(i)).getId()));
//                popup_intent.putExtra("post_thread", ((PostThread) adapterView.getItemAtPosition(i)) );
//                startActivity(popup_intent);

                return true;
            }
        });
//        list_view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(popup!=null && motionEvent.getAction()==MotionEvent.ACTION_UP) popup.dismiss();
//                return false; // do not return true: pass other events to other event listeners
//            }
//        });
        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                Log.d(TAG, String.format("%d m:%d l:%d", absListView.getLastVisiblePosition(), absListView.getCount(), list_view.getCount()));
//                if(absListView.getLastVisiblePosition()==count - 1 && list_view.getCount()>=30 ) {
                if(!is_updating && absListView.getLastVisiblePosition()==absListView.getCount() - 1 && absListView.getCount()>=1) {
                    is_updating = true;
                    PAGE += 1;
                    FetchPosts(current_subforum);
//                    Toast.makeText(absListView.getContext(), "Page "+String.valueOf(PAGE), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // 轉台 method here
    // 轉台 method here
    // 轉台 method here
    // 轉台 method here
    // 轉台 method here
    // 轉台 method here
    // 轉台 method here
    // 轉台 method here
    // 轉台 method here
    // if this java file name is changed, need to update the casting in DrawerMaster.java
    // if this java file name is changed, need to update the casting in DrawerMaster.java
    // if this java file name is changed, need to update the casting in DrawerMaster.java
    // if this java file name is changed, need to update the casting in DrawerMaster.java
    // if this java file name is changed, need to update the casting in DrawerMaster.java
    // if this java file name is changed, need to update the casting in DrawerMaster.java
    // if this java file name is changed, need to update the casting in DrawerMaster.java
    public void changeSubforum(int category_id ) {
//        Toast.makeText(this, String.format("轉台: cat_id=%d", category_id), Toast.LENGTH_SHORT).show();
        FetchPosts(category_id); // what is this crap
    }

    @Override
    public void onBackPressed() { // close drawer on back button press
        if( drawer.left().isDrawerOpen() ) drawer.left().closeDrawer();
        else if( drawer.right().isDrawerOpen() ) drawer.right().closeDrawer();
        else super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Set the color of the icon
        if(pref.getBoolean("incognito_mode", false)) menu.findItem(R.id.incognito_mode).setIcon( DrawableUtils.tintDrawable(this, R.drawable.ic_incognito, Color.parseColor("#03A9F4")));
        else menu.findItem(R.id.incognito_mode).setIcon( DrawableUtils.tintDrawable(this, R.drawable.ic_incognito, Color.parseColor("#FFFFFF")));
        menu.findItem(R.id.change_channel).setIcon( DrawableUtils.tintDrawable(this, R.drawable.ic_remote, Color.parseColor("#FFFFFF")));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.incognito_mode) {
            boolean in_mode = !pref.getBoolean("incognito_mode", false);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("incognito_mode", in_mode);
            editor.commit();
            Toast.makeText(this, String.format("無痕模式： %s", in_mode ? "開啟" : "關閉"), Toast.LENGTH_SHORT).show();
            if(in_mode) item.setIcon( DrawableUtils.tintDrawable(this, R.drawable.ic_incognito, Color.parseColor("#03A9F4")) );
            else item.setIcon( DrawableUtils.tintDrawable(this, R.drawable.ic_incognito, Color.parseColor("#FFFFFF")) );
            return true;
        } else if(id==R.id.change_channel) {
            drawer.right().openDrawer();
        } else if(id==R.id.refresh) {
            swipe_refresh_view.setRefreshing(true);
            refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    public void FetchPosts(int cat_id, boolean force_refresh) {
        if(force_refresh) {
            current_subforum = cat_id;
            PAGE=1;
            update_count=0;
            post_thread_list.clear();
//            count=30;
            // update the default_subforum for next time when application is launched
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("default_subforum", cat_id);
            editor.commit();
//            Toast.makeText(MainActivity.this, "成功轉台", Toast.LENGTH_SHORT).show();
        }
        FetchPosts(cat_id);
    }

    public void FetchPosts(int cat_id){
        if(cat_id!= current_subforum) {
            current_subforum =cat_id;
            PAGE=1;
            update_count=0;
            post_thread_list.clear();
//            count=30;
            // update the default_subforum for next time when application is launched
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("default_subforum", cat_id);
            editor.commit();
//            Toast.makeText(MainActivity.this, "成功轉台", Toast.LENGTH_SHORT).show();
        }
        String cat_id_str;
        switch(cat_id) {
            case 1: cat_id_str = "latest?"; break;
            case 2: cat_id_str = "hot?"; break;
            case 3: cat_id_str = "news?"; break;
            default: cat_id_str = String.format("category?cat_id=%d&", cat_id);
        }
        URL = "https://lihkg.com/api_v1_1/thread/" + cat_id_str + "page="+PAGE;
        Log.d(TAG, URL);

//        final Request request = new Request.Builder().url("https://lihkg.com/api_v1/system/property").build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "Failure http request");
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Log.d(TAG, response.body().string());
//                        } catch(IOException ioe) {
//                            Log.d(TAG, "Error during get body");
//                        }
//                    }
//                });
//            }
//        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(URL).build();
                try {
                    final Response response = client.newCall(request).execute();
                    resStr = response.body().string();
                    Log.d(TAG, resStr);
                    final ArrayList<PostThread> incoming_post_thread_list = new ArrayList<PostThread>();

                    try {
                        die_connection = false;
                        JSONObject reader = new JSONObject(resStr);

                        if( reader.getInt("success")==0 ) {
                            final String error_message = reader.getString("error_message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this
                                            , (error_message.equals("沒有相關文章") ? "冇得撈啦 :(" : error_message), Toast.LENGTH_SHORT
                                    ).show();
//                                    is_updating = false; // DO NOT RELEASE THE LOCK
                                }
                            });
                        } else {
                            final JSONObject json_response = reader.getJSONObject("response");
                            final String title = json_response.getJSONObject("category").getString("name");
                            toolbar.post(new Runnable() {
                                @Override
                                public void run() {
                                    MainActivity.super.setTitle(title);
                                }
                            });

                            final JSONArray items = json_response.getJSONArray("items");
                            if (update_count == 0) {
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject c = items.getJSONObject(i);
                                    post_thread_list.add(
                                            new PostThread(c)
                                    );
                                }
                            } else {
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject c = items.getJSONObject(i);
                                    incoming_post_thread_list.add(
                                            new PostThread(c)
                                    );
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (update_count == 0) {
                                        adapter = new PostThreadListAdapter(MainActivity.this, post_thread_list);
                                        list_view.setAdapter(adapter);
                                        update_count++;
                                    } else {
                                        adapter.addListItemToAdapter(incoming_post_thread_list);
                                    }
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this, String.format("第%d頁", PAGE), Toast.LENGTH_SHORT).show();
                                    MainActivity.is_updating = false;
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "Error while trying to read JSON response from:\n" + URL);
                        e.printStackTrace();
                    }
                } catch (ConnectException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PAGE=1;
                            update_count=0;
//                            post_thread_list = new ArrayList<PostThread>();
                            if(adapter==null) {
                                adapter = new PostThreadListAdapter(MainActivity.this, post_thread_list);
                                list_view.setAdapter(adapter);
                            }
                            adapter.notifyDataSetChanged();
                            if(!die_connection) Toast.makeText(MainActivity.this, "線已斷", Toast.LENGTH_SHORT).show();
                            MainActivity.die_connection = true;
                            MainActivity.is_updating = false;
                        }
                    });
                    Log.d(TAG, "ConnectException: cannot fetch records");
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PAGE=1;
                            update_count=0;
//                            post_thread_list = new ArrayList<PostThread>();
                            if(adapter==null) {
                                adapter = new PostThreadListAdapter(MainActivity.this, post_thread_list);
                                list_view.setAdapter(adapter);
                            }
                            adapter.notifyDataSetChanged();
                            if(!die_connection) Toast.makeText(MainActivity.this, "線已斷", Toast.LENGTH_SHORT).show();
                            MainActivity.die_connection = true;
                            MainActivity.is_updating = false;
                        }
                    });
                    Log.d(TAG, "UnknownHostException: cannot fetch records");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d(TAG, "Error while trying to execute request from:\n" + URL);
                    e.printStackTrace();
                }
            }
        });
    }
}
