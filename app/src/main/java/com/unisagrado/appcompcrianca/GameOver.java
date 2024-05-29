package com.unisagrado.appcompcrianca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class GameOver extends AppCompatActivity {
    TextView tvPoints, tvPersonalBest, textoFim;

    SharedPreferences sharedPreferences;

    ImageView overimg;

    ImageButton exit, retry;

    Button trofeuBtn;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        int points = getIntent().getExtras().getInt("pontos");
        tvPoints = findViewById(R.id.tvPoints);
        textoFim = findViewById(R.id.textofim);
        tvPersonalBest = findViewById(R.id.tvPersonalBest);
        overimg = findViewById(R.id.overimg);
        exit = findViewById(R.id.exit);
        retry = findViewById(R.id.retry);
        trofeuBtn = findViewById(R.id.trofeusBtn);
        boolean trophyLanguages;

        GlobalVariables globalVariables = (GlobalVariables) getApplicationContext();

        exit.setVisibility(View.VISIBLE);
        retry.setVisibility(View.VISIBLE);

        tvPoints.setText("" + points);
        sharedPreferences = getSharedPreferences("pref", 0);

        int pointsSP = sharedPreferences.getInt("pointsSP", 0);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(points > pointsSP){
            pointsSP = points;
            editor.putInt("pointsSP", pointsSP);
            editor.commit();
        }
        tvPersonalBest.setText("" + pointsSP);

        if(points == 8){
            overimg.setImageResource(R.drawable.trophy);
            textoFim.setText("Parabéns, você acertou tudo!");
            exit.setVisibility(View.INVISIBLE);
            retry.setVisibility(View.INVISIBLE);
            trofeuBtn.setVisibility(View.VISIBLE);
            trophyLanguages = true;
            updateData(trophyLanguages);

        }
        else{
            overimg.setImageResource(R.drawable.game_over);
            textoFim.setText("Tente novamente!");
        }

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOver.this, opcoes.class);
                startActivity(intent);
            }
        });

        trofeuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOver.this, Trophy.class);
                startActivity(intent);
            }
        });
    }

    public void restart(View view) {
        // Create an Intent object to launch StartGame Activity
        Intent intent = new Intent(GameOver.this, StartGame.class);
        startActivity(intent);
        // Finish GameOver Activity
        finish();
    }

    public void exit(View view) {
        // Define what should happen when the exit button is clicked
        // For example, you can navigate to another activity or close the app
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }

    private void updateData(boolean trophyLanguages){
        GlobalVariables globalVariables = (GlobalVariables) getApplicationContext();
        HashMap trophy = new HashMap();
        trophy.put("trophyLanguages", trophyLanguages);
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(globalVariables.getUserName()).updateChildren(trophy).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(GameOver.this, "Parabéns!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameOver.this, "Erro", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}