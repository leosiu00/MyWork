package hkucs.freeden;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hkucs.freeden.adapter.PostThreadListAdapter;
import hkucs.freeden.database.DBMaster;
import hkucs.freeden.model.PostThread;
import hkucs.freeden.utils.DrawableUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FavouriteActivity extends AppCompatActivity {

    private static final String TITLE = "已儲存";
    private boolean deletion_mode = false;

    private DBMaster db;
    Toolbar toolbar;
    SharedPreferences pref;
    private static final String TAG = FavouriteActivity.class.getSimpleName();
    PopUpPreview popup;
    private Menu menu;

    ListView list_view;
    SwipeRefreshLayout swipe_refresh_view;
    PostThreadListAdapter adapter;
    List<PostThread> post_thread_list;
    protected OkHttpClient client = new OkHttpClient();
    String URL = "";
    String resStr = "";

    final ExecutorService service = Executors.newSingleThreadExecutor(); // okhttp needs thread to run
//    int update_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_post_listing);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(TITLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        db = new DBMaster(this);

        list_view = (ListView) findViewById(R.id.generic_post_listing_list_view);
        swipe_refresh_view = (SwipeRefreshLayout) findViewById(R.id.generic_post_listing_swipe_refresh_view);
        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        db.close();
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(deletion_mode) {
            switchDeletionMode();
            return;
        }
        db.close();
        super.onBackPressed();
    }

    protected  void startIntentToOpenPost(int thread_id, boolean modify_db_record) { startIntentToOpenPost(thread_id, 1, modify_db_record); }
    protected void startIntentToOpenPost(int thread_id, int page, boolean modify_db_record ) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://lihkg.com/thread/%d/page/%d", thread_id, page)));
        if(modify_db_record) {
            db.addHistoryPostThread(thread_id);
            db.unwatchLaterPostThread(thread_id);
            adapter.addHistoryRecordToHistorySet(thread_id);
            adapter.removeWatchLaterRecordFromWatchLaterSet(thread_id);
            adapter.notifyDataSetChanged();
        }
        startActivity(intent);
    }

    protected void refresh() {
//        post_thread_list.clear();
        FetchPosts();
        swipe_refresh_view.setRefreshing(false);
    }

    protected void init() {
        post_thread_list = new ArrayList<PostThread>();

        pref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
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
                if(deletion_mode) {
                    adapter.removeFavouriteRecordFromFavouriteSet(p.getId());
                    adapter.getView(i, view, adapterView);
                    db.unsavePostThread(p.getId());
                    adapter.notifyDataSetChanged();
                } else {
                    if(!pref.getBoolean("incognito_mode", false)) {
                        startIntentToOpenPost(p.getId(), true);
                        adapter.getView(i, view, adapterView); // draw the list item again
                        adapter.notifyDataSetChanged();
                    } else {
                        startIntentToOpenPost(p.getId(), false);
                    }
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
                popup = new PopUpPreview(FavouriteActivity.this, p, adapter.isWatchLaterRecord(i), adapter.isFavouriteRecord(i), adapter.isBlockedUser(i));
                popup.show(findViewById(R.id.generic_post_listing_list_view), 0, 0);

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

                return true;
            }
        });

        FetchPosts();
    }

    protected void drawMenuIcons() {
        drawMenuIcon(menu.findItem(R.id.deletion_mode), deletion_mode ? R.drawable.ic_delete_forever_black_24dp : R.drawable.ic_delete_black_24dp);
    }

    protected void drawMenuIcon(MenuItem item, int drawable_id) {
        item.setIcon( DrawableUtils.tintDrawable(this, drawable_id, Color.parseColor("#FFFFFF")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generic_delete_op, menu);
        // Set the color of the icon
        drawMenuIcons();
        return true;
    }

    protected void switchDeletionMode() {
        deletion_mode = !deletion_mode;
        drawMenuIcons();
        getSupportActionBar().setTitle( deletion_mode ? TITLE + "：刪除模式": TITLE );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.deletion_mode) {
            if(!deletion_mode) {
                switchDeletionMode();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("清除所有紀錄？")
                        .setIcon(R.drawable.ic_delete_forever_black_24dp)
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.updateFavouriteSet();
                                db.unsaveAllPostThreads();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter = new PostThreadListAdapter(FavouriteActivity.this, post_thread_list);
                                        list_view.setAdapter(adapter);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                                onBackPressed();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
            return true;
        } else if(id==R.id.refresh) {
//            update_count = 0;
            swipe_refresh_view.setRefreshing(true);
            refresh();
        }

        return super.onOptionsItemSelected(item);
    }



    public void FetchPosts() {
        Integer[] post_id_arr = db.getAllSavedPostThreadIdsInArray();
        if(post_id_arr.length==0) {
            Toast.makeText(this, "沒有任何紀錄", Toast.LENGTH_SHORT).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    post_thread_list.clear();
//                    if (update_count == 0) {
                    adapter = new PostThreadListAdapter(FavouriteActivity.this, post_thread_list);
                    list_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
//                    update_count++;
//                    }
                }
            });
            return;
        }
        String post_ids_str = "";
        for(int i=0; i<post_id_arr.length; i++) post_ids_str += (i==0 ? "" : ",") + String.valueOf(post_id_arr[i]);
        URL = "https://lihkg.com/api_v1_1/thread/?thread_ids=[" + post_ids_str + "]";
        Log.d(TAG, URL);

        service.submit(new Runnable() {
            @Override
            public void run() {

                Request request = new Request.Builder().url(URL).build();
                try {
                    final Response response = client.newCall(request).execute();
                    resStr = response.body().string();
                    Log.d(TAG, resStr);

                    try {
                        JSONObject reader = new JSONObject(resStr);

                        if( reader.getInt("success")==0 ) {
                            final String error_message = reader.getString("error_message");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FavouriteActivity.this
                                            , (error_message.equals("沒有相關文章") ? "冇得撈啦 :(" : error_message), Toast.LENGTH_SHORT
                                    ).show();
//                                    is_updating = false; // DO NOT RELEASE THE LOCK
                                }
                            });
                        } else {
                            final JSONObject json_response = reader.getJSONObject("response");

                            final JSONArray items = json_response.getJSONArray("items");
                            post_thread_list.clear();
//                            if (update_count == 0) {
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject c = items.getJSONObject(i);
                                post_thread_list.add(
                                        new PostThread(c)
                                );
                            }
//                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    if (update_count == 0) {
                                    adapter = new PostThreadListAdapter(FavouriteActivity.this, post_thread_list);
                                    list_view.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
//                                    update_count++;
//                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.d(TAG, "Error while trying to read JSON response from:\n" + URL);
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Error while trying to execute request from:\n" + URL);
                    e.printStackTrace();
                }
            }
        });
    }

}
