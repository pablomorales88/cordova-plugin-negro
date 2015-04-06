/*global cordova, module*/

module.exports = {
    escan: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "escan", [name]);//service= "Hello" // action="greet"
    },
    activar: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "activar", [name]);//service= "Hello" // action="greet"
    },
    desactivar: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "desactivar", [name]);//service= "Hello" // action="greet"
    },
    conectar: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "conectar", [name]);//service= "Hello" // action="greet"
    },
    desconectar: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "desconectar", [name]);//service= "Hello" // action="greet"
    },
    leer: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "leer", [name]);//service= "Hello" // action="greet"
    },
    escribir: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "escribir", [name]);//service= "Hello" // action="greet"
    },
    isEnable: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "isEnable", [name]);//service= "Hello" // action="greet"
    }




};
