package com.tele.costa.app;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView usu = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);
        MaterialButton btnLogin = (MaterialButton) findViewById(R.id.loginbtn);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("")) {
                    password.setError("Ingrese una contraseña");
                }


                if (!usu.getText().toString().equals(null) && !password.getText().toString().equals(null)) {
                    String MD5_Hash_String = md5(password.getText().toString());
                    String url = "http://telecosta.tk:8080/telecostaweb-service/rest/usuarios/login/" + usu.getText().toString() + "/" + MD5_Hash_String;
                    //String url = "https://pokeapi.co/api/v2/pokemon/ditto";
                    //String url = "http://172.18.143.47:8080/telecostaweb-service/rest/usuarios/login/"+usu.getText().toString()+"/"+MD5_Hash_String;

                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("usuario", usu.getText().toString());
                    parametros.put("password", MD5_Hash_String);

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                    JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Respuesta ", response.toString());
                                    sessionManager.setLogin(true);
                                    sessionManager.setUsuario(usu.getText().toString().trim());

                                    try {
                                        if (response.has("idmunicipio")) {
                                            sessionManager.setIdMunicipio(response.getJSONObject("idmunicipio").getInt("idmunicipio"));
                                            sessionManager.setRoot(response.getJSONObject("root").getBoolean("root"));
                                            sessionManager.setIdUsuario(response.getJSONObject("idusuario").getInt("idusuario"));
                                            openActivity();
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("Error: ", error.toString());
                                    Toast.makeText(getApplicationContext(),
                                            "Usuario o contraseña invalidos!", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    requestQueue.add(objectRequest);
                } else {
                    Toast.makeText(MainActivity.this, "Usuario o contraseña invalida", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void openActivity() {
        Intent intent = new Intent(this, Menus.class);
        startActivity(intent);
        finish();
    }

}