package com.example.quanlysinhvien;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuanLyLopHoc extends AppCompatActivity {
    EditText maLop, tenLop, nienKhoa;
    Button them, sua, xoa, query;
    ListView lv;
    ArrayList<String> myList;
    ArrayAdapter<String> myAdapter;
    SQLiteDatabase myDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_lop_hoc);

        // Ánh xạ
        maLop = findViewById(R.id.edtMaLop);
        tenLop = findViewById(R.id.edtTenLop);
        nienKhoa = findViewById(R.id.edtNienKhoa);
        them = findViewById(R.id.them);
        sua = findViewById(R.id.sua);
        xoa = findViewById(R.id.xoa);
        query = findViewById(R.id.query);
        lv = findViewById(R.id.lv);

        // Tạo danh sách và adapter cho ListView
        myList = new ArrayList<>();
        myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        lv.setAdapter(myAdapter);

        // Mở hoặc tạo cơ sở dữ liệu
        myDatabase = openOrCreateDatabase("qlsv.db", MODE_PRIVATE, null);
        try {
            String sql = "CREATE TABLE IF NOT EXISTS c(maLop TEXT PRIMARY KEY, tenLop TEXT, nienKhoa TEXT)";
            myDatabase.execSQL(sql);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi tạo bảng", Toast.LENGTH_SHORT).show();
        }

        // Hiển thị dữ liệu khi ứng dụng khởi động
        loadData();

        // Xử lý sự kiện thêm
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ma = maLop.getText().toString();
                String ten = tenLop.getText().toString();
                String nien = nienKhoa.getText().toString();

                if (ma.isEmpty() || ten.isEmpty() || nien.isEmpty()) {
                    Toast.makeText(QuanLyLopHoc.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String sql = "INSERT INTO quanLyLop VALUES (?, ?, ?)";
                    myDatabase.execSQL(sql, new Object[]{ma, ten, nien});
                    Toast.makeText(QuanLyLopHoc.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    clearFields();
                    loadData(); // Cập nhật danh sách
                } catch (Exception e) {
                    Toast.makeText(QuanLyLopHoc.this, "Lớp đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện sửa
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ma = maLop.getText().toString();
                String ten = tenLop.getText().toString();
                String nien = nienKhoa.getText().toString();

                if (ma.isEmpty() || ten.isEmpty() || nien.isEmpty()) {
                    Toast.makeText(QuanLyLopHoc.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String sql = "UPDATE quanLyLop SET tenLop = ?, nienKhoa = ? WHERE maLop = ?";
                    myDatabase.execSQL(sql, new Object[]{ten, nien, ma});
                    Toast.makeText(QuanLyLopHoc.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    clearFields();
                    loadData(); // Cập nhật danh sách
                } catch (Exception e) {
                    Toast.makeText(QuanLyLopHoc.this, "Không thể cập nhật lớp!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện xóa
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ma = maLop.getText().toString();

                if (ma.isEmpty()) {
                    Toast.makeText(QuanLyLopHoc.this, "Vui lòng nhập mã lớp cần xóa!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String sql = "DELETE FROM quanLyLop WHERE maLop = ?";
                    myDatabase.execSQL(sql, new Object[]{ma});
                    Toast.makeText(QuanLyLopHoc.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    clearFields();
                    loadData(); // Cập nhật danh sách
                } catch (Exception e) {
                    Toast.makeText(QuanLyLopHoc.this, "Không thể xóa lớp!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sự kiện nhấn vào item trong ListView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Lấy dữ liệu dòng được chọn
                String selectedItem = myList.get(position);

                // Tách dữ liệu từ chuỗi "ma - ten - nienKhoa"
                String[] parts = selectedItem.split(" - ");
                if (parts.length == 3) {
                    maLop.setText(parts[0]);
                    tenLop.setText(parts[1]);
                    nienKhoa.setText(parts[2]);
                }
            }
        });
    }

    // Hàm xóa trắng các trường nhập liệu
    private void clearFields() {
        maLop.setText("");
        tenLop.setText("");
        nienKhoa.setText("");
    }

    // Hàm tải dữ liệu từ cơ sở dữ liệu và hiển thị lên ListView
    private void loadData() {
        myList.clear(); // Xóa danh sách cũ
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM quanLyLop", null);
        if (cursor.moveToFirst()) {
            do {
                String ma = cursor.getString(0);
                String ten = cursor.getString(1);
                String nien = cursor.getString(2);
                myList.add(ma + " - " + ten + " - " + nien); // Thêm dữ liệu vào danh sách
            } while (cursor.moveToNext());
        }
        cursor.close();
        myAdapter.notifyDataSetChanged(); // Cập nhật ListView
    }
}
