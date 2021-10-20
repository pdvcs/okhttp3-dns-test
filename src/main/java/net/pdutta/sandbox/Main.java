package net.pdutta.sandbox;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main (String[] args) {
        try {
            printUrlContents();
        } catch (IOException e) {
            System.out.println("main: error: " + e + "\n");
            e.printStackTrace();
        }
    }

    public static void printUrlContents() throws IOException {

        CustomDns myDns = new CustomDns();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.dns(myDns);
        OkHttpClient client = builder.build();

        // OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASE_URL + "/api/people/1")
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        System.out.println("response.code = " + response.code());
        System.out.println("response.body = \n" + Objects.requireNonNull(response.body()).string());
        response.close();
    }

    public static final String BASE_URL = "https://swapi.dev";
}

class CustomDns implements Dns {
    @NotNull
    public List<InetAddress> lookup(String hostname) {
        System.out.println("CustomDns: received request to resolve: " + hostname);
        InetAddress addr = null;
        try {
            assert(hostname.equals("swapi.dev"));
            addr = InetAddress.getByName("18.197.142.196");
            // addr = InetAddress.getByName("127.0.0.1"); // forces an error
        } catch (UnknownHostException e) {
            System.out.println("lookup: error: " + e + "\n");
            e.printStackTrace();
        }
        ArrayList<InetAddress> addresses = new ArrayList<>();
        addresses.add(addr);

        return addresses;
    }
}
