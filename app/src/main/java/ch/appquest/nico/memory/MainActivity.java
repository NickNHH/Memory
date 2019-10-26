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
    Bitmap bitmap;
    List<String[]> solutionList = new ArrayList<>();
    Context context = this;
    private LogBook logging = new LogBook();
    static List<String> imagePaths = new ArrayList<>();
    static List<String> textStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new ContactsAdapter();
        rv.setAdapter(mAdapter);

        final Button takePicBtn = findViewById(R.id.takePictureButton);
        final ImageView imageView = findViewById(R.id.showPic);

        // get picture button
        takePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeQrCodePicture();
                imageView.setImageBitmap(bitmap);
            }
        });

        final EditText solutionWord1 = findViewById(R.id.solution1);
        final EditText solutionWord2 = findViewById(R.id.solution2);
        final Button addSolutionBtn = findViewById(R.id.solutionButton);

        addSolutionBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveSolution(solutionWord1.getText().toString(), solutionWord2.getText().toString());
                solutionWord1.setText("");
                solutionWord2.setText("");
            }
        });

        final Button logBookBtn = findViewById(R.id.addLogbook);

        logBookBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean logbookInstalled = logging.checkIfLogbookInstalled(context);
                if (logbookInstalled) {
                    logging.passDataToLogbook(context, solutionList);
                }
            }
        });

        // get gallery button
        Button galleryBtn = findViewById(R.id.choosePictureButton);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implement method
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            assert extras != null;
            String path = extras.getString(Intents.Scan.RESULT_BARCODE_IMAGE_PATH);

            String code = extras.getString(Intents.Scan.RESULT);

            bitmap  = outputBitmap(path);
            String solution = outputCode(code);

            imagePaths.add(path);
            textStrings.add(solution);

            finish();
            startActivity(getIntent());
        }
    }

    public void takeQrCodePicture() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(MyCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
        integrator.initiateScan();
    }

    private Bitmap outputBitmap(String path){
        return BitmapFactory.decodeFile(path);
    }

    private String outputCode(String code){ return code; }

    private void saveSolution(String solution1, String solution2){
        String[] wordSet = new String[2];
        wordSet[0] = solution1;
        wordSet[1] = solution2;
        solutionList.add(wordSet);
    }
}
