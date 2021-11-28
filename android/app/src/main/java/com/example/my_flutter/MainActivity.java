package com.example.my_flutter;

import android.os.Build;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;

import androidx.annotation.NonNull;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        final MethodChannel channel = new MethodChannel(flutterEngine.getDartExecutor(), "com.example.my_flutter");
        channel.setMethodCallHandler(handler);
    }

    private MethodChannel.MethodCallHandler handler = (methodCall, result) -> {
        if (methodCall.method.equals("getPlatformVersion")) {
            result.success("Android Version: " + Build.VERSION.RELEASE);
        } else if (methodCall.method.equals("getBatteryLevel")) {
						int batteryLevel = getBatteryLevel();

						if (batteryLevel != -1) {
								result.success(batteryLevel);
						} else {
								result.error("UNAVAILABLE", "Battery level not available.", null);
						}
				} else {
            result.notImplemented();
        }
    };

		private int getBatteryLevel() {
			int batteryLevel = -1;
			if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
				BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
				batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
			} else {
				Intent intent = new ContextWrapper(getApplicationContext()).
						registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
				batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) /
						intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			}
		
			return batteryLevel;
		}
}