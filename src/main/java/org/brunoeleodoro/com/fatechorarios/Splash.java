package org.brunoeleodoro.com.fatechorarios;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class Splash extends AppCompatActivity {
    SQLiteDatabase db;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btn = (Button) findViewById(R.id.button);

        db = openOrCreateDatabase("bruno_DB",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS usuario(" +
                "cod INTEGER not null," +
                "curso varchar(500) not null," +
                "Primary key(cod));");
        try
        {
            String sql = getIntent().getStringExtra("sql");
            db.execSQL(sql);
        }
        catch (Exception e)
        {

        }
        Cursor c = db.rawQuery("SELECT * FROM usuario",null);
        if(c.getCount() > 0)
        {
            Intent i = new Intent(Splash.this,MainActivity.class);
            while(c.moveToNext())
            {
                i.putExtra("curso",c.getString(c.getColumnIndex("curso")));
            }
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }
        else
        {

        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                Intent i = new Intent(Splash.this,MainActivity.class);
                i.putExtra("curso",spinner.getSelectedItem().toString());
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                db.execSQL("INSERT INTO usuario VALUES(null,'"+spinner.getSelectedItem().toString()+"');");
                finish();
            }
        });
    }
}
