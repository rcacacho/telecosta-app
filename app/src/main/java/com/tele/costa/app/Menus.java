package com.tele.costa.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AlertDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.tele.costa.app.databinding.ActivityMenusBinding;
import com.tele.costa.app.model.ClienteAdapter;
import com.tele.costa.app.model.ClienteModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Menus extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenusBinding binding;
    FloatingActionButton btnCerrarSesion;
    SessionManager sessionManager;
    private ListView list;
    private List<ClienteModel> listCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getApplicationContext());
        binding = ActivityMenusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMenus.toolbar);
        binding.appBarMenus.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menus);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        list = findViewById(R.id.listCliente);
        btnCerrarSesion = findViewById(R.id.fab);
        jsonClientes();
        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialogo de alerta
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Cerrar Sesion");
                builder.setMessage("¿Esta seguro de cerrar sesion?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sessionManager.setLogin(false);
                        sessionManager.setIdMunicipio(null);
                        sessionManager.setUsuario("");
                        sessionManager.setIdUsuario(null);
                        sessionManager.setLogin(false);
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menus, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menus);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void jsonClientes() {
        final ProgressBar progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String url = "http://telecosta.tk:8080/telecostaweb-service/rest/pagos/clientes/" + sessionManager.getRoot() + "/"
                + sessionManager.getIdUsuario() + "/" + sessionManager.getIdMunicipio();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e("Respuesta ", response.toString());

                try {
                    listCliente = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("Error", jsonArray.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ClienteModel cl = new ClienteModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        Integer idcliente = object.getInt("idcliente");
                        String codigo = object.getString("codigo");
                        String nombre = object.getString("nombre");
                        String direccion = object.getString("direccion");
                        String fecha_pago = object.getString("fecha_pago");
                        cl.setNombre(nombre);
                        cl.setCodigo(codigo);
                        cl.setDireccion(direccion);
                        cl.setIdcliente(idcliente);
                        cl.setFecha_pago(fecha_pago);
                        listCliente.add(cl);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                ClienteAdapter adapter = new ClienteAdapter(Menus.this, R.layout.row, listCliente);
                list.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}