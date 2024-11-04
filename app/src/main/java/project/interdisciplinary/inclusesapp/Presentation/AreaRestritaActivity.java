package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityAreaRestritaBinding;

public class AreaRestritaActivity extends AppCompatActivity {

    private ActivityAreaRestritaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityAreaRestritaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewAreaRestritaBackButton.setOnClickListener(v -> {
            finish();
        });

        binding.textViewAreaRestritaBack.setOnClickListener(v -> {
            finish();
        });

        //Definição da URL
        binding.webViewAreaRestrita.loadUrl("https://siteincluses.onrender.com/mobile");

        binding.webViewAreaRestrita.getSettings().setJavaScriptEnabled(true); //permite o uso do JavaScript

        //permite continuar abrindo as paginas no webView
        binding.webViewAreaRestrita.setWebViewClient(new WebViewClient(){
            //Oque este método faz? Ele vai mostrar o progress bar enquanto estiver carregando as paginas
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.progressBar.setVisibility(View.VISIBLE); // torna o progress bar visível
            }

            //Oque este método faz? Ele vai esconder o progress bar quando as paginas estiverem carregadas
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressBar.setVisibility(View.INVISIBLE);// torna o progress bar invisível
            }
        });


    }
    //Este método permite voltar para a pagina anterior. Ele identifica a tecla que foi selecionada
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && binding.webViewAreaRestrita.canGoBack() ){
            binding.webViewAreaRestrita.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}