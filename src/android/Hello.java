package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothManager;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;

public class Hello extends CordovaPlugin {
    private BluetoothAdapter b2Adapter;
    private JSONObject jsonReturn;
    private int deviceFound = 0;
    private boolean isScanning = false;

    private Activity cordova;

    private final BroadcastReceiver newDeviceReceiver;
    private final BroadcastReceiver finishScanReceiver;
    private IntentFilter newDeviceFilter;
    private IntentFilter finishScanFilter;

    private TextView debugMessage;
    
    private boolean enables = true;


    /**
     * Constructor - Instancia el adaptador bluetooth
     */
    public Hello(){
        b2Adapter = BluetoothAdapter.getDefaultAdapter();
    
        jsonReturn = new JSONObject();
        newDeviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        finishScanFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        // Activity para poder usar el plugin
        
        //cordova = cordova.this.getActivity();
        // Clase abstracta
        newDeviceReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                deviceFound++;
                // Recupero el dispositivo bluetooth desde el Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                debugMessage.append("Name:" + device.getName());

                try {
                    jsonReturn.put("Id", String.valueOf(deviceFound));
                    jsonReturn.put("Name", device.getName());
                    jsonReturn.put("UUI", device.getUuids());
                    jsonReturn.put("Address", device.getAddress());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // Clase abstracta
        finishScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                debugMessage.setText("Scan finished");
                cordova.unregisterReceiver(newDeviceReceiver);
                cordova.unregisterReceiver(this);
            }
        };
    }














    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
          b2Adapter = BluetoothAdapter.getDefaultAdapter();

        if (action.equals("escan")) 
        {
            
             if (b2Adapter.isEnabled()) {
                callbackContext.success("Bluetooth Activadp.");
            } else {
                callbackContext.success("Bluetooth desactivoad");
            }
            
            
            
            return true;

        } 
        else if (action.equals("activar")) {
            enableBluetooth();
            callbackContext.success("Activado");
            return true;
        }
        else if (action.equals("desactivar")) {
            callbackContext.success("Apretamos - Desactivar");
            return true;
        }
        else if (action.equals("conectar")) {
            callbackContext.success("Apretamos - Conectar");
            return true;
        }
         else if (action.equals("desconctar")) {
            callbackContext.success("Apretamos - Desconectar");
            return true;
        }
         else if (action.equals("leer")) {
            callbackContext.success("Apretamos - Leer");
            return true;
        }
        else if (action.equals("escribir")) {
            callbackContext.success("Apretamos - Escribir");
            return true;
        }

        else {
            
            return false;

            }
        }



        /**
     * Devuelve el estado del adaptador del dispositivo
     *
     * @return true si el dispositivo posee adaptador bluetooth
     * @return false si el dispositivo no posee adaptador bluetooth
     */
    public boolean isAvailable(){
        if(b2Adapter != null){
            return true;
        }
        return false;
    }

    /**
     * Retorna el estado del adaptador bluetooth
     *
     * @reutnr true si el adaptador bluetooth esta encendido
     * @return false si el adaptador bluetooth no esta encendido
     */
    public boolean isEnabled(){
        return b2Adapter.isEnabled();

    }

    /**
     * Envia peticion de activacion del adaptador bluetooth a Android
     */
    public void enableBluetooth(){
        if(isAvailable() && !isEnabled()){
            b2Adapter.enable();
        }
    }

    /**
     * Envia peticion de desactivacion del adaptador buetooth a Android
     */
    public void disableBluetooth(){
        if(isAvailable() && isEnabled()){
            b2Adapter.disable();
        }
    }

    /**
     * Retorna estado de escaneo
     *
     * @return true el adaptador bluetooth esta escaneando
     * @return false el adaptador bluetooth no esta escaneando
     */
    public boolean isScanning(){
        return isScanning;
    }

    /**
     * Retorma dispositivos descubiertos
     */
    public JSONObject getJsonReturn(){
        return jsonReturn;
    }

    /**
     * Escaneo de dispositivos bluetooth
     */
    public void discoverDevices(TextView debugMessage){
        if(isEnabled() && !isScanning()){
            this.debugMessage = debugMessage;
            isScanning = true;
            b2Adapter.startDiscovery();
            cordova.registerReceiver(newDeviceReceiver, newDeviceFilter);
            cordova.registerReceiver(finishScanReceiver, finishScanFilter);
        }
    }

 
    }

