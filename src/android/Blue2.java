/*
	Licensed to the Apache Software Foundation (ASF) under one
	or more contributor license agreements.  See the NOTICE file
	distributed with this work for additional information
	regarding copyright ownership.  The ASF licenses this file
	to you under the Apache License, Version 2.0 (the
	"License"); you may not use this file except in compliance
	with the License.  You may obtain a copy of the License at

	 http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an
	"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
	KIND, either express or implied.  See the License for the
	specific language governing permissions and limitations
	under the License.

	Authors: 
		PAtO Aguirre - patoman.09@gmail.com
		Pablo Morales - moralespablogaston@gmail.com
*/
package com.patoman007.cordova.blue2;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;

public class Blue2 extends CordovaPlugin {

	/**
	 * Variables
	*/
	private Activity activity;

	private BluetoothAdapter b2Adapter;
	private BluetoothDevice	b2Device;
  private BluetoothSocket b2Socket;

	private BroadcastReceiver newDeviceReceiver;
  private BroadcastReceiver finishScanReceiver;
  private IntentFilter newDeviceFilter;
  private IntentFilter finishScanFilter;

  private InputStream b2InputStream;
  private OutputStream b2OutputStream;

	private JSONObject b2Response;

	private boolean validOption;
	private boolean isScanning = false;
  private boolean isConnected = false;
  private boolean isAvailable;
  private boolean isEnabled;

  /**
   * Methods implemented
  */
  private final String INIT = "init";
  private final String ISAVAILABLE = "isAvailable";
  private final String ISENABLE = "isEnabled";
  private final String ENABLEBLUETOOTH = "enableBluetooth";
  private final String DISABLEBLUETOOTH = "disableBluetooth";
  private final String ISSCANNING = "isScanning";
  private final String CONNECT = "connect";
  private final String CLOSECONECTION = "closeConection";
  private final String WRITEDATA = "writeData";
  private final String SENDSIGNALON = "sendSignalOn";
  private final String READ = "read";
  private final String DISCOVERDEVICES = "discoverDevidces";

	/**
	* Constructor
	*/
	public Blue2(){}

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView){
		super.initialize(cordova, webView);

		activity = cordova.getActivity();

		b2Adapter = BluetoothAdapter.getDefaultAdapter();
		b2Socket = null;
		b2Device = null;

		b2Response = new JSONObject();
		newDeviceFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		finishScanFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

		// Abstract class
		newDeviceReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent){
				// Recupero el dispositivo bluetooth desde el Intent
				b2Device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				try {
					b2Response.put("Id", String.valueOf(b2Device));
					b2Response.put("Name", b2Device.getName());
					b2Response.put("Address", b2Device.getAddress());
				} catch (JSONException e){
					
				}
			}
		};

		// Abstract class
		finishScanReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent){
				isScanning = false;
				activity.unregisterReceiver(newDeviceReceiver);
				activity.unregisterReceiver(this);
			}
		};
	}

	/**
	 * Executes the request and returns PluginResult.
	 *
	 * @param action            The action to execute.
	 * @param args              JSONArry of arguments for the plugin.
	 * @param callbackContext   The callback id used when calling back into JavaScript.
	 * @return                  True if the action was valid, false if not.
	 */
	@Override
	public boolean execute(String action, CordovaArgs args, 
			CallbackContext callbackContext) throws JSONException{

		validOption = true;

		if(action.equals(INIT)){
			if(b2Adapter != null){
				isAvailable = true;
			} else {
				isAvailable = false;
			}

			if(b2Adapter.isEnabled()){
				isEnabled = true;
			} else {
				isEnabled = false;
			}

		} else if(action.equals(ISAVAILABLE)){
			if(b2Adapter != null){
				isAvailable = true;
				b2Response.put("message" , "El dispositivo posee adaptador bluetooth");
				callbackContext.success(b2Response);
			}
			isAvailable = false;
			b2Response.put("message" , "El dispositivo no posee adaptador bluetooth");
			callbackContext.error(b2Response);

		} else if(action.equals(ISENABLE)){
			if(b2Adapter.isEnabled()){
				isEnabled = true;
				b2Response.put("messsage" , "El adaptador bluetooth esta disponible");
				callbackContext.success(b2Response);
			}
			isEnabled = false;
			b2Response.put("message" , "El adaptador bluetooth no esta disponible");
			callbackContext.error(b2Response);

		} else if(action.equals(ENABLEBLUETOOTH)){
			if(isAvailable && !isEnabled){
				isEnabled = true;
				b2Adapter.enable();
				b2Response.put("message" , "Se habilito el adaptador bluetooth del dispositivo");
				callbackContext.success(b2Response);
			}
			isEnabled = false;
			b2Response.put("message" , "El adaptador ya esta activado");
			callbackContext.error(b2Response);

		} else if(action.equals(DISABLEBLUETOOTH)){
			if(isAvailable && isEnabled){
				isEnabled = false;
				b2Adapter.disable();
				b2Response.put("message" , "Se deshabilito el adaptador bluetooth");
				callbackContext.success(b2Response);
			}
			isEnabled = true;
			b2Response.put("message" , "El adaptador ya esta desactivado");
			callbackContext.error(b2Response);

		} else if(action.equals(ISSCANNING)){
			if(isScanning){
				b2Response.put("message" , "El adaptador se encuentra escaneando");
				callbackContext.success(b2Response);
			}
			b2Response.put("message" , "El adaptador no esta escaneando");
			callbackContext.error(b2Response);
		
		} else if(action.equals(CONNECT)){
			if(!isConnected){
				b2Adapter.cancelDiscovery();

				/*---	FIXME: MAC ADDRESS HARCODED	---*/
				b2Device = b2Adapter.getRemoteDevice("98:D3:31:30:22:A9");

				try{
					UUID id = b2Device.getUuids()[0].getUuid();
					b2Socket = b2Device.createRfcommSocketToServiceRecord(id);

					try{
						b2Socket.connect();
						isConnected = true;
						b2Response.put("message" , "Se conecto correctamente");
						callbackContext.success(b2Response);
					} catch(IOException connectionException) {
						try{
							b2Socket.close();
							b2Response.put("message" , "No se pudo conectar, se cierra el socket");
							callbackContext.error(b2Response);
						} catch(IOException closeException){
							b2Response.put("message" , "No se pudo cerrar el socket");
							callbackContext.error(b2Response);
						}
					}
				} catch(IOException e) {
					b2Response.put("message" , "No se pudo establecer el socket");
					callbackContext.error(b2Response);
				}
			} else {
				b2Response.put("message" , "El dispositivo ya esta conectado");
				callbackContext.error(b2Response);
			}

		} else if(action.equals(CLOSECONECTION)){
			if(b2Socket != null && isConnected){
				try{
					b2Socket.close();
					isConnected = false;
					b2Response.put("message" , "La conexion se cerro exitosamente");
					callbackContext.success(b2Response);
				} catch(IOException e) {
					b2Response.put("message" , "No se pudo cerrar la conexion");
					callbackContext.error(b2Response);
				}
			}

		} else if(action.equals(SENDSIGNALON)){
			String msg = "A";
			byte[] bytes = msg.getBytes();
			try{
				b2OutputStream = b2Socket.getOutputStream();
				b2OutputStream.write(bytes);
				b2Response.put("message" , "La señal se envio exitosamente");
				callbackContext.success(b2Response);
			} catch(IOException e){
				b2Response.put("message" , "No fue posible enviar la señal");
				callbackContext.error(b2Response);
			}

		} else if(action.equals(DISCOVERDEVICES)){
			if(isEnabled && !isScanning){
				isScanning = true;
				b2Adapter.startDiscovery();
				activity.registerReceiver(newDeviceReceiver, newDeviceFilter);
				activity.registerReceiver(finishScanReceiver, finishScanFilter);
				b2Response.put("message" , "Algo");
				callbackContext.success(b2Response);
			}
			b2Response.put("message" , "Algo");
			callbackContext.error(b2Response);

		} else if(action.equals("PAtO")){
			b2Response.put("message" , "PAtO!");
			callbackContext.success(b2Response);

		} else{
			validOption = false;
		}

		return validOption;
	}

}
