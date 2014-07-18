package com.janderson.phonefinder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableStatusCodes;

import java.util.Collection;
import java.util.HashSet;

public class WearActivity extends Activity {

    private Node node;
    public static final String START_ACTIVITY_PATH = "/start/MainActivity";
    private TextView text;
    private boolean oddRotation;
    private CircledImageView circleButton;
    private GoogleApiClient googleApiClient;
    private boolean alreadyAnimated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();
        oddRotation = true;
        alreadyAnimated = true;
        sendMessage();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                text = (TextView) stub.findViewById(R.id.title);
                circleButton =
                        (CircledImageView) stub.findViewById(R.id.circle_button);
                circleButton.setCircleRadius(80);
                circleButton.setCircleColor(Color.parseColor("#009688"));
                circleButton.setImageResource(R.drawable.ic_bell);
                circleButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (oddRotation) {
                            circleButton.animate().rotation(360);
                            oddRotation = false;
                        } else {
                            circleButton.animate().rotation(0);
                            oddRotation = true;
                        }
                        alreadyAnimated = false;
                        sendMessage();
                        return false;
                    }
                });
            }
        });
    }


    protected void animateView(MessageApi.SendMessageResult sendMessageResult) {
        if (sendMessageResult.getStatus().isSuccess()) {
            text.animate().translationX(800).setStartDelay(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    text.setText("Your phone is now ringing");
                    text.setTranslationX(-800);
                    text.animate().translationX(0).setStartDelay(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            text.animate().translationX(800).setStartDelay(4000)
                                    .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    text.setText("Find my phone");
                                    text.setTranslationX(-800);
                                    text.animate().translationX(0).setStartDelay(0);
                                }
                            });
                        }
                    });
                }
            });
        } else {
            text.animate().translationX(800).withEndAction(new Runnable() {
                @Override
                public void run() {
                    text.setText("Could not contact your phone");
                    text.setTranslationX(-800);
                    text.animate().translationX(0);
                }
            });
        }
        alreadyAnimated = true;
    }

    private void animateCouldNotConnect() {
        if (!alreadyAnimated) {
            text.animate().translationX(800).setStartDelay(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    text.setText("Could not contact your phone");
                    text.setTranslationX(-800);
                    text.animate().translationX(0).setStartDelay(0).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            text.animate().translationX(800).setStartDelay(4000)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            text.setText("Find my phone");
                                            text.setTranslationX(-800);
                                            text.animate().translationX(0).setStartDelay(0);
                                        }
                                    });
                        }
                    });
                }
            });
        }
    }

    private void sendMessage() {
        PendingResult<NodeApi.GetConnectedNodesResult> nodes =
                Wearable.NodeApi.getConnectedNodes(googleApiClient);
        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult result) {
                for (int i = 0; i < result.getNodes().size(); i++) {
                    node = result.getNodes().get(i);

                    final PendingResult<MessageApi.SendMessageResult> messageResult =
                            Wearable.MessageApi.sendMessage(googleApiClient, node.getId(),
                                    "/start/MainActivity", null);
                    messageResult.setResultCallback(
                            new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            animateView(sendMessageResult);
                        }
                    });
                }
            }
        });
        animateCouldNotConnect();
    }

}
