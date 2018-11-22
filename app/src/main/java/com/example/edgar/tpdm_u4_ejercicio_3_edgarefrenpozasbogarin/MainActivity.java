package com.example.edgar.tpdm_u4_ejercicio_3_edgarefrenpozasbogarin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView ls_liebre,ls_tortuga;
    private TextView estado,estado_tortuga,estado_liebre;
    private SeekBar barra_tortuga,barra_liebre;
    private boolean corriendo;
    private Corredor ganador_1,ganador_2;
    private ArrayList<Corredor> corredores;
    private final int  MAX=70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barra_tortuga=findViewById(R.id.barra_tortuga);
        barra_liebre=findViewById(R.id.barra_liebre);
        estado=findViewById(R.id.estado);
        estado_tortuga=findViewById(R.id.estado_tortuga);
        estado_liebre=findViewById(R.id.estado_liebre);

        ls_liebre=findViewById(R.id.lista_liebre);
        ls_tortuga=findViewById(R.id.lista_tortuga);

        barra_liebre.setMax(MAX);
        barra_tortuga.setMax(MAX);

    }

    public void iniciar(View view) {
        ganador_1=ganador_2=null;
        corriendo=true;
        corredores=new ArrayList<>();
        corredores.add(new Corredor(0,barra_tortuga,estado_tortuga,ls_tortuga));
        corredores.add(new Corredor(1,barra_liebre,estado_liebre,ls_liebre));
        estado.setText("---");
        for (Corredor c:corredores){
            c.iniciar();
        }

    }
    private void termino(){
        if(ganador_1!=null&&ganador_2==null){
            estado.setText("El ganador es la tortuga");
        }
        if(ganador_1==null&&ganador_2!=null){
            estado.setText("El ganador es la liebre");
        }
        if(ganador_1!=null&&ganador_2!=null){
            estado.setText("Hubo un empate");
        }
    }

    class Corredor{
        private int id;
        private SeekBar barra;
        private TextView estado;
        private ListView ls;
        public ArrayList<String> situaciones;
        private int progreso=0;

        public Corredor(int id,SeekBar barra,TextView estado,ListView ls){
            this.id=id;
            this.barra=barra;
            this.estado=estado;
            this.ls=ls;
            situaciones=new ArrayList<>();
        }
        private Object[] set_probabilidad(int prob,int valor,ArrayList<Integer> opciones,String tag,ArrayList<String> tags){
            for (int i=0;i<prob;i++){
                opciones.add(valor);
                tags.add(tag);
            }
            return new Object[]{opciones,tags};
        }

        public void iniciar(){
            situaciones.clear();
            situaciones.add("Inicio carrera");
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, situaciones);
            ls.setAdapter(adapter);
            barra.setProgress(0);
            progreso=0;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (corriendo){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayList<Integer> opciones=new ArrayList<Integer>();
                                ArrayList<String> opciones_nombres=new ArrayList<String>();

                                if(id==0){
                                    Object[] arr=set_probabilidad(50,3,opciones,"Avance rápido",opciones_nombres);
                                    opciones=(ArrayList<Integer>)arr[0];
                                    opciones_nombres=(ArrayList<String>)arr[1];
                                    arr=set_probabilidad(20,-6,opciones,"Resbaló",opciones_nombres);
                                    opciones=(ArrayList<Integer>)arr[0];
                                    opciones_nombres=(ArrayList<String>)arr[1];
                                    arr=set_probabilidad(30,1,opciones,"Avance lento",opciones_nombres);
                                    opciones=(ArrayList<Integer>)arr[0];
                                    opciones_nombres=(ArrayList<String>)arr[1];
                                }else{
                                    Object[] arr=set_probabilidad(20,0,opciones,"Duerme",opciones_nombres);
                                    opciones=(ArrayList<Integer>)arr[0];
                                    opciones_nombres=(ArrayList<String>)arr[1];
                                    arr=set_probabilidad(20,9,opciones,"Gran salto",opciones_nombres);
                                    opciones=(ArrayList<Integer>)arr[0];
                                    opciones_nombres=(ArrayList<String>)arr[1];
                                    arr=set_probabilidad(10,-12,opciones,"Resbalón grande",opciones_nombres);
                                    opciones=(ArrayList<Integer>)arr[0];
                                    opciones_nombres=(ArrayList<String>)arr[1];
                                    arr=set_probabilidad(30,1,opciones,"Pequeño salto",opciones_nombres);
                                    opciones=(ArrayList<Integer>)arr[0];
                                    opciones_nombres=(ArrayList<String>)arr[1];
                                    arr=set_probabilidad(20,-2,opciones,"Resbalón pequeño",opciones_nombres);
                                    opciones=(ArrayList<Integer>)arr[0];
                                    opciones_nombres=(ArrayList<String>)arr[1];
                                }

                                int index=(int)(Math.random()*100);
                                progreso=opciones.get(index);


                                estado.setText(opciones_nombres.get(index));
                                situaciones.add(opciones_nombres.get(index));

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, situaciones);
                                ls.setAdapter(adapter);

                                barra.setProgress(barra.getProgress() + progreso);

                                if (barra.getProgress() >= barra.getMax()) {
                                    corriendo = false;
                                    if (id == 0)
                                        ganador_1 = Corredor.this;
                                    else
                                        ganador_2 = Corredor.this;
                                    termino();
                                    situaciones.add("Termino carrera");
                                }

                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
