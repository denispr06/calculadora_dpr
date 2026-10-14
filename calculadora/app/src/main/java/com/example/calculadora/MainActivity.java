package com.example.calculadora;
import android.os.Bundle;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {
    String numero = "0";
    double acumulado = 0;
    char operacion = 0;
    boolean nuevoNumero = true;
    boolean esperandoSegundo = false;
    boolean error = false;
    double memoria = 0;
    TextView pantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(    R.layout.activity_main);
        pantalla = findViewById(R.id.pantalla);
        int[] ids = {R.id.cero, R.id.uno, R.id.dos, R.id.tres, R.id.cuatro, R.id.cinco, R.id.seis, R.id.siete, R.id.ocho, R.id.nueve};


        for (int i = 0; i < ids.length; i++) {
           char d = (char) ('0' + i);
           findViewById(ids[i]).setOnClickListener(v -> {
               digito(d);
               actualizar();
           });
        }

        findViewById(R.id.mas).setOnClickListener(v -> {
            operador('+');
            actualizar();
        });

        findViewById(R.id.restar).setOnClickListener(v -> {
            operador('-');
            actualizar();
        });
        findViewById(R.id.multiplica).setOnClickListener(v -> {
            operador('*');
            actualizar();
        });
        findViewById(R.id.dividir).setOnClickListener(v -> {
            operador('/');
            actualizar();
        });
        findViewById(R.id.igual).setOnClickListener(v -> {
            igual();
            actualizar();
        });
        findViewById(R.id.coma).setOnClickListener(v -> {
            coma();
            actualizar();
        });
        findViewById(R.id.borrar).setOnClickListener(v -> {
            borrar();
            actualizar();
        });
        findViewById(R.id.borrar).setOnLongClickListener(v -> {
            limpiarTodo();
            actualizar();
            return true;
        });


        findViewById(R.id.MC).setOnClickListener(v -> memoria = 0);
        findViewById(R.id.MR).setOnClickListener(v -> {
            if (error){
                limpiarTodo();
            }
            numero = formatear(memoria);
            nuevoNumero = true;
            esperandoSegundo = false;
            actualizar();
        });
        findViewById(R.id.mmas).setOnClickListener(v -> {
            if (!error) {
                memoria += valor(); nuevoNumero = true;
            }
        });
        findViewById(R.id.mmenos).setOnClickListener(v -> {
            if (!error) {
                memoria -= valor(); nuevoNumero = true;
            }
        });
        actualizar();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    void digito(char d) {
        if (error) limpiarTodo();
        if (nuevoNumero) { numero = "0"; nuevoNumero = false; }
        esperandoSegundo = false;
        if (numero.equals("0")) {
            numero = String.valueOf(d);
        } else if (numero.length() < 15) {
            numero += d;
        }
    }
    void coma() {
        if (error) limpiarTodo();
        if (nuevoNumero) {
            numero = "0";
            nuevoNumero = false;
        }
        esperandoSegundo = false;
        if (!numero.contains(".")) numero += ".";
    }
    void borrar() {
        if (error) {
            limpiarTodo(); return;
        }
        if (nuevoNumero) {
            if (!esperandoSegundo) limpiarTodo();
            return;
        }
        numero = numero.substring(0, numero.length() - 1);
        if (numero.isEmpty() || numero.equals("-")) numero = "0";
    }
    void limpiarTodo() {
        numero = "0";
        acumulado = 0;
        operacion = 0;
        nuevoNumero = true;
        esperandoSegundo = false;
        error = false;
    }
    void operador(char op) {
        if (error) return;
        if (operacion != 0 && !esperandoSegundo) {
            calcular();
            if (error) return;
        } else if (operacion == 0) {
            acumulado = valor();
        }
        operacion = op;
        esperandoSegundo = true;
        nuevoNumero = true;
    }
    void igual() {
        if (error || operacion == 0) return;
        calcular();
        operacion = 0;
        esperandoSegundo = false;
        nuevoNumero = true;
    }
    void calcular() {
        double b = valor();
        double r;
        switch (operacion) {
            case '+': r = acumulado + b; break;
            case '-': r = acumulado - b; break;
            case '*': r = acumulado * b; break;
            default:
                if (b == 0) { error = true; return; }
                r = acumulado / b;
        }
        acumulado = r;
        numero = formatear(r);
    }
    double valor() {
        return Double.parseDouble(numero);
    }
    String formatear(double d) {
        if (d == (long) d) {
            return String.valueOf((long) d);
        }
        return String.valueOf(d);
    }
    void actualizar() {
        pantalla.setText(error ? "Error" : numero.replace('.', ','));
    }
}
