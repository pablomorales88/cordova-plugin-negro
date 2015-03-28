/* global cordova, module */
"use strict";

module.exports = {
	service : 'Blue2',

	init : function(success, error){
		cordova.exec(success, error, this.service, 'init', []);
	},

	isAvailable : function(success, error){
		cordova.exec(success, error, this.service, 'isAvailable', []);
	},

	isEnabled : function(success, error){
		cordova.exec(success, error, this.service, 'isEnabled', []);
	},

	enableBluetooth : function(success, error){
		cordova.exec(success, error, this.service, 'enableBluetooth', []);
	},

	disableBluetooth : function(success, error){
		cordova.exec(success, error, this.service, 'disableBluetooth', []);
	},

	isScanning : function(success, error){
		cordova.exec(success, error, this.service, 'isScanning', []);
	},

	connect : function(success, error){
		cordova.exec(success, error, this.service, 'connect', []);
	},

	closeConection : function(success, error){
		cordova.exec(success, error, this.service, 'closeConection', []);
	},

	sendSignalOn : function(success, error){
		cordova.exec(success, error, this.service, 'sendSignalOn', []);
	},

	pato : function(success, error){
		cordova.exec(success, error, this.service, 'PAtO', []);
	}

};