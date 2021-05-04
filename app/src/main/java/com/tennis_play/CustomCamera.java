package com.tennis_play;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.common.util.concurrent.ListenableFuture;
import com.tennis_play.databinding.CustomCameraLayoutBinding;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CustomCamera extends AppCompatActivity
{
    private CustomCameraLayoutBinding mainBinding;
    private ImageCapture imageCapture;
    private Executor executor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.custom_camera_layout);

        if( !this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH) )
            mainBinding.flashButton.setVisibility(View.GONE);

        setupCamera();
    }

    private void setupCamera()
    {
        ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        executor = ContextCompat.getMainExecutor(this);

        cameraProviderListenableFuture.addListener
        (
            () ->
            {
                try
                {
                    ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                    bindPreview(cameraProvider);
                }
                catch (ExecutionException | InterruptedException ignored) { }
            },
            executor
        );
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider)
    {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        preview.setSurfaceProvider( mainBinding.cameraPreview.getSurfaceProvider() );

        imageCapture = new ImageCapture.Builder()
            .setTargetRotation( mainBinding.getRoot().getDisplay().getRotation() )
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build();

        takePhoto();

        cameraProvider.unbindAll();

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, preview);
    }

    private void takePhoto()
    {
        if( imageCapture != null )
        {
            mainBinding.takePictureButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback()
                    {
                        @Override
                        public void onCaptureSuccess(@NonNull ImageProxy image)
                        {
                            Log.e("Tennis_play", "Capture image");
                            super.onCaptureSuccess(image);
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException exception)
                        {
                            Log.e("Tennis_play", "Capture image error" + exception.getMessage());
                            super.onError(exception);
                        }
                    });
                }
            });
        }
    }
}
