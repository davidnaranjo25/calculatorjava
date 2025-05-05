package com.example.taller;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class CreateActivity extends AppCompatActivity {
    private EditText etCedula, etNombre, etTelefono, etCorreo, etCarrera, etSemestre, etUniversidad, etCreditos;
    private Spinner spGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        etCedula = findViewById(R.id.etCedula);
        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etCorreo = findViewById(R.id.etCorreo);
        spGenero = findViewById(R.id.spGenero);
        etCarrera = findViewById(R.id.etCarrera);
        etSemestre = findViewById(R.id.etSemestre);
        etUniversidad = findViewById(R.id.etUniversidad);
        etCreditos = findViewById(R.id.etCreditos);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnHistorial = findViewById(R.id.btnHistorial);

        String[] generos = {"Masculino", "Femenino", "Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos);
        spGenero.setAdapter(adapter);

        btnGuardar.setOnClickListener(v -> {
            String cedula = etCedula.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();
            String correo = etCorreo.getText().toString().trim();
            String genero = spGenero.getSelectedItem().toString();
            String carrera = etCarrera.getText().toString().trim();

            int semestre;
            int creditos;
            try {
                semestre = Integer.parseInt(etSemestre.getText().toString().trim());
                creditos = Integer.parseInt(etCreditos.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(CreateActivity.this, "Por favor, ingrese un valor válido para semestre y créditos.", Toast.LENGTH_SHORT).show();
                return;
            }

            String universidad = etUniversidad.getText().toString().trim();

            if (cedulaYaExiste(cedula)) {
                Toast.makeText(CreateActivity.this, "La cédula ya está registrada.", Toast.LENGTH_SHORT).show();
                return;
            }

            Estudiante nuevo = new Estudiante(cedula, nombre, telefono, correo, genero, carrera, semestre, universidad, creditos);

            guardarEstudianteEnHistorial(nuevo);

            Log.d("DEBUG", "Estudiante agregado: " + nuevo.getNombre());
            Toast.makeText(this, "Estudiante agregado", Toast.LENGTH_SHORT).show();

            finish();
        });

        btnHistorial.setOnClickListener(v -> mostrarHistorial());
    }

    private void guardarEstudianteEnHistorial(Estudiante estudiante) {
        try {
            OutputStreamWriter file = new OutputStreamWriter(openFileOutput("estudiantes.txt", MODE_APPEND));

            String registro = String.format(
                    "Cédula: %s\nNombre: %s\nTeléfono: %s\nCorreo: %s\nGénero: %s\nCarrera: %s\nSemestre: %d\nUniversidad: %s\nCréditos: %d\n---\n",
                    estudiante.getCedula(),
                    estudiante.getNombre(),
                    estudiante.getTelefono(),
                    estudiante.getCorreo(),
                    estudiante.getGenero(),
                    estudiante.getCarrera(),
                    estudiante.getSemestre(),
                    estudiante.getUniversidad(),
                    estudiante.getCreditos()
            );

            file.write(registro);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar el estudiante", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean cedulaYaExiste(String cedula) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("estudiantes.txt")));
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("Cédula: " + cedula)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void mostrarHistorial() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("estudiantes.txt")));
            StringBuilder historial = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                historial.append(linea).append("\n");
            }
            reader.close();

            if (historial.length() == 0) {
                historial.append("No hay estudiantes registrados aún.");
            }

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Historial de Estudiantes")
                    .setMessage(historial.toString())
                    .setPositiveButton("OK", null)
                    .show();

        } catch (FileNotFoundException e) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Historial de Estudiantes")
                    .setMessage("No hay estudiantes registrados aún.")
                    .setPositiveButton("OK", null)
                    .show();
        } catch (IOException e) {
            e.printStackTrace();
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Ocurrió un error al leer el historial.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}

