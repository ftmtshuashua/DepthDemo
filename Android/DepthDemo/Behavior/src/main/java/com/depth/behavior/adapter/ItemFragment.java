package com.depth.behavior.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.depth.behavior.R;

import java.text.MessageFormat;

public class ItemFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup viewById = view.findViewById(R.id.layout_Content);

        int max = (int) (Math.random() * 10)+10;
        for (int i = 0; i < max; i++) {
            TextView inflate = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.layout_item_textview, viewById, false);
            inflate.setText(MessageFormat.format("假装顶部：{0}  -> MAX：{1}", i, max-1));

            viewById.addView(inflate);
        }

    }


}
