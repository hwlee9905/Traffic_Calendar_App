package com.example.btnavbar;



import static android.app.Activity.RESULT_OK;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class CustomCalendarView extends LinearLayout {

    int nPrevSelGridItem = -1;
    ImageButton NextButton, PreviousButton;
    TextView CurrentDate ,TEST;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.KOREAN);
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.KOREAN);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.KOREAN);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.KOREAN);
    SimpleDateFormat eventDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
    MyGridAdapter myGridAdapter;
    AlertDialog alertDialog;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();
    static TextView start;
    static TextView destination;
    FloatingActionButton floatingActionButton;
    DBOpenHelper dbOpenHelper;
    public CustomCalendarView(Context context) {
        super(context);
    }

    public CustomCalendarView(Context context, @Nullable AttributeSet attrs)  {

        super(context, attrs);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();

        PreviousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();;
            }
        });
        NextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            View viewPrev;
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View addView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_newevent_layout, null);



                try {
                    if (nPrevSelGridItem != -1) {
                        viewPrev = (View) gridView.getChildAt(nPrevSelGridItem);
                        viewPrev.setBackgroundColor(Color.WHITE);
                    }
                    nPrevSelGridItem = position;
                    if (nPrevSelGridItem == position) {
                        //View viewPrev = (View) gridview.getChildAt(nPrevSelGridItem);
                        view.setBackgroundColor(Color.parseColor("#ffbcc3"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                TextView EventTime = addView.findViewById(R.id.eventime);
                ImageButton SetTime = addView.findViewById(R.id.seteventime);
                Button AddEvent = addView.findViewById(R.id.addevent);
                EditText EventName = addView.findViewById(R.id.eventname);
                start = addView.findViewById(R.id.start);
                destination = addView.findViewById(R.id.destination);
                start.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (menuItem.getItemId() == R.id.action_menu1){
                                    Intent intent = new Intent(context, SearchActivity.class);
                                    ((MainActivity)MainActivity.mContext).getSearchResults.launch(intent);
                                }else if (menuItem.getItemId() == R.id.action_menu2){
                                    Intent intent = new Intent(context, LocationActivity.class);
                                    context.startActivity(intent);

                                }

                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
                destination.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.getMenuInflater().inflate(R.menu.pop_menu,popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (menuItem.getItemId() == R.id.action_menu1){
                                    Intent intent = new Intent(context, SearchActivity.class);
                                    ((MainActivity)MainActivity.mContext).getSearchResultd.launch(intent);
                                }else if (menuItem.getItemId() == R.id.action_menu2){
                                    Intent intent = new Intent(context, LocationActivity2.class);
                                    context.startActivity(intent);
                                }

                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });

                SetTime.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int minutes = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(addView.getContext(), android.R.style.Theme_Holo_Light_Dialog
                                , new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                c.setTimeZone(TimeZone.getDefault());
                                SimpleDateFormat hformat = new SimpleDateFormat("K:mm a", Locale.KOREAN);
                                String event_Time = hformat.format(c.getTime());
                                EventTime.setText(event_Time);
                            }
                        }, hours, minutes, false);
                        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        timePickerDialog.show();
                    }
                });
                String date= eventDateFormat.format(dates.get(position));
                String month = monthFormat.format(dates.get(position));
                String year = yearFormat.format(dates.get(position));

                AddEvent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveEvent(EventName.getText().toString(), EventTime.getText().toString(), date, month, year, start.getText().toString(), destination.getText().toString());
                        SetUpCalendar();
                        alertDialog.dismiss();

                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();

            }


        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String date = eventDateFormat.format(dates.get(position));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View showView = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_layout, null);
                RecyclerView recyclerView = showView.findViewById(R.id.EventsRV);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(showView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                EventRecyclerAdapter eventRecyclerAdapter = new EventRecyclerAdapter(showView.getContext()
                        , CollectEventByDate(date));
                recyclerView.setAdapter(eventRecyclerAdapter);
                eventRecyclerAdapter.notifyDataSetChanged();
                builder.setView(showView);
                alertDialog = builder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        SetUpCalendar();
                    }
                });
                return true;
            }
        });
    }


    private ArrayList<Events> CollectEventByDate(String date){
        ArrayList<Events> arrayList = new ArrayList<>();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEvents(date, database);
        while (cursor.moveToNext()){
            String event = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.TIME));
            String Date = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.MONTH));
            String YEAR = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.YEAR));
            String start = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.START));
            String destination = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.DESTINATION));
            Events events = new Events(event, time, date, month, YEAR, start, destination);
            arrayList.add(events);
        }
        cursor.close();
        dbOpenHelper.close();
        return arrayList;
    }
    public CustomCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void SaveEvent(String event, String time, String date, String month, String year, String start, String destination){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.SaveEvent(event, time, date, month, year, start, destination, database);
        dbOpenHelper.close();
        Toast.makeText(context, "일정 저장됨", Toast.LENGTH_LONG).show();
    }
    private  void InitializeLayout() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        NextButton = view.findViewById(R.id.nextBtn);
        PreviousButton = view.findViewById(R.id.previousBtn);
        CurrentDate = view.findViewById(R.id.current_Date);
        gridView = view.findViewById(R.id.gridview);
    }
    private void SetUpCalendar(){
        String currentDate = dateFormat.format(calendar.getTime());
        CurrentDate.setText(currentDate);
        dates.clear();
        Calendar monthCalendear = (Calendar) calendar.clone();
        monthCalendear.set(Calendar.DAY_OF_MONTH,1);
        int FirstDayofMonth = monthCalendear.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendear.add(Calendar.DAY_OF_MONTH, -FirstDayofMonth);
        CollectEventsPerMonth(monthFormat.format(calendar.getTime()),yearFormat.format(calendar.getTime()));
        while(dates.size() < MAX_CALENDAR_DAYS){
            dates.add(monthCalendear.getTime());
            monthCalendear.add(Calendar.DAY_OF_MONTH, 1);
        }
        myGridAdapter = new MyGridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(myGridAdapter);
    }
    private void CollectEventsPerMonth(String Month, String year){
        eventsList.clear();
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getReadableDatabase();
        Cursor cursor = dbOpenHelper.ReadEventsperMonth(Month, year, database);
        while(cursor.moveToNext()){

            String event = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.EVENT));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.TIME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.DATE));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.MONTH));
            String YEAR = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.YEAR));
            String start = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.START));
            String destination = cursor.getString(cursor.getColumnIndexOrThrow(DBStructure.DESTINATION));
            Events events = new Events(event, time, date, month, YEAR, start, destination);
            eventsList.add(events);

        }
        cursor.close();
        dbOpenHelper.close();
    }

}
