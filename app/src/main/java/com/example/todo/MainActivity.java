package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todo.model.ToDo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static class ViewHolder1 {
        TextView text;
    }

    public class ItemsListAdapter1 extends BaseAdapter {

        private Context context;
        private List<ToDo> list;

        ItemsListAdapter1(Context c, List<ToDo> l) {
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            MainActivity.ViewHolder1 viewHolder = new MainActivity.ViewHolder1();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_list, null);

                viewHolder.text = (TextView) rowView.findViewById(R.id.task_title);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (MainActivity.ViewHolder1) rowView.getTag();
            }

            final String itemStr = list.get(position).getName();
            ToDo tod = db.findData(itemStr);
            List<ToDo> subList = db.findSubs(tod.getId());
            viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            if(subList != null) {
                for (ToDo sub : subList) {
                    Log.i("done:","is :"+sub.getIsdone());
                    if (!sub.getIsdone())
                        viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
            else
                viewHolder.text.setPaintFlags(viewHolder.text.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

            viewHolder.text.setText(itemStr);

            return rowView;
        }
    }

    public MyDB db;
    public ImageView logo;
    public ListView todoList;
    private ArrayAdapter mAdapter;
    public Integer Request_code_info = 1;
    public List<ToDo> allTodos;
    public List<ToDo> subs;
    public Button add;
    public List<ToDo> taskList;
    public ItemsListAdapter1 myItemsListAdapter;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoList = findViewById(R.id.list_todo);
        add = findViewById(R.id.btnAdd);
        db = new MyDB(this);
        allTodos = new ArrayList<>();
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("result ", "name: " + 1);
                TextView t = (TextView) view.findViewById(R.id.task_title);
                String tasName = t.getText().toString();
                Toast.makeText(MainActivity.this, "clicked item is " + taskList.get(i), Toast.LENGTH_SHORT).show();
                Intent todoInfo = new Intent(getApplicationContext(), ToDoInfo.class);
                todoInfo.putExtra("name", tasName);
                Log.i("result ", "name: " + tasName);
                startActivityForResult(todoInfo, Request_code_info);
            }
        });
        updateUI();
    }

    public void createTask(View view) {
        Intent create = new Intent(getApplicationContext(), CreateTask.class);
        startActivity(create);
    }

    public void updateUI() {
        taskList = new ArrayList<>();
        taskList = db.findAll();
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        myItemsListAdapter = new MainActivity.ItemsListAdapter1(this, taskList);
        todoList.setAdapter(myItemsListAdapter);

    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        View pParent = (View) parent.getParent();
        TextView t = (TextView) pParent.findViewById(R.id.task_title);
        String tasName = t.getText().toString();
        db.delete(tasName);
        updateUI();
    }

    public void getInfo(View view) {
        TextView t = (TextView) view.findViewById(R.id.task_title);
        String tasName = t.getText().toString();
        Intent todoInfo = new Intent(getApplicationContext(), ToDoInfo.class);
        todoInfo.putExtra("name", tasName);
        Log.i("result ", "name: " + tasName);
        startActivityForResult(todoInfo, Request_code_info);
        updateUI();

    }

}