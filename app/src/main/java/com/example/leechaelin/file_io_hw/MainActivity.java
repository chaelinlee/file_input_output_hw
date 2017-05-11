package com.example.leechaelin.file_io_hw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout l1,l2;
    TextView t;
    Button btnsave,btncancel;
    ListView listview;
    EditText e;
    DatePicker dp;
    int count=0;
    ArrayList<titlename> name = new ArrayList<titlename>();
    ArrayAdapter<titlename> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Checkpermission();
        setContentView(R.layout.activity_main);
        e=(EditText)findViewById(R.id.edittext);
        t=(TextView)findViewById(R.id.tvCount);
        l1=(LinearLayout)findViewById(R.id.linear1);
        l2=(LinearLayout)findViewById(R.id.linear2);
        btnsave=(Button)findViewById(R.id.btnsave);
        btncancel=(Button)findViewById(R.id.btncancel);
        dp = (DatePicker)findViewById(R.id.DatePicker);
        listview =(ListView)findViewById(R.id.listview);
        adapter=  new ArrayAdapter<titlename>(this,android.R.layout.simple_list_item_1,name);
        listview.setAdapter(adapter);

        writing_diary();
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                String path = getExternalPath();
                File file = new File(path+"mydairy");
                file.delete();
                AlertDialog.Builder dlg= new AlertDialog.Builder(MainActivity.this);
                dlg.setMessage("정말로 삭제하시겠습니까 ")
                        .setNegativeButton("취소",null)
                        .setPositiveButton("확인 ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                name.remove(position);
                                count-=1;
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"삭제하였습니다. ",Toast.LENGTH_SHORT).show();
                                change_text();


                            }
                        }).show();
                return true;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try{
                    String path = getExternalPath();
                    BufferedReader br = new BufferedReader(new FileReader(path+name.get(position).getTitlename()));

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void onClick(View v){
        if(v.getId()==R.id.btn1){
            l1.setVisibility(View.INVISIBLE);
            l2.setVisibility(View.VISIBLE);
            listview.setVisibility(View.INVISIBLE);
        }
    }

    public void writing_diary(){
        final String filename = dp.getYear()+"-"+dp.getMonth()+"-"+dp.getDayOfMonth();
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String path = getExternalPath();

                    BufferedWriter bw = new BufferedWriter(new FileWriter(path+"mydiary/"+filename,true));
                    bw.write(e.getText().toString());
                    bw.close();
                    Toast.makeText(getApplicationContext(),"저장완료 ",Toast.LENGTH_SHORT).show();
                    count+=1;
                }catch (IOException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage()+":"+getFilesDir(),Toast.LENGTH_SHORT).show();
                }
                name.add(new titlename(filename.toString()+".memo"));
                adapter.notifyDataSetChanged();
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
                change_text();

            }
        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l1.setVisibility(View.VISIBLE);
                l2.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.VISIBLE);
            }
        });

    }

    public void change_text(){
        t.setText("등록된 메모 개수:"+String.valueOf(count));
    }
    public String getExternalPath(){
        String sdPath="";
        String ext = Environment.getExternalStorageState();
        if(ext.equals(Environment.MEDIA_MOUNTED)){
            sdPath=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/";
        }else
            sdPath=getFilesDir()+"";
        Toast.makeText(getApplicationContext(),sdPath,Toast.LENGTH_SHORT).show();
        return sdPath;
    }


    public void Checkpermission(){
        int permissioninfo = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permissioninfo== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"SDCard 쓰기 권한 있음 ",Toast.LENGTH_SHORT).show();
        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(),"권한의 필요성 설명 ",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }else{
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String str = null;
        if(requestCode==100){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                str = "SD Card 쓰기 권한 승인 ";
            else
                str ="SD Card 쓰기 권한 거부";
            Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
