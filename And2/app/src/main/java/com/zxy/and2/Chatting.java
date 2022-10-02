package com.zxy.and2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chatting extends BaseActivity {
    private List<Message> msgList = new ArrayList<>();
    private String name;
    private RecyclerView msgRecyclerView;
    private EditText message;
    private Button sendMessage;
    private MessageAdepter msgAdepter;
//    private String[] randomMsgList = new String[]{"爱一朵花，最好的方式是陪它盛开", "知所从来，思所将往，方明所去", "美好的时光正在路上···", "今天又是新的一天！", "人民有信仰，民族有希望，国家有力量", "你今天核酸做了吗？", "天气晴，宜收集快乐~", "普通小孩热爱生活中", "谢谢你陪伴我！", "好的~", "嗯嗯~", "然后呢？", "我也这么觉得", "yeah~"};
    private OkHttpClient client = new OkHttpClient();
    private TimerTask receiveMessageTask;
    private ScheduledExecutorService service;
    private ScheduledFuture<?> future;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Message.init();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        name = getIntent().getStringExtra("name");
        SharedPreferences pref_reader = getSharedPreferences(AppControlor.USER_TYPE.equals("1")?AppControlor.id+"_"+name:name+"_"+AppControlor.id, MODE_PRIVATE);
        SharedPreferences.Editor pref = pref_reader.edit();
//        pref.putInt("sum",pref_reader.getInt("sum",0));

        message = (EditText) findViewById(R.id.message);
        sendMessage = (Button) findViewById(R.id.send_message);
        msgRecyclerView = (RecyclerView) findViewById(R.id.message_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);

        int sum = pref_reader.getInt("sum", 0);
//        if (sum == 0) {
//            Message msg0 = new Message("你好，很高兴认识你", Message.TYPE_RECEIVED);
//            msgList.add(msg0);
//            pref.putInt("sum", 1);
//            pref.apply();
//            pref.putString("string1", "你好，很高兴认识你");
//            pref.putInt("type1", Message.TYPE_RECEIVED);
//            pref.apply();
//        } else {
            for (int i = 1; i <= sum; i++) {
                Message msg = new Message(pref_reader.getString("string" + i, null), pref_reader.getInt("type" + i, 0));
                msgList.add(msg);
            }
//        }
        msgAdepter = new MessageAdepter(msgList);
        msgRecyclerView.setAdapter(msgAdepter);
        msgRecyclerView.setItemAnimator(new DefaultItemAnimator());
        msgRecyclerView.scrollToPosition(msgList.size() - 1);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if (!message.getText().toString().equals("")) {
                    Message msg = new Message(message.getText().toString(), Message.TYPE_SENT);
                    msgList.add(msg);
                    int sum = pref_reader.getInt("sum", 0) + 1;
                    pref.putInt("sum", sum);
                    pref.apply();
                    pref.putString("string" + sum, message.getText().toString());
                    pref.putInt("type" + sum, Message.TYPE_SENT);
                    pref.apply();

//                    msgAdepter.notifyDataSetChanged();
                    msgAdepter.notifyItemInserted(msgList.size());
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
                    message.setText("");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                RequestBody rb = new FormBody.Builder()
                                        .add(AppControlor.USER_TYPE.equals("1")?"this":"that", AppControlor.id)
                                        .add(AppControlor.USER_TYPE.equals("1")?"that":"this", name)
                                        .add("type", AppControlor.USER_TYPE)
                                        .add("message", msg.getContent())
                                        .build();
                                Request request = new Request.Builder().url("http://" + AppControlor.serverIp + "/send_message").post(rb).build();
                                client.newCall(request).execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

//                    Timer timer = new Timer();
//                    TimerTask timerTask = new TimerTask() {
//                        @Override
//                        public void run() {
//                            msgRecyclerView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Message msg1 = new Message(randomMsgList[(int) (randomMsgList.length * Math.random())], Message.TYPE_RECEIVED);
//                                    msgList.add(msg1);
//                                    int sum = pref_reader.getInt("sum", 0) + 1;
//                                    pref.putInt("sum", sum);
//                                    pref.apply();
//                                    pref.putString("string" + sum, msg1.getContent());
//                                    pref.putInt("type" + sum, Message.TYPE_RECEIVED);
//                                    pref.apply();
//                                    msgAdepter.notifyItemInserted(msgList.size());
//                                    msgRecyclerView.scrollToPosition(msgList.size() - 1);
//                                }
//                            });
//                        }
//                    };
//                    timer.schedule(timerTask, 500);
                }
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        msgRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 300);
            }
        });

        Button messageReturn = (Button) findViewById(R.id.message_return);
        messageReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button messageMenu = (Button) findViewById(R.id.message_menu);
        messageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Chatting.this,Settings.class);
//                startActivity(intent);
            }
        });

        receiveMessageTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    RequestBody rb = new FormBody.Builder()
                            .add(AppControlor.USER_TYPE.equals("1")?"this":"that", AppControlor.id)
                            .add(AppControlor.USER_TYPE.equals("1")?"that":"this", name)
                            .add("type",AppControlor.USER_TYPE)
                            .add("sum",pref_reader.getInt("sum",0)+1+"")
                            .build();
                    Request request = new Request.Builder()
                            .url("http://" + AppControlor.serverIp + "/get_messages")
                            .post(rb)
                            .build();
                    Response response = client.newCall(request).execute();
                    Gson gson = new Gson();
                    List<Message> tmpList = gson.fromJson(response.body().string(), new TypeToken<List<Message>>() {}.getType());
                    if (tmpList != null) {
                        msgRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                for (Message msg1 : tmpList) {
                                    msgList.add(msg1);
                                    int sum = pref_reader.getInt("sum", 0) + 1;
                                    pref.putInt("sum", sum);
                                    pref.apply();
                                    pref.putString("string" + sum, msg1.getContent());
                                    pref.putInt("type" + sum, msg1.getType());
                                    pref.apply();
                                    msgAdepter.notifyItemInserted(msgList.size());
                                }
                                msgRecyclerView.scrollToPosition(msgList.size() - 1);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        service = Executors.newScheduledThreadPool(1);
        future = service.scheduleAtFixedRate(receiveMessageTask, 1, 2, TimeUnit.SECONDS);
    }

    @Override
    protected void onPause() {
        if (future != null && !future.isCancelled()) future.cancel(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (future == null || future.isCancelled())
            future = service.scheduleAtFixedRate(receiveMessageTask, 1, 2, TimeUnit.SECONDS);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (future != null && !future.isCancelled()) future.cancel(false);
        if (service != null && !service.isShutdown()) service.shutdown();
        super.onDestroy();
    }
}