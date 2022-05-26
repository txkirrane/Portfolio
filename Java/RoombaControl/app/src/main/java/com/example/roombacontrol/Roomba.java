package com.example.roombacontrol;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.roombacontrol.enums.Direction;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Roomba {

    // IPv4 address of the roomba
    private InetAddress address;

    // Current direction of the roomba
    private Direction currentDirection = Direction.STOPPED;

    // User-defined nickname of Roomba
    private String nickname;

    // Generate random UUID for roomba
    private UUID roombaID = UUID.randomUUID();

    /**
     * Creates a new roomba object
     * @param address String object holding the IP of the roomba
     * @param nickname Friendly name for the roomba
     */
    public Roomba(String address, String nickname) throws UnknownHostException {

        // Get IP from address string
        this.address = InetAddress.getByName(address);

        // Set friendly name
        this.nickname = nickname;

    }

    /**
     * Creates a new roomba object
     * @param address InetAddress object holding the IP of the roomba
     * @param nickname Friendly name for the roomba
     */
    public Roomba(InetAddress address, String nickname){

        // Set address
        this.address = address;

        // Set nickname
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * This constructor uses 192.168.94.100 as an address,
     * which is the default static address for the Roomba
     * @param nickname Friendly name for the roomba
     */
    public Roomba(String nickname){

        // Create new InetAddress object
        try {
            this.address = InetAddress.getByName("192.168.94.100");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Set nickname
        this.nickname = nickname;
    }

    public String getAddress(){
        return this.address.toString();
    }

    public StringRequest setDirection(Direction direction){

        // Sets the direction of the Roomba, creates a new StringRequest object

        /**
         * ~~~ CODE EXHIBIT FOR PRESENTATION ~~~
         */

        // Set currentDirection
        this.currentDirection = direction;

        // Create new StringRequest
        StringRequest request = new StringRequest(
                Request.Method.POST,
                "http:/" + address.toString(),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Set custom headers
                Map<String, String>  params = new HashMap<String, String>();
                params.put("direction", currentDirection.toString());
                System.out.println(currentDirection.toString());
                return params;
            }
        };

        // Return created request
        return request;
    }
}
