package com.example.proyectofinciclo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinciclo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarioFragment extends Fragment {

    public CalendarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendario, container, false);
    }
}