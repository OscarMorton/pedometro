package ml.oscarmorton.pedometro;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WalksActivity extends AppCompatActivity {

    public static String EXTRA_TEXTO = "ml.oscarmorton.pedometro.EXTRA_TEXTO";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walks);
        FragmentList list = (FragmentList)getSupportFragmentManager().findFragmentById(R.id.FrgList);
    }
}
