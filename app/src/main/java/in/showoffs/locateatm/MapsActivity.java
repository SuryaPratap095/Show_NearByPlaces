package in.showoffs.locateatm;


import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import in.showoffs.locateatm.CustomProgressBar.CustomProgressDialog;
import in.showoffs.locateatm.NavigationDrawarItem.AccountActivity;
import in.showoffs.locateatm.Place.Place_JSON;
import in.showoffs.locateatm.RecyclerView.RecycleviewAdapter;
import in.showoffs.locateatm.RecyclerView.Review;
import in.showoffs.locateatm.dialogFragment.MyDialogFragment;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,MyDialogFragment.OnDialogFragmentInteractionListener,NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MapsActivity";

    private SlidingUpPanelLayout mLayout;

    LinearLayout lAbout,lReview,lPhoto,slidingLayout;

    private GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    private double latitude;
    private double longitude;
    private CustomProgressDialog mCustomProgressDialog;
    private boolean flag = true;
    Boolean check = false;
    Boolean checkMark = false;
    EditText category,searchPlace;
    ImageButton btn_searchPlace,btn_backTosearchPlace;
    Button btn_searchCategory,btn_phone_call;
    RelativeLayout relativeLayoutPlacesearch,relativeLayoutCategorySearch;
    ArrayList<String> catList;
    MyDialogFragment dialogFragment;
    int values = 1;
    int width;
    String address = "",tempDist = "",tempTitle = "";
    LatLng latLng;
    LatLng origin = null;
    LatLng dest = null;
    ArrayList<LatLng> markerPoints;
    String[] mPlaceTypeName=null;
    Bitmap myBitmap;
    Marker marker = null;
    Circle myCircle;
    Marker dragMarker = null,marker2=null;
    Marker m = null;
    Spinner spinner;
    Button currentLocation,rangeButton,cancle,ok;
    TextView textView,rangeText,placeName,placeAddress,placeContact;
    RatingBar placeRating;
    LinearLayout linearLayout,linearLayoutSlidingBar,lPlaceContact;
    RadioGroup radioGroup;
    RadioButton drivingMode,walkingMode;
    CheckBox checkBoxTraffic,checkBoxSatellite;
    LinearLayout layout;
    List list1,list2;
    Polyline line = null;
    List<Polyline> polylines = new ArrayList<Polyline>();
    Toolbar appBarLayout;
    LinearLayout titleT;
    ImageView drivingModeImage,walkingModeImage;
    String getTag = null;
    RecyclerView recyclerView;
    SharedPreferences preferences;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomProgressDialog = new CustomProgressDialog(this);
        mCustomProgressDialog.show("");

        btn_phone_call = (Button)findViewById(R.id.phoneCall);
        lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
        setContentView(R.layout.activity_navigation_drawer);
        recyclerView = (RecyclerView)findViewById(R.id.reviewLayout);
        drivingModeImage = (ImageView)findViewById(R.id.drivingMode);
        walkingModeImage = (ImageView)findViewById(R.id.walkingMode);
        placeName = (TextView)findViewById(R.id.placeName);
        placeAddress = (TextView)findViewById(R.id.placeAddress);
        placeRating = (RatingBar)findViewById(R.id.placeRatingBar);
        placeContact = (TextView)findViewById(R.id.placeContact);
        appBarLayout = (Toolbar)findViewById(R.id.toolbar);
        titleT = (LinearLayout)findViewById(R.id.titleT);
        layout = (LinearLayout) findViewById(R.id.linear);
        lAbout = (LinearLayout)findViewById(R.id.aboutLinearLayout);
        lReview = (LinearLayout)findViewById(R.id.reviewLinearLayout);
        lPhoto = (LinearLayout)findViewById(R.id.photoLinearLayout);
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //mLayout.setEnabled(false);
        //mLayout.setVisibility(View.GONE);
        mLayout.setPanelHeight(0);
        slidingLayout = (LinearLayout)findViewById(R.id.slidingLayout);
        slidingLayout.setEnabled(false);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        int height = size.y;

        Log.e("width",""+width);
        Log.e("height", ""+height);
        Log.e("display", ""+display);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        relativeLayoutPlacesearch = (RelativeLayout) findViewById(R.id.RL_placeSearch);
        relativeLayoutCategorySearch = (RelativeLayout) findViewById(R.id.RL_categorySearch);
        searchPlace = (EditText) findViewById(R.id.ed_searchPlace);
        btn_searchPlace = (ImageButton) findViewById(R.id.iBtn_searchPlace);

        btn_searchCategory = (Button) findViewById(R.id.iBtn_selectCategory);
        btn_backTosearchPlace = (ImageButton) findViewById(R.id.iBtn_backToSearchPlace);


        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation) {
            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        } else {
            gpsTracker.showSettingsAlert();
        }
        try {
            if (Build.VERSION.SDK_INT<5.0&&Build.VERSION.SDK_INT>4.0)
            {
                getWindow().setStatusBarColor(Color.BLUE);
            }
            else {
                getWindow().setStatusBarColor(Color.BLUE);
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }catch (Exception e){}
        list1 = new ArrayList<String>();
        list2 = new ArrayList<String>();
        mPlaceTypeName = getResources().getStringArray(R.array.place_type_name);
        textView = (TextView) findViewById(R.id.showTime);

        btn_searchCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutCategorySearch.setVisibility(View.VISIBLE);
                relativeLayoutPlacesearch.setVisibility(View.GONE);
            }
        });

        btn_backTosearchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutPlacesearch.setVisibility(View.VISIBLE);
                relativeLayoutCategorySearch.setVisibility(View.GONE);
                map.clear();
                getCurrentLocation();
            }
        });

        btn_searchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String str = searchPlace.getText().toString();
                    if (str != null && !str.equals("")) {
                        new GeocoderTask().execute(str);
                    } else {
                        //Toast.makeText(getBaseContext(), "Enter place to search...", Toast.LENGTH_LONG).show();
                        Snackbar.make(v, "Enter place to search...", Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // access enter button of softInputKeyboard
        searchPlace.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) ||
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        //do something
                        //true because you handle the event
                        try {
                            String str = searchPlace.getText().toString();
                            if (str != null && !str.equals("")) {
                                new GeocoderTask().execute(str);
                            } else
                                //Toast.makeText(getBaseContext(), "Enter place to search...", Toast.LENGTH_LONG).show();
                                Snackbar.make(v,"Enter place to search...",Snackbar.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return true;
                    }
                return false;
            }
        });

        category = (EditText) findViewById(R.id.input_category);
        catList = new ArrayList<String>(Arrays.asList(mPlaceTypeName));
        dialogFragment = MyDialogFragment.newInstance(catList, "Select category");
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.show(getFragmentManager(), "abc");
            }
        });

        markerPoints = new ArrayList<LatLng>();
        currentLocation = (Button) findViewById(R.id.currentLocation);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
                if (gpsTracker.canGetLocation) {
                    //latLng = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
                } else {
                    gpsTracker.showSettingsAlert();
                }
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to location user
                        .zoom(17)           // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .build();                   // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1100, null);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                      /*  Toast.makeText(MapsActivity.this, "Hu hu ha ha ", Toast.LENGTH_SHORT).show();*/
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                       /* Toast.makeText(MapsActivity.this, "He he ha u hu", Toast.LENGTH_SHORT).show();*/
                    }
                })
                .addApi(AppIndex.API).build();

        final SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map = fm.getMap();
        mapFragment.getMap().setPadding(dpToPx(48), dpToPx(50), 0, 0);

      /*  spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                linearLayout.setVisibility(View.GONE);
                mCustomProgressDialog.show("");
                map.clear();
                getCurrentLocation();
                sbMethod(((getResources().getStringArray(R.array.place_type_name))[position]).replace(" ", "_"), latLng);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)      // Sets the center of the map to location user
                        .zoom(12)           // Sets the zoom
                        .bearing(0)                // Sets the orientation of the camera to east
                        .build();                   // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1100, null);
                mCustomProgressDialog.dismiss("");
            //    spinnerItemSelected();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                linearLayout.setVisibility(View.GONE);
            }

        });*/

        checkBoxTraffic = (CheckBox) findViewById(R.id.trafficCheck);
        checkBoxSatellite = (CheckBox) findViewById(R.id.satelliteCheck);
        checkBoxSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxSatellite.isChecked())
                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                else
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        checkBoxTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxTraffic.isChecked())
                    map.setTrafficEnabled(true);
                else
                    map.setTrafficEnabled(false);
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        drivingMode = (RadioButton) findViewById(R.id.drivingModeRadio);
        walkingMode = (RadioButton) findViewById(R.id.walkingModeRadio);

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (drivingMode.isChecked()) {

                    try {
                        marker.remove();
                    } catch (Exception e) {
                    }
                   *//* marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Your current location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                            .rotation(270));
*//*
                    try {
                        for (Polyline line : polylines) {
                            line.remove();
                        }
                        polylines.clear();
                    } catch (Exception e) {
                    }
                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask2 downloadTask = new DownloadTask2();
                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                } else if (walkingMode.isChecked()) {

                    try {
                        marker.remove();
                    } catch (Exception e) {
                    }
                   *//* marker = map.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Your current location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.foot))
                            .rotation(45));
                   *//* try {
                        for (Polyline line : polylines) {
                            line.remove();
                        }
                        polylines.clear();
                    } catch (Exception e) {
                    }
                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl1(origin, dest);
                    DownloadTask1 downloadTask = new DownloadTask1();
                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
            }
        });*/

        drivingModeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drivingModeImage.setImageDrawable(getResources().getDrawable(R.drawable.select_car));
                walkingModeImage.setImageDrawable(getResources().getDrawable(R.drawable.unselect_walking));
                try {
                    for (Polyline line : polylines) {
                        line.remove();
                    }
                    polylines.clear();
                } catch (Exception e) {
                }
                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);
                DownloadTask2 downloadTask = new DownloadTask2();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        });

        walkingModeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drivingModeImage.setImageDrawable(getResources().getDrawable(R.drawable.unselect_car));
                walkingModeImage.setImageDrawable(getResources().getDrawable(R.drawable.select_walking));
                try {
                    for (Polyline line : polylines) {
                        line.remove();
                    }
                    polylines.clear();
                } catch (Exception e) {
                }
                // Getting URL to the Google Directions API
                String url = getDirectionsUrl1(origin, dest);
                DownloadTask1 downloadTask = new DownloadTask1();
                // Start downloading json data from Google Directions API
                downloadTask.execute(url);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      /*  setSupportActionBar(toolbar);
*/
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        linearLayoutSlidingBar = (LinearLayout) findViewById(R.id.linearLayoutSlidingBar);
        rangeButton = (Button) findViewById(R.id.range);
        rangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSlidingBar.setVisibility(View.VISIBLE);
            }
        });
        final DiscreteSeekBar discreteSeekBar1 = (DiscreteSeekBar) findViewById(R.id.discrete1);
        discreteSeekBar1.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                Log.e(TAG, "" + value * 1000);
                values = value;
                return value;
            }
        });

        rangeText = (TextView) findViewById(R.id.rangeNumber);
        rangeText.setText("1");

        ok = (Button) findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                rangeText.setText("" + values);
                map.clear();
                if (category.getText().toString().equals("")) {
                    linearLayoutSlidingBar.setVisibility(View.GONE);
                } else {
                    sbMethod("" + category.getText().toString().replace(" ", "_"), "" + values, latLng);
                    linearLayoutSlidingBar.setVisibility(View.GONE);
                }
            }
        });
        mCustomProgressDialog.dismiss("");

        map.clear();
        getCurrentLocation();
        category.setText("");
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://in.showoffs.locateatm/http/host/path")
        );
        AppIndex.AppIndexApi.start(mGoogleApiClient, viewAction);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://in.showoffs.locateatm/http/host/path")
        );
        AppIndex.AppIndexApi.end(mGoogleApiClient, viewAction);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        mCustomProgressDialog.show("");
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation){
            latLng = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
        }else {
            gpsTracker.showSettingsAlert();
        }
        /*StringBuilder sbValue = new StringBuilder(sbMethod(latLng));
        System.out.println(sbValue);*/
        map.clear();
        getCurrentLocation();
        try {
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    mCustomProgressDialog.show("");
                    slidingLayout.setEnabled(false);
                    mLayout.setPanelHeight(0);
                    try {
                        line.remove();
                    } catch (Exception e) {
                    }
                    //drivingMode();
                    linearLayout.setVisibility(View.GONE);
                    origin = latLng;
                    getAddress(point);
                    dest = new LatLng(point.latitude, point.longitude);
                    // Getting URL to the Google Directions API
                    /*if (drivingMode.isChecked()) {
                        try {
                            for (Polyline line : polylines) {
                                line.remove();
                            }
                            polylines.clear();
                        } catch (Exception e) {
                        }
                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);
                        DownloadTask2 downloadTask = new DownloadTask2();
                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    } else if (walkingMode.isChecked()) {
                        try {
                            for (Polyline line : polylines) {
                                line.remove();
                            }
                            polylines.clear();
                        } catch (Exception e) {
                        }
                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl1(origin, dest);
                        DownloadTask1 downloadTask = new DownloadTask1();
                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }*/
                    try {
                        dragMarker.remove();
                    } catch (Exception e) {
                    }
                    dragMarker = map.addMarker(new MarkerOptions()
                            .position(dest)
                            .title("Your destination location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)));

                    check = true;
                    drivingModeImage.setImageDrawable(getResources().getDrawable(R.drawable.select_car));
                    walkingModeImage.setImageDrawable(getResources().getDrawable(R.drawable.unselect_walking));
                    dragMarker.setDraggable(true);
                    dragMarker.showInfoWindow();
                    mCustomProgressDialog.dismiss("");
                }


            });

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker marker) {
                    mCustomProgressDialog.show("");
                    //drivingMode();
                    try {
                        line.remove();
                    } catch (Exception e) {
                    }
                    mLayout.setPanelHeight(0);
                    textView.setText(tempDist);
                    drivingMode.setChecked(true);
                    drivingModeImage.setImageDrawable(getResources().getDrawable(R.drawable.select_car));
                    walkingModeImage.setImageDrawable(getResources().getDrawable(R.drawable.unselect_walking));

                  /*  LayoutInflater layoutInflater = (LayoutInflater) MapsActivity.this
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupView = layoutInflater.inflate(R.layout.popup_layout, null);

                    Button btnClose = (Button) popupView.findViewById(R.id.close);
                    final PopupWindow p = new PopupWindow(popupView, 300, 370, true);

                   *//* try {dragMarker.remove();} catch (Exception e) {}
*//*
                    p.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                    p.setHeight(20);
                    p.setWidth(30);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            p.dismiss();
                        }
                    });*/

                    try {

                        if (!marker.getTitle().equalsIgnoreCase("Your destination location")) {

                            dragMarker.remove();
                        } else if (!marker.getTitle().equalsIgnoreCase("Your destination location")) {
                            lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                            lPlaceContact.setVisibility(View.GONE);
                            if (width < 600)
                                mLayout.setPanelHeight(75);
                            else if (width > 600)
                                mLayout.setPanelHeight(150);
                        } else if (!marker.getTitle().equalsIgnoreCase("Your current location")) {
                            lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                            lPlaceContact.setVisibility(View.GONE);

                            if (width < 600)
                                mLayout.setPanelHeight(75);
                            else if (width > 600)
                                mLayout.setPanelHeight(150);

                        }


                    } catch (Exception e) {
                    }
                    Log.e("" + marker.getPosition().latitude, "" + marker.getPosition().longitude);
                    Location locationA = new Location("point A");
                    locationA.setLatitude(latitude);
                    locationA.setLongitude(longitude);
                    Location locationB = new Location("point B");
                    locationB.setLatitude(marker.getPosition().latitude);
                    locationB.setLongitude(marker.getPosition().longitude);
                    float distance = locationA.distanceTo(locationB);
                    Log.v("log", "distance " + distance);

                    origin = latLng;
                    dest = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    // Getting URL to the Google Directions API
                    /*if (drivingMode.isChecked()) {
                        try {
                            for (Polyline line : polylines) {
                                line.remove();
                            }
                            polylines.clear();
                        } catch (Exception e) {
                        }
                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl(origin, dest);
                        DownloadTask2 downloadTask = new DownloadTask2();
                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                        try {
                            for (Polyline line : polylines) {
                                line.remove();
                            }
                            polylines.clear();
                        } catch (Exception e) {
                        }
                        try {line.remove();}catch (Exception e){}
                    }*/

                     /*else if (walkingMode.isChecked()) {
                        try {
                            for (Polyline line : polylines) {
                                line.remove();
                            }
                            polylines.clear();
                        } catch (Exception e) {
                        }
                        // Getting URL to the Google Directions API
                        String url = getDirectionsUrl1(origin, dest);
                        DownloadTask1 downloadTask = new DownloadTask1();
                        // Start downloading json data from Google Directions API
                        downloadTask.execute(url);
                    }*/

                    /**************info window***************/
                    map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                                 @Override
                                                 public View getInfoWindow(Marker marker) {
                                                     return null;
                                                 }

                                                 @Override
                                                 public View getInfoContents(Marker marker) {
                                                     final View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                                                     try {

                                                         if (marker.getTitle().equalsIgnoreCase("Your destination location")) {

                                                             LatLng markerLatLng = marker.getPosition();
                                                             TextView title = (TextView) v.findViewById(R.id.title);
                                                             title.setText(marker.getTitle());
                                                             Button about = (Button) findViewById(R.id.aboutButton);
                                                             Button review = (Button) findViewById(R.id.reviewButton);
                                                             Button photo = (Button) findViewById(R.id.photoButton);

                                                             about.setBackgroundColor(0xff1141AF);
                                                             review.setBackgroundColor(0xff3E69BB);
                                                             photo.setBackgroundColor(0xff3E69BB);

                                                         } else {
                                                             LatLng markerLatLng = marker.getPosition();
                                                             TextView title = (TextView) v.findViewById(R.id.title);
                                                             title.setText(marker.getTitle());
                                                             Button about = (Button) findViewById(R.id.aboutButton);
                                                             Button review = (Button) findViewById(R.id.reviewButton);
                                                             Button photo = (Button) findViewById(R.id.photoButton);

                                                             about.setBackgroundColor(0xff1141AF);
                                                             review.setBackgroundColor(0xff3E69BB);
                                                             photo.setBackgroundColor(0xff3E69BB);
                                                         }
                                                     } catch (Exception e) {
                                                     }


                                                     map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                                                          @Override
                                                                                          public void onInfoWindowClick(final Marker marker) {
                                                                                              mCustomProgressDialog.show("");
                                                                                              about();
                                                                                              final Dialog dialog = new Dialog(MapsActivity.this);
                                                                                              dialog.setContentView(R.layout.popup_layout);
                                                                                              if (drivingMode.isChecked()) {
                                                                                                  try {
                                                                                                      for (Polyline line : polylines) {
                                                                                                          line.remove();
                                                                                                      }
                                                                                                      polylines.clear();
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                                  // Getting URL to the Google Directions API
                                                                                                  String url = getDirectionsUrl(origin, dest);
                                                                                                  DownloadTask2 downloadTask = new DownloadTask2();
                                                                                                  // Start downloading json data from Google Directions API
                                                                                                  downloadTask.execute(url);
                                                                                                  try {
                                                                                                      for (Polyline line : polylines) {
                                                                                                          line.remove();
                                                                                                      }
                                                                                                      polylines.clear();
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                                  try {
                                                                                                      line.remove();
                                                                                                  } catch (Exception e) {
                                                                                                  }
                                                                                              }
                                                                                              tempTitle = marker.getTitle().toString();
                                                                                              slidingLayout.setEnabled(true);
                                                                                              //getAllDetailsOfPlaces(marker);
                                                                                              placeName.setText(marker.getTitle());
                                                                                              //placeAddress.setText(marker.getSnippet());
                                                                                              mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
                                                                                                  @Override
                                                                                                  public void onPanelSlide(View panel, float slideOffset) {
                                                                                                      Log.i(TAG, "onPanelSlide, offset " + slideOffset);
                                                                                                      if (slideOffset > 0.5) {
                                                                                                          appBarLayout.setVisibility(View.GONE);
                                                                                                          titleT.setBackgroundColor(0xff1141AF);
                                                                                                          textView.setTextColor(Color.WHITE);
                                                                                                      }
                                                                                                      if (slideOffset > 0.1) {
                                                                                                          if (marker.getTitle().equalsIgnoreCase("Your destination location")) {
                                                                                                              textView.setText("Your destination location");
                                                                                                              lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                                                                                                              lPlaceContact.setVisibility(View.GONE);

                                                                                                              ImageView imageView = (ImageView) findViewById(R.id.backgroundImage);
                                                                                                              imageView.setVisibility(View.GONE);
                                                                                                              if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                                                                                                                  imageView.setVisibility(View.GONE);


                                                                                                          } else if (marker.getTitle().equalsIgnoreCase("Your current location")) {
                                                                                                              textView.setText("Your current location");
                                                                                                              lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                                                                                                              lPlaceContact.setVisibility(View.GONE);
                                                                                                              ImageView imageView = (ImageView) findViewById(R.id.backgroundImage);
                                                                                                              imageView.setVisibility(View.GONE);
                                                                                                              if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                                                                                                                  imageView.setVisibility(View.GONE);

                                                                                                          }
                                                                                                      }
                                                                                                  }

                                                                                                  @Override
                                                                                                  public void onPanelExpanded(View panel) {
                                                                                                      Log.i(TAG, "onPanelExpanded");
                                                                                                      drivingModeImage.setVisibility(View.GONE);
                                                                                                      walkingModeImage.setVisibility(View.GONE);
                                                                                                      panel.setOnClickListener(new View.OnClickListener() {
                                                                                                          @Override
                                                                                                          public void onClick(View v) {
                                                                                                          }
                                                                                                      });
                                                                                                      appBarLayout.setVisibility(View.GONE);
                                                                                                      titleT.setBackgroundColor(0xff1141AF);
                                                                                                      textView.setText(tempTitle);
                                                                                                      if (marker.getTitle().equalsIgnoreCase("Your destination location")) {
                                                                                                          textView.setText("Your destination location");
                                                                                                          lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                                                                                                          lPlaceContact.setVisibility(View.GONE);

                                                                                                      } else if (marker.getTitle().equalsIgnoreCase("Your current location")) {
                                                                                                          lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                                                                                                          lPlaceContact.setVisibility(View.GONE);
                                                                                                          textView.setText("Your current location");
                                                                                                      }

                                                                                                  }

                                                                                                  @Override
                                                                                                  public void onPanelCollapsed(View panel) {
                                                                                                      Log.i(TAG, "onPanelCollapsed");
                                                                                                      //panel.setClickable(true);
                                                                                                      drivingModeImage.setVisibility(View.VISIBLE);
                                                                                                      walkingModeImage.setVisibility(View.VISIBLE);
                                                                                                      appBarLayout.setVisibility(View.VISIBLE);
                                                                                                      titleT.setBackgroundColor(0xfff0f0f0);
                                                                                                      textView.setText(tempDist);
                                                                                                      textView.setTextColor(Color.BLACK);
                                                                                                  }

                                                                                                  @Override
                                                                                                  public void onPanelAnchored(View panel) {
                                                                                                      Log.i(TAG, "onPanelAnchored");
                                                                                                  }

                                                                                                  @Override
                                                                                                  public void onPanelHidden(View panel) {
                                                                                                      Log.i(TAG, "onPanelHidden");
                                                                                                  }

                                                                                              });


                                                                                              Button photoButton = (Button) findViewById(R.id.photoButton);
                                                                                              photoButton.setVisibility(View.VISIBLE);
                                                                                              Button reviewButton = (Button) findViewById(R.id.reviewButton);
                                                                                              reviewButton.setVisibility(View.VISIBLE);
                                                                                              Button aboutButton = (Button) findViewById(R.id.aboutButton);
                                                                                              aboutButton.setPressed(true);

                                                                                              if (marker.getTitle().equalsIgnoreCase("Your destination location")) {
                                                                                                 // Toast.makeText(MapsActivity.this, "dest", Toast.LENGTH_LONG).show();
                                                                                                  about();
                                                                                                  photoButton.setVisibility(View.GONE);
                                                                                                  reviewButton.setVisibility(View.GONE);

                                                                                                  lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                                                                                                  lPlaceContact.setVisibility(View.GONE);

                                                                                                  getAddress(dest);


                                                                                                  if (width < 600)
                                                                                                      mLayout.setPanelHeight(75);
                                                                                                  else if (width > 600)
                                                                                                      mLayout.setPanelHeight(150);
                                                                                                  mCustomProgressDialog.dismiss("");

                                                                                              } else if (marker.getTitle().equalsIgnoreCase("Your current location")) {
                                                                                                  //Toast.makeText(MapsActivity.this, "current", Toast.LENGTH_LONG).show();
                                                                                                  about();
                                                                                                  lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                                                                                                  lPlaceContact.setVisibility(View.GONE);
                                                                                                  photoButton.setVisibility(View.GONE);
                                                                                                  reviewButton.setVisibility(View.GONE);


                                                                                                  getAddress(dest);


                                                                                                  if (width < 600)
                                                                                                      mLayout.setPanelHeight(75);
                                                                                                  else if (width > 600)
                                                                                                      mLayout.setPanelHeight(150);
                                                                                                  mCustomProgressDialog.dismiss("");

                                                                                              } else if (marker.getSnippet().equalsIgnoreCase("friend")) {
                                                                                                  //Toast.makeText(MapsActivity.this, "current", Toast.LENGTH_LONG).show();
                                                                                                  about();
                                                                                                  ImageView imageView = (ImageView) findViewById(R.id.backgroundImage);
                                                                                                  imageView.setVisibility(View.GONE);

                                                                                                  lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                                                                                                  lPlaceContact.setVisibility(View.GONE);

                                                                                                  photoButton.setVisibility(View.GONE);
                                                                                                  reviewButton.setVisibility(View.GONE);

                                                                                                  getAddress(marker.getPosition());


                                                                                                  if (width < 600)
                                                                                                      mLayout.setPanelHeight(75);
                                                                                                  else if (width > 600)
                                                                                                      mLayout.setPanelHeight(150);
                                                                                                  mCustomProgressDialog.dismiss("");

                                                                                              } else {
                                                                                                  /********************Place About************************/
                                                                                                  //Toast.makeText(MapsActivity.this, "huuuuuu", Toast.LENGTH_LONG).show();

                                                                                                  ProgressBar progressBar = new MaterialProgressBar(MapsActivity.this);
                                                                                                  Ion.with(MapsActivity.this)
                                                                                                          .load("https://maps.googleapis.com/maps/api/place/details/json?reference=" + marker.getSnippet() + "&key=AIzaSyCPzNGhN0NapgTn_i1jJHEeuiAfNzTl_D0")
                                                                                                          .progressBar(progressBar)
                                                                                                          .progressHandler(new ProgressCallback() {
                                                                                                              @Override
                                                                                                              public void onProgress(long downloaded, long total) {
                                                                                                                  //mCustomProgressDialog.show("");
                                                                                                                  Log.e(downloaded + "----", "--" + total);
                                                                                                              }
                                                                                                          })
                                                                                                          .asJsonObject()
                                                                                                          .setCallback(new FutureCallback<JsonObject>() {
                                                                                                              @Override
                                                                                                              public void onCompleted(Exception e, JsonObject result) {
                                                                                                                  // mCustomProgressDialog.show("");
                                                                                                                  //try {Thread.sleep(2000);} catch (InterruptedException e1) {e1.printStackTrace();}

                                                                                                                  // do stuff with the result or error
                                                                                                                  Log.e("", "" + result);
                                                                                                                  String jsonString = result.toString();
                                                                                                                  try {
                                                                                                                      JSONObject jsonObject = new JSONObject(jsonString);
                                                                                                                      //Log.e("ddddddd", "" + jsonObject.getJSONObject("result").getString("formatted_address"));

                                                                                                                      /****About Place****/
                                                                                                                      if (jsonObject.getJSONObject("result").has("formatted_address"))
                                                                                                                          placeAddress.setText(jsonObject.getJSONObject("result").getString("formatted_address"));
                                                                                                                      else if (jsonObject.getJSONObject("result").has("vicinity"))
                                                                                                                          placeAddress.setText(jsonObject.getJSONObject("result").getString("vicinity"));
                                                                                                                      else
                                                                                                                          placeAddress.setText("");

                                                                                                                      if (jsonObject.getJSONObject("result").has("rating"))
                                                                                                                          placeRating.setRating(Float.parseFloat(jsonObject.getJSONObject("result").getString("rating")));
                                                                                                                      else
                                                                                                                          placeRating.setRating((float) 3.0);
                                                                                                                      lPlaceContact = (LinearLayout)findViewById(R.id.lPlaceContact);
                                                                                                                          if ((jsonObject.getJSONObject("result").has("international_phone_number"))) {
                                                                                                                              placeContact.setText(jsonObject.getJSONObject("result").getString("international_phone_number"));
                                                                                                                              lPlaceContact.setVisibility(View.VISIBLE);
                                                                                                                              //btn_phone_call.setVisibility(View.VISIBLE);
                                                                                                                          } else {

                                                                                                                              lPlaceContact.setVisibility(View.GONE);
                                                                                                                              //btn_phone_call.setVisibility(View.GONE);
                                                                                                                              placeContact.setText("");
                                                                                                                          }
                                                                                                                      /********************Place Photos************************/

                                                                                                                      ArrayList<String> photo_refrence = null;

                                                                                                                      JSONArray photos = new JSONArray();


                                                                                                                      if (jsonObject.getJSONObject("result").has("photos")) {
                                                                                                                          photos = jsonObject.getJSONObject("result").getJSONArray("photos");
                                                                                                                          ArrayList<String> refrenceArray = new ArrayList<String>();
                                                                                                                          for (int m = 0; m < photos.length(); m++) {
                                                                                                                              JSONObject imgJson = photos.getJSONObject(m);
                                                                                                                              String refe = imgJson.getString("photo_reference");
                                                                                                                              refrenceArray.add(refe);
                                                                                                                          }
                                                                                                                          photo_refrence = refrenceArray;
                                                                                                                      }

                                                                                                                      if (photo_refrence != null) {
                                                                                                                          layout.removeAllViews();
                                                                                                                          for (int i = 0; i < photo_refrence.size(); i++) {

                                                                                                                              ImageView imageView1 = new ImageView(MapsActivity.this);
                                                                                                                              imageView1.setPadding(5, 5, 5, 5);

                                                                                                                              Ion.with(MapsActivity.this)
                                                                                                                                      .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo_refrence.get(i).toString() + "&key=AIzaSyCPzNGhN0NapgTn_i1jJHEeuiAfNzTl_D0")
                                                                                                                                      .withBitmap()
                                                                                                                                      .placeholder(R.drawable.placeholder)
                                                                                                                                      .error(R.drawable.error_image)
                                                                                                                                      .intoImageView(imageView1);
                                                                                                                              if (width < 600) {
                                                                                                                                  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(550, 500);
                                                                                                                                  imageView1.setLayoutParams(layoutParams);
                                                                                                                                  imageView1.setScaleType(ImageView.ScaleType.FIT_XY);
                                                                                                                              } else if (width > 600) {
                                                                                                                                  LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1050, 1000);
                                                                                                                                  imageView1.setLayoutParams(layoutParams);
                                                                                                                                  imageView1.setScaleType(ImageView.ScaleType.FIT_XY);

                                                                                                                              }
                                                                                                                              layout.addView(imageView1);
                                                                                                                              ImageView imageView = (ImageView) findViewById(R.id.backgroundImage);
                                                                                                                              imageView.setVisibility(View.VISIBLE);
                                                                                                                              ImageView imageView11 = (ImageView) findViewById(R.id.backgroundImage);
                                                                                                                              if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                                                                                                                                  imageView11.setVisibility(View.GONE);

                                                                                                                              Ion.with(MapsActivity.this)
                                                                                                                                      .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photo_refrence.get(0).toString() + "&key=AIzaSyCPzNGhN0NapgTn_i1jJHEeuiAfNzTl_D0")
                                                                                                                                      .withBitmap()
                                                                                                                                      .placeholder(R.drawable.placeholder)
                                                                                                                                      .error(R.drawable.error_image)
                                                                                                                                      .intoImageView(imageView);

                                                                                                                              //imageView.setImageBitmap();

                                                                                                                          }
                                                                                                                          about();
                                                                                                                      } else {
                                                                                                                          Button photoButton = (Button) findViewById(R.id.photoButton);
                                                                                                                          photoButton.setVisibility(View.GONE);
                                                                                                                          ImageView imageView = (ImageView) findViewById(R.id.backgroundImage);
                                                                                                                          imageView.setVisibility(View.GONE);
                                                                                                                          about();
                                                                                                                      }

                                                                                                                      /********************Place Review************************/
                                                                                                                      List<Review> reviewList = new ArrayList<Review>();
                                                                                                                      Review rev = new Review();
                                                                                                                      List<Review> list = null;
                                                                                                                      JSONArray reviews = new JSONArray();
                                                                                                                      if (jsonObject.getJSONObject("result").has("reviews")) {
                                                                                                                          reviews = jsonObject.getJSONObject("result").getJSONArray("reviews");
                                                                                                                          ArrayList<String> userNameArray = new ArrayList<String>();
                                                                                                                          ArrayList<String> userProfileImageUrlArray = new ArrayList<String>();
                                                                                                                          ArrayList<Float> userRatingArray = new ArrayList<Float>();
                                                                                                                          ArrayList<String> userCommentArray = new ArrayList<String>();
                                                                                                                          for (int m = 0; m < reviews.length(); m++) {
                                                                                                                              JSONObject imgJson = reviews.getJSONObject(m);
                                                                                                                              String name = imgJson.getString("author_name");
                                                                                                                              String url = "";
                                                                                                                              Float rating = Float.valueOf(imgJson.getString("rating"));
                                                                                                                              String text = imgJson.getString("text");
                                                                                                                              if (imgJson.has("profile_photo_url")) {
                                                                                                                                  url = imgJson.getString("profile_photo_url");
                                                                                                                                  try {
                                                                                                                                      myBitmap = Ion.with(MapsActivity.this)
                                                                                                                                              .load("https:" + url).asBitmap().get();
                                                                                                                                  } catch (Exception er) {
                                                                                                                                  }
                                                                                                                              } else {
                                                                                                                                  Drawable myDrawable = getResources().getDrawable(R.drawable.user_image);
                                                                                                                                  myBitmap = ((BitmapDrawable) myDrawable).getBitmap();
                                                                                                                              }
                                                                                                                              reviewList.add(new Review(myBitmap, name, rating, text));
                                                                                                                              rev.setReview(reviewList);
                                                                                                                          }
                                                                                                                          LinearLayoutManager layoutManager = new LinearLayoutManager(MapsActivity.this);
                                                                                                                          recyclerView.setLayoutManager(layoutManager);
                                                                                                                          list = (ArrayList<Review>) rev.getReview();
                                                                                                                          RecycleviewAdapter adapter = new RecycleviewAdapter(list);
                                                                                                                          recyclerView.setAdapter(adapter);
                                                                                                                      } else {
                                                                                                                          Button reviewButton = (Button) findViewById(R.id.reviewButton);
                                                                                                                          reviewButton.setVisibility(View.GONE);
                                                                                                                          ImageView imageView = (ImageView) findViewById(R.id.backgroundImage);

                                                                                                                          //imageView.setImageDrawable(R.drawable.);
                                                                                                                          about();

                                                                                                                      }
                                                                                                                  } catch (JSONException e1) {
                                                                                                                      e1.printStackTrace();
                                                                                                                  }
                                                                                                                  //try {Thread.sleep(2000);} catch (InterruptedException e1) {e1.printStackTrace();}
                                                                                                                  if (width < 600)
                                                                                                                      mLayout.setPanelHeight(75);
                                                                                                                  else if (width > 600)
                                                                                                                      mLayout.setPanelHeight(150);
                                                                                                                  mCustomProgressDialog.dismiss("");
                                                                                                              }

                                                                                                          });

                                                                                              }
                                                                                          }
                                                                                      }

                                                     );

                                                     mCustomProgressDialog.dismiss("");
                                                     return v;
                                                 }
                                             }

                    );
                    mCustomProgressDialog.dismiss("");
                    check = true;
                    return false;

                }
            });
        }catch (Exception e){}
        ;
        if (flag){
            //map.clear();
            //getCurrentLocation();
            flag = false;
        }
        mCustomProgressDialog.dismiss("");
    }

    private void getAddress(LatLng latLng) {
        JsonObject json = new JsonObject();
        Ion.with(this)
                .load("http://maps.googleapis.com/maps/api/geocode/json?latlng="+latLng.latitude+","+latLng.longitude)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.e("result", "" + result);

                        String jsonString = result.toString();
                        JSONArray address = new JSONArray();

                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                                  if (jsonObject.has("results")) {

                                      address = jsonObject.getJSONArray("results");
                                      JSONObject addressJson = address.getJSONObject(0);
                                      String s = addressJson.getString("formatted_address");
                                      placeAddress.setText(s);
                                      placeRating.setVisibility(View.GONE);
                                      lPlaceContact.setVisibility(View.GONE);
                                      //placeContact.setVisibility(View.GONE);
                                      /*placeAddress.setText(jsonObject.getJSONArray("result").getString("formatted_address"));
                                      placeRating.setVisibility(View.GONE);*/
                                  }
                            else {

                            }
                        }catch (Exception er){}

                    }
                });
    }

    private StringBuilder sbMethod(LatLng latLng){
        return sbMethod(null,null,latLng);
    }
    private StringBuilder sbMethod(String type, LatLng latLng){
        return sbMethod(type,null,latLng);
    }
    public StringBuilder sbMethod(String type, String radius, LatLng latLng) {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        if(radius == null) radius = "1000";
        if(type == null){
            type = "";
        }else {
            radius = String.valueOf(values*1000);
            sb.append("location=" + latLng.latitude + "," + latLng.longitude);
            sb.append("&radius=" + radius);
            sb.append("&types=" + type.toLowerCase());
            sb.append("&sensor=true");
            sb.append("&key=AIzaSyCPzNGhN0NapgTn_i1jJHEeuiAfNzTl_D0");


            PlacesTask placesTask = new PlacesTask();
            placesTask.execute(sb.toString());
            return sb;
        }
        return sb;
    }

    private void getCurrentLocation() {
        try {
            marker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Your current location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            /*CircleOptions circleOptions = new CircleOptions()
                    .center(latLng)   //set center
                    .radius(200)   //set radius in meters
                    .fillColor(0x809FD6F4)  //default
                    .strokeColor(0x990099FF)
                    .strokeWidth(2);
            myCircle = map.addCircle(circleOptions);*/
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setMyLocationEnabled(true);
            map.getMyLocation();
            map.getUiSettings().setZoomControlsEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setRotateGesturesEnabled(true);
            map.getUiSettings().setZoomGesturesEnabled(true);
            map.getUiSettings().setAllGesturesEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(true);
            map.setBuildingsEnabled(true);
            map.getUiSettings().setIndoorLevelPickerEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(true);
            map.getUiSettings().setTiltGesturesEnabled(true);
        }catch (Exception e){}
    }

    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        /*Log.e("new location ", latitude + "<===>" + longitude);*/
        latLng = new LatLng(latitude,longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";

        String parameters = null;
        // Building the parameters to the web service


        parameters = str_origin+"&"+str_dest+"&"+sensor;

        //parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl2(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onDialogFragmentInteraction(String value, int position) {
        linearLayout.setVisibility(View.GONE);
        map.clear();
        mCustomProgressDialog.show("");
        sbMethod(((getResources().getStringArray(R.array.place_type_name))[position]).replace(" ", "_"), latLng);
        preferences=getSharedPreferences("map",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("position",position);
        editor.putString("value", value);
        editor.commit();
        mLayout.setPanelHeight(0);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)      // Sets the center of the map to location user
                .zoom(14)           // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1100, null);

        category.setText(value);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_myPlaces) {

        } else if (id == R.id.nav_nearPlaces) {

        } else if (id == R.id.nav_nearFriend) {
            showMyFriends();
        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_account) {
                startActivity(new Intent(MapsActivity.this, AccountActivity.class));
        } else if (id == R.id.nav_invite_friend) {

        } else if (id == R.id.nav_setting) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Fetches data from url passed
    private class DownloadTask2 extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            try{
                // Fetching the data from web service
                data = downloadUrl2(url[0]);
            } catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask2 parserTask = new ParserTask2();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask2 extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.po);
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            if (result.size() < 1) {
                //Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                Snackbar.make(relativeLayout, "No Points", Snackbar.LENGTH_SHORT).show();
                return;
            }
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                HashMap<String, String> point = null;
                LatLng position = null;
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                        point= path.get(j);
                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    position= new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(0xFFff9e00);
            }
            linearLayout.setVisibility(View.GONE);
            //textView.setText("Distance    :   " + distance + "\nDuration     :  " + duration);
            textView.setText("("+distance+") "+duration);
            tempDist = textView.getText().toString();
            try {line.remove();}catch (Exception e){}
            line = map.addPolyline(lineOptions);
        }
    }

    private String getDirectionsUrl1(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=walking";

        String parameters = null;
        // Building the parameters to the web service


        // parameters = str_origin+"&"+str_dest+"&"+sensor;

        parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl1(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }catch(Exception e){
            Log.d("Err : downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask1 extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            try{
                // Fetching the data from web service
                data = downloadUrl1(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask1 parserTask = new ParserTask1();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask1 extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";
            if (result.size() < 1) {
                //Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.po);
                Snackbar.make(relativeLayout, "No Points", Snackbar.LENGTH_SHORT).show();
                return;
            }
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                HashMap<String, String> point = null;
                LatLng position = null;
                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    point= path.get(j);
                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    position= new LatLng(lat, lng);
                    points.add(position);
                }
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }
            linearLayout.setVisibility(View.GONE);
            //textView.setText("Distance:   " + distance + "\nDuration   :  " + duration);
            textView.setText("("+distance+") "+duration);
            tempDist = textView.getText().toString();
            try {line.remove();}catch (Exception e){}
            line = map.addPolyline(lineOptions);
            //drawDashedPolyLine(map,points,Color.BLUE);
        }
    }

    private void drawDashedPolyLine(GoogleMap mMap, ArrayList<LatLng> listOfPoints, int color) {
    /* Boolean to control drawing alternate lines */
        boolean added = false;

        try{
            for(Polyline line : polylines) {
                line.remove();
            }
            polylines.clear();
        }catch (Exception e){}
        for (int i = 0; i < listOfPoints.size() - 1 ; i++) {
        /* Get distance between current and next point */
            double distance = getConvertedDistance(listOfPoints.get(i),listOfPoints.get(i + 1));

        /* If distance is less than 0.002 kms */
            if (distance < 0.002) {
                if (!added) {
                    polylines.add(mMap.addPolyline(new PolylineOptions()
                            .add(listOfPoints.get(i))
                            .add(listOfPoints.get(i + 1))
                            .color(0)));
                    added = true;
                } else {/* Skip this piece */
                    added = false;
                }
            } else {
            /* Get how many divisions to make of this line */
                int countOfDivisions = (int) ((distance/0.002));

            /* Get difference to add per lat/lng */
                double latdiff = (listOfPoints.get(i+1).latitude - listOfPoints
                        .get(i).latitude) / countOfDivisions;
                double lngdiff = (listOfPoints.get(i + 1).longitude - listOfPoints
                        .get(i).longitude) / countOfDivisions;

            /* Last known indicates start point of polyline. Initialized to ith point */
                LatLng lastKnowLatLng = new LatLng(listOfPoints.get(i).latitude, listOfPoints.get(i).longitude);
                for (int j = 0; j < countOfDivisions; j++) {

                /* Next point is point + diff */
                    LatLng nextLatLng = new LatLng(lastKnowLatLng.latitude + latdiff, lastKnowLatLng.longitude + lngdiff);
                    if (!added) {
                        polylines.add(mMap.addPolyline(new PolylineOptions()
                                .add(lastKnowLatLng)
                                .add(nextLatLng)
                                .color(color)));
                        added = true;
                    } else {
                        added = false;
                    }
                    lastKnowLatLng = nextLatLng;
                }
            }
        }

    }

    private double getConvertedDistance(LatLng latlng1, LatLng latlng2) {
        double distance = DistanceUtil.distance(latlng1.latitude,
                latlng1.longitude,
                latlng2.latitude,
                latlng2.longitude);
        BigDecimal bd = new BigDecimal(distance);
        BigDecimal res = bd.setScale(10, RoundingMode.DOWN);
        return res.doubleValue();
    }

    public void drivingMode(){
        try {marker.remove();} catch (Exception e) {}
        marker=map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Your current location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car))
                .rotation(270));
    }

    //*********//
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Err : downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            try {

                Log.d("Map", "list size: " + list.size());
                // Clears all the existing markers;
                //    mMap.clear();

                for (int i = 0; i < list.size(); i++) {

                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    // Getting a place from the places list
                    HashMap<String, String> hmPlace = list.get(i);


                    // Getting latitude of the place
                    double lat = Double.parseDouble(hmPlace.get("lat"));

                    // Getting longitude of the place
                    double lng = Double.parseDouble(hmPlace.get("lng"));

                    // Getting name
                    String name = hmPlace.get("place_name");

                    Log.d("Map", "place: " + name);

                    String icon = hmPlace.get("place_icon");

                    try {
                        URL url = new URL(icon);
                        try {
                            myBitmap = Ion.with(MapsActivity.this)
                                    .load(icon).asBitmap().get();
                            //myBitmap = BitmapFactory.decodeStream((InputStream) new URL(icon).getContent());
                        } catch (Exception e) {
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Getting vicinity
                    address = hmPlace.get("reference");

                    LatLng latLng = new LatLng(lat, lng);

                    // Setting the position for the marker
                    markerOptions.position(latLng);

                    markerOptions.title(name + "");

                    markerOptions.snippet("" + address.toString());

                    if (width<600)
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(myBitmap, 30, 30, false)));
                    else if (width>=600)
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(myBitmap, 70, 70, false)));

                    // Placing a marker on the touched position
                    m = map.addMarker(markerOptions);

                }
                mCustomProgressDialog.dismiss("");
                getTag = m.getId();
            }catch (Exception e){}
            mCustomProgressDialog.dismiss("");
        }

    }

    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {

            if(addresses==null || addresses.size()==0){
                //Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
                RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.po);
                Snackbar.make(relativeLayout, "No Location found", Snackbar.LENGTH_SHORT).show();
            }

            // Clears all the existing markers on the map
            // mMap.clear();

            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){

                Address address = (Address) addresses.get(i);

                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

              MarkerOptions  markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);

                map.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                   map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    public void clear(View v){
        linearLayout.setVisibility(View.GONE);
        map.clear();
        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation){
            latLng = new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude());
        }else {
            gpsTracker.showSettingsAlert();
        }
        mLayout.setPanelHeight(0);
        getCurrentLocation();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //mLayout.setPanelHeight(0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //mLayout.setPanelHeight(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mLayout.setPanelHeight(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mLayout.setPanelHeight(0);
        preferences=getSharedPreferences("map",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("position",-1);
        editor.putString("value", "");
        editor.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mLayout.setPanelHeight(150);
        //mLayout.setFadingEdgeLength(0);
        ImageView imageView = (ImageView) findViewById(R.id.backgroundImage);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            imageView.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility(View.GONE);

        preferences=getSharedPreferences("map", this.MODE_PRIVATE);
        int position = 0;
        position = preferences.getInt("position",-1);
        try {
            sbMethod(((getResources().getStringArray(R.array.place_type_name))[position]).replace(" ", "_"), latLng);
        }catch (Exception e){}
    }

    public void about() {
        lAbout.setVisibility(View.VISIBLE);
        lReview.setVisibility(View.GONE);
        lPhoto.setVisibility(View.GONE);
        Button about = (Button)findViewById(R.id.aboutButton);
        Button review = (Button)findViewById(R.id.reviewButton);
        Button photo = (Button)findViewById(R.id.photoButton);

        about.setBackgroundColor(0xff1141AF);
        review.setBackgroundColor(0xff3E69BB);
        photo.setBackgroundColor(0xff3E69BB);
    }

    public void phoneCall(View v){


        String phone_no= placeContact.getText().toString().replaceAll(" ", "");
        /*Intent in=new Intent(Intent.ACTION_CALL,Uri.parse(""+phone_no));
        try{
            startActivity(in);
        }

        catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
        }*/
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phone_no));
            startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("helloandroid dialing example", "Call failed", e);
        }
    }

    /********sliding view button********/

    public void about(View v){
        lAbout.setVisibility(View.VISIBLE);
        lReview.setVisibility(View.GONE);
        lPhoto.setVisibility(View.GONE);
        Button about = (Button)findViewById(R.id.aboutButton);
        Button review = (Button)findViewById(R.id.reviewButton);
        Button photo = (Button)findViewById(R.id.photoButton);

        about.setBackgroundColor(0xff1141AF);
        review.setBackgroundColor(0xff3E69BB);
        photo.setBackgroundColor(0xff3E69BB);
    }
    public void review(View v){
        lAbout.setVisibility(View.GONE);
        lReview.setVisibility(View.VISIBLE);
        lPhoto.setVisibility(View.GONE);
        Button about = (Button)findViewById(R.id.aboutButton);
        Button review = (Button)findViewById(R.id.reviewButton);
        Button photo = (Button)findViewById(R.id.photoButton);

        about.setBackgroundColor(0xff3E69BB);
        review.setBackgroundColor(0xff1141AF);
        photo.setBackgroundColor(0xff3E69BB);

    }
    public void photo(View v){
        lAbout.setVisibility(View.GONE);
        lReview.setVisibility(View.GONE);
        lPhoto.setVisibility(View.VISIBLE);
        Button about = (Button)findViewById(R.id.aboutButton);
        Button review = (Button)findViewById(R.id.reviewButton);
        Button photo = (Button)findViewById(R.id.photoButton);

        about.setBackgroundColor(0xff3E69BB);
        review.setBackgroundColor(0xff3E69BB);
        photo.setBackgroundColor(0xff1141AF);

    }

    /***********************************/

    public void showMyFriends(){
        map.clear();
        getCurrentLocation();


        Ion.with(this)
                .load("https://api.myjson.com/bins/3a1pb")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Log.e("hiiiiiii", result.toString());

                        String jsonString = result.toString();
                        JSONArray friendsArray = new JSONArray();


                        try {
                            JSONObject jsonObject = new JSONObject(jsonString);
                            if (jsonObject.has("contact")) {

                                friendsArray = jsonObject.getJSONArray("contact");

                                for (int i = 0; i <= friendsArray.length(); i++) {
                                    JSONObject friendJson = friendsArray.getJSONObject(i);
                                    double lat = Double.valueOf(friendJson.getJSONObject("latlong").getString("latitude"));
                                    double lng = Double.parseDouble(friendJson.getJSONObject("latlong").getString("longitude"));
                                    String firstName = friendJson.getJSONObject("details").getString("first name");
                                    String lastName = friendJson.getJSONObject("details").getString("last name");
                                    String title = firstName + " " + lastName;
                                    LatLng latLng = new LatLng(lat, lng);
                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(latLng);
                                    markerOptions.title(title);
                                    markerOptions.snippet("friend");
                                    map.addMarker(markerOptions);
                                    textView.setText(title);

                                    lPlaceContact = (LinearLayout) findViewById(R.id.lPlaceContact);
                                    lPlaceContact.setVisibility(View.GONE);
                                }
                            } else {
                                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.po);
                                Snackbar.make(relativeLayout, "No Friend found", Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (Exception er) {
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mLayout.setPanelHeight(0);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "back press",
                    Toast.LENGTH_LONG).show();
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
        return false;
        // Disable back button..............
    }
}