package com.example.gd9_a_0181;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.gd9_a_0181.api.MahasiswaApi;
import com.example.gd9_a_0181.models.Mahasiswa;
import com.example.gd9_a_0181.models.MahasiswaResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AddEditActivity extends AppCompatActivity {
    private static final String[] FAKULTAS_LIST = new String[]{"FTI", "FT", "FTB", "FBE", "FISIP", "FH"};
    private static final String[] PRODI_LIST = new String[]{"Informatika", "Arsitektur", "Biologi", "Manajemen", "Ilmu Komunikasi", "Ilmu Hukum"};
    private static final String[] JENIS_KELAMIN_LIST = new String[]{"Laki-laki", "Perempuan"};

    private EditText etName, etNpm;
    private AutoCompleteTextView edFakultas, edProdi, edJenisKelamin;
    private LinearLayout layoutLoading;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Pendeklarasian request queue
        queue = Volley.newRequestQueue(this);

        etName = findViewById(R.id.et_nama);
        etNpm = findViewById(R.id.et_npm);
        edFakultas = findViewById(R.id.ed_fakultas);
        edProdi = findViewById(R.id.ed_prodi);
        edJenisKelamin = findViewById(R.id.ed_jenis_kelamin);
        layoutLoading = findViewById(R.id.layout_loading);

        ArrayAdapter<String> adapterFakultas = new ArrayAdapter<>(this, R.layout.item_list, FAKULTAS_LIST);
        edFakultas.setAdapter(adapterFakultas);

        ArrayAdapter<String> adapterProdi = new ArrayAdapter<>(this, R.layout.item_list, PRODI_LIST);
        edProdi.setAdapter(adapterProdi);

        ArrayAdapter<String> adapterJenisKelamin = new ArrayAdapter<>(this, R.layout.item_list, JENIS_KELAMIN_LIST);
        edJenisKelamin.setAdapter(adapterJenisKelamin);

        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btn_save);
        TextView tvTitle = findViewById(R.id.tv_title);
        long id = getIntent().getLongExtra("id", -1);

        if(id == -1){
            tvTitle.setText(R.string.tambah_mahasiswa);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createMahasiswa();
                }
            });
        }else{
            tvTitle.setText(R.string.edit_mahasiswa);
            getMahasiswaById(id);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateMahasiswa(id);
                }
            });
        }
    }

    private void getMahasiswaById(long id) {
        setLoading(true);

        // Membuat request baru untuk mengambil data mahasiswa berdasarkan id
        StringRequest stringRequest = new StringRequest(GET, MahasiswaApi.GET_BY_ID_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                /* Deserialiasai data dari response JSON dari API menjadi java object MahasiswaResponse menggunakan Gson */
                MahasiswaResponse mahasiswaResponse = gson.fromJson(response, MahasiswaResponse.class);

                Mahasiswa mahasiswa = mahasiswaResponse.getMahasiswaList().get(0);

                etName.setText(mahasiswa.getNama());
                etNpm.setText(mahasiswa.getNpm());
                edFakultas.setText(mahasiswa.getFakultas(), false);
                edProdi.setText(mahasiswa.getProdi(), false);
                edJenisKelamin.setText(mahasiswa.getJenisKelamin(), false);

                Toast.makeText(AddEditActivity.this, mahasiswaResponse.getMessage(), Toast.LENGTH_SHORT).show();

                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);

                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(AddEditActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            // Menambahkan header pada request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        // Menambahkan request ke request queue
        queue.add(stringRequest);
    }

    private void createMahasiswa() {
        setLoading(true);

        Mahasiswa mahasiswa = new Mahasiswa(
                etName.getText().toString(),
                etNpm.getText().toString(),
                edJenisKelamin.getText().toString(),
                edFakultas.getText().toString(),
                edProdi.getText().toString());

        // Membuat request baru untuk membuat data mahasiswa baru
        StringRequest stringRequest = new StringRequest(POST, MahasiswaApi.ADD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                /* Deserialiasai data dari response JSON dari API menjadi java object MahasiswaResponse menggunakan Gson */
                MahasiswaResponse mahasiswaResponse = gson.fromJson(response, MahasiswaResponse.class);

                Toast.makeText(AddEditActivity.this, mahasiswaResponse.getMessage(), Toast.LENGTH_SHORT).show();

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);

                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(AddEditActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            // Menambahkan header pada request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
            }

            // Menambahkan request body berupa object mahasiswa
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                /* Serialisasi data dari java object MahasiswaResponse menjadi JSON string menggunakan Gson */
                String requestBody = gson.toJson(mahasiswa);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            // Mendeklarasikan content type dari request body yang ditambahkan
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Menambahkan request ke request queue
        queue.add(stringRequest);
    }

    private void updateMahasiswa(long id) {
        setLoading(true);

        Mahasiswa mahasiswa = new Mahasiswa(
                etName.getText().toString(),
                etNpm.getText().toString(),
                edJenisKelamin.getText().toString(),
                edFakultas.getText().toString(),
                edProdi.getText().toString());

        // Membuat request baru untuk mengedit data mahasiswa
        StringRequest stringRequest = new StringRequest(PUT, MahasiswaApi.UPDATE_URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                /* Deserialiasai data dari response JSON dari API menjadi java object MahasiswaResponse menggunakan Gson */
                MahasiswaResponse mahasiswaResponse = gson.fromJson(response, MahasiswaResponse.class);

                Toast.makeText(AddEditActivity.this, mahasiswaResponse.getMessage(), Toast.LENGTH_SHORT).show();

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);

                try {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(AddEditActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(AddEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            // Menambahkan header pada request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
            }

            // Menambahkan request body berupa object mahasiswa
            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                /* Serialisasi data dari java object MahasiswaResponse menjadi JSON string menggunakan Gson */
                String requestBody = gson.toJson(mahasiswa);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            // Mendeklarasikan content type dari request body yang ditambahkan
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Menambahkan request ke request queue
        queue.add(stringRequest);
    }

    // Fungsi untuk menampilkan layout loading
    private void setLoading(boolean isLoading) {
        if (isLoading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        }else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.INVISIBLE);
        }
    }
}