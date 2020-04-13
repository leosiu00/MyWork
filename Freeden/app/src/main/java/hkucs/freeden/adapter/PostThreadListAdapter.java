package hkucs.freeden.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hkucs.freeden.R;
import hkucs.freeden.database.DBMaster;
import hkucs.freeden.model.PostThread;
import hkucs.freeden.utils.DrawableUtils;

/**
 * Created by lshung on 22/11/2017.
 */

public class PostThreadListAdapter extends BaseAdapter {

    private static final String TAG = PostThreadListAdapter.class.getSimpleName();
    private Context mContext;
    private List<PostThread> mPostThreadList;
    private final DBMaster mdb;

    private HashSet<Integer> mHistorySet;
    private HashSet<Integer> mWatchLaterSet;
    private HashSet<Integer> mFavouriteSet;
    private HashSet<Integer> mBlockedUserIdSet;
    private LayoutInflater mInflater;

    public boolean isHistoryRecord(int position) { return mHistorySet.contains( ((PostThread) getItem(position)).getId() ); }
    public boolean isWatchLaterRecord(int position) { return mWatchLaterSet.contains( ((PostThread) getItem(position)).getId() ); }
    public boolean isFavouriteRecord(int position) { return mFavouriteSet.contains( ((PostThread) getItem(position)).getId() ); }
    public boolean isBlockedUser(int position) { return mBlockedUserIdSet.contains( ((PostThread) getItem(position)).getUser().getId() ); }

    public void updateHistorySet() {
        mHistorySet = mdb.getAllHistoryPostThreadIdsInHashSet();
    }

    public void updateWatchLaterSet() { mWatchLaterSet = mdb.getAllWatchLaterPostThreadIdsInHashSet(); }

    public void updateFavouriteSet() { mFavouriteSet = mdb.getAllSavedPostThreadIdsInHashSet(); }

    public void updateBlockedUserIdSet() { mBlockedUserIdSet = mdb.getAllBlockedUserIdsInHashSet(); }

    public void addHistoryRecordToHistorySet(int i) {
        mHistorySet.add((Integer) i);
    }
    public void removeHistoryRecordFromHistorySet(int i) { mHistorySet.remove((Integer) i); }

    public void removeWatchLaterRecordFromWatchLaterSet(int i) { mWatchLaterSet.remove((Integer) i); }

    public void removeFavouriteRecordFromFavouriteSet(int i) { mFavouriteSet.remove((Integer) i); }

    public PostThreadListAdapter(Context context, List<PostThread> mPostThreadList) {
        this.mContext = context;
        this.mPostThreadList = mPostThreadList;
        this.mdb = new DBMaster(mContext);
        this.mInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        this.mHistorySet = mdb.getAllHistoryPostThreadIdsInHashSet();
        this.mWatchLaterSet = mdb.getAllWatchLaterPostThreadIdsInHashSet();
        this.mFavouriteSet = mdb.getAllSavedPostThreadIdsInHashSet();
        this.mBlockedUserIdSet = mdb.getAllBlockedUserIdsInHashSet();
    }

    public void addListItemToAdapter(List<PostThread> list) {
        mPostThreadList.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() { return mPostThreadList.size(); }

    @Override
    public Object getItem(int position) { return mPostThreadList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.d(TAG, "blocked user cnt: " + String.valueOf(mBlockedUserIdSet.size()) + " cv null " + String.valueOf(convertView==null));
        PostThreadViewHolder holder;

        PostThread pt = (PostThread) getItem(position);

        if(convertView==null || convertView.getTag()==null) {
            holder = new PostThreadViewHolder();
            convertView = mInflater.inflate(R.layout.post_thread_row, null);

            holder.title_text = (TextView) convertView.findViewById(R.id.title_text);
            holder.vote_count_text = (TextView) convertView.findViewById(R.id.vote_count_text);
            holder.time_text = (TextView) convertView.findViewById(R.id.time_text);
            holder.category_name_text = (TextView) convertView.findViewById(R.id.category_name_text);
            holder.person_image = (ImageView) convertView.findViewById(R.id.person_image);
            holder.user_nickname_text = (TextView) convertView.findViewById(R.id.user_nickname_text);
            holder.reply_count_text = (TextView) convertView.findViewById(R.id.reply_count_text);
            holder.comment_image = (ImageView) convertView.findViewById(R.id.comment_image);
            holder.has_history_image = (ImageView) convertView.findViewById(R.id.history_or_watch_later_image);
            holder.is_hot_image = (ImageView) convertView.findViewById(R.id.is_hot_image);
            holder.is_bookmarked_image = (ImageView) convertView.findViewById(R.id.is_bookmarked_image);

            convertView.setTag(holder);
        } else {
            holder = (PostThreadViewHolder) convertView.getTag();
        }
//        Log.d(TAG, String.format("tid:%d\tuid:%d\tc:%b", pt.getId(), pt.getUser().getId(), mBlockedUserIdSet.contains(pt.getUser().getId())));

        if(mBlockedUserIdSet.contains(pt.getUser().getId())) {
//            holder.time_text.setVisibility(View.GONE);
//            holder.category_name_text.setVisibility(View.GONE);
//            holder.person_image.setVisibility(View.GONE);
//            holder.user_nickname_text.setVisibility(View.GONE);
//            holder.reply_count_text.setVisibility(View.GONE);
//            holder.comment_image.setVisibility(View.GONE);
//            holder.has_history_image.setVisibility(View.GONE);
//            holder.is_hot_image.setVisibility(View.GONE);
//            holder.is_bookmarked_image.setVisibility(View.GONE);
//
//            holder.title_text.setText(String.format("(Show blocked user - %s)", pt.getUserNickname()));
//            holder.title_text.setTextColor(Color.parseColor("#AAAAAA"));
//            holder.vote_count_text.setText(String.valueOf(pt.getLikeCount() - pt.getDislikeCount()));
//            holder.vote_count_text.setTextColor(pt.getVoteColor());
            View view2 = mInflater.inflate(R.layout.blocked_user_post_thread_row, null);
            TextView bu_title_text = (TextView) view2.findViewById(R.id.bu_title_text);
            TextView bu_vote_count_text = (TextView) view2.findViewById(R.id.bu_vote_count_text);
            bu_title_text.setText(String.format("(Show blocked user - %s)", pt.getUserNickname()));
            bu_title_text.setTextColor(Color.parseColor("#AAAAAA"));
            bu_vote_count_text.setText(String.valueOf(pt.getLikeCount() - pt.getDislikeCount()));
            bu_vote_count_text.setTextColor(pt.getVoteColor());
            return view2;
        } else {
//            holder.time_text.setVisibility(View.VISIBLE);
//            holder.category_name_text.setVisibility(View.VISIBLE);
//            holder.person_image.setVisibility(View.VISIBLE);
//            holder.user_nickname_text.setVisibility(View.VISIBLE);
//            holder.reply_count_text.setVisibility(View.VISIBLE);
//            holder.comment_image.setVisibility(View.VISIBLE);
//            holder.has_history_image.setVisibility(View.VISIBLE);
//            holder.is_hot_image.setVisibility(View.VISIBLE);
//            holder.is_bookmarked_image.setVisibility(View.VISIBLE);

            String last_reply_timestamp;
            long diff = System.currentTimeMillis() / 1000 - pt.getLastReplyTime();
            if (TimeUnit.SECONDS.toMinutes(diff) <= 0) last_reply_timestamp = "不久前";
            else if (TimeUnit.SECONDS.toHours(diff) == 0)
                last_reply_timestamp = String.format("%dm", TimeUnit.SECONDS.toMinutes(diff));
            else if (TimeUnit.SECONDS.toDays(diff) == 0)
                last_reply_timestamp = String.format("%dh", TimeUnit.SECONDS.toHours(diff));
            else last_reply_timestamp = String.format("%dd", TimeUnit.SECONDS.toDays(diff));
            //        Log.d(TAG, last_reply_timestamp);

            holder.title_text.setText(pt.getTitle());
            if (!mBlockedUserIdSet.contains(pt.getUser().getId()))
                holder.title_text.setTextColor(Color.parseColor("#FFFFFF"));
            holder.vote_count_text.setText(String.valueOf(pt.getLikeCount() - pt.getDislikeCount()));
            holder.vote_count_text.setTextColor(pt.getVoteColor());
            holder.time_text.setText(last_reply_timestamp);
            holder.category_name_text.setText(String.valueOf(pt.getCategory().getName()));
            holder.person_image.setImageDrawable(DrawableUtils.tintDrawable(mContext, R.drawable.ic_person_black_24dp, Color.parseColor("#808080")));
            holder.user_nickname_text.setText(pt.getUserNickname());
            holder.user_nickname_text.setTextColor(pt.getUser().getColor());
            holder.reply_count_text.setText(String.valueOf( pt.getNoOfReply()<=1 ? 0 : pt.getNoOfReply() - 1 ));
            holder.comment_image.setImageDrawable(DrawableUtils.tintDrawable(mContext, R.drawable.ic_comment_black_24dp, Color.parseColor("#808080")));

            holder.has_history_image.setImageDrawable(
                    mWatchLaterSet.contains(pt.getId()) ? DrawableUtils.tintDrawable(mContext, R.drawable.ic_watch_later_black_24dp, Color.parseColor("#808080")) :
                            mHistorySet.contains(pt.getId()) ? DrawableUtils.tintDrawable(mContext, R.drawable.ic_history_black_24dp, Color.parseColor("#808080")) : null
            );
            holder.is_hot_image.setImageDrawable(pt.getIsHot() ? DrawableUtils.tintDrawable(mContext, R.drawable.ic_fire, Color.parseColor("#F44336")) : null);
//            holder.is_bookmarked_image.setImageDrawable( pt.getIsBookmarked() ? DrawableUtils.tintDrawable(mContext, R.drawable.ic_bookmark_black_24dp, Color.parseColor("#03A9F4")) : null );
            holder.is_bookmarked_image.setImageDrawable(mFavouriteSet.contains(pt.getId()) ? DrawableUtils.tintDrawable(mContext, R.drawable.ic_bookmark_black_24dp, Color.parseColor("#03A9F4")) : null);
        }
        return convertView;
    }
}
