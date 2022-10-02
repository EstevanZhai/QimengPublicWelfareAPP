package com.zxy.and2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxy.and2.placeholder.VolPlaceholderContent;

import java.util.Timer;
import java.util.TimerTask;


public class VolunteeringFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;


    public VolunteeringFragment() {
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
        View view = inflater.inflate(R.layout.fragment_volunteering_list, container, false);

        AppControlor.setStatusBarMargin(requireActivity(), view.findViewById(R.id.vol_toolbar));
//        AppControlor.setNavigationBarMargin(requireActivity(), view.findViewById(R.id.vol_radiogroup));

        RecyclerView recyclerView = view.findViewById(R.id.vol_list);
        // Set the adapter
        if (recyclerView != null) {
            Context context = recyclerView.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            MyVolunteeringItemRecyclerViewAdapter volAdapter = new MyVolunteeringItemRecyclerViewAdapter(VolPlaceholderContent.ITEMS);
            volAdapter.setOnRecyclerViewItemClickListener(new MyVolunteeringItemRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View view, int position) {
                    Intent intent = new Intent(getActivity(), VolDetailActivity.class);
                    intent.putExtra("position",position);
                    intent.putExtra("title", ((TextView) (view.findViewById(R.id.vol_title))).getText().toString());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(volAdapter);
        }

        Button web1 = view.findViewById(R.id.web1);
        web1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), WebActivity.class);
                intent.putExtra("url", "http://www.pubchn.com/wap/");
                startActivity(intent);
            }
        });
        Button web2 = view.findViewById(R.id.web2);
        web2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), WebActivity.class);
                intent.putExtra("url", "https://m.gongyi.baidu.com/index/index.html#/all?pro=3&from=index");
                startActivity(intent);
            }
        });
        Button web3 = view.findViewById(R.id.web3);
        web3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), WebActivity.class);
                intent.putExtra("url", "https://gongyi.youku.com/?refer=qita_market.qrwang_00003026_000000_ZNf6ji_19041800");
                startActivity(intent);
            }
        });
        Button web4 = view.findViewById(R.id.web4);
        web4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), WebActivity.class);
                intent.putExtra("url", "http://www.cpwnews.com/index.php?m=wap&siteid=1");
                startActivity(intent);
            }
        });

        return view;
    }
}