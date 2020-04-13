package hkucs.freeden.adapter;

import android.content.Context;
import android.graphics.Color;
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
import hkucs.freeden.model.dbmodels.BlockedUserRecord;
import hkucs.freeden.utils.DrawableUtils;

/**
 * Created by lshung on 25/11/2017.
 */

public class BlockedUserListAdapter extends BaseAdapter {

    private static final String TAG = BlockedUserListAdapter.class.getSimpleName();
    private Context mContext;
    private List<BlockedUserRecord> mBlockedUserList;
    private final DBMaster mdb;

    private HashSet<Integer> mBlockedUserIdSet;
    private LayoutInflater mInflater;

    public boolean isBlockedUser(int position) { return mBlockedUserIdSet.contains( ((BlockedUserRecord) getItem(position)).getId() ); }

    public void updateBlockedUserIdSet() { mBlockedUserIdSet = mdb.getAllBlockedUserIdsInHashSet(); }

    public void addBlockedUserRecordToBlockedUserSet(int i) { mBlockedUserIdSet.add((Integer) i); }
    public void removeBlockedUserRecordFromBlockedUserSet(int i) { mBlockedUserIdSet.remove((Integer) i); }

    public BlockedUserListAdapter(Context context, List<BlockedUserRecord> blocked_user_list) {
        this.mContext = context;
        this.mBlockedUserList = blocked_user_list;
        this.mdb = new DBMaster(mContext);
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mBlockedUserIdSet = mdb.getAllBlockedUserIdsInHashSet();
    }

    public void addListItemToAdapter(List<BlockedUserRecord> list) {
        mBlockedUserList.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() { return mBlockedUserList.size(); }

    @Override
    public Object getItem(int position) { return mBlockedUserList.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        BlockedUserViewHolder holder;

        BlockedUserRecord bur = (BlockedUserRecord) getItem(position);

        if(contentView==null) {
            holder = new BlockedUserViewHolder();
            contentView = mInflater.inflate(R.layout.blocked_user_row, null);

            holder.user_nickname_text = (TextView) contentView.findViewById(R.id.user_nickname_text);
            holder.user_id_text = (TextView) contentView.findViewById(R.id.user_id_text);
            holder.time_image = (ImageView) contentView.findViewById(R.id.time_image);
            holder.block_duration_text = (TextView) contentView.findViewById(R.id.block_duration_text);
            holder.block_button = (ImageView) contentView.findViewById(R.id.block_button);
            holder.block_text = (TextView) contentView.findViewById(R.id.block_text);

            contentView.setTag(holder);
        } else {
            holder = (BlockedUserViewHolder) contentView.getTag();
        }

        String block_timestamp;
        long diff = System.currentTimeMillis() / 1000 - bur.getBlockDate();
        if (TimeUnit.SECONDS.toMinutes(diff) <= 0) block_timestamp = "不久前";
        else if (TimeUnit.SECONDS.toHours(diff) == 0)
            block_timestamp = String.format("%dm", TimeUnit.SECONDS.toMinutes(diff));
        else if (TimeUnit.SECONDS.toDays(diff) == 0)
            block_timestamp = String.format("%dh", TimeUnit.SECONDS.toHours(diff));
        else block_timestamp = String.format("%dd", TimeUnit.SECONDS.toDays(diff));

        holder.user_nickname_text.setText(bur.getNickname());
        holder.user_nickname_text.setTextColor(Color.parseColor("#FFFFFF"));
        holder.user_id_text.setText(String.format("#%d", bur.getId()));
        holder.time_image.setImageDrawable(DrawableUtils.tintDrawable(mContext, R.drawable.ic_access_time_black_24dp, Color.parseColor("#808080")));
        holder.block_duration_text.setText(block_timestamp);

        if(mBlockedUserIdSet.contains(bur.getId())) {
            holder.block_button.setImageDrawable(DrawableUtils.tintDrawable(mContext, R.drawable.ic_block_black_24dp, Color.parseColor("#F44336")));
            holder.block_text.setText("已封鎖");
        } else {
            holder.block_button.setImageDrawable(DrawableUtils.tintDrawable(mContext, R.drawable.ic_block_black_24dp, Color.parseColor("#FFFFFF")));
            holder.block_text.setText("已解封");
        }

        return contentView;
    }
}
