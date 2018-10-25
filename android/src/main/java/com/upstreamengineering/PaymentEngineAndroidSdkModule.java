package com.upstreamengineering;

import android.widget.Toast;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.usaepay.middleware.interfaces.OnLog;
import com.usaepay.middleware.publicclasses.UEMiddlewareInterface;
import com.usaepay.middleware.struct.UEMTransactionResult;
import com.usaepay.middleware.struct.UE_ERROR;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentEngineAndroidSdkModule extends ReactContextBaseJavaModule {

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  public PaymentEngineAndroidSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "PaymentEngineAndroidSdk";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  @ReactMethod
  public void show(String message, int duration) {
    emitLog("show(): " + message);
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }

  private void emitLog(String message) {
    WritableMap params = new WritableNativeMap();
    params.putString("message", message);
    getReactApplicationContext()
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit("onLog", params);
  }

  @ReactMethod
  public void startTransaction(
          final String mConnType,
          final String sourceKey,
          final String pin,
          final String host,
          final ReadableMap transInfo,
          final Promise promise
  ) {
    try {
      emitLog("Starting Transaction...");

      final HashMap<String, String> hashTransInfo = new HashMap<>();
      final HashMap<String, Object> hashStringObjTransInfo = transInfo.toHashMap();
      for (String key : hashStringObjTransInfo.keySet()) {
        final Object obj = hashStringObjTransInfo.get(key);
        hashTransInfo.put(key, obj.toString());
      }

      emitLog("Initializing TransactionHelper... " + hashTransInfo);

      final TransactionHelper transactionHelper = new TransactionHelper(
              getCurrentActivity(),
              mConnType,
              sourceKey,
              pin,
              host,
              hashTransInfo,
              new OnLog() {
                @Override
                public void onLog(String s) {
                  emitLog("Log - " + s);
                }

                @Override
                public void onLogTrace(String s) {
                  emitLog("Trace - " + s);
                }
              }
      );


      UEMiddlewareInterface ueMiddlewareInterface = new UEMiddlewareInterface() {
        boolean resolvedPromise = false;

        @Override
        public void onConnected() {
          emitLog("onConnected()");
          transactionHelper.startTransaction();
        }

        @Override
        public void onDisconnected() {
          emitLog("onDisconnected()");
          if (!resolvedPromise) {
            resolvedPromise = true;
            promise.resolve("Complete!");
          }

        }

        @Override
        public void onTransactionComplete(UEMTransactionResult uemTransactionResult) {
          emitLog("onTransactionComplete()");
          try {
            transactionHelper.disconnect();
          } catch (Throwable e) {
            if (!resolvedPromise) {
              resolvedPromise = true;
              promise.reject(e);
            }
          }
        }

        @Override
        public void onDeviceInfoReceived(HashMap<String, String> hashMap) {
          emitLog("onDeviceInfoReceived(): " + hashMap);
        }

        @Override
        public void onError(UE_ERROR ue_error) {
          emitLog("onError(): " + ue_error.text);
          if (!resolvedPromise) {
            resolvedPromise = true;
            promise.reject(ue_error.toString(), ue_error.text);
          }
        }

        @Override
        public void onStatusChanged(String s) {
          emitLog("Status Changed - " + s);
        }

        @Override
        public void onSeePhoneNFC(HashMap<String, String> hashMap) {
          emitLog("onSeePhoneNFC(): " + hashMap);
        }

        @Override
        public void onPromptForPartialAuth(String s, HashMap<String, String> hashMap) {
          emitLog("onPromptForPartialAuth(): " + s + " " + hashMap);
          transactionHelper.returnPartialAuthDecision();
        }

        @Override
        public void onProgressBarUpdateAvailable(String s, String s1, float v) {
        }

        @Override
        public void onReceiptReceived(String s) {
          emitLog("onReceiptReceived(): " + s);
        }

        @Override
        public void onMerchantCapabilitiesReceived(JSONObject jsonObject) {
          emitLog("onMerchantCapabilitiesReceived()");
        }

        @Override
        public void onGatewayInfoReceived(JSONObject jsonObject) {
          emitLog("onGatewayInfoReceived()");
        }
      };

      transactionHelper.setUeMiddlewareInterface(ueMiddlewareInterface);
      transactionHelper.connect();
    } catch (Throwable e) {
      promise.reject(e);
    }
  }
}