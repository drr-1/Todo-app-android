package com.example.todo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.todo.model.ToDo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ToDoInfo extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    static class ViewHolder {
        CheckBox checkBox;
        TextView text;
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<ToDo> list;

        ItemsListAdapter(Context c, List<ToDo> l) {
            context = c;
            list = l;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public boolean isChecked(int position) {
            return list.get(position).getIsdone();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            ViewHolder viewHolder = new ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_sub_list, null);

                viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.check);
                viewHolder.text = (TextView) rowView.findViewById(R.id.sub_task_title);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.checkBox.setChecked(list.get(position).getIsdone());

            final String itemStr = list.get(position).getName();
            viewHolder.text.setText(itemStr);

            viewHolder.checkBox.setTag(position);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean newState = !list.get(position).getIsdone();
                    list.get(position).setIsdone(newState);
                    ToDo t= db.findData(itemStr);
                    t.setIsdone(newState);
                    if(db.update(itemStr,t)) {
                        Toast.makeText(getApplicationContext(),
                                itemStr + " checked: " + newState,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

            viewHolder.checkBox.setChecked(isChecked(position));

            return rowView;
        }
    }

    public MyDB db;
    public Spinner type;
    public TextView taskName;
    public TextView dateTime;
    public TextView alarm;
    public Button addSub;
    public Button addAlarm;
    public ListView subList;
    public List<ToDo> taskList;
    private ArrayAdapter mAdapter;
    private ItemsListAdapter myItemsListAdapter;
    public String sname;
    public ToDo toDo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        taskName = findViewById(R.id.name);
//        type = findViewById(R.id.spinner1);
        addAlarm = findViewById(R.id.btnAlarm);
        dateTime = findViewById(R.id.dateTime);
        alarm = findViewById(R.id.alarmText);
        addSub = findViewById(R.id.addS);
        subList = findViewById(R.id.sub_task);
        db = new MyDB(this);
        Intent main = getIntent();
        final String name = main.getStringExtra("name");

        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        toDo = db.findData(name);

        taskName.setText(toDo.getName());
        dateTime.setText(toDo.getDescription());

        updateSubs();
//        initItems();

        addSub.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                final EditText taskEditText= new EditText(ToDoInfo.this);
                AlertDialog dialog = new AlertDialog.Builder(ToDoInfo.this)

                        .setTitle("What do you want to do next?")
                        .setMessage("")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                ToDo subtodo = new ToDo();
                                subtodo.setName(task);
                                subtodo.setParentId(toDo.getId());
                                Long id = db.insertData(subtodo);
//                                Log.d(TAG, "Task to add: " + task);
                                Log.i("insert", "id: " + id);
                                updateSubs();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);
        startAlarm(c);
    }
    private void updateTimeText(Calendar c) {
        String timeText = "";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        alarm.setText(timeText);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("name",taskName.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void updateSubs() {
        taskList = new ArrayList<>();
        taskList = db.findSubs(toDo.getId());
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        myItemsListAdapter = new ItemsListAdapter(this, taskList);
        subList.setAdapter(myItemsListAdapter);
    }

    public void deleteSubTask(View view) {
        View parent = (View) view.getParent();
        View pParent = (View) parent.getParent();
        TextView t = (TextView) pParent.findViewById(R.id.sub_task_title);
        String tasName = t.getText().toString();
        db.delete(tasName);
        updateSubs();
    }

}
