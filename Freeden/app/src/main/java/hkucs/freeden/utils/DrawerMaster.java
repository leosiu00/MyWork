package hkucs.freeden.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import hkucs.freeden.AboutActivity;
import hkucs.freeden.BlockedUserActivity;
import hkucs.freeden.FavouriteActivity;
import hkucs.freeden.HistoryActivity;
import hkucs.freeden.MainActivity;
import hkucs.freeden.R;
import hkucs.freeden.WatchLaterActivity;

/**
 * Created by lshung on 11/11/2017.
 */

public class DrawerMaster {
    private AccountHeader head;
    private Drawer left;
    private Drawer right;
    private Activity a;

    public DrawerMaster( final Activity activity ) {
        this.a = activity;

        head = constructHeader( a );
        left = constructLeftDrawer( a, head );
        right = constructRightDrawer( a, left() );
    }

    public AccountHeader head() { return head; }
    public Drawer left() { return left; }
    public Drawer right() { return right; }



    protected static Drawer constructRightDrawer( final Activity a, Drawer result ) {
        Toolbar toolbar = (Toolbar) a.findViewById( R.id.toolbar );
        SectionDrawerItem sdi_1 = new SectionDrawerItem().withName( R.string.category_section_name_1 );
        SectionDrawerItem sdi_2 = new SectionDrawerItem().withName( R.string.category_section_name_2 );
        SectionDrawerItem sdi_3 = new SectionDrawerItem().withName( R.string.category_section_name_3 );
        SectionDrawerItem sdi_4 = new SectionDrawerItem().withName( R.string.category_section_name_4 );
        SectionDrawerItem sdi_5 = new SectionDrawerItem().withName( R.string.category_section_name_5 );
        SectionDrawerItem sdi_6 = new SectionDrawerItem().withName( R.string.category_section_name_6 );

        PrimaryDrawerItem di_1 = new PrimaryDrawerItem().withIdentifier(1).withName( R.string.category_name_1 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_message, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_999 = new PrimaryDrawerItem().withIdentifier(999).withName( R.string.category_name_999 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_android_black_24dp, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_2 = new PrimaryDrawerItem().withIdentifier(2).withName( R.string.category_name_2 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_fire, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_3 = new PrimaryDrawerItem().withIdentifier(3).withName( R.string.category_name_3 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_newpost, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_4 = new PrimaryDrawerItem().withIdentifier(4).withName( R.string.category_name_4 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_cellphone, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_5 = new PrimaryDrawerItem().withIdentifier(5).withName( R.string.category_name_5 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_news, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_33 = new PrimaryDrawerItem().withIdentifier(33).withName( R.string.category_name_33 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_politics, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_6 = new PrimaryDrawerItem().withIdentifier(6).withName( R.string.category_name_6 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_sport, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_7 = new PrimaryDrawerItem().withIdentifier(7).withName( R.string.category_name_7 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_entertainment, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_8 = new PrimaryDrawerItem().withIdentifier(8).withName( R.string.category_name_8 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_anime, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_9 = new PrimaryDrawerItem().withIdentifier(9).withName( R.string.category_name_9 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_apps, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_10 = new PrimaryDrawerItem().withIdentifier(10).withName( R.string.category_name_10 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_gamepad_variant, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_11 = new PrimaryDrawerItem().withIdentifier(11).withName( R.string.category_name_11 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_movie, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_12 = new PrimaryDrawerItem().withIdentifier(12).withName( R.string.category_name_12 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_story, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_30 = new PrimaryDrawerItem().withIdentifier(30).withName( R.string.category_name_30 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_heart, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_13 = new PrimaryDrawerItem().withIdentifier(13).withName( R.string.category_name_13 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_fashion, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_14 = new PrimaryDrawerItem().withIdentifier(14).withName( R.string.category_name_14 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_work, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_15 = new PrimaryDrawerItem().withIdentifier(15).withName( R.string.category_name_15 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_coin, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_16 = new PrimaryDrawerItem().withIdentifier(16).withName( R.string.category_name_16 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_food_plate_knife_fork, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_17 = new PrimaryDrawerItem().withIdentifier(17).withName( R.string.category_name_17 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_travel, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_18 = new PrimaryDrawerItem().withIdentifier(18).withName( R.string.category_name_18 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_book, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_19 = new PrimaryDrawerItem().withIdentifier(19).withName( R.string.category_name_19 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_school, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_20 = new PrimaryDrawerItem().withIdentifier(20).withName( R.string.category_name_20 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_car, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_21 = new PrimaryDrawerItem().withIdentifier(21).withName( R.string.category_name_21 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_music, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_31 = new PrimaryDrawerItem().withIdentifier(31).withName( R.string.category_name_31 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_creative, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_22 = new PrimaryDrawerItem().withIdentifier(22).withName( R.string.category_name_22 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_hardware, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_23 = new PrimaryDrawerItem().withIdentifier(23).withName( R.string.category_name_23 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_camera, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_24 = new PrimaryDrawerItem().withIdentifier(24).withName( R.string.category_name_24 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_toys, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_25 = new PrimaryDrawerItem().withIdentifier(25).withName( R.string.category_name_25 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_pets, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_26 = new PrimaryDrawerItem().withIdentifier(26).withName( R.string.category_name_26 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_software, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_27 = new PrimaryDrawerItem().withIdentifier(27).withName( R.string.category_name_27 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_activities, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_35 = new PrimaryDrawerItem().withIdentifier(35).withName( R.string.category_name_35 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_radio, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_34 = new PrimaryDrawerItem().withIdentifier(34).withName( R.string.category_name_34 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_stream, Color.parseColor("#FFFFFF")) );
        PrimaryDrawerItem di_28 = new PrimaryDrawerItem().withIdentifier(28).withName( R.string.category_name_28 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_annouce, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_29 = new PrimaryDrawerItem().withIdentifier(29).withName( R.string.category_name_29 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_relationship, Color.parseColor("#FFFFFF") ) );
        PrimaryDrawerItem di_32 = new PrimaryDrawerItem().withIdentifier(32).withName( R.string.category_name_32 ).withSelectable( true ).withIcon( DrawableUtils.tintDrawable( a,
                R.drawable.ic_blackhole, Color.parseColor("#FFFFFF") ) );

        Drawer result_appended = new DrawerBuilder()
                .withActivity( a )
                .withToolbar( toolbar )
                .addDrawerItems(
                        sdi_1
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
//                        , di_999 // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        // 唔要自選台 冇得解
                        , di_2
                        , di_3
                        , di_1
                        , di_31
                        , di_12
                        , di_18
                        , sdi_2
                        , di_7
                        , di_15
                        , di_5
                        , di_33
                        , sdi_3
                        , di_22
                        , di_26
                        , di_4
                        , di_9
                        , di_35
                        , sdi_4
                        , di_14
                        , di_19
                        , di_27
                        , di_30
                        , di_16
                        , di_17
                        , sdi_5
                        , di_10
                        , di_8
                        , di_21
                        , di_25
                        , di_24
                        , di_29
                        , di_6
                        , di_11
                        , di_23
                        , di_20
                        , di_13
                        , di_34
                        , sdi_6
                        , di_28
                        , di_32
                )
                .withOnDrawerItemClickListener(new OnRightDrawerItemClickListener(a))
                .withDrawerGravity(Gravity.END)
                .withDrawerWidthDp(200)
                .append( result );
        return result_appended;
    }

    protected static Drawer constructLeftDrawer( final Activity a, AccountHeader header_result ) {
        Toolbar toolbar = (Toolbar) a.findViewById( R.id.toolbar );
        PrimaryDrawerItem di_history = new PrimaryDrawerItem().withIdentifier(0).withName("回　帶").withSelectable(false)
                .withIcon( DrawableUtils.tintDrawable( a, R.drawable.ic_history_black_24dp, Color.parseColor("#9C27B0") ) );
        PrimaryDrawerItem di_watch_later = new PrimaryDrawerItem().withIdentifier(1).withName("陣間睇").withSelectable(false)
                .withIcon( DrawableUtils.tintDrawable( a, R.drawable.ic_watch_later_black_24dp, Color.parseColor("#8BC34A") ) );
        PrimaryDrawerItem di_saved = new PrimaryDrawerItem().withIdentifier(2).withName("已儲存").withSelectable(false)
                .withIcon( DrawableUtils.tintDrawable( a, R.drawable.ic_bookmark_black_24dp, Color.parseColor("#03A9F4") ) );
        PrimaryDrawerItem di_blocked = new PrimaryDrawerItem().withIdentifier(3).withName("已封鎖").withSelectable(false)
                .withIcon( DrawableUtils.tintDrawable( a, R.drawable.ic_block_black_24dp, Color.parseColor("#F44336") ) );
        PrimaryDrawerItem di_login = new PrimaryDrawerItem().withIdentifier(4).withName("登入").withSelectable(false)
                .withIcon( DrawableUtils.tintDrawable( a, R.drawable.ic_account_circle_black_24dp, Color.parseColor("#CDDC39") ) );
        PrimaryDrawerItem di_about = new PrimaryDrawerItem().withIdentifier(5).withName("關於").withSelectable(false)
                .withIcon( DrawableUtils.tintDrawable( a, R.drawable.ic_info_black_24dp, Color.parseColor("#FFFFFF") ) );

        Drawer result = new DrawerBuilder()
                .withActivity( a )
                .withToolbar( toolbar )
                .withHasStableIds( true )
                .withAccountHeader( header_result )
                .withActionBarDrawerToggle( true )
                .withActionBarDrawerToggleAnimated( true )
                .withCloseOnClick( true )
                .withSelectedItem(-1)
                .addDrawerItems(
                        di_history
                        , di_watch_later
                        , di_saved
                        , di_blocked
                        , new DividerDrawerItem()
                        , di_login
                        , new DividerDrawerItem()
                        , di_about
                )
                .withOnDrawerItemClickListener( new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem ) {
                        if( drawerItem!=null ) { // check if drawerItem is set (not header/footer)
                            Intent intent = null;

                            long id = drawerItem.getIdentifier();
                            if( id==0 ) intent = new Intent(a, HistoryActivity.class);
                            else if( id==1 ) intent = new Intent(a, WatchLaterActivity.class);
                            else if( id==2 ) intent = new Intent(a, FavouriteActivity.class);
                            else if( id==3 ) intent = new Intent(a, BlockedUserActivity.class);
                            else if( id==4 ) Toast.makeText(view.getContext(),"冇API :(",
                                    Toast.LENGTH_SHORT).show();
                            else if( id==5 ) intent = new Intent(a, AboutActivity.class);

                            if( intent!=null ) {
                                view.getContext().startActivity(intent);
                            }
                        }
                        return false;
                    }
                } )
                .build();
        return result;
    }

    protected static AccountHeader constructHeader( final Activity a ) {
        AccountHeader result = new AccountHeaderBuilder()
                .withActivity( a )
                .withHeaderBackground( R.drawable.nav_menu_header_bg )
                .addProfiles(
                        new ProfileDrawerItem().withEmail("LIHKG討論區").withIcon( a.getResources().getDrawable( R.drawable.lihkg_logo ) )
                        , new ProfileDrawerItem().withEmail("HKGolden 香港高登").withIcon( a.getResources().getDrawable( R.drawable.hkgolden_logo ) )
                        , new ProfileDrawerItem().withEmail("HKGalden 香港膠登").withIcon( a.getResources().getDrawable( R.drawable.hkgalden_logo ) )
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        return result;
    }

//    public static Drawable tintDrawable( final Activity a, int id, int color ) {
//        Drawable wrapped_drawable = DrawableCompat.wrap(
//                a.getResources().getDrawable( id )
//        ).mutate();
//        DrawableCompat.setTint( wrapped_drawable, color );
//        return wrapped_drawable;
//    }

    private static class OnRightDrawerItemClickListener implements Drawer.OnDrawerItemClickListener {
        private Activity _a;
        public OnRightDrawerItemClickListener( final Activity a ) {
            this._a = a;
        }
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            if( drawerItem!=null ) {
                long id = drawerItem.getIdentifier();
                // SO MUCH CASTING IN JAVA SO ANNOYING
                // SO MUCH CASTING IN JAVA SO ANNOYING
                // SO MUCH CASTING IN JAVA SO ANNOYING
                // SO MUCH CASTING IN JAVA SO ANNOYING
                // SO MUCH CASTING IN JAVA SO ANNOYING
                // SO MUCH CASTING IN JAVA SO ANNOYING
                // SO MUCH CASTING IN JAVA SO ANNOYING
                ( (MainActivity) a() ).changeSubforum( (int) id );
                // 轉台 method here
                // 轉台 method here
                // 轉台 method here
                // 轉台 method here
                // 轉台 method here
                // 轉台 method here
                // 轉台 method here
                // 轉台 method here
                // 轉台 method here
            }
            return false;
        }
        public Activity a() { return _a; }
    }
}
