package com.zxy.and2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zxy.and2.placeholder.CpnPlaceholderContent;
import com.zxy.and2.placeholder.VolPlaceholderContent;


public class CompanionFragment extends Fragment {

    private int mColumnCount = 1;

    private static final String ARG_COLUMN_COUNT = "column-count";


    public CompanionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_companion_list, container, false);

        AppControlor.setStatusBarMargin(requireActivity(),view.findViewById(R.id.cpn_toolbar));

        RecyclerView recyclerView = view.findViewById(R.id.cpn_list);
        // Set the adapter
        if (recyclerView!=null) {
            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            CpnPlaceholderContent.init();
            MyCompanionItemRecyclerViewAdapter volAdapter = new MyCompanionItemRecyclerViewAdapter(CpnPlaceholderContent.ITEMS);
            volAdapter.setOnRecyclerViewItemClickListener(new MyCompanionItemRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View view, int position) {
                    Intent intent = new Intent(getActivity(),Chatting.class);
                    intent.putExtra("name",((TextView)(view.findViewById(R.id.cpn_name))).getText().toString());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(volAdapter);
        }

//        Button square = view.findViewById(R.id.cpn_square);
//        square.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(requireActivity(),SquareActivity.class);
//            }
//        });

        return view;
    }
}