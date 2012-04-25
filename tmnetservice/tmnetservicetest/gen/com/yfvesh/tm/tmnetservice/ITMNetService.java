/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\telematics_for_yfve\\tmnetservice\\tmnetservicetest\\src\\com\\yfvesh\\tm\\tmnetservice\\ITMNetService.aidl
 */
package com.yfvesh.tm.tmnetservice;
public interface ITMNetService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.yfvesh.tm.tmnetservice.ITMNetService
{
private static final java.lang.String DESCRIPTOR = "com.yfvesh.tm.tmnetservice.ITMNetService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.yfvesh.tm.tmnetservice.ITMNetService interface,
 * generating a proxy if needed.
 */
public static com.yfvesh.tm.tmnetservice.ITMNetService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.yfvesh.tm.tmnetservice.ITMNetService))) {
return ((com.yfvesh.tm.tmnetservice.ITMNetService)iin);
}
return new com.yfvesh.tm.tmnetservice.ITMNetService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setSimNum:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.setSimNum(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isUserLogged:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isUserLogged();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_userLogin:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _result = this.userLogin(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_cancelUserLogActionReq:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.cancelUserLogActionReq(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_userLogout:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.userLogout(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_gpsUpload:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.yfvesh.tm.tmnetservice.data.TMLocation _arg1;
if ((0!=data.readInt())) {
_arg1 = com.yfvesh.tm.tmnetservice.data.TMLocation.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
int _result = this.gpsUpload(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sendVehicleStatus:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _result = this.sendVehicleStatus(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getTMC:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _result = this.getTMC(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getWeatherByCity:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
java.lang.String _arg1;
_arg1 = data.readString();
int _result = this.getWeatherByCity(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getWeatherByLoc:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
double _arg1;
_arg1 = data.readDouble();
double _arg2;
_arg2 = data.readDouble();
int _result = this.getWeatherByLoc(_arg0, _arg1, _arg2);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_sendVehicleData:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
com.yfvesh.tm.tmnetservice.data.TMVehicleData _arg1;
if ((0!=data.readInt())) {
_arg1 = com.yfvesh.tm.tmnetservice.data.TMVehicleData.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
int _result = this.sendVehicleData(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.yfvesh.tm.tmnetservice.ITMNetService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/* set the simcard num needs for all net request */
public boolean setSimNum(java.lang.String simnum) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(simnum);
mRemote.transact(Stub.TRANSACTION_setSimNum, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* get current user login status */
public boolean isUserLogged() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isUserLogged, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* login to remote server */
public int userLogin(int id, java.lang.String username, java.lang.String password) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
_data.writeString(username);
_data.writeString(password);
mRemote.transact(Stub.TRANSACTION_userLogin, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* cancel login request */
public int cancelUserLogActionReq(int id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
mRemote.transact(Stub.TRANSACTION_cancelUserLogActionReq, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* logout from remote server */
public int userLogout(int id, java.lang.String username) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
_data.writeString(username);
mRemote.transact(Stub.TRANSACTION_userLogout, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* gps data upload to remote */
public int gpsUpload(int id, com.yfvesh.tm.tmnetservice.data.TMLocation tmloc) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
if ((tmloc!=null)) {
_data.writeInt(1);
tmloc.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_gpsUpload, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* upload vehicle working status */
public int sendVehicleStatus(int id, int status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
_data.writeInt(status);
mRemote.transact(Stub.TRANSACTION_sendVehicleStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* get TMC info */
public int getTMC(int id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
mRemote.transact(Stub.TRANSACTION_getTMC, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* get weather by citycode */
public int getWeatherByCity(int id, java.lang.String citycode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
_data.writeString(citycode);
mRemote.transact(Stub.TRANSACTION_getWeatherByCity, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* get weather report by lat/longt */
public int getWeatherByLoc(int id, double lat, double longt) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
_data.writeDouble(lat);
_data.writeDouble(longt);
mRemote.transact(Stub.TRANSACTION_getWeatherByLoc, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/* send */
public int sendVehicleData(int id, com.yfvesh.tm.tmnetservice.data.TMVehicleData vehicledata) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(id);
if ((vehicledata!=null)) {
_data.writeInt(1);
vehicledata.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_sendVehicleData, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setSimNum = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_isUserLogged = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_userLogin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_cancelUserLogActionReq = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_userLogout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_gpsUpload = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_sendVehicleStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getTMC = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getWeatherByCity = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_getWeatherByLoc = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_sendVehicleData = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
}
/* set the simcard num needs for all net request */
public boolean setSimNum(java.lang.String simnum) throws android.os.RemoteException;
/* get current user login status */
public boolean isUserLogged() throws android.os.RemoteException;
/* login to remote server */
public int userLogin(int id, java.lang.String username, java.lang.String password) throws android.os.RemoteException;
/* cancel login request */
public int cancelUserLogActionReq(int id) throws android.os.RemoteException;
/* logout from remote server */
public int userLogout(int id, java.lang.String username) throws android.os.RemoteException;
/* gps data upload to remote */
public int gpsUpload(int id, com.yfvesh.tm.tmnetservice.data.TMLocation tmloc) throws android.os.RemoteException;
/* upload vehicle working status */
public int sendVehicleStatus(int id, int status) throws android.os.RemoteException;
/* get TMC info */
public int getTMC(int id) throws android.os.RemoteException;
/* get weather by citycode */
public int getWeatherByCity(int id, java.lang.String citycode) throws android.os.RemoteException;
/* get weather report by lat/longt */
public int getWeatherByLoc(int id, double lat, double longt) throws android.os.RemoteException;
/* send */
public int sendVehicleData(int id, com.yfvesh.tm.tmnetservice.data.TMVehicleData vehicledata) throws android.os.RemoteException;
}
