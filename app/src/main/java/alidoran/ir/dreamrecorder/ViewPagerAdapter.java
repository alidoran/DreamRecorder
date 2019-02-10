package alidoran.ir.dreamrecorder;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ArrayList <Fragment> fragments;
    public ArrayList <String> titles;


    public ViewPagerAdapter ( FragmentManager fm ) {
        super ( fm );
        fragments = new ArrayList <> ( );
        titles = new ArrayList <> ( );

    }

    @Override
    public Fragment getItem ( int position ) {
        return fragments.get ( position );
    }

    @Override
    public int getCount ( ) {
        return fragments.size ();
    }

    public void addFragment(Fragment fragment,String title){
        fragments.add(fragment);
        titles.add ( title );

    }

    @Nullable
    @Override
    public CharSequence getPageTitle ( int position ) {
        return titles.get ( position );
    }
}
