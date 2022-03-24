package com.example.library_2;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MyAlertDialog extends AppCompatDialogFragment {

    Button add;
    EditText textAddition;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String file = "";
        String name = getArguments().getString("name");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(getActivity().openFileInput(name)));
            String str = "";
            while ((str = br.readLine()) != null) {
                file = file + str;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (file.equals("")) {
            View view = inflater.inflate(R.layout.fragment, null);
            builder.setView(view);

            add = view.findViewById(R.id.add);
            textAddition = view.findViewById(R.id.text_addition);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {

                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getActivity().openFileOutput(name, Context.MODE_PRIVATE)));
                        bw.write(textAddition.getText().toString());
                        bw.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            });

        } else {
            builder.setMessage(file);
        }
        return builder.setPositiveButton("Закрыть",null).create();

    }
}