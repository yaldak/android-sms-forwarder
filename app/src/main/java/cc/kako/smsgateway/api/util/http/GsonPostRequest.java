package cc.kako.smsgateway.api.util.http;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class GsonPostRequest<T> extends Request<Optional<T>> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final String json;
    private final Response.Listener<Optional<T>> listener;

    public GsonPostRequest(final String url,
                           final String json,
                           final Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.clazz = null;
        this.headers = null;
        this.json = json;
        this.listener = (response) -> { };
    }

    public GsonPostRequest(final String url,
                           final Class<T> clazz,
                           final Map<String, String> headers,
                           final String json,
                           final Response.Listener<Optional<T>> listener,
                           final Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.clazz = clazz;
        this.headers = headers;
        this.json = json;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(Optional<T> response) {
        listener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public byte[] getBody() {
        return json == null ? null : json.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    protected Response<Optional<T>> parseNetworkResponse(final NetworkResponse response) {
        // TODO: Handle errors and caching better
        Log.v("HttpDebug", new String(response.data, StandardCharsets.UTF_8));

        if (clazz == null) {
            return Response.success(Optional.empty(), HttpHeaderParser.parseCacheHeaders(response));
        }

        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            return Response.success(
                    Optional.of(gson.fromJson(json, clazz)),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
