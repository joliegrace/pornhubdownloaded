package com.minhnhatlpx.simplephdl;

/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.*;

public class HttpRetriever {

    public static String retrieve(String url) {
        URL targetURL;
        try {
            targetURL = new URL(url);
        } catch (MalformedURLException e) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        String response;
        try {
            urlConnection = (HttpURLConnection) targetURL.openConnection();
            urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Accept-Encoding", "gzip");
            urlConnection.setDoInput(true);
            urlConnection.connect();
            response = readStream(urlConnection);
        } catch (IOException e) {
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return response;
    }

    private static String readStream(HttpURLConnection connection) {
        BufferedReader br;
        StringBuilder builder = new StringBuilder();
        String line;
        try {
			if ("gzip".equals(connection.getContentEncoding())) {
				br = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
			}
			else {
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            //Unable to read from the stream
            return null;
        }


        return builder.toString();
    }
}
