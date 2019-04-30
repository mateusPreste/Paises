package com.example.paises.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.widget.Toast;

import com.example.paises.Model.Countries;
import com.example.paises.R;
import com.example.paises.Util.Http;
import com.example.paises.Util.HttpRetro;
import com.example.paises.dao.Repositorio;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private Adapter adapter;
    private List<Countries> contryList;
    private ListView listView;
    private SwipeRefreshLayout swiperefresh;
    Repositorio db;
    public int index;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
        this.index = index;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        //final TextView textView = root.findViewById(R.id.section_label);
        /**
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        **/

        swiperefresh = (SwipeRefreshLayout) root.findViewById((R.id.swiperefresh));
        //seta cores
        swiperefresh.setColorScheme(R.color.colorPrimary, R.color.colorAccent);
        swiperefresh.setOnRefreshListener(this);

        listView = (ListView) root.findViewById(R.id.listView);

        contryList = new ArrayList<Countries>();

        adapter = new Adapter(getContext(), contryList);

        db = new Repositorio(getContext());
        getDataRetro();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), contryList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }


    // chama AsyncTask para requisicao das ubs
    public void getDataHttp () {
        CountryTask mTask = new CountryTask();
        mTask.execute();
    }

    public Boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
        return false;
    }


    private void getDataSqlite(int index) {
        contryList.clear();
        contryList.addAll(db.listarPaises(index));
        adapter.notifyDataSetChanged();
    }

    private int getIndex(){
        return this.index;
    }

    public void getDataRetro() {

        swiperefresh.setRefreshing(true);

        // se tiver conexao faz get, senao pega do sqlite
        if (isConnected()) {
            HttpRetro.getCountryClient().getUbs().enqueue(new Callback<List<Countries>>() {
                public void onResponse(Call<List<Countries>> call, Response<List<Countries>> response) {
                    if (response.isSuccessful()) {
                        List<Countries> countryBody = response.body();
                        contryList.clear();

                        db.excluirAll();

                        for (Countries country : countryBody) {
                            //}
                            if(getIndex() == 2){
                                if(country.region.equals("South America"))
                                contryList.add(country);
                            } else{
                                contryList.add(country);

                            }
                            db.inserir(country);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        System.out.println(response.errorBody());
                    }
                    swiperefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<List<Countries>> call, Throwable t) {
                    t.printStackTrace();

                }

            });

        }else {
            swiperefresh.setRefreshing(false);
            Toast.makeText(getContext(),"Sem Conexão, listando Ubs do banco...",Toast.LENGTH_SHORT).show();
            getDataSqlite(index);

        }

    }



    void hasPermission(){
        //pede permissao de localizacao
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // ja pediu permissao?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                // solicita permissao de localizacao
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    @Override
    public void onRefresh() {
        getDataRetro();
    }

    class CountryTask extends AsyncTask<Void, Void, List<Countries>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swiperefresh.setRefreshing(true);
        }

        @Override
        protected List<Countries> doInBackground(Void... voids) {
            return Http.carregarCountryJson();
        }

        @Override
        protected void onPostExecute(List<Countries> ubs) {
            super.onPostExecute(ubs);
            if (ubs != null) {
                contryList.clear();
                contryList.addAll(ubs);
                adapter.notifyDataSetChanged();
            }
            swiperefresh.setRefreshing(false);
        }
    }
}