package com.example.mada.partybay.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mada.partybay.Class.SerializeurMono;
import com.example.mada.partybay.Class.User;
import com.example.mada.partybay.R;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
/*
 * Created by mada on 20/10/2014.
 */
public class Reglage extends Activity {

    private TextView pseudo = null;
    private TextView num = null;
    private TextView mail = null;

    private SerializeurMono<User> serializeur;
    private Button deco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reglage);
        ActionBar bar = this.getActionBar();
        bar.hide();
        Log.d("je suis la ", "ok");

        pseudo = (TextView) findViewById(R.id.pseudo);
        num = (TextView) findViewById(R.id.num);
        mail = (TextView) findViewById(R.id.email);
        deco = (Button) findViewById(R.id.reglage_deco_button);

        serializeur = new SerializeurMono<User>(getResources().getString(R.string.sdcard_user));

        JSONObject obj = new JSONObject();
        User user;
        user = serializeur.getObject();
        user.getId();

        pseudo.setText(user.getPseudo());
        num.setText(user.getPhone());
        mail.setText(user.getEmail());

        deco.setOnClickListener(decoListener);

    }

    View.OnClickListener decoListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            File directory =new File(getResources().getString(R.string.sdcard_path));

            if(!directory.exists()){
                System.out.println("Directory does not exist.");
                System.exit(0);
            }else{
                try{

                    delete(directory);

                }catch(IOException e){
                    e.printStackTrace();
                    System.exit(0);
                }

                Intent i = new Intent(Reglage.this,Chargement.class);
                startActivity(i);
                finish();
            }

        }
    };


    public static void delete(File file) throws IOException{

        if(file.isDirectory()){
            //directory is empty, then delete it
            if(file.list().length==0){
                file.delete();
                System.out.println("Directory is deleted : " + file.getAbsolutePath());

            }else{
                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                    System.out.println("Directory is deleted : " + file.getAbsolutePath());
                }
            }

        }else{
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }
}
