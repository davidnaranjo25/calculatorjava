package com.example.taller;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {
    private EditText etBuscar, etNuevoNombre, etNuevoTelefono, etNuevoCorreo, etNuevaCarrera, etNuevoSemestre, etNuevaUniversidad, etNuevosCreditos;
    private Spinner spNuevoGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etBuscar = findViewById(R.id.etBuscar); // Para buscar por cédula
        etNuevoNombre = findViewById(R.id.etNuevoNombre);
        etNuevoTelefono = findViewById(R.id.etNuevoTelefono);
        etNuevoCorreo = findViewById(R.id.etNuevoCorreo);
        spNuevoGenero = findViewById(R.id.spNuevoGenero);
        etNuevaCarrera = findViewById(R.id.etNuevaCarrera);
        etNuevoSemestre = findViewById(R.id.etNuevoSemestre);
        etNuevaUniversidad = findViewById(R.id.etNuevaUniversidad);
        etNuevosCreditos = findViewById(R.id.etNuevosCreditos);
        Button btnBuscar = findViewById(R.id.btnBuscar);
        Button btnActualizar = findViewById(R.id.btnActualizar);

        // Configuración del Spinner para género
        String[] generos = {"Masculino", "Femenino", "Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, generos);
        spNuevoGenero.setAdapter(adapter);

        // Listener para buscar estudiante por Cédula
        btnBuscar.setOnClickListener(v -> {
            String cedulaBuscar = etBuscar.getText().toString().trim(); // Eliminar espacios en blanco
            boolean encontrado = false;

            for (Estudiante e : MainActivity.estudiantes) {
                if (e.getCedula().equals(cedulaBuscar)) { // Comparación exacta de cédulas

                    // Mostrar los datos del estudiante
                    etNuevoNombre.setText(e.getNombre());
                    etNuevoTelefono.setText(e.getTelefono());
                    etNuevoCorreo.setText(e.getCorreo());
                    etNuevaCarrera.setText(e.getCarrera());
                    etNuevoSemestre.setText(String.valueOf(e.getSemestre()));
                    etNuevaUniversidad.setText(e.getUniversidad());
                    etNuevosCreditos.setText(String.valueOf(e.getCreditos()));
                    spNuevoGenero.setSelection(adapter.getPosition(e.getGenero()));

                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                Toast.makeText(this, "Estudiante no encontrado", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener para actualizar estudiante
        btnActualizar.setOnClickListener(v -> {
            String cedulaBuscar = etBuscar.getText().toString().trim(); // Eliminar espacios en blanco
            boolean actualizado = false;

            for (Estudiante e : MainActivity.estudiantes) {
                if (e.getCedula().equals(cedulaBuscar)) { // Comparación exacta de cédulas

                    // Actualizar los datos del estudiante
                    e.setNombre(etNuevoNombre.getText().toString().trim());
                    e.setTelefono(etNuevoTelefono.getText().toString().trim());
                    e.setCorreo(etNuevoCorreo.getText().toString().trim());
                    e.setGenero(spNuevoGenero.getSelectedItem().toString());
                    e.setCarrera(etNuevaCarrera.getText().toString().trim());
                    e.setSemestre(Integer.parseInt(etNuevoSemestre.getText().toString().trim()));
                    e.setUniversidad(etNuevaUniversidad.getText().toString().trim());
                    e.setCreditos(Integer.parseInt(etNuevosCreditos.getText().toString().trim()));

                    // Actualizar el archivo persistente
                    actualizarArchivo();

                    Toast.makeText(this, "Estudiante actualizado", Toast.LENGTH_SHORT).show();
                    actualizado = true;
                    finish();
                    break;
                }
            }

            if (!actualizado) {
                Toast.makeText(this, "Estudiante no encontrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarArchivo() {
        try {
            // Abrir archivo en modo "write" para sobrescribirlo
            FileOutputStream fos = openFileOutput("estudiantes.txt", MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            for (Estudiante estudiante : MainActivity.estudiantes) {
                // Crear el formato de registro para cada estudiante
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

                // Escribir en el archivo
                writer.write(registro);
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al actualizar el archivo", Toast.LENGTH_SHORT).show();
        }
    }
}
