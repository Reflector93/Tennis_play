package com.tennis_play;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.tennis_play.databinding.ActivityMainBinding;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity
{
    public interface requestCallback
    {
        void onGranted();
        void onCancelled();
    }

    requestCallback requestCallback;

    public ActivityResultLauncher<String> resultLauncher = registerForActivityResult
    (
        new ActivityResultContracts.RequestPermission(),
        isGranted ->
        {
            if( isGranted )
                requestCallback.onGranted();
            else
                requestCallback.onCancelled();
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mainBinding.navView, navController);
    }

    public void requestPermission(String permission, requestCallback callback)
    {
        if( callback != null )
        {
            requestCallback = callback;

            if( checkPermission(permission) )
                callback.onGranted();
            else
                resultLauncher.launch(permission);
        }
    }

    public boolean checkPermission(String permission)
    {
        if( permission != null )
        {
            if (Build.VERSION.SDK_INT < 23)
                return true;
            else
                return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
}