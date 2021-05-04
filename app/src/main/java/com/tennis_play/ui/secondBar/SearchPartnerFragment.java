package com.tennis_play.ui.secondBar;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.tennis_play.R;
import com.tennis_play.databinding.FragmentSearchPartnerBinding;

public class SearchPartnerFragment extends Fragment
{
    private FragmentSearchPartnerBinding mainBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        mainBinding = FragmentSearchPartnerBinding.inflate(inflater);
        mainBinding.swipeBlock.setColorSchemeColors( ContextCompat.getColor(getContext(), R.color.dark_green) );

        return mainBinding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        String url = "https://tennis-play.com/2/";
        WebSettings settings = mainBinding.webView.getSettings();

        mainBinding.swipeBlock.setRefreshing(true);

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);

        mainBinding.webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                mainBinding.swipeBlock.setRefreshing(false);
                super.onPageStarted(view, url, favicon);
            }
        });

        mainBinding.webView.loadUrl(url);

        mainBinding.swipeBlock.setOnRefreshListener(() ->
        {
            mainBinding.webView.loadUrl(url);
            mainBinding.swipeBlock.setRefreshing(false);
        });
    }
}