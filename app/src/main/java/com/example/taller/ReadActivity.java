package com.example.taller;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {
    private ListView lvEstudiantes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        lvEstudiantes = findViewById(R.id.lvEstudiantes);
        mostrarEstudiantes();
    }

    private void mostrarEstudiantes() {
        ArrayList<String> lista = new ArrayList<>();

        for (Estudiante e : MainActivity.estudiantes) {
            String estudianteInfo = String.format(
                    "Cédula: %s\nNombre: %s\nCarrera: %s\nSemestre: %d",
                    e.getCedula(), e.getNombre(), e.getCarrera(), e.getSemestre()
            );
            if (!lista.contains(estudianteInfo)) {
                lista.add(estudianteInfo);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        lvEstudiantes.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarEstudiantes();
    }
}

