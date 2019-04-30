package com.example.paises.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paises.Model.Countries;
import com.example.paises.R;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.List;



public class Adapter extends ArrayAdapter<Countries> {

    public Adapter(Context context, List<Countries> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Countries country = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ubs, parent, false);
        }

        TextView tvNome = (TextView) convertView.findViewById(R.id.nome);
        TextView tvBairro = (TextView) convertView.findViewById(R.id.bairro);
        ImageView flag = (ImageView) convertView.findViewById(R.id.flag);

        tvNome.setText(country.name);
        tvBairro.setText(country.region);

        try {
            String flagName = country.name;
            flagName = Normalizer.normalize(flagName, Normalizer.Form.NFD);
            flagName = flagName.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

            flagName = flagName.replace(" ", "_");
            flagName = flagName.replace("-", "_");
            flagName = flagName.toLowerCase();

            Class res = R.drawable.class;
            Field field = res.getField(flagName);
            int drawableId = field.getInt(null);
            flag.setImageResource(drawableId);
        }
        catch (Exception e) {
            Log.e("MyTag", "Failure to get drawable id.", e);
        }

        return convertView;
    }

}
