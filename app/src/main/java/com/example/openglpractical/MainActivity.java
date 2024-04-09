package com.example.openglpractical;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    // the view
    private OpenGLView myOpenGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // call the view layout --> renderer --> draw
        System.out.println("in MainActivity");
        myOpenGLView = (OpenGLView) findViewById(R.id.myopenglview);
    }

    @Override
    protected void onPause() {
        super.onPause();
        myOpenGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myOpenGLView.onResume();
    }
}