package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import project.interdisciplinary.inclusesapp.R;

public class VideoPlayerActivity extends AppCompatActivity {
    private VideoView videoView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        String videoUri = getIntent().getStringExtra("videoUri");
        Log.d("VideoPlayerActivity", "Video URI: " + videoUri);

        // Configure o MediaController
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // Carregar o vídeo
        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.requestFocus();

        // Mostrar o ProgressBar enquanto o vídeo está carregando
        videoView.setOnPreparedListener(mp -> {
            progressBar.setVisibility(View.GONE); // Oculta o ProgressBar quando o vídeo está pronto
            videoView.start();
        });

        // Oculte o ProgressBar se houver um erro
        videoView.setOnErrorListener((mp, what, extra) -> {
            progressBar.setVisibility(View.GONE); // Oculte o ProgressBar em caso de erro
            return true; // Retorne true se você tratou o erro
        });
    }
}
