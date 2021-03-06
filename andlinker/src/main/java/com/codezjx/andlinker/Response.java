package com.codezjx.andlinker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by codezjx on 2017/9/13.<br/>
 */
final class Response implements Parcelable {
    
    static final int STATUS_CODE_SUCCESS = 200;
    static final int STATUS_CODE_ILLEGAL_ACCESS = 400;
    static final int STATUS_CODE_INVOCATION_FAIL = 401;
    static final int STATUS_CODE_NOT_FOUND = 404;
    
    private int mStatusCode;
    private String mStatusMessage;
    private Object mResult;

    Response(int statusCode, String statusMessage, Object result) {
        mStatusCode = statusCode;
        mStatusMessage = statusMessage;
        mResult = result;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mStatusCode);
        dest.writeString(mStatusMessage);
        dest.writeValue(mResult);
    }

    private Response(Parcel in) {
        mStatusCode = in.readInt();
        mStatusMessage = in.readString();
        mResult = in.readValue(getClass().getClassLoader());
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel source) {
            return new Response(source);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };

    int getStatusCode() {
        return mStatusCode;
    }

    String getStatusMessage() {
        return mStatusMessage;
    }

    Object getResult() {
        return mResult;
    }

    @Override
    public String toString() {
        return "Response{" +
                "mStatusCode=" + mStatusCode +
                ", mStatusMessage='" + mStatusMessage + '\'' +
                ", mResult=" + mResult +
                '}';
    }
}
