package com.example.hp.chatapplication.VideoCall.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.example.hp.chatapplication.R;
import com.example.hp.chatapplication.VideoCall.utils.PermissionsChecker;

public class PermissionsActivity extends BaseActivity {

    private static final int PERMISSION_REQUEST_CODE = 0;
    private static final String EXTRA_PERMISSIONS = "extraPermissions";
    private static final String CHECK_ONLY_AUDIO = "checkAudio";
    private PermissionsChecker checker;
    private boolean requiresCheck;

    public static void startActivity(Activity activity, boolean checkOnlyAudio, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        intent.putExtra(CHECK_ONLY_AUDIO, checkOnlyAudio);
        ActivityCompat.startActivity(activity, intent, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("This Activity needs to be launched using the static startActivityForResult() method.");
        }
        setContentView(R.layout.activity_permissions);

        checker = new PermissionsChecker(this);
        requiresCheck = true;
    }

    @Override
    protected View getSnackbarAnchorView() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requiresCheck) {
            checkPermissions();
        } else {
            requiresCheck = true;
        }
    }

    private void checkPermissions() {
        String[] permissions = getPermissions();
        boolean checkOnlyAudio = getCheckOnlyAudio();

        if (checkOnlyAudio) {
            checkPermissionAudio(permissions[1]);
        } else {
            checkPermissionAudioVideo(permissions);
        }
    }

    private void checkPermissionAudio(String audioPermission) {
        if (checker.lacksPermissions(audioPermission)) {
            requestPermissions(audioPermission);
        } else {
            allPermissionsGranted();
        }
    }

    private void checkPermissionAudioVideo(String[] permissions) {
        if (checker.lacksPermissions(permissions)) {
            requestPermissions(permissions);
        } else {
            allPermissionsGranted();
        }
    }

    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }

    private boolean getCheckOnlyAudio() {
        return getIntent().getBooleanExtra(CHECK_ONLY_AUDIO, false);
    }

    private void requestPermissions(String... permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    private void allPermissionsGranted() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            requiresCheck = true;
            allPermissionsGranted();
        } else {
            requiresCheck = false;
            showDeniedResponse(grantResults);
            finish();
        }
    }

    private void showDeniedResponse(int[] grantResults) {
        if (grantResults.length > 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != 0) {
                    longToast(getString(R.string.permission_unavailable, permissionFeatures.values()[i]));
                }
            }
        } else {
            longToast(getString(R.string.permission_unavailable, permissionFeatures.MICROPHONE));
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private enum permissionFeatures {
        CAMERA,
        MICROPHONE
    }
}
