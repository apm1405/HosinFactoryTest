/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Documents and Settings\\Administrator\\×ÀÃæ\\Hosin_FactoryTest\\HosinFactoryTest\\src\\com\\mediatek\\FMRadio\\MyAIDL.aidl
 */
package com.mediatek.FMRadio;
public interface MyAIDL extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.mediatek.FMRadio.MyAIDL
{
private static final java.lang.String DESCRIPTOR = "com.mediatek.FMRadio.MyAIDL";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.mediatek.FMRadio.MyAIDL interface,
 * generating a proxy if needed.
 */
public static com.mediatek.FMRadio.MyAIDL asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.mediatek.FMRadio.MyAIDL))) {
return ((com.mediatek.FMRadio.MyAIDL)iin);
}
return new com.mediatek.FMRadio.MyAIDL.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
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
case TRANSACTION_setFrequency:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
this.setFrequency(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.mediatek.FMRadio.MyAIDL
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void setFrequency(float frequency) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(frequency);
mRemote.transact(Stub.TRANSACTION_setFrequency, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_setFrequency = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void setFrequency(float frequency) throws android.os.RemoteException;
}
