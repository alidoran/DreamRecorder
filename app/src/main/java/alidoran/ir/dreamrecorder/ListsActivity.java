package alidoran.ir.dreamrecorder;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListsActivity extends Fragment {

//all arguman

    ArrayList <String> arrayListfilename;
    ListView listView;
    ArrayAdapter <String> adapter;
    String address;
    MediaPlayer player;
    public static ImageView imglist;
    ImageView imgpause;
    ImageView imgplay;
    ImageView imgstop;
    ImageView imgreclist;
    SeekBar seekBar;
    Handler handler;
    Runnable runnable;
    long i;
    String path = "-1";
    File audiofile;
    File[] listfile;
    TextView txtplaytime;
    TextView txttotaltime;
    boolean checkprepare = false;
    TextView txtplayname;
    ImageView imgshare;
    ImageView imgrename;
    Button btnyesdelete;
    Button btnnodelete;
    Button btnyesrename;
    Button btnnorename;
    TextView txtinputrename;
    TextView txtviewrename;


    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState ) {

//all findid

        View rootView = inflater.inflate ( R.layout.activity_list , container , false );

        listView = rootView.findViewById ( R.id.listview );
        imglist = rootView.findViewById ( R.id.imglist );
        imgplay = rootView.findViewById ( R.id.imgplay );
        imgpause = rootView.findViewById ( R.id.imgpause );
        imgstop = rootView.findViewById ( R.id.imgstop );
        imgreclist = rootView.findViewById ( R.id.imgreclist );
        seekBar = rootView.findViewById ( R.id.seekbar );
        txtplaytime = rootView.findViewById ( R.id.txtplaytime );
        txttotaltime = rootView.findViewById ( R.id.txttotaltime );
        txtplayname = rootView.findViewById ( R.id.txtplayname );
        imgshare = rootView.findViewById ( R.id.imgshare );
        imgrename = rootView.findViewById ( R.id.imgrename );


        //all of listener load
        clicklisteners ( );

        return rootView;
    }

//delete dialog

    private void DeleteDialog ( final String deletepath ) {

        final AlertDialog.Builder alert = new AlertDialog.Builder ( MainActivity.context );
        alert.setView ( R.layout.dialog_delete );
        alert.setMessage ( "Delete" );

        final AlertDialog dialog = alert.create ( );
        dialog.getWindow ( ).setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
        dialog.show ( );

        btnyesdelete = dialog.findViewById ( R.id.btnyesdelete );
        btnnodelete = dialog.findViewById ( R.id.btnnodelete );
        btnyesdelete.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                File file = new File ( deletepath );
                file.delete ( );
                dialog.dismiss ( );
                showlist ( );
            }
        } );
        btnnodelete.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                dialog.dismiss ( );
            }
        } );
    }

//rename dialog

    private void renamedialog ( ) {


        stopaudio ( );

        final AlertDialog.Builder alert = new AlertDialog.Builder ( MainActivity.context );
        alert.setView ( R.layout.dialog_rename );
        alert.setTitle ( "rename" );


        final AlertDialog dialog = alert.create ( );
        dialog.getWindow ( ).setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
        dialog.show ( );

        btnyesrename = dialog.findViewById ( R.id.btnyesrename );
        btnnorename = dialog.findViewById ( R.id.btnnorename );
        txtinputrename = dialog.findViewById ( R.id.txtinputrename );
        txtviewrename = dialog.findViewById ( R.id.txtviewrename );
        txtviewrename.setText ( "Are you sure to rename " + txtplayname.getText ( ) + " file?" );

        btnyesrename.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                File file = new File ( path );
                File file2 = new File ( address + txtinputrename.getText ( ) + path.substring ( path.lastIndexOf ( "." ) ) );
                boolean success = file.renameTo ( file2 );
                showlist ( );
                dialog.dismiss ( );
            }
        } );
        btnnorename.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                dialog.dismiss ( );
            }
        } );
    }

//showlist method

    public void showlist ( ) {
        address = Environment.getExternalStorageDirectory ( ) + "/record_folder/";
        arrayListfilename = new ArrayList <String> ( );
        audiofile = new File ( address );
        listfile = audiofile.listFiles ( );
        adapter = new ArrayAdapter <String> ( MainActivity.context , android.R.layout.simple_list_item_1 , arrayListfilename );

//solved listview problem

            if (listfile.length == 0) {
                listView.setAdapter ( null );
            } else {

                for (int i = 0; i < listfile.length; i++) {

                    arrayListfilename.add ( listfile[i].getName ( ) + "\nduration:" + timecalculate ( address + listfile[i].getName ( ) ) + "      File size=" + filesize ( address + listfile[i].getName ( ) ) + "KB" );

                    listView.setAdapter ( adapter );
                    adapter.notifyDataSetChanged ( );
                }
            }
        }


//showplaytime

    public void showplaytime ( ) {
        int playsec = player.getCurrentPosition ( );
        txtplaytime.setText ( secToTime ( playsec ) );
        txtplaytime.setTextColor ( Color.parseColor ( "#ffffff" ) );
    }

//show total audio time

    public void showtotaltime ( ) {
        int totalsec = player.getDuration ( );
        txttotaltime.setText ( secToTime ( totalsec ) );
        txttotaltime.setTextColor ( Color.parseColor ( "#ffffff" ) );
    }

//clculate time for list show

    public String timecalculate ( String pathaddress ) {
        MediaPlayer mediacalculate = new MediaPlayer ( );
        try {
            mediacalculate.setDataSource ( pathaddress );
            mediacalculate.prepare ( );

        } catch (IOException e) {
            e.printStackTrace ( );
        }
        int seccalculate = mediacalculate.getDuration ( );
        String timecal = secToTime ( seccalculate );
        return timecal;
    }

//convert sec to 00:00:00 format

    public String secToTime ( int totalsec ) {
        int sec = (totalsec / 1000) % 60;
        int min = (((totalsec) / 1000) / 60) % 60;
        int hour = ((totalsec) / 1000) / 3600;
        return (String.format ( "%02d" , hour ) + ":" + String.format ( "%02d" , min ) + ":" + String.format ( "%02d" , sec ));
    }

//get file size for show in list

    public String filesize ( String pathaddress ) {
        File file = new File ( pathaddress );
        return String.valueOf ( file.length ( ) / 1024 );
    }

//update seekbar method

    public void updateSeekbar ( ) {
        float progress = (( float ) player.getCurrentPosition ( ) / player.getDuration ( ));  //find current progress position of mediaplayer
        seekBar.setProgress ( ( int ) (progress * 1000) ); //set this progress to seekbar
        handler.postDelayed ( runnable , 1000 ); //run handler again after 1 second
        showplaytime ( );
        showtotaltime ( );
    }

//multimedia method

    private void resumeaudio ( ) {
        if (checkprepare && !player.isPlaying ( )) {
            player.start ( );
            seekBar.setEnabled ( true );
            updateSeekbar ( );
        } else {
            return;
        }
    }

    private void pauseaudio ( ) {
        if (checkprepare && player.isPlaying ( )) {
            player.pause ( );
        } else {
            return;
        }
    }

    private void stopaudio ( ) {

        if (checkprepare && player.isPlaying ( )) {
            player.stop ( );
            seekBar.setEnabled ( false );
        } else {
            return;
        }
    }

    private void playaudio ( ) {
        stopaudio ( );
        player = new MediaPlayer ( );
        try {
            player.setDataSource ( path );
            player.prepare ( );
            checkprepare = true;
            player.start ( );
            seekBar.setEnabled ( true );
            updateSeekbar ( );
        } catch (IOException e) {
            e.printStackTrace ( );
        }
    }

//sharefile

    public void sharefile ( ) {
        if (checkprepare) {
            File sharefile = new File ( path );
            Uri shareuri = Uri.fromFile ( sharefile );
            String sharetext = "share by recorder ...";
            Intent shareintent = new Intent ( );
            shareintent.setAction ( Intent.ACTION_SEND );
            shareintent.putExtra ( Intent.EXTRA_TEXT , sharetext );
            shareintent.putExtra ( Intent.EXTRA_STREAM , shareuri );
            shareintent.setType ( "*/*" );
            shareintent.addFlags ( Intent.FLAG_GRANT_READ_URI_PERMISSION );
            startActivity ( shareintent.createChooser ( shareintent , "share file ..." ) );
        }
    }


//all cliclisteners

    public void clicklisteners ( ) {

        imgrename.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                animation ( v );
                if (path != "-1") {
                    renamedialog ( );
                } else return;
            }
        } );

        //refresh list key listener

        imglist.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                animation ( v );
                showlist ( );
            }
        } );

//click item list play

        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick ( AdapterView <?> parent , View view , int position , long id ) {
                animation ( view );
                path = listfile[position].toString ( );
                txtplayname.setText ( listfile[position].getName ( ) );
                txtplayname.setTextColor ( Color.parseColor ( "#ffffff" ) );
                playaudio ( );
            }
        } );

//long click for delete

        listView.setOnItemLongClickListener ( new AdapterView.OnItemLongClickListener ( ) {
            @Override
            public boolean onItemLongClick ( AdapterView <?> parent , View view , int position , long id ) {
                stopaudio ( );
                path = listfile[position].toString ( );
                DeleteDialog ( path );
                return true;
            }
        } );

//multimedia key pressed

        imgplay.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                animation ( v );
                resumeaudio ( );
            }
        } );

        imgstop.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                animation ( v );
                stopaudio ( );
            }
        } );

        imgpause.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                animation ( v );
                pauseaudio ( );
            }
        } );

        imgshare.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                animation ( v );
                if (checkprepare) {
                    sharefile ( );
                }
            }
        } );

//link record key in listview to main record key and intent to record fragment

        final MainActivity mainActivity = new MainActivity ( );
        final RecordActivity recordActivity = new RecordActivity ( );
        imgreclist.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                mainActivity.viewPager.setCurrentItem ( 0 , true );
                recordActivity.imgrec.performClick ( );
            }
        } );

//change seekbar

        seekBar.setOnSeekBarChangeListener ( new SeekBar.OnSeekBarChangeListener ( ) {
            @Override
            public void onProgressChanged ( SeekBar seekBar , int progress , boolean fromUser ) {
            }

            @Override
            public void onStartTrackingTouch ( SeekBar seekBar ) {
            }

            @Override
            public void onStopTrackingTouch ( SeekBar seekBar ) {
                int durration = player.getDuration ( );
                player.seekTo ( (durration * seekBar.getProgress ( )) / 1000 );
            }
        } );

//runable seekbar

        handler = new Handler ( );
        runnable = new Runnable ( ) {
            @Override
            public void run ( ) {
                updateSeekbar ( );
            }
        };
    }

    public void animation ( View v ) {
        @SuppressLint("ResourceType") Animation pulse = AnimationUtils.loadAnimation ( MainActivity.context , R.drawable.imageviewanimation );
        v.startAnimation ( pulse );
    }

}

