package com.tele.costa.app.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tele.costa.app.R;

import java.util.List;

public class ClienteAdapter extends ArrayAdapter<ClienteModel> {
    List<ClienteModel> list;
    Context context;

    public ClienteAdapter(@NonNull Context context, int resource, @NonNull List<ClienteModel> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.row, null);
        TextView txtNombre = (TextView) view.findViewById(R.id.textFilaNombre);
        TextView txtDireccion = (TextView) view.findViewById(R.id.textFilaDireccion);
        TextView txtFecha = (TextView) view.findViewById(R.id.textFilafecha);
        txtNombre.setText(list.get(position).nombre);
        txtDireccion.setText(list.get(position).direccion);
        txtFecha.setText(list.get(position).fecha_pago);
        return  view;
        //return super.getView(position, convertView, parent);
    }
}
