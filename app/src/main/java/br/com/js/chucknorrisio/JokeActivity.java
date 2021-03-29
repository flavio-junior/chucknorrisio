package br.com.js.chucknorrisio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import br.com.js.chucknorrisio.datasource.JokeRemoteDataSource;
import br.com.js.chucknorrisio.model.Joke;
import br.com.js.chucknorrisio.presentation.JokePresenter;

public class JokeActivity extends AppCompatActivity {

    static final String CATEGORY_KEY = "category_key";
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getExtras() != null) {
            String category = getIntent().getExtras().getString(CATEGORY_KEY);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(category);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                JokeRemoteDataSource dataSource = new JokeRemoteDataSource();
                final JokePresenter presenter = new JokePresenter(this, dataSource);
                presenter.findJokeBy(category);

                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(view -> {
                    presenter.findJokeBy(category);
                });
            }
        }
    }

    public void showJoke(Joke joke) {
        TextView txtJoke = findViewById(R.id.txt_joke);
        txtJoke.setText(joke.getValue());

        ImageView iv = findViewById(R.id.img_icon);
        Picasso.get().load(joke.getIconUrl()).into(iv);
    }


    public void showFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public void showProgressBar() {
        if (progress == null) {
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.loading));
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }
        progress.show();
    }

    public void hideProgressBar() {
        if (progress != null) {
            progress.hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }
}