import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.dmss.dmssevents.R;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;

/**
 * Created by sandeep.kumar on 21-03-2017.
 */
public class AlbumsActivity extends DmsEventsBaseActivity {
    GridView albumGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        albumGridView=(GridView)findViewById(R.id.albumGridView);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void actionBarSettings() {

    }
}
