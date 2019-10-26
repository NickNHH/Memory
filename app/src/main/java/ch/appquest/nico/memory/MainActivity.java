package ch.appquest.nico.memory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Bitmap bild;
    private List<String[]> loesungsListe = new ArrayList<>();
    Context context = this;
    private LogBook logging = new LogBook();
    static List<String> imagePaths = new ArrayList<>();
    static List<String> textStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new ContactsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        final Button fotoAufnehmenButton = findViewById(R.id.takePictureButton);
        final ImageView bildAnzeigen = findViewById(R.id.showPic);

        fotoAufnehmenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                qrCodeBildMachen();
                bildAnzeigen.setImageBitmap(bild);
            }
        });

        final EditText loesungsText = findViewById(R.id.solution1);
        final EditText loesungsText2 = findViewById(R.id.solution2);

        final Button loesungArrayErstellen = findViewById(R.id.solutionButton);
                loesungArrayErstellen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loesungSpeichern(loesungsText.getText().toString(), loesungsText2.getText().toString());
                loesungsText.setText("");
                loesungsText2.setText("");
            }
        });

        final Button logBuchButton = findViewById(R.id.addLogbook);

        logBuchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean logbookInstalled = logging.checkIfLogbookInstalled(context);
                if (logbookInstalled) {
                    logging.passDataToLogbook(context, loesungsListe);
                }
            }
        });
    }

    public void qrCodeBildMachen() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(MyCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            assert extras != null;
            String path = extras.getString(Intents.Scan.RESULT_BARCODE_IMAGE_PATH);

            String code = extras.getString(Intents.Scan.RESULT);

            bild  = bitmapAusgeben(path);
            String loesungsWort = codeAusgeben(code);

            imagePaths.add(path);
            textStrings.add(loesungsWort);

            finish();
            startActivity(getIntent());
        }
    }

    private Bitmap bitmapAusgeben(String path){
        return BitmapFactory.decodeFile(path);
    }

    private String codeAusgeben(String code){ return code; }

    private void loesungSpeichern(String loesung, String loesung2){
        String[] wortPaar = new String[2];
       wortPaar[0] = loesung;
       wortPaar[1] = loesung2;
       loesungsListe.add(wortPaar);
    }
}
