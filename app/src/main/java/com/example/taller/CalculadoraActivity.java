package com.example.taller;

import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculadoraActivity extends AppCompatActivity {
    private TextView tvResultado;
    private String operador = "";
    private double resultado = 0;
    private boolean nuevoNumero = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora);

        tvResultado = findViewById(R.id.tvResultado);
        configurarBotones();
    }

    private void configurarBotones() {
        findViewById(R.id.btnHistorial).setOnClickListener(v -> mostrarHistorial());
        int[] numeros = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : numeros) {
            findViewById(id).setOnClickListener(this::onNumeroClick);
        }

        int[] operaciones = {R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide};
        for (int id : operaciones) {
            findViewById(id).setOnClickListener(this::onOperadorClick);
        }

        findViewById(R.id.btnEquals).setOnClickListener(v -> realizarCalculo());
        findViewById(R.id.btnClear).setOnClickListener(v -> limpiar());
        findViewById(R.id.btnDot).setOnClickListener(v -> agregarPunto());
    }

    private void onNumeroClick(View view) {
        Button b = (Button) view;

        if (nuevoNumero) {
            tvResultado.setText("");
            nuevoNumero = false;
        }

        tvResultado.append(b.getText());
    }

    private void agregarPunto() {
        String actual = tvResultado.getText().toString();
        if (nuevoNumero) {
            tvResultado.setText("0.");
            nuevoNumero = false;
        } else if (!actual.contains(".")) {
            tvResultado.append(".");
        }
    }

    private void onOperadorClick(View view) {
        Button b = (Button) view;

        if (operador.isEmpty()) {
            resultado = Double.parseDouble(tvResultado.getText().toString());
        } else {
            realizarCalculo();
        }

        operador = b.getText().toString();
        nuevoNumero = true;
    }

    private void realizarCalculo() {
        if (nuevoNumero) {
            return;
        }

        double num2 = Double.parseDouble(tvResultado.getText().toString());
        double resultadoAnterior = resultado;

        switch (operador) {
            case "+":
                resultado += num2;
                break;
            case "-":
                resultado -= num2;
                break;
            case "×":
                resultado *= num2;
                break;
            case "÷":
                if (num2 != 0) {
                    resultado /= num2;
                } else {
                    resultado = 0;
                }
                break;
        }

        tvResultado.setText(String.valueOf(resultado));
        nuevoNumero = true;

        String operacion = resultadoAnterior + " " + operador + " " + num2 + " = " + resultado;
        guardarOperacion(operacion);
    }

    private void mostrarHistorial() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("historial.txt")));
            StringBuilder historial = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                historial.append(linea).append("\n");
            }
            reader.close();

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Historial de Operaciones")
                    .setMessage(historial.toString())
                    .setPositiveButton("OK", null)
                    .show();

        } catch (IOException e) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No se pudo leer el historial.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    public void guardarOperacion(String operacion) {
        try {
            OutputStreamWriter file = new OutputStreamWriter(openFileOutput("historial.txt", MODE_APPEND));
            file.write(operacion + "\n");
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void limpiar() {
        tvResultado.setText("0");
        resultado = 0;
        operador = "";
        nuevoNumero = true;
    }
}
