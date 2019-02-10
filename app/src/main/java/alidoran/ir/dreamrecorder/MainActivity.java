package alidoran.ir.dreamrecorder;

import android.Manifest;
import android.content.Context;
import android.media.AudioManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAd;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.TapsellAdShowListener;
import ir.tapsell.sdk.TapsellShowOptions;
import ir.tapsell.sdk.bannerads.TapsellBannerView;

import static ir.tapsell.sdk.TapsellAdRequestOptions.CACHE_TYPE_STREAMED;


public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    public static ViewPager viewPager;
    public static Context context;
    ImageView imgreclist;
    public static AudioManager audioManager;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        TapsellAdRequestOptions stream = new TapsellAdRequestOptions ( CACHE_TYPE_STREAMED );
        Tapsell.requestAd ( context , null , stream , new TapsellAdRequestListener ( ) {
            @Override
            public void onError ( String error ) {
            }

            @Override
            public void onAdAvailable ( TapsellAd ad ) {
            }

            @Override
            public void onNoAdAvailable ( ) {
            }

            @Override
            public void onNoNetwork ( ) {
            }

            @Override
            public void onExpiring ( TapsellAd ad ) {
            }
        } );

        TapsellShowOptions tapsellShowOptions = new TapsellShowOptions ( );
        tapsellShowOptions.setBackDisabled ( true );

        TapsellAd ad=new TapsellAd ();
        ad.show ( context , tapsellShowOptions , new TapsellAdShowListener ( ) {
            @Override
            public void onOpened ( TapsellAd ad ) {
            }
            @Override
            public void onClosed ( TapsellAd ad ) {
            }
        } );


        TapsellBannerView tView = findViewById ( R.id.tapsell_banner );



        audioManager = ( AudioManager ) getSystemService ( Context.AUDIO_SERVICE );

        ActivityCompat.requestPermissions ( MainActivity.this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.RECORD_AUDIO , Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.MODIFY_AUDIO_SETTINGS} , 0 );

        context = MainActivity.this;
        tabLayout = findViewById ( R.id.tablayout );
        viewPager = findViewById ( R.id.viewpager );

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter ( getSupportFragmentManager ( ) );

        viewPagerAdapter.addFragment ( new RecordActivity ( ) , "Record" );
        viewPagerAdapter.addFragment ( new ListsActivity ( ) , "Record List" );


        viewPager.setAdapter ( viewPagerAdapter );
        tabLayout.setupWithViewPager ( viewPager );

        tabLayout.getTabAt ( 0 ).setIcon ( R.mipmap.recordtab );
        tabLayout.getTabAt ( 1 ).setIcon ( R.mipmap.list );


//refresh change viewpage
        final ListsActivity listsActivity = new ListsActivity ( );
        viewPager.addOnPageChangeListener ( new ViewPager.OnPageChangeListener ( ) {
            @Override
            public void onPageScrolled ( int i , float v , int i1 ) {
            }

            @Override
            public void onPageSelected ( int i ) {
                listsActivity.imglist.performClick ( );
            }

            @Override
            public void onPageScrollStateChanged ( int i ) {
            }
        } );

    }
}


