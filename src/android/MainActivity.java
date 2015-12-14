package com.digitalvibes.swipeCards;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import org.apache.cordova.*;
import org.json.JSONObject;
import org.json.JSONException;


public class MainActivity extends CordovaActivity {

    private CardContainer mCardContainer;
    public static String PACKAGE_NAME;
    public static Resources RESOURCES;
    public static MainActivity myActivity;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        super.init();

        String package_name = getApplication().getPackageName();
        Resources resources = getApplication().getResources();
        PACKAGE_NAME = package_name;
        RESOURCES = resources;
        
        setContentView(resources.getIdentifier("mainlayout","layout",package_name));

        JSONObject cards = null;

        try {
            cards = new JSONObject(getIntent().getStringExtra("cards"));
            Log.i("SwipeCardsLength",new Integer(cards.length()).toString());
        } catch(JSONException e) {
            
        }

        final String successCallback = getIntent().getStringExtra("successCallback");
        final String failureCallback = getIntent().getStringExtra("failureCallback");

        mCardContainer = (CardContainer) findViewById(resources.getIdentifier("layoutview","id",package_name));

        Resources r = getResources();

        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);

        for(int i=0;i<cards.length();i++) {
            final int count  = i+1;
            try {
                JSONObject currentCard = cards.getJSONObject("item"+(i+1));
                Log.i("CardsTitle",currentCard.getString("title"));
                CardModel cardModel = new CardModel(currentCard.getString("title"), currentCard.getString("img"),mCardContainer);
                cardModel.setOnClickListener(new CardModel.OnClickListener() {
                    @Override
                    public void OnClickListener() {
                        Log.i("Swipeable Cards","I am pressing the card");

                    }
                });

                cardModel.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
                    @Override
                    public void onLike() {
                        //Log.i("Swipeable Cards", "javascript:" + successCallback + "();");
                        //appView.sendJavascript("alert('saddasds');");
                        
                        swipeCardsActivity.example.loadUrl("javascript:" + successCallback +
                         "("+count+");");

                        if(mCardContainer.getChildCount() == 1)
                            MainActivity.this.finish();
                    }

                    @Override
                    public void onDislike() {
                        //Log.i("Swipeable Cards","javascript:"+failureCallback+"();");
                        swipeCardsActivity.example.loadUrl("javascript:" + failureCallback +
                         "("+count+");");

                        if(mCardContainer.getChildCount() == 1)
                            MainActivity.this.finish();
                    }
                });
                
                adapter.add(cardModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mCardContainer.setAdapter(adapter);
    }




}

