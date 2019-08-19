/*
 * MIT License (MIT)
 * Copyright (c) 2018
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.example.activeledgersdk.event;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.example.activeledgersdk.API.HttpClient;
import com.here.oksse.ServerSentEvent;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class SSEUtil {

    static List<ServerSentEvent> openEvents = null;
    private static SSEUtil instance;
    public MutableLiveData<Event> eventLiveData = new MutableLiveData<>();

    public static SSEUtil getInstance() {
        if (instance == null) {
            instance = new SSEUtil();
            openEvents = new ArrayList<>();
        }
        return instance;
    }

    public String createURL(String protocol, String ip, String port, String api) {

        String url = protocol + "://" + ip + ":" + port + api;

        return url;
    }

    public ServerSentEvent subscribeToEvent(String protocol, String ip, String port, String api, ServerEventListener listener) {
        if (listener == null) {
            listener = createLister();
        }
        String url = createURL(protocol, ip, port, api);
        ServerSentEvent ssevent = HttpClient.getInstance().subscribeToEvent(url, listener);
        openEvents.add(ssevent);
        return ssevent;
    }


    public ServerEventListener createLister() {

        //should be the users resp to close it
        return new ServerEventListener() {
            @Override
            public void onOpen(ServerSentEvent sse, Response response) {
                // When the channel is opened
                Log.d("SSE EVENT", "onOpen response" + response);
            }

            @Override
            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
                // When a message is received
                Log.d("SSE EVENT", "onMessage message" + message);
                Event incomming_event = new Event(sse, id, event, message);
                eventLiveData.postValue(incomming_event);

            }

            @WorkerThread
            @Override
            public void onComment(ServerSentEvent sse, String comment) {
                // When a comment is received
                Log.d("SSE EVENT", "onComment comment" + comment);

            }

            @WorkerThread
            @Override
            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
                Log.d("SSE EVENT", "onRetryTime milliseconds" + milliseconds);
                return true;

            }

            @WorkerThread
            @Override
            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
                Log.d("SSE EVENT", "onRetryError response" + response);

                return true; // True to retry, false otherwise
            }

            @WorkerThread
            @Override
            public void onClosed(ServerSentEvent sse) {
                Log.d("SSE EVENT", "onClosed ");
                // Channel closed
            }

            @Override
            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
                Log.d("SSE EVENT", "onPreRetry originalRequest" + originalRequest);
                return null;
            }
        };
    }


    public void closeEvents() {
        for (int i = 0; i < openEvents.size(); i++) {
            openEvents.get(i).close();
        }
    }

}
