package com.example.timetable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

//打开主界面的时候，显示课程表
public class MainActivity extends AppCompatActivity {

    int currentCoursesNumber=0;
    int maxCoursesNumber=0;

    //SQLite Helper类
    private DatabaseHelper databaseHelper=new DatabaseHelper(this,"database.db",null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //加载工具条
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //从数据库读取数据
        loadData();
    }

    private void loadData(){
        ArrayList<Course> courseArrayList=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=databaseHelper.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from courses",null);
        if(cursor.moveToFirst()){
            do{
                courseArrayList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("course_name")),
                        cursor.getString(cursor.getColumnIndex("teacher_name")),
                        cursor.getString(cursor.getColumnIndex("class_room")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("class_start")),
                        cursor.getInt(cursor.getColumnIndex("class_end"))
                ));
            }while(cursor.moveToNext());
        }
        cursor.close();

        //使用从数据库读出来的信息加载课程表
        for(Course course:courseArrayList){
            createLeftView(course);
            createItemCourseView(course);
        }
    }

    //创建“第几节数”视图
    private void createLeftView(Course course){
        int endNumber=course.getClassEnd();
        if(endNumber>maxCoursesNumber){
            for(int i=0;i<endNumber-maxCoursesNumber;i++){   //创建新视图的计数方法
                View view = LayoutInflater.from(this).inflate(R.layout.left_view,null);  //inflate方法返回一个View对象
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(110,180);
                view.setLayoutParams(params);

                TextView text=view.findViewById(R.id.class_number_text);
                text.setText(String.valueOf(++currentCoursesNumber));

                LinearLayout leftViewLayout=findViewById(R.id.left_view_layout);
                leftViewLayout.addView(view);
            }
            maxCoursesNumber=endNumber;
        }
    }

    //创建单个课程视图
    private void createItemCourseView(final Course course){
        int getDay=course.getDay();
        if((getDay<1||getDay>7)||course.getClassStart()>course.getClassEnd()){
            Toast.makeText(this,"星期几没写对,或课程结束时间比开始时间还早~~",Toast.LENGTH_LONG).show();
        }else{
            int dayid=0;
            switch(getDay){
                case 1:dayid=R.id.monday;break;  //=100179 是个ID值
                case 2:dayid=R.id.tuesday;break;
                case 3:dayid=R.id.wednesday;break;
                case 4:dayid=R.id.thursday;break;
                case 5:dayid=R.id.friday;break;
                case 6:dayid=R.id.saturday;break;
                case 7:dayid=R.id.sunday;break;
            }
            RelativeLayout day=findViewById(dayid);  //星期一列

            int height=180;
            final View v=LayoutInflater.from(this).inflate(R.layout.course_card,null);
            v.setY(height*(course.getClassStart()-1));
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(course.getClassEnd()-course.getClassStart()+1)*height - 8);
            v.setLayoutParams(params);
            TextView text=v.findViewById(R.id.text_view);
            text.setText(course.getCourseName());
            day.addView(v);

            //长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    v.setVisibility(View.GONE); //先隐藏
                    day.removeView(v);  //再移除课程视图
                    //再在数据库中删除
                    SQLiteDatabase sqLiteDatabase=databaseHelper.getWritableDatabase();
                    sqLiteDatabase.execSQL("delete from courses where course_name=?",new String[]{course.getCourseName()});
                    return true;
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){ //导入菜单
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {  //选择菜单的item，跳转到其他页面
        switch(item.getItemId()){
            case R.id.addCourse:
                Intent intent=new Intent(MainActivity.this,AddCourseActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.aboutme:
                Intent intent1=new Intent(MainActivity.this,AboutMeActivity.class);
                startActivity(intent1);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode== Activity.RESULT_OK&&data!=null){
            Course course=(Course)data.getSerializableExtra("Course");
            //创建课程表左边视图(节数)
            createLeftView(course);
            //创建课程表视图
            createItemCourseView(course);
            //存储数据到数据库
            saveData(course);
        }
    }

    //存储数据到数据库
    private void saveData(Course course){
        SQLiteDatabase sqLiteDatabase=databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into courses(course_name,teacher_name,class_room,day,class_start,class_end)"+"values(?,?,?,?,?,?)",new String[]{course.getCourseName(),
                course.getTeacher(),
                course.getClassRoom(),
                course.getDay()+"",
                course.getClassStart()+"",
                course.getClassEnd()+""});    //加上“”是为了让int类型转换成String类型
        Log.d("MainActivity","存储数据到数据库");
    }

}