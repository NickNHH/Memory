package ch.appquest.nico.memory;

import android.content.Context;
import android.content.ContextWrapper;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private List<String[]> solutions = new ArrayList<>();
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

        final Button takePictureBtn = findViewById(R.id.takePictureButton);
        final ImageView showPicture = findViewById(R.id.showPic);

        takePictureBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takeQrCodePicture();
                showPicture.setImageBitmap(bitmap);
            }
        });

        final EditText solutionWord1 = findViewById(R.id.solution1);
        final EditText solutionWord2 = findViewById(R.id.solution2);

        final Button addToArrayBtn = findViewById(R.id.solutionButton);
        addToArrayBtn.setOnClickListener(new View.OnClickListener() {
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
                    logging.passDataToLogbook(context, solutions);
                }
            }
        });
    }

    public void takeQrCodePicture() {
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

            bitmap = BitmapFactory.decodeFile(path);
            String savedIMGPath = saveToInternalStorage(bitmap);
            loadImageFromStorage(savedIMGPath);

            imagePaths.add(path);
            textStrings.add(code);

            finish();
            startActivity(getIntent());
        }
    }

    private void saveSolution(String solution1, String solution2) {
        String[] wortPaar = new String[2];
        wortPaar[0] = solution1;
        wortPaar[1] = solution2;
        solutions.add(wortPaar);
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path) {
        try {
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = findViewById(R.id.showPic);
            img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
