package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.myapplication.Models.DBHelper;
import com.example.myapplication.Models.Student;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText surnameEditText;
    EditText ageEditTextNumberDecimal;
    EditText courseEditTextNumber;
    Button saveBtn;
    Button filterBtn;
    Button unselectBtn;
    Button removeBtn;
    Button reportBtn;
    ListView listView;
    ArrayAdapter<Student> listViewAdapter;
    ArrayList<Student> students;
    Student edit_student = null;
    boolean filter_flag = false;

    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        nameEditText = (EditText)findViewById(R.id.nameEditText);
        surnameEditText = (EditText)findViewById(R.id.surnameEditText);
        ageEditTextNumberDecimal = (EditText)findViewById(R.id.ageEditTextNumberDecimal);
        courseEditTextNumber = (EditText)findViewById(R.id.courseEditTextNumber);

        saveBtn = (Button)findViewById(R.id.saveBtn);
        filterBtn = (Button)findViewById(R.id.filterBtn);
        unselectBtn = (Button)findViewById(R.id.unselectBtn);
        removeBtn = (Button)findViewById(R.id.removeBtn);
        reportBtn = (Button)findViewById(R.id.reportBtn);

        listView = (ListView)findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        students = new ArrayList<Student>();
        students = dbHelper.load();
        listViewAdapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_single_choice, students);
        listView.setAdapter(listViewAdapter);

//        Toast.makeText(MainActivity.this, "123", Toast.LENGTH_LONG).show();

//        listViewAdapter.add(new Student("Ivan", "Ivanov", 15, 3));
//        listViewAdapter.add(new Student("Sergei", "Petrov", 17, 4));
//        listViewAdapter.add(new Student("Alexey", "Aleksandrov", 25, 1));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Student selectedStudent = (Student)adapterView.getItemAtPosition(i);
                if (selectedStudent != null) {
                    nameEditText.setText(selectedStudent.Name);
                    surnameEditText.setText(selectedStudent.Surname);
                    ageEditTextNumberDecimal.setText(String.valueOf(selectedStudent.Age));
                    courseEditTextNumber.setText(String.valueOf(selectedStudent.Course));
                    edit_student = selectedStudent;
                }
            }
        });
    }
    public void saveBtnClick(View v){
        if (nameEditText.getText().length() > 0 &&
                surnameEditText.getText().length() > 0 &&
                ageEditTextNumberDecimal.getText().length() > 0 &&
                courseEditTextNumber.getText().length() > 0) {

            String name = String.valueOf(nameEditText.getText());
            String surname = String.valueOf(surnameEditText.getText());
            int age = Integer.parseInt(String.valueOf(ageEditTextNumberDecimal.getText()));
            int course = Integer.parseInt(String.valueOf(courseEditTextNumber.getText()));

            if (edit_student != null) {
                Student student = new Student(name, surname, age, course);
                listViewAdapter.remove(edit_student);
                listViewAdapter.add(student);

            }else{
                listViewAdapter.add(new Student(name, surname, age, course));

            }
            nameEditText.setText("");
            surnameEditText.setText("");
            ageEditTextNumberDecimal.setText("");
            courseEditTextNumber.setText("");
            listView.clearChoices();
            listViewAdapter.notifyDataSetChanged();
            edit_student = null;
            dbHelper.save(students);
        }
    }
    public void filterBtnClick(View v){
        if (!filter_flag) {
            if (courseEditTextNumber.getText().length() > 0) {
                int course = Integer.parseInt(String.valueOf(courseEditTextNumber.getText()));
                ArrayList<Student> new_students = new ArrayList<Student>();
                for (Student student : students) {
                    if (student.Course == course) {
                        new_students.add(student);
                    }
                }
                listViewAdapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_single_choice, new_students);
                listView.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
                filter_flag = true;
            }
        }else{
            listViewAdapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_single_choice, students);
            listView.setAdapter(listViewAdapter);
            listViewAdapter.notifyDataSetChanged();
            filter_flag = false;
        }
        saveBtn.setEnabled(!filter_flag);
        unselectBtn.setEnabled(!filter_flag);
        removeBtn.setEnabled(!filter_flag);

        nameEditText.setEnabled(!filter_flag);
        surnameEditText.setEnabled(!filter_flag);
        ageEditTextNumberDecimal.setEnabled(!filter_flag);
        courseEditTextNumber.setEnabled(!filter_flag);
    }
    public void unselectBtnClick(View v){
        listView.clearChoices();
        listViewAdapter.notifyDataSetChanged();
        if (edit_student != null){
            nameEditText.setText("");
            surnameEditText.setText("");
            ageEditTextNumberDecimal.setText("");
            courseEditTextNumber.setText("");
            edit_student = null;
        }
    }
    public void removeBtnClick(View v){
        if (edit_student != null) {
            listViewAdapter.remove(edit_student);
            listView.clearChoices();
            listViewAdapter.notifyDataSetChanged();
            if (edit_student != null){
                nameEditText.setText("");
                surnameEditText.setText("");
                ageEditTextNumberDecimal.setText("");
                courseEditTextNumber.setText("");
                edit_student = null;
            }
            dbHelper.save(students);
        }
    }
    public void reportBtnClick(View v){
        Intent myIntent = new Intent(MainActivity.this, ReportActivity.class);
        ArrayList<String> new_students = new ArrayList<String>();

        for(int i = 0; i < listViewAdapter.getCount(); i++){
            new_students.add(listViewAdapter.getItem(i).toString());
        }

        myIntent.putExtra("students", new_students);
        this.startActivity(myIntent);
    }
}
