package com.example.nikhil.ice9;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nikhil.ice9.Fragments.MyFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONArray> , Response.ErrorListener, View.OnClickListener {
    RequestQueue queue;
    private static final String TAG = "MainActivity";
    Map<String  , String> params;
    private static String URL = "https://www.careerdec.net/android/api";
    private static final String PARAM_KEY  = "token" ;
    private String  TOKEN = null;
    Button openButton;
    MyFragment myFragment;
    RelativeLayout main_layout;
    LinearLayout sliding_layout;


    RelativeLayout rl_footer;
    ImageView iv_header;
    boolean isBottom = true;
    Button btn1;
    Animation mSlideInTop , mSlideOutTop,mSlideInBottom , mSlideOutBottom , noChange;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_animation);
        params = new HashMap<>();

        TOKEN = Ice9Application.getInstance(this).API_KEY;

        mSlideInTop = AnimationUtils.loadAnimation(this, R.anim.in_top);
        mSlideOutTop = AnimationUtils.loadAnimation(this, R.anim.out_top);
        mSlideInBottom = AnimationUtils.loadAnimation(this, R.anim.in_bottom);
        mSlideOutBottom = AnimationUtils.loadAnimation(this, R.anim.out_bottom);
        noChange = AnimationUtils.loadAnimation(this , R.anim.no_change);

        /*rl_footer = (RelativeLayout) findViewById(R.id.rl_footer);
        iv_header = (ImageView) findViewById(R.id.iv_up_arrow);

        iv_header.setOnClickListener(this);

*/       sliding_layout  = (LinearLayout) findViewById(R.id.sliding_layout);
        main_layout = (RelativeLayout) findViewById(R.id.main_layout);
       queue = Ice9Application.getInstance(this.getApplicationContext()).
                getRequestQueue();
        openButton = (Button) findViewById(R.id.slider_open);
        openButton.setOnClickListener(this);
        //myFragment = new MyFragment();
       // requestDataForList();




    }
    private void requestDataForList(){
        JsonArrayRequest listDataRequest = new JsonArrayRequest(Request.Method.GET, URL,this,
               this){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put(PARAM_KEY ,TOKEN );
                return params;
            }

        };

        Ice9Application.getInstance(this).addToRequestQueue(listDataRequest);


    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONArray response) {
        Toast.makeText(getApplicationContext() , response.toString() , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        //Fragment checkFragment = getFragmentManager().findFragmentByTag(getResources().getString(R.string.my_fragment_tag));
        Log.d(TAG, "onBackPressed: ");
        if(sliding_layout.getVisibility() == View.VISIBLE){
            sliding_layout.startAnimation(mSlideOutBottom);
            sliding_layout.setVisibility(View.GONE);
            openButton.setText("OPEN");
            main_layout.startAnimation(mSlideInBottom);
            main_layout.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }


/*        if(checkFragment != null &&checkFragment.isAdded())
        {
            getFragmentManager().popBackStackImmediate();
        }
        else {
            super.onBackPressed();
        }
*/

    }

    //Fragment code
    /*
* Fragment checkFragment = getFragmentManager().findFragmentByTag(getResources().getString(R.string.my_fragment_tag));
                Log.d(TAG, "onClick: " + checkFragment);
                if(checkFragment == null ){


                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.slide_up,
                                R.animator.slide_down,
                                R.animator.slide_up,
                                R.animator.slide_down)
                        .replace(R.id.container , myFragment,getResources().getString(R.string.my_fragment_tag))

                        .addToBackStack(null)

                        .commit();

            }
                else if(!(checkFragment.isAdded())){
                    return;
                }

*
* */

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.slider_open:
                if(sliding_layout.getVisibility() == View.GONE) {

                    sliding_layout.startAnimation(mSlideInBottom);
                    sliding_layout.setVisibility(View.VISIBLE);
                    openButton.setText("CLOSE");
                    main_layout.startAnimation(mSlideOutBottom);
                    main_layout.setVisibility(View.GONE);
                }
                else {

                    sliding_layout.startAnimation(mSlideOutBottom);
                    sliding_layout.setVisibility(View.GONE);
                    openButton.setText("OPEN");
                    main_layout.startAnimation(mSlideInBottom);
                    main_layout.setVisibility(View.VISIBLE);
                }

                break;
            /*
            *  case R.id.iv_up_arrow:
                iv_header.setImageResource(R.drawable.common_google_signin_btn_icon_dark_disabled);
                iv_header.setPadding(0, 10, 0, 0);
                rl_footer.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_disabled);
                if (isBottom) {
                    SlideToAbove();
                    isBottom = false;
                } else {
                    iv_header.setImageResource(R.drawable.common_google_signin_btn_icon_dark_disabled);
                    iv_header.setPadding(0, 0, 0, 10);
                    rl_footer.setBackgroundResource(R.drawable.common_google_signin_btn_icon_dark_disabled);
                    SlideToDown();
                    isBottom = true;
                }
            * */
            /*case R.id.list_activity_open:
                Intent listActivityIntent = new Intent(this , ListActivity.class);
                startActivity(listActivityIntent);
                overridePendingTransition(R.anim.slide_up_info,R.anim.no_change);
                break;
                */
        }
    }
    /*
    public void SlideToAbove() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -5.0f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        rl_footer.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                rl_footer.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        rl_footer.getWidth(), rl_footer.getHeight());
                // lp.setMargins(0, 0, 0, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rl_footer.setLayoutParams(lp);

            }

        });

    }

    public void SlideToDown() {
        Animation slide = null;
        slide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 5.2f);

        slide.setDuration(400);
        slide.setFillAfter(true);
        slide.setFillEnabled(true);
        rl_footer.startAnimation(slide);

        slide.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                rl_footer.clearAnimation();

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        rl_footer.getWidth(), rl_footer.getHeight());
                lp.setMargins(0, rl_footer.getWidth(), 0, 0);
                lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rl_footer.setLayoutParams(lp);

            }

        });

    }*/
}
