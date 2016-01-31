package de.tum.score.transport4you.mobile.presentation.presentationmanager.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.graphics.Bitmap;
import net.glxn.qrgen.android.QRCode;
import android.widget.ImageView;
import de.tum.score.transport4you.mobile.presentation.presentationmanager.IPresentation;
import de.tum.score.transport4you.mobile.R;
/**
 * Created by Arpit on 29/01/16.
 * Class to generate the QR code encoding them with list object
 */
public class QR extends Activity implements IPresentation {

    private Context currentContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrdisplay);
        currentContext = this;
        Bitmap myBitmap = QRCode.from(message).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.imageView1);
        myImage.setImageBitmap(myBitmap);

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void updateProgessDialog(String title, String message, boolean visible, Integer increment) {

    }
}
