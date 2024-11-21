package com.example.quanlysinhvien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnQuanLySinhVien, btnQuanLyLop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Đảm bảo gọi setContentView trước khi tìm view

        btnQuanLySinhVien = findViewById(R.id.btnQuanLySinhVien);
        btnQuanLyLop = findViewById(R.id.btnQuanLyLop);

        btnQuanLySinhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến trang Quản lý Sinh viên
                Intent intent = new Intent(MainActivity.this, QuanLySinhVien.class);
                startActivity(intent);
            }
        });

        btnQuanLyLop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Chuyển đến trang Quản lý Lớp
                Intent intent = new Intent(MainActivity.this, QuanLyLopHoc.class);
                startActivity(intent);
            }
        });
    }
}
