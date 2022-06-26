package com.example.hambola.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.hambola.R;
import com.example.hambola.prePathFinder;
import com.example.hambola.splashScreen;
import com.example.hambola.tictac;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.jar.Attributes;

import javax.xml.transform.Result;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s)
            {
                assert s != null;
                Log.i("Changed",s);
            }
        });

        return root;
    }



    @SuppressLint("SetTextI18n")
    public void onStart()
    {

        super.onStart();


        final SharedPreferences preferences = this.requireActivity().getPreferences(MODE_PRIVATE);
        NavigationView view = requireActivity().findViewById(R.id.nav_view);

        int logIn = preferences.getInt("logIn",3);
        int gems = preferences.getInt("hearts",-1);

        if(gems != -1)
        {
           View view1 = view.getHeaderView(0);
           TextView textView = view1.findViewById(R.id.gems);
           textView.setText(""+gems);
           if(gems < 10)
           {
               MaterialButton button = requireActivity().findViewById(R.id.tictactoe);
               button.setEnabled(false);
               MaterialButton button1 = requireActivity().findViewById(R.id.path);
               button1.setEnabled(false);
           }
           if(gems>= 10 && gems < 20)
           {
               MaterialButton button1 = requireActivity().findViewById(R.id.path);
               button1.setEnabled(false);
           }
        }
        else
        {
            preferences.edit().putInt("hearts",0).apply();
            MaterialButton button = requireActivity().findViewById(R.id.tictactoe);
            button.setEnabled(false);
            MaterialButton button1 = requireActivity().findViewById(R.id.path);
            button1.setEnabled(false);
        }

        if(logIn == 3 || logIn == 0)
        {

            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build(),new AuthUI.IdpConfig.GoogleBuilder().build(),new AuthUI.IdpConfig.PhoneBuilder().build());

            preferences.edit().putInt("logIn",2).apply();

            startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.logo)
                            .setIsSmartLockEnabled(true)
                            .setTheme(R.style.FullscreenTheme)
                            .build()
                    ,69);


        }
        else if(logIn == 1)
        {

            String name = preferences.getString("name",null);
            String urlPic = preferences.getString("photo_url",null);


            View v = view.getHeaderView(0);
            ImageView imageView = v.findViewById(R.id.picture);

            if(name != null)
            {
                TextView textView = v.findViewById(R.id.name);
                textView.setText(name);
            }
            if(urlPic != null)
            {
                imageDownload imageDownload = new imageDownload();
                try
                {
                    final Bitmap bitmap = imageDownload.execute(urlPic).get();
                    imageView.setImageBitmap(bitmap);
                    ContextWrapper contextWrapper = new ContextWrapper(requireActivity().getApplicationContext());
                    File file = contextWrapper.getDir("imageDir",MODE_PRIVATE);
                    File outFile = new File(file,"dpBitmap" + ".jpg");
                    final FileOutputStream stream = new FileOutputStream(outFile);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                            try {
                                stream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                }
                catch (ExecutionException e)
                {
                    e.printStackTrace();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            else
            {

                ContextWrapper contextWrapper = new ContextWrapper(requireActivity().getApplicationContext());
                File file = contextWrapper.getDir("imageDir",MODE_PRIVATE);
                File out = new File(file,"dpBitmap"+".jpg");
                try
                {
                    FileInputStream inputStream = new FileInputStream(out.getAbsoluteFile());
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

        }



        MaterialButton button = root.findViewById(R.id.tambola);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getPreferences(MODE_PRIVATE);
                int hearts = sharedPreferences.getInt("hearts",-1);
                NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                View view1 = navigationView.getHeaderView(0);
                TextView textView = view1.findViewById(R.id.gems);
                if(hearts == -1)
                {

                    sharedPreferences.edit().putInt("hearts",5).apply();
                    textView.setText(""+5);

                }
                else
                {
                    int val = sharedPreferences.getInt("hearts",0)+5;
                    sharedPreferences.edit().putInt("hearts",val).apply();
                    textView.setText(""+val);
                }
                updateUnlock();
                Intent intent = new Intent(getContext(),splashScreen.class);
                startActivity(intent);

            }
        });

        MaterialButton button1 = root.findViewById(R.id.tictactoe);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = requireActivity().getPreferences(MODE_PRIVATE);
                int hearts = sharedPreferences.getInt("hearts",-1);
                NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                View view1 = navigationView.getHeaderView(0);
                TextView textView = view1.findViewById(R.id.gems);
                if(hearts == -1)
                {

                    sharedPreferences.edit().putInt("hearts",5).apply();
                    textView.setText(""+5);

                }
                else
                {
                    int val = sharedPreferences.getInt("hearts",0)+5;
                    sharedPreferences.edit().putInt("hearts",val).apply();
                    textView.setText(""+val);
                }
                updateUnlock();
                Intent intent = new Intent(getContext(),tictac.class);
                startActivity(intent);
            }
        });


        MaterialButton button2 = root.findViewById(R.id.path);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = requireActivity().getPreferences(MODE_PRIVATE);
                int hearts = sharedPreferences.getInt("hearts",-1);
                NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                View view1 = navigationView.getHeaderView(0);
                TextView textView = view1.findViewById(R.id.gems);
                if(hearts == -1)
                {

                    sharedPreferences.edit().putInt("hearts",5).apply();
                    textView.setText(""+5);

                }
                else
                {
                    int val = sharedPreferences.getInt("hearts",0)+5;
                    sharedPreferences.edit().putInt("hearts",val).apply();
                    textView.setText(""+val);
                }
                updateUnlock();
                Intent intent = new Intent(getContext(),prePathFinder.class);
                startActivity(intent);
            }
        });

        ImageView insta = root.findViewById(R.id.insta);
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/amxn_25"));
                startActivity(intent);
            }
        });

        ImageView fb = root.findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/"));
                startActivity(intent);
            }
        });

        ImageView git = root.findViewById(R.id.git);
        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/hsr121/"));
                startActivity(intent);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 69)
        {

            if(resultCode == Activity.RESULT_OK && data != null)
            {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null)
                {
                    Toast.makeText(getContext(), "Good morning "+user.getDisplayName(), Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = this.requireActivity().getPreferences(MODE_PRIVATE);
                    preferences.edit().putInt("logIn",1).apply();
                    preferences.edit().putString("name",user.getDisplayName()).apply();

                    NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);

                    View head = navigationView.getHeaderView(0);
                    TextView name = head.findViewById(R.id.name);
                    name.setText(user.getDisplayName());
                    ImageView pic = head.findViewById(R.id.picture);
                    imageDownload imageDownload = new imageDownload();
                    if(user.getPhotoUrl() != null)
                    {
                        preferences.edit().putString("photo_url",user.getPhotoUrl().toString()).apply();
                        final Bitmap bitmap;
                        try
                        {
                            bitmap = imageDownload.execute(user.getPhotoUrl().toString()).get();
                            pic.setImageBitmap(bitmap);
                            ContextWrapper contextWrapper = new ContextWrapper(requireActivity().getApplicationContext());
                            File file = contextWrapper.getDir("imageDir",MODE_PRIVATE);
                            File outFile = new File(file,"dpBitmap" + ".jpg");
                            final FileOutputStream stream = new FileOutputStream(outFile);
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                                    try {
                                        stream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();

                        }
                        catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        preferences.edit().putString("photo_url",null).apply();
                    }
                }
            }

        }


    }
    public void updateUnlock()
    {
        SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
        int gems = preferences.getInt("hearts",0);
        if(gems >= 10 && gems < 20)
        {
            MaterialButton button = requireActivity().findViewById(R.id.tictactoe);
            button.setEnabled(true);
        }
        if(gems >= 20)
        {
            MaterialButton button = requireActivity().findViewById(R.id.path);
            button.setEnabled(true);
        }
    }

}

