package com.upstreamengineering;

import android.app.Activity;

import com.usaepay.middleware.interfaces.OnLog;
import com.usaepay.middleware.publicclasses.TerminalConfig;
import com.usaepay.middleware.publicclasses.UEMiddleware;
import com.usaepay.middleware.publicclasses.UEMiddlewareInterface;

import java.util.HashMap;

/**
 * Copied and modified from MP200_public_demo
 *
 * TransactionHelper SubClass
 *
 * Implements callback methods called during a transaction.
 */
class TransactionHelper {

    private UEMiddleware ueMiddleware;
    private Activity parent;

    /**
     *  Fields to initialize UsaEpay Helper
     */
    private String mConnType;
    private String sourceKey;
    private String pin;
    private String host;
    private HashMap<String, String> transInfo;
    private UEMiddlewareInterface ueMiddlewareInterface;
    private OnLog onLog;

    /**
     * Constructor
     */
    TransactionHelper(
            Activity parent,
            String mConnType,
            String sourceKey,
            String pin,
            String host,
            HashMap<String, String> transInfo,
            OnLog onLog
    ) {
        if (parent == null) {
            throw new IllegalArgumentException("parent is null");
        }

        if (mConnType == null) {
            throw new IllegalArgumentException("mConnType is null");
        }

        if (sourceKey == null) {
            throw new IllegalArgumentException("sourceKey is null");
        }

        if (pin == null) {
            throw new IllegalArgumentException("pin is null");
        }

        if (host == null) {
            throw new IllegalArgumentException("host is null");
        }

        if (transInfo == null) {
            throw new IllegalArgumentException("transInfo is null");
        }

        if (onLog == null) {
            throw new IllegalArgumentException("onLog is null");
        }

        this.parent = parent;
        this.mConnType = mConnType;
        this.sourceKey = sourceKey;
        this.pin = pin;
        this.host = host;
        this.transInfo = transInfo;
        this.onLog = onLog;

    }

    /**
     * Drives the transaction process.
     */
    void connect() {
        ueMiddleware = new UEMiddleware("MP200", parent, ueMiddlewareInterface, onLog);

        // Set Terminal Config
        TerminalConfig tc = TerminalConfig.getTipAdjustConfig();
        //TerminalConfig tc = TerminalConfig.getAllCVMConfig();
        //tc.setEnable_debit_msr(true);

        ueMiddleware.setTerminalConfig(tc);

        try {
            // Connect to device.
            ueMiddleware.connect(mConnType, sourceKey, pin, host);
        } catch (Exception e) {
            e.printStackTrace();
            onLog.onLogTrace(e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Explicitly disconnects the device from phone/tablet.
     */
    void disconnect() {
        ueMiddleware.disconnect();
    }

    void startTransaction() {
        ueMiddleware.startTransaction(transInfo, sourceKey, pin, false, false);
    }

    void cancelTransaction() {
        ueMiddleware.cancelTransaction();
    }

    void returnPartialAuthDecision() {
        // Tell the middleware you want to keep the transaction (onTransactionComplete will then be called)
        ueMiddleware.returnPartialAuthDecision(true);
    }

    void setUeMiddlewareInterface(UEMiddlewareInterface ueMiddlewareInterface) {
        if (ueMiddlewareInterface == null) {
            throw new IllegalArgumentException("ueMiddlewareInterface is null");
        }

        this.ueMiddlewareInterface = ueMiddlewareInterface;
    }
}
