package net.is.ps.addameer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.is.ps.addameer.R.layout.listrow_catagories;


public class CatagoriesFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView listView;

    private SwipeLayout swipeLayout;

    public String getLanguageCurrent() {
        Locale current = getResources().getConfiguration().locale;

        if ("en_US".equals(current.toString()) || "en_us".equals(current.toString())) {
            return "en";

        } else {
            return "ar";
        }
    }
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_catagories, container, false);

        Bundle b = getArguments();
        String s = b.getString("key1");
        listView = (ListView) rootView.findViewById(R.id.lst1);
      //  String url = "http://195.154.226.224/~aldameer/api/v1/news-cat/"+s+"/en";
        String url = Url_language.getBaseUrl()+"news-cat/"+s+"/"+getLanguageCurrent();

        final String device_type = "android";
        final String user_token = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config); // Do it on Application start


        final StringRequest postsrequest3 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //     Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    String encodedstring = URLEncoder.encode(response, "ISO-8859-1");
                    response = URLDecoder.decode(encodedstring, "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                List<Catagories> itemsList = new ArrayList<>();
                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray parentArray =parentObject.getJSONArray("items");
                    Gson gson = new Gson();
                    for(int i=0;i<parentArray.length();i++){

                        JSONObject finalObject = parentArray.getJSONObject(i);
                        Catagories item = gson.fromJson(finalObject.toString(),Catagories.class);
                        itemsList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/JF-Flat-regular.ttf");


             //without FAVORIT   CatagoriesFragmentAdapter adapter = new CatagoriesFragmentAdapter(getContext(), trylistrowforcatagories,itemsList,typeFace);
                CatagoriesFragmentAdapter adapter = new CatagoriesFragmentAdapter(getContext(), listrow_catagories,itemsList,typeFace);

                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        Catagories CatObject = (Catagories) parent.getAdapter().getItem(position);
                        String questionId= String.valueOf(CatObject.getId());
                         //  Toast.makeText(getContext(),questionId,Toast.LENGTH_LONG).show();

/*


                        OneNewsFragment oneNewsFragment = new OneNewsFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.container, oneNewsFragment );;

                        transaction.addToBackStack("cat");

                        Bundle bundle=new Bundle();
                        bundle.putString("key1",questionId);
                        oneNewsFragment.setArguments(bundle);

                        transaction.commit();
*/


                    }
                });


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap map = new HashMap();
                map.put("device_type", device_type);
                map.put("user_token", user_token);
                return map;
            }

        };
        Volley.newRequestQueue(getContext()).add(postsrequest3);



        return rootView;
    }



    public void onItemClick(AdapterView<?>arg0, View arg1 , int arg2, long arg3){
//        Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();

    }



}











