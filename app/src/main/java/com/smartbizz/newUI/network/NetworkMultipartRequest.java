package com.smartbizz.newUI.network;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.smartbizz.R;
import com.smartbizz.Util.MyLogger;
import com.smartbizz.newUI.pojo.DataPart;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NetworkMultipartRequest extends BaseRequest {
    private final String TAG = getClass().getSimpleName();

    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();
    private DataPart dataPart;
    private Map<String,String> params;

    public NetworkMultipartRequest(Context context, int method, String url, Map<String,String> params, DataPart dataPart, Response.Listener<NetworkResponse>  responseListener) {
        super(method, url, null);
        mContext = context;
        MyLogger.debug("===URL:: " + url);
        this.mListener = responseListener;
        this.dataPart = dataPart;
        this.params = params;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // populate data byte payload
            Map<String, DataPart> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
            }


            // populate text payload
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, getParamsEncoding());
            }

            // close multipart form data after text and file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    /**
     * Custom method handle data payload.
     *
     * @return Map data part label with data byte
     * @throws AuthFailureError
     */
    protected Map<String, DataPart> getByteData() {
        Map<String, DataPart> map = new HashMap<>();
        map.put("logo",dataPart);
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put(ApiConstants.Header.CONNECTION, "Keep-Alive");
       // headers.put(ApiConstants.Header.CONTENT_LENGTH, String.valueOf(entity.getContentLength()));
      //  headers.put(ApiConstants.Header.CONTENT_TYPE, ApiConstants.Header.APPLICATION_JSON);
//        String token = PreferenceManager.getString(mContext, Constants.PrefKeys.AUTH_TOKEN);
//        if (!TextUtils.isEmpty(token)) {
//            headers.put(ApiConstants.Header.AUTHORIZATION, "Bearer "+token);
//
//        }
        MyLogger.debug("HEADER:: " + new JSONObject(headers).toString());
        return headers;
    }


    @Override
    protected Response<NetworkResponse> parseNetworkResponse(com.android.volley.NetworkResponse response) {
        final NetworkResponse networkResponse = new NetworkResponse();
        networkResponse.setSuccess(false);
        networkResponse.setStatusCode(response.statusCode);
        try {
            final String responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            MyLogger.info("==RESPONSE Image:: " + responseString);
            if (!TextUtils.isEmpty(responseString)) {
                final JSONObject responseObject = new JSONObject(responseString);
                int status = responseObject.optInt(ApiConstants.RootKeys.STATUS);
                int isEditable = responseObject.optInt(ApiConstants.RootKeys.IS_EDITABLE_FLAG);
                networkResponse.setStatus(status);
                networkResponse.setSuccess(status == 1);
                networkResponse.setEditable(isEditable==1);
                networkResponse.setResponse(responseObject);
                networkResponse.setMessage(responseObject.optString(ApiConstants.RootKeys.MESSAGE));


            }
        } catch (Exception e) {
            MyLogger.error(TAG, e);
        }
        return Response.success(networkResponse, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }

    }
    @Override
    public void deliverError(VolleyError error) {
        final NetworkResponse networkResponse = new NetworkResponse();
        networkResponse.setSuccess(false);
        int statusCode = 0;
        String errorMessage = null;
        if (error != null) {
            if (error instanceof NoConnectionError) {
                errorMessage = mContext.getString(R.string.error_message_unable_to_connect);
            } else if (error instanceof TimeoutError) {
                errorMessage = mContext.getString(R.string.error_message_request_timeout);
            } else if (error.networkResponse != null && error.networkResponse.data != null) {
                String errorData = new String(error.networkResponse.data);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(errorData);
                    errorMessage = jsonObject.optString("errorMessage");
                } catch (Exception e) {
                    MyLogger.error(TAG, e);
                }

                networkResponse.setResponse(jsonObject);
                networkResponse.setMessage(errorMessage);
                statusCode = error.networkResponse.statusCode;
                networkResponse.setStatusCode(statusCode);
            }
        }
        if (TextUtils.isEmpty(errorMessage)) {
            networkResponse.setMessage(getErrorMessageForCode(statusCode));
        } else {
            networkResponse.setMessage(errorMessage);
        }
        mListener.onResponse(networkResponse);
    }

    /**
     * Parse string map into data output stream by key and value.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param params           string inputs collection
     * @param encoding         encode the inputs, default UTF-8
     * @throws IOException
     */
    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                MyLogger.debug("==Params::"+entry.getKey(),"="+entry.getValue());
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    /**
     * Parse data into data output stream.
     *
     * @param dataOutputStream data output stream handle file attachment
     * @param data             loop through data
     * @throws IOException
     */
    private void dataParse(DataOutputStream dataOutputStream, Map<String, DataPart> data) throws IOException {
        for (Map.Entry<String, DataPart> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    /**
     * Write string data into header and data output stream.
     *
     * @param dataOutputStream data output stream handle string parsing
     * @param parameterName    name of input
     * @param parameterValue   value of input
     * @throws IOException
     */
    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    /**
     * Write data file into header and data output stream.
     *
     * @param dataOutputStream data output stream handle data parsing
     * @param dataFile         data byte as DataPart from collection
     * @param inputName        name of data input
     * @throws IOException
     */
    private void buildDataPart(DataOutputStream dataOutputStream, DataPart dataFile, String inputName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                inputName + "\"; filename=\"" + dataFile.getFileName() + "\"" + lineEnd);
        if (dataFile.getType() != null && !dataFile.getType().trim().isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.getType() + lineEnd);
        }
        dataOutputStream.writeBytes(lineEnd);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataFile.getContent());
        int bytesAvailable = fileInputStream.available();

        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(lineEnd);
    }
}
