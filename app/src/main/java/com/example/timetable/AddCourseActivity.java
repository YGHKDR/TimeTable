package com.example.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddCourseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setFinishOnTouchOutside(false);

        final EditText inputCourseName=(EditText) findViewById(R.id.course_name);
        final EditText inputTeacher=(EditText)findViewById(R.id.teacher_name);
        final EditText inputClassRoom=(EditText)findViewById(R.id.classroom);
        final EditText inputDay=(EditText)findViewById(R.id.week) ;
        final EditText inputStart=(EditText)findViewById(R.id.class_begin);
        final EditText inputEnd=(EditText)findViewById(R.id.class_end);

        Button btn=(Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String courseName = inputCourseName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                String classRoom = inputClassRoom.getText().toString();
                String day = inputDay.getText().toString();
                String start = inputStart.getText().toString();
                String end = inputEnd.getText().toString();

                if (courseName.equals("") || day.equals("") || start.equals("") || end.equals("")) {
                    Toast.makeText(AddCourseActivity.this, "基本课程信息未填写", Toast.LENGTH_SHORT).show();
                } else {
                    Course course=new Course(courseName,teacher,classRoom,Integer.valueOf(day),Integer.valueOf(start),Integer.valueOf(end));
                    Intent intent=new Intent(AddCourseActivity.this,MainActivity.class);
                    intent.putExtra("Course",course);
                    setResult(Activity.RESULT_OK,intent);
                    Log.d("addCourseActivity","信息已传回MainActivity");
                    finish();
                }
            }
        });
    }
}
