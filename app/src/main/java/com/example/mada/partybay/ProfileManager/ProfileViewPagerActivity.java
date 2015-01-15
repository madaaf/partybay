package com.example.mada.partybay.ProfileManager;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mada.partybay.Activity.CameraSelfie;
import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;
import com.example.mada.partybay.TimeLineManager.PostActivity;

import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by mada on 10/11/2014.
 */
public class ProfileViewPagerActivity extends FragmentActivity{

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    ProfileViewPagerAdapter mAppSectionsPagerAdapter;
    View markerMoments;
    View markerTrackers;
    View markerTracking;
    int position = 0;
    ImageButton retour_b;
    SerializeurMono<User> serializeur_user;
    TextView pseudoTv;
    ImageView profile_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ActionBar bar = this.getActionBar();
        bar.hide();

        serializeur_user = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));
        JSONObject obj = new JSONObject();
        User user = serializeur_user.getObject();




        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager());

        markerMoments= (View)findViewById(R.id.markerMoments);
        markerTrackers=(View)findViewById(R.id.markerTrackers);
        markerTracking=(View)findViewById(R.id.markerTracking);
        retour_b=(ImageButton)findViewById(R.id.PROFILEretour);
        pseudoTv = (TextView)findViewById(R.id.profile_pseudo);
        profile_photo = (ImageView)findViewById(R.id.profile_photo);


        File fichierPhoto = new File("storage/emulated/0/Pictures/PartyBay/selfie.jpg");

        boolean exist = fichierPhoto.exists();
        System.out.println("EXIST"+exist);
        if(fichierPhoto.length()==0){
            fichierPhoto.delete();
            exist=false;
        }else{
            String path = "storage/emulated/0/Pictures/PartyBay/selfie.jpg";
            Bitmap bmp = decodeSampledBitmapFromFile(path, 500, 300);
            Bitmap temp = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);
            Canvas c = new Canvas(temp);
            c.drawBitmap(bmp, 0, 0, null);
            profile_photo.setImageBitmap(temp);
        }




        /*
        String photo_path = "/storage/sdcard0/PartyBay2/photo.jpeg";
        Bitmap bmp = decodeSampledBitmapFromFile(photo_path, 640, 640);
        profile_photo.setImageBitmap(getRoundedBitmap(bmp));*/
        profile_photo.setOnClickListener(ListenerPhotoSelfie);

       // markerMoments.setBackgroundResource(0);
        markerTrackers.setBackgroundResource(0);
        markerTracking.setBackgroundResource(0);

        System.out.println("BIRTHDAY  : "+user.getBirth());
        String year = user.getBirth().substring(0, 4);
        String month = user.getBirth().substring(5, 7);
        String jour = user.getBirth().substring(8, user.getBirth().length());

        int year2 = Integer.parseInt(year);
        int month2 = Integer.parseInt(month);
        int jour2 = Integer.parseInt(jour);
        int age = getAge(year2,month2,jour2);


        pseudoTv.setText(user.getPseudo()+","+age);
        mViewPager = (ViewPager)findViewById(R.id.profileviewpager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);


        retour_b.setOnClickListener(retourListener);


        // modifier l'indicateur lorsqu'on swippe
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {

                if(position==0){
                    System.out.println("position 0 ====+>"+position);
                    markerMoments.setBackgroundResource(R.color.red);
                    markerTrackers.setBackgroundResource(0);
                    markerTracking.setBackgroundResource(0);

                }
               if(position==1){
                    System.out.println("position  1  ====+>"+position);
                   markerMoments.setBackgroundResource(0);
                   markerTrackers.setBackgroundResource(R.color.red);
                   markerTracking.setBackgroundResource(0);
                }
                if(position==2){
                    System.out.println("position 2 ====+>"+position);
                    markerMoments.setBackgroundResource(0);
                    markerTrackers.setBackgroundResource(0);
                    markerTracking.setBackgroundResource(R.color.red);
                }


            }

        });


    }

    View.OnClickListener retourListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(ProfileViewPagerActivity.this, PostActivity.class );
            startActivity(i);
            finish();

        }
    };

    public int getAge (int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
    }


    public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        Log.d("Camera ", "decodeSampledBitmapFromFile class Camera");
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    View.OnClickListener ListenerPhotoSelfie = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            Intent i  = new Intent(ProfileViewPagerActivity.this,CameraSelfie.class);
            startActivity(i);
        }
    };
}
