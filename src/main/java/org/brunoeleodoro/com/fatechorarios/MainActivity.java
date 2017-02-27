package org.brunoeleodoro.com.fatechorarios;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ArrayList<Integer> dias = new ArrayList<Integer>();
    String curso = "";
    ListView lv;
    ImageButton img;
    Spinner spinner;
    Button btn;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //btn = (Button) findViewById(R.id.button2);
        /*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        */
        db = openOrCreateDatabase("bruno_DB",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS horario(" +
                "cod INTEGER not null," +
                "aula varchar(500) not null," +
                "professor varchar(500) not null," +
                "hora varchar(500) not null," +
                "dia varchar(500) not null," +
                "Primary key(cod));");

        spinner = (Spinner) findViewById(R.id.spinner3);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fill(spinner.getSelectedItemPosition() + 2);
            }
            //changed
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //fill(spinner.getSelectedItemPosition() + 2);
        curso = getIntent().getStringExtra("curso");
        //setTitle(curso);
        setTitle("-");
        lv = (ListView) findViewById(R.id.listView2);
        //Setando items na lista
        TextView txt_curso = (TextView) findViewById(R.id.txt_curso);
        txt_curso.setText(curso);
        int hoje_int = 0;
        Calendar calendar = Calendar.getInstance();
        hoje_int = calendar.get(Calendar.DAY_OF_WEEK);
        fill(hoje_int);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListaAdapter adapter = (ListaAdapter) lv.getAdapter();
                String selecionado = String.valueOf(adapter.getItem(i));
                return false;
            }
        });
        //spinner.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
    }

    public int indexOf(int day) {
        int res = 0;
        res = dias.indexOf(day);
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, Splash.class);
            i.putExtra("sql", "DELETE FROM usuario WHERE 1");
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
        else if (id == R.id.problem) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Aviso");
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setMessage("Envie um email para:\nbrunoeleodoro96@gmail.com");
            dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.setPositiveButton("ENVIAR EMAIL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String[] TO = {"brunoeleodoro96@gmail.com"};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Fatec - Horarios Mensagem");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Erro: " +
                            "\r\n");

                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));

                        //Log.i("Finished sending email...", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        //Toast.makeText(MainActivity.this,
                                //"There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        }
        else if(id == R.id.about)
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Sobre");
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setMessage("Desenvolvedor:\nBruno Eleodoro\nbrunoeleodoro96@gmail.com");
            dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            dialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
    public void fill(int hoje_int)
    {
        Aulas aulas = new Aulas();
        if(curso.equals("ADS 1º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_1_m_aulas,aulas.a_segunda_1_m_professores,aulas.a_segunda_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_1_m_aulas,aulas.a_terca_1_m_professores,aulas.a_terca_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_1_m_aulas,aulas.a_quarta_1_m_professores,aulas.a_quarta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_1_m_aulas,aulas.a_quinta_1_m_professores,aulas.a_quinta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_1_m_aulas,aulas.a_sexta_1_m_professores,aulas.a_sexta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 2º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_2_m_aulas,aulas.a_segunda_2_m_professores,aulas.a_segunda_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_2_m_aulas,aulas.a_terca_2_m_professores,aulas.a_terca_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_2_m_aulas,aulas.a_quarta_2_m_professores,aulas.a_quarta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_2_m_aulas,aulas.a_quinta_2_m_professores,aulas.a_quinta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_2_m_aulas,aulas.a_sexta_2_m_professores,aulas.a_sexta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 3º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_3_m_aulas,aulas.a_segunda_3_m_professores,aulas.a_segunda_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_3_m_aulas,aulas.a_terca_3_m_professores,aulas.a_terca_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_3_m_aulas,aulas.a_quarta_3_m_professores,aulas.a_quarta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_3_m_aulas,aulas.a_quinta_3_m_professores,aulas.a_quinta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_3_m_aulas,aulas.a_sexta_3_m_professores,aulas.a_sexta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 4º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_4_m_aulas,aulas.a_segunda_4_m_professores,aulas.a_segunda_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_4_m_aulas,aulas.a_terca_4_m_professores,aulas.a_terca_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_4_m_aulas,aulas.a_quarta_4_m_professores,aulas.a_quarta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_4_m_aulas,aulas.a_quinta_4_m_professores,aulas.a_quinta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_4_m_aulas,aulas.a_sexta_4_m_professores,aulas.a_sexta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 5º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_5_m_aulas,aulas.a_segunda_5_m_professores,aulas.a_segunda_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_5_m_aulas,aulas.a_terca_5_m_professores,aulas.a_terca_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_5_m_aulas,aulas.a_quarta_5_m_professores,aulas.a_quarta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_5_m_aulas,aulas.a_quinta_5_m_professores,aulas.a_quinta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_5_m_aulas,aulas.a_sexta_5_m_professores,aulas.a_sexta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 6º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_6_m_aulas,aulas.a_segunda_6_m_professores,aulas.a_segunda_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_6_m_aulas,aulas.a_terca_6_m_professores,aulas.a_terca_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_6_m_aulas,aulas.a_quarta_6_m_professores,aulas.a_quarta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_6_m_aulas,aulas.a_quinta_6_m_professores,aulas.a_quinta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_6_m_aulas,aulas.a_sexta_6_m_professores,aulas.a_sexta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 1º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_1_v_aulas,aulas.a_segunda_1_v_professores,aulas.a_segunda_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_1_v_aulas,aulas.a_terca_1_v_professores,aulas.a_terca_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_1_v_aulas,aulas.a_quarta_1_v_professores,aulas.a_quarta_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_1_v_aulas,aulas.a_quinta_1_v_professores,aulas.a_quinta_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_1_v_aulas,aulas.a_sexta_1_v_professores,aulas.a_sexta_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 2º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_2_v_aulas,aulas.a_segunda_2_v_professores,aulas.a_segunda_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_2_v_aulas,aulas.a_terca_2_v_professores,aulas.a_terca_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_2_v_aulas,aulas.a_quarta_2_v_professores,aulas.a_quarta_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_2_v_aulas,aulas.a_quinta_2_v_professores,aulas.a_quinta_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_2_v_aulas,aulas.a_sexta_2_v_professores,aulas.a_sexta_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 3º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_3_v_aulas,aulas.a_segunda_3_v_professores,aulas.a_segunda_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_3_v_aulas,aulas.a_terca_3_v_professores,aulas.a_terca_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_3_v_aulas,aulas.a_quarta_3_v_professores,aulas.a_quarta_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_3_v_aulas,aulas.a_quinta_3_v_professores,aulas.a_quinta_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_3_v_aulas,aulas.a_sexta_3_v_professores,aulas.a_sexta_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 4º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_4_v_aulas,aulas.a_segunda_4_v_professores,aulas.a_segunda_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_4_v_aulas,aulas.a_terca_4_v_professores,aulas.a_terca_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_4_v_aulas,aulas.a_quarta_4_v_professores,aulas.a_quarta_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_4_v_aulas,aulas.a_quinta_4_v_professores,aulas.a_quinta_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_4_v_aulas,aulas.a_sexta_4_v_professores,aulas.a_sexta_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 5º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_5_v_aulas,aulas.a_segunda_5_v_professores,aulas.a_segunda_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_5_v_aulas,aulas.a_terca_5_v_professores,aulas.a_terca_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_5_v_aulas,aulas.a_quarta_5_v_professores,aulas.a_quarta_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_5_v_aulas,aulas.a_quinta_5_v_professores,aulas.a_quinta_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_5_v_aulas,aulas.a_sexta_5_v_professores,aulas.a_sexta_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("ADS 6º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_segunda_6_v_aulas,aulas.a_segunda_6_v_professores,aulas.a_segunda_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_terca_6_v_aulas,aulas.a_terca_6_v_professores,aulas.a_terca_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quarta_6_v_aulas,aulas.a_quarta_6_v_professores,aulas.a_quarta_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_quinta_6_v_aulas,aulas.a_quinta_6_v_professores,aulas.a_quinta_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.a_sexta_6_v_aulas,aulas.a_sexta_6_v_professores,aulas.a_sexta_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 1º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_1_m_aulas,aulas.g_segunda_1_m_professores,aulas.g_segunda_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_1_m_aulas,aulas.g_terca_1_m_professores,aulas.g_terca_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_1_m_aulas,aulas.g_quarta_1_m_professores,aulas.g_quarta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_1_m_aulas,aulas.g_quinta_1_m_professores,aulas.g_quinta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_1_m_aulas,aulas.g_sexta_1_m_professores,aulas.g_sexta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 2º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_2_m_aulas,aulas.g_segunda_2_m_professores,aulas.g_segunda_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_2_m_aulas,aulas.g_terca_2_m_professores,aulas.g_terca_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_2_m_aulas,aulas.g_quarta_2_m_professores,aulas.g_quarta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_2_m_aulas,aulas.g_quinta_2_m_professores,aulas.g_quinta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_2_m_aulas,aulas.g_sexta_2_m_professores,aulas.g_sexta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 3º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_3_m_aulas,aulas.g_segunda_3_m_professores,aulas.g_segunda_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_3_m_aulas,aulas.g_terca_3_m_professores,aulas.g_terca_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_3_m_aulas,aulas.g_quarta_3_m_professores,aulas.g_quarta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_3_m_aulas,aulas.g_quinta_3_m_professores,aulas.g_quinta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_3_m_aulas,aulas.g_sexta_3_m_professores,aulas.g_sexta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 4º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_4_m_aulas,aulas.g_segunda_4_m_professores,aulas.g_segunda_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_4_m_aulas,aulas.g_terca_4_m_professores,aulas.g_terca_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_4_m_aulas,aulas.g_quarta_4_m_professores,aulas.g_quarta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_4_m_aulas,aulas.g_quinta_4_m_professores,aulas.g_quinta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_4_m_aulas,aulas.g_sexta_4_m_professores,aulas.g_sexta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 5º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_5_m_aulas,aulas.g_segunda_5_m_professores,aulas.g_segunda_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_5_m_aulas,aulas.g_terca_5_m_professores,aulas.g_terca_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_5_m_aulas,aulas.g_quarta_5_m_professores,aulas.g_quarta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_5_m_aulas,aulas.g_quinta_5_m_professores,aulas.g_quinta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_5_m_aulas,aulas.g_sexta_5_m_professores,aulas.g_sexta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 6º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_6_m_aulas,aulas.g_segunda_6_m_professores,aulas.g_segunda_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_6_m_aulas,aulas.g_terca_6_m_professores,aulas.g_terca_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_6_m_aulas,aulas.g_quarta_6_m_professores,aulas.g_quarta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_6_m_aulas,aulas.g_quinta_6_m_professores,aulas.g_quinta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_6_m_aulas,aulas.g_sexta_6_m_professores,aulas.g_sexta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }else if(curso.equals("Gestão Empresarial 1º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_1_v_aulas,aulas.g_segunda_1_v_professores,aulas.g_segunda_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_1_v_aulas,aulas.g_terca_1_v_professores,aulas.g_terca_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_1_v_aulas,aulas.g_quarta_1_v_professores,aulas.g_quarta_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_1_v_aulas,aulas.g_quinta_1_v_professores,aulas.g_quinta_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_1_v_aulas,aulas.g_sexta_1_v_professores,aulas.g_sexta_1_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 2º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_2_v_aulas,aulas.g_segunda_2_v_professores,aulas.g_segunda_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_2_v_aulas,aulas.g_terca_2_v_professores,aulas.g_terca_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_2_v_aulas,aulas.g_quarta_2_v_professores,aulas.g_quarta_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_2_v_aulas,aulas.g_quinta_2_v_professores,aulas.g_quinta_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_2_v_aulas,aulas.g_sexta_2_v_professores,aulas.g_sexta_2_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 3º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_3_v_aulas,aulas.g_segunda_3_v_professores,aulas.g_segunda_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_3_v_aulas,aulas.g_terca_3_v_professores,aulas.g_terca_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_3_v_aulas,aulas.g_quarta_3_v_professores,aulas.g_quarta_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_3_v_aulas,aulas.g_quinta_3_v_professores,aulas.g_quinta_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_3_v_aulas,aulas.g_sexta_3_v_professores,aulas.g_sexta_3_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 4º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_4_v_aulas,aulas.g_segunda_4_v_professores,aulas.g_segunda_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_4_v_aulas,aulas.g_terca_4_v_professores,aulas.g_terca_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_4_v_aulas,aulas.g_quarta_4_v_professores,aulas.g_quarta_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_4_v_aulas,aulas.g_quinta_4_v_professores,aulas.g_quinta_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_4_v_aulas,aulas.g_sexta_4_v_professores,aulas.g_sexta_4_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 5º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_5_v_aulas,aulas.g_segunda_5_v_professores,aulas.g_segunda_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_5_v_aulas,aulas.g_terca_5_v_professores,aulas.g_terca_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_5_v_aulas,aulas.g_quarta_5_v_professores,aulas.g_quarta_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_5_v_aulas,aulas.g_quinta_5_v_professores,aulas.g_quinta_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_5_v_aulas,aulas.g_sexta_5_v_professores,aulas.g_sexta_5_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Gestão Empresarial 6º sem - Vespertino"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_segunda_6_v_aulas,aulas.g_segunda_6_v_professores,aulas.g_segunda_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_terca_6_v_aulas,aulas.g_terca_6_v_professores,aulas.g_terca_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quarta_6_v_aulas,aulas.g_quarta_6_v_professores,aulas.g_quarta_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_quinta_6_v_aulas,aulas.g_quinta_6_v_professores,aulas.g_quinta_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.g_sexta_6_v_aulas,aulas.g_sexta_6_v_professores,aulas.g_sexta_6_v_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }else if(curso.equals("Jogos Digitais 1º sem - Noturno"))
        {
            
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_segunda_1_n_aulas,aulas.j_segunda_1_n_professores,aulas.j_segunda_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_terca_1_n_aulas,aulas.j_terca_1_n_professores,aulas.j_terca_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quarta_1_n_aulas,aulas.j_quarta_1_n_professores,aulas.j_quarta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quinta_1_n_aulas,aulas.j_quinta_1_n_professores,aulas.j_quinta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sexta_1_n_aulas,aulas.j_sexta_1_n_professores,aulas.j_sexta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sabado_1_n_aulas,aulas.j_sabado_1_n_professores,aulas.j_sabado_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Jogos Digitais 2º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_segunda_2_n_aulas,aulas.j_segunda_2_n_professores,aulas.j_segunda_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_terca_2_n_aulas,aulas.j_terca_2_n_professores,aulas.j_terca_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quarta_2_n_aulas,aulas.j_quarta_2_n_professores,aulas.j_quarta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quinta_2_n_aulas,aulas.j_quinta_2_n_professores,aulas.j_quinta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sexta_2_n_aulas,aulas.j_sexta_2_n_professores,aulas.j_sexta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sabado_2_n_aulas,aulas.j_sabado_2_n_professores,aulas.j_sabado_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Jogos Digitais 3º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_segunda_3_n_aulas,aulas.j_segunda_3_n_professores,aulas.j_segunda_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_terca_3_n_aulas,aulas.j_terca_3_n_professores,aulas.j_terca_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quarta_3_n_aulas,aulas.j_quarta_3_n_professores,aulas.j_quarta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quinta_3_n_aulas,aulas.j_quinta_3_n_professores,aulas.j_quinta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sexta_3_n_aulas,aulas.j_sexta_3_n_professores,aulas.j_sexta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sabado_3_n_aulas,aulas.j_sabado_3_n_professores,aulas.j_sabado_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Jogos Digitais 4º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_segunda_4_n_aulas,aulas.j_segunda_4_n_professores,aulas.j_segunda_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_terca_4_n_aulas,aulas.j_terca_4_n_professores,aulas.j_terca_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quarta_4_n_aulas,aulas.j_quarta_4_n_professores,aulas.j_quarta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quinta_4_n_aulas,aulas.j_quinta_4_n_professores,aulas.j_quinta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sexta_4_n_aulas,aulas.j_sexta_4_n_professores,aulas.j_sexta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sabado_4_n_aulas,aulas.j_sabado_4_n_professores,aulas.j_sabado_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Jogos Digitais 5º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_segunda_5_n_aulas,aulas.j_segunda_5_n_professores,aulas.j_segunda_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_terca_5_n_aulas,aulas.j_terca_5_n_professores,aulas.j_terca_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quarta_5_n_aulas,aulas.j_quarta_5_n_professores,aulas.j_quarta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quinta_5_n_aulas,aulas.j_quinta_5_n_professores,aulas.j_quinta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sexta_5_n_aulas,aulas.j_sexta_5_n_professores,aulas.j_sexta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sabado_5_n_aulas,aulas.j_sabado_5_n_professores,aulas.j_sabado_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Jogos Digitais 6º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_segunda_6_n_aulas,aulas.j_segunda_6_n_professores,aulas.j_segunda_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_terca_6_n_aulas,aulas.j_terca_6_n_professores,aulas.j_terca_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quarta_6_n_aulas,aulas.j_quarta_6_n_professores,aulas.j_quarta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_quinta_6_n_aulas,aulas.j_quinta_6_n_professores,aulas.j_quinta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sexta_6_n_aulas,aulas.j_sexta_6_n_professores,aulas.j_sexta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.j_sabado_6_n_aulas,aulas.j_sabado_6_n_professores,aulas.j_sabado_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }else if(curso.equals("Logistica 1º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_2_m_aulas,aulas.l_segunda_2_m_professores,aulas.l_segunda_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_2_m_aulas,aulas.l_terca_2_m_professores,aulas.l_terca_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_2_m_aulas,aulas.l_quarta_2_m_professores,aulas.l_quarta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_2_m_aulas,aulas.l_quinta_2_m_professores,aulas.l_quinta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_2_m_aulas,aulas.l_sexta_2_m_professores,aulas.l_sexta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Logistica 2º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_3_m_aulas,aulas.l_segunda_3_m_professores,aulas.l_segunda_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_3_m_aulas,aulas.l_terca_3_m_professores,aulas.l_terca_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_3_m_aulas,aulas.l_quarta_3_m_professores,aulas.l_quarta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_3_m_aulas,aulas.l_quinta_3_m_professores,aulas.l_quinta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_3_m_aulas,aulas.l_sexta_3_m_professores,aulas.l_sexta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Logistica 3º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_4_m_aulas,aulas.l_segunda_4_m_professores,aulas.l_segunda_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_4_m_aulas,aulas.l_terca_4_m_professores,aulas.l_terca_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_4_m_aulas,aulas.l_quarta_4_m_professores,aulas.l_quarta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_4_m_aulas,aulas.l_quinta_4_m_professores,aulas.l_quinta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_4_m_aulas,aulas.l_sexta_4_m_professores,aulas.l_sexta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Logistica 4º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_4_m_aulas,aulas.l_segunda_4_m_professores,aulas.l_segunda_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_4_m_aulas,aulas.l_terca_4_m_professores,aulas.l_terca_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_4_m_aulas,aulas.l_quarta_4_m_professores,aulas.l_quarta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_4_m_aulas,aulas.l_quinta_4_m_professores,aulas.l_quinta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_4_m_aulas,aulas.l_sexta_4_m_professores,aulas.l_sexta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }else if(curso.equals("Logistica 1º sem - Noturno"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_1_n_aulas,aulas.l_segunda_1_n_professores,aulas.l_segunda_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_1_n_aulas,aulas.l_terca_1_n_professores,aulas.l_terca_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_1_n_aulas,aulas.l_quarta_1_n_professores,aulas.l_quarta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_1_n_aulas,aulas.l_quinta_1_n_professores,aulas.l_quinta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_1_n_aulas,aulas.l_sexta_1_n_professores,aulas.l_sexta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Logistica 2º sem - Noturno"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_2_n_aulas,aulas.l_segunda_2_n_professores,aulas.l_segunda_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_2_n_aulas,aulas.l_terca_2_n_professores,aulas.l_terca_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_2_n_aulas,aulas.l_quarta_2_n_professores,aulas.l_quarta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_2_n_aulas,aulas.l_quinta_2_n_professores,aulas.l_quinta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_2_n_aulas,aulas.l_sexta_2_n_professores,aulas.l_sexta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Logistica 3º sem - Noturno"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_3_n_aulas,aulas.l_segunda_3_n_professores,aulas.l_segunda_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_3_n_aulas,aulas.l_terca_3_n_professores,aulas.l_terca_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_3_n_aulas,aulas.l_quarta_3_n_professores,aulas.l_quarta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_3_n_aulas,aulas.l_quinta_3_n_professores,aulas.l_quinta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_3_n_aulas,aulas.l_sexta_3_n_professores,aulas.l_sexta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Logistica 4º sem - Noturno"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_4_n_aulas,aulas.l_segunda_4_n_professores,aulas.l_segunda_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_4_n_aulas,aulas.l_terca_4_n_professores,aulas.l_terca_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_4_n_aulas,aulas.l_quarta_4_n_professores,aulas.l_quarta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_4_n_aulas,aulas.l_quinta_4_n_professores,aulas.l_quinta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_4_n_aulas,aulas.l_sexta_4_n_professores,aulas.l_sexta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Logistica 5º sem - Noturno"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_5_n_aulas,aulas.l_segunda_5_n_professores,aulas.l_segunda_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_5_n_aulas,aulas.l_terca_5_n_professores,aulas.l_terca_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_5_n_aulas,aulas.l_quarta_5_n_professores,aulas.l_quarta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_5_n_aulas,aulas.l_quinta_5_n_professores,aulas.l_quinta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_5_n_aulas,aulas.l_sexta_5_n_professores,aulas.l_sexta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Logistica 6º sem - Noturno"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_segunda_6_n_aulas,aulas.l_segunda_6_n_professores,aulas.l_segunda_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_terca_6_n_aulas,aulas.l_terca_6_n_professores,aulas.l_terca_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quarta_6_n_aulas,aulas.l_quarta_6_n_professores,aulas.l_quarta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_quinta_6_n_aulas,aulas.l_quinta_6_n_professores,aulas.l_quinta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.l_sexta_6_n_aulas,aulas.l_sexta_6_n_professores,aulas.l_sexta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }else if(curso.equals("Têxtil e Moda 1º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_segunda_1_m_aulas,aulas.m_segunda_1_m_professores,aulas.m_segunda_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_terca_1_m_aulas,aulas.m_terca_1_m_professores,aulas.m_terca_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_quarta_1_m_aulas,aulas.m_quarta_1_m_professores,aulas.m_quarta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_quinta_1_m_aulas,aulas.m_quinta_1_m_professores,aulas.m_quinta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_sexta_1_m_aulas,aulas.m_sexta_1_m_professores,aulas.m_sexta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Têxtil e Moda 2º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_segunda_2_m_aulas,aulas.m_segunda_2_m_professores,aulas.m_segunda_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_terca_2_m_aulas,aulas.m_terca_2_m_professores,aulas.m_terca_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_quarta_2_m_aulas,aulas.m_quarta_2_m_professores,aulas.m_quarta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_quinta_2_m_aulas,aulas.m_quinta_2_m_professores,aulas.m_quinta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_sexta_2_m_aulas,aulas.m_sexta_2_m_professores,aulas.m_sexta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Têxtil e Moda 3º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_segunda_3_m_aulas,aulas.m_segunda_3_m_professores,aulas.m_segunda_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_terca_3_m_aulas,aulas.m_terca_3_m_professores,aulas.m_terca_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_quarta_3_m_aulas,aulas.m_quarta_3_m_professores,aulas.m_quarta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_quinta_3_m_aulas,aulas.m_quinta_3_m_professores,aulas.m_quinta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_sexta_3_m_aulas,aulas.m_sexta_3_m_professores,aulas.m_sexta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Têxtil e Moda 4º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_segunda_4_m_aulas,aulas.m_segunda_4_m_professores,aulas.m_segunda_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_terca_4_m_aulas,aulas.m_terca_4_m_professores,aulas.m_terca_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_quarta_4_m_aulas,aulas.m_quarta_4_m_professores,aulas.m_quarta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_quinta_4_m_aulas,aulas.m_quinta_4_m_professores,aulas.m_quinta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.m_sexta_4_m_aulas,aulas.m_sexta_4_m_professores,aulas.m_sexta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }else if(curso.equals("Segurança da informação 1º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_1_m_aulas,aulas.s_segunda_1_m_professores,aulas.s_segunda_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_1_m_aulas,aulas.s_terca_1_m_professores,aulas.s_terca_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_1_m_aulas,aulas.s_quarta_1_m_professores,aulas.s_quarta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_1_m_aulas,aulas.s_quinta_1_m_professores,aulas.s_quinta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_1_m_aulas,aulas.s_sexta_1_m_professores,aulas.s_sexta_1_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 2º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_2_m_aulas,aulas.s_segunda_2_m_professores,aulas.s_segunda_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_2_m_aulas,aulas.s_terca_2_m_professores,aulas.s_terca_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_2_m_aulas,aulas.s_quarta_2_m_professores,aulas.s_quarta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_2_m_aulas,aulas.s_quinta_2_m_professores,aulas.s_quinta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_2_m_aulas,aulas.s_sexta_2_m_professores,aulas.s_sexta_2_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 3º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_3_m_aulas,aulas.s_segunda_3_m_professores,aulas.s_segunda_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_3_m_aulas,aulas.s_terca_3_m_professores,aulas.s_terca_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_3_m_aulas,aulas.s_quarta_3_m_professores,aulas.s_quarta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_3_m_aulas,aulas.s_quinta_3_m_professores,aulas.s_quinta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_3_m_aulas,aulas.s_sexta_3_m_professores,aulas.s_sexta_3_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 4º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_4_m_aulas,aulas.s_segunda_4_m_professores,aulas.s_segunda_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_4_m_aulas,aulas.s_terca_4_m_professores,aulas.s_terca_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_4_m_aulas,aulas.s_quarta_4_m_professores,aulas.s_quarta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_4_m_aulas,aulas.s_quinta_4_m_professores,aulas.s_quinta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_4_m_aulas,aulas.s_sexta_4_m_professores,aulas.s_sexta_4_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 5º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_5_m_aulas,aulas.s_segunda_5_m_professores,aulas.s_segunda_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_5_m_aulas,aulas.s_terca_5_m_professores,aulas.s_terca_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_5_m_aulas,aulas.s_quarta_5_m_professores,aulas.s_quarta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_5_m_aulas,aulas.s_quinta_5_m_professores,aulas.s_quinta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_5_m_aulas,aulas.s_sexta_5_m_professores,aulas.s_sexta_5_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 6º sem - Manhã"))
        {
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_6_m_aulas,aulas.s_segunda_6_m_professores,aulas.s_segunda_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_6_m_aulas,aulas.s_terca_6_m_professores,aulas.s_terca_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_6_m_aulas,aulas.s_quarta_6_m_professores,aulas.s_quarta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_6_m_aulas,aulas.s_quinta_6_m_professores,aulas.s_quinta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_6_m_aulas,aulas.s_sexta_6_m_professores,aulas.s_sexta_6_m_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }else if(curso.equals("Segurança da informação 1º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_1_n_aulas,aulas.s_segunda_1_n_professores,aulas.s_segunda_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_1_n_aulas,aulas.s_terca_1_n_professores,aulas.s_terca_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_1_n_aulas,aulas.s_quarta_1_n_professores,aulas.s_quarta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_1_n_aulas,aulas.s_quinta_1_n_professores,aulas.s_quinta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_1_n_aulas,aulas.s_sexta_1_n_professores,aulas.s_sexta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sabado_1_n_aulas,aulas.s_sabado_1_n_professores,aulas.s_sabado_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 2º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_2_n_aulas,aulas.s_segunda_2_n_professores,aulas.s_segunda_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_2_n_aulas,aulas.s_terca_2_n_professores,aulas.s_terca_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_2_n_aulas,aulas.s_quarta_2_n_professores,aulas.s_quarta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_2_n_aulas,aulas.s_quinta_2_n_professores,aulas.s_quinta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_2_n_aulas,aulas.s_sexta_2_n_professores,aulas.s_sexta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sabado_2_n_aulas,aulas.s_sabado_2_n_professores,aulas.s_sabado_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 3º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_3_n_aulas,aulas.s_segunda_3_n_professores,aulas.s_segunda_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_3_n_aulas,aulas.s_terca_3_n_professores,aulas.s_terca_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_3_n_aulas,aulas.s_quarta_3_n_professores,aulas.s_quarta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_3_n_aulas,aulas.s_quinta_3_n_professores,aulas.s_quinta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_3_n_aulas,aulas.s_sexta_3_n_professores,aulas.s_sexta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sabado_3_n_aulas,aulas.s_sabado_3_n_professores,aulas.s_sabado_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sabado_3_n_aulas,aulas.s_sabado_3_n_professores,aulas.s_sabado_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 4º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_4_n_aulas,aulas.s_segunda_4_n_professores,aulas.s_segunda_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_4_n_aulas,aulas.s_terca_4_n_professores,aulas.s_terca_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_4_n_aulas,aulas.s_quarta_4_n_professores,aulas.s_quarta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_4_n_aulas,aulas.s_quinta_4_n_professores,aulas.s_quinta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_4_n_aulas,aulas.s_sexta_4_n_professores,aulas.s_sexta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sabado_4_n_aulas,aulas.s_sabado_4_n_professores,aulas.s_sabado_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 5º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_5_n_aulas,aulas.s_segunda_5_n_professores,aulas.s_segunda_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_5_n_aulas,aulas.s_terca_5_n_professores,aulas.s_terca_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_5_n_aulas,aulas.s_quarta_5_n_professores,aulas.s_quarta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_5_n_aulas,aulas.s_quinta_5_n_professores,aulas.s_quinta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_5_n_aulas,aulas.s_sexta_5_n_professores,aulas.s_sexta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sabado_5_n_aulas,aulas.s_sabado_5_n_professores,aulas.s_sabado_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Segurança da informação 6º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_segunda_6_n_aulas,aulas.s_segunda_6_n_professores,aulas.s_segunda_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_terca_6_n_aulas,aulas.s_terca_6_n_professores,aulas.s_terca_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quarta_6_n_aulas,aulas.s_quarta_6_n_professores,aulas.s_quarta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_quinta_6_n_aulas,aulas.s_quinta_6_n_professores,aulas.s_quinta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sexta_6_n_aulas,aulas.s_sexta_6_n_professores,aulas.s_sexta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.s_sabado_6_n_aulas,aulas.s_sabado_6_n_professores,aulas.s_sabado_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }else if(curso.equals("Produção Textil 1º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_segunda_1_n_aulas,aulas.t_segunda_1_n_professores,aulas.t_segunda_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_terca_1_n_aulas,aulas.t_terca_1_n_professores,aulas.t_terca_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quarta_1_n_aulas,aulas.t_quarta_1_n_professores,aulas.t_quarta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quinta_1_n_aulas,aulas.t_quinta_1_n_professores,aulas.t_quinta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sexta_1_n_aulas,aulas.t_sexta_1_n_professores,aulas.t_sexta_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sabado_1_n_aulas,aulas.t_sabado_1_n_professores,aulas.t_sabado_1_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Produção Textil 2º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_segunda_2_n_aulas,aulas.t_segunda_2_n_professores,aulas.t_segunda_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_terca_2_n_aulas,aulas.t_terca_2_n_professores,aulas.t_terca_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quarta_2_n_aulas,aulas.t_quarta_2_n_professores,aulas.t_quarta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quinta_2_n_aulas,aulas.t_quinta_2_n_professores,aulas.t_quinta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sexta_2_n_aulas,aulas.t_sexta_2_n_professores,aulas.t_sexta_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sabado_2_n_aulas,aulas.t_sabado_2_n_professores,aulas.t_sabado_2_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Produção Textil 3º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_segunda_3_n_aulas,aulas.t_segunda_3_n_professores,aulas.t_segunda_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_terca_3_n_aulas,aulas.t_terca_3_n_professores,aulas.t_terca_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quarta_3_n_aulas,aulas.t_quarta_3_n_professores,aulas.t_quarta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quinta_3_n_aulas,aulas.t_quinta_3_n_professores,aulas.t_quinta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sexta_3_n_aulas,aulas.t_sexta_3_n_professores,aulas.t_sexta_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sabado_3_n_aulas,aulas.t_sabado_3_n_professores,aulas.t_sabado_3_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Produção Textil 4º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_segunda_4_n_aulas,aulas.t_segunda_4_n_professores,aulas.t_segunda_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_terca_4_n_aulas,aulas.t_terca_4_n_professores,aulas.t_terca_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quarta_4_n_aulas,aulas.t_quarta_4_n_professores,aulas.t_quarta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quinta_4_n_aulas,aulas.t_quinta_4_n_professores,aulas.t_quinta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sexta_4_n_aulas,aulas.t_sexta_4_n_professores,aulas.t_sexta_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sabado_4_n_aulas,aulas.t_sabado_4_n_professores,aulas.t_sabado_4_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Produção Textil 5º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_segunda_5_n_aulas,aulas.t_segunda_5_n_professores,aulas.t_segunda_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_terca_5_n_aulas,aulas.t_terca_5_n_professores,aulas.t_terca_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quarta_5_n_aulas,aulas.t_quarta_5_n_professores,aulas.t_quarta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quinta_5_n_aulas,aulas.t_quinta_5_n_professores,aulas.t_quinta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sexta_5_n_aulas,aulas.t_sexta_5_n_professores,aulas.t_sexta_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sabado_5_n_aulas,aulas.t_sabado_5_n_professores,aulas.t_sabado_5_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }
        else if(curso.equals("Produção Textil 6º sem - Noturno"))
        {
            //spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.dias2)));
            if(hoje_int == 2)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_segunda_6_n_aulas,aulas.t_segunda_6_n_professores,aulas.t_segunda_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 3)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_terca_6_n_aulas,aulas.t_terca_6_n_professores,aulas.t_terca_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 4)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quarta_6_n_aulas,aulas.t_quarta_6_n_professores,aulas.t_quarta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 5)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_quinta_6_n_aulas,aulas.t_quinta_6_n_professores,aulas.t_quinta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
            else if(hoje_int == 6)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sexta_6_n_aulas,aulas.t_sexta_6_n_professores,aulas.t_sexta_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }else if(hoje_int == 7)
            {
                lv.setAdapter(new ListaAdapter(this,aulas.t_sabado_6_n_aulas,aulas.t_sabado_6_n_professores,aulas.t_sabado_6_n_horarios));
                spinner.setSelection(hoje_int - 2);
            }
        }

    }
}
