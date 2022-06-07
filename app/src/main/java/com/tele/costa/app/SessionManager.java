package com.tele.costa.app;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("AppKey", 0);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void setLogin(boolean login) {
        editor.putBoolean("KEY_LOGIN", login);
        editor.commit();
    }

    public boolean getLogin() {
        return sharedPreferences.getBoolean("KEY_LOGIN", false);
    }

    public void setUsuario(String usuario) {
        editor.putString("KEY_USERNAME", usuario);
        editor.commit();
    }

    public String getUsuario(){
        return  sharedPreferences.getString("KEY_USERNAME", "");
    }

    public void setIdUsuario(Integer idusuario) {
        editor.putInt("KEY_IDUSERNAME", idusuario);
        editor.commit();
    }

    public Integer getIdUsuario(Object o){
        return  sharedPreferences.getInt("KEY_IDUSERNAME", 0);
    }

    public void setIdMunicipio(Integer idmunicipio) {
        editor.putInt("KEY_IDMUNICIPIO", idmunicipio);
        editor.commit();
    }

    public Integer getIdMunicipio(){
        return  sharedPreferences.getInt("KEY_IDMUNICIPIO", 0);
    }

    public void setRoot(boolean root) {
        editor.putBoolean("KEY_ROOT", root);
        editor.commit();
    }

    public boolean getRoot(){
        return  sharedPreferences.getBoolean("KEY_ROOT", false);
    }

}
