package up.edu.br.contatos;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class ContatoActivity extends AppCompatActivity {

    Contato contato;
    int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        EditText txtNome =
                (EditText) findViewById(R.id.txtNome);
        EditText txtTelefone =
                (EditText) findViewById(R.id.txtTelefone);
        Spinner spTipo =
                (Spinner) findViewById(R.id.spTipo);
        Spinner spEstado =
                (Spinner)findViewById(R.id.spEstado);
        Spinner spEstadocivil =
                (Spinner)findViewById(R.id.spEstadocivil);
        Spinner spGenero =
                (Spinner)findViewById(R.id.spGenero);
        EditText txtEndereco =
                (EditText)findViewById(R.id.txtEndereco);
        EditText txtEmail =
                (EditText)findViewById(R.id.txtEmail);

        ImageView image = (ImageView)
                findViewById(R.id.image);

        Intent it = getIntent();
        if (it != null && it.hasExtra("contato")) {

            contato = (Contato) it.
                    getSerializableExtra("contato");

            txtNome.setText(contato.getNome());
            spTipo.setSelection(
                    ((ArrayAdapter) spTipo.
                            getAdapter()).
                            getPosition(contato.getTipo()));
            txtTelefone.setText(contato.getTelefone());

            ByteArrayInputStream imageStream = new
                    ByteArrayInputStream(contato.getImagem());
            Bitmap bitmap = BitmapFactory.
                    decodeStream(imageStream);
            image.setImageBitmap(bitmap);
        }

        image = (ImageView)
                findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.
                        ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,
                        CAMERA_REQUEST);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contato, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save) {
            EditText txtNome =
                    (EditText) findViewById(R.id.txtNome);
            EditText txtTelefone =
                    (EditText) findViewById(R.id.txtTelefone);
            Spinner spTipo =
                    (Spinner) findViewById(R.id.spTipo);
            ImageView image = (ImageView)
                    findViewById(R.id.image);
            Spinner spEstado =
                    (Spinner)findViewById(R.id.spEstado);
            Spinner spEstadocivil =
                    (Spinner)findViewById(R.id.spEstadocivil);
            Spinner spGenero =
                    (Spinner)findViewById(R.id.spGenero);
            EditText txtEndereco =
                    (EditText)findViewById(R.id.txtEndereco);
            EditText txtEmail =
                    (EditText)findViewById(R.id.txtEmail);

            if (contato == null) {
                contato = new Contato();
            }
            contato.setNome(txtNome.getText().toString());
            contato.setTipo(spTipo.getSelectedItem().toString());
            contato.setTelefone(txtTelefone.getText().toString());
            //
            contato.setEmail(txtEmail.getText().toString());
            contato.setEndereco(txtEndereco.getText().toString());
            contato.setEstado(spEstado.getSelectedItem().toString());
            Bitmap bitmap = ((BitmapDrawable)
                    image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos =
                    new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,
                    100, baos);
            byte[] imageInByte = baos.toByteArray();
            contato.setImagem(imageInByte);

            new ContatoDao().salvar(contato);
            contato = null;

            Toast.makeText(getApplicationContext(),
                    "Salvo com sucesso!",
                    Toast.LENGTH_LONG).show();

            Intent it = new Intent(
                    ContatoActivity.this,
                    MainActivity.class);
            startActivity(it);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void ligar(View view) {
        EditText txtTelefone = (EditText)
                findViewById(R.id.txtTelefone);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" +
                txtTelefone.getText()));

        ActivityCompat.requestPermissions(
                ContatoActivity.this,
                new String[]{Manifest.
                        permission.CALL_PHONE},
                1);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }

    public void email(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(ContatoActivity.this);
        View promptView = layoutInflater.inflate(R.layout.dialog_email, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ContatoActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.txt_email);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //emailOfForm.setText();
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto",editText.getText().toString(), null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contato");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Olá");
                        startActivity(Intent.createChooser(emailIntent, "Email de contato"));
                    }
                })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();


    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (requestCode == CAMERA_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().
                    get("data");
            ImageView image = (ImageView)
                    findViewById(R.id.image);
            image.setImageBitmap(photo);
        }
    }

}
