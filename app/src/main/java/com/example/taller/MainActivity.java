package com.example.taller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Estudiante> estudiantes = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        cargarEstudiantesDesdeArchivo();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCrear = findViewById(R.id.btnCrear);
        Button btnLeer = findViewById(R.id.btnLeer);
        Button btnActualizar = findViewById(R.id.btnActualizar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        Button btnCalculadora = findViewById(R.id.btnCalculadora);

        cargarEstudiantesDesdeArchivo();

        btnCrear.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateActivity.class)));
        btnLeer.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ReadActivity.class)));
        btnActualizar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UpdateActivity.class)));
        btnEliminar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DeleteActivity.class)));

        // Listener para abrir la calculadora
        btnCalculadora.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CalculadoraActivity.class);
            startActivity(intent);
        });
    }

    public void cargarEstudiantesDesdeArchivo() {
        try {
            FileInputStream fis = openFileInput("estudiantes.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String linea;
            Estudiante estudianteActual = null;

            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("Cédula: ")) {
                    if (estudianteActual != null) {
                        estudiantes.add(estudianteActual); // Agregar el estudiante previo a la lista
                    }
                    estudianteActual = new Estudiante();
                    estudianteActual.setCedula(linea.replace("Cédula: ", "").trim());
                } else if (linea.startsWith("Nombre: ")) {
                    estudianteActual.setNombre(linea.replace("Nombre: ", "").trim());
                } else if (linea.startsWith("Teléfono: ")) {
                    estudianteActual.setTelefono(linea.replace("Teléfono: ", "").trim());
                } else if (linea.startsWith("Correo: ")) {
                    estudianteActual.setCorreo(linea.replace("Correo: ", "").trim());
                } else if (linea.startsWith("Género: ")) {
                    estudianteActual.setGenero(linea.replace("Género: ", "").trim());
                } else if (linea.startsWith("Carrera: ")) {
                    estudianteActual.setCarrera(linea.replace("Carrera: ", "").trim());
                } else if (linea.startsWith("Semestre: ")) {
                    estudianteActual.setSemestre(Integer.parseInt(linea.replace("Semestre: ", "").trim()));
                } else if (linea.startsWith("Universidad: ")) {
                    estudianteActual.setUniversidad(linea.replace("Universidad: ", "").trim());
                } else if (linea.startsWith("Créditos: ")) {
                    estudianteActual.setCreditos(Integer.parseInt(linea.replace("Créditos: ", "").trim()));
                } else if (linea.equals("---")) {
                    if (estudianteActual != null) {
                        estudiantes.add(estudianteActual); // Agregar el último estudiante
                        estudianteActual = null;
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {

            estudiantes.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
