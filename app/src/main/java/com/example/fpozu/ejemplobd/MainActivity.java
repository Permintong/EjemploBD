package com.example.fpozu.ejemplobd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText et_dni, et_nombre, et_ciudad,et_telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_dni=(EditText)findViewById(R.id.editText_DNI);
        et_telefono=(EditText)findViewById(R.id.editText_telefono);
        et_nombre=(EditText)findViewById(R.id.editText_nombreap);
        et_ciudad=(EditText)findViewById(R.id.editText_ciudad);
    }

    public void crear(View view) {
        //dar de alta usuario
        AdminHelper admin = new AdminHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        //Guardar los valores que leemos de los EditText en un registro
        //Para ello creams el registro
        ContentValues registro = new ContentValues();
        registro.put("dni", et_dni.getText().toString());
        registro.put("nombre", et_nombre.getText().toString());
        registro.put("ciudad", et_ciudad.getText().toString());
        registro.put("numero", et_telefono.getText().toString());

        //insertar en la base de datos
        db.insert("usuario", null, registro);

        //cerrar la base de datos
        db.close();

        //Poner a nulo los campos que hemos escrito

        et_dni.setText("");
        et_nombre.setText("");
        et_ciudad.setText("");
        et_telefono.setText("");

        Toast.makeText(this, "Usuario insertado en la base de datos", Toast.LENGTH_LONG).show();
    }

    public void borrar(View view) {
        AdminHelper admin = new AdminHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String dni = et_dni.getText().toString();
        int cant = db.delete("usuario", "dni="+"'"+dni+"'", null);

        db.close();

        if(cant==1){
            Toast.makeText(this, "Se ha eliminado un usuario", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "No se ha eliminado un usuario, no habia un usuario with such dni", Toast.LENGTH_LONG).show();

        }
        et_dni.setText("");
        et_nombre.setText("");
        et_ciudad.setText("");
        et_telefono.setText("");
    }

    public void consultar(View view) {
        AdminHelper admin = new AdminHelper(this, "administracion", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        String dni = et_dni.getText().toString();
        Cursor c = db.rawQuery("SELECT nombre, ciudad, numero FROM usuario WHERE dni="+"'"+dni+"'", null);
        Log.d("consulta", "antes de mover el cursor");
        if(c.moveToFirst()){
            do{
                String column1 = c.getString(0);
                String column2 = c.getString(1);
                String column3 = c.getString(2);
                et_nombre.setText(column1);
                et_ciudad.setText(column2);
                et_telefono.setText(column3);
            }while (c.moveToNext());
        }
        c.close();
        db.close();
    }
}
