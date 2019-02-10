package alidoran.ir.dreamrecorder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;


public class RecordActivity extends Fragment {

    public static ImageView imgrec;
    MediaRecorder recorder;
    ImageView imgmic;
    TextView txttimer;
    boolean recordstate = false;
    public static Context context;
    Button btnlist;
    int sec;
    int min;
    int hour;
    public static Handler handler = new Handler ( );
    Thread thread;
    RadioButton rbmp4;
    RadioButton rbamr;
    RadioButton rbaac;
    RadioButton rb3gp;
    int format = 2;
    String type = ".mp4";
    int encoder = 3;
    RadioButton rbbluetooth;
    RadioButton rbphone;
    RadioGroup rgformat;
    RadioGroup rginput;
    ImageView imgaboutus;
    Button btnaboutus;
    TextView txtaboutus;



    @Override
    public View onCreateView ( LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState ) {

        View rootView = inflater.inflate ( R.layout.activity_record , container , false );


        imgrec = rootView.findViewById ( R.id.imgrec );
        imgmic = rootView.findViewById ( R.id.imgmic );
        txttimer = rootView.findViewById ( R.id.txttimet );
        btnlist = rootView.findViewById ( R.id.imglist );
        rbmp4 = rootView.findViewById ( R.id.rbmp4 );
        rbaac = rootView.findViewById ( R.id.rbaac );
        rbamr = rootView.findViewById ( R.id.rbamr );
        rb3gp = rootView.findViewById ( R.id.rb3gp );
        rbbluetooth = rootView.findViewById ( R.id.rbbluetooth );
        rbphone = rootView.findViewById ( R.id.rbphone );
        rgformat = rootView.findViewById ( R.id.rgformat );
        rginput = rootView.findViewById ( R.id.rginput );
        imgaboutus=rootView.findViewById ( R.id.imgaboutus );
        txtaboutus=rootView.findViewById ( R.id.textaboutus );





        new File ( Environment.getExternalStorageDirectory ( ).getAbsolutePath ( ) + "/record_folder/" ).mkdirs ( );

imgaboutus.setOnClickListener ( new View.OnClickListener ( ) {
    @Override
    public void onClick ( View v ) {
        aboutusdialog ();
    }
} );


        imgrec.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( final View v ) {


//double click disabled

                v.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                }, 1000);

//after click

                    format ( );
                    input ( );
                    if (!recordstate) {

                        timer ( );
                        recorder = new MediaRecorder ( );
                        recorder.setAudioSource ( MediaRecorder.AudioSource.MIC );
                        recorder.setOutputFormat ( format );
                        recorder.setAudioEncoder ( encoder );
                        recorder.setOutputFile ( Environment.getExternalStorageDirectory ( ).getAbsolutePath ( ) + "/record_folder/" + TimeActivity.timeNow ( ) + type );
                        recorder.setAudioEncodingBitRate ( 128000 );
                        try {
                            recorder.prepare ( );
                            recorder.start ( );
                        } catch (IOException e) {
                            e.printStackTrace ( );
                        }
                    } else if (recordstate)
                    {
                        imgrec.setImageResource ( R.mipmap.recording );
                        imgmic.setImageResource ( R.mipmap.microphoneoff );
                        recordstate = false;
                        recorder.stop ( );
                        scooff ( );
                        recorder.reset ( );
                        scooff ( );
                    }
                }
    } );
        return rootView;
    }

    public void format ( ) {
        int rbmp4id = rbmp4.getId ( );
        int rbaacid = rbaac.getId ( );
        int rbamrid = rbamr.getId ( );
        int rb3gpid = rb3gp.getId ( );
        int rgformatselectid = rgformat.getCheckedRadioButtonId ( );

        if (rbmp4id == rgformatselectid) {
            format = 2;
            type = ".mp4";
            encoder = 3;
        } else if (rbaacid == rgformatselectid) {
            format = 6;
            type = ".aac";
            encoder = 3;
        } else if (rbamrid == rgformatselectid) {
            format = 3;
            type = ".amr";
            encoder = 1;
        } else if (rb3gpid == rgformatselectid) {
            format = 1;
            type = ".3gp";
            encoder = 5;
        }
    }

    public void input ( ) {

        int rbphoneid = rbphone.getId ( );
        int rbbluetoothid = rbbluetooth.getId ( );
        int rginputselectid = rginput.getCheckedRadioButtonId ( );

        if (rbphoneid == rginputselectid) {
            scooff ( );
        } else if (rbbluetoothid == rginputselectid) {
            try {
                MainActivity.audioManager.startBluetoothSco ( );
                MainActivity.audioManager.setBluetoothScoOn ( true );
            } catch (Exception e) {
                e.printStackTrace ( );
            }
        }
    }

    public void scooff ( ) {
        try {
            MainActivity.audioManager.stopBluetoothSco ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }
    }

    public void timer ( ) {
        sec = 00;
        min = 00;
        hour = 00;
        imgrec.setImageResource ( R.mipmap.recordstop );
        imgmic.setImageResource ( R.mipmap.microphoneon );
        recordstate = true;

        thread = new Thread ( new Runnable ( ) {
            @Override
            public void run ( ) {
                while (recordstate) {
                    try {
                        Thread.sleep ( 1000 );
                        if (recordstate) {
                            if (min != 59) {
                                if (sec != 59) {
                                    sec++;
                                } else {
                                    sec = 0;
                                    min++;
                                }
                            } else {
                                min = 0;
                                hour++;
                            }
                        }
                        handler.post ( new Runnable ( ) {

                            @Override
                            public void run ( ) {
                                txttimer.setText ( String.format ( "%02d" , hour ) + ":" + String.format ( "%02d" , min ) + ":" + String.format ( "%02d" , sec ) );
                            }
                        } );
                    } catch (InterruptedException e) {
                        e.printStackTrace ( );
                    }

                }

            }
        } );
        thread.start ( );
        imgaboutus.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {

            }
        } );
    }
    public void aboutusdialog(){
        AlertDialog.Builder alertaboutus = new AlertDialog.Builder ( MainActivity.context );
        alertaboutus.setView ( R.layout.activity_aboutus );
        alertaboutus.setMessage ( "about us" );
        final AlertDialog dialogaboutus = alertaboutus.create ( );
        dialogaboutus.getWindow ( ).setBackgroundDrawable ( new ColorDrawable ( Color.TRANSPARENT ) );
        dialogaboutus.show ( );
        btnaboutus=dialogaboutus.findViewById ( R.id.btnaboutus );
        btnaboutus.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                dialogaboutus.dismiss ();
            }
        } );
    }

}


