package hkucs.freeden;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import hkucs.freeden.model.PostThread;
import hkucs.freeden.utils.DrawableUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by lshung on 24/11/2017.
 */

public class PopUpPreview extends PopupWindow {
    private Context context;
    View popup_view;

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

    public PopUpPreview(Context context, PostThread pt, boolean is_w_lat, boolean is_fav, boolean is_b_usr) {
        super(context);
        this.context = context;
        popup_view = LayoutInflater.from(context).inflate(R.layout.popup_preview, null);
        setContentView(popup_view);

        URL = String.format("https://lihkg.com/api_v1_1/thread/%d/page/1", pt.getId());

        String last_reply_timestamp;
        long diff = System.currentTimeMillis() / 1000 - pt.getCreateTime();
        if( TimeUnit.SECONDS.toMinutes(diff)<=0 ) last_reply_timestamp = "不久前";
        else if( TimeUnit.SECONDS.toHours(diff)==0 ) last_reply_timestamp = String.format("%dm", TimeUnit.SECONDS.toMinutes(diff));
        else if( TimeUnit.SECONDS.toDays(diff) ==0 ) last_reply_timestamp = String.format("%dh", TimeUnit.SECONDS.toHours(diff));
        else last_reply_timestamp = String.format("%dd", TimeUnit.SECONDS.toDays(diff));

        title_text = (TextView) popup_view.findViewById(R.id.title_text);
        like_count_text = (TextView) popup_view.findViewById(R.id.like_count_text);
        dislike_count_text = (TextView) popup_view.findViewById(R.id.dislike_count_text);
        content_text = (TextView) popup_view.findViewById(R.id.content_text);
        like_image = (ImageView) popup_view.findViewById(R.id.like_image);
        dislike_image = (ImageView) popup_view.findViewById(R.id.dislike_image);
        comment_image = (ImageView) popup_view.findViewById(R.id.comment_image);
        save_button = (ImageButton) popup_view.findViewById(R.id.save_button);
        watch_later_button = (ImageButton) popup_view.findViewById(R.id.watch_later_button);
        block_user_button = (ImageButton) popup_view.findViewById(R.id.block_user_button);
        last_page_button = (ImageButton) popup_view.findViewById(R.id.last_page_button);
        incognito_button = (ImageButton) popup_view.findViewById(R.id.incognito_button);

        category_name_text = (TextView) popup_view.findViewById(R.id.category_name_text); //v2
        person_image = (ImageView) popup_view.findViewById(R.id.person_image); //v2
        user_nickname_text = (TextView) popup_view.findViewById(R.id.user_nickname_text); //v2
        time_text = (TextView) popup_view.findViewById(R.id.time_text); //v3

        title_text.setText(pt.getTitle());
        title_text.setTextColor(Color.parseColor("#FFFFFF"));
        like_count_text.setText(String.valueOf(pt.getLikeCount()));
        dislike_count_text.setText(String.valueOf(pt.getDislikeCount()));
        content_text.setText("載入中...");

        category_name_text.setText(pt.getCategory().getName()); //v2
        person_image.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_person_black_24dp, Color.parseColor("#808080"))); //v2
        user_nickname_text.setText(pt.getUserNickname()); //v2
        user_nickname_text.setTextColor(pt.getUser().getColor()); //v2
        time_text.setText(last_reply_timestamp); //v3

        like_image.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_thumb_up_black_24dp, Color.parseColor("#808080")));
        dislike_image.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_thumb_down_black_24dp, Color.parseColor("#808080")));
        comment_image.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_comment_black_24dp, Color.parseColor("#808080")));
        save_button.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_bookmark_black_24dp, !is_fav ? Color.parseColor("#FFFFFF") : Color.parseColor("#03A9F4") ));
        watch_later_button.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_watch_later_black_24dp, !is_w_lat ? Color.parseColor("#FFFFFF") : Color.parseColor("#8BC34A") ));
        block_user_button.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_block_black_24dp, !is_b_usr ? Color.parseColor("#FFFFFF") : Color.parseColor("#F44336") ));
        last_page_button.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_last_page_black_24dp, Color.parseColor("#FFFFFF")));
        incognito_button.setImageDrawable(DrawableUtils.tintDrawable(context, R.drawable.ic_incognito, Color.parseColor("#FFFFFF")));

        DisplayMetrics dm = new DisplayMetrics();
        ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setWidth((int) (dm.widthPixels * 0.8));

        // close popup preview when touch outside of window
        setOutsideTouchable(true);
        setFocusable(true);

        // remove default black background
//        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FetchContent();
    }

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
                        final String content = c.getString("msg").replaceAll("<img src=\"/assets/.+?>", "[表情符號]").replaceAll("<img.+?>", "[圖片]");
                        final Spanned spanned_content = Html.fromHtml(content);
                        ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                content_text.setText(spanned_content);
                                content_text.setTextColor(Color.parseColor("#FFFFFF"));
//                                Log.d(TAG, content);
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

    public void show(View anchor, int x, int y) {
        showAtLocation(anchor, Gravity.CENTER, x, y);
    }
}
