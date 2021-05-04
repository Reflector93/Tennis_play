package com.tennis_play;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class CustomChromeClient extends WebChromeClient
{
    private final Context context;
    private final MainActivity activity;

    public CustomChromeClient(Context context)
    {
        this.context = context;
        this.activity = (MainActivity) context;
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams)
    {
        String permission = Manifest.permission.CAMERA;

        if( activity.checkPermission(permission) )
        {
            context.startActivity( new Intent(context, CustomCamera.class) );
        }
        else
        {
            activity.requestPermission(permission, new MainActivity.requestCallback()
            {
                @Override
                public void onGranted()
                {
                    context.startActivity( new Intent(context, CustomCamera.class) );
                }

                @Override
                public void onCancelled()
                {
                    context.startActivity( fileChooserParams.createIntent().setType("image/*") );
                }
            });
        }

        return true;
//        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
    }
}
