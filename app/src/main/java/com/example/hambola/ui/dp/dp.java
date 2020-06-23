package com.example.hambola.ui.dp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hambola.R;
import com.example.hambola.ui.home.imageDownload;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class dp extends Fragment {

    public static dp newInstance() {
        return new dp();
    }

    View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dp, container, false);

        MaterialButton button = root.findViewById(R.id.button9);
        button.setAlpha(0f);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                ImageView imageView = root.findViewById(R.id.imageView12);

                NavigationView navigationView = requireActivity().findViewById(R.id.nav_view);
                View view = navigationView.getHeaderView(0);
                ImageView dp = view.findViewById(R.id.picture);
                BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                final Bitmap bitmap = drawable.getBitmap();
                ContextWrapper contextWrapper = new ContextWrapper(requireActivity().getApplicationContext());
                File file = contextWrapper.getDir("imageDir",MODE_PRIVATE);
                File outFile = new File(file,"dpBitmap" + ".jpg");
                try
                {

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

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                dp.setImageDrawable(imageView.getDrawable());

                Toast.makeText(getContext(), "Profile picture updated!", Toast.LENGTH_LONG).show();

                SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
                preferences.edit().putString("photo_url",null).apply();

                v.setEnabled(false);
                v.setAlpha(0f);

                MaterialButton materialButton = root.findViewById(R.id.button14);
                materialButton.setEnabled(false);
                materialButton.setAlpha(0f);

            }
        });
        MaterialButton cancel = root.findViewById(R.id.button14);
        cancel.setAlpha(0f);
        cancel.setEnabled(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ImageView imageView = root.findViewById(R.id.imageView12);
                imageView.setImageBitmap(null);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DpViewModel mViewModel = ViewModelProviders.of(this).get(DpViewModel.class);
    }

    public void onStart()
    {
        super.onStart();

        MaterialButton gallery = root.findViewById(R.id.button8);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getContext() != null && PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED)
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,1212);
                }
                else
                {
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CAMERA},0);
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED)
        {
            MaterialButton gallery = root.findViewById(R.id.button8);
            gallery.performClick();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1212 && resultCode== -1 && data != null)
        {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            ImageView imageView = root.findViewById(R.id.imageView12);

            imageView.setImageBitmap(bitmap);

            MaterialButton button = root.findViewById(R.id.button9);
            button.setAlpha(1f);
            button.setEnabled(true);
            MaterialButton cancel = root.findViewById(R.id.button14);
            cancel.setAlpha(1f);
            cancel.setEnabled(true);

        }
    }
}