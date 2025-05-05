package com.example.taller;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class DeleteActivity extends AppCompatActivity {
    private EditText etCedula;
    private Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        etCedula = findViewById(R.id.etEliminarCedula); // Asegúrate de que el ID coincide con el XML
        btnEliminar = findViewById(R.id.btnEliminar);

        btnEliminar.setOnClickListener(v -> {
            String cedula = etCedula.getText().toString().trim();

            if (cedula.isEmpty()) {
                Toast.makeText(DeleteActivity.this, "Por favor, ingresa una cédula", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean encontrado = false;
            for (int i = 0; i < MainActivity.estudiantes.size(); i++) {
                if (MainActivity.estudiantes.get(i).getCedula().equalsIgnoreCase(cedula)) {
                    MainActivity.estudiantes.remove(i); // Eliminar el estudiante
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                if (actualizarArchivo()) {
                    recargarListaDesdeArchivo(); // Recargar la lista desde el archivo
                    Toast.makeText(DeleteActivity.this, "Estudiante eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    etCedula.setText(""); // Limpiar el campo de entrada
                } else {
                    Toast.makeText(DeleteActivity.this, "Error al actualizar el archivo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DeleteActivity.this, "Estudiante no encontrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean actualizarArchivo() {
        try {
            // Abrir el archivo en modo escritura exclusiva para sobrescribirlo
            FileOutputStream fos = openFileOutput("estudiantes.txt", MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            // Recorrer la lista de estudiantes y escribir cada uno en el archivo
            for (Estudiante estudiante : MainActivity.estudiantes) {
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
                writer.write(registro);
            }

            // Asegurarse de cerrar los flujos para guardar los cambios
            writer.flush();
            writer.close();

            return true; // Indicar que el archivo se actualizó correctamente
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Indicar que hubo un error
        }
    }

    private void recargarListaDesdeArchivo() {
        try {
            FileInputStream fis = openFileInput("estudiantes.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String linea;
            MainActivity.estudiantes.clear(); // Limpiar la lista antes de recargar los datos
            Estudiante estudianteActual = null;

            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("Cédula: ")) {
                    if (estudianteActual != null) {
                        MainActivity.estudiantes.add(estudianteActual); // Agregar el estudiante previo a la lista
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
                        MainActivity.estudiantes.add(estudianteActual); // Agregar el último estudiante
                        estudianteActual = null;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}