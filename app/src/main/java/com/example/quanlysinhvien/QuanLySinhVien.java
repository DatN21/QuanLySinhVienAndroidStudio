package com.example.quanlysinhvien;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuanLySinhVien extends AppCompatActivity {

    // Các khai báo View
    EditText maSV, tenSV, namSinh, sdt, diaChi;
    Button them, sua, xoa;
    ListView lvSinhVien;
    Spinner spinnerMaLop;
    ArrayList<String> sinhVienList;
    ArrayAdapter<String> sinhVienAdapter;
    SQLiteDatabase myDatabase;
    ArrayList<String> maLopList;
    ArrayAdapter<String> maLopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_sinh_vien);

        // Liên kết các View
        maSV = findViewById(R.id.edtMaSV);
        tenSV = findViewById(R.id.edtTenSinhVien);
        namSinh = findViewById(R.id.edtNamSinh);
        sdt = findViewById(R.id.edtSDT);
        diaChi = findViewById(R.id.edtDiaChi);

        them = findViewById(R.id.btnThem);
        sua = findViewById(R.id.btnSua);
        xoa = findViewById(R.id.btnXoa);

        lvSinhVien = findViewById(R.id.lvSinhVien);
        spinnerMaLop = findViewById(R.id.spinnerMaLop);  // Thêm Spinner cho mã lớp

        sinhVienList = new ArrayList<>();
        sinhVienAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sinhVienList);
        lvSinhVien.setAdapter(sinhVienAdapter);

        maLopList = new ArrayList<>();
        maLopAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, maLopList);
        maLopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaLop.setAdapter(maLopAdapter);

        // Mở hoặc tạo cơ sở dữ liệu
        myDatabase = openOrCreateDatabase("qlsv.db", MODE_PRIVATE, null);

        // Kiểm tra bảng sinh viên đã tồn tại chưa, nếu chưa thì tạo bảng
        String createTableSQL = "CREATE TABLE IF NOT EXISTS quanLySinhVien(maSV TEXT PRIMARY KEY, tenSV TEXT, namSinh TEXT, sdt TEXT, diaChi TEXT, maLop TEXT, FOREIGN KEY(maLop) REFERENCES quanLyLop(maLop))";
        myDatabase.execSQL(createTableSQL);

        // Hiển thị danh sách sinh viên và mã lớp khi mở ứng dụng
        showSinhVienList();
        loadMaLopList();

        // Thêm mới sinh viên
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = maSV.getText().toString();
                String ten = tenSV.getText().toString();
                String nam = namSinh.getText().toString();
                String phone = sdt.getText().toString();
                String diachi = diaChi.getText().toString();
                String maLop = spinnerMaLop.getSelectedItem().toString();

                if (ma.isEmpty() || ten.isEmpty() || nam.isEmpty() || phone.isEmpty() || diachi.isEmpty()) {
                    Toast.makeText(QuanLySinhVien.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String insertSQL = "INSERT INTO quanLySinhVien(maSV, tenSV, namSinh, sdt, diaChi, maLop) VALUES(?, ?, ?, ?, ?, ?)";
                    myDatabase.execSQL(insertSQL, new Object[]{ma, ten, nam, phone, diachi, maLop});
                    Toast.makeText(QuanLySinhVien.this, "Thêm sinh viên thành công!", Toast.LENGTH_SHORT).show();
                    showSinhVienList();
                    clearInputFields();  // Xoá trắng các trường nhập sau khi thêm
                } catch (Exception e) {
                    Toast.makeText(QuanLySinhVien.this, "Lỗi khi thêm sinh viên!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Sửa thông tin sinh viên
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = maSV.getText().toString();
                String ten = tenSV.getText().toString();
                String nam = namSinh.getText().toString();
                String phone = sdt.getText().toString();
                String diachi = diaChi.getText().toString();
                String maLop = spinnerMaLop.getSelectedItem().toString();

                if (ma.isEmpty() || ten.isEmpty() || nam.isEmpty() || phone.isEmpty() || diachi.isEmpty()) {
                    Toast.makeText(QuanLySinhVien.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String updateSQL = "UPDATE quanLySinhVien SET tenSV = ?, namSinh = ?, sdt = ?, diaChi = ?, maLop = ? WHERE maSV = ?";
                    myDatabase.execSQL(updateSQL, new Object[]{ten, nam, phone, diachi, maLop, ma});
                    Toast.makeText(QuanLySinhVien.this, "Cập nhật sinh viên thành công!", Toast.LENGTH_SHORT).show();
                    showSinhVienList();
                    clearInputFields();  // Xoá trắng các trường nhập sau khi sửa
                } catch (Exception e) {
                    Toast.makeText(QuanLySinhVien.this, "Lỗi khi cập nhật sinh viên!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xóa sinh viên
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ma = maSV.getText().toString();

                if (ma.isEmpty()) {
                    Toast.makeText(QuanLySinhVien.this, "Vui lòng nhập mã sinh viên cần xóa!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    String deleteSQL = "DELETE FROM quanLySinhVien WHERE maSV = ?";
                    myDatabase.execSQL(deleteSQL, new Object[]{ma});
                    Toast.makeText(QuanLySinhVien.this, "Xóa sinh viên thành công!", Toast.LENGTH_SHORT).show();
                    showSinhVienList();
                    clearInputFields();  // Xoá trắng các trường nhập sau khi xóa
                } catch (Exception e) {
                    Toast.makeText(QuanLySinhVien.this, "Lỗi khi xóa sinh viên!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý khi click vào một item trong ListView
        lvSinhVien.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = sinhVienList.get(position);
            String[] details = selectedItem.split(" - ");
            String maSVSelected = details[0];

            // Tải thông tin sinh viên lên các trường nhập
            Cursor cursor = myDatabase.rawQuery("SELECT * FROM quanLySinhVien WHERE maSV = ?", new String[]{maSVSelected});
            if (cursor.moveToFirst()) {
                maSV.setText(cursor.getString(0));
                tenSV.setText(cursor.getString(1));
                namSinh.setText(cursor.getString(2));
                sdt.setText(cursor.getString(3));
                diaChi.setText(cursor.getString(4));
                // Set the correct spinner item
                String maLopSelected = cursor.getString(5);
                int positionSpinner = maLopList.indexOf(maLopSelected);
                spinnerMaLop.setSelection(positionSpinner);
            }
            cursor.close();
        });
    }

    // Hiển thị danh sách sinh viên từ cơ sở dữ liệu
    private void showSinhVienList() {
        sinhVienList.clear();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM quanLySinhVien", null);

        if (cursor.moveToFirst()) {
            do {
                String maSV = cursor.getString(0);
                String tenSV = cursor.getString(1);
                sinhVienList.add(maSV + " - " + tenSV);
            } while (cursor.moveToNext());
        }

        cursor.close();
        sinhVienAdapter.notifyDataSetChanged();
    }

    // Hiển thị danh sách mã lớp từ bảng quanLyLop
    private void loadMaLopList() {
        maLopList.clear();
        Cursor cursor = myDatabase.rawQuery("SELECT maLop FROM quanLyLop", null);

        if (cursor.moveToFirst()) {
            do {
                String maLop = cursor.getString(0);
                maLopList.add(maLop);
            } while (cursor.moveToNext());
        }

        cursor.close();
        maLopAdapter.notifyDataSetChanged();
    }

    // Hàm xoá trắng các trường nhập
    private void clearInputFields() {
        maSV.setText("");
        tenSV.setText("");
        namSinh.setText("");
        sdt.setText("");
        diaChi.setText("");
        spinnerMaLop.setSelection(0);  // Đặt lại spinner về lựa chọn đầu tiên
    }
}
