package hkucs.freeden;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import hkucs.freeden.database.DBMaster;
import hkucs.freeden.model.PostThread;
import hkucs.freeden.utils.DrawableUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Preview extends AppCompatActivity {

    private static final String TAG = Preview.class.getSimpleName();
    final DBMaster db = new DBMaster(this);

    TextView title_text;
    TextView like_count_text;
    TextView dislike_count_text;
    TextView content_text;

    ImageView like_image;
    ImageView dislike_image;
    ImageView comment_image;
    ImageButton save_button;
    ImageButton watch_later_button;
    ImageButton block_user_button;
    ImageButton last_page_button;
    ImageButton incognito_button;

    TextView category_name_text; //v2
    ImageView person_image; //v2
    TextView user_nickname_text; //v2
    TextView time_text; //v3

    String resStr = "";
    String URL = "";
    final private OkHttpClient client = new OkHttpClient();
    final ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_v3);

        // coded by Leo
        // set popup window dimensions
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels; // int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), WindowManager.LayoutParams.WRAP_CONTENT);
        // get post thread detail
        Intent intent = getIntent();
        PostThread pt = (PostThread) intent.getSerializableExtra("post_thread");
        URL = String.format("https://lihkg.com/api_v1_1/thread/%d/page/1", pt.getId());

        String last_reply_timestamp;
        long diff = System.currentTimeMillis() / 1000 - pt.getCreateTime();
        if( TimeUnit.SECONDS.toMinutes(diff)<=0 ) last_reply_timestamp = "不久前";
        else if( TimeUnit.SECONDS.toHours(diff)==0 ) last_reply_timestamp = String.format("%dm", TimeUnit.SECONDS.toMinutes(diff));
        else if( TimeUnit.SECONDS.toDays(diff) ==0 ) last_reply_timestamp = String.format("%dh", TimeUnit.SECONDS.toHours(diff));
        else last_reply_timestamp = String.format("%dd", TimeUnit.SECONDS.toDays(diff));

        title_text = (TextView) findViewById(R.id.title_text);
        like_count_text = (TextView) findViewById(R.id.like_count_text);
        dislike_count_text = (TextView) findViewById(R.id.dislike_count_text);
        content_text = (TextView) findViewById(R.id.content_text);
        like_image = (ImageView) findViewById(R.id.like_image);
        dislike_image = (ImageView) findViewById(R.id.dislike_image);
        comment_image = (ImageView) findViewById(R.id.comment_image);
        save_button = (ImageButton) findViewById(R.id.save_button);
        watch_later_button = (ImageButton) findViewById(R.id.watch_later_button);
        block_user_button = (ImageButton) findViewById(R.id.block_user_button);
        last_page_button = (ImageButton) findViewById(R.id.last_page_button);
        incognito_button = (ImageButton) findViewById(R.id.incognito_button);

        category_name_text = (TextView) findViewById(R.id.category_name_text); //v2
        person_image = (ImageView) findViewById(R.id.person_image); //v2
        user_nickname_text = (TextView) findViewById(R.id.user_nickname_text); //v2
        time_text = (TextView) findViewById(R.id.time_text); //v3

        title_text.setText(pt.getTitle());
        title_text.setTextColor(Color.parseColor("#FFFFFF"));
        like_count_text.setText(String.valueOf(pt.getLikeCount()));
        dislike_count_text.setText(String.valueOf(pt.getDislikeCount()));
        content_text.setText("載入中...");

        category_name_text.setText(pt.getCategory().getName()); //v2
        person_image.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_person_black_24dp, Color.parseColor("#808080"))); //v2
        user_nickname_text.setText(pt.getUserNickname()); //v2
        user_nickname_text.setTextColor(pt.getUser().getColor()); //v2
        time_text.setText(last_reply_timestamp); //v3

        like_image.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_thumb_up_black_24dp, Color.parseColor("#808080")));
        dislike_image.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_thumb_down_black_24dp, Color.parseColor("#808080")));
        comment_image.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_comment_black_24dp, Color.parseColor("#808080")));
        save_button.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_bookmark_black_24dp, Color.parseColor("#FFFFFF")));
        watch_later_button.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_watch_later_black_24dp, Color.parseColor("#FFFFFF")));
        block_user_button.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_block_black_24dp, Color.parseColor("#FFFFFF")));
        last_page_button.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_last_page_black_24dp, Color.parseColor("#FFFFFF")));
        incognito_button.setImageDrawable(DrawableUtils.tintDrawable(this, R.drawable.ic_incognito, Color.parseColor("#FFFFFF")));

        FetchContent();

//        findViewById(R.id.preview_view).setOnTouchListener(this);

    }

//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if(motionEvent.getAction()==MotionEvent.ACTION_UP) Log.d(TAG, "action_up");
//        if(motionEvent.getAction()==MotionEvent.ACTION_POINTER_UP) Log.d(TAG, "action_pointer_up");
//        return true;
//    }

    public void FetchContent() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, URL);
                Request request = new Request.Builder().url(URL).build();
                try {
                    final Response response = client.newCall(request).execute();
                    resStr = response.body().string();
                    Log.d(TAG, resStr);
                    try {
                        JSONObject reader = new JSONObject(resStr);
                        final JSONObject json_response = reader.getJSONObject("response");
                        final JSONObject c = json_response.getJSONArray("item_data").getJSONObject(0);
                        final String content = c.getString("msg").replaceAll("<img.+?>", "[圖片]");
                        final Spanned spanned_content = Html.fromHtml(content);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content_text.setText(spanned_content);
                                content_text.setTextColor(Color.parseColor("#FFFFFF"));
                                Log.d(TAG, content);
                            }
                        });
                    } catch (JSONException e) {
                        Log.d(TAG, String.format("Error while trying to parse JSON from %s", resStr));
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    Log.d(TAG, String.format("Error while trying to fetch content from %s", URL));
                    e.printStackTrace();
                }
            }
        });
    }
}
