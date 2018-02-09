package krishnas.imageuploadfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private StorageReference mstorage;
    private static final int GALLERY_PICK = 2;
    private Button mbutton;
    private ProgressDialog mprogress;
    private ImageView mimgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mstorage = FirebaseStorage.getInstance().getReference();
        mprogress=new ProgressDialog(this);
        mbutton = (Button) findViewById(R.id.button);
        mimgview=(ImageView)findViewById(R.id.imageView);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_PICK);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            mprogress.setMessage("UpLoaDing.............");
            mprogress.show();
            Uri uri = data.getData();
            StorageReference filepath =mstorage.child("photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloaduri=taskSnapshot.getDownloadUrl();
                    Picasso.with(MainActivity.this).load(downloaduri).fit().centerCrop().into(mimgview);

                    Toast.makeText(MainActivity.this,"image is uploaded",Toast.LENGTH_LONG).show();
                    mprogress.dismiss();
                }
            });

        }
    }
}