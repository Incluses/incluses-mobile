package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import project.interdisciplinary.inclusesapp.R;

public class VideoPlayerActivity extends AppCompatActivity {
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = findViewById(R.id.videoView);
        String videoUri = getIntent().getStringExtra("videoUri");

        // Configure o MediaController
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        // Carregar o vídeo
        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.requestFocus();

        // Adicione um listener para iniciar a reprodução
        videoView.setOnPreparedListener(mp -> videoView.start());
    }
}
