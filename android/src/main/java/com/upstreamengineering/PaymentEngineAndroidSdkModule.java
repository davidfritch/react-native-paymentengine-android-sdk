package com.upstreamengineering;

import android.support.annotation.Nullable;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
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
//      final String mConnType,
//      final String mLog,
//      final String sourceKey,
//      final String pin,
//      final String host,
//      final ReadableMap transInfo,
//      final Promise promise

          // Just for initial test
        final String foo,
        final Promise promise
  ) {
    try {
//      emitLog("Starting Transaction...");
      emitLog("Starting Transaction...: " + foo);

//      final HashMap<String, String> hashTransInfo = new HashMap<>();
//      final HashMap<String, Object> hashStringObjTransInfo = transInfo.toHashMap();
//      for (String key : hashStringObjTransInfo.keySet()) {
//        final Object obj = hashStringObjTransInfo.get(key);
//        hashStringObjTransInfo.put(key, obj.toString());
//      }

      emitLog("Initializing TransactionHelper...");

//    final TransactionHelper transactionHelper = new TransactionHelper(
//            getCurrentActivity(),
//            mConnType,
//            mLog,
//            sourceKey,
//            pin,
//            host,
//            hashTransInfo,
//            new OnLog() {
//              @Override
//              public void onLog(String s) {
//                emitLog("Log - " + s);
//              }
//
//              @Override
//              public void onLogTrace(String s) {
//                emitLog("Trace - " + s);
//              }
//            }
//    );
//
//    UEMiddlewareInterface ueMiddlewareInterface = new UEMiddlewareInterface() {
//      @Override
//      public void onConnected() {
//
//      }
//
//      @Override
//      public void onDisconnected() {
//      }
//
//      @Override
//      public void onTransactionComplete(UEMTransactionResult uemTransactionResult) {
//        transactionHelper.disconnect();
//      }
//
//      @Override
//      public void onDeviceInfoReceived(HashMap<String, String> hashMap) {
//      }
//
//      @Override
//      public void onError(UE_ERROR ue_error) {
//      }
//
//      @Override
//      public void onStatusChanged(String s) {
//      }
//
//      @Override
//      public void onSeePhoneNFC(HashMap<String, String> hashMap) {
//      }
//
//      @Override
//      public void onPromptForPartialAuth(String s, HashMap<String, String> hashMap) {
//        transactionHelper.returnPartialAuthDecision();
//      }
//
//      @Override
//      public void onProgressBarUpdateAvailable(String s, String s1, float v) {
//      }
//
//      @Override
//      public void onReceiptReceived(String s) {
//      }
//
//      @Override
//      public void onMerchantCapabilitiesReceived(JSONObject jsonObject) {
//      }
//
//      @Override
//      public void onGatewayInfoReceived(JSONObject jsonObject) {
//      }
//    };
//    transactionHelper.setUeMiddlewareInterface(ueMiddlewareInterface);
//
//    try {
//      transactionHelper.connect();
//      transactionHelper.startTransaction();
//    } catch (Exception e) {
//      Toast.makeText(getReactApplicationContext(), "An error occurred processing the transaction", Toast.LENGTH_LONG).show();
//    }

      emitLog("Transaction Completed!");
      promise.resolve("Finished!");
    } catch (Exception e) {
      emitLog("Error: " + e.getLocalizedMessage());
      promise.reject(e);
    }
  }
}